package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLogStatAnalysisJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.DimOpLogType;
import com.ailk.biapp.ci.model.CiLogStatAnalysisModel;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiLogStatAnalysisJDaoImpl extends JdbcBaseDao implements ICiLogStatAnalysisJDao {
    private Logger log = Logger.getLogger(CiLogStatAnalysisJDaoImpl.class);
    private static final String CI_LOG_OVERVIEW = "CI_LOG_OVERVIEW";
    private static final String CI_LOG_USER_DETAIL = "CI_LOG_USER_DETAIL";
    private static final String DIM_OP_LOG_TYPE = "DIM_OP_LOG_TYPE";

    public CiLogStatAnalysisJDaoImpl() {
    }

    public List<CiLogStatAnalysisModel> findAnaOpTypeInThePeriod(CiLogStatAnalysisModel model) throws Exception {
        DataBaseAdapter adpter = new DataBaseAdapter(Configure.getInstance().getProperty("CI_DBTYPE"));
        HashMap paramMap = new HashMap();
        String datePeriodSql = this.getDatePeriodSql(model, paramMap);
        String getAllOperTypeIdSql = "select dim_table_1.op_type_id,dim_table_1.op_type_name, " + adpter.getNvl("SUM(table_1.OP_TIMES)", "0") + " OP_TIMES from (select * from " + "DIM_OP_LOG_TYPE";
        if(StringUtil.isNotEmpty(model.getParentId())) {
            getAllOperTypeIdSql = getAllOperTypeIdSql + " where PARENT_ID=:parentId ";
            paramMap.put("parentId", model.getParentId());
        } else {
            getAllOperTypeIdSql = getAllOperTypeIdSql + " where PARENT_ID = \'-1\'";
        }

        getAllOperTypeIdSql = getAllOperTypeIdSql + "  ) dim_table_1 left join (select * from CI_LOG_OVERVIEW where 1=1 " + datePeriodSql;
        getAllOperTypeIdSql = getAllOperTypeIdSql + ") table_1 on table_1.op_type_id=dim_table_1.op_type_id  where dim_table_1.op_type_id is not null  group by dim_table_1.op_type_id,dim_table_1.op_type_name, dim_table_1.sort_num order by dim_table_1.SORT_NUM asc";
        this.log.debug(" -findAnaOpTypeInThePeriod sql:" + getAllOperTypeIdSql);
        List opTypeIdList = this.getSimpleJdbcTemplate().query(getAllOperTypeIdSql, ParameterizedBeanPropertyRowMapper.newInstance(CiLogStatAnalysisModel.class), paramMap);
        return opTypeIdList;
    }

    public List<CiLogStatAnalysisModel> findAllLogViewDataInThePeriod(Pager pager, CiLogStatAnalysisModel model) throws Exception {
        String[] opTypeIds = model.getOpTypeId().split(",");
        int opTypeIdNum = Math.min(opTypeIds.length, 10);
        HashMap paramMap = new HashMap();
        String datePeriodSql = this.getDatePeriodSql(model, paramMap);
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select \'").append(model.getBeginDate()).append("\'||\'~\'||\'").append(model.getEndDate()).append("\' data_Date,SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME");

        for(int queryForIntSql = 0; queryForIntSql < opTypeIdNum; ++queryForIntSql) {
            sqlBuf.append(", sum(case when OP_TYPE_ID=\'").append(opTypeIds[queryForIntSql]).append("\' then op_TIMES else 0 end) op_TIMES_").append(queryForIntSql + 1);
        }

        sqlBuf.append(" from ").append("CI_LOG_OVERVIEW").append(" where 1=1 ").append(datePeriodSql);
        if(StringUtil.isNotEmpty(model.getSecondDeptId()) && StringUtil.isEmpty(model.getThirdDeptId())) {
            sqlBuf.append(" and SECOND_DEPT_ID in ( ").append(model.getSecondDeptId()).append(" ) ");
        } else if(StringUtil.isEmpty(model.getSecondDeptId()) && StringUtil.isNotEmpty(model.getThirdDeptId())) {
            sqlBuf.append(" and THIRD_DEPT_ID in ( ").append(model.getThirdDeptId()).append(" ) ");
        } else if(StringUtil.isNotEmpty(model.getSecondDeptId()) && StringUtil.isNotEmpty(model.getThirdDeptId())) {
            sqlBuf.append(" and ( THIRD_DEPT_ID in ( ").append(model.getThirdDeptId()).append(" ) ").append(" or SECOND_DEPT_ID in ( ").append(model.getSecondDeptId()).append(" )) ");
        }

        sqlBuf.append(" group by SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME order by second_dept_name asc");
        this.log.debug(" -findAllLogViewDataInThePeriod sql:" + sqlBuf.toString());
        StringBuffer var12 = new StringBuffer();
        var12.append("select count(*) from (").append(sqlBuf.toString()).append(") abc");
        int count = this.getSimpleJdbcTemplate().queryForInt(var12.toString(), paramMap);
        pager.setTotalSize((long)count);
        pager.setTotalPage((int)Math.ceil((double)pager.getTotalSize() / (double)pager.getPageSize()));
        pager.setPageSize(1000);
        String pageSql = this.getDataBaseAdapter().getPagedSql(sqlBuf.toString(), pager.getPageNum(), pager.getPageSize());
        this.log.debug(" -findAllLogViewDataInThePeriod pageSql:" + pageSql);
        List resultList = this.getSimpleJdbcTemplate().query(pageSql, ParameterizedBeanPropertyRowMapper.newInstance(CiLogStatAnalysisModel.class), paramMap);
        return resultList;
    }

    public List<Map<String, Object>> findOneDeptOpLogTrend(CiLogStatAnalysisModel model) throws Exception {
        String[] opTypeIds = model.getOpTypeId().split(",");
        int opTypeIdNum = Math.min(opTypeIds.length, 10);
        HashMap paramMap = new HashMap();
        String datePeriodSql = this.getDatePeriodSql(model, paramMap);
        String deptSql = this.getDeptSql(model);
        String sql = "select data_Date" + deptSql;

        for(int resultList = 0; resultList < opTypeIdNum; ++resultList) {
            sql = sql + ",sum(case when OP_TYPE_ID=\'" + opTypeIds[resultList] + "\' then op_TIMES else 0 end) op_TIMES_" + (resultList + 1);
        }

        sql = sql + " from CI_LOG_OVERVIEW where 1=1 " + datePeriodSql;
        if(StringUtil.isNotEmpty(model.getSecondDeptId())) {
            sql = sql + " and SECOND_DEPT_ID=:secondDeptId ";
            paramMap.put("secondDeptId", model.getSecondDeptId());
        }

        if(StringUtil.isNotEmpty(model.getThirdDeptId())) {
            sql = sql + " and THIRD_DEPT_ID=:thirdDeptId ";
            paramMap.put("thirdDeptId", model.getThirdDeptId());
        }

        sql = sql + " group by data_Date" + deptSql;
        sql = sql + " order by data_date asc";
        this.log.debug(" -findLogViewDataInOneDept sql:" + sql);
        List var9 = this.getSimpleJdbcTemplate().queryForList(sql, paramMap);
        return var9;
    }

    private String getDeptSql(CiLogStatAnalysisModel model) {
        String tempSql = "";
        if(StringUtil.isNotEmpty(model.getSecondDeptId())) {
            tempSql = tempSql + ",SECOND_DEPT_ID,SECOND_DEPT_NAME";
        }

        if(StringUtil.isNotEmpty(model.getThirdDeptId())) {
            tempSql = tempSql + ",THIRD_DEPT_ID,THIRD_DEPT_NAME";
        }

        return tempSql;
    }

    public List<Map<String, Object>> findLogUserDetilList(Pager pager, CiLogStatAnalysisModel model) throws Exception {
        String[] opTypeIds = model.getOpTypeId().split(",");
        int opTypeIdNum = Math.min(opTypeIds.length, 10);
        HashMap paramMap = new HashMap();
        String datePeriodSql = this.getDatePeriodSql(model, paramMap);
        String sql = "select data_date,USER_ID,USER_NAME,SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME";

        for(int pageSql = 0; pageSql < opTypeIdNum; ++pageSql) {
            sql = sql + ",sum(case when t1.OP_TYPE_ID=\'" + opTypeIds[pageSql] + "\' or t2.PARENT_ID=\'" + opTypeIds[pageSql] + "\' then op_TIMES else 0 end) op_TIMES_" + (pageSql + 1);
        }

        sql = sql + " from CI_LOG_USER_DETAIL  t1 inner join DIM_OP_LOG_TYPE t2 on t1.op_type_id = t2.OP_TYPE_ID   and (t2.PARENT_ID in(\'" + model.getOpTypeId().replaceAll(",", "\',\'") + "\') OR t2.OP_TYPE_ID in(\'" + model.getOpTypeId().replaceAll(",", "\',\'") + "\'))" + " where 1=1 " + datePeriodSql;
        if(StringUtil.isNotEmpty(model.getSecondDeptId())) {
            sql = sql + " and SECOND_DEPT_ID=:secondDeptId ";
            paramMap.put("secondDeptId", model.getSecondDeptId());
        }

        if(StringUtil.isNotEmpty(model.getThirdDeptId())) {
            sql = sql + " and THIRD_DEPT_ID=:thirdDeptId ";
            paramMap.put("thirdDeptId", model.getThirdDeptId());
        }

        sql = sql + " group by data_date,USER_ID,USER_NAME,SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME order by data_date desc";
        this.log.debug(" -findLogUserDetilList sql:" + sql);
        String var10 = this.getDataBaseAdapter().getPagedSql(sql, pager.getPageNum(), pager.getPageSize());
        this.log.debug(" -findLogUserDetilList pagesql:" + var10);
        List resultList = this.getSimpleJdbcTemplate().queryForList(var10, paramMap);
        return resultList;
    }

    public int findLogUserDetilListCount(CiLogStatAnalysisModel model) throws Exception {
        String[] opTypeIds = model.getOpTypeId().split(",");
        int opTypeIdNum = Math.min(opTypeIds.length, 10);
        HashMap paramMap = new HashMap();
        String datePeriodSql = this.getDatePeriodSql(model, paramMap);
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select data_date,USER_ID,USER_NAME,SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME");

        for(int queryForIntSqlBuf = 0; queryForIntSqlBuf < opTypeIdNum; ++queryForIntSqlBuf) {
            sqlBuf.append(",sum(case when t1.OP_TYPE_ID=\'").append(opTypeIds[queryForIntSqlBuf]).append("\' or t2.PARENT_ID=\'").append(opTypeIds[queryForIntSqlBuf]).append("\' then op_TIMES else 0 end) op_TIMES_").append(queryForIntSqlBuf + 1);
        }

        sqlBuf.append(" from ").append("CI_LOG_USER_DETAIL").append("  t1 inner join ").append("DIM_OP_LOG_TYPE").append(" t2 on t1.op_type_id = t2.OP_TYPE_ID  ").append(" and (t2.PARENT_ID in(\'").append(model.getOpTypeId().replaceAll(",", "\',\'")).append("\') OR t2.OP_TYPE_ID in(\'").append(model.getOpTypeId().replaceAll(",", "\',\'")).append("\'))").append(" where 1=1 ").append(datePeriodSql);
        if(StringUtil.isNotEmpty(model.getSecondDeptId())) {
            sqlBuf.append(" and SECOND_DEPT_ID=:secondDeptId ");
            paramMap.put("secondDeptId", model.getSecondDeptId());
        }

        if(StringUtil.isNotEmpty(model.getThirdDeptId())) {
            sqlBuf.append(" and THIRD_DEPT_ID=:thirdDeptId ");
            paramMap.put("thirdDeptId", model.getThirdDeptId());
        }

        sqlBuf.append(" group by data_date,USER_ID,USER_NAME,SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME").append(" order by data_date desc");
        this.log.debug(" -findLogUserDetilList sql:" + sqlBuf.toString());
        StringBuffer var8 = new StringBuffer();
        var8.append("select count(*) from (").append(sqlBuf.toString()).append(") abc");
        return this.getSimpleJdbcTemplate().queryForInt(var8.toString(), paramMap);
    }

    public List<CiLogStatAnalysisModel> findOneOpLogTypeSpread(CiLogStatAnalysisModel model) throws Exception {
        HashMap paramMap = new HashMap();
        String datePeriodSql = this.getDatePeriodSql(model, paramMap);
        StringBuffer sql = new StringBuffer("SELECT ");
        if("1".equals(model.getThirdDeptId())) {
            sql.append("T1.op_type_id,T1.second_dept_id,T1.second_dept_name,Coalesce(T1.op_times,0) op_times,Coalesce(T2.op_times1,0) opTimes1,T2.parent_id");
            sql.append(" FROM (SELECT");
            sql.append("            t1.op_type_id,");
            sql.append("            t1.second_dept_id,");
            sql.append("            t1.second_dept_name,");
            sql.append("            SUM(Coalesce(t1.op_TIMES,0)) op_times");
            sql.append("        FROM ");
            sql.append("CI_LOG_OVERVIEW t1");
            sql.append("           WHERE  1=1" + datePeriodSql);
            if(StringUtil.isNotEmpty(model.getOpTypeId())) {
                sql.append("            AND op_type_id=:opTypeId");
                paramMap.put("opTypeId", model.getOpTypeId());
            }

            sql.append("            GROUP BY t1.op_type_id,t1.second_dept_id,t1.second_dept_name)T1 ");
            sql.append("            LEFT JOIN");
            sql.append("                ( SELECT ");
            if(StringUtil.isNotEmpty(model.getParentId())) {
                sql.append("SUM( CASE WHEN OP_TYPE_ID=:parentId THEN Coalesce(op_TIMES,0) END) OP_TIMES1,");
                paramMap.put("parentId", model.getParentId());
            }

            sql.append("           SECOND_DEPT_id ,OP_TYPE_ID PARENT_ID FROM CI_LOG_OVERVIEW WHERE  OP_TYPE_ID=:parentId" + datePeriodSql);
            sql.append("            GROUP BY  SECOND_DEPT_id ,OP_TYPE_ID )  T2");
            sql.append("            ON T1.second_dept_id=T2.second_dept_id");
        } else {
            sql.append("T2.op_type_id,T1.third_dept_id,T1.third_dept_name,Coalesce(T2.op_times,0) op_times,Coalesce(T1.op_times1,0) opTimes1,T1.parent_id");
            sql.append(" FROM ");
            sql.append("                ( SELECT ");
            if(StringUtil.isNotEmpty(model.getParentId())) {
                sql.append("SUM( CASE WHEN OP_TYPE_ID=:parentId THEN Coalesce(op_TIMES,0) END) OP_TIMES1,");
                paramMap.put("parentId", model.getParentId());
            }

            sql.append("           third_DEPT_id ,third_dept_name,OP_TYPE_ID PARENT_ID FROM CI_LOG_OVERVIEW WHERE  OP_TYPE_ID=:parentId" + datePeriodSql);
            sql.append(" and second_dept_id=:secondDeptId");
            paramMap.put("secondDeptId", model.getSecondDeptId());
            sql.append("            GROUP BY  third_DEPT_id ,third_dept_name,OP_TYPE_ID )  T1");
            sql.append("            LEFT JOIN");
            sql.append(" (SELECT");
            sql.append("            t1.op_type_id,");
            sql.append("            t1.third_dept_id,");
            sql.append("            SUM(Coalesce(t1.op_TIMES,0)) op_times");
            sql.append("        FROM ");
            sql.append("CI_LOG_OVERVIEW t1");
            sql.append("           WHERE  1=1" + datePeriodSql);
            if(StringUtil.isNotEmpty(model.getOpTypeId())) {
                sql.append("            AND op_type_id=:opTypeId");
                paramMap.put("opTypeId", model.getOpTypeId());
            }

            if(StringUtil.isNotEmpty(model.getSecondDeptId())) {
                sql.append(" and second_dept_id=:secondDeptId");
                paramMap.put("secondDeptId", model.getSecondDeptId());
            }

            sql.append("            GROUP BY t1.op_type_id,t1.third_dept_id,t1.third_dept_name)T2 ");
            sql.append("            ON T1.third_dept_id=T2.third_dept_id");
        }

        this.log.debug(" -findOneOpLogTypeSpread sql:" + sql);
        List resultList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLogStatAnalysisModel.class), paramMap);
        return resultList;
    }

    private String getDatePeriodSql(CiLogStatAnalysisModel model, Map<String, Object> paramMap) {
        StringBuffer datePeriodSqlBuf = new StringBuffer();
        if(StringUtil.isNotEmpty(model.getBeginDate())) {
            datePeriodSqlBuf.append(" and data_date>=:beginDate ");
            paramMap.put("beginDate", model.getBeginDate());
        }

        if(StringUtil.isNotEmpty(model.getEndDate())) {
            datePeriodSqlBuf.append(" and data_date<=:endDate ");
            paramMap.put("endDate", model.getEndDate());
        }

        return datePeriodSqlBuf.toString();
    }

    public List<CiLogStatAnalysisModel> findOpTypeChar(CiLogStatAnalysisModel ciLogStatAnalysisModel) throws Exception {
        HashMap paramMap = new HashMap();
        paramMap.put("opTypeId", ciLogStatAnalysisModel.getOpTypeId());
        String datePeriodSql = this.getDatePeriodSql(ciLogStatAnalysisModel, paramMap);
        StringBuffer sql = new StringBuffer();
        sql.append("select DATA_DATE ,sum(OP_TIMES) OP_TIMES from ").append("CI_LOG_OVERVIEW").append(" where OP_TYPE_ID =:opTypeId ").append(datePeriodSql).append(" group by data_date order by data_date asc");
        List resultList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLogStatAnalysisModel.class), paramMap);
        return resultList;
    }

    public List<DimOpLogType> findOpTypeListByParentId(String parentId) throws Exception {
        if(StringUtil.isEmpty(parentId)) {
            parentId = "-1";
        }

        ArrayList resultList = null;
        CopyOnWriteArrayList dimOpLogTypeList = CacheBase.getInstance().getObjectList("DIM_OP_LOG_TYPE");
        if(dimOpLogTypeList != null && dimOpLogTypeList.size() > 0) {
            resultList = new ArrayList();

            for(int i = 0; i < dimOpLogTypeList.size(); ++i) {
                String parentIdFromTable = ((DimOpLogType)dimOpLogTypeList.get(i)).getParentId();
                parentIdFromTable = StringUtil.isEmpty(parentIdFromTable)?"":parentIdFromTable.trim();
                if(parentId.equalsIgnoreCase(parentIdFromTable)) {
                    resultList.add(dimOpLogTypeList.get(i));
                }
            }
        }

        return resultList;
    }

    public List<CiLogStatAnalysisModel> findAllSecondLogViewDataInThePeriod(Pager pager, CiLogStatAnalysisModel model) throws Exception {
        String[] opTypeIds = model.getOpTypeId().split(",");
        int opTypeIdNum = Math.min(opTypeIds.length, 10);
        HashMap paramMap = new HashMap();
        String datePeriodSql = this.getDatePeriodSql(model, paramMap);
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select \'").append(model.getBeginDate()).append("\'||\'-\'||\'").append(model.getEndDate()).append("\' data_Date,SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME");

        for(int pageSql = 0; pageSql < opTypeIdNum; ++pageSql) {
            sqlBuf.append(", sum(case when OP_TYPE_ID=\'").append(opTypeIds[pageSql]).append("\' then op_TIMES else 0 end) op_TIMES_").append(pageSql + 1);
        }

        sqlBuf.append(" from CI_LOG_OVERVIEW where 1=1 ").append(datePeriodSql);
        sqlBuf.append(" group by SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME");
        this.log.debug(" -findAllLogViewDataInThePeriod sql:" + sqlBuf.toString());
        String var10 = this.getDataBaseAdapter().getPagedSql(sqlBuf.toString(), pager.getPageNum(), pager.getPageSize());
        this.log.debug(" -findAllLogViewDataInThePeriod pageSql:" + var10);
        List resultList = this.getSimpleJdbcTemplate().query(var10, ParameterizedBeanPropertyRowMapper.newInstance(CiLogStatAnalysisModel.class), paramMap);
        return resultList;
    }

    public int findAllSecondLogViewDataInThePeriodCount(CiLogStatAnalysisModel model) throws Exception {
        String[] opTypeIds = model.getOpTypeId().split(",");
        int opTypeIdNum = Math.min(opTypeIds.length, 10);
        HashMap paramMap = new HashMap();
        String datePeriodSql = this.getDatePeriodSql(model, paramMap);
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select \'").append(model.getBeginDate()).append("\'||\'-\'||\'").append(model.getEndDate()).append("\' data_Date,SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME");

        for(int countSqlBuf = 0; countSqlBuf < opTypeIdNum; ++countSqlBuf) {
            sqlBuf.append(", sum(case when OP_TYPE_ID=\'").append(opTypeIds[countSqlBuf]).append("\' then op_TIMES else 0 end) op_TIMES_").append(countSqlBuf + 1);
        }

        sqlBuf.append(" from ").append("CI_LOG_OVERVIEW").append(" where 1=1 ").append(datePeriodSql);
        sqlBuf.append(" group by SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME");
        this.log.debug(" -findAllLogViewDataInThePeriod sql:" + sqlBuf.toString());
        StringBuffer var8 = new StringBuffer();
        var8.append("select count(*) from (").append(var8.toString()).append(") abc");
        return this.getSimpleJdbcTemplate().queryForInt(var8.toString(), paramMap);
    }

    public List<CiLogStatAnalysisModel> findtitle(CiLogStatAnalysisModel logStatAnalysisModel) throws Exception {
        HashMap paramMap = new HashMap();
        paramMap.put("data_date", logStatAnalysisModel.getEndDate());
        DataBaseAdapter adpter = new DataBaseAdapter(Configure.getInstance().getProperty("CI_DBTYPE"));
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select dim_table_1.op_type_name, dim_table_1.op_type_id,").append(adpter.getNvl("table_1.OP_TIMES", "0")).append(" OP_TIMES from ( SELECT * FROM DIM_OP_LOG_TYPE WHERE PARENT_ID = \'-1\' ) dim_table_1 ").append(" left join ( SELECT DATA_DATE , OP_TYPE_ID, SUM(OP_TIMES) OP_TIMES FROM CI_LOG_OVERVIEW WHERE data_date=:data_date GROUP BY data_date, OP_TYPE_ID ) table_1 on table_1.op_type_id = dim_table_1.op_type_id order by dim_table_1.SORT_NUM ASC ");
        List resultList = this.getSimpleJdbcTemplate().query(sqlBuf.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLogStatAnalysisModel.class), paramMap);
        return resultList;
    }

    public List<CiLogStatAnalysisModel> findAllSecondLogViewDataInThePeriod(CiLogStatAnalysisModel model) throws Exception {
        String[] opTypeIds = model.getOpTypeId().split(",");
        int opTypeIdNum = Math.min(opTypeIds.length, 10);
        HashMap paramMap = new HashMap();
        String datePeriodSql = this.getDatePeriodSql(model, paramMap);
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select \'").append(model.getBeginDate()).append("\'||\'-\'||\'").append(model.getEndDate()).append("\' data_Date,SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME");

        for(int resultList = 0; resultList < opTypeIdNum; ++resultList) {
            sqlBuf.append(", sum(case when OP_TYPE_ID=\'").append(opTypeIds[resultList]).append("\' then op_TIMES else 0 end) op_TIMES_").append(resultList + 1);
        }

        sqlBuf.append(" from CI_LOG_OVERVIEW where 1=1 ").append(datePeriodSql);
        sqlBuf.append(" group by SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME");
        this.log.debug(" -findAllLogViewDataInThePeriod sql:" + sqlBuf.toString());
        List var8 = this.getSimpleJdbcTemplate().query(sqlBuf.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLogStatAnalysisModel.class), paramMap);
        return var8;
    }

    public List<Map<String, Object>> findLogUserDetilList(CiLogStatAnalysisModel model) throws Exception {
        String[] opTypeIds = model.getOpTypeId().split(",");
        int opTypeIdNum = Math.min(opTypeIds.length, 10);
        HashMap paramMap = new HashMap();
        String datePeriodSql = this.getDatePeriodSql(model, paramMap);
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select data_date,USER_ID,USER_NAME,SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME");

        for(int resultList = 0; resultList < opTypeIdNum; ++resultList) {
            sqlBuf.append(",sum(case when OP_TYPE_ID=\'").append(opTypeIds[resultList]).append("\' then op_TIMES else 0 end) op_TIMES_").append(resultList + 1);
        }

        sqlBuf.append(" from CI_LOG_USER_DETAIL where 1=1 ").append(datePeriodSql);
        if(StringUtil.isNotEmpty(model.getSecondDeptId())) {
            sqlBuf.append(" and SECOND_DEPT_ID=:secondDeptId ");
            paramMap.put("secondDeptId", model.getSecondDeptId());
        }

        if(StringUtil.isNotEmpty(model.getThirdDeptId())) {
            sqlBuf.append(" and THIRD_DEPT_ID=:thirdDeptId ");
            paramMap.put("thirdDeptId", model.getThirdDeptId());
        }

        sqlBuf.append(" and OP_TYPE_ID in(\'").append(model.getOpTypeId().replaceAll(",", "\',\'")).append("\')");
        sqlBuf.append(" group by data_date,USER_ID,USER_NAME,SECOND_DEPT_ID,SECOND_DEPT_NAME,THIRD_DEPT_ID,THIRD_DEPT_NAME").append(" order by data_date desc");
        this.log.debug(" -findLogUserDetilList sql:" + sqlBuf.toString());
        this.log.debug(" -findLogUserDetilList sql:" + sqlBuf.toString());
        List var8 = this.getSimpleJdbcTemplate().queryForList(sqlBuf.toString(), paramMap);
        return var8;
    }
}

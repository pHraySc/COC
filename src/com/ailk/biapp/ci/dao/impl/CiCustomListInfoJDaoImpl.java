package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomListInfoJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.model.CrmTypeModel;
import com.ailk.biapp.ci.util.JDBCUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomListInfoJDaoImpl extends JdbcBaseDao implements ICiCustomListInfoJDao {
    private Logger log = Logger.getLogger(CiCustomListInfoJDaoImpl.class);

    public CiCustomListInfoJDaoImpl() {
    }

    public int selectCustomersTotalCount(CiCustomListInfo bean) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from ").append(bean.getListTableName());
        this.log.debug(sql);
        return this.selectBackCountBySql(sql.toString());
    }

    public int selectCustomersListInfoCount(CiCustomListInfo bean) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from ci_custom_list_info where custom_group_id = :customGroupId");
        this.log.debug(sql.toString());
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(bean));
    }

    public int selectEndedCustomersListInfoCount(CiCustomListInfo bean) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from ci_custom_list_info where custom_group_id = :customGroupId and (DATA_STATUS = 0 or DATA_STATUS = 3)");
        this.log.debug(sql.toString());
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(bean));
    }

    public List<CiCustomListInfo> selectCustomersListInfo(int currPage, int pageSize, CiCustomListInfo bean) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from ci_custom_list_info where custom_group_id = :customGroupId order by data_date desc");
        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), currPage, pageSize);
        this.log.debug(sqlPage);
        return this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiCustomListInfo.class), new BeanPropertySqlParameterSource(bean));
    }

    public Map<String, Object> selectBackForMap(String sql, Object... param) {
        try {
            this.log.debug("selectBackForMap:" + sql);
            Map e = this.getBackSimpleJdbcTemplate().queryForMap(sql, param);
            return e;
        } catch (DataAccessException var4) {
            this.log.error(var4.getMessage() + sql);
            return null;
        }
    }

    public void dropTable(String tableName) {
        String sql = "drop table " + tableName;
        this.log.debug(sql.toString());
        if(this.tableExists(tableName)) {
            this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(sql);
        }

    }

    private boolean tableExists(String tableName) {
        boolean flag = true;

        try {
            StringBuffer e = new StringBuffer();
            e.append("SELECT COUNT(1) FROM ").append(tableName);
            this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(e.toString());
        } catch (Exception var4) {
            this.log.warn("table " + tableName + " is not exists!");
            flag = false;
        }

        return flag;
    }

    public boolean tableExists(String tableName, String columnName) {
        boolean flag = true;

        try {
            StringBuffer e = new StringBuffer();
            e.append("SELECT " + columnName + " FROM ").append(tableName);
            this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(e.toString());
        } catch (Exception var5) {
            this.log.warn("table " + tableName + "or columnName " + columnName + " is not exists!");
            flag = false;
        }

        return flag;
    }

    public String getSqlCreateAsTable(String newTab, String tmpTable, String column) {
        String createSql = this.getBackDataBaseAdapter().getSqlCreateAsTable(newTab, tmpTable, column);
        return createSql;
    }

    public void addTableColumn(String tableName, String column, String columnType) throws Exception {
        String createSql = this.getBackDataBaseAdapter().getSqlAddColumn(tableName, column, columnType);
        this.log.info("创建表中的列的字段的sql:" + createSql);
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(createSql);
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute("COMMIT");
    }

    public void executeInBackDataBase(String sql) {
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(sql);
    }

    public void executeInFrontDataBase(String sql) {
        this.getSimpleJdbcTemplate().getJdbcOperations().execute(sql);
    }

    public int insertInBackDataBase(String sql) throws Exception {
        boolean rows = false;
        Connection con = null;
        Statement stmt = null;

        int rows1;
        try {
            con = JDBCUtil.getConnection();
            stmt = con.createStatement();
            rows1 = stmt.executeUpdate(sql);
        } catch (SQLException var16) {
            this.log.debug("在BACK库里执行插入语句报错", var16);
            throw var16;
        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException var15) {
                    this.log.debug("关闭Statement报错", var15);
                }
            }

            if(con != null) {
                try {
                    JDBCUtil.closeConnection();
                } catch (Exception var14) {
                    this.log.debug("关闭Connection报错", var14);
                }
            }

        }

        return rows1;
    }

    public int insertInBackDataBase(String sql, String databaseUrl, String username, String password) throws Exception {
        boolean rows = false;
        Connection con = null;
        Statement stmt = null;

        int rows1;
        try {
            con = JDBCUtil.getConnection(databaseUrl, username, password);
            stmt = con.createStatement();
            rows1 = stmt.executeUpdate(sql);
        } catch (SQLException var47) {
            this.log.debug("在BACK库里执行插入语句报错", var47);
            throw var47;
        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException var46) {
                    this.log.debug("关闭Statement报错", var46);
                } finally {
                    if(con != null) {
                        try {
                            JDBCUtil.closeConnection();
                        } catch (Exception var45) {
                            this.log.debug("关闭Connection报错", var45);
                        }
                    }

                }
            }

        }

        return rows1;
    }

    public List<CrmTypeModel> selectCrmTypeModel() {
        String sql = "SELECT * from DIM_CRM_LABEL_TYPE where icon is not null";
        this.log.debug(sql);
        return this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CrmTypeModel.class), new Object[0]);
    }

    public List<Map<String, Object>> selectGroupListData(int currPage, int pageSize, String listName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ").append(listName);
        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), currPage, pageSize);
        List list = this.getBackSimpleJdbcTemplate().queryForList(sqlPage, new Object[0]);
        return list;
    }

    public List<CiLabelInfo> selectLabelInfoByName(String labelName, String labelIds, String parentId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.* FROM  ");
        if(StringUtil.isNotEmpty(parentId)) {
            sql.append("  (SELECT * FROM CI_LABEL_INFO B  START WITH B.PARENT_ID = \'" + parentId + "\' CONNECT BY PRIOR B.LABEL_ID= PARENT_ID)  ");
        } else {
            sql.append("  CI_LABEL_INFO  ");
        }

        sql.append(" A,CI_LABEL_EXT_INFO B ");
        sql.append(" WHERE LABEL_TYPE_ID<>").append(8);
        sql.append(" AND DATA_STATUS_ID=").append(2);
        sql.append(" AND IS_STAT_USER_NUM=").append(1);
        sql.append(" AND A.LABEL_ID=B.LABEL_ID  ");
        if(StringUtil.isNotEmpty(labelIds)) {
            sql.append(" and a.label_id  in (").append(labelIds).append(")");
        }

        sql.append(" AND LOWER(A.LABEL_NAME) LIKE ?  escape \'|\'");
        sql.append(" ORDER BY A.SORT_NUM");
        if(StringUtil.isEmpty(labelName)) {
            labelName = "";
        }

        this.log.debug("query label info by labelName->>" + sql.toString());
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelInfo.class), new Object[]{"%" + labelName.toLowerCase().replace("|", "||").replace("%", "|%").replace("_", "|_") + "%"});
    }

    public List<CiGroupAttrRel> selectGroupAttrRelByCustomeIds(String customIds, Date listCreateTime, String customName) {
        if(StringUtil.isEmpty(customIds)) {
            customIds = "\'\'";
        }

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.CUSTOM_GROUP_ID customId,A.ATTR_COL columnName,");
        sql.append(" A.ATTR_COL_TYPE,A.ATTR_COL_NAME,A.ATTR_SOURCE,A.LABEL_OR_CUSTOM_ID,A.LABEL_OR_CUSTOM_COLUMN,A.IS_VERTICAL_ATTR,A.STATUS, ");
        sql.append(" B.CUSTOM_GROUP_NAME customName ");
        sql.append(" FROM CI_GROUP_ATTR_REL A,CI_CUSTOM_GROUP_INFO B ");
        sql.append(" WHERE A.CUSTOM_GROUP_ID=B.CUSTOM_GROUP_ID ");
        sql.append(" AND   A.CUSTOM_GROUP_ID =").append(customIds);
        sql.append(" AND A.ATTR_COL_NAME LIKE ? escape \'|\'");
        sql.append(" AND ").append(this.getDataBaseAdapter().getFullDate("A.MODIFY_TIME")).append(" = ");
        sql.append(" (select ").append(this.getDataBaseAdapter().getFullDate("min(MODIFY_TIME)")).append("from CI_GROUP_ATTR_REL where MODIFY_TIME >= ? and  CUSTOM_GROUP_ID =").append(customIds).append(" ) ");
        sql.append(" ORDER BY  A.CUSTOM_GROUP_ID");
        if(StringUtil.isEmpty(customName)) {
            customName = "";
        }

        this.log.debug("query group attribte by custom id->>" + sql.toString());
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiGroupAttrRel.class), new Object[]{"%" + customName.replace("|", "||").replace("%", "|%").replace("_", "|_") + "%", listCreateTime});
    }

    public int selectCountPustReq(String customGroupId, String sysId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT count(1) FROM CI_CUSTOM_PUSH_REQ t1 WHERE EXISTS ");
        sql.append("( SELECT list_table_name FROM  CI_CUSTOM_GROUP_INFO t2, CI_CUSTOM_LIST_INFO t3 ");
        sql.append(" where t2.custom_group_id = t3.custom_group_id ");
        sql.append(" and  t3.list_table_name =t1.list_table_name  ");
        sql.append(" and  t2.update_cycle !=1");
        sql.append(" and t2.custom_group_id= ?");
        sql.append(") and t1.sys_id != ?");
        this.log.debug(sql);
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[]{customGroupId, sysId});
    }

    public int selectCiLabelRuleCountByListTableName(String customId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1) FROM CI_LABEL_RULE A WHERE EXISTS ( ");
        sql.append("SELECT LIST_TABLE_NAME FROM CI_CUSTOM_LIST_INFO B  ");
        sql.append("WHERE A.CALCU_ELEMENT=B.LIST_TABLE_NAME AND B.CUSTOM_GROUP_ID=\'");
        sql.append(customId).append("\' ");
        sql.append(") AND CUSTOM_TYPE=1 AND ELEMENT_TYPE=5");
        this.log.debug("query label rule count by list table name-->>" + sql);
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[0]);
    }

    private String getFromSql() {
        StringBuffer sb = new StringBuffer();
        sb.append("T.LABEL_ID,T.UPDATE_CYCLE,T.PARENT_ID,T.EFFEC_TIME,T.FAIL_TIME,T.CREATE_USER_ID,T.CREATE_TIME ");
        sb.append(",T.CREATE_DESC,T.DEPT_ID,T.DATA_STATUS_ID,T.LABEL_TYPE_ID,T.PUBLISH_USER_ID,T.PUBLISH_TIME,T.PUBLISH_DESC ");
        sb.append(",T.BUSI_CALIBER,T.DATA_SOURCE,T.BUSI_LEGEND,T.APPLY_SUGGEST,T.SORT_NUM ");
        return sb.toString();
    }

    public List<CiLabelInfo> selectLabelInfoByNameOrIds(String labelName, String labelIds, String parentId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ").append(this.getFromSql()).append(",B.COLUMN_CN_NAME columnCnName,B.COLUMN_ID columnId");
        sql.append(" ,CASE WHEN B.COLUMN_ID IS NULL THEN T.LABEL_NAME ELSE T.LABEL_NAME||\'-\'||B.COLUMN_CN_NAME  END LABEL_NAME ");
        sql.append("  FROM ");
        if(StringUtil.isNotEmpty(parentId)) {
            sql.append("  (SELECT * FROM CI_LABEL_INFO B  START WITH B.PARENT_ID = \'" + parentId + "\' CONNECT BY PRIOR B.LABEL_ID= PARENT_ID)  ");
        } else {
            sql.append("  CI_LABEL_INFO  ");
        }

        sql.append("  T ");
        sql.append(" LEFT JOIN ( ");
        sql.append(" SELECT C.LABEL_ID,A.COLUMN_CN_NAME,A.COLUMN_ID FROM CI_MDA_SYS_TABLE_COLUMN A ");
        sql.append(" ,CI_LABEL_VERTICAL_COLUMN_REL C ");
        sql.append(" WHERE A.COLUMN_ID = C.COLUMN_ID ) B ");
        sql.append(" ON T.LABEL_ID = B.LABEL_ID ,CI_LABEL_EXT_INFO C ");
        sql.append(" WHERE T.LABEL_ID = C.LABEL_ID ");
        sql.append(" AND T.DATA_STATUS_ID=").append(2);
        sql.append(" AND C.IS_STAT_USER_NUM=").append(1);
        if(StringUtil.isNotEmpty(labelIds)) {
            sql.append(" AND T.LABEL_ID  IN (").append(labelIds).append(")");
        }

        sql.append(" AND (LOWER(T.LABEL_NAME) LIKE ?  escape \'|\' OR LOWER(B.COLUMN_CN_NAME) LIKE  ?  escape \'|\' )");
        sql.append(" ORDER BY T.SORT_NUM");
        if(StringUtil.isEmpty(labelName)) {
            labelName = "";
        }

        this.log.debug("query label info by labelNameOrIds->>" + sql.toString());
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelInfo.class), new Object[]{"%" + labelName.toLowerCase().replace("|", "||").replace("%", "|%").replace("_", "|_") + "%", "%" + labelName.toLowerCase().replace("|", "||").replace("%", "|%").replace("_", "|_") + "%"});
    }

    public List<CiLabelInfo> selectLabelInfoTreeList() {
        StringBuffer sql = new StringBuffer();
        sql.append("select a.* from CI_LABEL_INFO a,CI_LABEL_EXT_INFO b where a.LABEL_ID = b.LABEL_ID and b.LABEL_LEVEL <4");
        this.log.debug("query label info  treee list->>" + sql.toString());
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelInfo.class), new Object[0]);
    }

    public String getListTableNameByCroupIdAndDataDate(String customGroupId, String dataDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("select l.* from ci_custom_list_info l where l.CUSTOM_GROUP_ID = ? and l.DATA_DATE = ?");
        CiCustomListInfo result = (CiCustomListInfo)this.getSimpleJdbcTemplate().queryForObject(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomListInfo.class), new Object[]{customGroupId, dataDate});
        return result.getListTableName();
    }

    public int selecrDistinctListNum(String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(distinct ").append(Configure.getInstance().getProperty("RELATED_COLUMN")).append(")  from ").append(tableName);
        this.log.debug(sql.toString());
        return this.tableExists(tableName)?this.selectBackCountBySql(sql.toString()):0;
    }

    public String selectListMaxNumSql(String sql, int listMaxNum, String attrSqlStr) {
        String sqlStr = this.getDataBaseAdapter().getLimtCountSql(sql, 1, listMaxNum, attrSqlStr);
        return sqlStr;
    }

    public int selectCustomListCount(CiCustomListInfo ciCustomListInfo) {
        StringBuffer sqlBuf = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        sqlBuf.append("select count(*) from ci_custom_list_info a ").append("left join ci_custom_group_info b on a.custom_group_id = b.custom_group_id").append(" where b.status = 1 and a.file_create_status = 3 and a.data_status = 3 and b.create_user_id = \'").append(ciCustomListInfo.getCreateUserId()).append("\'");
        String province = Configure.getInstance().getProperty("PROVINCE");
        if("zhejiang".equals(province)) {
            sqlBuf.append(" and a.file_approve_status = 3");
        }

        if(StringUtils.isNotBlank(ciCustomListInfo.getCustomGroupName())) {
            sqlBuf.append(" and lower(b.custom_group_name) like lower(\'%").append(ciCustomListInfo.getCustomGroupName()).append("%\')");
        }

        String startStr;
        if(ciCustomListInfo.getStartDate() != null) {
            startStr = format.format(ciCustomListInfo.getStartDate());
            sqlBuf.append(" and a.data_time >= ").append(this.getDataBaseAdapter().getTimeStamp(startStr, "00", "00", "00"));
        }

        if(ciCustomListInfo.getEndDate() != null) {
            startStr = format.format(ciCustomListInfo.getEndDate());
            sqlBuf.append(" and a.data_time <= ").append(this.getDataBaseAdapter().getTimeStamp(startStr, "23", "59", "59"));
        }

        if(ciCustomListInfo.getUpdateCycle() != null) {
            sqlBuf.append(" and b.update_cycle = ").append(ciCustomListInfo.getUpdateCycle());
        }

        this.log.info("sql:" + sqlBuf.toString());
        return this.getSimpleJdbcTemplate().queryForInt(sqlBuf.toString(), new Object[0]);
    }

    public List<CiCustomListInfo> selectCustomList(int start, int pageSize, CiCustomListInfo ciCustomListInfo) {
        StringBuffer sqlBuf = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        sqlBuf.append("select a.*,b.custom_group_name,b.create_user_id,b.update_cycle from ci_custom_list_info a ").append("left join ci_custom_group_info b on a.custom_group_id = b.custom_group_id").append(" where b.status = 1 and a.file_create_status = 3 and a.data_status = 3 and b.create_user_id = \'").append(ciCustomListInfo.getCreateUserId()).append("\'");
        String province = Configure.getInstance().getProperty("PROVINCE");
        if("zhejiang".equals(province)) {
            sqlBuf.append(" and a.file_approve_status = 3");
        }

        if(StringUtils.isNotBlank(ciCustomListInfo.getCustomGroupName())) {
            sqlBuf.append(" and lower(b.custom_group_name) like lower(\'%").append(ciCustomListInfo.getCustomGroupName()).append("%\')");
        }

        String sql;
        if(ciCustomListInfo.getStartDate() != null) {
            sql = format.format(ciCustomListInfo.getStartDate());
            sqlBuf.append(" and a.data_time >= ").append(this.getDataBaseAdapter().getTimeStamp(sql, "00", "00", "00"));
        }

        if(ciCustomListInfo.getEndDate() != null) {
            sql = format.format(ciCustomListInfo.getEndDate());
            sqlBuf.append(" and a.data_time <= ").append(this.getDataBaseAdapter().getTimeStamp(sql, "23", "59", "59"));
        }

        if(ciCustomListInfo.getUpdateCycle() != null) {
            sqlBuf.append(" and b.update_cycle = ").append(ciCustomListInfo.getUpdateCycle());
        }

        sqlBuf.append(" order by a.data_time desc");
        sql = this.getDataBaseAdapter().getPagedSql(sqlBuf.toString(), start, pageSize);
        this.log.info("sql:" + sql);
        return this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiCustomListInfo.class), new Object[0]);
    }

    public void updateCiCustomListInfo(String listTableName, String custGroupDate, Integer isstrengthen) {
        HashMap paramMap = new HashMap();
        paramMap.put("list_table_name", listTableName);
        paramMap.put("data_date", custGroupDate);
        paramMap.put("isStrengthen", isstrengthen);
        String sql = "UPDATE CI_CUSTOM_LIST_INFO SET  ISSTRENGTHEN = :isStrengthen WHERE  LIST_TABLE_NAME = :list_table_name AND DATA_DATE = :data_date";
        this.log.info("更新清单信息表的sql:" + sql);
        this.getSimpleJdbcTemplate().update(sql, paramMap);
    }
}

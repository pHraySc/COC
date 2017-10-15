package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.*;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiApproveUserInfo;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.DimCocLabelStatus;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class CiLabelInfoJDaoImpl extends JdbcBaseDao implements ICiLabelInfoJDao {
    protected Logger log = Logger.getLogger(CiLabelInfoJDaoImpl.class);
    @Autowired
    ICiApproveUserInfoHDao ciApproveUserInfoHDao;
    @Autowired
    IDimApproveStatusHDao dimApproveStatusHDao;
    @Autowired
    ICiApproveStatusHDao ciApproveStatusHDao;
    @Autowired
    ICiApproveHistoryHDao ciApproveHistoryHDao;

    public CiLabelInfoJDaoImpl() {
    }

    public int getCountBySql(CiLabelInfo ciLabelInfo, String processId) throws Exception {
        CiLabelInfo ciLabelInfoCopy = new CiLabelInfo();
        ciLabelInfoCopy.setCreateUserId(ciLabelInfo.getCreateUserId());
        ciLabelInfoCopy.setCurrApproveStatusId(ciLabelInfo.getCurrApproveStatusId());
        ciLabelInfoCopy.setDataStatusId(ciLabelInfo.getDataStatusId());
        ciLabelInfoCopy.setLabelName("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoCopy.setCreateDesc("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoCopy.setBusiCaliber("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoCopy.setIsStatUserNum(ciLabelInfo.getIsStatUserNum());
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) ").append(" from ci_label_info t ").append(" left join ci_label_ext_info ext on t.label_id = ext.label_id");
        if(StringUtil.isNotEmpty(ciLabelInfo.getCurrApproveStatusId()) && !"-1".equals(ciLabelInfo.getCurrApproveStatusId())) {
            sql.append(" inner join ci_approve_status e on ").append(this.getDataBaseAdapter().getIntToChar("t.label_id")).append("  = e.resource_id ").append(" and e.process_id=\'").append(processId).append("\'").append(" and e.curr_approve_status_id = :currApproveStatusId ");
        } else {
            sql.append(" left join ci_approve_status e on ").append(this.getDataBaseAdapter().getIntToChar("t.label_id")).append("  = e.resource_id ").append(" and e.process_id=\'").append(processId).append("\'");
        }

        sql.append(" left join dim_approve_status f on e.curr_approve_status_id = f.approve_status_id").append(" left join dim_approve_level g on g.approve_role_id = f.approve_role_id").append(" where  t.data_status_id <> ").append(6);
        if(StringUtil.isNotEmpty(ciLabelInfo.getCreateUserId()) && !"-1".equals(ciLabelInfo.getCreateUserId())) {
            boolean isAdmin = PrivilegeServiceUtil.isAdminUser(ciLabelInfo.getCreateUserId());
            if(isAdmin) {
                sql.append(" and (");
                sql.append(" (f.sort_num = 2 and g.approve_role_type = 2)");
                sql.append(" or t.create_user_id= :createUserId )");
            } else {
                sql.append(" and t.create_user_id= :createUserId");
            }
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getLabelName())) {
            sql.append(" and (LOWER(t.label_name) like :labelName  escape \'|\' or LOWER(t.create_desc) like :createDesc escape \'|\' or LOWER(t.busi_caliber) like :busiCaliber escape \'|\') ");
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getDataStatusId()) && -1 != ciLabelInfo.getDataStatusId().intValue()) {
            sql.append(" and t.data_status_id= :dataStatusId");
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getIsStatUserNum())) {
            sql.append(" and ext.is_stat_user_num= :isStatUserNum");
        }

        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(ciLabelInfoCopy));
    }

    public List<CiLabelInfo> getPageListBySql(int currPage, int pageSize, CiLabelInfo ciLabelInfo, String processId) throws Exception {
        StringBuilder sqls = new StringBuilder();
        CiLabelInfo ciLabelInfoClone = new CiLabelInfo();
        ciLabelInfoClone.setCreateUserId(ciLabelInfo.getCreateUserId());
        ciLabelInfoClone.setCurrApproveStatusId(ciLabelInfo.getCurrApproveStatusId());
        ciLabelInfoClone.setDataStatusId(ciLabelInfo.getDataStatusId());
        ciLabelInfoClone.setLabelName("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoClone.setCreateDesc("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoClone.setBusiCaliber("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoClone.setIsStatUserNum(ciLabelInfo.getIsStatUserNum());
        ciLabelInfoClone.setLabelIdLevelDesc(ciLabelInfo.getLabelIdLevelDesc());
        sqls.append("select t.label_id,t.label_name,t.create_time,t.update_cycle,t.parent_id,t.create_user_id,t.is_sys_recom,").append("c.label_type_name,").append("t.data_status_id,").append("d.data_status_name,").append("e.curr_approve_status_id,").append("f.approve_status_name,").append("f.sort_num,").append("ext.label_level,").append("ext.is_stat_user_num,").append(" g.approve_role_type").append(" from ").append(" ci_label_info t").append(" left join ci_label_ext_info ext on t.label_id = ext.label_id").append(" left join dim_label_type c on t.label_type_id = c.label_type_id").append(" left join dim_label_data_status d on t.data_status_id = d.data_status_id");
        if(StringUtil.isNotEmpty(ciLabelInfo.getCurrApproveStatusId()) && !"-1".equals(ciLabelInfo.getCurrApproveStatusId())) {
            sqls.append(" inner join ci_approve_status e on ").append(this.getDataBaseAdapter().getIntToChar("t.label_id")).append("  = e.resource_id ").append(" and e.process_id=\'" + processId + "\'").append(" and e.curr_approve_status_id = :currApproveStatusId ");
        } else {
            sqls.append(" left join ci_approve_status e on ").append(this.getDataBaseAdapter().getIntToChar("t.label_id")).append("  = e.resource_id ").append(" and e.process_id=\'" + processId + "\'");
        }

        sqls.append(" left join dim_approve_status f on e.curr_approve_status_id = f.approve_status_id").append(" left join dim_approve_level g on g.approve_role_id = f.approve_role_id").append(" where  t.data_status_id <> 6");
        if(StringUtil.isNotEmpty(ciLabelInfo.getCreateUserId()) && !"-1".equals(ciLabelInfo.getCreateUserId())) {
            boolean sqlPage = PrivilegeServiceUtil.isAdminUser(ciLabelInfo.getCreateUserId());
            if(sqlPage) {
                sqls.append(" and (");
                sqls.append(" (f.sort_num = 2 and g.approve_role_type = 2)");
                sqls.append(" or t.create_user_id = :createUserId )");
            } else {
                sqls.append(" and t.create_user_id = :createUserId");
            }
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getLabelName())) {
            sqls.append(" and (LOWER(t.label_name) like :labelName escape \'|\' or LOWER(t.create_desc) like :createDesc escape \'|\' or LOWER(t.busi_caliber) like :busiCaliber escape \'|\') ");
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getDataStatusId()) && -1 != ciLabelInfo.getDataStatusId().intValue()) {
            sqls.append(" and t.data_status_id = :dataStatusId");
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getIsStatUserNum())) {
            sqls.append(" and ext.is_stat_user_num = :isStatUserNum");
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getLabelIdLevelDesc())) {
            sqls.append(" and t.label_Id_Level_Desc like :labelIdLevelDesc");
        }

        sqls.append(" order by t.create_time desc");
        this.log.debug("标签分页查询：" + sqls);
        String sqlPage1 = this.getDataBaseAdapter().getPagedSql(sqls.toString(), currPage, pageSize);
        List list = this.getSimpleJdbcTemplate().query(sqlPage1, ParameterizedBeanPropertyRowMapper.newInstance(CiLabelInfo.class), new BeanPropertySqlParameterSource(ciLabelInfoClone));
        return list;
    }

    private String getLabelIdsByApproveUserInfo(CiApproveUserInfo ciApproveUserInfo) {
        Integer flag = Integer.valueOf(1);
        if(ciApproveUserInfo == null) {
            return "";
        } else {
            String approveRoleId = ciApproveUserInfo.getApproveRoleId();
            String approveStatusId = this.dimApproveStatusHDao.getApproveStatuByApproveRole(approveRoleId, flag);
            List labelIds = this.ciApproveStatusHDao.getLabelIdsByApprStaId(approveStatusId);
            String labelIds_str = labelIds.toString();
            return labelIds_str.substring(1, labelIds_str.length() - 1);
        }
    }

    public CiLabelInfo selectCiLabelInfoById(Integer labelId) {
        String sql = "select T3.*,CASE WHEN T2.SUMCC IS NULL THEN \'false\' ELSE \'true\' END ISATTENTION  from CI_LABEL_INFO T3 left join  ( SELECT T1.LABEL_ID, COUNT(T1.LABEL_ID) SUMCC FROM CI_USER_ATTENTION_LABEL T1 WHERE T1.USER_ID = ? GROUP BY T1.LABEL_ID) T2 ON T3.LABEL_ID = T2.LABEL_ID where T3.LABEL_ID = ? ";
        CiLabelInfo ciLabelInfo = (CiLabelInfo)this.getSimpleJdbcTemplate().queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiLabelInfo.class), new Object[]{PrivilegeServiceUtil.getUserId(), labelId});
        return ciLabelInfo;
    }

    public List<CiLabelInfo> selectSimilarityLabelNameList(CiLabelInfo ciLabelInfo) {
        String labelName = ciLabelInfo.getLabelName();
        this.replaceSpecialCharacter(labelName);
        labelName = "%" + labelName + "%";
        StringBuilder sqls = new StringBuilder();
        if(labelName != null && !labelName.trim().equals("")) {
            sqls.append(" select * from ci_label_info cli ").append(" where ").append(" cli.data_status_id <> ").append(6).append(" and cli.data_status_id <> ").append(5).append(" and cli.data_status_id <> ").append(4).append(" and cli.label_name like ? escape \'|\' ");
            //ci_label_info中的data_status_id ! = 456
            this.log.debug("selectSimilarityLabelNameList:" + sqls.toString());
            return this.getSimpleJdbcTemplate().query(sqls.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelInfo.class), new Object[]{labelName});
        } else {
            return null;
        }
    }

    public String selectDimTransIdByLabelId(Integer labelId) {
        String result = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DIM_TRANS_ID ");
        sql.append("FROM CI_LABEL_EXT_INFO A,CI_MDA_SYS_TABLE_COLUMN B  ");
        sql.append("WHERE A.COLUMN_ID=B.COLUMN_ID AND A.LABEL_ID=? ");
        Map map = this.getSimpleJdbcTemplate().queryForMap(sql.toString(), new Object[]{labelId});
        if(map != null && map.size() > 0) {
            result = (String)map.get("dim_trans_id");
        }

        return result;
    }

    public List<Map<String, Object>> getAllDimDataByDimTableDefine(DimTableDefine define, int currPage, int pageSize, String dimName, String cityAuthorityWhereStr) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM ( ");
        sql.append(" SELECT ").append(define.getDimCodeCol()).append("  V_KEY, ").append(define.getDimValueCol()).append("  V_NAME ");
        sql.append(" FROM ").append(define.getDimTablename());
        sql.append(" WHERE ").append("LOWER(").append(define.getDimValueCol()).append(")");
        sql.append(" LIKE ?  escape \'|\' ");
        if(StringUtil.isNotEmpty(cityAuthorityWhereStr)) {
            sql.append(" and ").append(cityAuthorityWhereStr);
        }

        sql.append(" ) ");
        if(StringUtil.isEmpty(dimName)) {
            dimName = "";
        } else {
            dimName = dimName.toLowerCase();
        }

        sql.append(" ORDER BY ").append(" V_KEY ");
        this.log.debug("getAllDimDataByDimTableDefine-->" + sql.toString());
        List result = this.getSimpleJdbcTemplate().queryForList(this.getDataBaseAdapter().getPagedSql(sql.toString(), currPage, pageSize), new Object[]{"%" + dimName + "%"});
        return result;
    }

    public List<Map<String, Object>> getAllDimDataByDimTableDefine(DimTableDefine define) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ").append(define.getDimCodeCol()).append("  V_KEY, ").append(define.getDimValueCol()).append("  V_NAME ");
        sql.append(" FROM ").append(define.getDimTablename());
        this.log.debug("getAllDimDataByDimTableDefine-->" + sql.toString());
        List result = this.getSimpleJdbcTemplate().queryForList(sql.toString(), new Object[0]);
        return result;
    }

    public long getAllDimDataCountByDimTableDefine(DimTableDefine define, String dimName, String cityAuthorityWhereStr) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) FROM ").append(define.getDimTablename());
        sql.append(" WHERE ").append("LOWER(").append(define.getDimValueCol()).append(")");
        sql.append(" LIKE ?  escape \'|\' ");
        if(StringUtil.isNotEmpty(cityAuthorityWhereStr)) {
            sql.append(" and ").append(cityAuthorityWhereStr);
        }

        if(StringUtil.isEmpty(dimName)) {
            dimName = "";
        } else {
            dimName = dimName.toLowerCase();
        }

        this.log.debug("getAllDimDataCountByDimTableDefine-->" + sql.toString());
        return this.getSimpleJdbcTemplate().queryForLong(sql.toString(), new Object[]{"%" + dimName + "%"});
    }

    private String replaceSpecialCharacter(String labelName) {
        if(StringUtil.isNotEmpty(labelName)) {
            labelName = labelName.trim();
            return labelName.replace("|", "||").replace("%", "|%").replace("_", "|_");//转译
        } else {
            return "";
        }
    }

    public List<Map<String, Object>> selectDimValueByImport(String ids, DimTableDefine define, String selectDimValueByImport) throws Exception {
        ArrayList result = new ArrayList();
        String[] idsArr = ids.split(",");
        int currentId = 0;
        ArrayList idsList = new ArrayList();
        StringBuffer sb = new StringBuffer();

        int i;
        for(i = 0; i < idsArr.length; ++i) {
            sb.append(idsArr[i]).append(",");
            ++currentId;
            if(currentId % 1000 == 0) {
                idsList.add(sb.toString().substring(0, sb.length() - 1));
                currentId = 0;
                sb = new StringBuffer();
            }
        }

        if(currentId != 0) {
            idsList.add(sb.toString().substring(0, sb.length() - 1));
        }

        for(i = 0; i < idsList.size(); ++i) {
            new ArrayList();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ").append(define.getDimCodeCol());
            sql.append(" as V_KEY, ").append(define.getDimValueCol());
            sql.append(" as V_NAME ");
            sql.append(" FROM ").append(define.getDimTablename());
            sql.append(" WHERE ").append(define.getDimCodeCol());
            sql.append(" in (").append((String)idsList.get(i)).append(") ");
            if(StringUtil.isNotEmpty(selectDimValueByImport)) {
                sql.append(" and ").append(selectDimValueByImport);
            }

            this.log.debug("sql-----------==>" + sql.toString());
            List item = this.getSimpleJdbcTemplate().queryForList(sql.toString(), new Object[0]);
            result.addAll(item);
        }

        return result;
    }

    public int updateCiLabelInfoDataDate(List<DimCocLabelStatus> labelStatList, String dataDate) {
        if(labelStatList != null && labelStatList.size() >= 1) {
            StringBuffer idSb = new StringBuffer();
            Iterator idStr = labelStatList.iterator();

            while(idStr.hasNext()) {
                DimCocLabelStatus sqlBuf = (DimCocLabelStatus)idStr.next();
                idSb.append(sqlBuf.getLabelId()).append(",");
            }

            String idStr1 = idSb.substring(0, idSb.length() - 1);
            StringBuffer sqlBuf1 = new StringBuffer();
            sqlBuf1.append("update CI_LABEL_INFO set DATA_DATE = ? where LABEL_ID in (").append(idStr1).append(")");
            return this.getSimpleJdbcTemplate().getJdbcOperations().update(sqlBuf1.toString(), new Object[]{dataDate});
        } else {
            return 0;
        }
    }

    public List<CiLabelInfo> selectVertAndEnumLabelList(Pager pager, CiLabelInfo ciLabelInfo) {
        StringBuffer sqlSb = new StringBuffer();
        CiLabelInfo ciLabelInfoCopy = new CiLabelInfo();
        ciLabelInfoCopy.setLabelName("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        sqlSb.append(" SELECT DISTINCT  A.* FROM ( ");
        sqlSb.append(" SELECT T1.* FROM  CI_LABEL_INFO T1, CI_LABEL_EXT_INFO T2 WHERE T1.LABEL_ID = T2.LABEL_ID ");
        sqlSb.append(" AND T1.DATA_STATUS_ID = ").append(2);
        sqlSb.append(" AND T2.IS_STAT_USER_NUM = ").append(1);
        sqlSb.append(" and ( T1.LABEL_TYPE_ID = ").append(5);
        sqlSb.append(" OR T1.LABEL_TYPE_ID = ").append(8).append(" ) ");
        sqlSb.append(" AND LOWER(T1.LABEL_NAME) LIKE :labelName  escape \'|\'");
        sqlSb.append(" ) A ");
        sqlSb.append(" LEFT JOIN CI_LABEL_VERTICAL_COLUMN_REL B ");
        sqlSb.append(" ON  A.LABEL_ID = B.LABEL_ID AND B.LABEL_TYPE_ID = ").append(5);
        String sqlStr = sqlSb.toString();
        this.log.debug("查询枚举及包含枚举属性的纵表标签列表SQL：" + sqlStr);
        String sqlPage = this.getDataBaseAdapter().getPagedSql(sqlStr.toString(), pager.getPageNum(), pager.getPageSize());
        this.log.debug("查询枚举及包含枚举属性的纵表标签列表分页SQL：" + sqlPage);
        List list = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiLabelInfo.class), new BeanPropertySqlParameterSource(ciLabelInfoCopy));
        return list;
    }

    public int selectVertAndEnumLabelCount(CiLabelInfo ciLabelInfo) {
        StringBuffer sqlSb = new StringBuffer();
        CiLabelInfo ciLabelInfoCopy = new CiLabelInfo();
        ciLabelInfoCopy.setLabelName("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        sqlSb.append(" SELECT COUNT(DISTINCT A.LABEL_ID) FROM ( ");
        sqlSb.append(" SELECT T1.* FROM  CI_LABEL_INFO T1, CI_LABEL_EXT_INFO T2 WHERE T1.LABEL_ID = T2.LABEL_ID ");
        sqlSb.append(" AND T1.DATA_STATUS_ID = ").append(2);
        sqlSb.append(" AND T2.IS_STAT_USER_NUM = ").append(1);
        sqlSb.append(" and ( T1.LABEL_TYPE_ID = ").append(5);
        sqlSb.append(" OR T1.LABEL_TYPE_ID = ").append(8).append(" ) ");
        sqlSb.append(" AND LOWER(T1.LABEL_NAME) LIKE :labelName  escape \'|\'");
        sqlSb.append(" ) A ");
        sqlSb.append(" LEFT JOIN CI_LABEL_VERTICAL_COLUMN_REL B ");
        sqlSb.append(" ON  A.LABEL_ID = B.LABEL_ID AND B.LABEL_TYPE_ID = ").append(5);
        String sqlStr = sqlSb.toString();
        this.log.debug("查询枚举及包含枚举属性的纵表标签列总数SQL：" + sqlStr);
        return this.getSimpleJdbcTemplate().queryForInt(sqlStr, new BeanPropertySqlParameterSource(ciLabelInfoCopy));
    }

    public int getLabelInfoListCountBySql(CiLabelInfo ciLabelInfo, String processId) throws Exception {
        CiLabelInfo ciLabelInfoCopy = new CiLabelInfo();
        ciLabelInfoCopy.setCreateUserId(ciLabelInfo.getCreateUserId());
        ciLabelInfoCopy.setCurrApproveStatusId(ciLabelInfo.getCurrApproveStatusId());
        ciLabelInfoCopy.setDataStatusId(ciLabelInfo.getDataStatusId());
        ciLabelInfoCopy.setLabelName("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoCopy.setCreateDesc("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoCopy.setBusiCaliber("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoCopy.setIsStatUserNum(ciLabelInfo.getIsStatUserNum());
        boolean count = false;
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) ").append(" from ci_label_info t ").append(" left join ci_label_ext_info ext on t.label_id = ext.label_id");
        if(StringUtil.isNotEmpty(ciLabelInfo.getCurrApproveStatusId()) && !"-1".equals(ciLabelInfo.getCurrApproveStatusId())) {
            sql.append(" inner join ci_approve_status e on ").append(this.getDataBaseAdapter().getIntToChar("t.label_id")).append("  = e.resource_id ").append(" and e.process_id=\'" + processId + "\'").append(" and e.curr_approve_status_id = :currApproveStatusId ");
        } else {
            sql.append(" left join ci_approve_status e on ").append(this.getDataBaseAdapter().getIntToChar("t.label_id")).append("  = e.resource_id ").append(" and e.process_id=\'" + processId + "\'");
        }

        sql.append(" left join dim_approve_status f on e.curr_approve_status_id = f.approve_status_id").append(" left join dim_approve_level g on g.approve_role_id = f.approve_role_id").append(" where  t.data_status_id <> 6");
        if(StringUtil.isNotEmpty(ciLabelInfo.getCreateUserId()) && !"-1".equals(ciLabelInfo.getCreateUserId())) {
            boolean isAdmin = PrivilegeServiceUtil.isAdminUser(ciLabelInfo.getCreateUserId());
            if(isAdmin) {
                sql.append(" and (");
                sql.append(" (f.sort_num = 2 and g.approve_role_type = 2)");
                sql.append(" or t.create_user_id= :createUserId )");
            } else {
                sql.append(" and t.create_user_id= :createUserId");
            }
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getLabelName())) {
            sql.append(" and (LOWER(t.label_name) like :labelName  escape \'|\' or LOWER(t.create_desc) like :createDesc escape \'|\' or LOWER(t.busi_caliber) like :busiCaliber escape \'|\') ");
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getDataStatusId()) && -1 != ciLabelInfo.getDataStatusId().intValue()) {
            sql.append(" and t.data_status_id= :dataStatusId");
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getIsStatUserNum())) {
            if(ciLabelInfo.getIsStatUserNum().equals(String.valueOf(1))) {
                sql.append(" and (ext.is_stat_user_num= :isStatUserNum or ext.is_stat_user_num is null)");
            } else {
                sql.append(" and ext.is_stat_user_num= :isStatUserNum");
            }
        }

        int count1 = this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(ciLabelInfoCopy));
        return count1;
    }

    public List<CiLabelInfo> getPageLabelInfoListBySql(int currPage, int pageSize, CiLabelInfo ciLabelInfo, String processId) throws Exception {
        StringBuilder sqls = new StringBuilder();
        CiLabelInfo ciLabelInfoClone = new CiLabelInfo();
        ciLabelInfoClone.setCreateUserId(ciLabelInfo.getCreateUserId());
        ciLabelInfoClone.setCurrApproveStatusId(ciLabelInfo.getCurrApproveStatusId());
        ciLabelInfoClone.setDataStatusId(ciLabelInfo.getDataStatusId());
        ciLabelInfoClone.setLabelName("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoClone.setCreateDesc("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoClone.setBusiCaliber("%" + this.replaceSpecialCharacter(ciLabelInfo.getLabelName()).toLowerCase() + "%");
        ciLabelInfoClone.setIsStatUserNum(ciLabelInfo.getIsStatUserNum());
        sqls.append("select t.label_id,t.label_name,t.create_time,t.update_cycle,t.parent_id,t.create_user_id,t.is_sys_recom,").append("c.label_type_name,").append("t.data_status_id,").append("d.data_status_name,").append("e.curr_approve_status_id,").append("f.approve_status_name,").append("f.sort_num,").append("ext.label_level,").append("ext.is_stat_user_num,").append(" g.approve_role_type").append(" from ").append(" ci_label_info t").append(" left join ci_label_ext_info ext on t.label_id = ext.label_id").append(" left join dim_label_type c on t.label_type_id = c.label_type_id").append(" left join dim_label_data_status d on t.data_status_id = d.data_status_id");
        if(StringUtil.isNotEmpty(ciLabelInfo.getCurrApproveStatusId()) && !"-1".equals(ciLabelInfo.getCurrApproveStatusId())) {
            sqls.append(" inner join ci_approve_status e on ").append(this.getDataBaseAdapter().getIntToChar("t.label_id")).append("  = e.resource_id ").append(" and e.process_id=\'" + processId + "\'").append(" and e.curr_approve_status_id = :currApproveStatusId ");
        } else {
            sqls.append(" left join ci_approve_status e on ").append(this.getDataBaseAdapter().getIntToChar("t.label_id")).append("  = e.resource_id ").append(" and e.process_id=\'" + processId + "\'");
        }

        sqls.append(" left join dim_approve_status f on e.curr_approve_status_id = f.approve_status_id").append(" left join dim_approve_level g on g.approve_role_id = f.approve_role_id").append(" where  t.data_status_id <> 6");
        if(StringUtil.isNotEmpty(ciLabelInfo.getCreateUserId()) && !"-1".equals(ciLabelInfo.getCreateUserId())) {
            boolean sqlPage = PrivilegeServiceUtil.isAdminUser(ciLabelInfo.getCreateUserId());
            if(sqlPage) {
                sqls.append(" and (");
                sqls.append(" (f.sort_num = 2 and g.approve_role_type = 2)");
                sqls.append(" or t.create_user_id= :createUserId )");
            } else {
                sqls.append(" and t.create_user_id= :createUserId");
            }
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getLabelName())) {
            sqls.append(" and (LOWER(t.label_name) like :labelName escape \'|\' or LOWER(t.create_desc) like :createDesc escape \'|\' or LOWER(t.busi_caliber) like :busiCaliber escape \'|\') ");
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getDataStatusId()) && -1 != ciLabelInfo.getDataStatusId().intValue()) {
            sqls.append(" and t.data_status_id= :dataStatusId");
        }

        if(StringUtil.isNotEmpty(ciLabelInfo.getIsStatUserNum())) {
            if(ciLabelInfo.getIsStatUserNum().equals(String.valueOf(1))) {
                sqls.append(" and (ext.is_stat_user_num= :isStatUserNum or ext.is_stat_user_num is null)");
            } else {
                sqls.append(" and ext.is_stat_user_num= :isStatUserNum");
            }
        }

        sqls.append(" order by t.create_time desc");
        this.log.debug("标签分页查询：" + sqls);
        String sqlPage1 = this.getDataBaseAdapter().getPagedSql(sqls.toString(), currPage, pageSize);
        List list = this.getSimpleJdbcTemplate().query(sqlPage1, ParameterizedBeanPropertyRowMapper.newInstance(CiLabelInfo.class), new BeanPropertySqlParameterSource(ciLabelInfoClone));
        return list;
    }

    public List<CiLabelInfo> selectEffectAndPublAndNoEffectLabel() throws Exception {
        StringBuilder sqls = new StringBuilder();
        sqls.append("select t.*,ext.*").append(" from ").append(" ci_label_info t").append(" left join ci_label_ext_info ext on t.label_id = ext.label_id ").append(" left join ci_approve_status e on ").append(this.getDataBaseAdapter().getIntToChar("t.label_id")).append("  = e.resource_id ").append(" left join dim_approve_status f on e.curr_approve_status_id = f.approve_status_id").append(" left join dim_approve_level g on g.approve_role_id = f.approve_role_id").append(" where  t.data_status_id = 2").append(" or (t.data_status_id = 1 and f.sort_num = 2 and g.approve_role_type = 2)");
        sqls.append(" order by t.create_time asc");
        this.log.debug("所有有效标签或者未生效但已发布标签列表的查询：" + sqls);
        return this.getSimpleJdbcTemplate().query(sqls.toString(), new ParameterizedBeanPropertyRowMapper() {
            public CiLabelInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                CiLabelInfo info = new CiLabelInfo();
                CiLabelExtInfo extInfo = new CiLabelExtInfo();
                extInfo.setLabelId(Integer.valueOf(rs.getInt("LABEL_ID")));
                extInfo.setIsStatUserNum(Integer.valueOf(rs.getInt("IS_STAT_USER_NUM")));
                extInfo.setLabelLevel(Integer.valueOf(rs.getInt("LABEL_LEVEL")));
                extInfo.setCtrlTypeId(rs.getString("CTRL_TYPE_ID"));
                extInfo.setMaxVal(Double.valueOf(rs.getDouble("MAX_VAL")));
                extInfo.setMinVal(Double.valueOf(rs.getDouble("MIN_VAL")));
                extInfo.setIsLeaf(Integer.valueOf(rs.getInt("IS_LEAF")));
                extInfo.setCustomNum(Integer.valueOf(rs.getInt("CUSTOM_NUM")));
                extInfo.setAttrVal(rs.getString("ATTR_VAL"));
                info.setCiLabelExtInfo(extInfo);
                info.setLabelId(Integer.valueOf(rs.getInt("LABEL_ID")));
                info.setLabelName(rs.getString("LABEL_NAME"));
                info.setParentId(Integer.valueOf(rs.getInt("PARENT_ID")));
                info.setLabelTypeId(Integer.valueOf(rs.getInt("LABEL_TYPE_ID")));
                info.setEffecTime(rs.getDate("EFFEC_TIME"));
                info.setFailTime(rs.getDate("FAIL_TIME"));
                return info;
            }
        }, new Object[0]);
    }

    public CiLabelInfo selectCiLabelInfoByLabelId(Integer labelId) {
        CiLabelInfo ciLabelInfo = null;
        String sql = "SELECT * FROM CI_LABEL_INFO WHERE  LABEL_ID = ? ";
        this.log.debug("sql:" + sql);
        List ciLabelInfoList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiLabelInfo.class), new Object[]{labelId});
        if(null != ciLabelInfoList && ciLabelInfoList.size() > 0) {
            ciLabelInfo = (CiLabelInfo)ciLabelInfoList.get(0);
        }

        return ciLabelInfo;
    }

    public List<Map<String, Object>> findLabelAllEnumValue(String dimValueCol, String dimTableName) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select ").append(dimValueCol).append(" ENUM_NAME from ").append(dimTableName);
        String sql = sqlBuf.toString();
        this.log.debug("查询枚举标签的枚举值sql：" + sql);
        List enumTables = this.getSimpleJdbcTemplate().queryForList(sql, new Object[0]);
        return enumTables;
    }

    public List<CiLabelInfo> findRecommendLabelInfoList() {
        StringBuffer sqlB = new StringBuffer("select T1.*,case when T2.useTimes is null then 0 else   T2.useTimes end useTimes from ");
        sqlB.append("( select T.* from CI_LABEL_INFO T,ci_custom_label_scene_rel TT  where T.label_id = tt.label_id ) T1 ");
        sqlB.append("  left join  ");
        sqlB.append("(select count(1) useTimes,label_id from ci_user_use_label group by label_id) T2 ");
        sqlB.append("on T1.label_id = T2.label_id ");
        List list = this.getSimpleJdbcTemplate().query(sqlB.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelInfo.class), new Object[0]);
        return list;
    }

    public List<CiLabelInfo> selectEffectLabel() throws Exception {
        StringBuilder sqls = new StringBuilder();
        sqls.append("select t.*,ext.*").append(" from ").append(" ci_label_info t").append(" left join ci_label_ext_info ext on t.label_id = ext.label_id ").append(" left join ci_approve_status e on ").append(this.getDataBaseAdapter().getIntToChar("t.label_id")).append("  = e.resource_id ").append(" left join dim_approve_status f on e.curr_approve_status_id = f.approve_status_id").append(" left join dim_approve_level g on g.approve_role_id = f.approve_role_id").append(" where  t.data_status_id = 2");
        sqls.append(" order by t.create_time asc");
        this.log.debug("所有有效标签列表的查询：" + sqls);
        return this.getSimpleJdbcTemplate().query(sqls.toString(), new ParameterizedBeanPropertyRowMapper() {
            public CiLabelInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                CiLabelInfo info = new CiLabelInfo();
                CiLabelExtInfo extInfo = new CiLabelExtInfo();
                extInfo.setLabelId(Integer.valueOf(rs.getInt("LABEL_ID")));
                extInfo.setIsStatUserNum(Integer.valueOf(rs.getInt("IS_STAT_USER_NUM")));
                extInfo.setLabelLevel(Integer.valueOf(rs.getInt("LABEL_LEVEL")));
                extInfo.setCtrlTypeId(rs.getString("CTRL_TYPE_ID"));
                extInfo.setMaxVal(Double.valueOf(rs.getDouble("MAX_VAL")));
                extInfo.setMinVal(Double.valueOf(rs.getDouble("MIN_VAL")));
                extInfo.setIsLeaf(Integer.valueOf(rs.getInt("IS_LEAF")));
                extInfo.setCustomNum(Integer.valueOf(rs.getInt("CUSTOM_NUM")));
                extInfo.setAttrVal(rs.getString("ATTR_VAL"));
                info.setCiLabelExtInfo(extInfo);
                info.setLabelId(Integer.valueOf(rs.getInt("LABEL_ID")));
                info.setLabelName(rs.getString("LABEL_NAME"));
                info.setParentId(Integer.valueOf(rs.getInt("PARENT_ID")));
                info.setLabelTypeId(Integer.valueOf(rs.getInt("LABEL_TYPE_ID")));
                String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
                if(dbType.equals("MYSQL")) {
                    info.setEffecTime(rs.getTimestamp("EFFEC_TIME"));
                    info.setFailTime(rs.getTimestamp("FAIL_TIME"));
                } else {
                    info.setEffecTime(rs.getDate("EFFEC_TIME"));
                    info.setFailTime(rs.getDate("FAIL_TIME"));
                }

                return info;
            }
        }, new Object[0]);
    }

    public List<Map<String, Object>> getAllDimDataByDefine(DimTableDefine define, String enumId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM ( ");
        sql.append(" SELECT ").append(define.getDimCodeCol()).append("  V_KEY, ").append(define.getDimValueCol()).append("  V_NAME ");
        sql.append(" FROM ").append(define.getDimTablename());
        if(StringUtil.isNotEmpty(enumId)) {
            if(!define.getDimCodeColType().equalsIgnoreCase("char") && !define.getDimCodeColType().startsWith("char")) {
                sql.append(" WHERE  " + define.getDimCodeCol() + " = " + enumId);
            } else {
                sql.append(" WHERE  " + define.getDimCodeCol() + " = \'" + enumId + "\'");
            }
        }

        String dbType = Configure.getInstance().getProperty("CI_DBTYPE");
        if(!"POSTGRESQL".equalsIgnoreCase(dbType) && !"MYSQL".equalsIgnoreCase(dbType)) {
            sql.append(" ) ");
        } else {
            sql.append(" ) temp_vip_level ");
        }

        sql.append(" ORDER BY ").append(" V_KEY ");
        this.log.debug("getAllDimDataByDefine-->" + sql.toString());
        List result = null;

        try {
            result = this.getSimpleJdbcTemplate().queryForList(sql.toString(), new Object[0]);
        } catch (Exception var7) {
            this.log.error("获得维表" + define.getDimTablename() + "中所有值失败", var7);
        }

        return result;
    }
}

package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICustomGroupInfoExternalJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.base.CustomGroupInfo;
import com.ailk.biapp.ci.entity.base.CustomListInfo;
import com.ailk.biapp.ci.entity.base.SysInfo;
import com.ailk.biapp.ci.model.Pager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CustomGroupInfoExternalJDaoImpl extends JdbcBaseDao implements ICustomGroupInfoExternalJDao {
    private Logger log = Logger.getLogger(CustomGroupInfoExternalJDaoImpl.class);

    public CustomGroupInfoExternalJDaoImpl() {
    }

    public List<CustomGroupInfo> getCustomsersList(SysInfo sysInfo, Pager pager) {
        StringBuffer sql = new StringBuffer();
        HashMap paramMap = new HashMap();
        paramMap.put("userId", sysInfo.getUserId());
        paramMap.put("sysId", sysInfo.getSysId());
        sql.append("SELECT CUSTOM_GROUP_ID,CUSTOM_GROUP_NAME,CUSTOM_GROUP_DESC,UPDATE_CYCLE,LABEL_OPT_RULE_SHOW,START_DATE,END_DATE,CUSTOM_NUM,MONTH_LABEL_DATE,DAY_LABEL_DATE,DUPLICATE_NUM ");
        sql.append("FROM CI_CUSTOM_GROUP_INFO where STATUS = ").append(1).append(" AND CREATE_USER_ID=:userId and SYS_ID=:sysId");
        sql.append(" ORDER BY NEW_MODIFY_TIME DESC");
        String sqlPage;
        if(pager.getPageSize() != 0) {
            sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), pager.getPageNum(), pager.getPageSize());
        } else {
            sqlPage = sql.toString();
        }

        this.log.debug("sql=" + sqlPage);
        Object list = new ArrayList();

        try {
            list = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CustomGroupInfo.class), paramMap);
        } catch (Exception var8) {
            this.log.error(sysInfo.getSysId() + sysInfo.getSysName() + "调用接口getCustomsersList报错", var8);
        }

        return (List)list;
    }

    public int getCustomsersListCount(SysInfo sysInfo) {
        StringBuffer sql = new StringBuffer();
        HashMap paramMap = new HashMap();
        paramMap.put("userId", sysInfo.getUserId());
        paramMap.put("sysId", sysInfo.getSysId());
        sql.append("SELECT COUNT(CUSTOM_GROUP_ID) FROM CI_CUSTOM_GROUP_INFO where STATUS = ").append(1).append(" AND CREATE_USER_ID=:userId and SYS_ID=:sysId");
        this.log.debug("sql=" + sql.toString());
        int totalCount = 0;

        try {
            totalCount = this.getSimpleJdbcTemplate().queryForInt(sql.toString(), paramMap);
        } catch (Exception var6) {
            this.log.error(sysInfo.getSysId() + sysInfo.getSysName() + "查询客户群总数报错", var6);
        }

        return totalCount;
    }

    public CustomGroupInfo selectCustomGroupById(String customGroupId) {
        HashMap paramMap = new HashMap();
        paramMap.put("customGroupId", customGroupId);
        StringBuffer sql = new StringBuffer();
        sql.append("select CUSTOM_GROUP_ID,CUSTOM_GROUP_NAME,CUSTOM_GROUP_DESC,UPDATE_CYCLE,LABEL_OPT_RULE_SHOW,START_DATE,END_DATE,CUSTOM_NUM,MONTH_LABEL_DATE,DAY_LABEL_DATE,DUPLICATE_NUM ");
        sql.append("from CI_CUSTOM_GROUP_INFO where STATUS = ").append(1).append(" AND CUSTOM_GROUP_ID = :customGroupId");
        this.log.debug("selectCustomGroupById--" + sql);
        List list = null;
        CustomGroupInfo info = null;

        try {
            list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CustomGroupInfo.class), paramMap);
            if(list != null && list.size() > 0) {
                info = (CustomGroupInfo)list.get(0);
            }
        } catch (Exception var7) {
            this.log.error("调用接口selectCustomGroupById报错", var7);
        }

        return info;
    }

    public List<CustomListInfo> queryCustomListByCustomId(String customGroupId) throws Exception {
        HashMap paramMap = new HashMap();
        paramMap.put("customGroupId", customGroupId);
        StringBuffer sql = new StringBuffer();
        sql.append("select LIST_TABLE_NAME,DATA_DATE,CUSTOM_NUM,DUPLICATE_NUM ");
        sql.append("from CI_CUSTOM_LIST_INFO where ").append("CUSTOM_GROUP_ID = :customGroupId");
        this.log.debug("queryCustomListByCustomId--" + sql);
        List list = null;

        try {
            list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CustomListInfo.class), paramMap);
        } catch (Exception var6) {
            this.log.error("调用接口queryCustomListByCustomId报错", var6);
        }

        return list;
    }

    public List<SysInfo> querySysInfos() throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("select SYS_ID,SYS_NAME from CI_SYS_INFO");
        this.log.debug("querySysInfos--" + sql);
        List list = null;

        try {
            list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(SysInfo.class), new Object[0]);
        } catch (Exception var4) {
            this.log.error("调用接口querySysInfos报错", var4);
        }

        return list;
    }
}

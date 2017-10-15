package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiUserCustomRelJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.model.CiCustomRelModel;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.ailk.biapp.ci.util.DateUtil;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiUserCustomRelJDaoImpl extends JdbcBaseDao implements ICiUserCustomRelJDao {
    private Logger log = Logger.getLogger(this.getClass());
    private static final String USER_LABEL_REL_TABLE = " CI_USER_CUSTOM_REL";

    public CiUserCustomRelJDaoImpl() {
    }

    public List<CiCustomRelModel> selectRelCustom(String userId, CiCustomRelModel ciCustomRelModel) throws Exception {
        List list = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT C.CUSTOM_GROUP_ID AS CUSTOM_GROUP_ID,C.ASSOCI_LABEL_ID AS REL_LABEL_ID FROM ").append(" CI_USER_CUSTOM_REL").append(" C ").append(" WHERE C.STATUS = 1 AND C.CUSTOM_GROUP_ID = \'").append(ciCustomRelModel.getCustomGroupId() + "\'").append(" AND C.USER_ID = \'").append(userId).append("\'");
        this.log.debug("selectRelCustom sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomRelModel.class), new Object[0]);
        return list;
    }

    public void deleteRelCustom(String userId, String customGroupId, String relLabelIds) throws Exception {
        if(!"".equals(relLabelIds) && relLabelIds.endsWith(",")) {
            relLabelIds = relLabelIds.substring(0, relLabelIds.length() - 1);
            String[] sql = relLabelIds.split(",");
            StringBuffer dbType = new StringBuffer();
            String[] dataBaseAdapter = sql;
            int len$ = sql.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String labelId = dataBaseAdapter[i$];
                dbType.append("\'").append(labelId).append("\'").append(",");
            }

            relLabelIds = dbType.substring(0, dbType.length() - 1);
        }

        StringBuffer var10 = new StringBuffer();
        String var11 = Configure.getInstance().getProperty("CI_DBTYPE");
        DataBaseAdapter var12 = new DataBaseAdapter(var11);
        var10.append(" UPDATE ").append(" CI_USER_CUSTOM_REL").append(" SET STATUS = ").append(ServiceConstants.PUBLIC_STATUS_DEL).append(", DEL_TIME = ").append(var12.getTimeStamp(DateUtil.getCurrentDay().substring(0, 10))).append(" WHERE USER_ID = \'").append(userId).append("\' ").append(" AND CUSTOM_GROUP_ID = \'").append(customGroupId).append("\' AND ASSOCI_LABEL_ID NOT IN (").append(relLabelIds).append(") ");
        this.log.debug("deleteUserLabelRel sql : " + var10);
        this.getSimpleJdbcTemplate().update(var10.toString(), new Object[0]);
    }
}

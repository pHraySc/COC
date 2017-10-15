package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICustomersCompareAnalysisJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiUserCustomContrastId;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CustomersCompareAnalysisJDaoImpl extends JdbcBaseDao implements ICustomersCompareAnalysisJDao {
    private Logger log = Logger.getLogger(CustomersCompareAnalysisJDaoImpl.class);

    public CustomersCompareAnalysisJDaoImpl() {
    }

    public List<LabelShortInfo> getCustomersCompareAnalysis(String customId, String userId, String dataDate) throws Exception {
        String labelStatTableName = DateUtil.getLabelStatTableName(dataDate);
        String customNum = this.getDataBaseAdapter().getNvl("t2.CUSTOM_NUM", "0");
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT t1.UPDATE_CYCLE,t1.CONTRAST_LABEL_ID as LABEL_ID,t1.LABEL_NAME," + customNum + " AS CUSTOM_NUM,t2.DATA_DATE ").append(" FROM").append(" (SELECT r.CONTRAST_LABEL_ID,i.LABEL_NAME,i.UPDATE_CYCLE").append(" FROM").append(" CI_USER_CUSTOM_CONTRAST r,CI_LABEL_INFO i").append(" WHERE r.CONTRAST_LABEL_ID = ").append(this.getDataBaseAdapter().getIntToChar("i.LABEL_ID")).append(" AND r.STATUS=1 AND r.CUSTOM_GROUP_ID =:customGroupId AND").append(" r.USER_ID=:userId ORDER BY i.CREATE_TIME)").append(" t1").append(" LEFT JOIN");
        boolean hasAuthority = false;
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        if(needAuthority && StringUtil.isNotEmpty(userId)) {
            hasAuthority = needAuthority && !PrivilegeServiceUtil.isAdminUser(userId) && !PrivilegeServiceUtil.isProvinceUser(userId);
        }

        if(hasAuthority) {
            List queryId = PrivilegeServiceUtil.getUserCityIds(userId);
            if(queryId != null && queryId.size() > 0) {
                String cityStrs;
                if(queryId.size() == 1) {
                    sql.append("( SELECT n.LABEL_ID,n.CUSTOM_NUM,n.DATA_DATE FROM " + labelStatTableName + " n WHERE");
                    cityStrs = (String)queryId.get(0);
                    sql.append(" n.CITY_ID = ").append(cityStrs).append(" AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.DATA_DATE=\'" + dataDate + "\' ORDER BY n.LABEL_ID) t2");
                } else {
                    sql.append("( SELECT n.LABEL_ID, n.DATA_DATE, sum(n.CUSTOM_NUM) AS CUSTOM_NUM FROM " + labelStatTableName + " n WHERE");
                    cityStrs = "";

                    for(int i = 0; i < queryId.size(); ++i) {
                        cityStrs = cityStrs + (String)queryId.get(i) + (i == queryId.size() - 1?"":",");
                    }

                    sql.append(" n.CITY_ID IN (").append(cityStrs).append(")").append(" AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.DATA_DATE=\'" + dataDate + "\' GROUP BY n.LABEL_ID, n.DATA_DATE ORDER BY n.LABEL_ID) t2");
                }
            }
        } else {
            sql.append("( SELECT n.LABEL_ID,n.CUSTOM_NUM,n.DATA_DATE FROM " + labelStatTableName + " n WHERE n.CITY_ID=-1 AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.DATA_DATE=\'" + dataDate + "\' ORDER BY n.LABEL_ID) t2");
        }

        sql.append(" ON t1.CONTRAST_LABEL_ID = ").append(this.getBackDataBaseAdapter().getIntToChar("t2.LABEL_ID"));
        this.log.debug("客户群对比分析回显sql: " + sql.toString());
        CiUserCustomContrastId var13 = new CiUserCustomContrastId();
        var13.setCustomGroupId(customId);
        var13.setUserId(userId);
        List results = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), new BeanPropertySqlParameterSource(var13));
        return results;
    }
}

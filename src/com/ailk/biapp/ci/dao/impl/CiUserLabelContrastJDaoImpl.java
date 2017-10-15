package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiUserLabelContrastJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiUserLabelContrastId;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository("ciUserLabelContrastJDao")
public class CiUserLabelContrastJDaoImpl extends JdbcBaseDao implements ICiUserLabelContrastJDao {
    private static Logger log = Logger.getLogger(CiUserLabelContrastJDaoImpl.class);

    public CiUserLabelContrastJDaoImpl() {
    }

    public List<LabelShortInfo> getCiUserLabelContrastList(String userId, String labelId) {
        String sql = " SELECT l.LABEL_ID,l.LABEL_NAME FROM CI_LABEL_INFO l,  (SELECT r.CONTRAST_LABEL_ID FROM CI_USER_LABEL_CONTRAST r WHERE r.STATUS=1 AND r.LABEL_ID =:labelId AND r.USER_ID=:userId ) t  WHERE l.LABEL_ID = t.CONTRAST_LABEL_ID ";
        CiUserLabelContrastId queryId = new CiUserLabelContrastId();
        queryId.setLabelId(labelId);
        queryId.setUserId(userId);
        List results = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), new BeanPropertySqlParameterSource(queryId));
        return results;
    }

    public List<LabelShortInfo> getLabelContrastRecommend(int currPage, int pageSize, String labelId) {
        String sql = " SELECT c.LABEL_ID,c.UPDATE_CYCLE,c.LABEL_NAME,c.TIMES FROM (  SELECT l.LABEL_ID,l.UPDATE_CYCLE,l.LABEL_NAME,COUNT(*) AS TIMES FROM CI_LABEL_INFO l,  (SELECT r.CONTRAST_LABEL_ID FROM CI_USER_LABEL_CONTRAST r WHERE r.STATUS=1 AND r.LABEL_ID =:labelId) t  WHERE l.LABEL_ID = t.CONTRAST_LABEL_ID AND l.DATA_STATUS_ID=2 GROUP BY LABEL_ID,LABEL_NAME,UPDATE_CYCLE  ) c  ORDER BY TIMES DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        log.debug(sqlPag);
        CiUserLabelContrastId queryId = new CiUserLabelContrastId();
        queryId.setLabelId(labelId);
        List results = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), new BeanPropertySqlParameterSource(queryId));
        return results;
    }

    public List<LabelShortInfo> getLabelContrastRecommend(int currPage, int pageSize, String labelId, String queryLabelName) {
        String sql = " SELECT c.LABEL_ID,c.UPDATE_CYCLE,c.LABEL_NAME,c.TIMES FROM (  SELECT l.LABEL_ID,l.UPDATE_CYCLE,l.LABEL_NAME,COUNT(*) AS TIMES FROM CI_LABEL_INFO l,  (SELECT r.CONTRAST_LABEL_ID FROM CI_USER_LABEL_CONTRAST r WHERE r.STATUS=1 AND r.LABEL_ID =:labelId) t  WHERE l.LABEL_ID = t.CONTRAST_LABEL_ID AND l.DATA_STATUS_ID=2 AND l.LABEL_NAME like :queryLabelName GROUP BY LABEL_ID,LABEL_NAME,UPDATE_CYCLE  ) c  ORDER BY TIMES DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        log.debug(sqlPag);
        HashMap queryParam = new HashMap();
        queryParam.put("labelId", labelId);
        queryParam.put("queryLabelName", "%" + queryLabelName + "%");
        List results = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), queryParam);
        return results;
    }

    public List<LabelShortInfo> getCiUserLabelContrastList(String userId, String labelId, String dataDate) {
        List results = null;

        try {
            int e = CacheBase.getInstance().getEffectiveLabel(labelId).getUpdateCycle().intValue();
            String tableName = DateUtil.getLabelTrendTableName(e);
            String customNum = this.getDataBaseAdapter().getNvl("t2.CUSTOM_NUM", "0");
            StringBuffer sqlBuffer = new StringBuffer();
            String sqlFront = " SELECT t1.CONTRAST_LABEL_ID as LABEL_ID,t1.UPDATE_CYCLE,t1.LABEL_NAME," + customNum + " as CUSTOM_NUM,t2.DATA_DATE FROM " + " ( SELECT r.CONTRAST_LABEL_ID,i.UPDATE_CYCLE,i.LABEL_NAME FROM CI_USER_LABEL_CONTRAST r,CI_LABEL_INFO i WHERE r.CONTRAST_LABEL_ID=i.LABEL_ID and r.STATUS=1 AND r.LABEL_ID =:labelId AND r.USER_ID=:userId ORDER BY r.CREATE_TIME) t1" + " LEFT JOIN ";
            String sqlMiddle = "( SELECT n.LABEL_ID,n.CUSTOM_NUM,n.DATA_DATE FROM " + tableName + " n WHERE n.CITY_ID=-1 AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.DATA_DATE=\'" + dataDate + "\' ORDER BY n.LABEL_ID) t2";
            String sqlEnd = " ON t1.CONTRAST_LABEL_ID = t2.LABEL_ID ";
            boolean hasAuthority = false;
            boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            if(needAuthority && StringUtil.isNotEmpty(userId)) {
                hasAuthority = needAuthority && !PrivilegeServiceUtil.isAdminUser(userId) && !PrivilegeServiceUtil.isProvinceUser(userId);
            }

            sqlBuffer.append(sqlFront);
            String sql;
            if(!hasAuthority) {
                sqlBuffer.append(sqlMiddle);
            } else {
                List queryId = PrivilegeServiceUtil.getUserCityIds(userId);
                if(queryId != null && queryId.size() > 0) {
                    if(queryId.size() == 1) {
                        sqlBuffer.append("( SELECT n.LABEL_ID,n.CUSTOM_NUM,n.DATA_DATE FROM " + tableName + " n WHERE");
                        sql = (String)queryId.get(0);
                        sqlBuffer.append(" n.CITY_ID = ").append(sql).append(" AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.DATA_DATE=\'" + dataDate + "\' ORDER BY n.LABEL_ID) t2");
                    } else {
                        sqlBuffer.append("( SELECT n.LABEL_ID, n.DATA_DATE, sum(n.CUSTOM_NUM) AS CUSTOM_NUM FROM " + tableName + " n WHERE");
                        sql = "";

                        for(int i = 0; i < queryId.size(); ++i) {
                            sql = sql + (String)queryId.get(i) + (i == queryId.size() - 1?"":",");
                        }

                        sqlBuffer.append(" n.CITY_ID IN (").append(sql).append(")").append(" AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.DATA_DATE=\'" + dataDate + "\' GROUP BY n.LABEL_ID, n.DATA_DATE ORDER BY n.LABEL_ID) t2");
                    }
                }
            }

            sqlBuffer.append(sqlEnd);
            CiUserLabelContrastId var18 = new CiUserLabelContrastId();
            var18.setLabelId(labelId);
            var18.setUserId(userId);
            sql = sqlBuffer.toString();
            log.debug("对比分析初始化sql： " + sql);
            results = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), new BeanPropertySqlParameterSource(var18));
        } catch (Exception var17) {
            log.error("获取用户权限错误", var17);
        }

        return results;
    }

    public int getCountUserLabelContrast(String userId, String labelId) {
        String sql = " SELECT COUNT(*) FROM CI_USER_LABEL_CONTRAST WHERE USER_ID=:userId AND LABEL_ID=:labelId AND STATUS=1 ";
        CiUserLabelContrastId queryId = new CiUserLabelContrastId();
        queryId.setLabelId(labelId);
        queryId.setUserId(userId);
        int count = this.getSimpleJdbcTemplate().queryForInt(sql, new BeanPropertySqlParameterSource(queryId));
        return count;
    }
}

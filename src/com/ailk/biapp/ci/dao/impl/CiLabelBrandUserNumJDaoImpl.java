package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelBrandUserNumJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiLabelBrandUserNum;
import com.ailk.biapp.ci.entity.CiLabelBrandUserNumId;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.model.LabelTrendTableInfo;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository("ciLabelBrandUserNumJDao")
public class CiLabelBrandUserNumJDaoImpl extends JdbcBaseDao implements ICiLabelBrandUserNumJDao {
    private static Logger log = Logger.getLogger(CiLabelBrandUserNumJDaoImpl.class);

    public CiLabelBrandUserNumJDaoImpl() {
    }

    public String getLabelBrandUserNumById(String labelId, String dataDate) {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        String sql = " SELECT * FROM " + tableName + " WHERE CITY_ID=-1 AND VIP_LEVEL_ID=-1 AND BRAND_ID=-1 " + " AND LABEL_ID=:labelId and DATA_DATE=:dataDate ";
        CiLabelBrandUserNumId queryBean = new CiLabelBrandUserNumId();
        queryBean.setLabelId(new Integer(labelId));
        queryBean.setDataDate(dataDate);
        String count = "-";
        List entityList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiLabelBrandUserNum.class), new BeanPropertySqlParameterSource(queryBean));
        if(null != entityList && entityList.size() > 0) {
            CiLabelBrandUserNum entity = (CiLabelBrandUserNum)entityList.get(0);
            log.debug(entity);
            count = entity.getCustomNum().toString();
        }

        return count;
    }

    public LabelShortInfo getLabelBrandUserNum(String labelId, String dataDate) {
        String customNum = this.getDataBaseAdapter().getNvl("t2.CUSTOM_NUM", "0");
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        String sql = " SELECT t1.LABEL_ID,t1.LABEL_NAME,t2.DATA_DATE," + customNum + " AS CUSTOM_NUM FROM " + " (SELECT i.LABEL_ID,i.LABEL_NAME FROM CI_LABEL_INFO i WHERE i.LABEL_ID=:labelId) t1 LEFT JOIN " + " (SELECT n.LABEL_ID,n.CUSTOM_NUM,n.DATA_DATE FROM " + tableName + " n WHERE n.CITY_ID=-1 AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.LABEL_ID=:labelId and n.DATA_DATE=:dataDate) t2" + " ON t1.LABEL_ID = t2.LABEL_ID ";
        CiLabelBrandUserNumId queryBean = new CiLabelBrandUserNumId();
        queryBean.setLabelId(new Integer(labelId));
        queryBean.setDataDate(dataDate);
        LabelShortInfo labelInfo = null;
        List entityList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelShortInfo.class), new BeanPropertySqlParameterSource(queryBean));
        if(null != entityList && entityList.size() > 0) {
            labelInfo = (LabelShortInfo)entityList.get(0);
        }

        return labelInfo;
    }

    public List<LabelTrendInfo> getLabelUserNumsTrendInfo(int currPage, int pageSize, String labelId) {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        String sql = " SELECT LABEL_ID,DATA_DATE,CUSTOM_NUM AS VALUE FROM " + tableName + " WHERE CITY_ID=-1 AND VIP_LEVEL_ID=-1 AND BRAND_ID=-1 AND LABEL_ID=? " + " ORDER BY DATA_DATE DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        List labelTrendList = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendInfo.class), new Object[]{labelId});
        return labelTrendList;
    }

    /** @deprecated */
    @Deprecated
    public List<LabelTrendTableInfo> getLabelTrendTableInfo(int currPage, int pageSize, String labelId) {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        String sql = " SELECT n.LABEL_ID,n.DATA_DATE,n.CUSTOM_NUM,s.USE_TIMES FROM " + tableName + " n  " + " LEFT JOIN CI_LABEL_USE_STAT s ON n.DATA_DATE = s.DATA_DATE " + " AND n.CITY_ID=-1 AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND s.LABEL_USE_TYPE_ID=-1 AND n.LABEL_ID=?" + " ORDER BY n.DATA_DATE DESC ";
        String sqlPag = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        List labelTrendTableList = this.getSimpleJdbcTemplate().query(sqlPag, ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendTableInfo.class), new Object[]{labelId});
        return labelTrendTableList;
    }

    public List<LabelTrendInfo> getLabelUserNumsTrendInfo(String labelId, String dataDate, String labelType) {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        String userId = PrivilegeServiceUtil.getUserId();
        StringBuffer sql = new StringBuffer("SELECT LABEL_ID,DATA_DATE,sum(CUSTOM_NUM) AS VALUE FROM ");

        try {
            boolean labelTrendList = PrivilegeServiceUtil.isAdminUser(userId);
            boolean startMonth = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(userId);
            sql.append(tableName).append(" where 1 = 1 ");
            if(startMonth && !labelTrendList && !isProvinceUser) {
                List cityList = PrivilegeServiceUtil.getUserCityIds(userId);
                if(cityList == null || cityList.size() <= 0) {
                    throw new Exception("用户没有任何权限！");
                }

                String cityStrs;
                if(cityList.size() == 1) {
                    cityStrs = (String)cityList.get(0);
                    sql.append(" AND CITY_ID =  ").append(cityStrs);
                } else {
                    cityStrs = "";

                    for(int i = 0; i < cityList.size(); ++i) {
                        cityStrs = cityStrs + (String)cityList.get(i) + (i == cityList.size() - 1?"":",");
                    }

                    sql.append(" AND CITY_ID IN (").append(cityStrs).append(") ");
                }
            } else {
                sql.append(" AND CITY_ID =  ").append(-1);
            }
        } catch (Exception var14) {
            var14.printStackTrace();
        }

        sql.append(" AND VIP_LEVEL_ID=-1 AND BRAND_ID=-1 AND LABEL_ID=? ");
        String var15;
        if(labelType.equals("1")) {
            var15 = DateUtil.getFrontDay(31, dataDate);
            log.debug("startDay->" + var15 + ",endDay->" + dataDate);
            sql.append(" AND DATA_DATE>=\'" + var15 + "\' AND DATA_DATE<=\'" + dataDate + "\' group by LABEL_ID,DATA_DATE ORDER BY DATA_DATE ASC ");
        } else {
            var15 = dataDate.substring(0, 6);
            String var17 = DateUtil.getFrontMonth(12, var15);
            log.debug("startMonth->" + var17 + ",endMonth->" + var15);
            sql.append(" AND DATA_DATE>=\'" + var17 + "\' AND DATA_DATE<=\'" + var15 + "\' group by LABEL_ID,DATA_DATE ORDER BY DATA_DATE ASC ");
        }

        log.debug("SQL->" + sql.toString());
        List var16 = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendInfo.class), new Object[]{labelId});
        return var16;
    }

    public List<LabelTrendTableInfo> getLabelTrendTableInfo(int currPage, int pageSize, String labelId, String dataDate, String labelType) {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        String userId = PrivilegeServiceUtil.getUserId();
        StringBuffer sqlBuf = new StringBuffer();
        String user_times = this.getDataBaseAdapter().getNvl("sum(s.USE_TIMES)", "0");
        sqlBuf.append(" SELECT t.LABEL_ID,t.DATA_DATE,sum(t.CUSTOM_NUM) CUSTOM_NUM," + user_times + " USE_TIMES FROM ");
        HashMap queryParam = new HashMap();
        queryParam.put("labelId", labelId);
        StringBuffer citySql = new StringBuffer("1 = 1");

        try {
            boolean sqlPag = PrivilegeServiceUtil.isAdminUser(userId);
            boolean labelTrendTableList = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            List cityList = PrivilegeServiceUtil.getUserCityIds(userId);
            boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(userId);
            if(labelTrendTableList && !sqlPag && !isProvinceUser) {
                if(cityList == null || cityList.size() <= 0) {
                    throw new Exception("用户没有任何权限！");
                }

                String cityStrs;
                if(cityList.size() == 1) {
                    cityStrs = (String)cityList.get(0);
                    citySql.append(" AND n.CITY_ID =  ").append(cityStrs);
                } else {
                    cityStrs = "";

                    for(int i = 0; i < cityList.size(); ++i) {
                        cityStrs = cityStrs + (String)cityList.get(i) + (i == cityList.size() - 1?"":",");
                    }

                    citySql.append(" AND n.CITY_ID IN (").append(cityStrs).append(") ");
                }
            } else {
                citySql.append(" AND n.CITY_ID =  ").append(-1);
            }
        } catch (Exception var19) {
            var19.printStackTrace();
        }

        String var20;
        if(labelType.equals("1")) {
            var20 = DateUtil.getFrontDay(31, dataDate);
            log.debug("startDay->" + var20 + ",endDay->" + dataDate);
            sqlBuf.append("( SELECT * FROM " + tableName + " n WHERE  " + citySql + " AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.LABEL_ID=:labelId AND n.DATA_DATE>=\'" + var20 + "\' AND n.DATA_DATE<=\'" + dataDate + "\') t LEFT JOIN (SELECT * FROM CI_LABEL_USE_STAT u WHERE u.LABEL_ID=:labelId ) s ON t.DATA_DATE = s.DATA_DATE group by t.LABEL_ID,t.DATA_DATE,s.USE_TIMES ORDER BY t.DATA_DATE DESC ");
        } else {
            var20 = dataDate.substring(0, 6);
            String var21 = DateUtil.getFrontMonth(12, var20);
            log.debug("startMonth->" + var21 + ",endMonth->" + var20);
            sqlBuf.append("( SELECT * FROM " + tableName + " n WHERE  " + citySql + " AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.LABEL_ID=:labelId AND n.DATA_DATE>=\'" + var21 + "\' AND n.DATA_DATE<=\'" + var20 + "\') t LEFT JOIN (SELECT * FROM CI_LABEL_USE_STAT u WHERE u.LABEL_ID=:labelId ) s ON t.DATA_DATE = s.DATA_DATE group by t.LABEL_ID,t.DATA_DATE ORDER BY t.DATA_DATE DESC ");
        }

        var20 = this.getDataBaseAdapter().getPagedSql(sqlBuf.toString(), currPage, pageSize);
        log.debug("SQL->" + var20);
        List var22 = this.getSimpleJdbcTemplate().query(var20, ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendTableInfo.class), queryParam);
        return var22;
    }

    public long getLabelTrednTableInfoNums(String labelId, String dataDate, String labelType) {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(labelId).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT COUNT(*) FROM ( ");
        sqlBuf.append(" SELECT t.LABEL_ID,t.DATA_DATE,t.CUSTOM_NUM,s.USE_TIMES FROM ");
        HashMap queryParam = new HashMap();
        queryParam.put("labelId", labelId);
        String count;
        if(labelType.equals("1")) {
            count = DateUtil.getFrontDay(31, dataDate);
            log.debug("startDay->" + count + ",endDay->" + dataDate);
            sqlBuf.append("( SELECT * FROM " + tableName + " n WHERE  n.CITY_ID=-1 AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.LABEL_ID=:labelId AND n.DATA_DATE>\'" + count + "\' AND n.DATA_DATE<=\'" + dataDate + "\') t LEFT JOIN (SELECT * FROM CI_LABEL_USE_STAT u WHERE u.LABEL_ID=:labelId ) s ON t.DATA_DATE = s.DATA_DATE ORDER BY t.DATA_DATE DESC ");
        } else {
            count = dataDate.substring(0, 6);
            String startMonth = DateUtil.getFrontMonth(12, count);
            log.debug("startMonth->" + startMonth + ",endMonth->" + count);
            sqlBuf.append("( SELECT * FROM " + tableName + " n WHERE  n.CITY_ID=-1 AND n.VIP_LEVEL_ID=-1 AND n.BRAND_ID=-1 AND n.LABEL_ID=:labelId AND n.DATA_DATE>\'" + startMonth + "\' AND n.DATA_DATE<=\'" + count + "\') t LEFT JOIN (SELECT * FROM CI_LABEL_USE_STAT u WHERE u.LABEL_ID=:labelId ) s ON t.DATA_DATE = s.DATA_DATE ORDER BY t.DATA_DATE DESC ");
        }

        sqlBuf.append(" ) abc");
        log.debug("SQL->" + sqlBuf.toString());
        long count1 = this.getSimpleJdbcTemplate().queryForLong(sqlBuf.toString(), queryParam);
        return count1;
    }

    public List<CiLabelBrandUserNum> getLabelBrandUserNumListByDataDate(String dataDate) {
        String tableName = DateUtil.getLabelStatTableName(dataDate);
        String sql = " SELECT * FROM " + tableName + " WHERE CITY_ID=-1 AND VIP_LEVEL_ID=-1 AND BRAND_ID=-1 " + " AND DATA_DATE=:dataDate ";
        CiLabelBrandUserNumId queryBean = new CiLabelBrandUserNumId();
        queryBean.setDataDate(dataDate);
        List entityList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiLabelBrandUserNum.class), new BeanPropertySqlParameterSource(queryBean));
        return entityList;
    }

    public List<CiLabelBrandUserNum> getLabelBrandUserNumList(CiLabelBrandUserNum ciLabelBrandUserNum) {
        String tableName = DateUtil.getLabelStatTableName(ciLabelBrandUserNum.getDataDate());
        StringBuilder sql = new StringBuilder(" SELECT * FROM " + tableName + " WHERE 1=1 ");
        if(null != ciLabelBrandUserNum.getLabelId()) {
            sql.append(" and LABEL_ID = :labelId");
        }

        if(null != ciLabelBrandUserNum.getDataDate()) {
            sql.append(" and DATA_DATE = :dataDate");
        }

        if(StringUtils.isNotEmpty(ciLabelBrandUserNum.getDataDate())) {
            sql.append(" and CITY_ID = :cityId");
        }

        if(null != ciLabelBrandUserNum.getBrandId()) {
            sql.append(" and BRAND_ID = :brandId");
        }

        if(null != ciLabelBrandUserNum.getVipLevelId()) {
            sql.append(" and VIP_LEVEL_ID = :vipLevelId");
        }

        List entityList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelBrandUserNum.class), new BeanPropertySqlParameterSource(ciLabelBrandUserNum));
        return entityList;
    }

    public List<CiLabelBrandUserNum> getAllCityLabelBrandUserNumListByDataDate(String dataDate) {
        String tableName = DateUtil.getLabelStatTableName(dataDate);
        String sql = " SELECT * FROM " + tableName + " WHERE VIP_LEVEL_ID=-1 AND BRAND_ID=-1 " + " AND DATA_DATE=:dataDate ";
        CiLabelBrandUserNumId queryBean = new CiLabelBrandUserNumId();
        queryBean.setDataDate(dataDate);
        List entityList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiLabelBrandUserNum.class), new BeanPropertySqlParameterSource(queryBean));
        return entityList;
    }
}

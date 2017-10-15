package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelFormAnalysisJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.DimBrand;
import com.ailk.biapp.ci.entity.DimCity;
import com.ailk.biapp.ci.entity.DimVipLevel;
import com.ailk.biapp.ci.model.CiBrandHisModel;
import com.ailk.biapp.ci.model.CiCityHisModel;
import com.ailk.biapp.ci.model.CiLabelFormModel;
import com.ailk.biapp.ci.model.CiLabelFormTrendModel;
import com.ailk.biapp.ci.model.CiVipHisModel;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelFormAnalysisJDaoImpl extends JdbcBaseDao implements ICiLabelFormAnalysisJDao {
    private Logger log = Logger.getLogger(this.getClass());
    private static final String DIM_BRAND_TABLE = "DIM_BRAND";
    private static final String DIM_VIP_LEVEL_TABLE = "DIM_VIP_LEVEL";
    private static final String DIM_CITY_TABLE = "DIM_CITY";

    public CiLabelFormAnalysisJDaoImpl() {
    }

    public List<CiLabelFormModel> selectBrandFormChartData(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        List list = null;
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            String cityStr = this.getUserCitySqlStr();
            sql.append(" SELECT C.BRAND_ID, SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append(tableName).append(" C ").append(" LEFT JOIN ").append("DIM_BRAND").append(" B ").append(" ON (C.BRAND_ID = B.BRAND_ID) ").append(" WHERE 1=1 ").append(this.getSqlCondition(ciLabelFormModel)).append(" AND C.CITY_ID IN (").append(cityStr).append(") ").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID != ").append(-1).append(" AND B.PARENT_ID = ").append(-1).append(" GROUP BY C.BRAND_ID").append(" ORDER BY C.BRAND_ID");
        } else {
            sql.append(" SELECT C.* FROM ").append(tableName).append(" C ").append(" LEFT JOIN ").append("DIM_BRAND").append(" B ").append(" ON (C.BRAND_ID = B.BRAND_ID) ").append(" WHERE 1=1 ").append(this.getSqlCondition(ciLabelFormModel)).append(" AND C.CITY_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID != ").append(-1).append(" AND B.PARENT_ID = ").append(-1).append(" ORDER BY C.BRAND_ID");
        }

        this.log.debug("selectBrandFormChartData sql:" + sql.toString());
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelFormModel.class), new Object[0]);
        return list;
    }

    public List<CiLabelFormModel> selectSubBrandFormChartData(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        List list = null;
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            String cityStr = this.getUserCitySqlStr();
            sql.append(" SELECT C.BRAND_ID, SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append(tableName).append(" C ").append(" LEFT JOIN ").append("DIM_BRAND").append(" B ").append(" ON (C.BRAND_ID = B.BRAND_ID) ").append(" WHERE 1=1 ").append(this.getSqlCondition(ciLabelFormModel)).append(" AND C.CITY_ID IN (").append(cityStr).append(") ").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID != ").append(-1);
            if(!StringUtil.isEmpty(ciLabelFormModel.getBrandId())) {
                sql.append(" AND B.PARENT_ID = ").append(ciLabelFormModel.getBrandId());
            }

            sql.append(" GROUP BY C.BRAND_ID").append(" ORDER BY C.BRAND_ID");
        } else {
            sql.append(" SELECT C.* FROM ").append(tableName).append(" C ").append(" LEFT JOIN ").append("DIM_BRAND").append(" B ").append(" ON (C.BRAND_ID = B.BRAND_ID) ").append(" WHERE 1=1 ").append(this.getSqlCondition(ciLabelFormModel)).append(" AND C.CITY_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID != ").append(-1);
            if(!StringUtil.isEmpty(ciLabelFormModel.getBrandId())) {
                sql.append(" AND B.PARENT_ID = ").append(ciLabelFormModel.getBrandId());
            }

            sql.append(" ORDER BY C.BRAND_ID");
        }

        this.log.debug("selectSubBrandFormChartData sql:" + sql.toString());
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelFormModel.class), new Object[0]);
        return list;
    }

    public List<CiLabelFormModel> selectCityFormChartData(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        List list = null;
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        String moreLevelProvince = Configure.getInstance().getProperty("MORE_LEVEL_PROVINCE");
        String province = Configure.getInstance().getProperty("PROVINCE");
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        String cityStr;
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            cityStr = this.getCityFormCitySqlStr(isAdminUser);
            sql.append(" SELECT C.CITY_ID, SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(this.getSqlCondition(ciLabelFormModel)).append(" AND C.CITY_ID IN (").append(cityStr).append(") ").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID = ").append(-1).append(" GROUP BY C.CITY_ID").append(" ORDER BY C.CITY_ID");
        } else {
            sql.append(" SELECT C.* FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(this.getSqlCondition(ciLabelFormModel)).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID = ").append(-1);
            if(isAdminUser && moreLevelProvince.contains(province)) {
                cityStr = this.getCityFormCitySqlStr(isAdminUser);
                sql.append(" AND C.CITY_ID IN (").append(cityStr).append(") ");
            } else {
                sql.append(" AND C.CITY_ID != ").append(-1);
            }

            sql.append(" ORDER BY C.CITY_ID ");
        }

        this.log.debug("selectCityFormChartData sql:" + sql.toString());
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelFormModel.class), new Object[0]);
        return list;
    }

    public List<CiLabelFormModel> selectVipFormChartData(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        List list = null;
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            String cityStr = this.getUserCitySqlStr();
            sql.append(" SELECT C.VIP_LEVEL_ID, SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(this.getSqlCondition(ciLabelFormModel)).append(" AND C.CITY_ID IN (").append(cityStr).append(") ").append(" AND C.VIP_LEVEL_ID != ").append(-1).append(" AND C.BRAND_ID = ").append(-1).append(" GROUP BY C.VIP_LEVEL_ID").append(" ORDER BY C.VIP_LEVEL_ID");
        } else {
            sql.append(" SELECT C.* FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(this.getSqlCondition(ciLabelFormModel)).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID != ").append(-1).append(" ORDER BY C.VIP_LEVEL_ID");
        }

        this.log.debug("selectVipFormChartData sql:" + sql.toString());
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelFormModel.class), new Object[0]);
        return list;
    }

    public List<CiLabelFormTrendModel> selectBrandTrendChartData(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        byte trendNum = 6;
        ArrayList list = new ArrayList();
        List brandList = null;
        String frontDate = DateUtil.getFrontDate(trendNum - 1, ciLabelFormModel.getDataDate(), ciLabelFormModel.getUpdateCycle().intValue());
        List labelFormList = null;
        StringBuffer brandSql = new StringBuffer();
        brandSql.append(" SELECT * FROM ").append("DIM_BRAND").append(" WHERE PARENT_ID = ").append(-1).append(" ORDER BY BRAND_ID ");
        brandList = this.getSimpleJdbcTemplate().query(brandSql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimBrand.class), new Object[0]);
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            String map = this.getUserCitySqlStr();
            sql.append(" SELECT C.DATA_DATE,C.BRAND_ID, SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.CITY_ID IN (").append(map).append(") ").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID =:brandId ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" AND C.DATA_DATE <= \'").append(ciLabelFormModel.getDataDate()).append("\' ").append(" GROUP BY C.DATA_DATE,C.BRAND_ID ").append(" ORDER BY C.DATA_DATE");
        } else {
            sql.append(" SELECT C.* FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" AND C.BRAND_ID =:brandId ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" AND C.DATA_DATE <= \'").append(ciLabelFormModel.getDataDate()).append("\' ").append(" ORDER BY C.DATA_DATE ");
        }

        this.log.debug("selectBrandTrendChartData sql: " + sql);
        HashMap map1 = new HashMap();
        Iterator iterator = brandList.iterator();

        while(iterator.hasNext()) {
            DimBrand brand = (DimBrand)iterator.next();
            map1.put("brandId", brand.getBrandId());
            CiLabelFormTrendModel trendModel = new CiLabelFormTrendModel();
            trendModel.setBrandId(brand.getBrandId());
            labelFormList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelFormModel.class), map1);
            if(labelFormList != null && labelFormList.size() > 0) {
                trendModel.setLabelFormList(labelFormList);
                list.add(trendModel);
            }
        }

        return list;
    }

    public List<CiLabelFormTrendModel> selectCityTrendChartData(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        byte trendNum = 6;
        ArrayList list = new ArrayList();
        List cityList = null;
        String frontDate = DateUtil.getFrontDate(trendNum - 1, ciLabelFormModel.getDataDate(), ciLabelFormModel.getUpdateCycle().intValue());
        List labelFormList = null;
        StringBuffer citySql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            String sql = this.getCityFormCitySqlStr(isAdminUser);
            citySql.append(" SELECT * FROM ").append("DIM_CITY").append(" WHERE PARENT_ID = ").append(-1).append(" AND CITY_ID IN (").append(sql).append(") ").append(" ORDER BY CITY_ID ");
        } else {
            citySql.append(" SELECT * FROM ").append("DIM_CITY").append(" WHERE PARENT_ID = ").append(-1).append(" ORDER BY CITY_ID ");
        }

        cityList = this.getSimpleJdbcTemplate().query(citySql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimCity.class), new Object[0]);
        StringBuffer sql1 = new StringBuffer();
        sql1.append(" SELECT C.* FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID =:cityId ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" AND C.DATA_DATE <= \'").append(ciLabelFormModel.getDataDate()).append("\' ").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("selectBrandTrendChartData sql: " + sql1);
        HashMap map = new HashMap();
        Iterator iterator = cityList.iterator();

        while(iterator.hasNext()) {
            DimCity city = (DimCity)iterator.next();
            map.put("cityId", city.getCityId());
            CiLabelFormTrendModel trendModel = new CiLabelFormTrendModel();
            trendModel.setCityId(city.getCityId());
            labelFormList = this.getSimpleJdbcTemplate().query(sql1.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelFormModel.class), map);
            if(labelFormList != null && labelFormList.size() > 0) {
                trendModel.setLabelFormList(labelFormList);
                list.add(trendModel);
            }
        }

        return list;
    }

    public List<CiLabelFormModel> selectCityTrendChartDataByCityId(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        byte trendNum = 6;
        List list = null;
        String frontDate = DateUtil.getFrontDate(trendNum - 1, ciLabelFormModel.getDataDate(), ciLabelFormModel.getUpdateCycle().intValue());
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.* FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(ciLabelFormModel.getCityId()).append(" AND C.DATA_DATE <= \'").append(ciLabelFormModel.getDataDate()).append("\' ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("selectCityTrendChartDataByCityId sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelFormModel.class), new Object[0]);
        return list;
    }

    public List<CiLabelFormModel> selectMoreCityTrendChartDataByCityId(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        byte trendNum = 0;
        if(updateCycle == 1) {
            trendNum = 12;
        } else if(updateCycle == 2) {
            trendNum = 30;
        }

        List list = null;
        String frontDate = DateUtil.getFrontDate(trendNum - 1, ciLabelFormModel.getDataDate(), ciLabelFormModel.getUpdateCycle().intValue());
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.* FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(ciLabelFormModel.getCityId()).append(" AND C.DATA_DATE <= \'").append(ciLabelFormModel.getDataDate()).append("\' ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("selectCityTrendChartDataByCityId sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelFormModel.class), new Object[0]);
        return list;
    }

    public List<CiLabelFormTrendModel> selectVipTrendChartData(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        byte trendNum = 6;
        ArrayList list = new ArrayList();
        List vipLevelList = null;
        String frontDate = DateUtil.getFrontDate(trendNum - 1, ciLabelFormModel.getDataDate(), ciLabelFormModel.getUpdateCycle().intValue());
        List labelFormList = null;
        StringBuffer vipLevelSql = new StringBuffer();
        vipLevelSql.append(" SELECT * FROM ").append("DIM_VIP_LEVEL").append(" ORDER BY VIP_LEVEL_ID ");
        vipLevelList = this.getSimpleJdbcTemplate().query(vipLevelSql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimVipLevel.class), new Object[0]);
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            String map = this.getUserCitySqlStr();
            sql.append(" SELECT C.VIP_LEVEL_ID,C.DATA_DATE,SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID IN (").append(map).append(") ").append(" AND C.VIP_LEVEL_ID =:vipLevelId ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" AND C.DATA_DATE <= \'").append(ciLabelFormModel.getDataDate()).append("\' ").append(" GROUP BY C.DATA_DATE,C.VIP_LEVEL_ID ").append(" ORDER BY C.DATA_DATE ");
        } else {
            sql.append(" SELECT C.* FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID =:vipLevelId ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" AND C.DATA_DATE <= \'").append(ciLabelFormModel.getDataDate()).append("\' ").append(" ORDER BY C.DATA_DATE ");
        }

        this.log.debug("selectBrandTrendChartData sql: " + sql);
        HashMap map1 = new HashMap();
        Iterator iterator = vipLevelList.iterator();

        while(iterator.hasNext()) {
            DimVipLevel vipLevel = (DimVipLevel)iterator.next();
            map1.put("vipLevelId", vipLevel.getVipLevelId());
            CiLabelFormTrendModel trendModel = new CiLabelFormTrendModel();
            trendModel.setVipLevelId(vipLevel.getVipLevelId());
            labelFormList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelFormModel.class), map1);
            if(labelFormList != null && labelFormList.size() > 0) {
                trendModel.setLabelFormList(labelFormList);
                list.add(trendModel);
            }
        }

        return list;
    }

    public List<CiLabelFormModel> selectVipTrendChartDataByVipLevelId(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        byte trendNum = 6;
        List list = null;
        String frontDate = DateUtil.getFrontDate(trendNum - 1, ciLabelFormModel.getDataDate(), ciLabelFormModel.getUpdateCycle().intValue());
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            String cityStr = this.getUserCitySqlStr();
            sql.append(" SELECT C.VIP_LEVEL_ID,C.DATA_DATE,SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(ciLabelFormModel.getVipLevelId()).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID IN (").append(cityStr).append(") ").append(" AND C.DATA_DATE <= \'").append(ciLabelFormModel.getDataDate()).append("\' ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" GROUP BY C.DATA_DATE,C.VIP_LEVEL_ID ").append(" ORDER BY C.DATA_DATE ");
        } else {
            sql.append(" SELECT C.* FROM ").append(tableName).append(" C ").append(" WHERE 1=1 ").append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(ciLabelFormModel.getVipLevelId()).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" AND C.DATA_DATE <= \'").append(ciLabelFormModel.getDataDate()).append("\' ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" ORDER BY C.DATA_DATE ");
        }

        this.log.debug("selectVipTrendChartDataByVipLevelId sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiLabelFormModel.class), new Object[0]);
        return list;
    }

    public List<CiBrandHisModel> selectBrandHistoryData(int currentPage, int pageSize, List<String> dateList, CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        List list = null;
        ArrayList modelList = new ArrayList();
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        int map;
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            String var18 = this.getUserCitySqlStr();
            sql.append("SELECT C.BRAND_ID,B.PARENT_ID");

            for(map = 0; map < dateList.size(); ++map) {
                sql.append(", MAX(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(map)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(map + 1);
            }

            sql.append(" FROM ").append(tableName).append(" C ").append(" LEFT JOIN ").append("DIM_BRAND").append(" B ON (C.BRAND_ID = B.BRAND_ID) ").append(" WHERE B.PARENT_ID = ").append(-1).append(" AND C.BRAND_ID != ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID IN (").append(var18).append(") ").append(" GROUP BY C.BRAND_ID,B.PARENT_ID ORDER BY C.BRAND_ID");
        } else {
            sql.append("SELECT C.BRAND_ID,B.PARENT_ID");

            for(int sub_sql = 0; sub_sql < dateList.size(); ++sub_sql) {
                sql.append(", MAX(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(sub_sql)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(sub_sql + 1);
            }

            sql.append(" FROM ").append(tableName).append(" C ").append(" LEFT JOIN ").append("DIM_BRAND").append(" B ON (C.BRAND_ID = B.BRAND_ID) ").append(" WHERE B.PARENT_ID = ").append(-1).append(" AND C.BRAND_ID != ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" GROUP BY C.BRAND_ID,B.PARENT_ID ORDER BY C.BRAND_ID");
        }

        this.log.debug("selectBrandHistoryData sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiBrandHisModel.class), new Object[0]);
        StringBuffer var19 = new StringBuffer();
        if(needAuthority && !isAdminUser) {
            String var20 = this.getUserCitySqlStr();
            var19.append("SELECT C.BRAND_ID,B.PARENT_ID");

            for(int iterator = 0; iterator < dateList.size(); ++iterator) {
                var19.append(", SUM(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(iterator)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(iterator + 1);
            }

            var19.append(" FROM ").append(tableName).append(" C ").append(" LEFT JOIN ").append("DIM_BRAND").append(" B ON (C.BRAND_ID = B.BRAND_ID) ").append(" WHERE B.PARENT_ID =:brandId ").append(" AND C.BRAND_ID != ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID IN (").append(var20).append(") ").append(" GROUP BY C.BRAND_ID,B.PARENT_ID ORDER BY C.BRAND_ID");
        } else {
            var19.append("SELECT C.BRAND_ID,B.PARENT_ID");

            for(map = 0; map < dateList.size(); ++map) {
                var19.append(", MAX(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(map)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(map + 1);
            }

            var19.append(" FROM ").append(tableName).append(" C ").append(" LEFT JOIN ").append("DIM_BRAND").append(" B ON (C.BRAND_ID = B.BRAND_ID) ").append(" WHERE B.PARENT_ID =:brandId ").append(" AND C.BRAND_ID != ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" GROUP BY C.BRAND_ID,B.PARENT_ID ORDER BY C.BRAND_ID");
        }

        HashMap var21 = new HashMap();
        Iterator var22 = list.iterator();

        while(var22.hasNext()) {
            CiBrandHisModel brand = (CiBrandHisModel)var22.next();
            var21.put("brandId", brand.getBrandId());
            List tempModelList = this.getSimpleJdbcTemplate().query(var19.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiBrandHisModel.class), var21);
            modelList.add(brand);
            if(null != tempModelList && tempModelList.size() > 0) {
                modelList.addAll(tempModelList);
            }
        }

        return modelList;
    }

    public List<CiVipHisModel> selectVipHistoryData(List<String> dateList, CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        List list = null;
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            String var12 = this.getUserCitySqlStr();
            sql.append("SELECT C.VIP_LEVEL_ID");

            for(int i1 = 0; i1 < dateList.size(); ++i1) {
                sql.append(", SUM(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(i1)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(i1 + 1);
            }

            sql.append(" FROM ").append(tableName).append(" C ").append(" WHERE  1=1 ").append(" AND C.VIP_LEVEL_ID != ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID IN (").append(var12).append(") ").append(" GROUP BY C.VIP_LEVEL_ID ");
        } else {
            sql.append("SELECT C.VIP_LEVEL_ID");

            for(int i = 0; i < dateList.size(); ++i) {
                sql.append(", MAX(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(i)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(i + 1);
            }

            sql.append(" FROM ").append(tableName).append(" C ").append(" WHERE  1=1 ").append(" AND C.VIP_LEVEL_ID != ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" GROUP BY C.VIP_LEVEL_ID ");
        }

        this.log.debug("selectVipHistoryData sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiVipHisModel.class), new Object[0]);
        return list;
    }

    public List<CiCityHisModel> selectCityHistoryData(List<String> dateList, CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        List list = null;
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        String moreLevelProvince = Configure.getInstance().getProperty("MORE_LEVEL_PROVINCE");
        String province = Configure.getInstance().getProperty("PROVINCE");
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        String var14;
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            var14 = this.getCityFormCitySqlStr(isAdminUser);
            sql.append("SELECT C.CITY_ID");

            for(int i = 0; i < dateList.size(); ++i) {
                sql.append(", SUM(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(i)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(i + 1);
            }

            sql.append(" FROM ").append(tableName).append(" C ").append(" WHERE  1=1 ").append(" AND C.CITY_ID IN (").append(var14).append(") ").append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" GROUP BY C.CITY_ID ");
        } else {
            sql.append("SELECT C.CITY_ID");

            for(int cityStr = 0; cityStr < dateList.size(); ++cityStr) {
                sql.append(", MAX(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(cityStr)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(cityStr + 1);
            }

            sql.append(" FROM ").append(tableName).append(" C ").append(" WHERE  1=1 ");
            if(isAdminUser && moreLevelProvince.contains(province)) {
                var14 = this.getCityFormCitySqlStr(isAdminUser);
                sql.append(" AND C.CITY_ID IN (").append(var14).append(") ");
            } else {
                sql.append(" AND C.CITY_ID != ").append(-1);
            }

            sql.append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" GROUP BY C.CITY_ID ");
        }

        this.log.debug("selectCityHistoryData sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCityHisModel.class), new Object[0]);
        return list;
    }

    protected String getSqlCondition(CiLabelFormModel ciLabelFormModel) {
        StringBuffer condition = new StringBuffer();
        if(null != ciLabelFormModel) {
            if(!StringUtil.isEmpty(ciLabelFormModel.getDataDate())) {
                condition.append(" AND C.DATA_DATE = \'").append(ciLabelFormModel.getDataDate()).append("\' ");
            }

            if(!StringUtil.isEmpty(ciLabelFormModel.getLabelId())) {
                condition.append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId());
            }
        }

        return condition.toString();
    }

    public long selectBrandHistoryDataCount(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        String countSql;
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            countSql = this.getUserCitySqlStr();
            sql.append("SELECT C.BRAND_ID ");
            sql.append(" FROM ").append(tableName).append(" C ").append(" WHERE 1 = 1 ").append(" AND C.BRAND_ID != ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID IN (").append(countSql).append(") ").append(" GROUP BY C.BRAND_ID ");
        } else {
            sql.append("SELECT C.BRAND_ID ");
            sql.append(" FROM ").append(tableName).append(" C ").append(" WHERE 1 = 1 ").append(" AND C.BRAND_ID != ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" GROUP BY C.BRAND_ID ");
        }

        this.log.debug("selectBrandHistoryDataCount sql : " + sql);
        countSql = this.getCountSql(sql.toString());
        long count = (long)this.getSimpleJdbcTemplate().queryForInt(countSql, new Object[0]);
        return count;
    }

    public long selectCityHistoryDataCount(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        String moreLevelProvince = Configure.getInstance().getProperty("MORE_LEVEL_PROVINCE");
        String province = Configure.getInstance().getProperty("PROVINCE");
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        String countSql;
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            countSql = this.getCityFormCitySqlStr(isAdminUser);
            sql.append("SELECT C.CITY_ID ");
            sql.append(" FROM ").append(tableName).append(" C ").append(" WHERE 1 = 1 ").append(" AND C.BRAND_ID = ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID IN (").append(countSql).append(") ").append(" GROUP BY C.CITY_ID ");
        } else {
            sql.append("SELECT C.CITY_ID ");
            sql.append(" FROM ").append(tableName).append(" C ").append(" WHERE 1 = 1 ").append(" AND C.BRAND_ID = ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID = ").append(-1);
            if(isAdminUser && moreLevelProvince.contains(province)) {
                countSql = this.getCityFormCitySqlStr(isAdminUser);
                sql.append(" AND C.CITY_ID IN (").append(countSql).append(") ");
            } else {
                sql.append(" AND C.CITY_ID != ").append(-1);
            }

            sql.append(" GROUP BY C.CITY_ID ");
        }

        this.log.debug("selectCityHistoryDataCount sql : " + sql);
        countSql = this.getCountSql(sql.toString());
        long count = (long)this.getSimpleJdbcTemplate().queryForInt(countSql, new Object[0]);
        return count;
    }

    public long selectVipHistoryDataCount(CiLabelFormModel ciLabelFormModel) throws Exception {
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        String tableName = DateUtil.getLabelTrendTableName(updateCycle);
        StringBuffer sql = new StringBuffer();
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        boolean isProvinceUser = PrivilegeServiceUtil.isProvinceUser(PrivilegeServiceUtil.getUserId());
        String countSql;
        if(needAuthority && !isAdminUser && !isProvinceUser) {
            countSql = this.getUserCitySqlStr();
            sql.append("SELECT C.VIP_LEVEL_ID ");
            sql.append(" FROM ").append(tableName).append(" C ").append(" WHERE 1 = 1 ").append(" AND C.BRAND_ID = ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID != ").append(-1).append(" AND C.CITY_ID IN (").append(countSql).append(") ").append(" GROUP BY C.VIP_LEVEL_ID ");
        } else {
            sql.append("SELECT C.VIP_LEVEL_ID ");
            sql.append(" FROM ").append(tableName).append(" C ").append(" WHERE 1 = 1 ").append(" AND C.BRAND_ID = ").append(-1).append(" AND C.LABEL_ID = ").append(ciLabelFormModel.getLabelId()).append(" AND C.VIP_LEVEL_ID != ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" GROUP BY C.VIP_LEVEL_ID ");
        }

        this.log.debug("selectCityHistoryDataCount sql : " + sql);
        countSql = this.getCountSql(sql.toString());
        long count = (long)this.getSimpleJdbcTemplate().queryForInt(countSql, new Object[0]);
        return count;
    }

    public String getCountSql(String sql) {
        StringBuffer countSql = new StringBuffer();
        countSql.append(" SELECT COUNT(1) FROM (").append(sql).append(") abc ");
        return countSql.toString();
    }

    protected String getUserCitySqlStr() {
        String cityStr = "";

        try {
            String e = PrivilegeServiceUtil.getUserId();
            List cityIdList = PrivilegeServiceUtil.getCityIdsForFormAnalysis(e);
            if(null != cityIdList && cityIdList.size() > 0) {
                String cityId;
                for(Iterator i$ = cityIdList.iterator(); i$.hasNext(); cityStr = cityStr + cityId + ",") {
                    cityId = (String)i$.next();
                }

                cityStr = cityStr.substring(0, cityStr.lastIndexOf(","));
            }
        } catch (Exception var6) {
            this.log.error(var6);
        }

        return cityStr;
    }

    protected String getCityFormCitySqlStr(boolean isAdminUser) {
        String cityStr = "";
        String noCity = Configure.getInstance().getProperty("NO_CITY");
        String centerCity = Configure.getInstance().getProperty("CENTER_CITYID");

        try {
            String e = PrivilegeServiceUtil.getUserId();
            List cityIdList = PrivilegeServiceUtil.getCityIdsForFormAnalysis(e);
            String cityId;
            if(null != cityIdList && cityIdList.size() > 0) {
                for(Iterator i$ = cityIdList.iterator(); i$.hasNext(); cityStr = cityStr + cityId + ",") {
                    cityId = (String)i$.next();
                    if(noCity.equals(cityId) || centerCity.equals(cityId) || isAdminUser) {
                        cityStr = "";
                        CopyOnWriteArrayList dmCityList = CacheBase.getInstance().getObjectList("DIM_CITY");
                        if(null != dmCityList && dmCityList.size() > 0) {
                            Iterator i$1 = dmCityList.iterator();

                            while(i$1.hasNext()) {
                                DimCity dmCity = (DimCity)i$1.next();
                                if(-1 == dmCity.getParentId().intValue()) {
                                    cityStr = cityStr + dmCity.getCityId() + ",";
                                }
                            }
                        } else {
                            cityStr = noCity;
                        }
                        break;
                    }
                }
            } else {
                cityStr = noCity;
            }

            cityStr = cityStr.substring(0, cityStr.lastIndexOf(","));
        } catch (Exception var12) {
            this.log.error(var12);
        }

        return cityStr;
    }
}

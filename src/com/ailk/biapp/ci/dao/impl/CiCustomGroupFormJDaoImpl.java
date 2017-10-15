//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomGroupFormJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomGroupForm;
import com.ailk.biapp.ci.entity.DimBrand;
import com.ailk.biapp.ci.entity.DimCity;
import com.ailk.biapp.ci.entity.DimVipLevel;
import com.ailk.biapp.ci.model.CiBrandHisModel;
import com.ailk.biapp.ci.model.CiCityHisModel;
import com.ailk.biapp.ci.model.CiCustomersFormTrendModel;
import com.ailk.biapp.ci.model.CiVipHisModel;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomGroupFormJDaoImpl extends JdbcBaseDao implements ICiCustomGroupFormJDao {
    private Logger log = Logger.getLogger(this.getClass());
    private static final String LABEL_STAT_TABLE = "CI_CUSTOM_GROUP_FORM";
    private static final String DIM_BRAND_TABLE = "DIM_BRAND";
    private static final String DIM_CITY_TABLE = "DIM_CITY";
    private static final String DIM_VIP_LEVEL_TABLE = "DIM_VIP_LEVEL";

    public CiCustomGroupFormJDaoImpl() {
    }

    public List<CiCustomersFormTrendModel> queryBrandFormChartData(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        ArrayList list = new ArrayList();
        List brandList = null;
        List customGroupFormList = null;
        StringBuffer brandSql = new StringBuffer();
        brandSql.append(" SELECT * FROM ").append("DIM_BRAND").append(" WHERE PARENT_ID = ").append(-1).append(" ORDER BY BRAND_ID");
        brandList = this.getSimpleJdbcTemplate().query(brandSql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimBrand.class), new Object[0]);
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.* FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1=1 ").append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId()).append("\'").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" AND C.BRAND_ID =:brandId ").append(" AND C.DATA_DATE = \'").append(ciCustomGroupForm.getDataDate()).append("\'").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("queryBrandFormChartData sql: " + sql);
        HashMap map = new HashMap();
        Iterator iterator = brandList.iterator();

        while(iterator.hasNext()) {
            DimBrand brand = (DimBrand)iterator.next();
            map.put("brandId", brand.getBrandId());
            CiCustomersFormTrendModel trendModel = new CiCustomersFormTrendModel();
            trendModel.setBrandId(brand.getBrandId());
            customGroupFormList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupForm.class), map);
            if(customGroupFormList != null && customGroupFormList.size() > 0) {
                trendModel.setCustomGroupFormList(customGroupFormList);
                list.add(trendModel);
            }
        }

        return list;
    }

    public List<CiCustomersFormTrendModel> selectBrandTrendChartData(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        byte trendNum = 6;
        ArrayList list = new ArrayList();
        List brandList = null;
        String frontDate = DateUtil.getCustomFrontDate(trendNum - 1, ciCustomGroupForm.getDataDate(), ciCustomGroupForm.getUpdateCycle().intValue());
        List customGroupFormList = null;
        StringBuffer brandSql = new StringBuffer();
        brandSql.append(" SELECT * FROM ").append("DIM_BRAND").append(" WHERE PARENT_ID = ").append(-1).append(" ORDER BY BRAND_ID");
        brandList = this.getSimpleJdbcTemplate().query(brandSql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimBrand.class), new Object[0]);
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.* FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1=1 ").append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId()).append("\'").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" AND C.BRAND_ID =:brandId ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" AND C.DATA_DATE <= \'").append(ciCustomGroupForm.getDataDate()).append("\' ").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("selectBrandTrendChartData sql: " + sql);
        HashMap map = new HashMap();
        Iterator iterator = brandList.iterator();

        while(iterator.hasNext()) {
            DimBrand brand = (DimBrand)iterator.next();
            map.put("brandId", brand.getBrandId());
            CiCustomersFormTrendModel trendModel = new CiCustomersFormTrendModel();
            trendModel.setBrandId(brand.getBrandId());
            customGroupFormList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupForm.class), map);
            if(customGroupFormList != null && customGroupFormList.size() > 0) {
                trendModel.setCustomGroupFormList(customGroupFormList);
                list.add(trendModel);
            }
        }

        return list;
    }

    public List<CiCustomGroupForm> selectSubBrandFormChartData(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        List list = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.*,B.BRAND_NAME FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" LEFT JOIN ").append("DIM_BRAND").append(" B ").append(" ON (C.BRAND_ID = B.BRAND_ID) ").append(" WHERE 1=1 ").append(this.getSqlCondition(ciCustomGroupForm)).append(" AND C.CITY_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID != ").append(-1);
        if(!StringUtil.isEmpty(ciCustomGroupForm.getBrandId())) {
            sql.append(" AND B.PARENT_ID = ").append(ciCustomGroupForm.getBrandId());
        }

        this.log.debug("selectSubBrandFormChartData sql:" + sql.toString());
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupForm.class), new Object[0]);
        return list;
    }

    public int selectBrandHistoryDataCount(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT C.BRAND_ID ");
        sql.append(" FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1 = 1 ").append(" AND C.BRAND_ID != ").append(-1).append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId()).append("\'").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" GROUP BY C.BRAND_ID ");
        this.log.debug("selectBrandHistoryDataCount sql : " + sql);
        String countSql = this.getCountSql(sql.toString());
        int count = this.getSimpleJdbcTemplate().queryForInt(countSql, new Object[0]);
        return count;
    }

    public int selectCityHistoryDataCount(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT C.CITY_ID ");
        sql.append(" FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1 = 1 ").append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID != ").append(-1).append(" GROUP BY C.CITY_ID ");
        this.log.debug("selectCityHistoryDataCount sql : " + sql);
        String countSql = this.getCountSql(sql.toString());
        int count = this.getSimpleJdbcTemplate().queryForInt(countSql, new Object[0]);
        return count;
    }

    public int selectVipHistoryDataCount(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT C.VIP_LEVEL_ID ");
        sql.append(" FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1 = 1 ").append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.VIP_LEVEL_ID != ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" GROUP BY C.VIP_LEVEL_ID ");
        this.log.debug("selectCityHistoryDataCount sql : " + sql);
        String countSql = this.getCountSql(sql.toString());
        int count = this.getSimpleJdbcTemplate().queryForInt(countSql, new Object[0]);
        return count;
    }

    public List<CiVipHisModel> selectVipHistoryData(List<String> dateList, CiCustomGroupForm ciCustomGroupForm) throws Exception {
        List list = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT C.VIP_LEVEL_ID");

        for(int i = 0; i < dateList.size(); ++i) {
            sql.append(", MAX(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(i)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(i + 1);
        }

        sql.append(" FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE  1=1 ").append(" AND C.VIP_LEVEL_ID != ").append(-1).append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" GROUP BY C.VIP_LEVEL_ID ");
        this.log.debug("selectVipHistoryData sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiVipHisModel.class), new Object[0]);
        return list;
    }

    public List<CiBrandHisModel> selectBrandHistoryData(int currentPage, int pageSize, List<String> dateList, CiCustomGroupForm ciCustomGroupForm) throws Exception {
        List list = null;
        ArrayList modelList = new ArrayList();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT C.BRAND_ID,B.PARENT_ID");

        for(int sub_sql = 0; sub_sql < dateList.size(); ++sub_sql) {
            sql.append(", MAX(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(sub_sql)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(sub_sql + 1);
        }

        sql.append(" FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" LEFT JOIN ").append("DIM_BRAND").append(" B ON (C.BRAND_ID = B.BRAND_ID) ").append(" WHERE B.PARENT_ID = ").append(-1).append(" AND C.BRAND_ID != ").append(-1).append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" GROUP BY C.BRAND_ID,B.PARENT_ID ORDER BY C.BRAND_ID");
        this.log.debug("selectBrandHistoryData sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiBrandHisModel.class), new Object[0]);
        StringBuffer var13 = new StringBuffer();
        var13.append("SELECT C.BRAND_ID,B.PARENT_ID");

        for(int map = 0; map < dateList.size(); ++map) {
            var13.append(", MAX(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(map)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(map + 1);
        }

        var13.append(" FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" LEFT JOIN ").append("DIM_BRAND").append(" B ON (C.BRAND_ID = B.BRAND_ID) ").append(" WHERE B.PARENT_ID =:brandId ").append(" AND C.BRAND_ID != ").append(-1).append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" GROUP BY C.BRAND_ID,B.PARENT_ID ORDER BY C.BRAND_ID");
        HashMap var14 = new HashMap();
        Iterator iterator = list.iterator();

        while(iterator.hasNext()) {
            CiBrandHisModel brand = (CiBrandHisModel)iterator.next();
            var14.put("brandId", brand.getBrandId());
            List tempModelList = this.getSimpleJdbcTemplate().query(var13.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiBrandHisModel.class), var14);
            modelList.add(brand);
            if(null != tempModelList && tempModelList.size() > 0) {
                modelList.addAll(tempModelList);
            }
        }

        return modelList;
    }

    public List<CiCustomersFormTrendModel> selectCityFormChartData(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        ArrayList list = new ArrayList();
        List cityList = null;
        List customGroupFormList = null;
        boolean isAdminUser = PrivilegeServiceUtil.isAdminUser(PrivilegeServiceUtil.getUserId());
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        StringBuffer citySql = new StringBuffer();
        if(needAuthority && !isAdminUser) {
            String sql = this.getCityFormCitySqlStr(isAdminUser);
            citySql.append(" SELECT * FROM ").append("DIM_CITY").append(" WHERE 1=1 ").append(" AND CITY_ID IN (").append(sql).append(") ").append(" ORDER BY CITY_ID ");
        } else {
            citySql.append(" SELECT * FROM ").append("DIM_CITY").append(" WHERE PARENT_ID = ").append(-1).append(" ORDER BY CITY_ID");
        }

        cityList = this.getSimpleJdbcTemplate().query(citySql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimCity.class), new Object[0]);
        StringBuffer sql1 = new StringBuffer();
        sql1.append(" SELECT C.* FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1=1 ").append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID =:cityId ").append(" AND C.DATA_DATE = \'").append(ciCustomGroupForm.getDataDate()).append("\' ").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("selectBrandTrendChartData sql: " + sql1);
        HashMap map = new HashMap();
        Iterator iterator = cityList.iterator();

        while(iterator.hasNext()) {
            DimCity city = (DimCity)iterator.next();
            map.put("cityId", city.getCityId());
            CiCustomersFormTrendModel trendModel = new CiCustomersFormTrendModel();
            trendModel.setCityId(city.getCityId());
            customGroupFormList = this.getSimpleJdbcTemplate().query(sql1.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupForm.class), map);
            if(customGroupFormList != null && customGroupFormList.size() > 0) {
                trendModel.setCustomGroupFormList(customGroupFormList);
                list.add(trendModel);
            }
        }

        return list;
    }

    public List<CiCustomersFormTrendModel> selectCityTrendChartData(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        byte trendNum = 6;
        ArrayList list = new ArrayList();
        List cityList = null;
        String frontDate = DateUtil.getCustomFrontDate(trendNum - 1, ciCustomGroupForm.getDataDate(), ciCustomGroupForm.getUpdateCycle().intValue());
        List customGroupFormList = null;
        StringBuffer citySql = new StringBuffer();
        citySql.append(" SELECT * FROM ").append("DIM_CITY").append(" WHERE PARENT_ID = ").append(-1).append(" ORDER BY CITY_ID");
        cityList = this.getSimpleJdbcTemplate().query(citySql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimCity.class), new Object[0]);
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.* FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1=1 ").append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID =:cityId ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" AND C.DATA_DATE <= \'").append(ciCustomGroupForm.getDataDate()).append("\' ").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("selectBrandTrendChartData sql: " + sql);
        HashMap map = new HashMap();
        Iterator iterator = cityList.iterator();

        while(iterator.hasNext()) {
            DimCity city = (DimCity)iterator.next();
            map.put("cityId", city.getCityId());
            CiCustomersFormTrendModel trendModel = new CiCustomersFormTrendModel();
            trendModel.setCityId(city.getCityId());
            customGroupFormList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupForm.class), map);
            if(customGroupFormList != null && customGroupFormList.size() > 0) {
                trendModel.setCustomGroupFormList(customGroupFormList);
                list.add(trendModel);
            }
        }

        return list;
    }

    public List<CiCustomGroupForm> selectCityTrendChartDataByCityId(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        byte trendNum = 6;
        List list = null;
        String frontDate = DateUtil.getCustomFrontDate(trendNum, ciCustomGroupForm.getDataDate(), ciCustomGroupForm.getUpdateCycle().intValue());
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.* FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1=1 ").append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(ciCustomGroupForm.getCityId()).append(" AND C.DATA_DATE <= \'").append(ciCustomGroupForm.getDataDate()).append("\' ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("selectCityTrendChartDataByCityId sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupForm.class), new Object[0]);
        return list;
    }

    public List<CiCustomGroupForm> selectMoreCityTrendChartDataByCityId(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        byte trendNum = 12;
        List list = null;
        String frontDate = DateUtil.getCustomFrontDate(trendNum, ciCustomGroupForm.getDataDate(), ciCustomGroupForm.getUpdateCycle().intValue());
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.* FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1=1 ").append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(ciCustomGroupForm.getCityId()).append(" AND C.DATA_DATE <= \'").append(ciCustomGroupForm.getDataDate()).append("\' ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("selectCityTrendChartDataByCityId sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupForm.class), new Object[0]);
        return list;
    }

    public List<CiCityHisModel> selectCityHistoryData(List<String> dateList, CiCustomGroupForm ciCustomGroupForm) throws Exception {
        List list = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT C.CITY_ID");

        for(int i = 0; i < dateList.size(); ++i) {
            sql.append(", MAX(CASE WHEN C.DATA_DATE = \'").append((String)dateList.get(i)).append("\' THEN C.CUSTOM_NUM ELSE 0 END) AS ").append("HIS").append(i + 1);
        }

        sql.append(" FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE  1=1 ").append(" AND C.CITY_ID != ").append(-1).append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.BRAND_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID = ").append(-1).append(" GROUP BY C.CITY_ID ");
        this.log.debug("selectCityHistoryData sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCityHisModel.class), new Object[0]);
        return list;
    }

    public List<CiCustomersFormTrendModel> selectVipFormChartData(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        ArrayList list = new ArrayList();
        List vipLevelList = null;
        List customGroupFormList = null;
        StringBuffer vipLevelSql = new StringBuffer();
        vipLevelSql.append(" SELECT * FROM ").append("DIM_VIP_LEVEL").append(" ORDER BY VIP_LEVEL_ID");
        vipLevelList = this.getSimpleJdbcTemplate().query(vipLevelSql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimVipLevel.class), new Object[0]);
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.* FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1=1 ").append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID =:vipLevelId ").append(" AND C.DATA_DATE = \'").append(ciCustomGroupForm.getDataDate()).append("\' ").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("selectBrandTrendChartData sql: " + sql);
        HashMap map = new HashMap();
        Iterator iterator = vipLevelList.iterator();

        while(iterator.hasNext()) {
            DimVipLevel vipLevel = (DimVipLevel)iterator.next();
            map.put("vipLevelId", vipLevel.getVipLevelId());
            CiCustomersFormTrendModel trendModel = new CiCustomersFormTrendModel();
            trendModel.setVipLevelId(vipLevel.getVipLevelId());
            customGroupFormList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupForm.class), map);
            if(customGroupFormList != null && customGroupFormList.size() > 0) {
                trendModel.setCustomGroupFormList(customGroupFormList);
                list.add(trendModel);
            }
        }

        return list;
    }

    public List<CiCustomersFormTrendModel> selectVipTrendChartData(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        byte trendNum = 6;
        ArrayList list = new ArrayList();
        List vipLevelList = null;
        String frontDate = DateUtil.getCustomFrontDate(trendNum - 1, ciCustomGroupForm.getDataDate(), ciCustomGroupForm.getUpdateCycle().intValue());
        List customGroupFormList = null;
        StringBuffer vipLevelSql = new StringBuffer();
        vipLevelSql.append(" SELECT * FROM ").append("DIM_VIP_LEVEL").append(" ORDER BY VIP_LEVEL_ID");
        vipLevelList = this.getSimpleJdbcTemplate().query(vipLevelSql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimVipLevel.class), new Object[0]);
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.* FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1=1 ").append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" AND C.VIP_LEVEL_ID =:vipLevelId ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" AND C.DATA_DATE <= \'").append(ciCustomGroupForm.getDataDate()).append("\' ").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("selectBrandTrendChartData sql: " + sql);
        HashMap map = new HashMap();
        Iterator iterator = vipLevelList.iterator();

        while(iterator.hasNext()) {
            DimVipLevel vipLevel = (DimVipLevel)iterator.next();
            map.put("vipLevelId", vipLevel.getVipLevelId());
            CiCustomersFormTrendModel trendModel = new CiCustomersFormTrendModel();
            trendModel.setVipLevelId(vipLevel.getVipLevelId());
            customGroupFormList = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupForm.class), map);
            if(customGroupFormList != null && customGroupFormList.size() > 0) {
                trendModel.setCustomGroupFormList(customGroupFormList);
                list.add(trendModel);
            }
        }

        return list;
    }

    public List<CiCustomGroupForm> selectVipTrendChartDataByVipLevelId(CiCustomGroupForm ciCustomGroupForm) throws Exception {
        byte trendNum = 6;
        List list = null;
        String frontDate = DateUtil.getCustomFrontDate(trendNum, ciCustomGroupForm.getDataDate(), ciCustomGroupForm.getUpdateCycle().intValue());
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.* FROM ").append("CI_CUSTOM_GROUP_FORM").append(" C ").append(" WHERE 1=1 ").append(" AND C.CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId() + "\'").append(" AND C.VIP_LEVEL_ID = ").append(ciCustomGroupForm.getVipLevelId()).append(" AND C.BRAND_ID = ").append(-1).append(" AND C.CITY_ID = ").append(-1).append(" AND C.DATA_DATE <= \'").append(ciCustomGroupForm.getDataDate()).append("\' ").append(" AND C.DATA_DATE >= \'").append(frontDate).append("\' ").append(" ORDER BY C.DATA_DATE ");
        this.log.debug("selectVipTrendChartDataByVipLevelId sql : " + sql);
        list = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiCustomGroupForm.class), new Object[0]);
        return list;
    }

    protected String getSqlCondition(CiCustomGroupForm ciCustomGroupForm) {
        StringBuffer condition = new StringBuffer();
        if(null != ciCustomGroupForm) {
            if(!StringUtil.isEmpty(ciCustomGroupForm.getDataDate())) {
                condition.append(" AND DATA_DATE = \'").append(ciCustomGroupForm.getDataDate()).append("\'");
            }

            if(!StringUtil.isEmpty(ciCustomGroupForm.getCustomGroupId())) {
                condition.append(" AND CUSTOM_GROUP_ID = \'").append(ciCustomGroupForm.getCustomGroupId()).append("\'");
            }
        }

        return condition.toString();
    }

    public int selectCustomersTotalByTrendAnalysisCount(String customersId, String dataDate, int cycleType) throws Exception {
        boolean count = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(CUSTOM_GROUP_ID)").append(" FROM CI_CUSTOM_GROUP_FORM").append(" WHERE CITY_ID=-1 AND VIP_LEVEL_ID=-1 AND BRAND_ID=-1 AND CUSTOM_GROUP_ID=? ");
        String endMonth;
        if(cycleType == 3) {
            endMonth = DateUtil.getFrontDay(31, dataDate);
            this.log.debug("startDay->" + endMonth + ",endDay->" + dataDate);
            sql.append(" AND DATA_DATE>=\'" + endMonth + "\' AND DATA_DATE<=\'" + dataDate);
        } else {
            endMonth = dataDate.substring(0, 6);
            String startMonth = DateUtil.getFrontMonth(12, endMonth);
            this.log.debug("startMonth->" + startMonth + ",endMonth->" + endMonth);
            sql.append(" AND DATA_DATE>=\'" + startMonth + "\' AND DATA_DATE<=\'" + endMonth + "\' ");
        }

        this.log.debug("SQL->" + sql.toString());
        int count1 = this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[]{customersId});
        return count1;
    }

    public int selectCustomersTotalByCustomerListCount(String customersId, String dataDate, int cycleType) throws Exception {
        boolean count = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1)").append(" FROM CI_CUSTOM_LIST_INFO").append(" WHERE CUSTOM_GROUP_ID=? ");
        String endMonth;
        if(cycleType == 3) {
            endMonth = DateUtil.getFrontDay(31, dataDate);
            this.log.debug("startDay->" + endMonth + ",endDay->" + dataDate);
            sql.append(" AND DATA_DATE>=\'" + endMonth + "\' AND DATA_DATE<=\'" + dataDate + "\'");
        } else if(cycleType == 2) {
            endMonth = dataDate.substring(0, 6);
            String startMonth = DateUtil.getFrontMonth(12, endMonth);
            this.log.debug("startMonth->" + startMonth + ",endMonth->" + endMonth);
            sql.append(" AND DATA_DATE>=\'" + startMonth + "\' AND DATA_DATE<=\'" + endMonth + "\' ");
        } else {
            sql.append("");
        }

        this.log.debug("SQL->" + sql.toString() + "  -- " + customersId);
        int count1 = this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[]{customersId});
        return count1;
    }

    public List<LabelTrendInfo> selectCustomersTotalByTrendAnalysis(String customersId, String dataDate, int cycleType) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT CUSTOM_GROUP_ID,DATA_DATE,CUSTOM_NUM AS VALUE").append(" FROM CI_CUSTOM_GROUP_FORM").append(" WHERE CITY_ID=-1 AND VIP_LEVEL_ID=-1 AND BRAND_ID=-1 AND CUSTOM_GROUP_ID=? ");
        String labelTrendList;
        if(cycleType == 3) {
            labelTrendList = DateUtil.getFrontDay(31, dataDate);
            this.log.debug("startDay->" + labelTrendList + ",endDay->" + dataDate);
            sql.append(" AND DATA_DATE>=\'" + labelTrendList + "\' AND DATA_DATE<=\'" + dataDate + "\' ORDER BY DATA_DATE ASC ");
        } else {
            labelTrendList = dataDate.substring(0, 6);
            String startMonth = DateUtil.getFrontMonth(12, labelTrendList);
            this.log.debug("startMonth->" + startMonth + ",endMonth->" + labelTrendList);
            sql.append(" AND DATA_DATE>=\'" + startMonth + "\' AND DATA_DATE<=\'" + labelTrendList + "\' ORDER BY DATA_DATE ASC ");
        }

        this.log.debug("SQL->" + sql.toString());
        List labelTrendList1 = this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendInfo.class), new Object[]{customersId});
        return labelTrendList1;
    }

    public List<LabelTrendInfo> selectCustomersTotalByTrendAnalysis(String customersId, String dataDate, int cycleType, int pageNum, int pageSize) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT CUSTOM_GROUP_ID,DATA_DATE,CUSTOM_NUM AS VALUE").append(" FROM CI_CUSTOM_GROUP_FORM").append(" WHERE CITY_ID=-1 AND VIP_LEVEL_ID=-1 AND BRAND_ID=-1 AND CUSTOM_GROUP_ID=? ");
        String sqlPage;
        if(cycleType == 3) {
            sqlPage = DateUtil.getFrontDay(31, dataDate);
            this.log.debug("startDay->" + sqlPage + ",endDay->" + dataDate);
            sql.append(" AND DATA_DATE>=\'" + sqlPage + "\' AND DATA_DATE<=\'" + dataDate + "\' ORDER BY DATA_DATE DESC ");
        } else {
            sqlPage = dataDate.substring(0, 6);
            String labelTrendList = DateUtil.getFrontMonth(12, sqlPage);
            this.log.debug("startMonth->" + labelTrendList + ",endMonth->" + sqlPage);
            sql.append(" AND DATA_DATE>=\'" + labelTrendList + "\' AND DATA_DATE<=\'" + sqlPage + "\' ORDER BY DATA_DATE DESC ");
        }

        this.log.debug("SQL->" + sql.toString());
        sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), pageNum, pageSize);
        List labelTrendList1 = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendInfo.class), new Object[]{customersId});
        return labelTrendList1;
    }

    public List<LabelTrendInfo> selectCustomersTotalByCustomerListPage(String customersId, String dataDate, int cycleType, String sort, Integer pageNum, Integer pageSize) throws Exception {
        String sql = this.createCustomersTotalSqlByCustomerList(dataDate, cycleType, sort);
        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql, pageNum.intValue(), pageSize.intValue());
        List labelTrendList = this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendInfo.class), new Object[]{customersId});
        return labelTrendList;
    }

    public List<LabelTrendInfo> selectCustomersTotalByCustomerList(String customersId, String dataDate, int cycleType, String sort) throws Exception {
        String sql = this.createCustomersTotalSqlByCustomerList(dataDate, cycleType, sort);
        List labelTrendList = this.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(LabelTrendInfo.class), new Object[]{customersId});
        return labelTrendList;
    }

    private String createCustomersTotalSqlByCustomerList(String dataDate, int cycleType, String sort) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT CUSTOM_GROUP_ID,DATA_DATE,CUSTOM_NUM AS VALUE,LIST_TABLE_NAME,").append("FILE_CREATE_STATUS FROM CI_CUSTOM_LIST_INFO").append(" WHERE CUSTOM_GROUP_ID=? AND DATA_STATUS =3 ");
        if(cycleType == 3) {
            String endMonth = DateUtil.getFrontDay(31, dataDate);
            this.log.debug("startDay->" + endMonth + ",endDay->" + dataDate);
            sql.append(" AND DATA_DATE>=\'" + endMonth + "\' AND DATA_DATE<=\'" + dataDate + "\' ORDER BY DATA_DATE ").append(sort);
        } else if(cycleType == 2) {
            String startMonth = DateUtil.getFrontMonth(12, dataDate);
            this.log.debug("startMonth->" + startMonth + ",endMonth->" + dataDate);
            sql.append(" AND DATA_DATE>=\'" + startMonth + "\' AND DATA_DATE<=\'" + dataDate + "\' ORDER BY DATA_DATE ").append(sort);
        } else {
            sql.append("");
        }

        this.log.debug("SQL->" + sql.toString());
        return sql.toString();
    }

    public String getCountSql(String sql) {
        StringBuffer countSql = new StringBuffer();
        countSql.append(" SELECT COUNT(*) FROM (").append(sql).append(") abc ");
        return countSql.toString();
    }

    protected String getCityFormCitySqlStr(boolean isAdminUser) {
        String cityStr = "";
        String noCity = Configure.getInstance().getProperty("NO_CITY");
        String centerCity = Configure.getInstance().getProperty("CENTER_CITYID");

        try {
            String e = PrivilegeServiceUtil.getUserId();
            List cityIdList = PrivilegeServiceUtil.getCityIdsForFormAnalysis(e);
            if(null != cityIdList && cityIdList.size() > 0) {
                String cityId;
                for(Iterator i$ = cityIdList.iterator(); i$.hasNext(); cityStr = cityStr + cityId + ",") {
                    cityId = (String)i$.next();
                    if(noCity.equals(cityId) || centerCity.equals(cityId) || isAdminUser) {
                        List subCities = PrivilegeServiceUtil.getSubCitys(cityId);
                        ICity subCity;
                        if(null != subCities && subCities.size() > 0) {
                            for(Iterator i$1 = subCities.iterator(); i$1.hasNext(); cityStr = cityStr + subCity.getDmCityId() + ",") {
                                subCity = (ICity)i$1.next();
                            }
                        }
                        break;
                    }
                }

                cityStr = cityStr.substring(0, cityStr.lastIndexOf(","));
            }
        } catch (Exception var12) {
            this.log.error(var12);
        }

        return cityStr;
    }

    public void deleteByListTableName(String listTableName) {
        String sql = "delete from CI_CUSTOM_GROUP_FORM where CUSTOM_GROUP_ID = ?";
        this.getSimpleJdbcTemplate().update(sql, new Object[]{listTableName});
    }

    public void deleteByCustomGroupIdAndDataDate(String customGroupId, String dataDate) {
        String sql = "delete from CI_CUSTOM_GROUP_FORM where CUSTOM_GROUP_ID = ? and DATA_DATE = ?";
        this.getSimpleJdbcTemplate().update(sql, new Object[]{customGroupId, dataDate});
    }
}

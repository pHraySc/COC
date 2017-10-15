package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomListInfoHDao;
import com.ailk.biapp.ci.dao.ICustomGroupStatJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.DimBrand;
import com.ailk.biapp.ci.entity.DimCity;
import com.ailk.biapp.ci.entity.DimVipLevel;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CustomGroupDaoStatImpl extends JdbcBaseDao implements ICustomGroupStatJDao {
    private Logger logger = Logger.getLogger(CustomGroupDaoStatImpl.class);
    private static final String TABLE_CUSTOM_STAT_INFO = "CI_CUSTOM_GROUP_FORM";
    private static final String TABLE_CUSTOM_LIST_INFO = "CI_CUSTOM_LIST_INFO";
    private static final String TABLE_CUSTOM_GROUP_INFO = "CI_CUSTOM_GROUP_INFO";
    private static final String TEMP_CUSTOM_STAT_TABLE = "CI_CUSTOM_GROUP_NUM_TEMP";
    @Autowired
    private ICiCustomListInfoHDao ciCustomListInfoHDao;

    public CustomGroupDaoStatImpl() {
    }

    public void statCustomGroupCustomNum(String dataDate, int updateCycle) throws CIServiceException {
        String keyColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
        String phoneNoColumn = Configure.getInstance().getProperty("MAIN_COLUMN");
        StringBuffer allCusGroupSql = new StringBuffer();
        allCusGroupSql.append(" SELECT T1.LIST_TABLE_NAME, T1.CUSTOM_GROUP_ID,T1.CUSTOM_NUM FROM ").append("CI_CUSTOM_LIST_INFO").append(" T1 LEFT JOIN ").append("CI_CUSTOM_GROUP_INFO").append(" T2 ").append(" ON(T1.CUSTOM_GROUP_ID = T2.CUSTOM_GROUP_ID) ").append(" WHERE T2.UPDATE_CYCLE = ").append(updateCycle).append(" AND T1.DATA_DATE = \'").append(dataDate).append("\' ").append(" AND T2.STATUS = ").append(1).append(" AND T1.DATA_STATUS = ").append(3);
        this.logger.debug("allCusGroupSql : " + allCusGroupSql);
        List allCusGroupList = this.getSimpleJdbcTemplate().queryForList(allCusGroupSql.toString(), new Object[0]);
        String dwFormTableName = Configure.getInstance().getProperty("DW_LABEL_FORM_TABLE");
        String dwFormTableNameNoDate = Configure.getInstance().getProperty("DW_LABEL_FORM_TABLE_NO_DATE");
        String newDataDate = CacheBase.getInstance().getNewLabelMonth();
        if("true".equals(dwFormTableNameNoDate) && dataDate.equals(newDataDate)) {
            dwFormTableName = dwFormTableName.substring(0, dwFormTableName.length() - 1);
        }

        String listTableSchema = Configure.getInstance().getProperty("CI_SCHEMA");
        dwFormTableName = listTableSchema + "." + dwFormTableName;
        String brandId = Configure.getInstance().getProperty("BRAND_ID");
        String subBrandId = Configure.getInstance().getProperty("SUB_BRAND_ID");
        String vipLevelId = Configure.getInstance().getProperty("VIP_LEVEL_ID");
        String cityId = Configure.getInstance().getProperty("CITY_ID");
        StringBuffer insertCustomNumSql = new StringBuffer();
        insertCustomNumSql.append("INSERT INTO ").append("CI_CUSTOM_GROUP_FORM").append(" (CUSTOM_GROUP_ID,DATA_DATE,CITY_ID,VIP_LEVEL_ID,BRAND_ID,CUSTOM_NUM) ");
        StringBuffer brandSql = new StringBuffer();
        brandSql.append(insertCustomNumSql).append(" SELECT CUSTOM_GROUP_ID,DATA_DATE,").append(-1).append(" AS CITY_ID,").append(-1).append(" AS VIP_LEVEL_ID,BRAND_ID,SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append("CI_CUSTOM_GROUP_NUM_TEMP").append(" GROUP BY DATA_DATE, CUSTOM_GROUP_ID, BRAND_ID ");
        this.logger.debug("brandSql : " + brandSql);
        StringBuffer citySql = new StringBuffer();
        citySql.append(insertCustomNumSql).append(" SELECT CUSTOM_GROUP_ID,DATA_DATE,CITY_ID,").append(-1).append(" AS VIP_LEVEL_ID,").append(-1).append(" AS BRAND_ID,SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append("CI_CUSTOM_GROUP_NUM_TEMP").append(" GROUP BY DATA_DATE, CUSTOM_GROUP_ID, CITY_ID ");
        this.logger.debug("citySql : " + citySql);
        StringBuffer vipSql = new StringBuffer();
        vipSql.append(insertCustomNumSql).append(" SELECT CUSTOM_GROUP_ID,DATA_DATE,").append(-1).append(" AS CITY_ID,VIP_LEVEL_ID,").append(-1).append(" AS BRAND_ID,SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append("CI_CUSTOM_GROUP_NUM_TEMP").append(" GROUP BY DATA_DATE, CUSTOM_GROUP_ID, VIP_LEVEL_ID ");
        this.logger.debug("vipSql : " + vipSql);
        StringBuffer delTempDataSql = new StringBuffer();
        delTempDataSql.append("DELETE FROM ").append("CI_CUSTOM_GROUP_NUM_TEMP");
        String customGroupId = "";
        String listTableName = "";
        if(null != allCusGroupList && allCusGroupList.size() > 0) {
            Iterator iterator = allCusGroupList.iterator();

            while(iterator.hasNext()) {
                try {
                    Map e = (Map)iterator.next();
                    customGroupId = (String)e.get("CUSTOM_GROUP_ID");
                    listTableName = (String)e.get("LIST_TABLE_NAME");
                    StringBuffer tempSql = new StringBuffer();
                    tempSql.append(" INSERT INTO ").append("CI_CUSTOM_GROUP_NUM_TEMP").append(" (CUSTOM_GROUP_ID,DATA_DATE,CITY_ID,VIP_LEVEL_ID,BRAND_ID,CUSTOM_NUM) ").append(" SELECT \'").append(customGroupId).append("\' AS CUSTOM_GROUP_ID, \'").append(dataDate).append("\' AS DATA_DATE, T2.").append(cityId).append(" AS CITY_ID,").append(" T2.").append(vipLevelId).append(" AS VIP_LEVEL_ID,T2.").append(brandId).append(", COUNT(T1.").append(keyColumn).append(") AS CUSTOM_NUM ").append(" FROM ").append(listTableName).append(" T1 LEFT JOIN ").append(dwFormTableName).append(dataDate.equals(newDataDate) && dwFormTableNameNoDate.equals("true")?"":dataDate).append(" T2 ON (T1.").append(keyColumn).append(" = T2.").append(phoneNoColumn).append(") ").append(" GROUP BY T2.").append(cityId).append(",T2.").append(vipLevelId).append(",T2.").append(brandId);
                    this.logger.debug(" tempSql : " + tempSql);
                    StringBuffer tempSql2 = new StringBuffer();
                    tempSql2.append(" INSERT INTO ").append("CI_CUSTOM_GROUP_NUM_TEMP").append(" (CUSTOM_GROUP_ID,DATA_DATE,CITY_ID,VIP_LEVEL_ID,BRAND_ID,CUSTOM_NUM) ").append(" SELECT \'").append(customGroupId).append("\' AS CUSTOM_GROUP_ID, \'").append(dataDate).append("\' AS DATA_DATE, T2.").append(cityId).append(" AS CITY_ID,").append(" T2.").append(vipLevelId).append(" AS VIP_LEVEL_ID,T2.").append(subBrandId).append(" AS BRAND_ID, COUNT(T1.").append(keyColumn).append(") AS CUSTOM_NUM ").append(" FROM ").append(listTableName).append(" T1 LEFT JOIN ").append(dwFormTableName).append(dataDate.equals(newDataDate) && dwFormTableNameNoDate.equals("true")?"":dataDate).append(" T2 ON (T1.").append(keyColumn).append(" = T2.").append(phoneNoColumn).append(") ").append(" GROUP BY T2.").append(cityId).append(",T2.").append(cityId).append(",T2.").append(subBrandId).append(" HAVING T2.").append(subBrandId).append(" > 0  ");
                    this.getSimpleJdbcTemplate().getJdbcOperations().execute(tempSql.toString());
                    this.getSimpleJdbcTemplate().getJdbcOperations().execute(tempSql2.toString());
                    this.getSimpleJdbcTemplate().getJdbcOperations().execute(brandSql.toString());
                    this.getSimpleJdbcTemplate().getJdbcOperations().execute(citySql.toString());
                    this.getSimpleJdbcTemplate().getJdbcOperations().execute(vipSql.toString());
                    this.getSimpleJdbcTemplate().getJdbcOperations().execute(delTempDataSql.toString());
                } catch (DataAccessException var26) {
                    this.logger.error("统计客户群客户数失败" + var26.getMessage());
                }
            }
        }

    }

    public void statCustomGroupRingNum(String dataDate, int updateCycle) throws CIServiceException {
        StringBuffer allCusGroupSql = new StringBuffer();
        allCusGroupSql.append(" SELECT T1.CUSTOM_GROUP_ID,T1.CUSTOM_NUM FROM ").append("CI_CUSTOM_LIST_INFO").append(" T1 LEFT JOIN ").append("CI_CUSTOM_GROUP_INFO").append(" T2 ").append(" ON(T1.CUSTOM_GROUP_ID = T2.CUSTOM_GROUP_ID) ").append(" WHERE T2.UPDATE_CYCLE = ").append(updateCycle).append(" AND T1.DATA_DATE = \'").append(dataDate).append("\' ").append(" AND T2.STATUS = ").append(1);
        this.logger.debug("allCusGroupSql : " + allCusGroupSql);
        List allCusGroupList = this.getSimpleJdbcTemplate().queryForList(allCusGroupSql.toString(), new Object[0]);
        StringBuffer frontCustomNumSql = new StringBuffer();
        frontCustomNumSql.append(" select T1.CUSTOM_NUM from ").append("CI_CUSTOM_LIST_INFO").append(" T1 ").append(" where T1.CUSTOM_GROUP_ID = :customGroupId ").append(" and T1.data_date = :frontDate");
        this.logger.debug("frontCustomNumSql : " + frontCustomNumSql);
        StringBuffer updateRingSql = new StringBuffer();
        updateRingSql.append(" UPDATE ").append("CI_CUSTOM_LIST_INFO").append(" SET RING_NUM = :ringNum ").append(" WHERE CUSTOM_GROUP_ID = :customGroupId AND DATA_DATE = \'").append(dataDate).append("\' ");
        this.logger.debug("updateRingSql : " + updateRingSql);
        String customGroupId = "";
        Integer customNum = Integer.valueOf(0);
        Integer ringNum = Integer.valueOf(0);
        Integer lastCustomNum = Integer.valueOf(0);
        String frontDate = DateUtil.getFrontMonth(1, dataDate);
        List frontCustomNumList = null;
        HashMap param = new HashMap();
        if(null != allCusGroupList && allCusGroupList.size() > 0) {
            Iterator iterator = allCusGroupList.iterator();

            while(iterator.hasNext()) {
                try {
                    Map e = (Map)iterator.next();
                    customGroupId = (String)e.get("CUSTOM_GROUP_ID");
                    customNum = Integer.valueOf((Integer)e.get("CUSTOM_NUM") == null?0:((Integer)e.get("CUSTOM_NUM")).intValue());
                    param.put("customGroupId", customGroupId);
                    param.put("frontDate", frontDate);
                    frontCustomNumList = this.getSimpleJdbcTemplate().queryForList(frontCustomNumSql.toString(), param);
                    if(null != frontCustomNumList && frontCustomNumList.size() > 0) {
                        lastCustomNum = (Integer)((Map)frontCustomNumList.get(0)).get("CUSTOM_NUM");
                        if(null == lastCustomNum) {
                            ringNum = null;
                        } else {
                            ringNum = Integer.valueOf(customNum.intValue() - lastCustomNum.intValue());
                        }
                    } else {
                        ringNum = null;
                    }

                    param.put("ringNum", ringNum);
                    this.getSimpleJdbcTemplate().update(updateRingSql.toString(), param);
                } catch (DataAccessException var16) {
                    this.logger.error("统计客户群环比增长量" + var16.getMessage());
                }
            }
        }

    }

    public void statCustomGroupRingNumById(String customGroupId, String listTableName, String dataDate) throws CIServiceException {
        StringBuffer frontCustomNumSql = new StringBuffer();
        frontCustomNumSql.append(" select T1.CUSTOM_NUM from ").append("CI_CUSTOM_LIST_INFO").append(" T1 ").append(" where T1.CUSTOM_GROUP_ID = :customGroupId ").append(" and T1.data_date = :frontDate");
        this.logger.debug("frontCustomNumSql : " + frontCustomNumSql);
        StringBuffer updateRingSql = new StringBuffer();
        updateRingSql.append(" UPDATE ").append("CI_CUSTOM_LIST_INFO").append(" SET RING_NUM = :ringNum ").append(" WHERE CUSTOM_GROUP_ID = :customGroupId AND DATA_DATE = \'").append(dataDate).append("\' ");
        this.logger.debug("updateRingSql : " + updateRingSql);
        Integer customNum = Integer.valueOf(0);
        Integer ringNum = Integer.valueOf(0);
        Integer lastCustomNum = Integer.valueOf(0);
        String frontDate = DateUtil.getFrontMonth(1, dataDate);
        List frontCustomNumList = null;
        HashMap param = new HashMap();

        try {
            CiCustomListInfo e = this.ciCustomListInfoHDao.selectById(listTableName);
            customNum = Integer.valueOf(e.getCustomNum().intValue());
            param.put("customGroupId", customGroupId);
            param.put("frontDate", frontDate);
            frontCustomNumList = this.getSimpleJdbcTemplate().queryForList(frontCustomNumSql.toString(), param);
            if(null != frontCustomNumList && frontCustomNumList.size() > 0) {
                lastCustomNum = (Integer)((Map)frontCustomNumList.get(0)).get("CUSTOM_NUM");
                if(null == lastCustomNum) {
                    ringNum = null;
                } else {
                    ringNum = Integer.valueOf(customNum.intValue() - lastCustomNum.intValue());
                }
            } else {
                ringNum = null;
            }

            param.put("ringNum", ringNum);
            this.getSimpleJdbcTemplate().update(updateRingSql.toString(), param);
        } catch (DataAccessException var13) {
            this.logger.error("统计客户群环比增长量" + var13.getMessage());
        }

    }

    public void statCustomGroupCustomNumById(String customGroupId, String listTableName, String dataDate) throws CIServiceException {
        long start = System.currentTimeMillis();
        boolean aBrandFlag = false;
        String keyColumn = Configure.getInstance().getProperty("RELATED_COLUMN");
        String phoneNoColumn = Configure.getInstance().getProperty("MAIN_COLUMN");
        String province = Configure.getInstance().getProperty("PROVINCE");
        String aBrandCitys = Configure.getInstance().getProperty("A_BRAND_CITYS");
        String dwFormTableName = Configure.getInstance().getProperty("DW_LABEL_FORM_TABLE");
        String dwFormTableNameNoDate = Configure.getInstance().getProperty("DW_LABEL_FORM_TABLE_NO_DATE");
        String brandId = Configure.getInstance().getProperty("BRAND_ID");
        String subBrandId = Configure.getInstance().getProperty("SUB_BRAND_ID");
        String vipLevelId = Configure.getInstance().getProperty("VIP_LEVEL_ID");
        String cityId = Configure.getInstance().getProperty("CITY_ID");
        String listTableSchema = Configure.getInstance().getProperty("CI_SCHEMA");
        listTableName = listTableSchema + "." + listTableName;
        String newDataDate = CacheBase.getInstance().getNewLabelMonth();
        if("true".equals(dwFormTableNameNoDate) && dataDate.equals(newDataDate)) {
            dwFormTableName = dwFormTableName.substring(0, dwFormTableName.length() - 1);
        }

        dwFormTableName = listTableSchema + "." + dwFormTableName;
        StringBuffer insertCustomNumSql = new StringBuffer();
        insertCustomNumSql.append("INSERT INTO ").append("CI_CUSTOM_GROUP_FORM").append(" (CUSTOM_GROUP_ID,DATA_DATE,CITY_ID,VIP_LEVEL_ID,BRAND_ID,CUSTOM_NUM) ");
        StringBuffer brandSql = new StringBuffer();
        brandSql.append(insertCustomNumSql).append(" SELECT CUSTOM_GROUP_ID,DATA_DATE,").append(-1).append(" AS CITY_ID,").append(-1).append(" AS VIP_LEVEL_ID,BRAND_ID,SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append("CI_CUSTOM_GROUP_NUM_TEMP").append(" GROUP BY DATA_DATE, CUSTOM_GROUP_ID, BRAND_ID ");
        StringBuffer citySql = new StringBuffer();
        citySql.append(insertCustomNumSql).append(" SELECT CUSTOM_GROUP_ID,DATA_DATE,CITY_ID,").append(-1).append(" AS VIP_LEVEL_ID,").append(-1).append(" AS BRAND_ID,SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append("CI_CUSTOM_GROUP_NUM_TEMP").append(" GROUP BY DATA_DATE, CUSTOM_GROUP_ID, CITY_ID ");
        StringBuffer vipSql = new StringBuffer();
        vipSql.append(insertCustomNumSql).append(" SELECT CUSTOM_GROUP_ID,DATA_DATE,").append(-1).append(" AS CITY_ID,VIP_LEVEL_ID,").append(-1).append(" AS BRAND_ID,SUM(CUSTOM_NUM) AS CUSTOM_NUM FROM ").append("CI_CUSTOM_GROUP_NUM_TEMP").append(" GROUP BY DATA_DATE, CUSTOM_GROUP_ID, VIP_LEVEL_ID ");
        StringBuffer delTempDataSql = new StringBuffer();
        delTempDataSql.append("DELETE FROM ").append("CI_CUSTOM_GROUP_NUM_TEMP");

        try {
            StringBuffer e = new StringBuffer();
            e.append(" INSERT INTO ").append("CI_CUSTOM_GROUP_NUM_TEMP").append(" (CUSTOM_GROUP_ID,DATA_DATE,CITY_ID,VIP_LEVEL_ID,BRAND_ID,CUSTOM_NUM) ").append(" SELECT \'").append(customGroupId).append("\' AS CUSTOM_GROUP_ID, \'").append(dataDate).append("\' AS DATA_DATE, T2.").append(cityId).append(" AS CITY_ID,").append(" T2.").append(vipLevelId).append(" AS VIP_LEVEL_ID,T2.").append(brandId).append(", COUNT(T1.").append(keyColumn).append(") AS CUSTOM_NUM ").append(" FROM ").append(listTableName).append(" T1 LEFT JOIN ").append(dwFormTableName).append(dataDate.equals(newDataDate) && dwFormTableNameNoDate.equals("true")?"":dataDate).append(" T2 ON (T1.").append(keyColumn).append(" = T2.").append(phoneNoColumn).append(") ").append(" WHERE T2.").append(cityId).append(" IS NOT NULL AND T2.").append(vipLevelId).append(" IS NOT NULL AND T2.").append(brandId).append(" IS NOT NULL  ").append(" GROUP BY T2.").append(cityId).append(",T2.").append(vipLevelId).append(",T2.").append(brandId).append(" ");
            StringBuffer tempSql2 = new StringBuffer();
            tempSql2.append(" INSERT INTO ").append("CI_CUSTOM_GROUP_NUM_TEMP").append(" (CUSTOM_GROUP_ID,DATA_DATE,CITY_ID,VIP_LEVEL_ID,BRAND_ID,CUSTOM_NUM) ").append(" SELECT \'").append(customGroupId).append("\' AS CUSTOM_GROUP_ID, \'").append(dataDate).append("\' AS DATA_DATE, T2.").append(cityId).append(" AS CITY_ID,").append(" T2.").append(vipLevelId).append(" AS VIP_LEVEL_ID,T2.").append(subBrandId).append(" AS BRAND_ID, COUNT(T1.").append(phoneNoColumn).append(") AS CUSTOM_NUM ").append(" FROM ").append(listTableName).append(" T1 LEFT JOIN ").append(dwFormTableName).append(dataDate.equals(newDataDate) && dwFormTableNameNoDate.equals("true")?"":dataDate).append(" T2 ON (T1.").append(keyColumn).append(" = T2.").append(phoneNoColumn).append(") ").append(" WHERE T2.").append(cityId).append(" IS NOT NULL AND T2.").append(vipLevelId).append(" IS NOT NULL AND T2.").append(subBrandId).append(" IS NOT NULL  ").append(" GROUP BY T2.").append(cityId).append(",T2.").append(vipLevelId).append(",T2.").append(subBrandId).append(" ");
            this.logger.debug("tempSql:" + e);
            this.getSimpleJdbcTemplate().getJdbcOperations().execute(e.toString());
            this.logger.debug("citySql:" + citySql);
            this.getSimpleJdbcTemplate().getJdbcOperations().execute(citySql.toString());
            this.logger.debug("vipSql:" + vipSql);
            this.getSimpleJdbcTemplate().getJdbcOperations().execute(vipSql.toString());
            this.logger.debug("tempSql2:" + tempSql2);
            if(StringUtil.isNotEmpty(aBrandCitys)) {
                String[] aBrandCityArr = aBrandCitys.split(",");
                String[] arr$ = aBrandCityArr;
                int len$ = aBrandCityArr.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    String aBrandCity = arr$[i$];
                    if(aBrandCity.equals(province)) {
                        aBrandFlag = true;
                    }
                }
            }

            if(!aBrandFlag) {
                this.getSimpleJdbcTemplate().getJdbcOperations().execute(tempSql2.toString());
            }

            this.logger.debug("brandSql:" + brandSql);
            this.getSimpleJdbcTemplate().getJdbcOperations().execute(brandSql.toString());
        } catch (DataAccessException var35) {
            this.logger.error("statCustomGroupCustomNumById error" + var35.getMessage(), var35);
            throw new CIServiceException("统计指定客户群各维度客户数失败 " + var35.getMessage());
        } finally {
            this.logger.debug("delTempDataSql:" + delTempDataSql.toString());
            this.getSimpleJdbcTemplate().getJdbcOperations().execute(delTempDataSql.toString());
            this.logger.debug("statCustomGroupCustomNumById finish,cost:" + (System.currentTimeMillis() - start));
        }

    }

    public void insertCustomGroupNotExistData(String customGroupId, String dataDate) {
        this.logger.debug("插入客户群个维度不存在的数据开始.....");
        StringBuffer brandSql = new StringBuffer();
        StringBuffer citySql = new StringBuffer();
        StringBuffer vipSql = new StringBuffer();
        new ArrayList();
        new ArrayList();
        new ArrayList();
        brandSql.append(" SELECT * FROM DIM_BRAND ");
        citySql.append(" SELECT * FROM DIM_CITY ");
        vipSql.append(" SELECT * FROM DIM_VIP_LEVEL ");
        List brandList = this.getSimpleJdbcTemplate().query(brandSql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimBrand.class), new Object[0]);
        List cityList = this.getSimpleJdbcTemplate().query(citySql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimCity.class), new Object[0]);
        List vipLevelList = this.getSimpleJdbcTemplate().query(vipSql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(DimVipLevel.class), new Object[0]);
        StringBuffer brandFormSql = new StringBuffer();
        StringBuffer cityFormSql = new StringBuffer();
        StringBuffer vipLevelFormSql = new StringBuffer();
        brandFormSql.append(" SELECT * FROM ").append("CI_CUSTOM_GROUP_FORM").append(" WHERE custom_group_id =? and data_date =? and BRAND_ID =? AND CITY_ID = -1 AND VIP_LEVEL_ID = -1");
        cityFormSql.append(" SELECT * FROM ").append("CI_CUSTOM_GROUP_FORM").append(" WHERE custom_group_id =? and data_date =? and CITY_ID =? AND BRAND_ID = -1 AND VIP_LEVEL_ID = -1");
        vipLevelFormSql.append(" SELECT * FROM ").append("CI_CUSTOM_GROUP_FORM").append(" WHERE custom_group_id =? and data_date =? and VIP_LEVEL_ID =? AND CITY_ID = -1 AND BRAND_ID = -1");
        StringBuffer brandInsertSql = new StringBuffer();
        StringBuffer cityInsertSql = new StringBuffer();
        StringBuffer vipLevelInsertSql = new StringBuffer();
        brandInsertSql.append(" INSERT INTO ").append("CI_CUSTOM_GROUP_FORM").append("(CUSTOM_GROUP_ID,DATA_DATE,BRAND_ID,CITY_ID,VIP_LEVEL_ID,CUSTOM_NUM)").append(" values(\'").append(customGroupId).append("\',\'").append(dataDate).append("\',?,-1,-1,0)");
        cityInsertSql.append(" INSERT INTO ").append("CI_CUSTOM_GROUP_FORM").append("(CUSTOM_GROUP_ID,DATA_DATE,BRAND_ID,CITY_ID,VIP_LEVEL_ID,CUSTOM_NUM)").append(" values(\'").append(customGroupId).append("\',\'").append(dataDate).append("\',-1,?,-1,0)");
        vipLevelInsertSql.append(" INSERT INTO ").append("CI_CUSTOM_GROUP_FORM").append("(CUSTOM_GROUP_ID,DATA_DATE,BRAND_ID,CITY_ID,VIP_LEVEL_ID,CUSTOM_NUM)").append(" values(\'").append(customGroupId).append("\',\'").append(dataDate).append("\',-1,-1,?,0)");
        HashMap param = new HashMap();
        param.put("customGroupId", customGroupId);
        param.put("dataDate", dataDate);
        List brandFormList = null;

        Iterator cityFormList;
        DimBrand vipLevelFormList;
        for(cityFormList = brandList.iterator(); cityFormList.hasNext(); brandFormList = null) {
            vipLevelFormList = (DimBrand)cityFormList.next();
            param.put("brandId", vipLevelFormList.getBrandId());
            brandFormList = this.getSimpleJdbcTemplate().queryForList(brandFormSql.toString(), new Object[]{customGroupId, dataDate, vipLevelFormList.getBrandId()});
            if(null == brandFormList || brandFormList.size() <= 0) {
                this.getSimpleJdbcTemplate().update(brandInsertSql.toString(), new Object[]{vipLevelFormList.getBrandId()});
            }
        }

        cityFormList = null;

        for(Iterator vipLevelFormList1 = cityList.iterator(); vipLevelFormList1.hasNext(); cityFormList = null) {
            DimCity iterator = (DimCity)vipLevelFormList1.next();
            param.put("cityId", iterator.getCityId());
            List cityFormList1 = this.getSimpleJdbcTemplate().queryForList(cityFormSql.toString(), new Object[]{customGroupId, dataDate, iterator.getCityId()});
            if(null == cityFormList1 || cityFormList1.size() <= 0) {
                this.getSimpleJdbcTemplate().update(cityInsertSql.toString(), new Object[]{iterator.getCityId()});
            }
        }

        vipLevelFormList = null;

        for(Iterator iterator1 = vipLevelList.iterator(); iterator1.hasNext(); vipLevelFormList = null) {
            DimVipLevel vipLevel = (DimVipLevel)iterator1.next();
            param.put("vipLevelId", vipLevel.getVipLevelId());
            List vipLevelFormList2 = this.getSimpleJdbcTemplate().queryForList(vipLevelFormSql.toString(), new Object[]{customGroupId, dataDate, vipLevel.getVipLevelId()});
            if(null == vipLevelFormList2 || vipLevelFormList2.size() <= 0) {
                this.getSimpleJdbcTemplate().update(vipLevelInsertSql.toString(), new Object[]{vipLevel.getVipLevelId()});
            }
        }

        this.logger.debug("插入客户群个维度不存在的数据结束.....");
    }

    public int dataDateExist(String dataDate) throws CIServiceException {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT COUNT(1) FROM ").append("CI_CUSTOM_GROUP_FORM").append(" WHERE DATA_DATE = \'").append(dataDate).append("\' ");
        this.logger.debug("custom dataDateExist sql : " + sql);
        int dataDateNum = this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[0]);
        return dataDateNum;
    }

    public void updateDwNullDataTo0ForMonth(String month) throws Exception {
        String dwFormTableName = Configure.getInstance().getProperty("DW_LABEL_FORM_TABLE");
        StringBuffer formBrandSql = new StringBuffer();
        formBrandSql.append(" UPDATE ").append(dwFormTableName).append("_").append(month).append(" SET BRAND_ID = 0 WHERE BRAND_ID IS NULL");
        StringBuffer formSubBrandSql = new StringBuffer();
        formSubBrandSql.append(" UPDATE ").append(dwFormTableName).append("_").append(month).append(" SET SUB_BRAND_ID = 0 WHERE SUB_BRAND_ID IS NULL");
        StringBuffer formCountySql = new StringBuffer();
        formCountySql.append(" UPDATE ").append(dwFormTableName).append("_").append(month).append(" SET COUNTY_ID = 0 WHERE COUNTY_ID IS NULL");
        StringBuffer formVipSql = new StringBuffer();
        formVipSql.append(" UPDATE ").append(dwFormTableName).append("_").append(month).append(" SET VIP_SCALE_ID = 0 WHERE VIP_SCALE_ID IS NULL");
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(formBrandSql.toString());
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(formSubBrandSql.toString());
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(formCountySql.toString());
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(formVipSql.toString());
    }

    public void updateDwNullDataTo0ForDay(String day) throws Exception {
        String dwFormTableName = Configure.getInstance().getProperty("DW_LABEL_FORM_TABLE");
        StringBuffer formBrandSql = new StringBuffer();
        formBrandSql.append(" UPDATE ").append(dwFormTableName).append("_").append(day).append(" SET BRAND_ID = 0 WHERE BRAND_ID IS NULL");
        StringBuffer formSubBrandSql = new StringBuffer();
        formSubBrandSql.append(" UPDATE ").append(dwFormTableName).append("_").append(day).append(" SET SUB_BRAND_ID = 0 WHERE SUB_BRAND_ID IS NULL");
        StringBuffer formCountySql = new StringBuffer();
        formCountySql.append(" UPDATE ").append(dwFormTableName).append("_").append(day).append(" SET COUNTY_ID = 0 WHERE COUNTY_ID IS NULL");
        StringBuffer formVipSql = new StringBuffer();
        formVipSql.append(" UPDATE ").append(dwFormTableName).append("_").append(day).append(" SET VIP_SCALE_ID = 0 WHERE VIP_SCALE_ID IS NULL");
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(formBrandSql.toString());
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(formSubBrandSql.toString());
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(formCountySql.toString());
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(formVipSql.toString());
    }

    public void deleteCustomGroupData(String customGroupId, String month) throws CIServiceException {
        StringBuffer sql = new StringBuffer();
        sql.append(" DELETE FROM ").append("CI_CUSTOM_GROUP_FORM").append(" WHERE CUSTOM_GROUP_ID = \'").append(customGroupId).append("\' AND DATA_DATE = \'").append(month).append("\'");
        this.getSimpleJdbcTemplate().getJdbcOperations().execute(sql.toString());
    }
}

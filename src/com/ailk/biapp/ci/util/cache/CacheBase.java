package com.ailk.biapp.ci.util.cache;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.*;
import com.ailk.biapp.ci.entity.base.SysInfo;
import com.ailk.biapp.ci.market.entity.CiMarketScene;
import com.ailk.biapp.ci.market.entity.CiMarketTask;
import com.ailk.biapp.ci.model.CiNoticeModel;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.*;
import com.ailk.biapp.ci.service.impl.CiLabelInfoServiceImpl;
import com.ailk.biapp.ci.service.impl.CiProductServiceImpl;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.dimtable.service.impl.DimTableServiceImpl2;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CacheBase {
    private Logger log = Logger.getLogger(CacheBase.class);
    private static CacheBase instance = new CacheBase();
    @Autowired
    private ICiUserNoticeSetService ciUserNoticeSetService;
    private Map<String, Map<Object, Object>> cacheKVContainer;
    private Map<String, CopyOnWriteArrayList<?>> cacheListContainer;
    private Map<String, CopyOnWriteArrayList<?>> cacheKeyArray;
    private JdbcBaseDao jdbcBaseDao = null;
    private CiLabelInfoServiceImpl ciLabelInfoService;
    private CiProductServiceImpl ciProductService;
    private ICiSysAnnouncementService sysAnnouncementService;
    private ICiPersonNoticeService personNoticeService;
    private IDimCocLabelStatusService dimCocLabelStatusService;
    private ILabelStatService labelStatService;
    private Map<String, CiNoticeModel> userNoticeContainer;

    private Map<String,String> hiddenLabelMap;//lewis lee 过滤标签map
    private static Set<String> dimCitySet = new HashSet();
    private DimTableServiceImpl2 dimTableService2;
    @Autowired
    private ILabelInfoCacheService labelInfoCacheService;

    public static Set<String> getDimCitySet() {
        return dimCitySet;
    }

    private CacheBase() {
    }

    public static CacheBase getInstance() {
        if(dimCitySet == null || dimCitySet.size() == 0) {
            String dimIdsStr = Configure.getInstance().getProperty("DIM_IDS");
            String[] dimIds = (String[])null;
            if(StringUtil.isNotEmpty(dimIdsStr)) {
                dimIds = dimIdsStr.split(",");
                String[] var5 = dimIds;
                int var4 = dimIds.length;

                for(int var3 = 0; var3 < var4; ++var3) {
                    String dimId = var5[var3];
                    dimCitySet.add(dimId.toUpperCase());
                }
            }
        }

        return instance;
    }

    public boolean init(boolean isStart) throws Exception {
        this.cacheKVContainer = new ConcurrentHashMap();
        this.cacheListContainer = new ConcurrentHashMap();
        this.cacheKeyArray = new ConcurrentHashMap();
        this.jdbcBaseDao = (JdbcBaseDao)SystemServiceLocator.getInstance().getService("jdbcBaseDao");
        this.ciLabelInfoService = (CiLabelInfoServiceImpl)SystemServiceLocator.getInstance().getService("ciLabelInfoServiceImpl");
        this.ciProductService = (CiProductServiceImpl)SystemServiceLocator.getInstance().getService("ciProductServiceImpl");
        this.sysAnnouncementService = (ICiSysAnnouncementService)SystemServiceLocator.getInstance().getService("ciSysAnnouncementServiceImpl");
        this.ciUserNoticeSetService = (ICiUserNoticeSetService)SystemServiceLocator.getInstance().getService("ciUserNoticeSetService");
        this.personNoticeService = (ICiPersonNoticeService)SystemServiceLocator.getInstance().getService("ciPersonNoticeServiceImpl");
        this.dimCocLabelStatusService = (IDimCocLabelStatusService)SystemServiceLocator.getInstance().getService("dimCocLabelStatusServiceImpl");
        this.labelStatService = (ILabelStatService)SystemServiceLocator.getInstance().getService("labelStatService");
        this.userNoticeContainer = new ConcurrentHashMap();
        this.dimTableService2 = (DimTableServiceImpl2)SystemServiceLocator.getInstance().getService("dimTable_newService");
        this.labelInfoCacheService = (ILabelInfoCacheService)SystemServiceLocator.getInstance().getService("labelInfoCacheServiceImpl");
//        this.initHiddenLabelMap();//lewis添加初始化隐藏标签列表

        System.out.println("#################################################################line 149");
        this.initDimLabelUseType();     //加载维表dim_label_user_type
        this.initDimApproveLevel();
        this.initDimLabelDataStatus();
        this.initDimApproveStatus();
        this.initDimCustomCreateType();
        this.initDimCustomDataStatus();
        this.initDimBrand();
        this.initDimVipLevel();
        this.initDimCity();
        this.initCiNewestLabelDate();
        this.initDimOpLogTypeDetail();
        this.initDimOpLogType();
        this.initDimScene();
        this.initDimCityThreadConfig();
        this.initDimCityCustomGroupNum();
        this.initDimAnnouncementType();
        this.initDimListTactics();
        this.initMdaSysTable();
        this.initDimLabelType();
        this.initDimApproveResourceType();

        System.out.println("#################################################################line 171");
        this.initAllEffectiveLabel();
        this.initAllEffectiveProduct();
        this.initCiProductCategory();
        this.initCustomerGroupInfo(isStart);
        this.initHotLabelInfo();
        this.initHotCustomGroupInfo();
        this.initAllCiMarketTask();
        this.initAllLabelColumnName();
        this.initDimTableDefine();
        this.initMarketScene();
        String province = Configure.getInstance().getProperty("PROVINCE");
        if("zhejiang".equals(province)) {
            this.initZjUser();
        }

        return true;
    }


    public String getNameByKey(String tableName, Object key) {
        return ((Map)this.cacheKVContainer.get(tableName)).get(key) == null?"":((Map)this.cacheKVContainer.get(tableName)).get(key).toString();
    }

    public String getLabelNameByKey(String tableName, Object key) {
        return ((CiLabelInfo)((Map)this.cacheKVContainer.get(tableName)).get("LABEL_" + key)).getLabelName();
    }

    public Object getObjectByKey(String tableName, Object key) {
        if(key == null) {
            return null;
        } else {
            int index = ((CopyOnWriteArrayList)this.cacheKeyArray.get(tableName)).indexOf(key);
            return index != -1?((CopyOnWriteArrayList)this.cacheListContainer.get(tableName)).get(index):null;
        }
    }

    public CopyOnWriteArrayList<?> getObjectList(String tableName) {
        CopyOnWriteArrayList objectList = (CopyOnWriteArrayList)this.cacheListContainer.get(tableName);
        return objectList;
    }

    public CopyOnWriteArrayList<String> getNamesByKey(String tableName, List<?> keys) {
        CopyOnWriteArrayList names = new CopyOnWriteArrayList();
        Iterator var5 = keys.iterator();

        while(var5.hasNext()) {
            Object key = var5.next();
            names.add(((Map)this.cacheKVContainer.get(tableName)).get(key).toString());
        }

        return names;
    }

    public Object getKeyObject(String tableName, String key) {
        List keyList = (List)this.cacheKeyArray.get(tableName);
        if(keyList != null && !keyList.isEmpty()) {
            Object dimKey = keyList.get(0);
            return dimKey instanceof Integer?new Integer(key):(dimKey instanceof String?key:key);
        } else {
            return key;
        }
    }

    public CopyOnWriteArrayList<?> getKeyList(String tableName) {
        CopyOnWriteArrayList keyList = (CopyOnWriteArrayList)this.cacheKeyArray.get(tableName);
        return keyList;
    }

    /*
    public void initHiddenLabelMap(){
        String hiddenLabelUrl=Configure.getInstance().getProperty("LABEL_HIDDEN_PATH");
        hiddenLabelMap= FileUtil.loadFileToMapByLine(hiddenLabelUrl);
        this.log.info("开始过滤显示标签"+hiddenLabelUrl);
        this.log.info(hiddenLabelMap.toString());
    }
    */

    public Map<String,String> getHiddenLabelMap(){
        return hiddenLabelMap;
    }

    private void initDimLabelUseType() {
        String cacheSql = "select LABEL_USE_TYPE_ID ,LABEL_USE_TYPE_NAME,LABEL_USE_TYPE_DESC from DIM_LABEL_USE_TYPE";
        List lableUserTypeTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(cacheSql, ParameterizedBeanPropertyRowMapper.newInstance(DimLabelUseType.class), new Object[0]);
        CopyOnWriteArrayList lableUserTypeList = new CopyOnWriteArrayList(lableUserTypeTemp);
        ConcurrentHashMap lableUserTypeMap = new ConcurrentHashMap();
        CopyOnWriteArrayList lableUserTypeKeys = new CopyOnWriteArrayList();
        Iterator var7 = lableUserTypeList.iterator();

        while(var7.hasNext()) {
            DimLabelUseType dimLabelUseType = (DimLabelUseType)var7.next();
            lableUserTypeMap.put(dimLabelUseType.getLabelUseTypeId(), dimLabelUseType.getLabelUseTypeName());
            lableUserTypeKeys.add(dimLabelUseType.getLabelUseTypeId());
        }

        this.cacheKVContainer.put("DIM_LABEL_USE_TYPE", lableUserTypeMap);
        this.cacheListContainer.put("DIM_LABEL_USE_TYPE", lableUserTypeList);
        this.cacheKeyArray.put("DIM_LABEL_USE_TYPE", lableUserTypeKeys);
    }

    private void initDimApproveLevel() {
        String dimApproveLevelSql = "select APPROVE_ROLE_ID, APPROVE_ROLE_NAME, APPROVE_ROLE_TYPE, APPROVE_LEVEL from DIM_APPROVE_LEVEL";
        List approveLevelTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(dimApproveLevelSql, ParameterizedBeanPropertyRowMapper.newInstance(DimApproveLevel.class), new Object[0]);
        CopyOnWriteArrayList approveLevelList = new CopyOnWriteArrayList(approveLevelTemp);
        ConcurrentHashMap approveLevelMap = new ConcurrentHashMap();
        CopyOnWriteArrayList approveLevelKeys = new CopyOnWriteArrayList();
        Iterator var7 = approveLevelList.iterator();

        while(var7.hasNext()) {
            DimApproveLevel approveLevel = (DimApproveLevel)var7.next();
            approveLevelMap.put(approveLevel.getApproveRoleId(), approveLevel.getApproveRoleName());
            approveLevelKeys.add(approveLevel.getApproveRoleId());
        }

        this.cacheKVContainer.put("DIM_APPROVE_LEVEL", approveLevelMap);
        this.cacheListContainer.put("DIM_APPROVE_LEVEL", approveLevelList);
        this.cacheKeyArray.put("DIM_APPROVE_LEVEL", approveLevelKeys);
    }

    public void initDimApproveResourceType() {
        String dimApproveResourceTypeSql = "select RESOURCE_TYPE_ID, RESOURCE_TYPE_NAME, RESOURCE_DETAIL_LINK from DIM_APPROVE_RESOURCE_TYPE";
        List approveResourceTypeTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(dimApproveResourceTypeSql, ParameterizedBeanPropertyRowMapper.newInstance(DimApproveResourceType.class), new Object[0]);
        CopyOnWriteArrayList approveResourceTypeList = new CopyOnWriteArrayList(approveResourceTypeTemp);
        ConcurrentHashMap approveResourceTypeMap = new ConcurrentHashMap();
        CopyOnWriteArrayList approveResourceTypeKeys = new CopyOnWriteArrayList();
        Iterator var7 = approveResourceTypeList.iterator();

        while(var7.hasNext()) {
            DimApproveResourceType approveResourceType = (DimApproveResourceType)var7.next();
            approveResourceTypeMap.put(approveResourceType.getResourceTypeId(), approveResourceType.getResourceTypeName());
            approveResourceTypeKeys.add(approveResourceType.getResourceTypeId());
        }

        this.cacheKVContainer.put("DIM_APPROVE_RESOURCE_TYPE", approveResourceTypeMap);
        this.cacheListContainer.put("DIM_APPROVE_RESOURCE_TYPE", approveResourceTypeList);
        this.cacheKeyArray.put("DIM_APPROVE_RESOURCE_TYPE", approveResourceTypeKeys);
    }

    private void initDimLabelDataStatus() {
        String labelDataStatusSql = "select DATA_STATUS_ID,DATA_STATUS_NAME, DATA_STATUS_DESC from DIM_LABEL_DATA_STATUS t where t.DATA_STATUS_ID <> 6";
        List labelDataStatusTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(labelDataStatusSql, ParameterizedBeanPropertyRowMapper.newInstance(DimLabelDataStatus.class), new Object[0]);
        CopyOnWriteArrayList labelDataStatusList = new CopyOnWriteArrayList(labelDataStatusTemp);
        ConcurrentHashMap labelDataStatusMap = new ConcurrentHashMap();
        CopyOnWriteArrayList labelDataStatusKeys = new CopyOnWriteArrayList();
        Iterator var7 = labelDataStatusList.iterator();

        while(var7.hasNext()) {
            DimLabelDataStatus labelDataStatus = (DimLabelDataStatus)var7.next();
            labelDataStatusMap.put(labelDataStatus.getDataStatusId(), labelDataStatus.getDataStatusName());
            labelDataStatusKeys.add(labelDataStatus.getDataStatusId());
        }

        this.cacheKVContainer.put("DIM_LABEL_DATA_STATUS", labelDataStatusMap);
        this.cacheListContainer.put("DIM_LABEL_DATA_STATUS", labelDataStatusList);
        this.cacheKeyArray.put("DIM_LABEL_DATA_STATUS", labelDataStatusKeys);
    }

    private void initDimOpLogType() {
        String dimOpLogTypeSql = "select OP_TYPE_ID,OP_TYPE_NAME, PARENT_ID,DESC_TXT,SORT_NUM from DIM_OP_LOG_TYPE order by SORT_NUM ";
        List dimOpLogTypeTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(dimOpLogTypeSql, ParameterizedBeanPropertyRowMapper.newInstance(DimOpLogType.class), new Object[0]);
        CopyOnWriteArrayList dimOpLogTypeList = new CopyOnWriteArrayList(dimOpLogTypeTemp);
        ConcurrentHashMap dimOpLogTypeMap = new ConcurrentHashMap();
        CopyOnWriteArrayList dimOpLogTypeKeys = new CopyOnWriteArrayList();
        Iterator var7 = dimOpLogTypeList.iterator();

        while(var7.hasNext()) {
            DimOpLogType dimOpLogType = (DimOpLogType)var7.next();
            dimOpLogTypeMap.put(dimOpLogType.getOpTypeId(), dimOpLogType.getOpTypeName());
            dimOpLogTypeKeys.add(dimOpLogType.getOpTypeId());
        }

        this.cacheKVContainer.put("DIM_OP_LOG_TYPE", dimOpLogTypeMap);
        this.cacheListContainer.put("DIM_OP_LOG_TYPE", dimOpLogTypeList);
        this.cacheKeyArray.put("DIM_OP_LOG_TYPE", dimOpLogTypeKeys);
    }

    public void initDimOpLogTypeDetail() {
        String sql = " SELECT * FROM DIM_OP_LOG_TYPE_DETAIL ";
        List dimOpLogTypeDetailTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(DimOpLogTypeDetail.class), new Object[0]);
        CopyOnWriteArrayList dimOpLogTypeDetailList = new CopyOnWriteArrayList(dimOpLogTypeDetailTemp);
        ConcurrentHashMap dimOpLogTypeDetailMap = new ConcurrentHashMap();
        CopyOnWriteArrayList dimOpLogTypeDetailKeys = new CopyOnWriteArrayList();
        Iterator var7 = dimOpLogTypeDetailList.iterator();

        while(var7.hasNext()) {
            DimOpLogTypeDetail dimOpLogTypeDetail = (DimOpLogTypeDetail)var7.next();
            dimOpLogTypeDetailMap.put(dimOpLogTypeDetail.getOpTypeDetailId(), dimOpLogTypeDetail);
            dimOpLogTypeDetailKeys.add(dimOpLogTypeDetail.getOpTypeDetailId());
        }

        this.cacheKVContainer.put("DIM_OP_LOG_TYPE_DETAIL", dimOpLogTypeDetailMap);
        this.cacheListContainer.put("DIM_OP_LOG_TYPE_DETAIL", dimOpLogTypeDetailList);
        this.cacheKeyArray.put("DIM_OP_LOG_TYPE_DETAIL", dimOpLogTypeDetailKeys);
    }

    private void initDimCustomCreateType() {
        String sql = "select CREATE_TYPE_ID, CREATE_TYPE_NAME, CREATE_TYPE_DESC from DIM_CUSTOM_CREATE_TYPE";
        List createTypeTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(DimCustomCreateType.class), new Object[0]);
        CopyOnWriteArrayList createTypeList = new CopyOnWriteArrayList(createTypeTemp);
        ConcurrentHashMap createTypeMap = new ConcurrentHashMap();
        CopyOnWriteArrayList createTypeKeys = new CopyOnWriteArrayList();
        String productMenu = Configure.getInstance().getProperty("PRODUCT_MENU");
        String customerAnalysisMenu = Configure.getInstance().getProperty("CUSTOMER_ANALYSIS");
        String labelAnalysisMenu = Configure.getInstance().getProperty("LABEL_ANALYSIS");
        Iterator var10 = createTypeList.iterator();

        while(true) {
            while(var10.hasNext()) {
                DimCustomCreateType createType = (DimCustomCreateType)var10.next();
                if("false".equals(productMenu) && (8 == createType.getCreateTypeId().intValue() || 9 == createType.getCreateTypeId().intValue())) {
                    createTypeList.remove(createType);
                } else if("false".equals(customerAnalysisMenu) && (5 == createType.getCreateTypeId().intValue() || 6 == createType.getCreateTypeId().intValue() || 2 == createType.getCreateTypeId().intValue() || 10 == createType.getCreateTypeId().intValue())) {
                    createTypeList.remove(createType);
                } else if("false".equals(labelAnalysisMenu) && (3 == createType.getCreateTypeId().intValue() || 3 == createType.getCreateTypeId().intValue())) {
                    createTypeList.remove(createType);
                } else {
                    createTypeMap.put(createType.getCreateTypeId(), createType.getCreateTypeName());
                    createTypeKeys.add(createType.getCreateTypeId());
                }
            }

            this.cacheKVContainer.put("DIM_CUSTOM_CREATE_TYPE", createTypeMap);
            this.cacheListContainer.put("DIM_CUSTOM_CREATE_TYPE", createTypeList);
            this.cacheKeyArray.put("DIM_CUSTOM_CREATE_TYPE", createTypeKeys);
            return;
        }
    }

    public boolean isExistTable(String tableName) {
        return ((Map)this.cacheKVContainer.get("CI_DIM_TABLE_MAP")).containsKey(tableName);
    }

    private void initDimCustomDataStatus() {
        String sql = "select DATA_STATUS_ID, DATA_STATUS_NAME, DATA_STATUS_DESC, SORT_NUM from DIM_CUSTOM_DATA_STATUS order by SORT_NUM asc";
        List customDataStatusTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(DimCustomDataStatus.class), new Object[0]);
        CopyOnWriteArrayList customDataStatusList = new CopyOnWriteArrayList(customDataStatusTemp);
        ConcurrentHashMap customDataStatusMap = new ConcurrentHashMap();
        CopyOnWriteArrayList customDataStatusKeys = new CopyOnWriteArrayList();
        Iterator var7 = customDataStatusList.iterator();

        while(var7.hasNext()) {
            DimCustomDataStatus customDataStatus = (DimCustomDataStatus)var7.next();
            customDataStatusMap.put(customDataStatus.getDataStatusId(), customDataStatus.getDataStatusName());
            customDataStatusKeys.add(customDataStatus.getDataStatusId());
        }

        this.cacheKVContainer.put("DIM_CUSTOM_DATA_STATUS", customDataStatusMap);
        this.cacheListContainer.put("DIM_CUSTOM_DATA_STATUS", customDataStatusList);
        this.cacheKeyArray.put("DIM_CUSTOM_DATA_STATUS", customDataStatusKeys);
    }

    private void initDimBrand() {
        String sql = "select BRAND_ID, BRAND_NAME, BRAND_DESC, PARENT_ID, COLUMN_ID, SORT_NUM from DIM_BRAND order by SORT_NUM asc";
        List brandTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(DimBrand.class), new Object[0]);
        CopyOnWriteArrayList brandList = new CopyOnWriteArrayList(brandTemp);
        ConcurrentHashMap brandMap = new ConcurrentHashMap();
        CopyOnWriteArrayList brandIds = new CopyOnWriteArrayList();
        Iterator var7 = brandList.iterator();

        while(var7.hasNext()) {
            DimBrand brand = (DimBrand)var7.next();
            brandMap.put(brand.getBrandId(), brand.getBrandName());
            brandIds.add(brand.getBrandId());
        }

        this.cacheKVContainer.put("DIM_BRAND", brandMap);
        this.cacheListContainer.put("DIM_BRAND", brandList);
        this.cacheKeyArray.put("DIM_BRAND", brandIds);
    }

    private void initDimCityThreadConfig() {
        String sql = "select CITY_ID, CITY_NAME, THREAD_NUM from DIM_CITY_THREAD_CONFIG";
        List cityThreadConfigTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(DimCityThreadConfig.class), new Object[0]);
        CopyOnWriteArrayList cityThreadConfigList = new CopyOnWriteArrayList(cityThreadConfigTemp);
        ConcurrentHashMap cityThreadConfigMap = new ConcurrentHashMap();
        CopyOnWriteArrayList cityThreadConfigIds = new CopyOnWriteArrayList();
        Iterator var7 = cityThreadConfigList.iterator();

        while(var7.hasNext()) {
            DimCityThreadConfig cityThreadConfig = (DimCityThreadConfig)var7.next();
            cityThreadConfigMap.put(cityThreadConfig.getCityId(), cityThreadConfig.getThreadNum());
            cityThreadConfigIds.add(cityThreadConfig.getCityId());
        }

        this.cacheKVContainer.put("DIM_CITY_THREAD_CONFIG", cityThreadConfigMap);
        this.cacheListContainer.put("DIM_CITY_THREAD_CONFIG", cityThreadConfigList);
        this.cacheKeyArray.put("DIM_CITY_THREAD_CONFIG", cityThreadConfigIds);
    }

    private void initDimCityCustomGroupNum() {
        String sql = "select CITY_ID, CITY_NAME, CUSTOMGROUP_NUM from DIM_CITY_DIALY_CUSTOMGROUP_NUM";
        List cityCustomGroupNumTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(DimCityDialyCustomGroupNum.class), new Object[0]);
        CopyOnWriteArrayList cityCustomGroupNumList = new CopyOnWriteArrayList(cityCustomGroupNumTemp);
        ConcurrentHashMap cityCustomGroupNumMap = new ConcurrentHashMap();
        CopyOnWriteArrayList cityCustomGroupNumIds = new CopyOnWriteArrayList();
        Iterator var7 = cityCustomGroupNumList.iterator();

        while(var7.hasNext()) {
            DimCityDialyCustomGroupNum cityCustomGroupNum = (DimCityDialyCustomGroupNum)var7.next();
            cityCustomGroupNumMap.put(cityCustomGroupNum.getCityId(), cityCustomGroupNum.getCustomgroupNum());
            cityCustomGroupNumIds.add(cityCustomGroupNum.getCityId());
        }

        this.cacheKVContainer.put("DIM_CITY_DIALY_CUSTOMGROUP_NUM", cityCustomGroupNumMap);
        this.cacheListContainer.put("DIM_CITY_DIALY_CUSTOMGROUP_NUM", cityCustomGroupNumList);
        this.cacheKeyArray.put("DIM_CITY_DIALY_CUSTOMGROUP_NUM", cityCustomGroupNumIds);
    }

    private void initDimVipLevel() {
        String sql = "select VIP_LEVEL_ID, VIP_LEVEL_NAME, VIP_LEVEL_DESC, COLUMN_ID, SORT_NUM from DIM_VIP_LEVEL order by SORT_NUM asc";
        List vipLevelTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(DimVipLevel.class), new Object[0]);
        CopyOnWriteArrayList vipLevelList = new CopyOnWriteArrayList(vipLevelTemp);
        ConcurrentHashMap vipLevelMap = new ConcurrentHashMap();
        CopyOnWriteArrayList vipLevelIds = new CopyOnWriteArrayList();
        Iterator var7 = vipLevelList.iterator();

        while(var7.hasNext()) {
            DimVipLevel vipLevel = (DimVipLevel)var7.next();
            vipLevelMap.put(vipLevel.getVipLevelId(), vipLevel.getVipLevelName());
            vipLevelIds.add(vipLevel.getVipLevelId());
        }

        this.cacheKVContainer.put("DIM_VIP_LEVEL", vipLevelMap);
        this.cacheListContainer.put("DIM_VIP_LEVEL", vipLevelList);
        this.cacheKeyArray.put("DIM_VIP_LEVEL", vipLevelIds);
    }

    private void initDimCity() {
        String sql = "select CITY_ID, CITY_NAME, CITY_DESC, PARENT_ID, COLUMN_ID, SORT_NUM from DIM_CITY order by SORT_NUM asc";
        List cityTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(DimCity.class), new Object[0]);
        CopyOnWriteArrayList cityList = new CopyOnWriteArrayList(cityTemp);
        ConcurrentHashMap cityMap = new ConcurrentHashMap();
        CopyOnWriteArrayList cityIds = new CopyOnWriteArrayList();
        String centerCity = Configure.getInstance().getProperty("CENTER_CITYID");
        int centerCityId = Integer.parseInt(centerCity);
        CopyOnWriteArrayList onlyCityIds = new CopyOnWriteArrayList();
        Iterator var10 = cityList.iterator();

        while(var10.hasNext()) {
            DimCity city = (DimCity)var10.next();
            cityMap.put(city.getCityId(), city.getCityName());
            cityIds.add(city.getCityId());
            if(city.getParentId().intValue() == centerCityId) {
                onlyCityIds.add(city.getCityId().toString());
            }
        }

        this.cacheKVContainer.put("DIM_CITY", cityMap);
        this.cacheListContainer.put("DIM_CITY", cityList);
        this.cacheKeyArray.put("DIM_CITY", cityIds);
        this.cacheKeyArray.put("CITY_IDS_LIST", onlyCityIds);
    }

    public void initDimApproveStatus() {
        String dimAppriveStatusSql = " SELECT DISTINCT a.APPROVE_STATUS_ID,a.APPROVE_ROLE_ID,a.APPROVE_STATUS_NAME,a.APPROVE_STATUS_DESC,a.SORT_NUM, b.PROCESS_ID FROM DIM_APPROVE_STATUS a LEFT JOIN DIM_APPROVE_LEVEL b ON a.APPROVE_ROLE_ID=b.APPROVE_ROLE_ID ORDER BY a.APPROVE_STATUS_ID  ";
        List dimApproveStatusTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(dimAppriveStatusSql, ParameterizedBeanPropertyRowMapper.newInstance(DimApproveStatus.class), new Object[0]);
        CopyOnWriteArrayList dimApproveStatusList = new CopyOnWriteArrayList(dimApproveStatusTemp);
        ConcurrentHashMap dimApproveStatusMap = new ConcurrentHashMap();
        CopyOnWriteArrayList dimApproveStatusKeys = new CopyOnWriteArrayList();
        Iterator var7 = dimApproveStatusList.iterator();

        while(var7.hasNext()) {
            DimApproveStatus dimApproveStatus = (DimApproveStatus)var7.next();
            dimApproveStatusMap.put(dimApproveStatus.getApproveStatusId(), dimApproveStatus.getApproveStatusName());
            dimApproveStatusKeys.add(dimApproveStatus.getApproveStatusId());
        }

        this.cacheKVContainer.put("DIM_APPROVE_STATUS", dimApproveStatusMap);
        this.cacheListContainer.put("DIM_APPROVE_STATUS", dimApproveStatusList);
        this.cacheKeyArray.put("DIM_APPROVE_STATUS", dimApproveStatusKeys);
    }

    public void initAllCiMarketTask() {
        String ciMarketTaskSql = "select * from ci_market_task order by sort_num";
        List ciMarketTaskTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(ciMarketTaskSql, ParameterizedBeanPropertyRowMapper.newInstance(CiMarketTask.class), new Object[0]);
        CopyOnWriteArrayList ciMarketTaskList = new CopyOnWriteArrayList(ciMarketTaskTemp);
        HashMap ciMarketTaskMap = new HashMap();
        CopyOnWriteArrayList ciMarketTaskKeys = new CopyOnWriteArrayList();
        Iterator var7 = ciMarketTaskList.iterator();

        while(var7.hasNext()) {
            CiMarketTask marketTask = (CiMarketTask)var7.next();
            ciMarketTaskMap.put(marketTask.getMarketTaskId(), marketTask);
            ciMarketTaskKeys.add(marketTask.getMarketTaskId());
        }

        this.cacheKVContainer.put("CI_MARKET_TASK_MAP", ciMarketTaskMap);
        this.cacheListContainer.put("CI_MARKET_TASK_MAP", ciMarketTaskList);
        this.cacheKeyArray.put("CI_MARKET_TASK_MAP", ciMarketTaskKeys);
        this.initCiMarketTaskTree();
    }

    public void initExternalLabelInfo() {
        SysInfo sysInfo = new SysInfo();
        sysInfo.setSysId("vgop");

        try {
            System.out.println(" ===================>>>>  初始化外部系统标签 start");
            CopyOnWriteArrayList e = new CopyOnWriteArrayList(this.labelInfoCacheService.queryLabelCategoryInfo(sysInfo));
            System.out.println("categoryList == null: " + e == null?"null":Integer.valueOf(e.size()));
            CopyOnWriteArrayList externalLabelRelList = new CopyOnWriteArrayList(this.labelInfoCacheService.queryExternalSysLabelRel());
            System.out.println("externalLabelRelList == null: " + externalLabelRelList == null?"null":Integer.valueOf(externalLabelRelList.size()));
            CopyOnWriteArrayList allExternalLabelList = new CopyOnWriteArrayList(this.labelInfoCacheService.queryLabelInfo(sysInfo));
            System.out.println("allExternalLabelList == null: " + allExternalLabelList == null?"null":Integer.valueOf(allExternalLabelList.size()));
            this.cacheListContainer.put(sysInfo.getSysId() + "_LABEL_CATEGORY_LIST", e);
            this.cacheListContainer.put(sysInfo.getSysId() + "_SYS_LABEL_REL_LIST", externalLabelRelList);
            this.cacheListContainer.put(sysInfo.getSysId() + "_ALL_LABEL_LIST", allExternalLabelList);
            System.out.println(" ========================初始化外部系统标签 end");
        } catch (Exception var5) {
            this.log.error("查询外部系统分类与标签的对应关系异常", var5);
            var5.printStackTrace();
        }

    }

    public void initCiMarketTaskTree() {
        List ciMarketTaskList = (List)this.cacheListContainer.get("CI_MARKET_TASK_MAP");
        CopyOnWriteArrayList treeList = new CopyOnWriteArrayList();
        Iterator var4 = ciMarketTaskList.iterator();

        while(true) {
            CiMarketTask marketTask;
            do {
                if(!var4.hasNext()) {
                    this.cacheListContainer.put("CI_MARKET_TASK_TREE", treeList);
                    return;
                }

                marketTask = (CiMarketTask)var4.next();
            } while(marketTask.getParentId() != null);

            ArrayList childCiMarketTaskList = new ArrayList();
            Iterator var7 = ciMarketTaskList.iterator();

            while(var7.hasNext()) {
                CiMarketTask childMarketTask = (CiMarketTask)var7.next();
                if(childMarketTask.getParentId() != null && marketTask.getMarketTaskId().equals(childMarketTask.getParentId())) {
                    childCiMarketTaskList.add(childMarketTask);
                }
            }

            marketTask.setChildCiMarketTaskList(childCiMarketTaskList);
            treeList.add(marketTask);
        }
    }

    public List<CiMarketTask> getCiMarketTree() {
        return (List)this.cacheListContainer.get("CI_MARKET_TASK_TREE");
    }

    public void initAllEffectiveLabel() {
        int dayStatus = this.getNewLabelDayStatus().intValue();
        String dayDataDate = this.getNewLabelDay();
        if(dayStatus == 0) {
            try {
                List monthStatus = this.dimCocLabelStatusService.queryDimCocLabelStatusList(dayDataDate);
                int monthDataDate = this.labelStatService.updateCiLabelInfoDataDate(monthStatus, dayDataDate);
                this.log.debug("更新标签最新日记录数：" + monthDataDate);
            } catch (Exception var19) {
                this.log.error("更新标签最新日统计周期异常", var19);
                var19.printStackTrace();
            }
        }

        int monthStatus1 = this.getNewLabelMonthStatus().intValue();
        String monthDataDate1 = this.getNewLabelMonth();
        List ciLabelInfoTemp ;
        if(monthStatus1 == 0) {
            try {
                ciLabelInfoTemp = this.dimCocLabelStatusService.queryDimCocLabelStatusList(monthDataDate1);
                int ciLabelInfoList = this.labelStatService.updateCiLabelInfoDataDate(ciLabelInfoTemp, monthDataDate1);
                this.log.debug("更新标签最新月记录数：" + ciLabelInfoList);
            } catch (Exception var18) {
                this.log.error("更新标签最新月统计周期异常", var18);
                var18.printStackTrace();
            }
        }
        /*------------------------开始过滤标签----lewislee 2016-11-09 10:02----------------------------------------*/
        /*
        List<CiLabelInfo>  ciLabelInfoTempAll=this.ciLabelInfoService.queryAllEffectiveLabel();
//        ciLabelInfoTemp=null;
        List<CiLabelInfo> rList=new ArrayList<CiLabelInfo>();
        System.out.println("开始过滤显示标签"+ciLabelInfoTempAll.size());
        Map <String,String> hiddenLabel= this.getHiddenLabelMap();
        if (hiddenLabel==null){
            this.initHiddenLabelMap();
            hiddenLabel=this.getHiddenLabelMap();
        }
        System.out.println("需要过滤的标签："+hiddenLabel.toString());
        if (hiddenLabel!=null){
            Iterator<CiLabelInfo> iterator=ciLabelInfoTempAll.iterator();
            while (iterator.hasNext()){
                CiLabelInfo ciLabelInfo=iterator.next();
                if (hiddenLabel.get(String.valueOf(ciLabelInfo.getLabelId()))!=null){
                    System.out.println("过滤标签："+ciLabelInfo.getLabelId()+"\t"+ciLabelInfo.getLabelName());
                }else {
                    if (ciLabelInfo!=null){
                        System.out.println("标签："+ciLabelInfo.getLabelId()+ciLabelInfo.getLabelName()+ciLabelInfo.getParentId());
                        rList.add(ciLabelInfo);
                    }
                }
            }
        }
        System.out.println("过滤显示标签结束"+rList.size());
        */
        /*------------------------------------------------过滤标签结束---------------------------------------------*/
        ciLabelInfoTemp=this.ciLabelInfoService.queryAllEffectiveLabel();
//        CopyOnWriteArrayList ciLabelInfoList1 = new CopyOnWriteArrayList(rList);
        CopyOnWriteArrayList ciLabelInfoList1 = new CopyOnWriteArrayList(ciLabelInfoTemp);
        ConcurrentHashMap ciLabelInfoMap = new ConcurrentHashMap();
        ConcurrentHashMap columnMap = new ConcurrentHashMap();
        CopyOnWriteArrayList labelIds = new CopyOnWriteArrayList();
        Iterator var11 = ciLabelInfoList1.iterator();

        while(true) {
            CiLabelInfo c;
            int labelType;
            do {
                CiMdaSysTableColumn col;
                do {
                    if(!var11.hasNext()) {
                        this.cacheKVContainer.put("ALL_EFFECTIVE_LABEL_MAP", ciLabelInfoMap);
                        this.cacheListContainer.put("ALL_EFFECTIVE_LABEL_MAP", ciLabelInfoList1);
                        this.cacheKeyArray.put("ALL_EFFECTIVE_LABEL_MAP", labelIds);
                        this.cacheKVContainer.put("ALL_EFFECTIVE_LABEL_COLUMNS", columnMap);
                        return;
                    }

                    c = (CiLabelInfo)var11.next();
                    labelIds.add("" + c.getLabelId());
                    ciLabelInfoMap.put("LABEL_" + c.getLabelId(), c);
                    labelType = c.getLabelTypeId().intValue();
                    col = c.getCiLabelExtInfo().getCiMdaSysTableColumn();
                } while(col == null);

                columnMap.put("COLUMN_" + col.getColumnId(), col);
            } while(8 != labelType);

            Set relSet = c.getCiLabelExtInfo().getCiLabelVerticalColumnRels();
            Iterator var16 = relSet.iterator();

            while(var16.hasNext()) {
                CiLabelVerticalColumnRel rel = (CiLabelVerticalColumnRel)var16.next();
                CiMdaSysTableColumn child = rel.getCiMdaSysTableColumn();
                columnMap.put("COLUMN_" + child.getColumnId(), child);
            }
        }
    }

    public void initAllEffectiveProduct() {
        List ciProductInfoTemp = this.ciProductService.queryEffectiveCiProductInfo();
        CopyOnWriteArrayList ciProductInfoList = new CopyOnWriteArrayList(ciProductInfoTemp);
        ConcurrentHashMap ciProductInfoMap = new ConcurrentHashMap();
        CopyOnWriteArrayList productIds = new CopyOnWriteArrayList();
        Iterator var6 = ciProductInfoList.iterator();

        while(var6.hasNext()) {
            CiProductInfo p = (CiProductInfo)var6.next();
            productIds.add("" + p.getProductId());
            ciProductInfoMap.put("PRODUCT_" + p.getProductId(), p);
        }

        this.cacheKVContainer.put("ALL_EFFECTIVE_PRODUCT_MAP", ciProductInfoMap);
        this.cacheListContainer.put("ALL_EFFECTIVE_PRODUCT_MAP", ciProductInfoList);
        this.cacheKeyArray.put("ALL_EFFECTIVE_PRODUCT_MAP", productIds);
    }

    private void initCiProductCategory() {
        String sql = "select CATEGORY_ID, PARENT_ID, CATEGORY_NAME, DESC_TXT, IS_LEAF from CI_PRODUCT_CATEGORY ";
        List categoryTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiProductCategory.class), new Object[0]);
        CopyOnWriteArrayList categoryList = new CopyOnWriteArrayList(categoryTemp);
        ConcurrentHashMap categoryMap = new ConcurrentHashMap();
        CopyOnWriteArrayList categoryIds = new CopyOnWriteArrayList();
        Iterator var7 = categoryList.iterator();

        while(var7.hasNext()) {
            CiProductCategory category = (CiProductCategory)var7.next();
            categoryIds.add("" + category.getCategoryId());
            categoryMap.put("PRODUCT_CATEGORY_" + category.getCategoryId(), category);
        }

        this.cacheKVContainer.put("ALL_PRODUCT_CATEGORY_MAP", categoryMap);
        this.cacheListContainer.put("ALL_PRODUCT_CATEGORY_MAP", categoryList);
        this.cacheKeyArray.put("ALL_PRODUCT_CATEGORY_MAP", categoryIds);
    }

    private void initCiNewestLabelDate() {
        String sql = "select DAY_NEWEST_DATE, MONTH_NEWEST_DATE,DAY_NEWEST_STATUS,MONTH_NEWEST_STATUS from CI_NEWEST_LABEL_DATE ";
        List newDateTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiNewestLabelDate.class), new Object[0]);
        CopyOnWriteArrayList newDateList = new CopyOnWriteArrayList(newDateTemp);
        ConcurrentHashMap newDateMap = new ConcurrentHashMap();
        Iterator var6 = newDateList.iterator();

        while(var6.hasNext()) {
            CiNewestLabelDate newDate = (CiNewestLabelDate)var6.next();
            newDateMap.put("NEW_DATE", newDate);
        }

        this.cacheKVContainer.put("CI_NEWEST_LABEL_DATE", newDateMap);
        this.cacheListContainer.put("CI_NEWEST_LABEL_DATE", newDateList);
    }

    private void initDimScene() {
        String dimSceneSql = "select scene_id,scene_name,scene_desc,SORT_NUM  from DIM_SCENE order by SORT_NUM ";
        List dimSceneTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(dimSceneSql, ParameterizedBeanPropertyRowMapper.newInstance(DimScene.class), new Object[0]);
        CopyOnWriteArrayList dimSceneList = new CopyOnWriteArrayList(dimSceneTemp);
        ConcurrentHashMap dimSceneMap = new ConcurrentHashMap();
        CopyOnWriteArrayList dimSceneKeys = new CopyOnWriteArrayList();
        Iterator var7 = dimSceneList.iterator();

        while(var7.hasNext()) {
            DimScene dimScene = (DimScene)var7.next();
            dimSceneMap.put(dimScene.getSceneId(), dimScene.getSceneName());
            dimSceneKeys.add(dimScene.getSceneId());
        }

        this.cacheKVContainer.put("DIM_SCENE", dimSceneMap);
        this.cacheListContainer.put("DIM_SCENE", dimSceneList);
        this.cacheKeyArray.put("DIM_SCENE", dimSceneKeys);
    }

    private void initDimAnnouncementType() {
        String dimAnnouncementTypeSql = "SELECT TYPE_ID,TYPE_NAME,TYPE_DESC,HAS_SUCCESS_FAIL FROM DIM_ANNOUNCEMENT_TYPE";
        List dimAnnouncementTypeTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(dimAnnouncementTypeSql, ParameterizedBeanPropertyRowMapper.newInstance(DimAnnouncementType.class), new Object[0]);
        CopyOnWriteArrayList dimAnnouncementTypeList = new CopyOnWriteArrayList(dimAnnouncementTypeTemp);
        ConcurrentHashMap dimAnnouncementTypeMap = new ConcurrentHashMap();
        CopyOnWriteArrayList dimAnnouncementTypeKeys = new CopyOnWriteArrayList();
        Iterator var7 = dimAnnouncementTypeList.iterator();

        while(var7.hasNext()) {
            DimAnnouncementType dimAnnouncementType = (DimAnnouncementType)var7.next();
            dimAnnouncementTypeMap.put(dimAnnouncementType.getTypeId(), dimAnnouncementType.getTypeName());
            dimAnnouncementTypeKeys.add(dimAnnouncementType.getTypeId());
        }

        this.cacheKVContainer.put("DIM_ANNOUNCEMENT_TYPE", dimAnnouncementTypeMap);
        this.cacheListContainer.put("DIM_ANNOUNCEMENT_TYPE", dimAnnouncementTypeList);
        this.cacheKeyArray.put("DIM_ANNOUNCEMENT_TYPE", dimAnnouncementTypeKeys);
    }

    private void initDimListTactics() {
        String dimListTacticsSql = "SELECT TACTICS_ID,TACTICS_NAME,DESC_TXT FROM DIM_LIST_TACTICS";
        List dimAnnouncementTypeTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(dimListTacticsSql, ParameterizedBeanPropertyRowMapper.newInstance(DimListTactics.class), new Object[0]);
        CopyOnWriteArrayList dimListTacticsList = new CopyOnWriteArrayList(dimAnnouncementTypeTemp);
        ConcurrentHashMap dimListTacticsMap = new ConcurrentHashMap();
        CopyOnWriteArrayList dimListTacticsKeys = new CopyOnWriteArrayList();
        Iterator var7 = dimListTacticsList.iterator();

        while(var7.hasNext()) {
            DimListTactics dimListTactics = (DimListTactics)var7.next();
            dimListTacticsMap.put(dimListTactics.getTacticsId(), dimListTactics.getTacticsName());
            dimListTacticsKeys.add(dimListTactics.getTacticsId());
        }

        this.cacheKVContainer.put("DIM_LIST_TACTICS", dimListTacticsMap);
        this.cacheListContainer.put("DIM_LIST_TACTICS", dimListTacticsList);
        this.cacheKeyArray.put("DIM_LIST_TACTICS", dimListTacticsKeys);
    }

    public void initMdaSysTable() {
        String sql = "select TABLE_ID,TABLE_NAME,TABLE_CN_NAME,TABLE_DESC,TABLE_POSTFIX,TABLE_SCHEMA,TABLE_TYPE,UPDATE_CYCLE  from CI_MDA_SYS_TABLE";
        List sysTableList = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiMdaSysTable.class), new Object[0]);
        HashMap sysTableMap = new HashMap();
        CopyOnWriteArrayList sysTableKeys = new CopyOnWriteArrayList();
        CopyOnWriteArrayList copySysTableList = new CopyOnWriteArrayList(sysTableList);
        Iterator var7 = sysTableList.iterator();

        while(var7.hasNext()) {
            CiMdaSysTable sysTable = (CiMdaSysTable)var7.next();
            sysTableMap.put(sysTable.getTableId(), sysTable.getTableName());
            sysTableKeys.add(sysTable.getTableId());
        }

        this.cacheKVContainer.put("CI_SYS_TABLE_MAP", sysTableMap);
        this.cacheListContainer.put("CI_SYS_TABLE_MAP", copySysTableList);
        this.cacheKeyArray.put("CI_SYS_TABLE_MAP", sysTableKeys);
    }

    private void initDimLabelType() {
        String dimLabelTypeSql = " select LABEL_TYPE_ID,LABEL_TYPE_NAME,LABEL_TYPE_DESC  from DIM_LABEL_TYPE ";
        List dimLabelTypeTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(dimLabelTypeSql, ParameterizedBeanPropertyRowMapper.newInstance(DimLabelType.class), new Object[0]);
        CopyOnWriteArrayList dimLabelTypeList = new CopyOnWriteArrayList(dimLabelTypeTemp);
        ConcurrentHashMap dimLabelTypeMap = new ConcurrentHashMap();
        CopyOnWriteArrayList dimLabelTypeKeys = new CopyOnWriteArrayList();
        Iterator var7 = dimLabelTypeList.iterator();

        while(var7.hasNext()) {
            DimLabelType dimLabelType = (DimLabelType)var7.next();
            dimLabelTypeMap.put(dimLabelType.getLabelTypeId(), dimLabelType.getLabelTypeName());
            dimLabelTypeKeys.add(dimLabelType.getLabelTypeId());
        }

        this.cacheKVContainer.put("DIM_LABEL_TYPE", dimLabelTypeMap);
        this.cacheListContainer.put("DIM_LABEL_TYPE", dimLabelTypeList);
        this.cacheKeyArray.put("DIM_LABEL_TYPE", dimLabelTypeKeys);
    }

    public void initAllLabelColumnName() {
        StringBuffer allLabelColumnNameSql = new StringBuffer();
        allLabelColumnNameSql.append("SELECT L.LABEL_ID,C.COLUMN_NAME FROM CI_LABEL_INFO L,CI_LABEL_EXT_INFO E,CI_MDA_SYS_TABLE_COLUMN C ").append("WHERE L.LABEL_ID = E.LABEL_ID AND C.COLUMN_ID = E.COLUMN_ID");
        List list = this.jdbcBaseDao.getSimpleJdbcTemplate().queryForList(allLabelColumnNameSql.toString(), new Object[0]);
        ConcurrentHashMap allLabelColumnNameMap = new ConcurrentHashMap();
        Iterator var5 = list.iterator();

        while(var5.hasNext()) {
            Map map = (Map)var5.next();
            allLabelColumnNameMap.put(map.get("LABEL_ID"), map.get("COLUMN_NAME"));
        }

        this.cacheKVContainer.put("TABEL_ALL_LABEL_COLUMN_NAME", allLabelColumnNameMap);
    }

    public void initDimTableDefine() {
        List dimTableListCOC = this.dimTableService2.findDimTableDefineByJndiId(Configure.getInstance().getProperty("JNDI_ID"));
        HashMap dimTableMap = new HashMap();
        CopyOnWriteArrayList dimTableKeys = new CopyOnWriteArrayList();
        Iterator var5 = dimTableListCOC.iterator();

        while(var5.hasNext()) {
            DimTableDefine copyDimTableList = (DimTableDefine)var5.next();
            dimTableMap.put(copyDimTableList.getDimId(), copyDimTableList.getDimId());
            dimTableKeys.add(copyDimTableList.getDimId());
        }

        CopyOnWriteArrayList copyDimTableList1 = new CopyOnWriteArrayList(dimTableListCOC);
        this.cacheKVContainer.put("CI_DIM_TABLE_MAP", dimTableMap);
        this.cacheListContainer.put("CI_DIM_TABLE_MAP", copyDimTableList1);
        this.cacheKeyArray.put("CI_DIM_TABLE_MAP", dimTableKeys);
    }

    public void initMarketScene() {
        String sql = "select scene_id,parent_id,scene_name,scene_desc,sort_num,default_op from ci_market_scene order by sort_num";
        List marketScenes = this.jdbcBaseDao.getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(CiMarketScene.class), new Object[0]);
        CopyOnWriteArrayList copyDimTableList = new CopyOnWriteArrayList(marketScenes);
        this.cacheListContainer.put("CI_MARKET_SCENE_MAP", copyDimTableList);
    }

    private CiLabelInfo getEffectiveLabelInner(String key) {
        return (CiLabelInfo)((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_MAP")).get("LABEL_" + key);
    }

    public CiLabelInfo getEffectiveLabelByUser(String key, String userId) {
        CiLabelInfo labelInfo = (CiLabelInfo)((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_MAP")).get("LABEL_" + key);

        try {
            String e = Configure.getInstance().getProperty("NO_CITY");
            String centerCity = Configure.getInstance().getProperty("CENTER_CITYID");
            if(labelInfo.getLabelTypeId() != null && labelInfo.getLabelTypeId().intValue() == 3 && dimCitySet != null && dimCitySet.size() > 0 && labelInfo.getCiLabelExtInfo().getCiMdaSysTableColumn() != null && StringUtil.isNotEmpty(labelInfo.getCiLabelExtInfo().getCiMdaSysTableColumn().getDimTransId()) && dimCitySet.contains(labelInfo.getCiLabelExtInfo().getCiMdaSysTableColumn().getDimTransId().toUpperCase())) {
                List cities = PrivilegeServiceUtil.getAllUserCityIds(userId);
                if(!cities.contains(e) && !cities.contains(centerCity) && !cities.contains(labelInfo.getCiLabelExtInfo().getAttrVal())) {
                    CiLabelInfo newLabel = labelInfo.clone();
                    newLabel.getCiLabelExtInfo().setIsStatUserNum(Integer.valueOf(0));
                    return newLabel;
                }
            }
        } catch (Exception var8) {
            this.log.error(var8);
            var8.printStackTrace();
        }

        return labelInfo;
    }

    public CiLabelInfo getEffectiveLabel(String key) {
        return (CiLabelInfo)((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_MAP")).get("LABEL_" + key);
    }

    public CiMdaSysTableColumn getEffectiveColumn(String key) {
        return (CiMdaSysTableColumn)((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_COLUMNS")).get("COLUMN_" + key);
    }

    public DimOpLogTypeDetail getDimOpLogTypeDetail(String key) {
        return (DimOpLogTypeDetail)((Map)this.cacheKVContainer.get("DIM_OP_LOG_TYPE_DETAIL")).get(key);
    }

    public CiLabelInfo getCityLabel(String key) {
        return (CiLabelInfo)((Map)this.cacheKVContainer.get("CITY_LABEL_MAP")).get("CITY_" + key);
    }

    public Integer getCityThreadConfigNum(String cityId) {
        return (Integer)((Map)this.cacheKVContainer.get("DIM_CITY_THREAD_CONFIG")).get(cityId);
    }

    public Map<Object, Object> getCityThreadConfigMap() {
        return (Map)this.cacheKVContainer.get("DIM_CITY_THREAD_CONFIG");
    }

    public Map<Object, Object> getCityCustomGroupNumMap() {
        return (Map)this.cacheKVContainer.get("DIM_CITY_DIALY_CUSTOMGROUP_NUM");
    }

    public synchronized void addEffectiveLabel(String key, CiLabelInfo value) {
        int type = value.getLabelId().intValue();
        CiMdaSysTableColumn col = value.getCiLabelExtInfo().getCiMdaSysTableColumn();
        ((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_COLUMNS")).put("COLUMN_" + col.getColumnId(), col);
        if(8 == type) {
            Set labelIds = value.getCiLabelExtInfo().getCiLabelVerticalColumnRels();
            Iterator var7 = labelIds.iterator();

            while(var7.hasNext()) {
                CiLabelVerticalColumnRel ciLabelInfoList = (CiLabelVerticalColumnRel)var7.next();
                CiMdaSysTableColumn child = ciLabelInfoList.getCiMdaSysTableColumn();
                ((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_COLUMNS")).put("COLUMN_" + child.getColumnId(), child);
            }
        }

        ((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_MAP")).put("LABEL_" + key, value);
        CopyOnWriteArrayList labelIds1 = this.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
        labelIds1.add(key);
        this.cacheKeyArray.put("ALL_EFFECTIVE_LABEL_MAP", labelIds1);
        CopyOnWriteArrayList ciLabelInfoList1 = this.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
        ciLabelInfoList1.add(value);
        this.cacheListContainer.put("ALL_EFFECTIVE_LABEL_MAP", ciLabelInfoList1);
    }

    public synchronized void modifyEffectiveLabel(String key, CiLabelInfo value) {
        ((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_MAP")).put("LABEL_" + key, value);
        CopyOnWriteArrayList ciLabelInfoList = this.getObjectList("ALL_EFFECTIVE_LABEL_MAP");
        CiLabelInfo o = this.getEffectiveLabel(key);
        ciLabelInfoList.remove(o);
        ciLabelInfoList.add(value);
        this.cacheListContainer.put("ALL_EFFECTIVE_LABEL_MAP", ciLabelInfoList);
    }

    public synchronized void removeEffectiveLabel(String key) {
        CopyOnWriteArrayList ciLabelInfoList = (CopyOnWriteArrayList)this.cacheListContainer.get("ALL_EFFECTIVE_LABEL_MAP");
        CopyOnWriteArrayList labelIds = this.getKeyList("ALL_EFFECTIVE_LABEL_MAP");
        Iterator extInfo = labelIds.iterator();

        while(extInfo.hasNext()) {
            String info = (String)extInfo.next();
            if(info.equals(key)) {
                labelIds.remove(info);
                break;
            }
        }

        this.cacheKeyArray.put("ALL_EFFECTIVE_LABEL_MAP", labelIds);
        extInfo = ciLabelInfoList.iterator();

        CiLabelInfo info1;
        while(extInfo.hasNext()) {
            info1 = (CiLabelInfo)extInfo.next();
            if(key.equals(String.valueOf(info1.getLabelId()))) {
                ciLabelInfoList.remove(info1);
                break;
            }
        }

        this.cacheListContainer.put("ALL_EFFECTIVE_LABEL_MAP", ciLabelInfoList);
        info1 = (CiLabelInfo)((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_MAP")).get("LABEL_" + key);
        if(info1 != null) {
            CiLabelExtInfo extInfo1 = info1.getCiLabelExtInfo();
            if(extInfo1 != null) {
                CiMdaSysTableColumn col = extInfo1.getCiMdaSysTableColumn();
                int type = info1.getLabelId().intValue();
                if(8 == type) {
                    Set relSet = info1.getCiLabelExtInfo().getCiLabelVerticalColumnRels();
                    Iterator var10 = relSet.iterator();

                    while(var10.hasNext()) {
                        CiLabelVerticalColumnRel rel = (CiLabelVerticalColumnRel)var10.next();
                        CiMdaSysTableColumn child = rel.getCiMdaSysTableColumn();
                        ((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_COLUMNS")).remove("COLUMN_" + child.getColumnId());
                    }
                }

                ((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_COLUMNS")).remove("COLUMN_" + col.getColumnId());
                ((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_LABEL_MAP")).remove("LABEL_" + key);
            }
        }

    }

    public CiProductCategory getProductCategory(String key) {
        return (CiProductCategory)((Map)this.cacheKVContainer.get("ALL_PRODUCT_CATEGORY_MAP")).get("PRODUCT_CATEGORY_" + key);
    }

    public CiMdaSysTable getMdaSysTable(String key) {
        return (CiMdaSysTable)((Map)this.cacheKVContainer.get("CI_SYS_TABLE_MAP")).get(key);
    }

    public synchronized void addProductCategory(String key, CiProductCategory value) {
        ((Map)this.cacheKVContainer.get("ALL_PRODUCT_CATEGORY_MAP")).put("PRODUCT_CATEGORY_" + key, value);
        CopyOnWriteArrayList categoryIds = this.getKeyList("ALL_PRODUCT_CATEGORY_MAP");
        categoryIds.add(key);
        this.cacheKeyArray.put("ALL_PRODUCT_CATEGORY_MAP", categoryIds);
        CopyOnWriteArrayList ciProductCategoryList = this.getObjectList("ALL_PRODUCT_CATEGORY_MAP");
        ciProductCategoryList.add(value);
        this.cacheListContainer.put("ALL_PRODUCT_CATEGORY_MAP", ciProductCategoryList);
    }

    public synchronized void removeProductCategory(String key) {
        CopyOnWriteArrayList ciProductCategoryList = (CopyOnWriteArrayList)this.cacheListContainer.get("ALL_PRODUCT_CATEGORY_MAP");
        CopyOnWriteArrayList categoryIds = this.getKeyList("ALL_PRODUCT_CATEGORY_MAP");
        Iterator var5 = categoryIds.iterator();

        while(var5.hasNext()) {
            String pc = (String)var5.next();
            if(pc.equals(key)) {
                categoryIds.remove(pc);
                break;
            }
        }

        this.cacheKeyArray.put("ALL_PRODUCT_CATEGORY_MAP", categoryIds);
        var5 = ciProductCategoryList.iterator();

        while(var5.hasNext()) {
            CiProductCategory pc1 = (CiProductCategory)var5.next();
            if(key.equals(String.valueOf(pc1.getCategoryId()))) {
                ciProductCategoryList.remove(pc1);
                break;
            }
        }

        this.cacheListContainer.put("ALL_PRODUCT_CATEGORY_MAP", ciProductCategoryList);
        ((Map)this.cacheKVContainer.get("ALL_PRODUCT_CATEGORY_MAP")).remove("PRODUCT_CATEGORY_" + key);
    }

    public CiProductInfo getEffectiveProduct(String key) {
        return (CiProductInfo)((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_PRODUCT_MAP")).get("PRODUCT_" + key);
    }

    public String getDimScene(String key) {
        return (String)((Map)this.cacheKVContainer.get("DIM_SCENE")).get(key);
    }

    public String getDimAnnouncementType(String key) {
        return (String)((Map)this.cacheKVContainer.get("DIM_ANNOUNCEMENT_TYPE")).get(key);
    }

    public String getDimListTactics(String key) {
        return (String)((Map)this.cacheKVContainer.get("DIM_LIST_TACTICS")).get(key);
    }

    public String getDimLabelType(Integer key) {
        return (String)((Map)this.cacheKVContainer.get("DIM_LABEL_TYPE")).get(key);
    }

    public synchronized void addEffectiveProduct(String key, CiProductInfo value) {
        ((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_PRODUCT_MAP")).put("PRODUCT_" + key, value);
        CopyOnWriteArrayList productIds = this.getKeyList("ALL_EFFECTIVE_PRODUCT_MAP");
        productIds.add(key);
        this.cacheKeyArray.put("ALL_EFFECTIVE_PRODUCT_MAP", productIds);
        CopyOnWriteArrayList ciProductInfoList = this.getObjectList("ALL_EFFECTIVE_PRODUCT_MAP");
        ciProductInfoList.add(value);
        this.cacheListContainer.put("ALL_EFFECTIVE_PRODUCT_MAP", ciProductInfoList);
    }

    public synchronized void removeEffectiveProduct(String key) {
        CopyOnWriteArrayList ciProductInfoList = (CopyOnWriteArrayList)this.cacheListContainer.get("ALL_EFFECTIVE_PRODUCT_MAP");
        CopyOnWriteArrayList productIds = this.getKeyList("ALL_EFFECTIVE_PRODUCT_MAP");
        Iterator var5 = productIds.iterator();

        while(var5.hasNext()) {
            String p = (String)var5.next();
            if(p.equals(key)) {
                productIds.remove(p);
                break;
            }
        }

        this.cacheKeyArray.put("ALL_EFFECTIVE_PRODUCT_MAP", productIds);
        var5 = ciProductInfoList.iterator();

        while(var5.hasNext()) {
            CiProductInfo p1 = (CiProductInfo)var5.next();
            if(key.equals(String.valueOf(p1.getProductId()))) {
                ciProductInfoList.remove(p1);
                break;
            }
        }

        this.cacheListContainer.put("ALL_EFFECTIVE_PRODUCT_MAP", ciProductInfoList);
        ((Map)this.cacheKVContainer.get("ALL_EFFECTIVE_PRODUCT_MAP")).remove("PRODUCT_" + key);
    }

    public String getNewLabelDay() {
        CiNewestLabelDate labelDate = (CiNewestLabelDate)((Map)this.cacheKVContainer.get("CI_NEWEST_LABEL_DATE")).get("NEW_DATE");
        return labelDate.getDayNewestDate();
    }

    public Integer getNewLabelDayStatus() {
        CiNewestLabelDate labelDate = (CiNewestLabelDate)((Map)this.cacheKVContainer.get("CI_NEWEST_LABEL_DATE")).get("NEW_DATE");
        return labelDate.getDayNewestStatus();
    }

    public String getNewLabelMonth() {
        CiNewestLabelDate labelDate = (CiNewestLabelDate)((Map)this.cacheKVContainer.get("CI_NEWEST_LABEL_DATE")).get("NEW_DATE");
        return labelDate.getMonthNewestDate();
    }

    public Integer getNewLabelMonthStatus() {
        CiNewestLabelDate labelDate = (CiNewestLabelDate)((Map)this.cacheKVContainer.get("CI_NEWEST_LABEL_DATE")).get("NEW_DATE");
        return labelDate.getMonthNewestStatus();
    }

    private void initCustomerGroupInfo(boolean isStart) {
        try {
            ICustomersManagerService e = (ICustomersManagerService)SystemServiceLocator.getInstance().getService("customersManagerServiceImpl");
            ICustomersListInfoService listInfoSer = (ICustomersListInfoService)SystemServiceLocator.getInstance().getService("customersListInfoServiceImpl");
            e.resetProductAutoMacthFlag4CiCustomGroupInfo();
            List allList = e.queryCiCustomListInfoAll();
            ConcurrentHashMap data = new ConcurrentHashMap();
            HashMap regenMap = new HashMap();
            Iterator var8 = allList.iterator();

            while(true) {
                CiCustomListInfo info;
                while(true) {
                    if(!var8.hasNext()) {
                        if(isStart) {
                            var8 = regenMap.values().iterator();

                            while(var8.hasNext()) {
                                CiCustomGroupInfo info2 = (CiCustomGroupInfo)var8.next();
                                e.genCusomerList(info2);
                            }
                        }

                        this.log.info("reAdd " + regenMap.size() + " info to CustomerListCreaterThread");
                        this.log.info("客户群清单总数：" + allList.size() + ",需要重跑的客户群清单数 :" + regenMap.size() + " info to CustomerListCreaterThread");
                        System.out.println("客户群清单总数：" + allList.size() + ",需要重跑的客户群清单数 :" + regenMap.size() + " info to CustomerListCreaterThread");
                        this.cacheKVContainer.put("CI_CUSTOM_LIST_INFO_MAP", data);
                        return;
                    }

                    info = (CiCustomListInfo)var8.next();
                    if(!isStart || info.getDataStatus() == null || info.getDataStatus().intValue() != 2 && info.getDataStatus().intValue() != 1) {
                        break;
                    }

                    info.setDataStatus(Integer.valueOf(1));
                    CiCustomListInfo list = new CiCustomListInfo();
                    PropertyUtils.copyProperties(list, info);
                    String tabName = Configure.getInstance().getProperty("CUST_LIST_TMP_TABLE");
                    tabName = tabName.replace("YYMMDDHHMISSTTTTTT", "") + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L);
                    list.setListTableName(tabName);
                    list.setExcpInfo("");
                    String ifMultiService = Configure.getInstance().getProperty("CYCLE_CUSTOM_WEBSERVICE");
                    CiCustomGroupInfo info1 = e.queryCiCustomGroupInfoForCache(info.getCustomGroupId());
                    if(info1.getDataStatus().intValue() != 4) {
                        info1.setNewModifyTime(new Date());
                        if(StringUtil.isNotEmpty(ifMultiService) && ifMultiService.equalsIgnoreCase("true")) {
                            String ciListFailureInfo1 = Configure.getInstance().getProperty("CURRENT_SERVER_ID");
                            if(StringUtil.isNotEmpty(ciListFailureInfo1) && ciListFailureInfo1.equalsIgnoreCase(info1.getServerId()) && StringUtil.isEmpty(info1.getListCreateTime())) {
                                CiListFailureInfo earlierThanCustomExeTime1 = new CiListFailureInfo();
                                earlierThanCustomExeTime1.setListTableName(info.getListTableName());
                                earlierThanCustomExeTime1.setInsertTime(new Date());
                                listInfoSer.saveCiListFailureInfo(earlierThanCustomExeTime1);
                                e.saveCiCustomListInfo(list);
                                listInfoSer.deleteCustomerListInfo(info);
                                regenMap.put(info1.getCustomGroupId(), info1);
                                break;
                            }

                            if(StringUtil.isNotEmpty(info1.getListCreateTime())) {
                                boolean earlierThanCustomExeTime = false;
                                earlierThanCustomExeTime = e.isEarlierThanCustomExeTime(info1);
                                if(earlierThanCustomExeTime) {
                                    regenMap.put(info1.getCustomGroupId(), info1);
                                }
                            }
                            break;
                        }

                        CiListFailureInfo ciListFailureInfo = new CiListFailureInfo();
                        ciListFailureInfo.setListTableName(info.getListTableName());
                        ciListFailureInfo.setInsertTime(new Date());
                        listInfoSer.saveCiListFailureInfo(ciListFailureInfo);
                        e.saveCiCustomListInfo(list);
                        listInfoSer.deleteCustomerListInfo(info);
                        regenMap.put(info1.getCustomGroupId(), info1);
                        break;
                    }
                }

                if(data.containsKey(info.getCustomGroupId())) {
                    ((List)data.get(info.getCustomGroupId())).add(info);
                } else {
                    ArrayList list1 = new ArrayList();
                    list1.add(info);
                    data.put(info.getCustomGroupId(), list1);
                }
            }
        } catch (Exception var15) {
            var15.printStackTrace();
            this.log.error("initCustomerGroupInfo error", var15);
        }
    }

    private void initHotCustomGroupInfo() {
        try {
            ICustomersManagerService e = (ICustomersManagerService)SystemServiceLocator.getInstance().getService("customersManagerServiceImpl");
            Pager pager = new Pager();
            pager.setPageNum(1);
            pager.setPageSize(ServiceConstants.SHOW_RECOMMEND_CUSTOMS_NUM);
            pager.setOrder("desc");
            pager.setOrderBy("USECOUNT_HOT");
            CiCustomGroupInfo customGroupInfo = new CiCustomGroupInfo();
            customGroupInfo.setDateType("3");
            customGroupInfo.setIsPrivate(Integer.valueOf(0));
            List hotCustomGroupInfos = e.queryCustomersListTask(pager, (String)null, customGroupInfo);
            ConcurrentHashMap hotCustomGroupInfoMap = new ConcurrentHashMap();
            Iterator var7 = hotCustomGroupInfos.iterator();

            while(var7.hasNext()) {
                CiCustomGroupInfo groupInfo = (CiCustomGroupInfo)var7.next();
                hotCustomGroupInfoMap.put(groupInfo.getCustomGroupId(), groupInfo);
            }

            this.cacheKVContainer.put("HOT_CUSTOMS", hotCustomGroupInfoMap);
            this.log.info("initHotCustomGroupInfo success");
        } catch (Exception var8) {
            this.log.error("initCustomerGroupInfo error", var8);
        }

    }

    private void initHotLabelInfo() {
        try {
            List e = this.ciLabelInfoService.querySysRecommendLabelTask();
            CopyOnWriteArrayList hotLabelInfoList = new CopyOnWriteArrayList(e);
            ConcurrentHashMap hotLabelInfoMap = new ConcurrentHashMap();
            Iterator var5 = hotLabelInfoList.iterator();

            while(var5.hasNext()) {
                LabelDetailInfo detailInfo = (LabelDetailInfo)var5.next();
                hotLabelInfoMap.put(detailInfo.getLabelId(), detailInfo);
            }

            this.cacheKVContainer.put("HOT_LABELS", hotLabelInfoMap);
            this.log.info("initHotLabelInfo success");
        } catch (Exception var6) {
            this.log.error("initHotLabelInfo error", var6);
        }

    }

    public LabelDetailInfo getHotLabelByKey(String name, Object key) {
        return (LabelDetailInfo)((Map)this.cacheKVContainer.get(name)).get(key);
    }

    public CiCustomGroupInfo getHotCustomByKey(String name, Object key) {
        return (CiCustomGroupInfo)((Map)this.cacheKVContainer.get(name)).get(key);
    }

    public <R> R get(String name, String key, Class<R> type) {
        Map dataMap = (Map)this.cacheKVContainer.get(name);
        return (R)(dataMap != null && key != null?dataMap.get(key):null);
    }

    public synchronized void put(String name, String key, Object value) {
        Object dataMap = (Map)this.cacheKVContainer.get(name);
        if(dataMap == null) {
            this.cacheKVContainer.put(name, (Map)dataMap);
        }

        if(key != null) {
            if(value == null) {
                ((Map)dataMap).remove(key);
            } else {
                ((Map)dataMap).put(key, value);
            }
        }

    }

    public CiNoticeModel getNoticeModelByUser(String userId) {
        CiNoticeModel ciNoticeModel = new CiNoticeModel();

        try {
            if(this.userNoticeContainer.containsKey(userId)) {
                ciNoticeModel = (CiNoticeModel)this.userNoticeContainer.get(userId);
            } else {
                CiSysAnnouncement e = new CiSysAnnouncement();
                e.setReadStatus(Integer.valueOf(0));
                List sysAnnouncementList = this.sysAnnouncementService.queryCiUserReadInfoListByUserId(userId, e, 1, 2147483647);
                e.setStatus(Integer.valueOf(1));
                int sysCount = this.sysAnnouncementService.queryCiUserReadInfoCountByUserId(userId, e);
                Object alarmStartDate = null;
                Object alarmEndDate = null;
                int customerAlarmCount = 0;
                int labelAlarmCount = 0;

                try {
                    List personNoticeBean = CIAlarmServiceUtil.getPageAlarmRecords((String)alarmStartDate, (String)alarmEndDate, (String)null, "CustomersAlarm", (String)null, (String)null, (String)null, 0, 0, 2147483647);
                    List personNoticeCount = CIAlarmServiceUtil.getPageAlarmRecords((String)alarmStartDate, (String)alarmEndDate, (String)null, "LabelAlarm", (String)null, (String)null, (String)null, 0, 0, 2147483647);
                    customerAlarmCount = personNoticeBean.size();
                    labelAlarmCount = personNoticeCount.size();
                } catch (Exception var21) {
                    this.log.error("获取预警结果失败", var21);
                }

                CiPersonNotice personNoticeBean1 = new CiPersonNotice();
                personNoticeBean1.setReceiveUserId(userId);
                personNoticeBean1.setStatus(Integer.valueOf(1));
                personNoticeBean1.setReadStatus(Integer.valueOf(1));
                int personNoticeCount1 = this.personNoticeService.getCountOfPersonNotice(personNoticeBean1);
                LinkedHashMap personNoticeMap = new LinkedHashMap();
                LinkedHashMap sysNoticeMap = new LinkedHashMap();
                personNoticeBean1.setIsShowTip(Integer.valueOf(1));
                List personNoticeList = this.personNoticeService.queryPersonNoticeList(personNoticeBean1);
                List userNoticeSet = this.ciUserNoticeSetService.queryUserNoticeSet(userId);
                HashMap setMap = new HashMap();
                HashMap sysSetMap = new HashMap();
                Iterator var19 = userNoticeSet.iterator();

                while(var19.hasNext()) {
                    CiUserNoticeSet sysNotice = (CiUserNoticeSet)var19.next();
                    Object success = new HashMap();
                    if(sysNotice.getId().getNoticeType().intValue() == 2 && setMap.containsKey(Integer.valueOf(Integer.parseInt(sysNotice.getId().getNoticeId())))) {
                        success = (Map)setMap.get(Integer.valueOf(Integer.parseInt(sysNotice.getId().getNoticeId())));
                    }

                    if(sysNotice.getId().getNoticeType().intValue() == 1 && sysSetMap.containsKey(Integer.valueOf(Integer.parseInt(sysNotice.getId().getNoticeId())))) {
                        success = (Map)sysSetMap.get(Integer.valueOf(Integer.parseInt(sysNotice.getId().getNoticeId())));
                    }

                    ((Map)success).put(sysNotice.getId().getIsSuccess(), sysNotice.getIsReceive());
                    if(sysNotice.getId().getNoticeType().intValue() == 2) {
                        setMap.put(Integer.valueOf(Integer.parseInt(sysNotice.getId().getNoticeId())), success);
                    } else if(sysNotice.getId().getNoticeType().intValue() == 1) {
                        sysSetMap.put(Integer.valueOf(Integer.parseInt(sysNotice.getId().getNoticeId())), success);
                    }
                }

                var19 = personNoticeList.iterator();

                Integer success1;
                while(var19.hasNext()) {
                    CiPersonNotice sysNotice1 = (CiPersonNotice)var19.next();
                    success1 = Integer.valueOf(2);
                    if(sysNotice1.getIsSuccess() == null || sysNotice1.getIsSuccess().intValue() == 0) {
                        success1 = Integer.valueOf(1);
                    }

                    if(StringUtil.isNotEmpty(setMap.get(sysNotice1.getNoticeTypeId())) && StringUtil.isNotEmpty(((Map)setMap.get(sysNotice1.getNoticeTypeId())).get(success1)) && ((Integer)((Map)setMap.get(sysNotice1.getNoticeTypeId())).get(success1)).intValue() == 1) {
                        personNoticeMap.put(sysNotice1.getNoticeId(), sysNotice1);
                    }
                }

                var19 = sysAnnouncementList.iterator();

                while(var19.hasNext()) {
                    CiSysAnnouncement sysNotice2 = (CiSysAnnouncement)var19.next();
                    success1 = Integer.valueOf(2);
                    if(StringUtil.isNotEmpty(((Map)sysSetMap.get(sysNotice2.getTypeId())).get(success1)) && ((Integer)((Map)sysSetMap.get(sysNotice2.getTypeId())).get(success1)).intValue() == 1) {
                        sysNoticeMap.put(sysNotice2.getAnnouncementId(), sysNotice2);
                    }
                }

                ciNoticeModel.setSysCount(sysCount);
                ciNoticeModel.setPersonNoticeCount(personNoticeCount1);
                ciNoticeModel.setLabelAlarmCount(labelAlarmCount);
                ciNoticeModel.setCustomerAlarmCount(customerAlarmCount);
                ciNoticeModel.setRecentNoticeMap(personNoticeMap);
                ciNoticeModel.setSysNoticeMap(sysNoticeMap);
                ciNoticeModel.setRecentNoticeCount(personNoticeList.size() + sysAnnouncementList.size());
                this.userNoticeContainer.put(userId, ciNoticeModel);
            }
        } catch (Exception var22) {
            this.log.error("缓存查询通知异常", var22);
            var22.printStackTrace();
        }

        return ciNoticeModel;
    }

    public synchronized void addPersonNotice(CiPersonNotice personNotice) {
        String receiveUserId = personNotice.getReceiveUserId();
        if(!StringUtil.isEmpty(receiveUserId)) {
            List userNoticeSet = this.ciUserNoticeSetService.queryUserNoticeSet(receiveUserId, String.valueOf(2), "1");
            HashMap setMap = new HashMap();
            Iterator ciNoticeModel = userNoticeSet.iterator();

            Object personNoticeMap;
            while(ciNoticeModel.hasNext()) {
                CiUserNoticeSet success = (CiUserNoticeSet)ciNoticeModel.next();
                personNoticeMap = new HashMap();
                if(setMap.containsKey(Integer.valueOf(Integer.parseInt(success.getId().getNoticeId())))) {
                    personNoticeMap = (Map)setMap.get(Integer.valueOf(Integer.parseInt(success.getId().getNoticeId())));
                }

                ((Map)personNoticeMap).put(success.getId().getIsSuccess(), success.getIsReceive());
                setMap.put(Integer.valueOf(Integer.parseInt(success.getId().getNoticeId())), personNoticeMap);
            }

            Integer success1 = Integer.valueOf(1);
            if(personNotice.getIsSuccess() == null || personNotice.getIsSuccess().intValue() == 0) {
                success1 = Integer.valueOf(2);
            }

            CiNoticeModel ciNoticeModel1;
            if(this.userNoticeContainer.containsKey(receiveUserId)) {
                ciNoticeModel1 = (CiNoticeModel)this.userNoticeContainer.get(receiveUserId);
                if(((Integer)((Map)setMap.get(personNotice.getNoticeTypeId())).get(success1)).intValue() == 1) {
                    personNoticeMap = ciNoticeModel1.getRecentNoticeMap();
                    if(personNoticeMap == null) {
                        personNoticeMap = new HashMap();
                    }

                    ((Map)personNoticeMap).put(personNotice.getNoticeId(), personNotice);
                    ciNoticeModel1.setRecentNoticeMap((Map)personNoticeMap);
                }

                ciNoticeModel1.setPersonNoticeCount(ciNoticeModel1.getPersonNoticeCount() + 1);
                ciNoticeModel1.setRecentNoticeCount(ciNoticeModel1.getRecentNoticeCount() + 1);
                this.userNoticeContainer.put(receiveUserId, ciNoticeModel1);
            } else {
                ciNoticeModel1 = new CiNoticeModel();
                if(((Integer)((Map)setMap.get(personNotice.getNoticeTypeId())).get(success1)).intValue() == 1) {
                    HashMap personNoticeMap1 = new HashMap();
                    personNoticeMap1.put(personNotice.getNoticeId(), personNotice);
                    ciNoticeModel1.setRecentNoticeMap(personNoticeMap1);
                    ciNoticeModel1.setRecentNoticeMap(personNoticeMap1);
                }

                ciNoticeModel1.setPersonNoticeCount(1);
                ciNoticeModel1.setRecentNoticeCount(1);
                this.userNoticeContainer.put(receiveUserId, ciNoticeModel1);
            }

        }
    }

    public synchronized void addSysAnnouncement(CiSysAnnouncement sysAnnouncement) {
        Iterator it = this.userNoticeContainer.entrySet().iterator();

        while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            String userId = (String)entry.getKey();
            CiNoticeModel ciNoticeModel = (CiNoticeModel)entry.getValue();
            List userNoticeSet = this.ciUserNoticeSetService.queryUserNoticeSet(userId, String.valueOf(1), "1");
            HashMap sysSetMap = new HashMap();
            Iterator success = userNoticeSet.iterator();

            while(success.hasNext()) {
                CiUserNoticeSet sysNoticeMap = (CiUserNoticeSet)success.next();
                Object tmpMap = new HashMap();
                if(sysNoticeMap.getId().getNoticeType().intValue() == 1 && sysSetMap.containsKey(Integer.valueOf(Integer.parseInt(sysNoticeMap.getId().getNoticeId())))) {
                    tmpMap = (Map)sysSetMap.get(Integer.valueOf(Integer.parseInt(sysNoticeMap.getId().getNoticeId())));
                }

                ((Map)tmpMap).put(sysNoticeMap.getId().getIsSuccess(), sysNoticeMap.getIsReceive());
                if(sysNoticeMap.getId().getNoticeType().intValue() == 1) {
                    sysSetMap.put(Integer.valueOf(Integer.parseInt(sysNoticeMap.getId().getNoticeId())), tmpMap);
                }
            }

            Object sysNoticeMap1 = ciNoticeModel.getSysNoticeMap();
            if(sysNoticeMap1 == null) {
                sysNoticeMap1 = new HashMap();
            }

            Integer success1 = Integer.valueOf(1);
            if(((Integer)((Map)sysSetMap.get(sysAnnouncement.getTypeId())).get(success1)).intValue() == 1) {
                ((Map)sysNoticeMap1).put(sysAnnouncement.getAnnouncementId(), sysAnnouncement);
                ciNoticeModel.setSysNoticeMap((Map)sysNoticeMap1);
            }

            ciNoticeModel.setSysCount(ciNoticeModel.getSysCount() + 1);
            ciNoticeModel.setRecentNoticeCount(ciNoticeModel.getRecentNoticeCount() + 1);
            this.userNoticeContainer.put(userId, ciNoticeModel);
        }

    }

    public synchronized void deleteNoticeUserId(String userId) {
        this.userNoticeContainer.remove(userId);
    }

    public synchronized void closeNotice(String userId, String noticeId) {
        if(StringUtil.isNotEmpty(userId) && StringUtil.isNotEmpty(noticeId)) {
            new CiNoticeModel();
            if(this.userNoticeContainer.containsKey(userId)) {
                CiNoticeModel ciNoticeModel = (CiNoticeModel)this.userNoticeContainer.get(userId);
                Map recentNoticeMap = ciNoticeModel.getRecentNoticeMap();
                if(recentNoticeMap != null && recentNoticeMap.containsKey(noticeId)) {
                    recentNoticeMap.remove(noticeId);
                    ciNoticeModel.setRecentNoticeMap(recentNoticeMap);
                }

                Map sysAnnouncementMap = ciNoticeModel.getSysNoticeMap();
                if(sysAnnouncementMap != null && sysAnnouncementMap.containsKey(noticeId)) {
                    sysAnnouncementMap.remove(noticeId);
                    ciNoticeModel.setSysNoticeMap(sysAnnouncementMap);
                }

                this.userNoticeContainer.put(userId, ciNoticeModel);
            }
        }

    }

    public synchronized void removeNotice(String userId, String noticeId, int typeId) {
        if(StringUtil.isNotEmpty(userId) && StringUtil.isNotEmpty(noticeId)) {
            new CiNoticeModel();
            if(this.userNoticeContainer.containsKey(userId)) {
                CiNoticeModel ciNoticeModel = (CiNoticeModel)this.userNoticeContainer.get(userId);
                Map sysAnnouncementMap;
                int sysCount;
                if(typeId == 2) {
                    sysAnnouncementMap = ciNoticeModel.getRecentNoticeMap();
                    if(sysAnnouncementMap != null && sysAnnouncementMap.containsKey(noticeId)) {
                        sysAnnouncementMap.remove(noticeId);
                        ciNoticeModel.setRecentNoticeMap(sysAnnouncementMap);
                    }

                    sysCount = ciNoticeModel.getPersonNoticeCount();
                    if(sysCount > 0) {
                        --sysCount;
                        ciNoticeModel.setPersonNoticeCount(sysCount);
                    }
                }

                if(typeId == 1) {
                    sysAnnouncementMap = ciNoticeModel.getSysNoticeMap();
                    if(sysAnnouncementMap != null && sysAnnouncementMap.containsKey(noticeId)) {
                        sysAnnouncementMap.remove(noticeId);
                        ciNoticeModel.setSysNoticeMap(sysAnnouncementMap);
                    }

                    sysCount = ciNoticeModel.getSysCount();
                    if(sysCount > 0) {
                        --sysCount;
                        ciNoticeModel.setSysCount(sysCount);
                    }
                }

                this.userNoticeContainer.put(userId, ciNoticeModel);
            }
        }

    }

    public void clearUserCache() {
        if(this.userNoticeContainer != null) {
            this.userNoticeContainer.clear();
        }

    }

    public void refreshSysNoticeCache() {
        CiSysAnnouncement searchBean = new CiSysAnnouncement();
        searchBean.setStatus(Integer.valueOf(1));
        List allCiSysAnnouncementList = this.sysAnnouncementService.querySysAnnouncementListBySysAnnouncement(searchBean);
        Iterator var4 = allCiSysAnnouncementList.iterator();

        while(var4.hasNext()) {
            CiSysAnnouncement ciSysAnnouncement = (CiSysAnnouncement)var4.next();
            Timestamp effectiveTime = ciSysAnnouncement.getEffectiveTime();
            if(effectiveTime != null && effectiveTime.getTime() < (new Date()).getTime()) {
                ciSysAnnouncement.setStatus(Integer.valueOf(2));
                this.sysAnnouncementService.modifySysAnnouncement(ciSysAnnouncement);
            }
        }

    }

    public void refreshLabelAndProduct() {
        this.initAllEffectiveLabel();
        this.initAllEffectiveProduct();
        this.initCiProductCategory();
    }

    public void refreshNewestDayAndMonth() {
        this.initCiNewestLabelDate();
    }

    public void refreshHoLabelAndCustomDaily() {
        this.initHotLabelInfo();
        this.initHotCustomGroupInfo();
    }

    public Map<Object, Object> getCityMap() {
        return (Map)this.cacheKVContainer.get("DIM_CITY");
    }

    public Map<Object, Object> getAllTableColumnNameMap() {
        return (Map)this.cacheKVContainer.get("TABEL_ALL_LABEL_COLUMN_NAME");
    }

    public Integer getLabelTableId(String tableName) {
        Integer tableId = null;
        Map map = (Map)this.cacheKVContainer.get("CI_SYS_TABLE_MAP");
        if(map.containsValue(tableName)) {
            Iterator var5 = map.entrySet().iterator();

            while(var5.hasNext()) {
                Entry entry = (Entry)var5.next();
                if(entry.getValue().equals(tableName)) {
                    tableId = (Integer)entry.getKey();
                }
            }
        }

        return tableId;
    }

    public void initZjUser() {
        String sql = "select user_id from ZJ_USERS";
        List list = this.jdbcBaseDao.getSimpleJdbcTemplate().queryForList(sql.toString(), new Object[0]);
        ArrayList userIds = new ArrayList();
        Iterator var5 = list.iterator();

        while(var5.hasNext()) {
            Map copylist = (Map)var5.next();
            String userId = (String)copylist.get("USER_ID");
            userIds.add(userId);
        }

        CopyOnWriteArrayList copylist1 = new CopyOnWriteArrayList(userIds);
        this.cacheListContainer.put("ZJ_USER", copylist1);
    }

    public List<String> getZjUsers() {
        List list = (List)this.cacheListContainer.get("ZJ_USER");
        return list;
    }

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(1);
        BigDecimal b = (new BigDecimal(0)).multiply(new BigDecimal(100.0D));
        double c = b.divide(a, 2, RoundingMode.HALF_UP).doubleValue();
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        System.out.println(nf.format(c));
    }
}

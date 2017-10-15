package com.ailk.biapp.ci.ia.utils;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.ia.entity.CiIaAnalyseAttr;
import com.ailk.biapp.ci.ia.entity.DimChartInfo;
import com.ailk.biapp.ci.ia.entity.DimGuidelineAggregation;
import com.ailk.biapp.ci.ia.service.IAnalysisAttrService;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

public class Cache {
    private Logger log = Logger.getLogger(Cache.class);
    private static Cache instance = new Cache();
    private Map<String, Map<Object, Object>> cacheKVContainer;
    private Map<String, CopyOnWriteArrayList<?>> cacheListContainer;
    private Map<String, CopyOnWriteArrayList<?>> cacheKeyArray;
    private JdbcBaseDao jdbcBaseDao = null;
    private IAnalysisAttrService analysisAttrService;
    private static boolean isInited = false;

    private Cache() {
    }

    public static Cache getInstance() {
        if(!isInited) {
            try {
                instance.init();
                isInited = true;
            } catch (Exception var1) {
                var1.printStackTrace();
            }
        }

        return instance;
    }

    public void init() throws Exception {
        this.log.debug("加载智能分析缓存数据开始...");
        this.cacheKVContainer = new ConcurrentHashMap();
        this.cacheListContainer = new ConcurrentHashMap();
        this.cacheKeyArray = new ConcurrentHashMap();
        this.jdbcBaseDao = (JdbcBaseDao)SystemServiceLocator.getInstance().getService("jdbcBaseDao");
        this.analysisAttrService = (IAnalysisAttrService)SystemServiceLocator.getInstance().getService("analysisAttrService");
        this.initEffectiveAttr();
        this.intDimChartInfo();
        this.initDimGuidelineAggregation();
        this.log.debug("加载智能分析缓存数据结束...");
    }

    public String getNameByKey(String tableName, Object key) {
        return ((Map)this.cacheKVContainer.get(tableName)).get(key) == null?"":((Map)this.cacheKVContainer.get(tableName)).get(key).toString();
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

    public CiMdaSysTableColumn getEffectiveColumnByAttrId(int attrId) {
        CiIaAnalyseAttr attr = (CiIaAnalyseAttr)this.getObjectByKey("ANALYSE_ATTR", Integer.valueOf(attrId));
        CiMdaSysTableColumn ciMdaSysTableColumn = attr.getCiMdaSysTableColumn();
        if(ciMdaSysTableColumn == null) {
            CiLabelInfo ciLabelInfo = attr.getCiLabelInfo();
            CiLabelExtInfo ciLabelExtInfo = ciLabelInfo.getCiLabelExtInfo();
            ciMdaSysTableColumn = ciLabelExtInfo.getCiMdaSysTableColumn();
        }

        return ciMdaSysTableColumn;
    }

    public CiLabelInfo getLabelInfo(int attrId) {
        CiIaAnalyseAttr attr = (CiIaAnalyseAttr)this.getObjectByKey("ANALYSE_ATTR", Integer.valueOf(attrId));
        return attr == null?null:attr.getCiLabelInfo();
    }

    private void initEffectiveAttr() {
        this.log.debug("加载系统预置分析属性");
        List analyseAttrs = this.analysisAttrService.selectEffectiveAttrBySource(1);
        CopyOnWriteArrayList attrList = new CopyOnWriteArrayList(analyseAttrs);
        ConcurrentHashMap attrMap = new ConcurrentHashMap();
        CopyOnWriteArrayList attrIdKeys = new CopyOnWriteArrayList();
        Iterator i$ = attrList.iterator();

        while(i$.hasNext()) {
            CiIaAnalyseAttr attr = (CiIaAnalyseAttr)i$.next();
            attrMap.put(attr.getAttrId(), attr.getAttrName());
            attrIdKeys.add(attr.getAttrId());
        }

        this.cacheListContainer.put("ANALYSE_ATTR", attrList);
        this.cacheKVContainer.put("ANALYSE_ATTR", attrMap);
        this.cacheKeyArray.put("ANALYSE_ATTR", attrIdKeys);
    }

    private void intDimChartInfo() {
        String cacheSql = "select chart_id,chart_name,chart_desc from DIM_CHART_INFO";
        this.log.debug("加载维表_图表信息[" + cacheSql + "]");
        List dimChartInfoTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(cacheSql, ParameterizedBeanPropertyRowMapper.newInstance(DimChartInfo.class), new Object[0]);
        CopyOnWriteArrayList dimChartInfoList = new CopyOnWriteArrayList(dimChartInfoTemp);
        ConcurrentHashMap dimChartInfoMap = new ConcurrentHashMap();
        Iterator i$ = dimChartInfoList.iterator();

        while(i$.hasNext()) {
            DimChartInfo info = (DimChartInfo)i$.next();
            dimChartInfoMap.put(info.getChartId(), info.getChartName());
        }

        this.cacheListContainer.put("DIM_CHART_INFO", dimChartInfoList);
        this.cacheKVContainer.put("DIM_CHART_INFO", dimChartInfoMap);
    }

    private void initDimGuidelineAggregation() {
        String cacheSql = "select aggregation_id,aggregation_name,aggregation_desc,aggregation_func from DIM_GUIDELINE_AGGREGATION";
        this.log.debug("加载维表_指标聚合方式[" + cacheSql + "]");
        List dimGuidelineAggregationTemp = this.jdbcBaseDao.getSimpleJdbcTemplate().query(cacheSql, ParameterizedBeanPropertyRowMapper.newInstance(DimGuidelineAggregation.class), new Object[0]);
        CopyOnWriteArrayList dimGuidelineAggregationList = new CopyOnWriteArrayList(dimGuidelineAggregationTemp);
        ConcurrentHashMap dimGuidelineAggregationMap = new ConcurrentHashMap();
        Iterator i$ = dimGuidelineAggregationList.iterator();

        while(i$.hasNext()) {
            DimGuidelineAggregation aggregation = (DimGuidelineAggregation)i$.next();
            dimGuidelineAggregationMap.put(aggregation.getAggregationId(), aggregation.getAggregationName());
        }

        this.cacheListContainer.put("DIM_GUIDELINE_AGGREGATION", dimGuidelineAggregationList);
        this.cacheKVContainer.put("DIM_GUIDELINE_AGGREGATION", dimGuidelineAggregationMap);
    }
}

package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.asiainfo.biframe.alarm.alarmRecord.model.AlarmRecord;
import com.asiainfo.biframe.alarm.dao.IAlarmThresholdVarRefDao;
import com.asiainfo.biframe.alarm.facade.IAlarmFacade;
import com.asiainfo.biframe.alarm.model.AlarmRecordDetail;
import com.asiainfo.biframe.alarm.model.AlarmReturn;
import com.asiainfo.biframe.alarm.model.AlarmThreshold;
import com.asiainfo.biframe.alarm.model.BusinessInfo;
import com.asiainfo.biframe.alarm.model.ThresholdVarRef;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class CIAlarmServiceUtil {
    private static Logger log = Logger.getLogger(CIAlarmServiceUtil.class);
    private static IAlarmFacade alarmFacade = null;
    private static IAlarmThresholdVarRefDao alarmThresholdVarRefDao;

    public CIAlarmServiceUtil() {
    }

    public static String createAlarmThreshold(AlarmThreshold alarmThreshold) throws Exception {
        alarmThreshold.setRefType("user");
        alarmThreshold.setRefId(alarmThreshold.getCreator());
        alarmThreshold.setProductId("COC");
        alarmThreshold.setColumnName((String)ServiceConstants.alarmColumnIdToNameMap.get(alarmThreshold.getColumnId()));
        ThresholdVarRef varRef = new ThresholdVarRef();
        varRef.setAttColumn("city_id");
        varRef.setMinValue("-1");
        ArrayList varRefs = new ArrayList();
        varRefs.add(varRef);
        alarmThreshold.setVarRefs(varRefs);
        String thresholdId = alarmFacade.addAlarmThreshold(alarmThreshold);
        return thresholdId;
    }

    public static String updateAlarmThreshold(AlarmThreshold alarmThreshold) throws Exception {
        alarmThreshold.setColumnName((String)ServiceConstants.alarmColumnIdToNameMap.get(alarmThreshold.getColumnId()));
        AlarmThreshold threshold = getAlarmThresholdById(alarmThreshold.getThresholdId());
        List thresholdVarRefList = threshold.getVarRefs();
        alarmFacade.updateAlarmThreshold(alarmThreshold);
        if(CollectionUtils.isNotEmpty(thresholdVarRefList)) {
            ThresholdVarRef varRef = (ThresholdVarRef)thresholdVarRefList.get(0);
            varRef.setConditionId((String)null);
            alarmThresholdVarRefDao.saveOrUpdateThresholdVarRef(varRef);
        }

        return null;
    }

    public static String deleteAlarmThreshold(String threshold) throws Exception {
        alarmFacade.deleteAlarmThreshold(threshold);
        return null;
    }

    public static List<AlarmThreshold> getPageAlarmThresholdsByMore(String userId, String alarmType, String busiId, String columnId, int flag, int pageNum, int pageSize) throws Exception {
        List thresholds = alarmFacade.getPageAlarmThresholdsByMore("COC", (List)null, "user", userId, alarmType, busiId, columnId, flag, pageNum, pageSize);
        return thresholds;
    }

    public static List<AlarmThreshold> getAlarmThresholds(String refType, String userId, String alarmType, String busiId, int flag) throws Exception {
        List thresholds = alarmFacade.getAlarmThresholds(refType, userId, alarmType, busiId, flag);
        return thresholds;
    }

    public static AlarmThreshold getAlarmThresholdById(String thresholdId) throws Exception {
        return alarmFacade.getAlarmThresholdById(thresholdId);
    }

    public static int getCountOfAlarmThresholds(String userId, String alarmType, String busiId, String columnId, int flag) throws Exception {
        int size = alarmFacade.getCountOfAlarmThresholds((List)null, (String)null, userId, alarmType, busiId, columnId, flag);
        return size;
    }

    public static void saveAlarmRecords(List<AlarmRecord> alarmRecords) throws Exception {
        alarmFacade.saveAlarmRecords(alarmRecords);
    }

    public static List<AlarmReturn> createAlarmList(List<BusinessInfo> businessInfos) throws Exception {
        return alarmFacade.createAlarmList(businessInfos);
    }

    public static List<AlarmRecordDetail> getPageAlarmRecordDetails(String startTime, String endTime, String userId, String alarmType, String busiId, String columnId, int pageNum, int pageSize) throws Exception {
        return alarmFacade.getPageAlarmRecordDetailsByMore("COC_PRODUCT_ID", startTime, endTime, (List)null, "user", userId, alarmType, busiId, columnId, pageNum, pageSize);
    }

    public static List<AlarmRecord> getPageAlarmRecords(String startTime, String endTime, String userId, String alarmType, String busiName, String busiId, String columnId, int checkFlag, int pageNum, int pageSize) throws Exception {
        return alarmFacade.getPageAlarmRecordByMore("COC", startTime, endTime, (List)null, (List)null, "user", userId, alarmType, busiName, busiId, columnId, checkFlag, pageNum, pageSize);
    }

    public static void updateCheckFlag(String recordId, int newFlag) throws Exception {
        alarmFacade.updateCheckFlag(recordId, newFlag);
    }

    public static boolean haveAlarmRecords(String date, String userId, String alarmType, String busiId) throws Exception {
        boolean result = false;
        String startTime = date;
        String endTime = date;
        if(null != date) {
            if(date.length() == 6) {
                startTime = date.substring(0, 4) + "-" + date.substring(4, 6) + "-01 00:00:00";
                endTime = DateUtil.date2String(DateUtil.lastDayOfMonth(DateUtil.string2Timestamp(startTime)), "yyyy-MM-dd") + " 23:59:59";
            }

            if(date.length() == 8) {
                startTime = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " 00:00:00";
                endTime = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " 23:59:59";
            }
        }

        List alarmRecordList = getPageAlarmRecords(startTime, endTime, userId, alarmType, (String)null, busiId, (String)null, -1, 0, 2147483647);
        if(CollectionUtils.isNotEmpty(alarmRecordList)) {
            result = true;
        }

        return result;
    }

    public static int getCountOfAlarmRecords(String startTime, String endTime, String refId, String alarmType, String busiName, String busiId, String columnId, String levelId, int checkFlag) throws Exception {
        return alarmFacade.getCountOfRecordsByMore("COC", startTime, endTime, (List)null, (List)null, "user", refId, alarmType, busiName, busiId, columnId, levelId, checkFlag);
    }

    public static String createAlarmCase(AlarmRecord theAlarmRecord, Map<String, AlarmThreshold> thresholdIdToAlarmThresholdMap) {
        StringBuilder alarmCase = new StringBuilder("");
        alarmCase.append(theAlarmRecord.getColumnName()).append("为").append(CiUtil.formatBigDecimal(theAlarmRecord.getResultValue())).append(",");
        AlarmThreshold alarmThreshold = (AlarmThreshold)thresholdIdToAlarmThresholdMap.get(theAlarmRecord.getThresholdId());
        alarmCase.append(createThresholdDesc(alarmThreshold)).append("。");
        return alarmCase.toString();
    }

    public static String createThresholdDesc(AlarmThreshold alarmThreshold) {
        StringBuilder thresholdDesc = new StringBuilder("");
        if(alarmThreshold == null) {
            return thresholdDesc.toString();
        } else {
            String maxValue = CiUtil.formatBigDecimal(alarmThreshold.getMaxValue());
            String minValue = CiUtil.formatBigDecimal(alarmThreshold.getMinValue());
            if("more".equals(alarmThreshold.getThresholdType())) {
                thresholdDesc.append("大于等于上限值").append(maxValue);
            } else if("less".equals(alarmThreshold.getThresholdType())) {
                thresholdDesc.append("小于等于下限值").append(minValue);
            } else if("in".equals(alarmThreshold.getThresholdType())) {
                thresholdDesc.append("大于等于下限值").append(minValue).append(",").append("小于等于上限值").append(maxValue);
            } else if("out".equals(alarmThreshold.getThresholdType())) {
                thresholdDesc.append("小于等于下限值").append(minValue).append("或").append("大于等于上限值").append(maxValue);
            }

            return thresholdDesc.toString();
        }
    }

    public static Map<String, AlarmThreshold> getThresholdIdToAlarmThresholdMap(List<AlarmThreshold> allAlarmThresholds) throws Exception {
        HashMap idToAlarmThresholdMap = new HashMap();
        Iterator i$ = allAlarmThresholds.iterator();

        while(i$.hasNext()) {
            AlarmThreshold theAlarmThreshold = (AlarmThreshold)i$.next();
            String thresholdId = theAlarmThreshold.getThresholdId();
            idToAlarmThresholdMap.put(thresholdId, theAlarmThreshold);
        }

        return idToAlarmThresholdMap;
    }

    public static Map<String, List<AlarmThreshold>> getBusiIdToAlarmThresholdsMap(List<AlarmThreshold> allAlarmThresholds) throws Exception {
        HashMap idToAlarmThresholdMap = new HashMap();
        Iterator i$ = allAlarmThresholds.iterator();

        while(i$.hasNext()) {
            AlarmThreshold theAlarmThreshold = (AlarmThreshold)i$.next();
            String busiId = theAlarmThreshold.getBusiId();
            Object alarmThresholds = new ArrayList();
            if(idToAlarmThresholdMap.containsKey(busiId)) {
                alarmThresholds = (List)idToAlarmThresholdMap.get(busiId);
            }

            ((List)alarmThresholds).add(theAlarmThreshold);
            idToAlarmThresholdMap.put(busiId, alarmThresholds);
        }

        return idToAlarmThresholdMap;
    }

    public static Map<String, Map<String, List<AlarmThreshold>>> getUserIdAndBusiIdToAlarmThresholdsMap(List<AlarmThreshold> allAlarmThresholds) throws Exception {
        HashMap userIdAndBusiIdToAlarmThresholdsMap = new HashMap();
        Iterator i$ = allAlarmThresholds.iterator();

        while(i$.hasNext()) {
            AlarmThreshold theAlarmThreshold = (AlarmThreshold)i$.next();
            String busiId = theAlarmThreshold.getBusiId();
            String userId = theAlarmThreshold.getRefId();
            Object busiIdToAlarmThresholdMap = new HashMap();
            if(userIdAndBusiIdToAlarmThresholdsMap.containsKey(userId)) {
                busiIdToAlarmThresholdMap = (Map)userIdAndBusiIdToAlarmThresholdsMap.get(userId);
            }

            Object alarmThresholds = new ArrayList();
            if(((Map)busiIdToAlarmThresholdMap).containsKey(busiId)) {
                alarmThresholds = (List)((Map)busiIdToAlarmThresholdMap).get(busiId);
            }

            ((List)alarmThresholds).add(theAlarmThreshold);
            ((Map)busiIdToAlarmThresholdMap).put(busiId, alarmThresholds);
            userIdAndBusiIdToAlarmThresholdsMap.put(userId, busiIdToAlarmThresholdMap);
        }

        return userIdAndBusiIdToAlarmThresholdsMap;
    }

    public static void deleteAlarmThresholdByBusiId(String alarmType, String busiId) throws Exception {
        List allAlarmThresholds = getAlarmThresholds((String)null, (String)null, alarmType, busiId, 0);
        Iterator i$ = allAlarmThresholds.iterator();

        while(i$.hasNext()) {
            AlarmThreshold alarmThreshold = (AlarmThreshold)i$.next();
            alarmFacade.deleteAlarmThreshold(alarmThreshold.getThresholdId());
        }

    }

    static {
        try {
            alarmFacade = (IAlarmFacade)SystemServiceLocator.getInstance().getService("alarmFacade");
            alarmThresholdVarRefDao = (IAlarmThresholdVarRefDao)SystemServiceLocator.getInstance().getService("varRefDao");
        } catch (Exception var1) {
            log.error("获取告警组件对象异常", var1);
            throw new CIServiceException("获取告警组件对象异常");
        }
    }
}

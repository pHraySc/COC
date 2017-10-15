package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.alarm.alarmRecord.model.AlarmRecord;
import com.asiainfo.biframe.alarm.model.AlarmReturn;
import com.asiainfo.biframe.alarm.model.AlarmThreshold;
import com.asiainfo.biframe.alarm.model.BusinessInfo;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomersAlarmJob {
    private static Logger log = Logger.getLogger(CustomersAlarmJob.class);
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    private ICiPersonNoticeService personNoticeService;
    private final String alarmType = "CustomersAlarm";

    public CustomersAlarmJob() {
    }

    public void createDailyAlarmRecord() {
        log.info("Enter CustomersAlarmJob.createDailyAlarmRecord method");

        try {
            this.createAlarmRecord(1);
        } catch (Exception var2) {
            log.error("创建日客户群预警记录失败", var2);
        }

        log.info("Exit CustomersAlarmJob.createDailyAlarmRecord method");
    }

    public void createMonthlyAlarmRecord() {
        log.info("Enter CustomersAlarmJob.createMonthlyAlarmRecord method");

        try {
            this.createAlarmRecord(0);
        } catch (Exception var2) {
            log.error("创建月客户群预警记录失败", var2);
        }

        log.info("Exit CustomersAlarmJob.createMonthlyAlarmRecord method");
    }

    private void createAlarmRecord(int dateType) throws Exception {
        String offsetDataStr = null;
        if(dateType == 1) {
            offsetDataStr = CacheBase.getInstance().getNewLabelDay();
        } else if(dateType == 0) {
            offsetDataStr = CacheBase.getInstance().getNewLabelMonth();
        }

        Date offsetDataDate = this.getCalculateDate(offsetDataStr);
        List unCalcAlarmThresholdList = this.getUnCalcAlarmThresholdList(offsetDataDate);
        Map busiIdToAlarmThresholdMap = CIAlarmServiceUtil.getBusiIdToAlarmThresholdsMap(unCalcAlarmThresholdList);
        Map thresholdIdToAlarmThresholdMap = CIAlarmServiceUtil.getThresholdIdToAlarmThresholdMap(unCalcAlarmThresholdList);
        CiCustomListInfo customListInfoVO = new CiCustomListInfo();
        customListInfoVO.setDataDate(offsetDataStr);
        customListInfoVO.setDataStatus(Integer.valueOf(3));
        List customListInfoList = this.customersManagerService.queryCiCustomListInfoByCustomListInfo(customListInfoVO);
        List businessInfos = this.createLabelAlarmBusinessInfoList(busiIdToAlarmThresholdMap, customListInfoList);
        List alarmReturns = CIAlarmServiceUtil.createAlarmList(businessInfos);
        List alarmRecords = this.createAlarmRecordList(alarmReturns, thresholdIdToAlarmThresholdMap, offsetDataStr);
        CIAlarmServiceUtil.saveAlarmRecords(alarmRecords);
        Iterator i$ = alarmRecords.iterator();

        while(i$.hasNext()) {
            AlarmRecord alarmRecord = (AlarmRecord)i$.next();
            CiPersonNotice personNotice = new CiPersonNotice();
            personNotice.setStatus(Integer.valueOf(1));
            personNotice.setCustomerGroupId(alarmRecord.getBusiId());
            personNotice.setNoticeName(alarmRecord.getBusiName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_PUBLISH_CUSTOMERS_ALARM);
            personNotice.setNoticeDetail("客户群：" + alarmRecord.getBusiName() + "产生客户群预警,数据日期：" + offsetDataStr + ",预警原因：" + alarmRecord.getAlarmCase());
            personNotice.setNoticeSendTime(new Date());
            personNotice.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_PUBLISH_CUSTOMERS_ALARM));
            personNotice.setReadStatus(Integer.valueOf(1));
            personNotice.setIsSuccess(Integer.valueOf(1));
            String userId = ((AlarmThreshold)thresholdIdToAlarmThresholdMap.get(alarmRecord.getThresholdId())).getRefId();
            personNotice.setReleaseUserId(userId);
            personNotice.setReceiveUserId(userId);
            this.personNoticeService.addPersonNotice(personNotice);
        }

    }

    private List<AlarmThreshold> getUnCalcAlarmThresholdList(Date alarmData) throws Exception {
        List allAlarmThresholds = CIAlarmServiceUtil.getAlarmThresholds((String)null, (String)null, "CustomersAlarm", (String)null, 0);
        Set thresholdIdSetOfRecord = this.getThresholdIdSetOfRecord(alarmData);
        ArrayList result = new ArrayList();
        Iterator i$ = allAlarmThresholds.iterator();

        while(i$.hasNext()) {
            AlarmThreshold alarmThreshold = (AlarmThreshold)i$.next();
            if(!thresholdIdSetOfRecord.contains(alarmThreshold.getThresholdId())) {
                result.add(alarmThreshold);
            }
        }

        return result;
    }

    private Set<String> getThresholdIdSetOfRecord(Date alarmData) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(alarmData);
        Object alarmRecordList = new ArrayList();

        try {
            alarmRecordList = CIAlarmServiceUtil.getPageAlarmRecords(date, date, (String)null, "CustomersAlarm", (String)null, (String)null, (String)null, -1, 0, 2147483647);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        HashSet thresholdIdSet = new HashSet();
        Iterator i$ = ((List)alarmRecordList).iterator();

        while(i$.hasNext()) {
            AlarmRecord alarmRecord = (AlarmRecord)i$.next();
            thresholdIdSet.add(alarmRecord.getThresholdId());
        }

        return thresholdIdSet;
    }

    private Date getCalculateDate(String offsetDataDate) {
        return offsetDataDate.length() == 6?DateUtil.string2Timestamp(offsetDataDate.substring(0, 4) + "-" + offsetDataDate.substring(4, 6) + "-01 00:00:00"):(offsetDataDate.length() == 8?DateUtil.string2Timestamp(offsetDataDate.substring(0, 4) + "-" + offsetDataDate.substring(4, 6) + "-" + offsetDataDate.substring(6, 8) + " 00:00:00"):null);
    }

    private List<AlarmRecord> createAlarmRecordList(List<AlarmReturn> alarmReturns, Map<String, AlarmThreshold> thresholdIdToAlarmThresholdMap, String offsetDataDate) {
        ArrayList alarmRecords = new ArrayList();
        Map customerGroupInfoIdToNameMap = this.getCustomerGroupInfoIdToNameMap();
        if(CollectionUtils.isNotEmpty(alarmReturns)) {
            Iterator i$ = alarmReturns.iterator();

            while(i$.hasNext()) {
                AlarmReturn alarmReturn = (AlarmReturn)i$.next();
                if(alarmReturn.isAlarm()) {
                    AlarmRecord alarmRecord = new AlarmRecord();
                    String busiId = alarmReturn.getBusiInfo().getBusiId();
                    alarmRecord.setBusiId(busiId);
                    alarmRecord.setBusiName((String)customerGroupInfoIdToNameMap.get(busiId));
                    alarmRecord.setCalculateDate(this.getCalculateDate(offsetDataDate));
                    alarmRecord.setCheckFlag(0);
                    alarmRecord.setColumnId(alarmReturn.getBusiInfo().getColumnId());
                    alarmRecord.setColumnName((String)ServiceConstants.alarmColumnIdToNameMap.get(alarmRecord.getColumnId()));
                    alarmRecord.setResultValue(alarmReturn.getBusiInfo().getColumnValue());
                    alarmRecord.setThresholdId(alarmReturn.getThresholdId());
                    alarmRecord.setAlarmCase(CIAlarmServiceUtil.createAlarmCase(alarmRecord, thresholdIdToAlarmThresholdMap));
                    alarmRecords.add(alarmRecord);
                }
            }
        }

        return alarmRecords;
    }

    private List<BusinessInfo> createLabelAlarmBusinessInfoList(Map<String, List<AlarmThreshold>> idToAlarmThresholdMap, List<CiCustomListInfo> customListInfoList) {
        ArrayList businessInfos = new ArrayList();
        Iterator i$ = customListInfoList.iterator();

        label38:
        while(true) {
            CiCustomListInfo ciCustomListInfo;
            String busiId;
            do {
                if(!i$.hasNext()) {
                    return businessInfos;
                }

                ciCustomListInfo = (CiCustomListInfo)i$.next();
                busiId = String.valueOf(ciCustomListInfo.getCustomGroupId());
            } while(!idToAlarmThresholdMap.containsKey(busiId));

            List alarmThresholdList = (List)idToAlarmThresholdMap.get(busiId);
            Iterator i$1 = alarmThresholdList.iterator();

            while(true) {
                AlarmThreshold theAlarmThreshold;
                BusinessInfo bInfo;
                BigDecimal columnValue;
                while(true) {
                    if(!i$1.hasNext()) {
                        continue label38;
                    }

                    theAlarmThreshold = (AlarmThreshold)i$1.next();
                    bInfo = new BusinessInfo();
                    bInfo.setAlarmType(theAlarmThreshold.getThresholdType());
                    bInfo.setBusiId(theAlarmThreshold.getBusiId());
                    bInfo.setColumnId(theAlarmThreshold.getColumnId());
                    columnValue = null;
                    if("0".equals(theAlarmThreshold.getColumnId())) {
                        columnValue = new BigDecimal(ciCustomListInfo.getCustomNum().longValue());
                        break;
                    }

                    if(!"1".equals(theAlarmThreshold.getColumnId())) {
                        break;
                    }

                    if(ciCustomListInfo.getRingNum() != null) {
                        columnValue = new BigDecimal(ciCustomListInfo.getRingNum().longValue());
                        break;
                    }
                }

                bInfo.setColumnValue(columnValue);
                bInfo.setThresholdId(theAlarmThreshold.getThresholdId());
                bInfo.setUserId(theAlarmThreshold.getRefId());
                businessInfos.add(bInfo);
            }
        }
    }

    private Map<String, String> getCustomerGroupInfoIdToNameMap() {
        HashMap result = new HashMap();
        CiCustomGroupInfo customGroupInfoVo = new CiCustomGroupInfo();
        List customGroupInfoList = this.customersManagerService.queryCustomersListByCustomGroupInfo(customGroupInfoVo, (String)null, 3);
        Iterator i$ = customGroupInfoList.iterator();

        while(i$.hasNext()) {
            CiCustomGroupInfo customGroupInfo = (CiCustomGroupInfo)i$.next();
            result.put(customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName());
        }

        return result;
    }
}

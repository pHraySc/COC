package com.ailk.biapp.ci.task;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiLabelBrandUserNum;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.entity.CiUserAttentionLabelId;
import com.ailk.biapp.ci.service.ICiLabelBrandUserNumService;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.service.ILabelQueryService;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.alarm.alarmRecord.model.AlarmRecord;
import com.asiainfo.biframe.alarm.model.AlarmReturn;
import com.asiainfo.biframe.alarm.model.AlarmThreshold;
import com.asiainfo.biframe.alarm.model.BusinessInfo;
import com.asiainfo.biframe.utils.config.Configure;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabelAlarmJob {
    private static Logger log = Logger.getLogger(LabelAlarmJob.class);
    @Autowired
    private ICiLabelBrandUserNumService labelBrandUserNumService;
    @Autowired
    private ICiPersonNoticeService personNoticeService;
    @Autowired
    private ILabelQueryService labelQueryService;
    private final String alarmType = "LabelAlarm";

    public LabelAlarmJob() {
    }

    public void createDailyAlarmRecord() {
        log.info("Enter LabelAlarmJob.createDailyAlarmRecord method");

        try {
            this.createAlarmRecord(1);
        } catch (Exception var2) {
            log.error("创建日标签预警记录失败", var2);
        }

        log.info("Exit LabelAlarmJob.createDailyAlarmRecord method");
    }

    public void createMonthlyAlarmRecord() {
        log.info("Enter LabelAlarmJob.createMonthlyAlarmRecord method");

        try {
            this.createAlarmRecord(0);
        } catch (Exception var2) {
            log.error("创建月标签预警记录失败", var2);
        }

        log.info("Exit LabelAlarmJob.createMonthlyAlarmRecord method");
    }

    private void createAlarmRecord(int dateType) throws Exception {
        log.debug("createAlarmRecord dateType=" + dateType);
        String offsetDataStr = null;
        if(dateType == 1) {
            offsetDataStr = CacheBase.getInstance().getNewLabelDay();
        } else if(dateType == 0) {
            offsetDataStr = CacheBase.getInstance().getNewLabelMonth();
        }

        Date offsetDataDate = this.getCalculateDate(offsetDataStr);
        log.debug("createAlarmRecord offsetDataDate=" + offsetDataDate);
        List unCalcAlarmThresholdList = this.getUnCalcAlarmThresholdList(offsetDataDate);
        Map thresholdIdToAlarmThresholdMap = CIAlarmServiceUtil.getThresholdIdToAlarmThresholdMap(unCalcAlarmThresholdList);
        Object businessInfos = new ArrayList();
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        List alarmReturns;
        if(needAuthority) {
            alarmReturns = this.labelBrandUserNumService.getAllCityLabelBrandUserNumListByDataDate(offsetDataStr);
            Set alarmRecords = this.getAllRelatedUserIdSet(unCalcAlarmThresholdList);
            Map i$ = this.labelBrandUserNumService.getUserIdAndlabelIdToLabelStatsMap(alarmReturns, alarmRecords);
            Map alarmRecord = CIAlarmServiceUtil.getUserIdAndBusiIdToAlarmThresholdsMap(unCalcAlarmThresholdList);
            Iterator personNotice = alarmRecord.entrySet().iterator();

            while(personNotice.hasNext()) {
                Entry userId = (Entry)personNotice.next();
                String attentionList = (String)userId.getKey();
                if(i$.containsKey(attentionList)) {
                    Map i$1 = (Map)userId.getValue();
                    ArrayList attention = new ArrayList();
                    attention.addAll(((Map)i$.get(attentionList)).values());
                    List currUserBusinessInfos = this.createLabelAlarmBusinessInfoList(i$1, attention);
                    ((List)businessInfos).addAll(currUserBusinessInfos);
                }
            }
        } else {
            alarmReturns = this.labelBrandUserNumService.getLabelBrandUserNumListByDataDate(offsetDataStr);
            Map alarmRecords1 = CIAlarmServiceUtil.getBusiIdToAlarmThresholdsMap(unCalcAlarmThresholdList);
            businessInfos = this.createLabelAlarmBusinessInfoList(alarmRecords1, alarmReturns);
        }

        alarmReturns = CIAlarmServiceUtil.createAlarmList((List)businessInfos);
        List alarmRecords2 = this.createAlarmRecordList(alarmReturns, thresholdIdToAlarmThresholdMap, offsetDataStr);
        CIAlarmServiceUtil.saveAlarmRecords(alarmRecords2);
        Iterator i$2 = alarmRecords2.iterator();

        while(true) {
            CiPersonNotice personNotice1;
            String userId1;
            List attentionList1;
            do {
                AlarmRecord alarmRecord1;
                do {
                    if(!i$2.hasNext()) {
                        return;
                    }

                    alarmRecord1 = (AlarmRecord)i$2.next();
                    personNotice1 = new CiPersonNotice();
                    personNotice1.setStatus(Integer.valueOf(1));
                    personNotice1.setLabelId(Integer.valueOf(Integer.parseInt(alarmRecord1.getBusiId())));
                    personNotice1.setNoticeName(alarmRecord1.getBusiName() + " " + ServiceConstants.PERSON_NOTICE_TYPE_STRING_PUBLISH_LABEL_ALARM);
                    personNotice1.setNoticeDetail("标签：" + alarmRecord1.getBusiName() + "产生标签预警,数据日期：" + offsetDataStr + ",预警原因：" + alarmRecord1.getAlarmCase());
                    personNotice1.setNoticeSendTime(new Date());
                    personNotice1.setNoticeTypeId(Integer.valueOf(ServiceConstants.PERSON_NOTICE_TYPE_PUBLISH_LABEL_ALARM));
                    personNotice1.setReadStatus(Integer.valueOf(1));
                    personNotice1.setIsSuccess(Integer.valueOf(1));
                    userId1 = ((AlarmThreshold)thresholdIdToAlarmThresholdMap.get(alarmRecord1.getThresholdId())).getRefId();
                    personNotice1.setReleaseUserId(userId1);
                    personNotice1.setReceiveUserId(userId1);
                    this.personNoticeService.addPersonNotice(personNotice1);
                } while(needAuthority);

                attentionList1 = this.labelQueryService.queryAttentionRecordByLabelId(alarmRecord1.getBusiId());
            } while(attentionList1 == null);

            Iterator i$3 = attentionList1.iterator();

            while(i$3.hasNext()) {
                CiUserAttentionLabelId attention1 = (CiUserAttentionLabelId)i$3.next();
                if(!attention1.getUserId().equals(userId1)) {
                    personNotice1.setNoticeId((String)null);
                    personNotice1.setReceiveUserId(attention1.getUserId());
                    this.personNoticeService.addPersonNotice(personNotice1);
                }
            }
        }
    }

    private Set<String> getAllRelatedUserIdSet(List<AlarmThreshold> unCalcAlarmThresholdList) {
        HashSet allRelatedUserIdSet = new HashSet();
        Iterator i$ = unCalcAlarmThresholdList.iterator();

        while(i$.hasNext()) {
            AlarmThreshold alarmThreshold = (AlarmThreshold)i$.next();
            allRelatedUserIdSet.add(alarmThreshold.getRefId());
        }

        return allRelatedUserIdSet;
    }

    private List<AlarmThreshold> getUnCalcAlarmThresholdList(Date alarmData) throws Exception {
        List allAlarmThresholds = CIAlarmServiceUtil.getAlarmThresholds((String)null, (String)null, "LabelAlarm", (String)null, 0);
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
            alarmRecordList = CIAlarmServiceUtil.getPageAlarmRecords(date, date, (String)null, "LabelAlarm", (String)null, (String)null, (String)null, -1, 0, 2147483647);
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
        if(CollectionUtils.isNotEmpty(alarmReturns)) {
            Iterator i$ = alarmReturns.iterator();

            while(i$.hasNext()) {
                AlarmReturn alarmReturn = (AlarmReturn)i$.next();
                if(alarmReturn.isAlarm()) {
                    AlarmRecord alarmRecord = new AlarmRecord();
                    String labelId = alarmReturn.getBusiInfo().getBusiId();
                    alarmRecord.setBusiId(labelId);
                    alarmRecord.setBusiName(CacheBase.getInstance().getEffectiveLabel(labelId).getLabelName());
                    alarmRecord.setCalculateDate(this.getCalculateDate(offsetDataDate));
                    alarmRecord.setCheckFlag(0);
                    alarmRecord.setColumnId(alarmReturn.getBusiInfo().getColumnId());
                    alarmRecord.setColumnName((String)ServiceConstants.alarmColumnIdToNameMap.get(alarmRecord.getColumnId()));
                    BigDecimal resultValue = alarmReturn.getBusiInfo().getColumnValue().setScale(2, 4);
                    alarmRecord.setResultValue(resultValue);
                    alarmRecord.setThresholdId(alarmReturn.getThresholdId());
                    alarmRecord.setAlarmCase(CIAlarmServiceUtil.createAlarmCase(alarmRecord, thresholdIdToAlarmThresholdMap));
                    alarmRecords.add(alarmRecord);
                }
            }
        }

        return alarmRecords;
    }

    private List<BusinessInfo> createLabelAlarmBusinessInfoList(Map<String, List<AlarmThreshold>> idToAlarmThresholdMap, List<CiLabelBrandUserNum> ciLabelBrandUserNums) {
        ArrayList businessInfos = new ArrayList();
        Iterator i$ = ciLabelBrandUserNums.iterator();

        label41:
        while(true) {
            CiLabelBrandUserNum labelUserNum;
            String busiId;
            do {
                if(!i$.hasNext()) {
                    return businessInfos;
                }

                labelUserNum = (CiLabelBrandUserNum)i$.next();
                busiId = String.valueOf(labelUserNum.getLabelId());
            } while(!idToAlarmThresholdMap.containsKey(busiId));

            List alarmThresholdList = (List)idToAlarmThresholdMap.get(busiId);
            Iterator i$1 = alarmThresholdList.iterator();

            while(true) {
                AlarmThreshold theAlarmThreshold;
                BusinessInfo bInfo;
                BigDecimal columnValue;
                while(true) {
                    if(!i$1.hasNext()) {
                        continue label41;
                    }

                    theAlarmThreshold = (AlarmThreshold)i$1.next();
                    bInfo = new BusinessInfo();
                    bInfo.setAlarmType(theAlarmThreshold.getThresholdType());
                    bInfo.setBusiId(theAlarmThreshold.getBusiId());
                    bInfo.setColumnId(theAlarmThreshold.getColumnId());
                    columnValue = null;
                    if("0".equals(theAlarmThreshold.getColumnId())) {
                        columnValue = new BigDecimal(labelUserNum.getCustomNum().longValue());
                        break;
                    }

                    if("1".equals(theAlarmThreshold.getColumnId())) {
                        if(labelUserNum.getRingNum() == null) {
                            continue;
                        }

                        columnValue = new BigDecimal(labelUserNum.getRingNum().longValue());
                        break;
                    }

                    if("2".equals(theAlarmThreshold.getColumnId())) {
                        columnValue = new BigDecimal(labelUserNum.getProportion().doubleValue());
                    }
                    break;
                }

                bInfo.setColumnValue(columnValue);
                bInfo.setThresholdId(theAlarmThreshold.getThresholdId());
                bInfo.setUserId(theAlarmThreshold.getRefId());
                businessInfos.add(bInfo);
            }
        }
    }
}

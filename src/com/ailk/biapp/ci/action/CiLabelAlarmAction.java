package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiLabelBrandUserNum;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CIAlarmInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiLabelBrandUserNumService;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.task.LabelAlarmJob;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.alarm.alarmRecord.model.AlarmRecord;
import com.asiainfo.biframe.alarm.model.AlarmThreshold;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiLabelAlarmAction extends CIBaseAction {
    public static final String ALARM_NAME_LABEL_UNDEFINED = "ciLabelUndefined";
    private Pager pager;
    private AlarmThreshold alarmThreshold = null;
    private CIAlarmInfo alarmInfo;
    private CiLabelInfo ciLabelInfo;
    private static final String ERROR_CODE_ALARM_INTERFACE = "0";
    private static final String ERROR_CODE_ALARM_EXISTS = "1";
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private LabelAlarmJob labelAlarmJob;
    @Autowired
    private ICiLabelBrandUserNumService labelBrandUserNumService;

    public CiLabelAlarmAction() {
    }

    public String initRecord() throws CIServiceException {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        String fromPageFlag = this.getRequest().getParameter("fromPageFlag");
        this.pager.setResult(new ArrayList());

        try {
            List e = CIAlarmServiceUtil.getPageAlarmRecords((String)null, (String)null, (String)null, "LabelAlarm", (String)null, (String)null, (String)null, 0, 0, 2147483647);
            if(e != null && e.size() > 0) {
                Iterator userId = e.iterator();

                while(userId.hasNext()) {
                    AlarmRecord record = (AlarmRecord)userId.next();
                    CIAlarmServiceUtil.updateCheckFlag(record.getRecordId(), 1);
                }

                String userId1 = PrivilegeServiceUtil.getUserId();
                CacheBase.getInstance().deleteNoticeUserId(userId1);
            }

            if("1".equals(fromPageFlag)) {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_RESULT_SELECT", this.userId, "个人信息进入标签预警结果查询", "个人信息进入标签预警结果查询", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var5) {
            this.log.error("获取预警结果失败", var5);
        }

        return "recordIndex";
    }

    private String getUserIdFromPriv() throws Exception {
        String userId = this.getUserId();
        return PrivilegeServiceUtil.isAdminUser(userId)?null:userId;
    }

    public String queryRecord() throws CIServiceException {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        Object userId = null;
        String startTime = null;
        String endTime = null;
        String busiName = null;
        String columnId = null;
        String thresholdType = "";

        try {
            if(null != this.alarmInfo) {
                if(StringUtils.isNotEmpty(this.alarmInfo.getStartTime())) {
                    startTime = this.alarmInfo.getStartTime();
                }

                if(StringUtils.isNotEmpty(this.alarmInfo.getEndTime())) {
                    endTime = this.alarmInfo.getEndTime();
                }

                busiName = this.alarmInfo.getBusiName() == null?null:this.alarmInfo.getBusiName().trim();
                columnId = this.alarmInfo.getColumnId();
            }

            if(columnId != null) {
                if(columnId.equals("0")) {
                    thresholdType = "基础";
                } else if(columnId.equals("1")) {
                    thresholdType = "环比";
                } else if(columnId.equals("2")) {
                    thresholdType = "占比";
                }
            }

            List e = CIAlarmServiceUtil.getPageAlarmRecords(startTime, endTime, (String)userId, "LabelAlarm", busiName, (String)null, columnId, -1, 0, 2147483647);
            int totalSize = e.size();
            this.pager.setTotalSize((long)totalSize);
            this.pager = this.pager.pagerFlip();
            List result = CIAlarmServiceUtil.getPageAlarmRecords(startTime, endTime, (String)userId, "LabelAlarm", busiName, (String)null, columnId, -1, this.pager.getPageNum() - 1, this.pager.getPageSize());
            this.pager.setResult(result);
            StringBuffer sb = new StringBuffer("");
            if(StringUtil.isNotEmpty(busiName)) {
                sb.append("名称 :").append(busiName).append(" ");
            }

            if(StringUtil.isNotEmpty(columnId)) {
                sb.append("预警类型 :").append(thresholdType).append(" ");
            }

            if(StringUtil.isNotEmpty(startTime)) {
                sb.append("起始时间 :").append(startTime).append(" ");
            }

            if(StringUtil.isNotEmpty(endTime)) {
                sb.append("结束时间 :").append(endTime).append(" ");
            }

            String conditionStr = sb.toString();
            if(StringUtil.isNotEmpty(conditionStr)) {
                conditionStr = ",查询条件【" + conditionStr + "】";
            }

            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_RESULT_SELECT_MANAGE", "", "标签预警结果查询", "标签预警结果查询成功" + conditionStr, OperResultEnum.Success, LogLevelEnum.Medium);
            return "recordList";
        } catch (Exception var12) {
            this.log.error("查询客户群预警结果失败", var12);
            throw new CIServiceException(var12);
        }
    }

    private String queryLabelIdsStrByLabelName(String labelName) {
        CiLabelInfo ciLabelInfo = new CiLabelInfo();
        ciLabelInfo.setLabelName(this.alarmThreshold.getBusiName());
        List ciLabelInfoList = this.ciLabelInfoService.queryPageList(1, 2147483647, ciLabelInfo);
        new String();
        String labelIdsStr;
        if(CollectionUtils.isEmpty(ciLabelInfoList)) {
            labelIdsStr = "ciLabelUndefined";
        } else {
            StringBuilder builder = new StringBuilder();

            for(int idx = 0; idx < ciLabelInfoList.size(); ++idx) {
                builder.append(((CiLabelInfo)ciLabelInfoList.get(idx)).getLabelId());
                if(idx < ciLabelInfoList.size() - 1) {
                    builder.append(",");
                }
            }

            labelIdsStr = builder.toString();
        }

        return labelIdsStr;
    }

    public String init() throws CIServiceException {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setResult(new ArrayList());
        return "thresholdIndex";
    }

    public String queryThreshold() throws CIServiceException {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        String labelIdsStr = null;
        String columnId = null;
        String thresholdType = "";
        if(null != this.alarmThreshold) {
            if(StringUtils.isNotEmpty(this.alarmThreshold.getBusiName())) {
                labelIdsStr = this.queryLabelIdsStrByLabelName(this.alarmThreshold.getBusiName());
            }

            columnId = this.alarmThreshold.getColumnId();
        }

        if(columnId != null) {
            if(columnId.equals("0")) {
                thresholdType = "基础";
            } else if(columnId.equals("1")) {
                thresholdType = "环比";
            } else if(columnId.equals("2")) {
                thresholdType = "占比";
            }
        }

        try {
            String e = this.getUserIdFromPriv();
            int totalSize = 0;
            Object result = new ArrayList();
            if(!"ciLabelUndefined".equals(labelIdsStr)) {
                totalSize = CIAlarmServiceUtil.getCountOfAlarmThresholds(e, "LabelAlarm", labelIdsStr, columnId, 0);
                this.pager.setTotalSize((long)totalSize);
                this.pager = this.pager.pagerFlip();
                result = CIAlarmServiceUtil.getPageAlarmThresholdsByMore(e, "LabelAlarm", labelIdsStr, columnId, 0, this.pager.getPageNum() - 1, this.pager.getPageSize());
            }

            this.pager.setTotalSize((long)totalSize);
            this.pager.setResult((List)result);
            StringBuffer sb = new StringBuffer("");
            if(StringUtil.isNotEmpty(this.alarmThreshold.getBusiName())) {
                sb.append("名称 :").append(this.alarmThreshold.getBusiName()).append(" ");
            }

            if(StringUtil.isNotEmpty(columnId)) {
                sb.append("预警类型 :").append(thresholdType).append(" ");
            }

            String conditionStr = sb.toString();
            if(StringUtil.isNotEmpty(conditionStr)) {
                conditionStr = ",查询条件【" + conditionStr + "】";
            }

            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_SELECT", "", "", "标签预警设置查询成功" + conditionStr, OperResultEnum.Success, LogLevelEnum.Medium);
            return "thresholdList";
        } catch (Exception var9) {
            this.log.error("标签告警列表查询", var9);
            throw new CIServiceException(var9);
        }
    }

    public String toEdit() throws CIServiceException {
        if(null != this.alarmThreshold && StringUtils.isNotEmpty(this.alarmThreshold.getThresholdId())) {
            try {
                this.alarmThreshold = CIAlarmServiceUtil.getAlarmThresholdById(this.alarmThreshold.getThresholdId());
                this.ciLabelInfo = CacheBase.getInstance().getEffectiveLabel(this.alarmThreshold.getBusiId());
            } catch (Exception var2) {
                this.log.error("跳转到修改预警界面出错：" + var2.getMessage());
                var2.printStackTrace();
                throw new CIServiceException("跳转到修改预警界面出错：" + var2.getMessage());
            }
        }

        return "editThreshold";
    }

    private boolean isThresholdExists(String userId) throws CIServiceException {
        boolean isExists = false;

        try {
            List e = CIAlarmServiceUtil.getPageAlarmThresholdsByMore(userId, "LabelAlarm", this.alarmThreshold.getBusiId(), this.alarmThreshold.getColumnId(), 0, 0, 2147483647);
            Iterator i$ = e.iterator();

            while(i$.hasNext()) {
                AlarmThreshold theThreshold = (AlarmThreshold)i$.next();
                if(!theThreshold.getThresholdId().equals(this.alarmThreshold.getThresholdId()) && theThreshold.getLevelId().equals(this.alarmThreshold.getLevelId())) {
                    isExists = true;
                    break;
                }
            }
        } catch (Exception var6) {
            this.log.error("判读预警在数据库中是否存在错误", var6);
            var6.printStackTrace();
        }

        return isExists;
    }

    public void addThreshold() throws CIServiceException {
        String userId = this.getUserId();
        String labelId = this.getRequest().getParameter("labelId");
        boolean isExists = this.isThresholdExists(userId);
        boolean success = true;
        HashMap returnMsg = new HashMap();
        String msg = "";
        String errorType = "";
        if(isExists) {
            success = false;
            errorType = "1";
        } else {
            this.alarmThreshold.setCreator(userId);
            this.alarmThreshold.setTypeId("LabelAlarm");

            try {
                CIAlarmServiceUtil.createAlarmThreshold(this.alarmThreshold);
                if(!StringUtils.isEmpty(labelId)) {
                    CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_ADD_MANAGE", this.alarmThreshold.getBusiId(), this.alarmThreshold.getBusiName(), "标签管理->新增标签预警成功【标签名称:" + this.alarmThreshold.getBusiName() + ",标签ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                } else {
                    CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_ADD", this.alarmThreshold.getBusiId(), this.alarmThreshold.getBusiName(), "新增标签预警成功【标签名称:" + this.alarmThreshold.getBusiName() + ",标签ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                }
            } catch (Exception var11) {
                success = false;
                errorType = "0";
                if(!StringUtils.isEmpty(labelId)) {
                    CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_ADD_MANAGE", "-1", "标签管理->新增标签预警失败", "标签管理->新增标签预警失败【标签名称:" + this.alarmThreshold.getBusiName() + ",标签ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                } else {
                    CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_ADD", "-1", "标签预警设置->新增标签预警失败", "标签预警设置->新增标签预警失败【标签名称:" + this.alarmThreshold.getBusiName() + ",标签ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                }

                throw new CIServiceException(var11);
            }
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("errorType", errorType);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var10) {
            this.log.error("发送json串异常", var10);
            throw new CIServiceException(var10);
        }
    }

    public void modifyThreshold() throws CIServiceException {
        String userId = this.getUserId();
        boolean isExists = this.isThresholdExists(userId);
        boolean success = true;
        HashMap returnMsg = new HashMap();
        String msg = "";
        String errorType = "";
        if(isExists) {
            success = false;
            errorType = "1";
        } else {
            this.alarmThreshold.setCreator(userId);
            this.alarmThreshold.setTypeId("LabelAlarm");

            try {
                CIAlarmServiceUtil.updateAlarmThreshold(this.alarmThreshold);
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_UPDATE", this.alarmThreshold.getBusiId(), this.alarmThreshold.getBusiName(), "修改标签预警成功【标签名称:" + this.alarmThreshold.getBusiName() + ",标签ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var10) {
                this.log.error("添加告警阈值设置错误", var10);
                success = false;
                errorType = "0";
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_UPDATE", this.alarmThreshold.getBusiId(), this.alarmThreshold.getBusiName(), "修改标签预警失败【标签名称:" + this.alarmThreshold.getBusiName() + ",标签ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                throw new CIServiceException(var10);
            }
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("errorType", errorType);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var9) {
            this.log.error("发送json串异常", var9);
            throw new CIServiceException(var9);
        }
    }

    public void deleteThreshold() throws CIServiceException {
        boolean success = true;
        String msg = "";
        HashMap returnMsg = new HashMap();
        if(this.alarmInfo != null && CollectionUtils.isNotEmpty(this.alarmInfo.getThresholdIdList())) {
            Iterator response = this.alarmInfo.getThresholdIdList().iterator();

            while(response.hasNext()) {
                String e = (String)response.next();

                try {
                    this.alarmThreshold = CIAlarmServiceUtil.getAlarmThresholdById(e);

                    try {
                        CIAlarmServiceUtil.deleteAlarmThreshold(e);
                        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_DELETE", this.alarmThreshold.getBusiId(), this.alarmThreshold.getBusiName(), "删除标签预警成功【标签名称:" + this.alarmThreshold.getBusiName() + ",标签ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                    } catch (Exception var8) {
                        success = false;
                        this.log.error("删除阈值设置[thresholdId = " + e + "]失败");
                        msg = msg + "删除阈值设置[thresholdId = " + e + "]失败,异常[" + var8.getMessage() + "];";
                        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_DELETE", this.alarmThreshold.getBusiId(), this.alarmThreshold.getBusiName(), "删除标签预警失败【标签名称:" + this.alarmThreshold.getBusiName() + ",标签ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                    }
                } catch (Exception var9) {
                    success = false;
                    this.log.error("获取阈值设置[thresholdId = " + e + "]失败");
                    msg = msg + "获取阈值设置[thresholdId = " + e + "]失败,异常[" + var9.getMessage() + "];";
                }
            }

            if(success) {
                msg = "删除成功";
            }
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public void findLabelAlarmRecord() throws Exception {
        HashMap returnMap = new HashMap();
        boolean success = false;

        try {
            String response = this.getUserId();
            boolean e1 = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
            boolean showAllAlarm = !e1 || e1 && PrivilegeServiceUtil.isAdminUser(response);
            if(showAllAlarm) {
                response = null;
            }

            int labelId = this.ciLabelInfo.getLabelId().intValue();
            String dataDate = this.ciLabelInfo.getDataDate();
            String alarmStartDate = null;
            String alarmEndDate = null;
            Timestamp alarmTable;
            if(this.ciLabelInfo.getUpdateCycle().intValue() == 2) {
                alarmTable = DateUtil.string2Timestamp(dataDate.substring(0, 4) + "-" + dataDate.substring(4, 6) + "-01 00:00:00");
                alarmStartDate = DateUtil.date2String(DateUtil.firstDayOfMonth(alarmTable), "yyyy-MM-dd") + " 00:00:00";
                alarmEndDate = DateUtil.date2String(DateUtil.lastDayOfMonth(alarmTable), "yyyy-MM-dd") + " 23:59:59";
            } else if(this.ciLabelInfo.getUpdateCycle().intValue() == 1) {
                alarmTable = DateUtil.string2Timestamp(dataDate.substring(0, 4) + "-" + dataDate.substring(4, 6) + "-" + dataDate.substring(6, 8) + " 00:00:00");
                alarmStartDate = DateUtil.date2String(alarmTable, "yyyy-MM-dd") + " 00:00:00";
                alarmEndDate = DateUtil.date2String(alarmTable, "yyyy-MM-dd") + " 23:59:59";
            }

            Map alarmTable1 = this.getAlarmAlertTable(dataDate, alarmStartDate, alarmEndDate, response, labelId, "LabelAlarm");
            success = true;
            returnMap.put("result", alarmTable1);
            returnMap.put("success", Boolean.valueOf(success));
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_DEATIL_VIEW", this.ciLabelInfo.getLabelId().toString(), "", "标签分析->标签预警详情查看成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var12) {
            String e = "查询标签预警信息失败";
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ALARM_DEATIL_VIEW", this.ciLabelInfo.getLabelId().toString(), "", "个性化通知->个人通知类单独设置成功", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error(e + "，标签ID：" + this.ciLabelInfo.getLabelId(), var12);
            returnMap.put("msg", e);
            success = false;
            returnMap.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    private Map<String, Map<String, String>> getAlarmAlertTable(String dataDate, String alarmStartDate, String alarmEndDate, String userId, int labelId, String type) throws Exception {
        HashMap columnIdToThresholdMap = new HashMap();
        HashMap thresholdIdToThresholdMap = new HashMap();
        List alarmThresholdList = CIAlarmServiceUtil.getPageAlarmThresholdsByMore(userId, type, String.valueOf(labelId), (String)null, 0, 0, 2147483647);
        Iterator ciLabelBrandUserNum = alarmThresholdList.iterator();

        while(ciLabelBrandUserNum.hasNext()) {
            AlarmThreshold needAuthority = (AlarmThreshold)ciLabelBrandUserNum.next();
            columnIdToThresholdMap.put(needAuthority.getColumnId(), needAuthority);
            thresholdIdToThresholdMap.put(needAuthority.getThresholdId(), needAuthority);
        }

        CiLabelBrandUserNum var23 = null;
        boolean var24 = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
        if(var24) {
            List columnIdToRealVal = this.labelBrandUserNumService.getAllCityLabelBrandUserNumListByDataDate(dataDate);
            HashSet thresholdToRecordMap = new HashSet();
            thresholdToRecordMap.add(userId);
            Map allResult = this.labelBrandUserNumService.getUserIdAndlabelIdToLabelStatsMap(columnIdToRealVal, thresholdToRecordMap);
            Map alarmTable = (Map)allResult.get(userId);
            if(null != alarmTable) {
                var23 = (CiLabelBrandUserNum)alarmTable.get(String.valueOf(labelId));
            }
        } else {
            CiLabelBrandUserNum var25 = new CiLabelBrandUserNum();
            var25.setLabelId(Integer.valueOf(labelId));
            var25.setDataDate(dataDate);
            var25.setCityId(Integer.valueOf(-1));
            var25.setBrandId(Integer.valueOf(-1));
            var25.setVipLevelId(Integer.valueOf(-1));
            List var27 = this.labelBrandUserNumService.getLabelBrandUserNumList(var25);
            if(CollectionUtils.isNotEmpty(var27)) {
                var23 = (CiLabelBrandUserNum)var27.get(0);
            }
        }

        HashMap var26 = new HashMap();
        if(null != var23) {
            var26.put("0", String.valueOf(var23.getCustomNum()));
            Long var28 = var23.getRingNum();
            if(var28 == null) {
                var26.put("1", "-");
            } else {
                var26.put("1", String.valueOf(var28));
            }

            var26.put("2", String.valueOf(var23.getProportion()));
        }

        HashMap var29 = new HashMap();
        List var30 = CIAlarmServiceUtil.getPageAlarmRecords(alarmStartDate, alarmEndDate, userId, type, (String)null, String.valueOf(labelId), (String)null, -1, 0, 2147483647);
        Iterator var31 = var30.iterator();

        while(var31.hasNext()) {
            AlarmRecord alarmColumns = (AlarmRecord)var31.next();
            var29.put(alarmColumns.getThresholdId(), alarmColumns);
        }

        HashMap var32 = new HashMap();
        String[] var33 = new String[]{String.valueOf("0"), String.valueOf("1"), String.valueOf("2")};
        String[] arr$ = var33;
        int len$ = var33.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String alarmColumn = arr$[i$];
            HashMap colMap = new HashMap();
            if(columnIdToThresholdMap.containsKey(alarmColumn)) {
                AlarmThreshold threshold = (AlarmThreshold)columnIdToThresholdMap.get(alarmColumn);
                colMap.put("threshold", CIAlarmServiceUtil.createThresholdDesc(threshold));
                if(var29.containsKey(threshold.getThresholdId())) {
                    if("1".equals(threshold.getLevelId())) {
                        colMap.put("level", "高");
                    } else if("2".equals(threshold.getLevelId())) {
                        colMap.put("level", "中");
                    } else if("3".equals(threshold.getLevelId())) {
                        colMap.put("level", "低");
                    }
                } else {
                    colMap.put("level", "正常");
                }
            } else {
                colMap.put("level", "未设置");
                colMap.put("threshold", "未设置");
            }

            if(var26.containsKey(alarmColumn)) {
                colMap.put("realValue", var26.get(alarmColumn));
            } else {
                colMap.put("realValue", "-");
            }

            var32.put(alarmColumn, colMap);
        }

        return var32;
    }

    public void createAlarmRecord() {
        this.labelAlarmJob.createMonthlyAlarmRecord();
    }

    public AlarmThreshold getAlarmThreshold() {
        return this.alarmThreshold;
    }

    public void setAlarmThreshold(AlarmThreshold alarmThreshold) {
        this.alarmThreshold = alarmThreshold;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public CIAlarmInfo getAlarmInfo() {
        return this.alarmInfo;
    }

    public void setAlarmInfo(CIAlarmInfo alarmInfo) {
        this.alarmInfo = alarmInfo;
    }

    public CiLabelInfo getCiLabelInfo() {
        return this.ciLabelInfo;
    }

    public void setCiLabelInfo(CiLabelInfo ciLabelInfo) {
        this.ciLabelInfo = ciLabelInfo;
    }
}

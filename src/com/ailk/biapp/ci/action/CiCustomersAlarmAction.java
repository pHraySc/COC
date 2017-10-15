package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CIAlarmInfo;
import com.ailk.biapp.ci.model.CiCustomRelModel;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.task.CustomersAlarmJob;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.alarm.alarmRecord.model.AlarmRecord;
import com.asiainfo.biframe.alarm.model.AlarmThreshold;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiCustomersAlarmAction extends CIBaseAction {
    public static final String ALARM_NAME_CUSTOMER_UNDEFINED = "ciCustomerUndefined";
    private Pager pager;
    private AlarmThreshold alarmThreshold = null;
    private CIAlarmInfo alarmInfo;
    private CiCustomRelModel customRelModel;
    private static final String ERROR_CODE_ALARM_INTERFACE = "0";
    private static final String ERROR_CODE_ALARM_EXISTS = "1";
    private String fromPageFlag;
    private String showType;
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    private CustomersAlarmJob customersAlarmJob;

    public CiCustomersAlarmAction() {
    }

    public String initRecord() throws CIServiceException {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        String fromPageFlag = this.getRequest().getParameter("fromPageFlag");
        this.pager.setResult(new ArrayList());

        try {
            List e = CIAlarmServiceUtil.getPageAlarmRecords((String)null, (String)null, (String)null, "CustomersAlarm", (String)null, (String)null, (String)null, 0, 0, 2147483647);
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
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_ALARM_RESULT_SELECT", "", "", "个人信息进入客户群预警结果查询", OperResultEnum.Success, LogLevelEnum.Medium);
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

        String startTime = null;
        String endTime = null;
        String busiName = null;
        String columnId = null;
        String thresholdType = "";

        try {
            String e = this.getUserIdFromPriv();
            if(null != this.alarmInfo) {
                if(StringUtils.isNotEmpty(this.alarmInfo.getStartTime())) {
                    startTime = this.alarmInfo.getStartTime();
                }

                if(StringUtils.isNotEmpty(this.alarmInfo.getEndTime())) {
                    endTime = this.alarmInfo.getEndTime();
                }

                busiName = this.alarmInfo.getBusiName();
                columnId = this.alarmInfo.getColumnId();
            }

            if(columnId != null) {
                if(columnId.equals("0")) {
                    thresholdType = "基础";
                } else if(columnId.equals("1")) {
                    thresholdType = "环比";
                }
            }

            this.log.debug("queryRecord startTime=" + startTime + ",endTime=" + endTime + ",userId=" + e + ",busiName=" + busiName + ",columnId=" + columnId);
            List allResult = CIAlarmServiceUtil.getPageAlarmRecords(startTime, endTime, e, "CustomersAlarm", busiName, (String)null, columnId, -1, 0, 2147483647);
            int totalSize = allResult.size();
            this.pager.setTotalSize((long)totalSize);
            this.pager = this.pager.pagerFlip();
            List result = CIAlarmServiceUtil.getPageAlarmRecords(startTime, endTime, e, "CustomersAlarm", busiName, (String)null, columnId, -1, this.pager.getPageNum() - 1, this.pager.getPageSize());
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

            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_ALARM_RESULT_SELECT", "", "客户群预警结果查询", "客户群预警结果查询成功" + conditionStr, OperResultEnum.Success, LogLevelEnum.Medium);
            return "recordList";
        } catch (Exception var12) {
            this.log.error("CiCustomersAlarmAction.queryRecord出错:", var12);
            throw new CIServiceException(var12);
        }
    }

    public String alarmMain() throws CIServiceException {
        return "alarmMain";
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

        String customerGroupIdsStr = null;
        String columnId = null;
        String thresholdType = "";

        try {
            String e = this.getUserIdFromPriv();
            if(null != this.alarmThreshold) {
                if(StringUtils.isNotEmpty(this.alarmThreshold.getBusiName())) {
                    customerGroupIdsStr = this.queryCustomerIdsStrByCustomerName(e, this.alarmThreshold.getBusiName());
                }

                columnId = this.alarmThreshold.getColumnId();
            }

            if(columnId != null) {
                if(columnId.equals("0")) {
                    thresholdType = "基础";
                } else if(columnId.equals("1")) {
                    thresholdType = "环比";
                }
            }

            int totalSize = 0;
            Object result = new ArrayList();
            this.log.debug("queryThreshold userId=" + e + ",customerGroupIdsStr=" + customerGroupIdsStr + ",columnId=" + columnId);
            if(!"ciCustomerUndefined".equals(customerGroupIdsStr)) {
                totalSize = CIAlarmServiceUtil.getCountOfAlarmThresholds(e, "CustomersAlarm", customerGroupIdsStr, columnId, 0);
                this.pager.setTotalSize((long)totalSize);
                this.pager = this.pager.pagerFlip();
                result = CIAlarmServiceUtil.getPageAlarmThresholdsByMore(e, "CustomersAlarm", customerGroupIdsStr, columnId, 0, this.pager.getPageNum() - 1, this.pager.getPageSize());
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

            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_ALARM_SELECT", "", "", "客户群预警设置查询成功" + conditionStr, OperResultEnum.Success, LogLevelEnum.Medium);
            return "thresholdList";
        } catch (Exception var9) {
            this.log.error("客户群预警设置查询失败", var9);
            throw new CIServiceException(var9);
        }
    }

    private String queryCustomerIdsStrByCustomerName(String userId, String customerName) {
        CiCustomGroupInfo customGroupInfoVO = new CiCustomGroupInfo();
        customGroupInfoVO.setCreateUserId(userId);
        customGroupInfoVO.setCustomGroupName(customerName);
        List customGroupInfoList = this.customersManagerService.queryCustomersListByCustomGroupInfo(customGroupInfoVO, (String)null, 3);
        String customerGroupIdsStr;
        if(CollectionUtils.isEmpty(customGroupInfoList)) {
            customerGroupIdsStr = "ciCustomerUndefined";
        } else {
            StringBuilder builder = new StringBuilder();

            for(int idx = 0; idx < customGroupInfoList.size(); ++idx) {
                builder.append(((CiCustomGroupInfo)customGroupInfoList.get(idx)).getCustomGroupId());
                if(idx < customGroupInfoList.size() - 1) {
                    builder.append(",");
                }
            }

            customerGroupIdsStr = builder.toString();
        }

        return customerGroupIdsStr;
    }

    public String toEdit() throws CIServiceException {
        if(null != this.alarmThreshold && StringUtils.isNotEmpty(this.alarmThreshold.getThresholdId())) {
            try {
                this.alarmThreshold = CIAlarmServiceUtil.getAlarmThresholdById(this.alarmThreshold.getThresholdId());
            } catch (Exception var2) {
                this.log.error("跳转到修改预警界面出错：" + var2.getMessage());
                throw new CIServiceException("跳转到修改预警界面出错：" + var2.getMessage());
            }
        }

        return "editThreshold";
    }

    private boolean isThresholdExists(String userId) throws CIServiceException {
        boolean isExists = false;

        try {
            List e = CIAlarmServiceUtil.getPageAlarmThresholdsByMore(userId, "CustomersAlarm", this.alarmThreshold.getBusiId(), this.alarmThreshold.getColumnId(), 0, 0, 2147483647);
            Iterator i$ = e.iterator();

            while(i$.hasNext()) {
                AlarmThreshold theThreshold = (AlarmThreshold)i$.next();
                if(!theThreshold.getThresholdId().equals(this.alarmThreshold.getThresholdId()) && theThreshold.getLevelId().equals(this.alarmThreshold.getLevelId())) {
                    isExists = true;
                    break;
                }
            }

            return isExists;
        } catch (Exception var6) {
            this.log.error("CiCustomersAlarmAction.isThresholdExists出错:", var6);
            throw new CIServiceException(var6);
        }
    }

    public void addThreshold() throws CIServiceException {
        String LOG_CUSTOMER_ALARM_ADD = "COC_CUSTOMER_ALARM_ADD";
        if(StringUtil.isNotEmpty(this.fromPageFlag) && "1".equals(this.fromPageFlag)) {
            LOG_CUSTOMER_ALARM_ADD = "COC_CUSTOMER_ALARM_ADD_MANAGE";
        }

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
            this.alarmThreshold.setTypeId("CustomersAlarm");

            try {
                CIAlarmServiceUtil.createAlarmThreshold(this.alarmThreshold);
                CILogServiceUtil.getLogServiceInstance().log(LOG_CUSTOMER_ALARM_ADD, this.alarmThreshold.getBusiId(), this.alarmThreshold.getBusiName(), "新增客户群预警成功【客户群名称:" + this.alarmThreshold.getBusiName() + ",客户群ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var11) {
                success = false;
                errorType = "0";
                CILogServiceUtil.getLogServiceInstance().log(LOG_CUSTOMER_ALARM_ADD, "-1", "新增客户群预警失败", "新增客户群预警失败【客户群名称:" + this.alarmThreshold.getBusiName() + ",客户群ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
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
            this.alarmThreshold.setTypeId("CustomersAlarm");

            try {
                CIAlarmServiceUtil.updateAlarmThreshold(this.alarmThreshold);
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_ALARM_UPDATE", this.alarmThreshold.getBusiId(), this.alarmThreshold.getBusiName(), "修改客户群预警成功【客户群名称:" + this.alarmThreshold.getBusiName() + ",客户群ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var10) {
                success = false;
                errorType = "0";
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_ALARM_UPDATE", this.alarmThreshold.getBusiId(), this.alarmThreshold.getBusiName(), "修改客户群预警失败【客户群名称:" + this.alarmThreshold.getBusiName() + ",客户群ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
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
                        CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_ALARM_DELETE", this.alarmThreshold.getBusiId(), this.alarmThreshold.getBusiName(), "删除客户群预警成功【客户群名称:" + this.alarmThreshold.getBusiName() + ",客户群ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                    } catch (Exception var8) {
                        success = false;
                        this.log.error("删除阈值设置[thresholdId = " + e + "]失败", var8);
                        msg = msg + "删除阈值设置[thresholdId = " + e + "]失败,异常[" + var8.getMessage() + "];";
                        CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_ALARM_DELETE", this.alarmThreshold.getBusiId(), this.alarmThreshold.getBusiName(), "删除客户群预警失败【客户群名称:" + this.alarmThreshold.getBusiName() + ",客户群ID:" + this.alarmThreshold.getBusiId() + "，预警级别:" + this.alarmThreshold.getLevelId() + ",预警阈值描述:" + CIAlarmServiceUtil.createThresholdDesc(this.alarmThreshold) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                    }
                } catch (Exception var9) {
                    success = false;
                    this.log.error("获取阈值设置[thresholdId = " + e + "]失败", var9);
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

    public void findCustomerGroupTree() throws Exception {
        List treeList = null;
        boolean success = false;
        HashMap result = new HashMap();

        try {
            String response = this.getUserIdFromPriv();
            treeList = this.customersManagerService.queryUserCustomerGroupTree(response);
            success = true;
            result.put("treeList", treeList);
            result.put("success", Boolean.valueOf(success));
        } catch (CIServiceException var7) {
            String e = "获得我的客户群树失败";
            this.log.error(e, var7);
            success = false;
            result.put("msg", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void findCustomersAlarmRecord() throws Exception {
        HashMap returnMap = new HashMap();
        boolean success = false;

        String e;
        try {
            String response = this.getUserId();
            e = this.customRelModel.getCustomGroupId();
            CiCustomGroupInfo ciCustomGroupInfo = this.customersManagerService.queryCiCustomGroupInfo(e);
            String dataDate = ciCustomGroupInfo.getDataDate();
            String alarmStartDate = null;
            String alarmEndDate = null;
            Timestamp date = DateUtil.string2Timestamp(dataDate.substring(0, 4) + "-" + dataDate.substring(4, 6) + "-01 00:00:00");
            alarmStartDate = DateUtil.date2String(DateUtil.firstDayOfMonth(date), "yyyy-MM-dd") + " 00:00:00";
            alarmEndDate = DateUtil.date2String(DateUtil.lastDayOfMonth(date), "yyyy-MM-dd") + " 23:59:59";
            Map alarmTable = this.getAlarmAlertTable(dataDate, alarmStartDate, alarmEndDate, response, e, "CustomersAlarm");
            CILogServiceUtil.getLogServiceInstance().log("COC_CUS_ALARM_DEATIL_VIEW", this.customRelModel.getCustomGroupId(), ciCustomGroupInfo.getCustomGroupName(), "客户群预警详情查看【客户群ID：" + ciCustomGroupInfo.getCustomGroupId() + "名称：" + this.customRelModel.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            success = true;
            returnMap.put("result", alarmTable);
            returnMap.put("success", Boolean.valueOf(success));
        } catch (Exception var12) {
            e = "查询客户群预警信息失败";
            this.log.error(e + "，客户群ID：" + this.customRelModel.getCustomGroupId(), var12);
            returnMap.put("msg", e);
            success = false;
            returnMap.put("success", Boolean.valueOf(success));
            CILogServiceUtil.getLogServiceInstance().log("COC_CUS_ALARM_DEATIL_VIEW", this.customRelModel.getCustomGroupId(), this.customRelModel.getCustomGroupName(), "客户群预警详情查看【客户群ID：" + this.customRelModel.getCustomGroupId() + "名称：" + this.customRelModel.getCustomGroupName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    private Map<String, Map<String, String>> getAlarmAlertTable(String dataDate, String alarmStartDate, String alarmEndDate, String userId, String customGroupId, String type) throws Exception {
        HashMap columnIdToThresholdMap = new HashMap();
        HashMap thresholdIdToThresholdMap = new HashMap();
        List alarmThresholdList = CIAlarmServiceUtil.getPageAlarmThresholdsByMore(userId, type, String.valueOf(customGroupId), (String)null, 0, 0, 2147483647);
        Iterator customListInfoVO = alarmThresholdList.iterator();

        while(customListInfoVO.hasNext()) {
            AlarmThreshold customListInfoList = (AlarmThreshold)customListInfoVO.next();
            columnIdToThresholdMap.put(customListInfoList.getColumnId(), customListInfoList);
            thresholdIdToThresholdMap.put(customListInfoList.getThresholdId(), customListInfoList);
        }

        CiCustomListInfo var23 = new CiCustomListInfo();
        var23.setDataDate(dataDate);
        var23.setDataStatus(Integer.valueOf(3));
        var23.setCustomGroupId(customGroupId);
        List var24 = this.customersManagerService.queryCiCustomListInfoByCustomListInfo(var23);
        HashMap columnIdToRealVal = new HashMap();
        if(CollectionUtils.isNotEmpty(var24)) {
            CiCustomListInfo thresholdToRecordMap = (CiCustomListInfo)var24.get(0);
            columnIdToRealVal.put("0", String.valueOf(thresholdToRecordMap.getCustomNum()));
            Long allResult = thresholdToRecordMap.getRingNum();
            if(allResult == null) {
                columnIdToRealVal.put("1", "-");
            } else {
                columnIdToRealVal.put("1", String.valueOf(allResult));
            }
        }

        HashMap var25 = new HashMap();
        List var26 = CIAlarmServiceUtil.getPageAlarmRecords(alarmStartDate, alarmEndDate, userId, type, (String)null, String.valueOf(customGroupId), (String)null, -1, 0, 2147483647);
        Iterator alarmTable = var26.iterator();

        while(alarmTable.hasNext()) {
            AlarmRecord alarmColumns = (AlarmRecord)alarmTable.next();
            var25.put(alarmColumns.getThresholdId(), alarmColumns);
        }

        HashMap var27 = new HashMap();
        String[] var28 = new String[]{String.valueOf("0"), String.valueOf("1")};
        String[] arr$ = var28;
        int len$ = var28.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String alarmColumn = arr$[i$];
            HashMap colMap = new HashMap();
            if(columnIdToThresholdMap.containsKey(alarmColumn)) {
                AlarmThreshold threshold = (AlarmThreshold)columnIdToThresholdMap.get(alarmColumn);
                colMap.put("threshold", CIAlarmServiceUtil.createThresholdDesc(threshold));
                if(var25.containsKey(threshold.getThresholdId())) {
                    if("1".equals(threshold.getLevelId())) {
                        colMap.put("level", "高");
                    } else if("2".equals(threshold.getLevelId())) {
                        colMap.put("level", "中");
                    } else if("3".equals(threshold.getLevelId())) {
                        colMap.put("level", "低");
                    }

                    colMap.put("realValue", columnIdToRealVal.get(alarmColumn));
                } else {
                    colMap.put("level", "正常");
                    colMap.put("realValue", "-");
                }
            } else {
                colMap.put("level", "未设置");
                colMap.put("threshold", "未设置");
                colMap.put("realValue", "-");
            }

            var27.put(alarmColumn, colMap);
        }

        return var27;
    }

    public void createAlarmRecord() {
        this.customersAlarmJob.createMonthlyAlarmRecord();
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

    public CiCustomRelModel getCustomRelModel() {
        return this.customRelModel;
    }

    public void setCustomRelModel(CiCustomRelModel customRelModel) {
        this.customRelModel = customRelModel;
    }

    public String getFromPageFlag() {
        return this.fromPageFlag;
    }

    public void setFromPageFlag(String fromPageFlag) {
        this.fromPageFlag = fromPageFlag;
    }

    public String getShowType() {
        return this.showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }
}

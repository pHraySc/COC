package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomSourceRel;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CustomGroupContrastDetailInfo;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICustomersAnalysisService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import common.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CustomersCompareAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CustomersCompareAnalysisAction.class);
    private String dataDate;
    private CiCustomGroupInfo customGroupInfo;
    private List<LabelShortInfo> contrastLabelInfos;
    private CustomGroupContrastDetailInfo customGroupContrast;
    private String hasContrastLabel;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICustomersAnalysisService customersAnalysisService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;

    public CustomersCompareAnalysisAction() {
    }

    public String customGroupConstrast() {
        String fromPageFlag = this.getRequest().getParameter("fromPageFlag");
        String COC_CUS_CONTRAST_ANALYSIS_LINK = "";
        if("1".equals(fromPageFlag)) {
            COC_CUS_CONTRAST_ANALYSIS_LINK = "COC_CUS_CONTRAST_ANALYSIS_LINK_INDEX";
        } else {
            COC_CUS_CONTRAST_ANALYSIS_LINK = "COC_CUS_CONTRAST_ANALYSIS_LINK";
        }

        CiCustomListInfo customListInfo = null;
        String customGroupId = this.customGroupContrast.getCustomGroupId();
        String listTableName = this.customGroupContrast.getListTableName();
        if(StringUtils.isNotBlank(this.dataDate)) {
            this.customGroupContrast.setDataDate(this.dataDate);
        } else {
            this.customGroupContrast.setDataDate(CacheBase.getInstance().getNewLabelMonth());
        }

        if(StringUtils.isBlank(listTableName)) {
            customListInfo = this.customersService.queryCiCustomListInfoByCGroupIdAndDataDate(customGroupId, this.dataDate);
        } else {
            customListInfo = this.customersService.queryCiCustomListInfoById(listTableName);
        }

        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        CILogServiceUtil.getLogServiceInstance().log(COC_CUS_CONTRAST_ANALYSIS_LINK, customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群对比分析页【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Normal);
        this.customGroupContrast.setCustomGroupName(customGroupInfo.getCustomGroupName());
        this.customGroupContrast.setListTableName(listTableName);
        this.customGroupContrast.setCustomGroupNum(customListInfo.getCustomNum() + "");
        return "customGroupConstrast";
    }

    public String customersCompareAnalysis() {
        String customGroupId = this.customGroupContrast.getCustomGroupId();
        String dataDate = this.customGroupContrast.getDataDate();
        String userId = this.getUserId();
        long customNum = 0L;
        String listTableName = null;
        this.contrastLabelInfos = this.customersAnalysisService.queryCustomersCompareAnalysis(customGroupId, userId, dataDate);
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        CiCustomListInfo customListInfo = this.customersService.queryCiCustomListInfoByCGroupIdAndDataDate(customGroupId, dataDate);
        if(customListInfo != null) {
            customNum = customListInfo.getCustomNum().longValue();
            listTableName = customListInfo.getListTableName();
        }

        this.hasContrastLabel = "false";
        if(this.contrastLabelInfos != null && this.contrastLabelInfos.size() != 0 && listTableName != null) {
            this.hasContrastLabel = "true";
            Iterator labelShortInfo = this.contrastLabelInfos.iterator();

            while(labelShortInfo.hasNext()) {
                LabelShortInfo labelId = (LabelShortInfo)labelShortInfo.next();
                String ciLabelRuleList = labelId.getUpdateCycle();
                if(ciLabelRuleList.equals("1")) {
                    Integer lastDate = this.getLabelUserNum(labelId.getLabelId(), (String)null);
                    labelId.setCustomNum(String.valueOf(lastDate));
                }
            }

            LabelShortInfo labelShortInfo1 = (LabelShortInfo)this.contrastLabelInfos.get(0);
            String labelId1 = labelShortInfo1.getLabelId();
            List ciLabelRuleList1 = this.getLabelRuleList(labelId1);
            String lastDate1 = this.getNewLabelDay();
            String selectPhoneNoSql = this.customersService.getWithColumnSqlStr(ciLabelRuleList1, dataDate, lastDate1, this.getUserId(), (List)null, (Integer)null, (Integer)null);
            Integer labelCustomNum = this.getLabelUserNum(labelId1, dataDate);
            this.customGroupContrast.setContrastLabelNum(labelCustomNum);
            this.customGroupContrast.setCustomGroupNum(customNum + "");
            this.customGroupContrast = this.customersAnalysisService.queryCompareAnalysis(selectPhoneNoSql, listTableName, this.customGroupContrast);
            this.customGroupContrast.setCustomGroupNum(customListInfo.getCustomNum() + "");
            this.customGroupContrast.setContrastLabelId(labelShortInfo1.labelId);
            this.customGroupContrast.setContrastLabelName(labelShortInfo1.getLabelName());
            this.customGroupContrast.setListTableName(listTableName);
            this.customGroupContrast.setCustomGroupId(customGroupId);
            this.customGroupContrast.setCustomGroupName(customGroupInfo.getCustomGroupName());
            this.customGroupContrast.setDataDate(dataDate);
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_CONTRAST_ANALYSIS_VIEW", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群对比分析页【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customersCompareAnalysis";
    }

    public void findCustomContrast() {
        String userId = PrivilegeServiceUtil.getUserId();
        String customGroupId = this.customGroupContrast.getCustomGroupId();
        String dataDate = this.customGroupContrast.getDataDate();
        String contrastLabelstr = this.customGroupContrast.getContrastLabelstr();
        String listTableName = this.customGroupContrast.getListTableName();
        String customNum = this.customGroupContrast.getCustomGroupNum();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "客户群对比查询成功！";
        ArrayList contrastList = new ArrayList();

        try {
            CiCustomGroupInfo response = this.customersService.queryCiCustomGroupInfo(customGroupId);
            if(StringUtils.isNotBlank(listTableName)) {
                String contrastLabelId;
                String selectPhoneNoSql;
                if(StringUtils.isNotBlank(contrastLabelstr)) {
                    String[] var25 = contrastLabelstr.split(",");
                    if(null != var25 && var25.length > 0) {
                        String[] var26 = var25;
                        int var27 = var25.length;

                        for(int var28 = 0; var28 < var27; ++var28) {
                            contrastLabelId = var26[var28];
                            CustomGroupContrastDetailInfo var29 = new CustomGroupContrastDetailInfo();
                            Integer var30 = this.getLabelUserNum(contrastLabelId, dataDate);
                            var29.setContrastLabelNum(var30);
                            var29.setCustomGroupNum(customNum);
                            List var31 = this.getLabelRuleList(contrastLabelId);
                            selectPhoneNoSql = this.getNewLabelDay();
                            String selectPhoneNoSql1 = this.customersService.getWithColumnSqlStr(var31, dataDate, selectPhoneNoSql, this.getUserId(), (List)null, (Integer)null, (Integer)null);
                            var29 = this.customersAnalysisService.queryCompareAnalysis(selectPhoneNoSql1, listTableName, var29);
                            var29.setCustomGroupId(customGroupId);
                            var29.setCustomGroupName(response.getCustomGroupName());
                            var29.setCustomGroupNum(customNum + "");
                            var29.setContrastLabelId(contrastLabelId);
                            var29.setContrastLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId));
                            contrastList.add(var29);
                        }
                    }
                } else {
                    List e = this.customersAnalysisService.queryCustomersCompareAnalysis(customGroupId, userId, dataDate);
                    if(null != e && e.size() > 0) {
                        Iterator i$ = e.iterator();

                        while(i$.hasNext()) {
                            LabelShortInfo contrastLabel = (LabelShortInfo)i$.next();
                            CustomGroupContrastDetailInfo contrastDetailInfo = new CustomGroupContrastDetailInfo();
                            contrastLabelId = contrastLabel.getLabelId();
                            Integer labelNum = this.getLabelUserNum(contrastLabelId, dataDate);
                            contrastDetailInfo.setContrastLabelNum(labelNum);
                            contrastDetailInfo.setCustomGroupNum(customNum);
                            List ciLabelRuleList = this.getLabelRuleList(contrastLabelId);
                            String lastDate = this.getNewLabelDay();
                            selectPhoneNoSql = this.customersService.getWithColumnSqlStr(ciLabelRuleList, dataDate, lastDate, this.getUserId(), (List)null, (Integer)null, (Integer)null);
                            contrastDetailInfo = this.customersAnalysisService.queryCompareAnalysis(selectPhoneNoSql, listTableName, contrastDetailInfo);
                            contrastDetailInfo.setCustomGroupId(customGroupId);
                            contrastDetailInfo.setCustomGroupName(response.getCustomGroupName());
                            contrastDetailInfo.setCustomGroupNum(customNum);
                            contrastDetailInfo.setContrastLabelId(contrastLabel.labelId);
                            contrastDetailInfo.setContrastLabelName(contrastLabel.getLabelName());
                            contrastList.add(contrastDetailInfo);
                        }
                    }
                }

                returnMsg.put("contrastCustomList", contrastList);
            } else {
                message = "客户群没有清单表";
                success = false;
            }
        } catch (Exception var23) {
            message = "标签对比查询异常";
            this.log.error(message, var23);
            success = false;
        }

        HttpServletResponse var24 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(var24, JsonUtil.toJson(returnMsg));
        } catch (Exception var22) {
            this.log.error("发送json串异常", var22);
            throw new CIServiceException(var22);
        }
    }

    public void findCustomContrastList() {
        String userId = PrivilegeServiceUtil.getUserId();
        String customGroupId = this.customGroupContrast.getCustomGroupId();
        String dataDate = this.customGroupContrast.getDataDate();
        String contrastLabelstr = this.customGroupContrast.getContrastLabelstr();
        String listTableName = this.customGroupContrast.getListTableName();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "客户群对比查询成功！";
        ArrayList contrastList = new ArrayList();

        try {
            if(StringUtils.isNotBlank(listTableName)) {
                if(StringUtils.isNotBlank(contrastLabelstr)) {
                    String[] var18 = contrastLabelstr.split(",");
                    if(null != var18 && var18.length > 0) {
                        String[] var20 = var18;
                        int var21 = var18.length;

                        for(int var22 = 0; var22 < var21; ++var22) {
                            String contrastLabelId = var20[var22];
                            CustomGroupContrastDetailInfo contrastDetailInfo1 = new CustomGroupContrastDetailInfo();
                            contrastDetailInfo1.setContrastLabelId(contrastLabelId);
                            contrastDetailInfo1.setContrastLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId));
                            contrastList.add(contrastDetailInfo1);
                        }
                    }
                } else {
                    List response = this.customersAnalysisService.queryCustomersCompareAnalysis(customGroupId, userId, dataDate);
                    if(null != response && response.size() > 0) {
                        Iterator e = response.iterator();

                        while(e.hasNext()) {
                            LabelShortInfo contrastLabel = (LabelShortInfo)e.next();
                            CustomGroupContrastDetailInfo contrastDetailInfo = new CustomGroupContrastDetailInfo();
                            contrastDetailInfo.setContrastLabelId(contrastLabel.labelId);
                            contrastDetailInfo.setContrastLabelName(contrastLabel.getLabelName());
                            contrastList.add(contrastDetailInfo);
                        }
                    }
                }

                returnMsg.put("contrastCustomList", contrastList);
            } else {
                message = "客户群没有清单表";
                success = false;
            }
        } catch (Exception var17) {
            message = "标签对比查询异常";
            this.log.error(message, var17);
            success = false;
        }

        HttpServletResponse var19 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(var19, JsonUtil.toJson(returnMsg));
        } catch (Exception var16) {
            this.log.error("发送json串异常", var16);
            throw new CIServiceException(var16);
        }
    }

    public void customersCompareAnalysisResult() {
        boolean success = true;
        String message = "标签对比分析成功！";
        HashMap returnMsg = new HashMap();
        String contrastLabelId = this.customGroupContrast.getContrastLabelId();
        String dataDate = this.customGroupContrast.getDataDate();
        String customNum = this.customGroupContrast.getCustomGroupNum();
        String listTableName = this.customGroupContrast.getListTableName();
        String customGroupId = this.customGroupContrast.getCustomGroupId();
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);

        try {
            if(StringUtils.isNotBlank(contrastLabelId) && StringUtils.isNotBlank(listTableName) && StringUtils.isNotBlank(dataDate)) {
                Integer e = this.getLabelUserNum(contrastLabelId, dataDate);
                this.customGroupContrast.setContrastLabelNum(e);
                this.customGroupContrast.setCustomGroupNum(customNum);
                List ciLabelRuleList = this.getLabelRuleList(contrastLabelId);
                String lastDate = this.getNewLabelDay();
                String selectPhoneNoSql = this.customersService.getWithColumnSqlStr(ciLabelRuleList, dataDate, lastDate, this.getUserId(), (List)null, (Integer)null, (Integer)null);
                this.customGroupContrast = this.customersAnalysisService.queryCompareAnalysis(selectPhoneNoSql, listTableName, this.customGroupContrast);
                this.customGroupContrast.setContrastLabelId(contrastLabelId);
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_CONTRAST_ANALYSIS", contrastLabelId, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId), "主标签客户群与对比标签的分析取数成功,【主标签客户群ID :" + customGroupId + "主标签客户群名称 :" + customGroupInfo.getCustomGroupName() + ",对比标签ID:" + contrastLabelId + "对比标签名称 :" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var24) {
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_CONTRAST_ANALYSIS", contrastLabelId, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId), "主标签客户群与对比标签的分析取数失败,【主标签客户群ID :" + customGroupId + "主标签客户群名称 :" + customGroupInfo.getCustomGroupName() + ",对比标签ID:" + contrastLabelId + "对比标签名称 :" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId) + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            message = "标签关联用户数分析统计查询异常";
            this.log.error(message, var24);
            success = false;
        } finally {
            HttpServletResponse response = this.getResponse();
            returnMsg.put("customGroupContrast", this.customGroupContrast);
            returnMsg.put("success", Boolean.valueOf(success));
            returnMsg.put("message", message);

            try {
                this.sendJson(response, JsonUtil.toJson(returnMsg));
            } catch (Exception var23) {
                this.log.error("发送json串异常", var23);
                throw new CIServiceException(var23);
            }
        }

    }

    public String saveCustomersCompareAnalysis() {
        String userId = this.getUserId();
        String customGroupId = this.customGroupContrast.getCustomGroupId();
        String contrastLabelstr = this.customGroupContrast.getContrastLabelstr();
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "客户群对比保存成功！";
        String contrastLabelNamestr = "";

        try {
            if(StringUtils.isNotBlank(contrastLabelstr) && StringUtils.isNotBlank(customGroupId)) {
                this.customersAnalysisService.updateCiUserCustomContrast(userId, customGroupId);
                String[] response = contrastLabelstr.split(",");
                String[] e = response;
                int len$ = response.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    String contrastLabelId = e[i$];
                    this.customersAnalysisService.saveCompareAnalysis(customGroupId, contrastLabelId, userId);
                    contrastLabelNamestr = contrastLabelNamestr + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId) + ",";
                }

                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_CONTRAST_SAVE", customGroupId, customGroupInfo.getCustomGroupName(), "保存客户群与对比标签的对比关系成功,【客户群ID：" + customGroupId + ",客户群名称：" + customGroupInfo.getCustomGroupName() + ",关联标签ID：" + contrastLabelstr + ",关联标签名称：" + contrastLabelNamestr.indexOf(0, contrastLabelNamestr.lastIndexOf(",") - 1) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var15) {
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_CONTRAST_SAVE", customGroupId, customGroupInfo.getCustomGroupName(), "保存客户群与对比标签的对比关系失败,【客户群ID：" + customGroupId + ",客户群名称：" + customGroupInfo.getCustomGroupName() + ",关联标签ID：" + contrastLabelstr + ",关联标签名称：" + contrastLabelNamestr.indexOf(0, contrastLabelNamestr.lastIndexOf(",") - 1) + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            message = "客户群对比保存异常";
            this.log.error(message, var15);
            success = false;
        }

        HttpServletResponse var16 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(var16, JsonUtil.toJson(returnMsg));
            return null;
        } catch (Exception var14) {
            this.log.error("发送json串异常", var14);
            throw new CIServiceException(var14);
        }
    }

    public String customContrastSave() {
        String userId = PrivilegeServiceUtil.getUserId();
        String customGroupId = this.customGroupContrast.getCustomGroupId();
        String dataDate = this.customGroupContrast.getDataDate();
        String listTableName = this.customGroupContrast.getListTableName();
        this.log.info(this.customGroupContrast.getDataDate());
        if(StringUtils.isEmpty(dataDate)) {
            dataDate = CacheBase.getInstance().getNewLabelMonth();
            this.customGroupContrast.setDataDate(dataDate);
        }

        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        this.contrastLabelInfos = this.customersAnalysisService.queryCustomersCompareAnalysis(customGroupId, userId, dataDate);
        if(null != this.contrastLabelInfos && this.contrastLabelInfos.size() > 0) {
            Iterator contrastLabel = this.contrastLabelInfos.iterator();

            Integer labelNum;
            while(contrastLabel.hasNext()) {
                LabelShortInfo contrastLabelId = (LabelShortInfo)contrastLabel.next();
                String e = contrastLabelId.getUpdateCycle();
                if(e.equals("1")) {
                    labelNum = this.getLabelUserNum(contrastLabelId.getLabelId(), (String)null);
                    contrastLabelId.setCustomNum(String.valueOf(labelNum));
                }
            }

            LabelShortInfo contrastLabel1 = (LabelShortInfo)this.contrastLabelInfos.get(0);
            String contrastLabelId1 = contrastLabel1.getLabelId();

            try {
                List e1 = this.getLabelRuleList(contrastLabelId1);
                labelNum = this.getLabelUserNum(contrastLabelId1, dataDate);
                this.customGroupContrast.setContrastLabelNum(labelNum);
                String lastDate = this.getNewLabelDay();
                String selectPhoneNoSql = this.customersService.getWithColumnSqlStr(e1, dataDate, lastDate, this.getUserId(), (List)null, (Integer)null, (Integer)null);
                this.customGroupContrast = this.customersAnalysisService.queryCompareAnalysis(selectPhoneNoSql, listTableName, this.customGroupContrast);
                this.customGroupContrast.setCustomGroupName(customGroupInfo.getCustomGroupName());
                this.customGroupContrast.setContrastLabelId(contrastLabel1.getLabelId());
                this.customGroupContrast.setContrastLabelName(contrastLabel1.getLabelName());
            } catch (Exception var12) {
                this.log.error("标签关联用户数分析统计查询异常", var12);
                throw new CIServiceException("标签关联用户数分析统计查询异常", var12);
            }
        }

        return "customContrastSave";
    }

    public void updateUserNum() {
        HashMap returnMsg = new HashMap();
        Integer customNum = Integer.valueOf(0);
        String listTableName = "";
        boolean success = false;
        String message = "查询客户群数量成功";
        if(this.customGroupContrast != null) {
            try {
                String response = this.customGroupContrast.getCustomGroupId();
                String e = this.customGroupContrast.getDataDate();
                if(StringUtils.isNotBlank(response) && StringUtils.isNotBlank(e)) {
                    CiCustomGroupInfo customGroup = new CiCustomGroupInfo();
                    customGroup.setCustomGroupId(response);
                    customGroup.setDataDate(e);
                    CiCustomListInfo customListInfo = this.customersService.queryCustomListInfo(customGroup);
                    if(customListInfo != null && customListInfo.getCustomNum() != null) {
                        customNum = Integer.valueOf(customListInfo.getCustomNum().intValue());
                        listTableName = customListInfo.getListTableName();
                        success = true;
                    } else {
                        success = false;
                        message = "客户群没有清单表";
                    }
                }
            } catch (Exception var11) {
                message = "查询客户群数量异常";
            }
        }

        HttpServletResponse response1 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("customNum", customNum);
        returnMsg.put("listTableName", listTableName);
        returnMsg.put("message", message);

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var10) {
            this.log.error("发送json串异常", var10);
            throw new CIServiceException(var10);
        }
    }

    public void isShowAlarm() {
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "查询客户群监控成功";
        String isAlarmStr = "false";
        if(this.customGroupContrast != null) {
            try {
                String response = this.customGroupContrast.getCustomGroupId();
                String e = this.customGroupContrast.getDataDate();
                if(StringUtils.isNotBlank(response) && StringUtils.isNotBlank(e)) {
                    boolean isAlarm = CIAlarmServiceUtil.haveAlarmRecords(e, this.userId, "CustomersAlarm", response);
                    if(isAlarm) {
                        isAlarmStr = "true";
                    } else {
                        isAlarmStr = "false";
                    }
                }
            } catch (Exception var9) {
                message = "查询客户群监控异常";
            }
        }

        HttpServletResponse response1 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);
        returnMsg.put("isAlarm", isAlarmStr);

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public void delUserCustomContrast() {
        String userId = this.getUserId();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "客户群对比删除成功！";
        String customGroupId = this.customGroupContrast.getCustomGroupId();
        String contrastLabelId = this.customGroupContrast.getContrastLabelId();
        String contrastLabelName = IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId);

        try {
            this.customersAnalysisService.delUserCustomContrast(customGroupId, contrastLabelId, userId);
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_CONTRAST_DELETE", contrastLabelId, contrastLabelName, "客户群对比分析删除标签成功！,【客户群ID:" + customGroupId + ",对比标签ID:" + contrastLabelId + ",对比标签名称:" + contrastLabelName + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var11) {
            message = "客户群对比删除异常";
            this.log.error(message, var11);
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_CONTRAST_DELETE", contrastLabelId, contrastLabelName, "客户群对比分析删除标签失败！,【客户群ID:" + customGroupId + ",对比标签ID:" + contrastLabelId + ",对比标签名称:" + contrastLabelName + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse response = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var10) {
            this.log.error("发送json串异常", var10);
            throw new CIServiceException(var10);
        }
    }

    public void saveCustomContrastCustomGroup() throws Exception {
        String type = this.getRequest().getParameter("type");
        HashMap returnMap = new HashMap();
        String userId = PrivilegeServiceUtil.getUserId();
        if(this.customGroupInfo != null) {
            this.customGroupInfo.setUpdateCycle(Integer.valueOf(1));
            this.customGroupInfo.setCreateTypeId(Integer.valueOf(5));
            this.customGroupInfo.setCreateUserId(userId);
            this.customGroupInfo.setStatus(Integer.valueOf(1));
            this.customGroupInfo.setDataStatus(Integer.valueOf(1));
            String contrastLabelId = this.customGroupContrast.getContrastLabelId();
            String listTableName = this.customGroupContrast.getListTableName();
            String customGroupId = this.customGroupContrast.getCustomGroupId();
            CiCustomGroupInfo customGroup = this.customersService.queryCiCustomGroupInfo(customGroupId);
            String customGroupName = customGroup.getCustomGroupName();
            ArrayList ciCustomSourceRelList = new ArrayList();
            CacheBase cache = CacheBase.getInstance();
            CiLabelInfo ciLabelInfo = cache.getEffectiveLabel(contrastLabelId);
            CiLabelExtInfo ciLabelExtInfo = ciLabelInfo.getCiLabelExtInfo();
            ArrayList ciLabelRuleList = new ArrayList();
            CiLabelRule rule = new CiLabelRule();
            rule.setCalcuElement(String.valueOf(contrastLabelId));
            rule.setElementType(Integer.valueOf(2));
            rule.setLabelFlag(Integer.valueOf(1));
            rule.setMaxVal(Double.valueOf(1.0D));
            rule.setMinVal(Double.valueOf(0.0D));
            if(ciLabelInfo.getLabelTypeId().intValue() == 1) {
                rule.setMaxVal(new Double("1"));
                rule.setMinVal(new Double("0"));
            } else if(ciLabelInfo.getLabelTypeId().intValue() == 3) {
                rule.setAttrVal(ciLabelExtInfo.getAttrVal());
            }

            rule.setSortNum(Long.valueOf(0L));
            rule.setLabelFlag(Integer.valueOf(1));
            ciLabelRuleList.add(rule);
            String optRule = "";
            String labelName = IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId);
            CiCustomSourceRel response;
            CiCustomSourceRel e;
            if(type.equals("intersection")) {
                response = new CiCustomSourceRel();
                response.setElementType(Integer.valueOf(1));
                response.setCalcuElement(listTableName);
                e = new CiCustomSourceRel();
                e.setElementType(Integer.valueOf(0));
                e.setCalcuElement("AND");
                ciCustomSourceRelList.add(response);
                ciCustomSourceRelList.add(e);
                optRule = customGroupName + " 且 " + labelName;
            }

            if(type.equals("mainDifference")) {
                response = new CiCustomSourceRel();
                response.setElementType(Integer.valueOf(1));
                response.setCalcuElement(listTableName);
                response.setSortNum(Long.valueOf(0L));
                e = new CiCustomSourceRel();
                e.setElementType(Integer.valueOf(0));
                e.setCalcuElement("NOT");
                e.setSortNum(Long.valueOf(1L));
                ciCustomSourceRelList.add(response);
                ciCustomSourceRelList.add(e);
                optRule = customGroupName + "且 (非 " + labelName + ")";
            }

            if(type.equals("contrastDifference")) {
                response = new CiCustomSourceRel();
                response.setElementType(Integer.valueOf(0));
                response.setCalcuElement("NOT");
                response.setSortNum(Long.valueOf(0L));
                e = new CiCustomSourceRel();
                e.setElementType(Integer.valueOf(1));
                e.setCalcuElement(listTableName);
                e.setSortNum(Long.valueOf(1L));
                ciCustomSourceRelList.add(response);
                ciCustomSourceRelList.add(e);
                optRule = "(非 " + customGroupName + ") 且" + labelName;
            }

            if(null != ciLabelRuleList && null != ciCustomSourceRelList) {
                try {
                    this.customGroupInfo.setCustomOptRuleShow(optRule);
                    this.customersService.addCiCustomGroupInfo(this.customGroupInfo, ciLabelRuleList, ciCustomSourceRelList, (CiTemplateInfo)null, userId, false, (List)null);
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_CUS_ANALYSIS", this.customGroupInfo.getCustomGroupId().toString(), this.customGroupInfo.getCustomGroupName().toString(), "客户群分析->对比分析保存成功,【客户群名称 :" + this.customGroupInfo.getCustomGroupName() + ",客户群描述:" + this.customGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                    returnMap.put("success", Boolean.valueOf(true));
                } catch (Exception var20) {
                    this.log.error("保存客户群分析->对比分析异常", var20);
                    CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_CUS_ANALYSIS", "-1", this.customGroupInfo.getCustomGroupName().toString(), "客户群分析->对比分析保存失败,【客户群名称 :" + this.customGroupInfo.getCustomGroupName() + ",客户群描述:" + this.customGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                    String e1 = "创建客户群";
                    returnMap.put("success", Boolean.valueOf(false));
                    returnMap.put("cmsg", e1 + "失败");
                    throw new CIServiceException(var20);
                }
            }

            HttpServletResponse response1 = this.getResponse();

            try {
                this.sendJson(response1, JsonUtil.toJson(returnMap));
            } catch (Exception var19) {
                this.log.error("发送json串异常", var19);
                throw new CIServiceException(var19);
            }
        }

    }

    private List<CiLabelRule> getLabelRuleList(String labelId) {
        CacheBase cache = CacheBase.getInstance();
        CiLabelInfo ciLabelInfo = cache.getEffectiveLabel(labelId);
        CiLabelExtInfo ciLabelExtInfo = ciLabelInfo.getCiLabelExtInfo();
        CiLabelRule labelRule = new CiLabelRule();
        labelRule.setSortNum(new Long(3L));
        labelRule.setElementType(Integer.valueOf(2));
        labelRule.setCalcuElement(labelId);
        if(ciLabelInfo.getLabelTypeId().intValue() == 1) {
            labelRule.setMaxVal(new Double("1"));
            labelRule.setMinVal(new Double("0"));
        } else if(ciLabelInfo.getLabelTypeId().intValue() == 3) {
            labelRule.setAttrVal(ciLabelExtInfo.getAttrVal());
        }

        labelRule.setLabelFlag(Integer.valueOf(1));
        ArrayList ciLabelRuleList = new ArrayList();
        ciLabelRuleList.add(labelRule);
        return ciLabelRuleList;
    }

    private Integer getLabelUserNum(String labelId, String dataDate) {
        String updateCycle = this.getLabelUpdateType(labelId);
        if(StringUtils.isEmpty(dataDate)) {
            dataDate = CacheBase.getInstance().getNewLabelMonth();
        }

        if(updateCycle.equals("1")) {
            dataDate = CacheBase.getInstance().getNewLabelDay();
        }

        long userNum = this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(labelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, this.userId).longValue();
        return Integer.valueOf(userNum + "");
    }

    private String getLabelUpdateType(String labelId) {
        CiLabelInfo ciLabelInfo = CacheBase.getInstance().getEffectiveLabel(String.valueOf(labelId));
        Integer updateCycle = ciLabelInfo.getUpdateCycle();
        return updateCycle.toString();
    }

    public CustomGroupContrastDetailInfo getCustomGroupContrast() {
        return this.customGroupContrast;
    }

    public void setCustomGroupContrast(CustomGroupContrastDetailInfo customGroupContrast) {
        this.customGroupContrast = customGroupContrast;
    }

    public List<LabelShortInfo> getContrastLabelInfos() {
        return this.contrastLabelInfos;
    }

    public void setContrastLabelInfos(List<LabelShortInfo> contrastLabelInfos) {
        this.contrastLabelInfos = contrastLabelInfos;
    }

    public CiCustomGroupInfo getCustomGroupInfo() {
        return this.customGroupInfo;
    }

    public void setCustomGroupInfo(CiCustomGroupInfo customGroupInfo) {
        this.customGroupInfo = customGroupInfo;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getHasContrastLabel() {
        return this.hasContrastLabel;
    }

    public void setHasContrastLabel(String hasContrastLabel) {
        this.hasContrastLabel = hasContrastLabel;
    }
}

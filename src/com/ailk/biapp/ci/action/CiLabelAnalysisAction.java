package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiUserAttentionLabel;
import com.ailk.biapp.ci.entity.CiUserAttentionLabelId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICiLabelUserUseService;
import com.ailk.biapp.ci.service.ILabelQueryService;
import com.ailk.biapp.ci.util.CIAlarmServiceUtil;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiLabelAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(this.getClass());
    private LabelDetailInfo labelDetailInfo;
    private CiLabelInfo ciLabelInfo;
    private String selectLabelId;
    private String fromPageFlag;
    boolean approver = false;
    @Autowired
    private ILabelQueryService labelQueryService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICiLabelUserUseService ciLabelUserUseService;

    public CiLabelAnalysisAction() {
    }

    public String initLabelAnalysis() {
        String COC_LABEL_ANALYSIS_LINK = "";
        if("1".equals(this.fromPageFlag)) {
            COC_LABEL_ANALYSIS_LINK = "COC_LABEL_ANALYSIS_LINK_INDEX";
        } else {
            COC_LABEL_ANALYSIS_LINK = "COC_LABEL_ANALYSIS_LINK";
        }

        String labelId = this.getRequest().getParameter("labelId");
        if(StringUtil.isNotEmpty(labelId)) {
            this.ciLabelUserUseService.addLabelUserUseLog(labelId, 1);
            this.labelDetailInfo = this.labelQueryService.queryLabelDetailInfo(labelId);
            String updateCycle = this.labelDetailInfo.getUpdateCycle();
            String dataDate;
            String year;
            String month;
            if(updateCycle.equals("1")) {
                dataDate = CacheBase.getInstance().getNewLabelDay();
                year = dataDate.substring(0, 4);
                month = dataDate.substring(4, 6);
                String customNum = dataDate.substring(6);
                this.labelDetailInfo.setDataDate(dataDate);
                this.labelDetailInfo.setYear(year);
                this.labelDetailInfo.setMonth(month);
                this.labelDetailInfo.setDay(customNum);
                Long customNum1 = this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(labelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, this.userId);
                this.labelDetailInfo.setCustomNum(customNum1.toString());
            } else {
                dataDate = CacheBase.getInstance().getNewLabelMonth();
                year = dataDate.substring(0, 4);
                month = dataDate.substring(4, 6);
                this.labelDetailInfo.setDataDate(dataDate);
                this.labelDetailInfo.setYear(year);
                this.labelDetailInfo.setMonth(month);
                Long customNum2 = this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(labelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, this.userId);
                this.labelDetailInfo.setCustomNum(customNum2.toString());
            }

            CILogServiceUtil.getLogServiceInstance().log(COC_LABEL_ANALYSIS_LINK, this.labelDetailInfo.getLabelId().toString(), this.labelDetailInfo.getLabelName(), "标签分析查询,【标签Id:" + this.labelDetailInfo.getLabelId() + ",标签名称:" + this.labelDetailInfo.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Normal);
            this.log.debug("LabelDetailInfo->" + this.labelDetailInfo);
        }

        return "success";
    }

    public String isShowAlarm() {
        String labelId = this.getRequest().getParameter("labelId");
        String dataDate = this.getRequest().getParameter("dataDate");
        String userId = PrivilegeServiceUtil.getUserId();
        boolean needAuthority = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();

        try {
            boolean returnMsg = !needAuthority || needAuthority && PrivilegeServiceUtil.isAdminUser(userId);
            if(returnMsg) {
                userId = null;
            }
        } catch (Exception var13) {
            this.log.error("判断是否管理员失败", var13);
            var13.printStackTrace();
        }

        HashMap returnMsg1 = new HashMap();
        boolean success = true;
        String message = "标签是否监控查询成功！";
        String isAlarmStr = "false";
        if(StringUtil.isNotEmpty(labelId) && StringUtil.isNotEmpty(dataDate)) {
            try {
                boolean response = CIAlarmServiceUtil.haveAlarmRecords(dataDate, userId, "LabelAlarm", labelId);
                if(response) {
                    isAlarmStr = "true";
                } else {
                    isAlarmStr = "false";
                }
            } catch (Exception var12) {
                this.log.error("", var12);
            }
        } else {
            success = false;
            message = "参数有误";
        }

        HttpServletResponse response1 = this.getResponse();
        returnMsg1.put("success", Boolean.valueOf(success));
        returnMsg1.put("message", message);
        returnMsg1.put("isAlarm", isAlarmStr);

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg1));
            return null;
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    public String findLabelBrandUserNum() {
        String labelId = this.getRequest().getParameter("labelId");
        String dataDate = this.getRequest().getParameter("dataDate");
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "标签关联用户数查询成功！";
        String userNum = "-";
        if(StringUtil.isNotEmpty(labelId) && StringUtil.isNotEmpty(dataDate)) {
            try {
                userNum = this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(labelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, this.userId).toString();
            } catch (Exception var10) {
                success = false;
                message = "标签关联用户数查询异常";
                this.log.error("标签关联用户数查询异常", var10);
                throw new CIServiceException("标签关联用户数查询异常");
            }
        } else {
            success = false;
            message = "参数有误";
        }

        HttpServletResponse response = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);
        returnMsg.put("userNum", userNum);

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
            return null;
        } catch (Exception var9) {
            this.log.error("发送json串异常", var9);
            throw new CIServiceException(var9);
        }
    }

    public void addUserAttentionLabel() {
        String labelId = this.getRequest().getParameter("labelId");
        String userId = PrivilegeServiceUtil.getUserId();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "标签收藏成功";
        CiUserAttentionLabel po = this.ciLabelInfoService.queryUserAttentionLabelRecord(new Integer(labelId), userId);
        if(null != po) {
            message = "抱歉！该标签已经收藏。";
        } else if(StringUtil.isNotEmpty(labelId) && StringUtil.isNotEmpty(userId)) {
            try {
                this.ciLabelInfoService.userAttentionLabelRecord(new Integer(labelId), userId);
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ADD_ATTENTION", labelId, "", "标签分析->添加标签收藏成功,【标签Id:" + labelId + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var10) {
                success = false;
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ADD_ATTENTION", labelId, "", "标签分析->添加标签收藏失败,【标签Id:" + labelId + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                message = "标签收藏失败";
                this.log.error("增加标签收藏记录失败", var10);
            }
        } else {
            success = false;
            message = "参数有误";
        }

        HttpServletResponse response = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var9) {
            this.log.error("发送json串异常", var9);
            throw new CIServiceException(var9);
        }
    }

    public void delUserAttentionLabel() {
        String labelId = this.getRequest().getParameter("labelId");
        String userId = PrivilegeServiceUtil.getUserId();
        Integer delAttentionLabelFromPage = Integer.valueOf(0);

        try {
            delAttentionLabelFromPage = Integer.valueOf(Integer.parseInt(this.getRequest().getParameter("delAttentionLabelFromPage")));
        } catch (Exception var13) {
            ;
        }

        String COC_LABEL_CANCEL_ATTENTION = "";
        if(1 == delAttentionLabelFromPage.intValue()) {
            COC_LABEL_CANCEL_ATTENTION = "COC_LABEL_CANCEL_ATTENTION_INDEX";
        } else {
            COC_LABEL_CANCEL_ATTENTION = "COC_LABEL_CANCEL_ATTENTION";
        }

        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "取消标签收藏成功";
        if(StringUtil.isNotEmpty(labelId) && StringUtil.isNotEmpty(userId)) {
            this.ciLabelInfo = CacheBase.getInstance().getEffectiveLabel(labelId);
            CiUserAttentionLabelId response = new CiUserAttentionLabelId();
            response.setLabelId(Integer.valueOf(labelId));
            response.setUserId(userId);
            CiUserAttentionLabel e = this.ciLabelInfoService.queryUserAttentionLabelRecord(new Integer(labelId), userId);
            if(null == e) {
                message = "抱歉！该标签已经取消收藏。";
            } else {
                try {
                    this.ciLabelInfoService.deleteUserAttentionLabelRecord(new Integer(labelId), userId);
                    CILogServiceUtil.getLogServiceInstance().log(COC_LABEL_CANCEL_ATTENTION, labelId, this.ciLabelInfo.getLabelName(), "用户取消标签收藏成功，查询条件：【标签ID：" + labelId + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                } catch (Exception var12) {
                    success = false;
                    message = "取消标签收藏失败";
                    this.log.error(message, var12);
                    CILogServiceUtil.getLogServiceInstance().log(COC_LABEL_CANCEL_ATTENTION, labelId, this.ciLabelInfo.getLabelName(), "用户取消标签收藏失败，查询条件：【标签ID：" + labelId + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                }
            }
        }

        HttpServletResponse response1 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    public void isUserAttentionLabel() {
        String labelId = this.getRequest().getParameter("labelId");
        String userId = PrivilegeServiceUtil.getUserId();
        boolean isAttention = false;
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "标签收藏判断成功！";
        if(StringUtil.isNotEmpty(labelId) && StringUtil.isNotEmpty(userId)) {
            try {
                CiUserAttentionLabel response = this.ciLabelInfoService.queryUserAttentionLabelRecord(new Integer(labelId), userId);
                if(null != response) {
                    isAttention = true;
                }
            } catch (Exception var10) {
                success = false;
                message = "标签收藏判断失败";
                this.log.error(message, var10);
                throw new CIServiceException(message, var10);
            }
        }

        HttpServletResponse response1 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);
        returnMsg.put("isAttention", Boolean.valueOf(isAttention));

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var9) {
            this.log.error("发送json串异常", var9);
            throw new CIServiceException(var9);
        }
    }

    public String labelAnalysisMain() {
        if(this.ciLabelInfo != null) {
            this.ciLabelInfo = CacheBase.getInstance().getEffectiveLabel(String.valueOf(this.ciLabelInfo.getLabelId()));
        }

        return "labelAnalysisMain";
    }

    public void saveLabelDetailLog() {
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_DETAIL_VIEW", this.ciLabelInfo.getLabelId().toString(), "", "标签分析->标签详情查看成功,【标签Id:" + this.ciLabelInfo.getLabelId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
    }

    public String initlabelAnalysisIndex() {
        String createUserId = PrivilegeServiceUtil.getUserId();
        this.approver = this.ciLabelInfoService.judgeCurrentUserIsApprover(createUserId);
        if(this.selectLabelId != null) {
            this.ciLabelInfo = CacheBase.getInstance().getEffectiveLabel(this.selectLabelId);
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ANALYSIS_MAIN_LINK", "-1", "标签分析首页", "转入标签分析首页成功！", OperResultEnum.Success, LogLevelEnum.Normal);
        return "labelAnalysisIndex";
    }

    public CiLabelInfo getCiLabelInfo() {
        return this.ciLabelInfo;
    }

    public void setCiLabelInfo(CiLabelInfo ciLabelInfo) {
        this.ciLabelInfo = ciLabelInfo;
    }

    public boolean isApprover() {
        return this.approver;
    }

    public void setApprover(boolean approver) {
        this.approver = approver;
    }

    public LabelDetailInfo getLabelDetailInfo() {
        return this.labelDetailInfo;
    }

    public void setLabelDetailInfo(LabelDetailInfo labelDetailInfo) {
        this.labelDetailInfo = labelDetailInfo;
    }

    public String getSelectLabelId() {
        return this.selectLabelId;
    }

    public void setSelectLabelId(String selectLabelId) {
        this.selectLabelId = selectLabelId;
    }

    public String getFromPageFlag() {
        return this.fromPageFlag;
    }

    public void setFromPageFlag(String fromPageFlag) {
        this.fromPageFlag = fromPageFlag;
    }
}

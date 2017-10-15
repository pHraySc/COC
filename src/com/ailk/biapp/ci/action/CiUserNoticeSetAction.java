package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiUserNoticeSet;
import com.ailk.biapp.ci.entity.CiUserNoticeSetId;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiUserNoticeSetService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiUserNoticeSetAction extends CIBaseAction {
    private Logger log = Logger.getLogger(this.getClass());
    private List<CiUserNoticeSet> noticeSets;
    @Autowired
    private ICiUserNoticeSetService ciUserNoticeSetService;
    private String sendModeId;

    public CiUserNoticeSetAction() {
    }

    public String saveUserNoticeSet() throws Exception {
        this.ciUserNoticeSetService.addUserNoticeSet(this.noticeSets);
        return "";
    }

    public String findUserNoticeSet() throws Exception {
        String userId = PrivilegeServiceUtil.getUserId();
        this.noticeSets = this.ciUserNoticeSetService.queryUserNoticeSet(userId);
        return "";
    }

    public String initUserNoticeSet() {
        String fromPageFlag = this.getRequest().getParameter("fromPageFlag");
        String COC_PERSONAL_NOTICE_SET_LINK = "";
        String descStr = "";
        if("1".equals(fromPageFlag)) {
            COC_PERSONAL_NOTICE_SET_LINK = "COC_PERSONAL_NOTICE_SET_LINK_NOTICE";
            descStr = "个性化通知设置页";
        } else if("2".equals(fromPageFlag)) {
            COC_PERSONAL_NOTICE_SET_LINK = "COC_PERSONAL_NOTICE_SET_LINK_ALARM";
            descStr = "个性化通知预警设置页";
        }

        String userId = PrivilegeServiceUtil.getUserId();
        this.noticeSets = this.ciUserNoticeSetService.queryUserNoticeSetInit(userId, this.sendModeId);
        CILogServiceUtil.getLogServiceInstance().log(COC_PERSONAL_NOTICE_SET_LINK, "", "", descStr + "查看成功", OperResultEnum.Success, LogLevelEnum.Medium);
        return "userNoticeSet";
    }

    public void batchUserNoticeSet() {
        String noticeType = this.getRequest().getParameter("noticeType");
        String isReceive = this.getRequest().getParameter("isReceive");
        this.log.debug("noticeType->" + noticeType + ",isReceive=" + isReceive);
        String userId = PrivilegeServiceUtil.getUserId();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "用户批量个性化设置成功！";

        try {
            List response = this.ciUserNoticeSetService.queryUserNoticeSet(userId, noticeType, this.sendModeId);
            if(null != response && response.size() > 0) {
                Iterator e = response.iterator();

                while(e.hasNext()) {
                    CiUserNoticeSet set = (CiUserNoticeSet)e.next();
                    set.setIsReceive(new Integer(isReceive));
                    this.log.debug(set);
                }

                this.ciUserNoticeSetService.addUserNoticeSet(response);
            }

            CacheBase.getInstance().deleteNoticeUserId(userId);
            if("1".equals(noticeType)) {
                CILogServiceUtil.getLogServiceInstance().log("COC_SYS_ANNOUNCEMENT_BATCH_SET", "", "", "个性化通知->公告类批量设置成功", OperResultEnum.Success, LogLevelEnum.Medium);
            } else {
                CILogServiceUtil.getLogServiceInstance().log("COC_PERSONAL_NOTICE_BATCH_SET", "", "", "个性化通知->个人通知类批量设置成功", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var11) {
            message = "用户批量个性化设置异常";
            if("1".equals(noticeType)) {
                CILogServiceUtil.getLogServiceInstance().log("COC_SYS_ANNOUNCEMENT_BATCH_SET", "", "", "个性化通知->公告类批量设置失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            } else {
                CILogServiceUtil.getLogServiceInstance().log("COC_PERSONAL_NOTICE_BATCH_SET", "", "", "个性化通知->个人通知类批量设置失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            }

            this.log.error(message, var11);
            success = false;
        }

        HttpServletResponse response1 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var10) {
            this.log.error("发送json串异常", var10);
            throw new CIServiceException(var10);
        }
    }

    public void userNoticeSet() {
        String isReceive = this.getRequest().getParameter("isReceive");
        String noticeId = this.getRequest().getParameter("noticeId");
        String noticeType = this.getRequest().getParameter("noticeType");
        String isSuccess = this.getRequest().getParameter("isSuccess");
        this.log.debug("noticeType->" + noticeType + ",isReceive=" + isReceive);
        String userId = PrivilegeServiceUtil.getUserId();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "用户个性化设置成功！";
        CiUserNoticeSet noticeSet = new CiUserNoticeSet();
        CiUserNoticeSetId id = new CiUserNoticeSetId();
        id.setIsSuccess(new Integer(isSuccess));
        id.setNoticeId(noticeId);
        id.setNoticeType(new Integer(noticeType));
        id.setUserId(userId);
        id.setSendModeId(this.sendModeId);
        noticeSet.setId(id);
        noticeSet.setIsReceive(new Integer(isReceive));

        try {
            this.ciUserNoticeSetService.addUserNoticeSet(noticeSet);
            CacheBase.getInstance().deleteNoticeUserId(userId);
            if("1".equals(noticeType)) {
                CILogServiceUtil.getLogServiceInstance().log("COC_SYS_ANNOUNCEMENT_SINGLE_SET", noticeSet.getId().toString(), noticeSet.getTypeName(), "个性化通知->公告类单独设置成功", OperResultEnum.Success, LogLevelEnum.Medium);
            } else {
                CILogServiceUtil.getLogServiceInstance().log("COC_PERSONAL_NOTICE_SINGLE_SET", noticeSet.getId().toString(), noticeSet.getTypeName(), "个性化通知->个人通知类单独设置成功", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var14) {
            message = "用户个性化设置异常";
            if("1".equals(noticeType)) {
                CILogServiceUtil.getLogServiceInstance().log("COC_SYS_ANNOUNCEMENT_SINGLE_SET", noticeSet.getId().toString(), noticeSet.getTypeName(), "个性化通知->公告类单独设置失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            } else {
                CILogServiceUtil.getLogServiceInstance().log("COC_PERSONAL_NOTICE_SINGLE_SET", noticeSet.getId().toString(), noticeSet.getTypeName(), "个性化通知->个人通知类单独设置失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            }

            this.log.error(message, var14);
            success = false;
        }

        HttpServletResponse response = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var13) {
            this.log.error("发送json串异常", var13);
            throw new CIServiceException(var13);
        }
    }

    public void checkUserNoticeSet() {
        this.log.info("Enter CiUserNoticeSetAction.checkUserNoticeSet() method");
        String noticeType = this.getRequest().getParameter("noticeType");
        String isReceive = this.getRequest().getParameter("isReceive");
        String userId = PrivilegeServiceUtil.getUserId();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "用户个性化设置查询成功！";
        String isBatch = "true";

        try {
            List response = this.ciUserNoticeSetService.queryUserNoticeSet(userId, noticeType, this.sendModeId);
            Iterator e = response.iterator();

            while(e.hasNext()) {
                CiUserNoticeSet noticeSet = (CiUserNoticeSet)e.next();
                String tempIsReceive = noticeSet.getIsReceive().toString();
                if(!tempIsReceive.equals(isReceive)) {
                    isBatch = "false";
                    break;
                }
            }
        } catch (Exception var13) {
            message = "用户个性化设置查询失败";
            this.log.error(message, var13);
            success = false;
        }

        HttpServletResponse response1 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);
        returnMsg.put("isBatch", isBatch);

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var12) {
            this.log.error("发送json串异常", var12);
            throw new CIServiceException(var12);
        }
    }

    public List<CiUserNoticeSet> getNoticeSets() {
        return this.noticeSets;
    }

    public void setNoticeSets(List<CiUserNoticeSet> noticeSets) {
        this.noticeSets = noticeSets;
    }

    public String getSendModeId() {
        return this.sendModeId;
    }

    public void setSendModeId(String sendModeId) {
        this.sendModeId = sendModeId;
    }
}

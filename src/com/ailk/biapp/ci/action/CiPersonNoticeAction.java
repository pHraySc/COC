package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiNoticeModel;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiPersonNoticeService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiPersonNoticeAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiPersonNoticeAction.class);
    private Pager pager;
    private int currentRecordNum;
    private String noticeIds;
    @Autowired
    private ICiPersonNoticeService personNoticeService;
    private CiPersonNotice personNotice;
    private HashMap<String, Object> pojo = new HashMap();
    private List<CiPersonNotice> ciPersonNoticeList;

    public CiPersonNoticeAction() {
    }

    public String show() throws CIServiceException {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setResult(new ArrayList());
        return "showIndex";
    }

    public void showNoticeLog() {
        String noticeName = "";

        try {
            String e = this.getRequest().getParameter("noticeName");
            if(StringUtil.isNotEmpty(e)) {
                noticeName = URLDecoder.decode(URLDecoder.decode(e, "UTF-8"), "UTF-8");
            }
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_PERSONAL_NOTICE_VIEW", "", noticeName, "个人通知查看成功，查看公告【" + (StringUtil.isEmpty(noticeName)?"无":noticeName) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
    }

    public String updateReadStatus() throws CIServiceException {
        String noticeId = this.pojo.get("noticeId").toString();
        boolean result = false;
        HashMap returnMsg = new HashMap();

        try {
            this.personNoticeService.readPersonNotice(noticeId);
            this.queryReadNotCount();
            result = true;
        } catch (Exception var8) {
            var8.printStackTrace();
            throw new CIServiceException(var8);
        }

        HttpServletResponse response = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(result));
        returnMsg.put("readStatusNot", this.pojo.get("readStatusNot").toString());
        String userId = PrivilegeServiceUtil.getUserId();
        CacheBase.getInstance().removeNotice(userId, noticeId, 2);

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var7) {
            this.log.error(var7.getMessage(), var7);
            var7.printStackTrace();
        }

        return null;
    }

    public void updateAllReadStatus() throws CIServiceException {
        boolean success = false;
        String msg = "";
        HashMap returnMsg = new HashMap();

        try {
            this.personNoticeService.readPersonNotice(this.noticeIds);
            this.queryReadNotCount();
            success = true;
            msg = "标记已读成功";
        } catch (Exception var8) {
            success = false;
            msg = "标记已读失败";
            this.log.error("个人通知标记已读失败", var8);
        }

        String[] noticeIds_array = this.noticeIds.trim().split(",");
        if(noticeIds_array != null && noticeIds_array.length > 0) {
            String response = PrivilegeServiceUtil.getUserId();

            for(int e = 0; e < noticeIds_array.length; ++e) {
                CacheBase.getInstance().removeNotice(response, noticeIds_array[e], 2);
            }
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse var9 = this.getResponse();

        try {
            this.sendJson(var9, JsonUtil.toJson(returnMsg));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public void del() throws Exception {
        boolean success = false;
        String msg = "";
        HashMap returnMsg = new HashMap();
        ArrayList noticeIdList = new ArrayList();

        try {
            String[] response = this.noticeIds.trim().split(",");
            if(response != null && response.length > 0) {
                for(int e = 0; e < response.length; ++e) {
                    noticeIdList.add(response[e]);
                }
            }

            this.personNoticeService.delete(noticeIdList);
            success = true;
            msg = "删除成功";
            String var10 = PrivilegeServiceUtil.getUserId();
            CacheBase.getInstance().deleteNoticeUserId(var10);
            CILogServiceUtil.getLogServiceInstance().log("COC_PERSONAL_NOTICE_DELETE", "-1", "删除", "删除个人通知成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var8) {
            success = false;
            msg = "删除失败";
            this.log.error("删除个人通知失败", var8);
            CILogServiceUtil.getLogServiceInstance().log("COC_PERSONAL_NOTICE_DELETE", "-1", "删除", "删除个人通知失败", OperResultEnum.Success, LogLevelEnum.Medium);
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse var9 = this.getResponse();

        try {
            this.sendJson(var9, JsonUtil.toJson(returnMsg));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public String showQuery() throws CIServiceException {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        try {
            String e = this.getRequest().getParameter("noticeId");
            new ArrayList();
            boolean totalSize = false;
            if(this.personNotice == null) {
                this.personNotice = new CiPersonNotice();
            }

            int pageNum = 0;
            int recNum = 0;
            int noticeNum = 0;
            String userId = PrivilegeServiceUtil.getUserId();
            this.personNotice.setReceiveUserId(userId);
            this.personNotice.setStatus(Integer.valueOf(1));
            if(e != null && !e.equals("") && !e.equals("null")) {
                List totalPage = this.personNoticeService.queryPagePersonNoticeList(1, 2147483647, this.personNotice);

                for(Iterator i$ = totalPage.iterator(); i$.hasNext(); ++noticeNum) {
                    CiPersonNotice notice = (CiPersonNotice)i$.next();
                    if(notice.getNoticeId().equals(e)) {
                        ++noticeNum;
                        break;
                    }
                }

                pageNum = (int)Math.ceil((double)noticeNum / (double)this.pager.getPageSize());
                recNum = noticeNum - (pageNum - 1) * this.pager.getPageSize();
            }

            int var12 = this.personNoticeService.getCountOfPersonNotice(this.personNotice);
            this.pager.setTotalSize((long)var12);
            int var13 = (int)Math.ceil((double)var12 / (double)this.pager.getPageSize());
            this.pager.setTotalPage(var13);
            this.pager = this.pager.pagerFlip();
            if(pageNum > 0 && noticeNum <= var12) {
                this.pager.setPageNum(pageNum);
            } else if(this.pager.getPageNum() == 0) {
                this.pager.setPageNum(1);
            }

            List result = this.personNoticeService.queryPagePersonNoticeList(this.pager.getPageNum(), this.pager.getPageSize(), this.personNotice);
            this.pager.setResult(result);
            if(var12 > 0 && recNum == 0) {
                recNum = 1;
            }

            this.setCurrentRecordNum(recNum);
            this.queryReadNotCount();
            this.queryNoticeCount();
            CILogServiceUtil.getLogServiceInstance().log("COC_PERSONAL_NOTICE_SELECT", "", "", "个人通知列表查看成功", OperResultEnum.Success, LogLevelEnum.Medium);
            return "noticeShowList";
        } catch (Exception var11) {
            this.log.error("标签告警列表查询错误", var11);
            var11.printStackTrace();
            CILogServiceUtil.getLogServiceInstance().log("COC_PERSONAL_NOTICE_SELECT", "", "", "个人通知列表查看失败", OperResultEnum.Success, LogLevelEnum.Medium);
            throw new CIServiceException(var11);
        }
    }

    public String noticeCenter() {
        CacheBase cacheBase = CacheBase.getInstance();
        CiNoticeModel ciNoticeModel = cacheBase.getNoticeModelByUser(this.getUserId());
        int totalCount = ciNoticeModel.getPersonNoticeCount() + ciNoticeModel.getSysCount();
        this.pojo.put("totalCount", Integer.valueOf(totalCount));
        this.pojo.put("sysCount", Integer.valueOf(ciNoticeModel.getSysCount()));
        return "noticeCenter";
    }

    private void queryReadNotCount() {
        CiPersonNotice personNotice = new CiPersonNotice();
        String userId = PrivilegeServiceUtil.getUserId();
        personNotice.setReceiveUserId(userId);
        personNotice.setStatus(Integer.valueOf(1));
        personNotice.setReadStatus(Integer.valueOf(1));
        int readStatusNot = this.personNoticeService.getCountOfPersonNotice(personNotice);
        this.pojo.put("readStatusNot", Integer.valueOf(readStatusNot));
    }

    private void queryNoticeCount() {
        CiPersonNotice personNotice = new CiPersonNotice();
        String userId = PrivilegeServiceUtil.getUserId();
        personNotice.setReceiveUserId(userId);
        personNotice.setStatus(Integer.valueOf(1));
        int totalCount = this.personNoticeService.getCountOfPersonNotice(personNotice);
        this.pojo.put("totalCount", Integer.valueOf(totalCount));
    }

    public String updateShowTipStatus() throws CIServiceException {
        String noticeId = this.pojo.get("noticeId").toString();
        boolean result = false;
        HashMap returnMsg = new HashMap();

        try {
            this.personNoticeService.modifyShowTipStatus(noticeId);
            result = true;
        } catch (Exception var8) {
            var8.printStackTrace();
            throw new CIServiceException(var8);
        }

        HttpServletResponse response = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(result));
        String userId = PrivilegeServiceUtil.getUserId();
        CacheBase.getInstance().closeNotice(userId, noticeId);

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var7) {
            this.log.error(var7.getMessage(), var7);
            var7.printStackTrace();
        }

        return null;
    }

    public void updateAllRead() throws CIServiceException {
        boolean success = false;
        String msg = "";
        HashMap returnMsg = new HashMap();
        if(this.personNotice == null) {
            this.personNotice = new CiPersonNotice();
        }

        try {
            String response = this.getUserId();
            this.personNotice.setReceiveUserId(response);
            this.personNotice.setStatus(Integer.valueOf(1));
            this.personNotice.setReadStatus(Integer.valueOf(1));
            this.personNoticeService.readPersonNoticeAll(this.personNotice);
            this.queryReadNotCount();
            success = true;
            msg = "全部标记已读成功";
        } catch (Exception var7) {
            success = false;
            msg = "全部已读失败";
            this.log.error("个人通知标记已读失败", var7);
        }

        CacheBase.getInstance().clearUserCache();
        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void delAll() throws Exception {
        boolean success = false;
        String msg = "";
        HashMap returnMsg = new HashMap();
        if(this.personNotice == null) {
            this.personNotice = new CiPersonNotice();
        }

        try {
            String response = this.getUserId();
            this.personNotice.setReceiveUserId(response);
            this.personNotice.setStatus(Integer.valueOf(1));
            this.personNoticeService.deleteAll(this.personNotice);
            CacheBase.getInstance().clearUserCache();
            success = true;
            msg = "删除成功";
            CILogServiceUtil.getLogServiceInstance().log("COC_PERSONAL_NOTICE_DELETE", "-1", "删除", "删除个人通知成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var7) {
            success = false;
            msg = "删除失败";
            this.log.error("删除个人通知失败", var7);
            CILogServiceUtil.getLogServiceInstance().log("COC_PERSONAL_NOTICE_DELETE", "-1", "删除", "删除个人通知失败", OperResultEnum.Success, LogLevelEnum.Medium);
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public int getCurrentRecordNum() {
        return this.currentRecordNum;
    }

    public void setCurrentRecordNum(int currentRecordNum) {
        this.currentRecordNum = currentRecordNum;
    }

    public Pager getPager() {
        return this.pager;
    }

    public String getNoticeIds() {
        return this.noticeIds;
    }

    public void setNoticeIds(String noticeIds) {
        this.noticeIds = noticeIds;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public CiPersonNotice getPersonNotice() {
        return this.personNotice;
    }

    public void setPersonNotice(CiPersonNotice personNotice) {
        this.personNotice = personNotice;
    }

    public HashMap<String, Object> getPojo() {
        return this.pojo;
    }

    public void setPojo(HashMap<String, Object> pojo) {
        this.pojo = pojo;
    }

    public List<CiPersonNotice> getCiPersonNoticeList() {
        return this.ciPersonNoticeList;
    }

    public void setCiPersonNoticeList(List<CiPersonNotice> ciPersonNoticeList) {
        this.ciPersonNoticeList = ciPersonNoticeList;
    }
}

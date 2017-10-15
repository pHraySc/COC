package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import com.ailk.biapp.ci.entity.CiUserReadInfo;
import com.ailk.biapp.ci.entity.CiUserReadInfoId;
import com.ailk.biapp.ci.entity.DimAnnouncementType;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiSysAnnouncementService;
import com.ailk.biapp.ci.service.ICiUserReadInfoService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiSysAnnouncementAction extends CIBaseAction {
    private Pager pager;
    private int announce;
    @Autowired
    private ICiSysAnnouncementService sysAnnouncementService;
    private CiSysAnnouncement sysAnnouncement;
    @Autowired
    private ICiUserReadInfoService ciUserReadInfoService;
    private static final String ERROR_CODE_ANNO_INTERFACE = "0";
    private static final String ERROR_CODE_ANNO_EXISTS = "1";
    private Pager syspager;
    private List<DimAnnouncementType> dimAnnouncementTypeList;
    private HashMap<String, Object> pojo = new HashMap();

    public CiSysAnnouncementAction() {
    }

    public String init() throws CIServiceException {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setResult(new ArrayList());
        CacheBase cache = CacheBase.getInstance();
        this.dimAnnouncementTypeList =(List<DimAnnouncementType>) cache.getObjectList("DIM_ANNOUNCEMENT_TYPE");
        return "index";
    }

    public String sysAnnouncementIndex() throws CIServiceException {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setResult(new ArrayList());
        return "index";
    }

    public String query() throws CIServiceException {
        if(this.syspager == null) {
            this.syspager = new Pager();
        }

        String sysType = this.getRequest().getParameter("sysType");
        String sysPriority = this.getRequest().getParameter("sysPriority");
        String sysStatus = this.getRequest().getParameter("sysStatus");

        try {
            new ArrayList();
            boolean totalSize = false;
            if(this.sysAnnouncement == null) {
                this.sysAnnouncement = new CiSysAnnouncement();
            }

            int totalSize1 = this.sysAnnouncementService.querySysAnnouncementCount(this.sysAnnouncement);
            this.syspager.setTotalSize((long)totalSize1);
            int totalpage = (int)Math.ceil((double)this.syspager.getTotalSize() / (double)this.syspager.getPageSize());
            this.syspager.setTotalPage(totalpage);
            this.syspager = this.syspager.pagerFlip();
            List e = this.sysAnnouncementService.querySysAnnouncementList(this.syspager.getPageNum(), this.syspager.getPageSize(), this.sysAnnouncement);
            this.syspager.setResult(e);
            if(this.syspager.isFirstPage() && (StringUtil.isNotEmpty(this.sysAnnouncement.getAnnouncementName()) || StringUtil.isNotEmpty(this.sysAnnouncement.getTypeId()) || StringUtil.isNotEmpty(this.sysAnnouncement.getPriorityId()) || StringUtil.isNotEmpty(this.sysAnnouncement.getStatus()))) {
                CILogServiceUtil.getLogServiceInstance().log("COC_SYSTEM_ANNOUNCEMENT_SELECT", "", "", "系统公告查询成功,查询条件【公告标题 :" + this.sysAnnouncement.getAnnouncementName() + ",公告类型:" + sysType + ",公告优先级:" + sysPriority + ",公告状态 " + sysStatus + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            }

            return "announcementList";
        } catch (Exception var7) {
            var7.printStackTrace();
            this.log.error("系统公告查询失败", var7);
            if(this.syspager.isFirstPage() && (StringUtil.isNotEmpty(this.sysAnnouncement.getAnnouncementName()) || StringUtil.isNotEmpty(this.sysAnnouncement.getTypeId()) || StringUtil.isNotEmpty(this.sysAnnouncement.getPriorityId()) || StringUtil.isNotEmpty(this.sysAnnouncement.getStatus()))) {
                CILogServiceUtil.getLogServiceInstance().log("COC_SYSTEM_ANNOUNCEMENT_SELECT", "", "", "系统公告查询失败,查询条件【公告标题 :" + this.sysAnnouncement.getAnnouncementName() + ",公告类型:" + sysType + ",公告优先级:" + sysPriority + ",公告状态 " + sysStatus + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            }

            throw new CIServiceException(var7);
        }
    }

    public String show() throws CIServiceException {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setResult(new ArrayList());
        return "showIndex";
    }

    public void showAnnounceLog() {
        String announcementName = "";

        try {
            String e = this.getRequest().getParameter("announcementName");
            if(StringUtil.isNotEmpty(e)) {
                announcementName = URLDecoder.decode(URLDecoder.decode(e, "UTF-8"), "UTF-8");
            }
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_SYS_ANNOUNCEMENT_VIEW", "", announcementName, "系统公告查看成功，查看公告【" + (StringUtil.isEmpty(announcementName)?"无":announcementName) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
    }

    public String showQuery() throws CIServiceException {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        try {
            String e = this.getRequest().getParameter("noticeId");
            String userId = this.getUserId();
            new ArrayList();
            boolean totalSize = false;
            if(this.sysAnnouncement == null) {
                this.sysAnnouncement = new CiSysAnnouncement();
            }

            this.sysAnnouncement.setStatus(Integer.valueOf(1));
            int var12 = this.sysAnnouncementService.queryCiUserReadInfoCountByUserId(userId, this.sysAnnouncement);
            int pageNum = 0;
            int recNum = 0;
            int noticeNum = 0;
            if(e != null && !e.equals("") && !e.equals("null")) {
                List allSysAnnoucementList = this.sysAnnouncementService.queryCiUserReadInfoListByUserId(userId, this.sysAnnouncement, 1, 2147483647);

                for(Iterator i$ = allSysAnnoucementList.iterator(); i$.hasNext(); ++noticeNum) {
                    CiSysAnnouncement notice = (CiSysAnnouncement)i$.next();
                    if(notice.getAnnouncementId().equals(e)) {
                        ++noticeNum;
                        break;
                    }
                }

                pageNum = (int)Math.ceil((double)noticeNum / (double)this.pager.getPageSize());
                recNum = noticeNum - (pageNum - 1) * this.pager.getPageSize();
            }

            this.pager.setTotalSize((long)var12);
            this.pager = this.pager.pagerFlip();
            if(pageNum > 0 && noticeNum <= var12) {
                this.pager.setPageNum(pageNum);
            } else if(this.pager.getPageNum() == 0) {
                this.pager.setPageNum(1);
            }

            if(var12 > 0 && recNum == 0) {
                recNum = 1;
            }

            this.setAnnounce(recNum);
            List result = this.sysAnnouncementService.queryCiUserReadInfoListByUserId(userId, this.sysAnnouncement, this.pager.getPageNum(), this.pager.getPageSize());
            this.pager.setResult(result);
            this.queryReadNotCount();
            this.queryAnnouncementCount();
            CILogServiceUtil.getLogServiceInstance().log("COC_SYS_ANNOUNCEMENT_SELECT", "", "", "系统公告列表查看成功", OperResultEnum.Success, LogLevelEnum.Medium);
            return "announcementShowList";
        } catch (Exception var11) {
            this.log.error(var11);
            var11.printStackTrace();
            CILogServiceUtil.getLogServiceInstance().log("COC_SYS_ANNOUNCEMENT_SELECT", "-1", "", "系统公告列表查看失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new CIServiceException(var11);
        }
    }

    private void queryAnnouncementCount() {
        CiSysAnnouncement sysAnnouncement = new CiSysAnnouncement();
        String userId = PrivilegeServiceUtil.getUserId();
        sysAnnouncement.setStatus(Integer.valueOf(1));
        int totalCount = this.sysAnnouncementService.queryCiUserReadInfoCountByUserId(userId, sysAnnouncement);
        this.pojo.put("totalCount", Integer.valueOf(totalCount));
    }

    public void delete() throws CIServiceException {
        boolean success = true;
        String msg = "";
        HashMap returnMsg = new HashMap();
        if(this.sysAnnouncement != null) {
            boolean response = this.sysAnnouncementService.batchDeleteSysAnnouncementList(this.sysAnnouncement.getAnnouncementIdList());
            if(response) {
                msg = "删除成功";
                CILogServiceUtil.getLogServiceInstance().log("COC_SYSTEM_ANNOUNCEMENT_DELETE", this.sysAnnouncement.getAnnouncementIdList().toString(), (String)null, "删除系统公告成功", OperResultEnum.Success, LogLevelEnum.Medium);
            } else {
                this.log.error("删除系统公告[id = " + this.sysAnnouncement.getAnnouncementId() + "]失败");
                msg = "删除系统公告[id = " + this.sysAnnouncement.getAnnouncementId() + "]失败";
                CILogServiceUtil.getLogServiceInstance().log("COC_SYSTEM_ANNOUNCEMENT_DELETE", this.sysAnnouncement.getAnnouncementIdList().toString(), (String)null, "删除系统公告失败", OperResultEnum.Success, LogLevelEnum.Medium);
            }
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

    private boolean isSysAnnouncementExists(String announcementName, String announcementId) throws CIServiceException {
        boolean isExists = false;

        try {
            CiSysAnnouncement e = new CiSysAnnouncement();
            e.setAnnouncementName(announcementName);
            List list = this.sysAnnouncementService.querySysAnnouncementListBySysAnnouncement(e);

            for(int i$ = 0; i$ < list.size(); ++i$) {
                if(((CiSysAnnouncement)list.get(i$)).getStatus().intValue() == 0) {
                    list.remove(i$);
                }
            }

            if(CollectionUtils.isNotEmpty(list)) {
                Iterator var9 = list.iterator();

                while(var9.hasNext()) {
                    CiSysAnnouncement object = (CiSysAnnouncement)var9.next();
                    if(!object.getAnnouncementId().equals(announcementId)) {
                        isExists = true;
                        break;
                    }
                }
            }

            return isExists;
        } catch (Exception var8) {
            throw new CIServiceException(var8);
        }
    }

    public void save() throws CIServiceException {
        boolean success = true;
        String msg = "";
        String errorType = "";
        HashMap returnMsg = new HashMap();
        if(this.sysAnnouncement != null) {
            if(this.isSysAnnouncementExists(this.sysAnnouncement.getAnnouncementName(), this.sysAnnouncement.getAnnouncementId())) {
                success = false;
                errorType = "1";
            } else {
                Timestamp response = this.sysAnnouncement.getEffectiveTime();
                if(response != null) {
                    this.log.debug("--------------------------系统有效时间timeTemp：" + response);
                }

                this.sysAnnouncement.setAnnouncementId((String)null);
                this.sysAnnouncement.setReleaseDate(new Timestamp((new Date()).getTime()));
                this.sysAnnouncement.setStatus(Integer.valueOf(1));
                boolean e = this.sysAnnouncementService.addSysAnnouncement(this.sysAnnouncement);
                if(e) {
                    msg = "保存成功";
                    CILogServiceUtil.getLogServiceInstance().log("COC_SYSTEM_ANNOUNCEMENT_ADD", this.sysAnnouncement.getAnnouncementId(), this.sysAnnouncement.getAnnouncementName(), "新增系统公告成功【公告名称:" + this.sysAnnouncement.getAnnouncementName() + ",公告ID:" + this.sysAnnouncement.getAnnouncementId() + "，公告下发时间:" + this.sysAnnouncement.getReleaseDate() + ",公告描述:" + this.sysAnnouncement.getAnnouncementDetail() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                } else {
                    errorType = "0";
                    this.log.error("保存系统公告[id = " + this.sysAnnouncement.getAnnouncementId() + "]失败");
                    msg = "保存系统公告[id = " + this.sysAnnouncement.getAnnouncementId() + "]失败";
                    CILogServiceUtil.getLogServiceInstance().log("COC_SYSTEM_ANNOUNCEMENT_ADD", "-1", "新增系统公告失败", "新增系统公告失败【公告名称:" + this.sysAnnouncement.getAnnouncementName() + ",公告下发时间:" + this.sysAnnouncement.getReleaseDate() + ",公告描述:" + this.sysAnnouncement.getAnnouncementDetail() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                }
            }
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("errorType", errorType);
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public void modify() throws CIServiceException {
        boolean success = true;
        String msg = "";
        String errorType = "";
        HashMap returnMsg = new HashMap();
        if(this.sysAnnouncement != null) {
            if(this.isSysAnnouncementExists(this.sysAnnouncement.getAnnouncementName(), this.sysAnnouncement.getAnnouncementId())) {
                success = false;
                errorType = "1";
            } else {
                this.sysAnnouncement.setReleaseDate(new Timestamp((new Date()).getTime()));
                this.sysAnnouncement.setStatus(Integer.valueOf(1));
                boolean response = this.sysAnnouncementService.modifySysAnnouncement(this.sysAnnouncement);
                if(response) {
                    msg = "修改成功";
                    CILogServiceUtil.getLogServiceInstance().log("COC_SYSTEM_ANNOUNCEMENT_UPDATE", this.sysAnnouncement.getAnnouncementId(), this.sysAnnouncement.getAnnouncementName(), "修改系统公告成功【公告名称:" + this.sysAnnouncement.getAnnouncementName() + ",公告ID:" + this.sysAnnouncement.getAnnouncementId() + "，公告下发时间:" + this.sysAnnouncement.getReleaseDate() + ",公告描述:" + this.sysAnnouncement.getAnnouncementDetail() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                } else {
                    errorType = "0";
                    this.log.error("保存系统公告[id = " + this.sysAnnouncement.getAnnouncementId() + "]失败");
                    msg = "保存系统公告[id = " + this.sysAnnouncement.getAnnouncementId() + "]失败";
                    CILogServiceUtil.getLogServiceInstance().log("COC_SYSTEM_ANNOUNCEMENT_UPDATE", this.sysAnnouncement.getAnnouncementId(), this.sysAnnouncement.getAnnouncementName(), "修改系统公告失败【公告名称:" + this.sysAnnouncement.getAnnouncementName() + ",公告ID:" + this.sysAnnouncement.getAnnouncementId() + "，公告下发时间:" + this.sysAnnouncement.getReleaseDate() + ",公告描述:" + this.sysAnnouncement.getAnnouncementDetail() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                }
            }
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("errorType", errorType);
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public String toEdit() throws CIServiceException {
        if(null != this.sysAnnouncement) {
            this.sysAnnouncement = this.sysAnnouncementService.selectCiSysAnnouncementById(this.sysAnnouncement.getAnnouncementId());
        }

        CacheBase cache = CacheBase.getInstance();
        this.dimAnnouncementTypeList = (List<DimAnnouncementType>)cache.getObjectList("DIM_ANNOUNCEMENT_TYPE");
        return "editSysAnnouncement";
    }

    public String updateReadStatus() throws CIServiceException {
        boolean result = false;
        HashMap returnMsg = new HashMap();

        try {
            CiUserReadInfo response = new CiUserReadInfo();
            CiUserReadInfoId userId = new CiUserReadInfoId();
            String e = this.getUserId();
            userId.setUserId(e);
            userId.setAnnouncementId(this.sysAnnouncement.getAnnouncementId());
            response.setId(userId);
            response.setStatus(Integer.valueOf(1));
            this.ciUserReadInfoService.addCiUserReadInfo(response);
            this.queryReadNotCount();
            result = true;
        } catch (Exception var7) {
            this.log.error("更新系统公告已读状态失败", var7);
            throw new CIServiceException(var7);
        }

        HttpServletResponse response1 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(result));
        returnMsg.put("readStatusNot", this.pojo.get("readStatusNot").toString());
        String userId1 = PrivilegeServiceUtil.getUserId();
        CacheBase.getInstance().removeNotice(userId1, this.sysAnnouncement.getAnnouncementId(), 1);

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var6) {
            this.log.error(var6.getMessage(), var6);
            var6.printStackTrace();
        }

        return null;
    }

    public void updateAllReadStatus() throws CIServiceException {
        boolean success = false;
        String msg = "";
        HashMap returnMsg = new HashMap();

        try {
            String response = this.pojo.get("announcementIds").toString();
            String[] e = response.trim().split(",");
            if(e != null && e.length > 0) {
                for(int i = 0; i < e.length; ++i) {
                    CiUserReadInfo ciUserReadInfo = new CiUserReadInfo();
                    CiUserReadInfoId id = new CiUserReadInfoId();
                    String userId = this.getUserId();
                    id.setUserId(userId);
                    id.setAnnouncementId(e[i]);
                    ciUserReadInfo.setId(id);
                    ciUserReadInfo.setStatus(Integer.valueOf(1));
                    this.ciUserReadInfoService.addCiUserReadInfo(ciUserReadInfo);
                    CacheBase.getInstance().removeNotice(userId, e[i], 1);
                }
            }

            this.queryReadNotCount();
            success = true;
            msg = "标记已读成功";
        } catch (Exception var11) {
            success = false;
            msg = "标记已读失败";
            this.log.error("系统公告标记已读失败", var11);
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse var12 = this.getResponse();

        try {
            this.sendJson(var12, JsonUtil.toJson(returnMsg));
        } catch (Exception var10) {
            this.log.error("发送json串异常", var10);
            throw new CIServiceException(var10);
        }
    }

    private void queryReadNotCount() {
        CiSysAnnouncement sysAnnouncement = new CiSysAnnouncement();
        String userId = PrivilegeServiceUtil.getUserId();
        sysAnnouncement.setStatus(Integer.valueOf(1));
        sysAnnouncement.setReadStatus(Integer.valueOf(0));
        int readStatusNot = this.sysAnnouncementService.queryCiUserReadInfoCountByUserId(userId, sysAnnouncement);
        this.pojo.put("readStatusNot", Integer.valueOf(readStatusNot));
    }

    public void del() throws Exception {
        boolean success = false;
        String msg = "";
        HashMap returnMsg = new HashMap();
        ArrayList announcementIdList = new ArrayList();

        try {
            String response = this.pojo.get("announcementIds").toString();
            String[] e = response.trim().split(",");
            if(e != null && e.length > 0) {
                for(int userId = 0; userId < e.length; ++userId) {
                    announcementIdList.add(e[userId]);
                }
            }

            String var11 = this.getUserId();
            this.ciUserReadInfoService.delete(announcementIdList, var11);
            success = true;
            msg = "删除成功";
            CacheBase.getInstance().deleteNoticeUserId(var11);
        } catch (Exception var9) {
            success = false;
            msg = "删除失败";
            this.log.error("删除系统公告失败", var9);
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse var10 = this.getResponse();

        try {
            this.sendJson(var10, JsonUtil.toJson(returnMsg));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public String updateNotReadStatus() throws CIServiceException {
        boolean result = false;
        HashMap returnMsg = new HashMap();

        try {
            CiUserReadInfo response = new CiUserReadInfo();
            CiUserReadInfoId userId = new CiUserReadInfoId();
            String e = this.getUserId();
            userId.setUserId(e);
            userId.setAnnouncementId(this.sysAnnouncement.getAnnouncementId());
            response.setId(userId);
            response.setStatus(Integer.valueOf(2));
            this.ciUserReadInfoService.addCiUserReadInfo(response);
            this.queryReadNotCount();
            result = true;
        } catch (Exception var7) {
            this.log.error("更新系统公告未读状态失败", var7);
            throw new CIServiceException(var7);
        }

        HttpServletResponse response1 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(result));
        returnMsg.put("readStatusNot", this.pojo.get("readStatusNot").toString());
        String userId1 = PrivilegeServiceUtil.getUserId();
        CacheBase.getInstance().closeNotice(userId1, this.sysAnnouncement.getAnnouncementId());

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var6) {
            this.log.error(var6.getMessage(), var6);
            var6.printStackTrace();
        }

        return null;
    }

    public int getAnnounce() {
        return this.announce;
    }

    public void setAnnounce(int announce) {
        this.announce = announce;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public CiSysAnnouncement getSysAnnouncement() {
        return this.sysAnnouncement;
    }

    public void setSysAnnouncement(CiSysAnnouncement sysAnnouncement) {
        this.sysAnnouncement = sysAnnouncement;
    }

    public Pager getSyspager() {
        return this.syspager;
    }

    public void setSyspager(Pager syspager) {
        this.syspager = syspager;
    }

    public List<DimAnnouncementType> getDimAnnouncementTypeList() {
        return this.dimAnnouncementTypeList;
    }

    public void setDimAnnouncementTypeList(List<DimAnnouncementType> dimAnnouncementTypeList) {
        this.dimAnnouncementTypeList = dimAnnouncementTypeList;
    }

    public HashMap<String, Object> getPojo() {
        return this.pojo;
    }

    public void setPojo(HashMap<String, Object> pojo) {
        this.pojo = pojo;
    }
}

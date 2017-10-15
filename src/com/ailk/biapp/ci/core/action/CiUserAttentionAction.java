package com.ailk.biapp.ci.core.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiUserAttentionLabel;
import com.ailk.biapp.ci.entity.CiUserAttentionLabelId;
import com.ailk.biapp.ci.entity.DimApproveStatus;
import com.ailk.biapp.ci.entity.DimLabelDataStatus;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiUserAttentionAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiUserAttentionAction.class);
    private Pager pager;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private List<CiCustomGroupInfo> ciCustomGroupInfoList;
    private Integer havePushFun;
    private CiLabelInfo ciLabelInfo;
    private String searchType;
    private List<LabelDetailInfo> labelDetailList;
    private List<DimLabelDataStatus> dimLabelDataStatusList;
    private List<DimApproveStatus> dimApproveStatusList;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;

    public CiUserAttentionAction() {
    }

    public void addUserAttentionCustom() {
        String customGroupId = this.getRequest().getParameter("customGroupId");
        CiCustomGroupInfo ciCustomGroupInfoNow = this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        String userId = PrivilegeServiceUtil.getUserId();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "用户群收藏成功";
        if(0 != ciCustomGroupInfoNow.getStatus().intValue() && 2 != ciCustomGroupInfoNow.getDataStatus().intValue()) {
            if(StringUtil.isNotEmpty(customGroupId) && StringUtil.isNotEmpty(userId)) {
                try {
                    this.customersService.userAttentionCustom(customGroupId, userId);
                } catch (Exception var10) {
                    success = false;
                    message = "用户群收藏失败";
                    this.log.error("收藏用户群失败", var10);
                    throw new CIServiceException("收藏用户群失败");
                }
            } else {
                success = false;
                message = "参数有误";
            }
        } else {
            success = false;
            message = "不能收藏已删除或者创建中的用户群！";
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

    public void delUserAttentionCustom() {
        String customGroupId = this.getRequest().getParameter("customGroupId");
        CiCustomGroupInfo ciCustomGroupInfoNow = this.ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        String userId = PrivilegeServiceUtil.getUserId();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "取消用户群收藏成功";
        if(0 != ciCustomGroupInfoNow.getStatus().intValue() && 2 != ciCustomGroupInfoNow.getDataStatus().intValue()) {
            if(StringUtil.isNotEmpty(customGroupId) && StringUtil.isNotEmpty(userId)) {
                try {
                    this.customersService.deleteUserAttentionCustom(customGroupId, userId);
                } catch (Exception var10) {
                    success = false;
                    message = "取消用户群收藏失败";
                    this.log.error(message, var10);
                    throw new CIServiceException(message, var10);
                }
            }
        } else {
            success = false;
            message = "不能取消对已删除或者创建中的用户群的收藏！";
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

    public String userAttentionIndex() {
        return "userAttentionIndex";
    }

    public String findUserAttentionCustom() {
        String msg;
        try {
            boolean e = false;
            if(this.ciCustomGroupInfo == null) {
                this.ciCustomGroupInfo = new CiCustomGroupInfo();
            }

            if(StringUtils.isBlank(this.ciCustomGroupInfo.getDataDate())) {
                this.newLabelMonth = this.getNewLabelMonth();
            }

            msg = this.getUserId();
            String createCityId = PrivilegeServiceUtil.getCityIdFromSession();
            String root = Configure.getInstance().getProperty("CENTER_CITYID");
            if(!PrivilegeServiceUtil.isAdminUser(msg)) {
                this.ciCustomGroupInfo.setCreateUserId(msg);
                this.ciCustomGroupInfo.setCreateCityId(createCityId);
            } else if(!createCityId.equals(root)) {
                this.ciCustomGroupInfo.setCreateCityId(createCityId);
            }

            if(this.pager == null) {
                this.pager = new Pager();
            }

            Pager p2 = new Pager(this.customersService.getUserAttentionCustomserCount(this.ciCustomGroupInfo).longValue());
            this.pager.setTotalPage(p2.getTotalPage());
            this.pager.setTotalSize(p2.getTotalSize());
            this.pager = this.pager.pagerFlip();
            int currentPage = this.pager.getPageNum();
            int e1 = this.pager.getTotalPage();
            if(currentPage <= e1) {
                this.ciCustomGroupInfoList = this.customersService.queryUserAttentionCustomersList(this.pager, this.newLabelMonth, this.ciCustomGroupInfo);
                if(this.ciCustomGroupInfoList != null) {
                    Iterator ciSysInfoRightList = this.ciCustomGroupInfoList.iterator();

                    label83:
                    while(true) {
                        CiCustomGroupInfo info;
                        do {
                            do {
                                if(!ciSysInfoRightList.hasNext()) {
                                    break label83;
                                }

                                info = (CiCustomGroupInfo)ciSysInfoRightList.next();
                                String offsetStr = this.customersService.queryCustomerOffsetStr(info);
                                info.setKpiDiffRule((info.getKpiDiffRule() == null?"":info.getKpiDiffRule()) + offsetStr);
                                if(info.getMonthLabelDate() != null && !info.getMonthLabelDate().equals("")) {
                                    info.setMonthLabelDate(DateUtil.string2StringFormat(info.getMonthLabelDate(), "yyyyMM", "yyyy-MM"));
                                }

                                if(info.getDayLabelDate() != null && !info.getDayLabelDate().equals("")) {
                                    info.setDayLabelDate(DateUtil.string2StringFormat(info.getDayLabelDate(), "yyyyMMdd", "yyyy-MM-dd"));
                                }
                            } while(info.getMonthLabelDate() != null && !info.getMonthLabelDate().equals(""));
                        } while(info.getDayLabelDate() != null && !info.getDayLabelDate().equals(""));

                        if(info.getDataDate() == null) {
                            info.setDataDateStr("");
                        } else if(info.getDataDate().length() > 6) {
                            info.setDataDateStr(DateUtil.string2StringFormat(info.getDataDate(), "yyyyMMdd", "yyyy-MM-dd"));
                        } else {
                            info.setDataDateStr(DateUtil.string2StringFormat(info.getDataDate(), "yyyyMM", "yyyy-MM"));
                        }
                    }
                }
            }

            List ciSysInfoRightList1 = this.customersService.queryCiSysInfo(1);
            if(ciSysInfoRightList1 != null && ciSysInfoRightList1.size() > 0) {
                this.havePushFun = Integer.valueOf(1);
            } else {
                this.havePushFun = Integer.valueOf(0);
            }

            return "userAttentionCustom";
        } catch (Exception var10) {
            msg = "用户关注标签列表查询发生异常";
            this.log.error(msg, var10);
            throw new CIServiceException(var10);
        }
    }

    public String findUserAttentionLabel() {
        String dataScope = this.getRequest().getParameter("dataScope");
        if(StringUtil.isEmpty(dataScope)) {
            dataScope = "all";
        }

        this.searchType = "userAttentionSearch";
        this.log.debug("dataScope->" + dataScope);

        try {
            String e = PrivilegeServiceUtil.getUserId();
            long msg1 = this.ciLabelInfoService.queryUserAttentionLabelNum(e, dataScope);
            if(this.pager == null) {
                this.pager = new Pager();
            }

            Pager p2 = new Pager(msg1);
            this.pager.setTotalPage(p2.getTotalPage());
            this.pager.setTotalSize(p2.getTotalSize());
            this.pager = this.pager.pagerFlip();
            String orderType = "date";
            int totalPage = this.pager.getTotalPage();
            int currentPage = this.pager.getPageNum();
            if(currentPage <= totalPage) {
                this.labelDetailList = this.ciLabelInfoService.queryUserAttentionLabel(currentPage, this.pager.getPageSize(), e, orderType, dataScope);
            }

            return "userAttentionLabel";
        } catch (Exception var9) {
            String msg = "用户关注标签列表查询发生异常";
            this.log.error(msg, var9);
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

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public List<CiCustomGroupInfo> getCiCustomGroupInfoList() {
        return this.ciCustomGroupInfoList;
    }

    public void setCiCustomGroupInfoList(List<CiCustomGroupInfo> ciCustomGroupInfoList) {
        this.ciCustomGroupInfoList = ciCustomGroupInfoList;
    }

    public Integer getHavePushFun() {
        return this.havePushFun;
    }

    public void setHavePushFun(Integer havePushFun) {
        this.havePushFun = havePushFun;
    }

    public String getSearchType() {
        return this.searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public List<LabelDetailInfo> getLabelDetailList() {
        return this.labelDetailList;
    }

    public void setLabelDetailList(List<LabelDetailInfo> labelDetailList) {
        this.labelDetailList = labelDetailList;
    }

    public List<DimLabelDataStatus> getDimLabelDataStatusList() {
        return this.dimLabelDataStatusList;
    }

    public void setDimLabelDataStatusList(List<DimLabelDataStatus> dimLabelDataStatusList) {
        this.dimLabelDataStatusList = dimLabelDataStatusList;
    }

    public List<DimApproveStatus> getDimApproveStatusList() {
        return this.dimApproveStatusList;
    }

    public void setDimApproveStatusList(List<DimApproveStatus> dimApproveStatusList) {
        this.dimApproveStatusList = dimApproveStatusList;
    }

    public CiLabelInfo getCiLabelInfo() {
        return this.ciLabelInfo;
    }

    public void setCiLabelInfo(CiLabelInfo ciLabelInfo) {
        this.ciLabelInfo = ciLabelInfo;
    }
}

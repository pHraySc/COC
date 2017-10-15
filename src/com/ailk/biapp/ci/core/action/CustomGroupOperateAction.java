package com.ailk.biapp.ci.core.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiCustomFileRel;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiCustomGroupPushCycle;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiCustomPushReq;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiSysInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.market.entity.CiCustomLabelSceneRel;
import com.ailk.biapp.ci.market.entity.CiMarketScene;
import com.ailk.biapp.ci.market.entity.CiMarketTask;
import com.ailk.biapp.ci.market.service.ICiMarketService;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiCustomFileRelService;
import com.ailk.biapp.ci.service.ICiGroupAttrRelService;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICustomersAnalysisService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CustomGroupOperateAction extends CIBaseAction {
    private static Logger log = Logger.getLogger(CustomGroupOperateAction.class);
    private int page;
    private int rows;
    private Pager pager;
    private String isPush;
    private String pushCycle;
    private CiCustomFileRel customFile;
    private Integer havePushFun;
    private CiCustomGroupInfo customGroupInfo;
    private CiCustomPushReq ciCustomPushReq;
    private List<String> sysIds;
    private String listTableName;
    private String dataDate;
    private List<LabelTrendInfo> trendAnalysisList;
    private Map<String, Object> resultMap;
    private List<CiMarketTask> firstLevelTasks;
    private List<CiMarketScene> firstLevelScenes;
    private String taskId;
    private String sceneId;
    private CiCustomLabelSceneRel customLabelSceneRel;
    private List<CiSysInfo> sysInfoCycleList;
    private List<CiSysInfo> ciSysInfoList;
    @Autowired
    private ICiCustomFileRelService loadCustomGroupFileService;
    @Autowired
    private ICustomersAnalysisService customersAnalysisService;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICiMarketService ciMarketService;
    @Autowired
    private ICiGroupAttrRelService ciGroupAttrRelService;

    public CustomGroupOperateAction() {
    }

    public String initCustomersTrendDetail() {
        String customGroupId = this.getRequest().getParameter("customGroupId");
        boolean success = false;
        String msg = "";

        try {
            this.customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
            String e = this.customersService.queryCustomerOffsetStr(this.customGroupInfo);
            this.customGroupInfo.setKpiDiffRule((this.customGroupInfo.getKpiDiffRule() == null?"":this.customGroupInfo.getKpiDiffRule()) + e);
            success = true;
            Date today = new Date();
            String month = DateUtil.date2String(today, "yyyyMM");
            this.dataDate = DateUtil.getFrontMonth(1, month);
            if(success && 0 == this.customGroupInfo.getStatus().intValue()) {
                success = false;
                msg = "该用户群已被删除！";
            }
        } catch (Exception var7) {
            success = false;
            msg = "用户群清单趋势查询出错";
            log.error(msg, var7);
            throw new CIServiceException(var7);
        }

        this.resultMap = new HashMap();
        this.resultMap.put("success", Boolean.valueOf(success));
        this.resultMap.put("msg", msg);
        return "customersTrendDetail";
    }

    public String findCustomerTrendTable() {
        List ciSysInfoRightList = this.customersService.queryCiSysInfo(1);
        if(ciSysInfoRightList != null && ciSysInfoRightList.size() > 0) {
            this.havePushFun = Integer.valueOf(1);
        } else {
            this.havePushFun = Integer.valueOf(0);
        }

        Object ciSysInfoList = this.customersService.queryCiSysInfo(1);
        String province = Configure.getInstance().getProperty("PROVINCE");
        String customGroupId;
        if("zhejiang".equals(province)) {
            customGroupId = Configure.getInstance().getProperty("SYS_FLAG");
            String ciCustomGroupInfo = (String)this.getSession().getAttribute("SYS_FLAG");
            ArrayList cycle = new ArrayList();
            if(customGroupId.equals(ciCustomGroupInfo)) {
                Iterator currentPageSize = ((List)ciSysInfoList).iterator();

                while(currentPageSize.hasNext()) {
                    CiSysInfo size = (CiSysInfo)currentPageSize.next();
                    String i = size.getSysId();
                    if(i.contains(customGroupId)) {
                        cycle.add(size);
                    }
                }

                ciSysInfoList = cycle;
            }
        }

        this.getSession().setAttribute("ciSysInfoList", ciSysInfoList);
        customGroupId = this.getRequest().getParameter("customGroupId");
        CiCustomGroupInfo var11 = this.customersService.queryCiCustomGroupInfo(customGroupId);
        int var12 = var11.getUpdateCycle().intValue();
        if(this.pager == null) {
            this.pager = new Pager();
        }

        byte var13 = 6;
        this.pager.setPageSize(var13);
        if(this.pager.getTotalSize() == 0L) {
            this.pager.setTotalSize((long)this.customersAnalysisService.selectCustomersTotalByTrendAnalysisCount(customGroupId, var11.getDataDate(), var12));
        }

        this.pager = this.pager.pagerFlip();
        this.trendAnalysisList = this.customersAnalysisService.queryTrendAnalysis(customGroupId, var11.getDataDate(), var12, this.pager.getPageNum(), this.pager.getPageSize());
        int var14;
        if(var12 == 3) {
            if(this.pager.getTotalSize() > 8L) {
                this.pager.setTotalSize(8L);
            }

            if(this.pager.getPageNum() > 1 && this.trendAnalysisList.size() > 0) {
                var14 = 2;
                if(this.trendAnalysisList.size() < var14) {
                    var14 = this.trendAnalysisList.size();
                }

                this.trendAnalysisList = this.trendAnalysisList.subList(0, var14);
            }
        }

        var14 = var13 - this.trendAnalysisList.size();
        if(var14 < var13) {
            for(int var15 = 0; var15 < var14; ++var15) {
                LabelTrendInfo l = new LabelTrendInfo();
                this.trendAnalysisList.add(l);
            }
        }

        this.pager.setResult(this.trendAnalysisList);
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_TREND_LIST_VIEW", var11.getCustomGroupId(), var11.getCustomGroupName(), "首页群乐享-用户群趋势表格查看【用户群ID：" + var11.getCustomGroupId() + "名称：" + var11.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customersTrendTable";
    }

    public String customersTrendChartData() {
        String customGroupId = this.getRequest().getParameter("customGroupId");
        CiCustomGroupInfo ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        int cycle = ciCustomGroupInfo.getUpdateCycle().intValue();
        this.trendAnalysisList = this.customersAnalysisService.queryTrendAnalysis(customGroupId, ciCustomGroupInfo.getDataDate(), cycle);
        this.customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        if(this.customGroupInfo.getUpdateCycle().intValue() == 3 && this.trendAnalysisList.size() > 8) {
            Collections.reverse(this.trendAnalysisList);
            this.trendAnalysisList = this.trendAnalysisList.subList(0, 8);
            Collections.reverse(this.trendAnalysisList);
        }

        String customGroupName = "";
        if(this.customGroupInfo != null) {
            customGroupName = this.customGroupInfo.getCustomGroupName();
        }

        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_TREND_LIST_VIEW", customGroupId, customGroupName, "查询用户群用户数趋势数据成功【用户群ID:" + customGroupId + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customersTrendChartData";
    }

    public String recomLabelOrCustomPage() {
        this.firstLevelTasks = this.ciMarketService.findFirstLevelTasks();
        this.firstLevelScenes = this.ciMarketService.findFirstLevelScenes();
        return "recomCustomOrLabel";
    }

    public void findSecondTasks() throws Exception {
        HashMap result = new HashMap();
        boolean success = false;

        try {
            List response = this.ciMarketService.findSecondTasksByTaskId(this.taskId);
            result.put("secondLevelTasks", response);
            success = true;
        } catch (Exception var6) {
            log.error("根据一级营销任务查询二级营销任务出错", var6);
            success = false;
        }

        result.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var5) {
            log.error("发送json串异常", var5);
            throw new CIServiceException(var5);
        }
    }

    public void findSecondScenes() throws Exception {
        HashMap result = new HashMap();
        boolean success = false;

        try {
            List response = this.ciMarketService.findSecondScenesBySceneId(this.sceneId);
            result.put("secondLevelScenes", response);
            success = true;
        } catch (Exception var6) {
            success = false;
            log.error("根据一级营销任务查询二级营销任务出错", var6);
            throw new CIServiceException(var6);
        }

        result.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var5) {
            log.error("发送json串异常", var5);
            throw new CIServiceException(var5);
        }
    }

    public void saveCustomOrLabelSceneRel() throws Exception {
        HashMap result = new HashMap();
        boolean success = false;
        String msg = "";

        try {
            Integer response = this.customLabelSceneRel.getShowType();
            String e = this.customLabelSceneRel.getCustomGroupId();
            Integer labelId = this.customLabelSceneRel.getLabelId();
            String labelCustomName = "";
            Double avgScore = Double.valueOf(0.0D);
            String newestTime = "";
            success = this.ciMarketService.isRepeatScene(this.customLabelSceneRel);
            msg = "不能重复设置该场景分类，请选择其他场景设置";
            if(success) {
                if(StringUtil.isNotEmpty(response) && response.intValue() == ServiceConstants.RECOM_SHOW_TYPE_LABEL && StringUtil.isNotEmpty(labelId)) {
                    CacheBase ciCustomGroupInfo1 = CacheBase.getInstance();
                    CiLabelInfo ciLabelInfo = ciCustomGroupInfo1.getEffectiveLabel(labelId + "");
                    if(ciLabelInfo != null) {
                        labelCustomName = ciLabelInfo.getLabelName();
                        avgScore = ciLabelInfo.getAvgScore();
                        if(ciLabelInfo.getDataDate().length() > 6) {
                            newestTime = DateUtil.string2StringFormat(ciLabelInfo.getDataDate(), "yyyyMMdd", "yyyy-MM-dd");
                        } else {
                            newestTime = DateUtil.string2StringFormat(ciLabelInfo.getDataDate(), "yyyyMM", "yyyy-MM");
                        }

                        ciLabelInfo.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_SYS_RECOM));
                        this.ciLabelInfoService.modify(ciLabelInfo);
                        ciCustomGroupInfo1.modifyEffectiveLabel(ciLabelInfo.getLabelId() + "", ciLabelInfo);
                        success = true;
                    }
                } else if(StringUtil.isNotEmpty(response) && response.intValue() == ServiceConstants.RECOM_SHOW_TYPE_CUSTOM && StringUtil.isNotEmpty(e)) {
                    CiCustomGroupInfo ciCustomGroupInfo = null;
                    if(StringUtil.isNotEmpty(e)) {
                        ciCustomGroupInfo = this.customersService.queryCiCustomGroupInfo(e);
                    }

                    if(ciCustomGroupInfo != null) {
                        labelCustomName = ciCustomGroupInfo.getCustomGroupName();
                        avgScore = ciCustomGroupInfo.getAvgScore();
                        newestTime = DateUtil.date2String(ciCustomGroupInfo.getNewModifyTime(), "yyyy-MM-dd HH:mm:ss");
                        ciCustomGroupInfo.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_SYS_RECOM));
                        success = this.customersService.modifyCiCustomGroupInfo(ciCustomGroupInfo, this.getUserId());
                    }
                }

                if(success) {
                    if(this.customLabelSceneRel.getAvgScore() == null) {
                        this.customLabelSceneRel.setAvgScore(Double.valueOf(ServiceConstants.AVG_SCORE));
                    } else {
                        this.customLabelSceneRel.setAvgScore(avgScore);
                    }

                    this.customLabelSceneRel.setLabelCustomName(labelCustomName);
                    this.customLabelSceneRel.setNewestTime(newestTime);
                    this.customLabelSceneRel.setSortNum(Integer.valueOf(ServiceConstants.SORT_NUM));
                    this.customLabelSceneRel.setStatus(Integer.valueOf(ServiceConstants.SCENE_REL_STATUS_VALIDATE));
                    this.ciMarketService.saveCustomOrLabelSceneRel(this.customLabelSceneRel);
                    msg = "推荐设置成功";
                }
            }
        } catch (Exception var13) {
            success = false;
            msg = "推荐设置失败";
            log.error("推荐设置失败", var13);
            throw new CIServiceException(var13);
        }

        HttpServletResponse response1 = this.getResponse();
        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var12) {
            log.error("发送json串异常", var12);
            throw new CIServiceException(var12);
        }
    }

    public void shareCustom() throws Exception {
        Map returnMap = null;
        String message = "共享用户群失败";
        boolean success = false;

        try {
            String response = this.customGroupInfo.getCreateCityId();
            String e = this.getUserId();
            returnMap = this.customersService.isShareNameExist(this.customGroupInfo, e, response);
            String ciCustomGroupInfoId = this.customGroupInfo.getCustomGroupId();
            CiCustomGroupInfo info = this.customersService.queryCiCustomGroupInfo(ciCustomGroupInfoId);
            if(((Boolean)returnMap.get("success")).booleanValue()) {
                if(0 != info.getStatus().intValue() && 2 != info.getDataStatus().intValue()) {
                    info.setIsPrivate(Integer.valueOf(0));
                    info.setCustomGroupName(this.customGroupInfo.getCustomGroupName());
                    info.setNewModifyTime(new Date());
                    this.customersService.modifyCiCustomGroupInfo(info, e);
                    success = true;
                } else {
                    success = false;
                    message = "不能分享已删除或者创建中的用户群！";
                }
            }
        } catch (Exception var9) {
            log.error(message + " : " + this.customGroupInfo.getCustomGroupName() + " 失败", var9);
            returnMap.put("msg", message);
        }

        returnMap.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var8) {
            log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public void cancelShareCustom() throws Exception {
        Map returnMap = null;
        String message = "取消共享用户群失败";
        boolean success = false;

        try {
            String response = this.customGroupInfo.getCustomGroupId();
            returnMap = this.customersService.isUsingCustom(response);
            CiCustomGroupInfo e = this.customersService.queryCiCustomGroupInfo(response);
            if(((Boolean)returnMap.get("success")).booleanValue()) {
                if(0 != e.getStatus().intValue() && 2 != e.getDataStatus().intValue()) {
                    e.setIsPrivate(Integer.valueOf(1));
                    e.setNewModifyTime(new Date());
                    this.customersService.modifyCiCustomGroupInfo(e, this.userId);
                    success = true;
                } else {
                    success = false;
                    message = "不能取消分享已删除或者创建中的用户群！";
                }
            }
        } catch (Exception var7) {
            log.error(message + " : " + this.customGroupInfo.getCustomGroupName() + " 失败", var7);
            returnMap.put("msg", message);
        }

        returnMap.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var6) {
            log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public String findGroupList() throws Exception {
        boolean success = false;
        String msg = "";
        CiCustomGroupInfo cg = null;

        try {
            cg = this.customersService.queryCiCustomGroupInfo(this.customGroupInfo.getCustomGroupId());
            success = true;
            CiCustomListInfo e = this.customersService.queryCustomListInfo(this.customGroupInfo);
            if(e == null) {
                success = false;
                msg = "该用户群无清单信息";
            } else {
                if(success && (0 == cg.getStatus().intValue() || 2 == cg.getDataStatus().intValue())) {
                    success = false;
                    msg = "已删除的用户群不能查看清单！";
                }

                if(success) {
                    CiCustomListInfo listInfo = this.customersService.queryCustomListInfo(this.customGroupInfo);
                    if(null != listInfo) {
                        List groupAttrRelList = this.ciGroupAttrRelService.queryGroupAttrRelList(listInfo.getCustomGroupId(), listInfo.getDataTime());
                        Object attrRelList = new ArrayList();
                        if(groupAttrRelList == null || groupAttrRelList.size() != 1 || groupAttrRelList.get(0) == null || ((CiGroupAttrRel)groupAttrRelList.get(0)).getStatus() == null || ((CiGroupAttrRel)groupAttrRelList.get(0)).getStatus().intValue() != 2) {
                            attrRelList = groupAttrRelList;
                        }

                        this.getRequest().setAttribute("groupAttrRelList", attrRelList);
                    }
                }
            }
        } catch (Exception var8) {
            success = false;
            msg = "用户群清单表头查询出错";
            log.error(msg, var8);
            throw new CIServiceException(var8);
        }

        this.resultMap = new HashMap();
        this.resultMap.put("success", Boolean.valueOf(success));
        this.resultMap.put("msg", msg);
        return "queryGroupList";
    }

    public void findGroupInfoList() throws Exception {
        boolean success = false;
        String msg = "";
        List list = null;
        HashMap result = new HashMap();

        try {
            if(this.pager == null) {
                this.pager = new Pager();
            }

            CiCustomListInfo response = this.customersService.queryCustomListInfo(this.customGroupInfo);
            if(response != null && response.getDataStatus().intValue() == 3) {
                this.pager.setPageNum(this.page);
                this.pager.setPageSize(this.rows);
                this.pager.setTotalSize(response.getCustomNum().longValue());
                list = this.customersService.queryCustomerPhoneNumList(this.pager.getPageNum(), this.pager.getPageSize(), this.customGroupInfo);
                success = true;
            } else {
                this.pager.setTotalSize(0L);
            }
        } catch (Exception var8) {
            msg = "查询用户群清单数据错误";
            log.error(msg, var8);
            success = false;
        }

        if(success) {
            result.put("total", Long.valueOf(this.pager.getTotalSize()));
            result.put("rows", list);
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var7) {
            log.error("发送json串异常", var7);
        }

    }

    public String prePushCustomerSingle() throws Exception {
        String msg = "";
        boolean success = false;
        CiCustomGroupInfo cg = null;
        List pushedCycle = null;

        try {
            cg = this.customersService.queryCiCustomGroupInfo(this.customGroupInfo.getCustomGroupId());
            pushedCycle = this.customersService.queryPushCycleByGroupId(this.customGroupInfo.getCustomGroupId());
            if(cg.getUpdateCycle().intValue() != 1 && pushedCycle != null && pushedCycle.size() > 0) {
                this.isPush = "1";
            } else {
                this.isPush = "0";
            }

            if(cg.getUpdateCycle().intValue() == 2 || cg.getUpdateCycle().intValue() == 3) {
                this.sysInfoCycleList = this.customersService.queryCiSysInfo(cg);
            }

            success = true;
            this.ciSysInfoList = this.customersService.queryCiSysInfo(1);
            if(success && 0 == cg.getStatus().intValue()) {
                success = false;
                msg = "不能推送已删除的用户群！";
            }
        } catch (Exception var6) {
            log.error("用户群初始化页面失败，customGroupId = " + this.customGroupInfo.getCustomGroupId(), var6);
            msg = var6.getMessage();
            success = false;
            throw new CIServiceException(msg, var6);
        }

        this.resultMap = new HashMap();
        this.resultMap.put("success", Boolean.valueOf(success));
        this.resultMap.put("msg", msg);
        if(success) {
            this.customGroupInfo = cg;
        }

        return "pushCustomerGroupSingleInit";
    }

    public void pushCustomerGroupSingle() throws Exception {
        boolean success = true;
        String msg = "";
        HashMap result = new HashMap();
        if(success) {
            try {
                this.ciCustomPushReq = this.genCiCustomPushReq(this.customGroupInfo.getCustomGroupId());
                this.customersService.pushCustomersAfterSave(this.ciCustomPushReq, this.pushCycle, this.customGroupInfo);
                msg = "推送设置成功！";
                success = true;
                if(this.sysIds == null && (String.valueOf(2).equals(this.pushCycle) || String.valueOf(3).equals(this.pushCycle))) {
                    msg = "取消推送设置成功！";
                }

                if(this.sysIds == null && this.customGroupInfo.getUpdateCycle().intValue() == 1) {
                    success = false;
                    msg = "请选择推送平台！";
                }

                if(this.sysIds == null && (this.customGroupInfo.getUpdateCycle().intValue() == 2 || this.customGroupInfo.getUpdateCycle().intValue() == 3) && !"1".equals(this.isPush)) {
                    success = false;
                    msg = "请选择推送平台！";
                }

                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_PUSH_SET", this.customGroupInfo.getCustomGroupId(), this.customGroupInfo.getCustomGroupName(), "用户群推送设置", OperResultEnum.Success, LogLevelEnum.Risk);
            } catch (Exception var7) {
                msg = "推送设置失败，请联系管理员！";
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_PUSH_SET", this.customGroupInfo.getCustomGroupId(), this.customGroupInfo.getCustomGroupName(), "用户群推送设置失败", OperResultEnum.Failure, LogLevelEnum.Risk);
                success = false;
            }
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void findPushedCycle() throws Exception {
        String msg = "";
        boolean success = false;
        HashMap result = new HashMap();
        List pushedCycle = null;
        if(this.customGroupInfo == null) {
            msg = "接收参数失败！";
            success = false;
        }

        try {
            pushedCycle = this.customersService.queryPushCycleByGroupId(this.customGroupInfo.getCustomGroupId());
            if(pushedCycle != null && pushedCycle.size() > 0) {
                StringBuffer response = new StringBuffer();
                Iterator e = pushedCycle.iterator();

                while(e.hasNext()) {
                    CiCustomGroupPushCycle item = (CiCustomGroupPushCycle)e.next();
                    response.append(item.getId().getSysId()).append(",");
                }

                msg = response.substring(0, response.length() - 1).toString();
            }

            success = true;
        } catch (Exception var9) {
            log.error("查询已推送的平台信息失败，customGroupId = " + this.customGroupInfo.getCustomGroupId(), var9);
            msg = var9.getMessage();
            success = false;
            throw new CIServiceException(msg, var9);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var8) {
            log.error("发送json串异常", var8);
            throw new CIServiceException("发送json串异常");
        }
    }

    public void pushCustomerAfterSave() throws Exception {
        boolean success = true;
        String msg = "推送设置成功！";
        HashMap result = new HashMap();

        try {
            this.ciCustomPushReq = this.genCiCustomPushReq(this.customGroupInfo.getCustomGroupId());
            if(this.ciCustomPushReq != null) {
                this.customersService.pushCustomersAfterSave(this.ciCustomPushReq, this.pushCycle, this.customGroupInfo);
            }
        } catch (Exception var7) {
            msg = "推送设置失败，请联系管理员！";
            success = false;
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    private CiCustomPushReq genCiCustomPushReq(String customGroupId) {
        CiCustomPushReq req = new CiCustomPushReq();
        req.setUserId(this.getUserId());
        req.setReqTime(new Date());
        req.setStatus(Integer.valueOf(1));
        this.customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        List customListList = this.customersService.queryCiCustomListInfoByCGroupId(this.customGroupInfo.getCustomGroupId());
        if(customListList == null) {
            return null;
        } else {
            CiCustomListInfo customList = (CiCustomListInfo)customListList.get(0);
            if(customList == null) {
                return null;
            } else {
                req.setListTableName(customList.getListTableName());
                req.setSysIds(this.sysIds);
                req.setReqId("COC" + CiUtil.convertLongMillsToYYYYMMDDHHMMSS(-1L));
                return req;
            }
        }
    }

    public void checkReGenerateCustomer() {
        boolean success = false;
        String msg = "";
        HashMap result = new HashMap();

        try {
            if(this.customGroupInfo != null && this.customGroupInfo.getCustomGroupId() != null) {
                this.customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.customGroupInfo.getCustomGroupId());
                if(this.customGroupInfo != null && this.customGroupInfo.getDataStatus().intValue() != 3) {
                    success = true;
                    msg = "抱歉，该用户群正在被使用，无法重新生成，请稍后再试";
                }
            } else {
                msg = "参数不正确";
            }
        } catch (Exception var7) {
            log.error(var7);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void reGenerateCustomer() {
        boolean success = false;
        String msg = "";
        HashMap result = new HashMap();

        try {
            if(this.customGroupInfo != null && this.customGroupInfo.getCustomGroupId() != null) {
                if(StringUtil.isNotEmpty(this.customGroupInfo.getDataDate())) {
                    this.listTableName = this.customersService.getLastListTableName(this.customGroupInfo.getCustomGroupId(), this.customGroupInfo.getDataDate());
                }

                this.customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.customGroupInfo.getCustomGroupId());
                if(0 != this.customGroupInfo.getStatus().intValue() && 2 != this.customGroupInfo.getDataStatus().intValue()) {
                    if(this.customGroupInfo != null) {
                        this.customersService.reGenerate(this.customGroupInfo, this.getUserId(), this.listTableName);
                        msg = "已提重新生成任务，完成时会在个人通知中通知您。";
                        success = true;
                        CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_REGENERATE", this.customGroupInfo.getCustomGroupId(), this.customGroupInfo.getCustomGroupName(), "用户群重新生成成功【用户群ID：" + this.customGroupInfo.getCustomGroupId() + "名称：" + this.customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                    } else {
                        msg = "用户群不存在";
                    }
                } else {
                    success = false;
                    msg = "不能重新生成已删除或者创建中的用户群！";
                }
            } else {
                msg = "参数不正确";
            }
        } catch (Exception var7) {
            log.error(var7);
            msg = var7.getMessage();
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_REGENERATE", "-1", this.customGroupInfo.getCustomGroupName(), "用户群重新生成失败【用户群ID：" + this.customGroupInfo.getCustomGroupId() + "名称：" + this.customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void findCustomer() throws Exception {
        boolean success = false;
        HashMap returnMap = new HashMap();

        String e;
        try {
            this.customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.customGroupInfo.getCustomGroupId());
            if(this.customGroupInfo != null && StringUtil.isNotEmpty(this.customGroupInfo.getCreateTypeId()) && 7 == this.customGroupInfo.getCreateTypeId().intValue()) {
                this.customFile = this.loadCustomGroupFileService.queryCustomFileByCustomGroupId(this.customGroupInfo.getCustomGroupId());
            }

            IUser response = PrivilegeServiceUtil.getUserById(this.customGroupInfo.getCreateUserId());
            if(response != null) {
                this.customGroupInfo.setCreateUserName(response.getUsername());
            }

            e = this.customersService.queryCustomerOffsetStr(this.customGroupInfo);
            this.customGroupInfo.setKpiDiffRule((this.customGroupInfo.getKpiDiffRule() == null?"":this.customGroupInfo.getKpiDiffRule()) + e);
            success = true;
            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("customGroupInfo", this.customGroupInfo);
            CacheBase cache = CacheBase.getInstance();
            CopyOnWriteArrayList customCreateTypeList = cache.getObjectList("DIM_CUSTOM_CREATE_TYPE");
            CopyOnWriteArrayList customDataStatusList = cache.getObjectList("DIM_CUSTOM_DATA_STATUS");
            returnMap.put("customCreateType", customCreateTypeList);
            returnMap.put("customDataStatus", customDataStatusList);
            returnMap.put("customFile", this.customFile);
        } catch (Exception var9) {
            e = "查看用户群详情失败";
            log.error(e + ": customGroupId = " + this.customGroupInfo.getCustomGroupId(), var9);
            success = false;
            returnMap.put("success", Boolean.valueOf(success));
            returnMap.put("msg", e);
            throw new CIServiceException(e + ": customGroupId = " + this.customGroupInfo.getCustomGroupId(), var9);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            SimpleDateFormat e1 = new SimpleDateFormat("yyyy-MM-dd");
            this.sendJson(response1, JsonUtil.toJson(returnMap, e1));
        } catch (Exception var8) {
            log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public String getPushCycle() {
        return this.pushCycle;
    }

    public void setPushCycle(String pushCycle) {
        this.pushCycle = pushCycle;
    }

    public CiCustomPushReq getCiCustomPushReq() {
        return this.ciCustomPushReq;
    }

    public void setCiCustomPushReq(CiCustomPushReq ciCustomPushReq) {
        this.ciCustomPushReq = ciCustomPushReq;
    }

    public List<String> getSysIds() {
        return this.sysIds;
    }

    public void setSysIds(List<String> sysIds) {
        this.sysIds = sysIds;
    }

    public String getListTableName() {
        return this.listTableName;
    }

    public void setListTableName(String listTableName) {
        this.listTableName = listTableName;
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

    public Map<String, Object> getResultMap() {
        return this.resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public Integer getHavePushFun() {
        return this.havePushFun;
    }

    public void setHavePushFun(Integer havePushFun) {
        this.havePushFun = havePushFun;
    }

    public List<LabelTrendInfo> getTrendAnalysisList() {
        return this.trendAnalysisList;
    }

    public void setTrendAnalysisList(List<LabelTrendInfo> trendAnalysisList) {
        this.trendAnalysisList = trendAnalysisList;
    }

    public List<CiMarketTask> getFirstLevelTasks() {
        return this.firstLevelTasks;
    }

    public void setFirstLevelTasks(List<CiMarketTask> firstLevelTasks) {
        this.firstLevelTasks = firstLevelTasks;
    }

    public List<CiMarketScene> getFirstLevelScenes() {
        return this.firstLevelScenes;
    }

    public void setFirstLevelScenes(List<CiMarketScene> firstLevelScenes) {
        this.firstLevelScenes = firstLevelScenes;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSceneId() {
        return this.sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public CiCustomLabelSceneRel getCustomLabelSceneRel() {
        return this.customLabelSceneRel;
    }

    public void setCustomLabelSceneRel(CiCustomLabelSceneRel customLabelSceneRel) {
        this.customLabelSceneRel = customLabelSceneRel;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getIsPush() {
        return this.isPush;
    }

    public void setIsPush(String isPush) {
        this.isPush = isPush;
    }

    public CiCustomFileRel getCustomFile() {
        return this.customFile;
    }

    public void setCustomFile(CiCustomFileRel customFile) {
        this.customFile = customFile;
    }

    public List<CiSysInfo> getSysInfoCycleList() {
        return this.sysInfoCycleList;
    }

    public void setSysInfoCycleList(List<CiSysInfo> sysInfoCycleList) {
        this.sysInfoCycleList = sysInfoCycleList;
    }

    public List<CiSysInfo> getCiSysInfoList() {
        return this.ciSysInfoList;
    }

    public void setCiSysInfoList(List<CiSysInfo> ciSysInfoList) {
        this.ciSysInfoList = ciSysInfoList;
    }
}

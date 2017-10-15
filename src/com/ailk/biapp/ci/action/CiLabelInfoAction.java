package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.entity.CiApproveHistory;
import com.ailk.biapp.ci.entity.CiApproveStatus;
import com.ailk.biapp.ci.entity.CiApproveUserInfo;
import com.ailk.biapp.ci.entity.CiEnumCategoryInfo;
import com.ailk.biapp.ci.entity.CiLabelExtInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelSceneRel;
import com.ailk.biapp.ci.entity.CiLabelSceneRelId;
import com.ailk.biapp.ci.entity.CiMdaSysTable;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.entity.DimApproveStatus;
import com.ailk.biapp.ci.entity.DimCocLabelCountRules;
import com.ailk.biapp.ci.entity.DimLabelDataStatus;
import com.ailk.biapp.ci.entity.DimScene;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.market.entity.CiCustomLabelSceneRel;
import com.ailk.biapp.ci.market.service.ICiMarketService;
import com.ailk.biapp.ci.model.CiConfigLabeInfo;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.LabelOperManager;
import com.ailk.biapp.ci.model.LabelUseLogInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.model.TreeNode;
import com.ailk.biapp.ci.service.ICiApproveService;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICiLabelUserUseService;
import com.ailk.biapp.ci.service.ILabelQueryService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.http.ResponseEncodingUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiLabelInfoAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiLabelInfoAction.class);
    private CiLabelInfo createLabelInfo;
    private CiLabelInfo searchConditionInfo;
    private CiLabelInfo ciLabelInfo;
    private Pager pager;
    private String labelIds;
    private String labelId;
    private String labelName;
    private String dimName;
    private String columnId;
    private String enumCategoryId;
    private List<LabelDetailInfo> labelDetailList;
    private List<LabelUseLogInfo> labelUseLogInfo;
    boolean approver = false;
    private String showType;
    private String searchType;
    private int rankingListType = 1;
    private List<LabelOperManager> labelOperManagerList;
    @Autowired
    private ILabelQueryService labelQueryService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICiApproveService ciApproveService;
    @Autowired
    private ICiLabelUserUseService ciLabelUserUseService;
    @Autowired
    private ICiMarketService marketService;
    private List<DimLabelDataStatus> dimLabelDataStatusList;
    private List<DimApproveStatus> dimApproveStatusList;
    private List<CiLabelInfo> ciLabelInfoList;
    private CiEnumCategoryInfo ciEnumCategoryInfo;
    private String enumCategoryIds;
    private String enumCategoryInfoId;
    private File file;
    private String fileFileName;
    private Integer labelTypeId;
    private List<DimScene> dimScenes;
    private String sceneIds;
    private int IsSysRecom;

    public CiLabelInfoAction() {
    }

    public String labelManageIndex() throws Exception {
        return "labelManageIndex";
    }

    public String init() throws Exception {
        CacheBase cacheBase = CacheBase.getInstance();
        this.dimLabelDataStatusList =(List<DimLabelDataStatus>) cacheBase.getObjectList("DIM_LABEL_DATA_STATUS");
        CopyOnWriteArrayList list = cacheBase.getObjectList("DIM_APPROVE_STATUS");
        ArrayList approveStatusList = new ArrayList();
        Iterator i$ = list.iterator();

        while(true) {
            DimApproveStatus dimApproveStatus;
            do {
                if(!i$.hasNext()) {
                    this.dimApproveStatusList = approveStatusList;
                    if(this.pager == null) {
                        this.pager = new Pager();
                    }

                    return "index";
                }

                dimApproveStatus = (DimApproveStatus)i$.next();
            } while(!"1".equals(dimApproveStatus.getProcessId()) && dimApproveStatus.getSortNum().longValue() != 0L);

            approveStatusList.add(dimApproveStatus);
        }
    }

    public String query() throws Exception {
        String createUserId = PrivilegeServiceUtil.getUserId();
        if(this.searchConditionInfo == null) {
            this.searchConditionInfo = new CiLabelInfo();
        }

        this.searchConditionInfo.setCreateUserId(createUserId);
        if(this.pager == null) {
            this.pager = new Pager();
        }

        Pager p2 = new Pager((long)this.ciLabelInfoService.queryTotalPageCount(this.searchConditionInfo));
        this.pager.setTotalPage(p2.getTotalPage());
        this.pager.setTotalSize(p2.getTotalSize());
        this.pager = this.pager.pagerFlip();
        List ciLabelInfoList = this.ciLabelInfoService.queryPageList(this.pager.getPageNum(), this.pager.getPageSize(), this.searchConditionInfo);
        String labelStopOnOffLine = Configure.getInstance().getProperty("LABEL_STOPONOFFLINE");
        if("true".equals(labelStopOnOffLine)) {
            CopyOnWriteArrayList allEffectiveLabel = CacheBase.getInstance().getObjectList("ALL_EFFECTIVE_LABEL_MAP");
            this.labelOperManagerList = new ArrayList();

            LabelOperManager lom;
            for(Iterator i$ = ciLabelInfoList.iterator(); i$.hasNext(); this.labelOperManagerList.add(lom)) {
                CiLabelInfo ci = (CiLabelInfo)i$.next();
                lom = new LabelOperManager();
                int statusId = ci.getDataStatusId().intValue();
                if(statusId == 2 || statusId == 4) {
                    List cil = this.ciLabelInfoService.queryChildrenById(ci.getLabelId(), allEffectiveLabel);
                    if(ci.getLabelLevel().intValue() != 1 && ci.getLabelLevel().intValue() != 2 && cil.size() == 0) {
                        if(statusId == 2) {
                            lom.setStopFlag(Integer.valueOf(1));
                        }

                        if(statusId == 4) {
                            lom.setOffLineFlag(Integer.valueOf(1));
                        }
                    }

                    if(statusId == 4 && null != ci.getParentId() && ci.getParentId().intValue() != -1) {
                        CiLabelInfo parentCiLabelInfo = this.ciLabelInfoService.queryCiLabelInfoById(ci.getParentId());
                        if(parentCiLabelInfo.getDataStatusId().intValue() == 2) {
                            lom.setOnLineFlag(Integer.valueOf(1));
                        }
                    }
                }
            }
        }

        this.pager.setResult(ciLabelInfoList);
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_SELECT", "", "", "我创建的标签查询成功,【标签名称 :" + this.searchConditionInfo.getLabelName() + ",数据状态ID:" + this.searchConditionInfo.getDataStatusId() + ",审批状态ID:" + this.searchConditionInfo.getCurrApproveStatusId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "query";
    }

    public void save() throws Exception {
        String msg = "";
        HashMap returnMsg = new HashMap();
        HttpServletResponse response = this.getResponse();

        try {
            String e = PrivilegeServiceUtil.getUserId();
            Integer deptId = PrivilegeServiceUtil.getUserDeptId(e);
            String deptIdStr = "";
            if(deptId != null) {
                deptIdStr = Integer.toString(deptId.intValue());
            }

            this.createLabelInfo.setCreateTime(new Date());
            this.createLabelInfo.setCreateUserId(e);
            this.createLabelInfo.setDeptId(deptIdStr);
            this.createLabelInfo.setDataStatusId(Integer.valueOf(1));
            this.createLabelInfo.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_NOT_SYS_RECOM));
            Boolean hasSameLabel = this.ciLabelInfoService.hasSameLabel(this.createLabelInfo);
            if(hasSameLabel.booleanValue()) {
                msg = "标签有重名";
                returnMsg.put("msgType", "sameLabel");
            } else {
                if(this.createLabelInfo.getLabelId() != null) {
                    msg = "标签修改成功";
                } else {
                    msg = "标签创建成功";
                }

                ArrayList sceneList = new ArrayList();
                if(StringUtil.isNotEmpty(this.sceneIds)) {
                    String[] sceneIdsArr = this.sceneIds.split(",");

                    for(int i = 0; i < sceneIdsArr.length; ++i) {
                        if(StringUtil.isNotEmpty(sceneIdsArr[i].trim())) {
                            CiLabelSceneRel item = new CiLabelSceneRel();
                            item.setId(new CiLabelSceneRelId());
                            item.getId().setSceneId(sceneIdsArr[i].trim());
                            sceneList.add(item);
                        }
                    }

                    this.createLabelInfo.setSceneList(sceneList);
                }

                this.ciLabelInfoService.add(this.createLabelInfo);
                this.insertOrSubmitApprove(this.createLabelInfo, 1);
                returnMsg.put("msgType", "successSave");
                returnMsg.put("labelId", this.createLabelInfo.getLabelId());
                returnMsg.put("labelName", this.createLabelInfo.getLabelName());
                if(msg.contains("修改")) {
                    CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_UPDATE", this.createLabelInfo.getLabelId().toString(), this.createLabelInfo.getLabelName(), msg + ",【标签名称 :" + this.createLabelInfo.getLabelName() + ",业务口径:" + this.createLabelInfo.getBusiCaliber() + ",更新周期:" + this.createLabelInfo.getUpdateCycle() + ",失效日期:" + DateUtil.date2String(this.createLabelInfo.getFailTime(), this.createLabelInfo.getUpdateCycle().intValue() == 1?"yyyy-MM-dd":"yyyy-MM") + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                } else {
                    CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_ADD", this.createLabelInfo.getLabelId().toString(), this.createLabelInfo.getLabelName(), msg + ",【标签名称 :" + this.createLabelInfo.getLabelName() + ",业务口径:" + this.createLabelInfo.getBusiCaliber() + ",更新周期:" + this.createLabelInfo.getUpdateCycle() + ",失效日期:" + DateUtil.date2String(this.createLabelInfo.getFailTime(), this.createLabelInfo.getUpdateCycle().intValue() == 1?"yyyy-MM-dd":"yyyy-MM") + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                }
            }

            returnMsg.put("msgContent", msg);
        } catch (Exception var13) {
            this.log.error("标签保存出错", var13);
            if(this.createLabelInfo.getLabelId() != null) {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_UPDATE", "-1", "标签修改出错", "标签修改失败,【标签名称 :" + this.createLabelInfo.getLabelName() + ",业务口径:" + this.createLabelInfo.getBusiCaliber() + ",更新周期:" + this.createLabelInfo.getUpdateCycle() + ",失效日期:" + DateUtil.date2String(this.createLabelInfo.getFailTime(), this.createLabelInfo.getUpdateCycle().intValue() == 1?"yyyy-MM-dd":"yyyy-MM") + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            } else {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_ADD", "-1", "标签创建出错", "标签创建失败,【标签名称 :" + this.createLabelInfo.getLabelName() + ",业务口径:" + this.createLabelInfo.getBusiCaliber() + ",更新周期:" + this.createLabelInfo.getUpdateCycle() + ",失效日期:" + DateUtil.date2String(this.createLabelInfo.getFailTime(), this.createLabelInfo.getUpdateCycle().intValue() == 1?"yyyy-MM-dd":"yyyy-MM") + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            }

            throw new CIServiceException(var13);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var12) {
            this.log.error("发送json串异常", var12);
            throw new CIServiceException(var12);
        }
    }

    public void findSimilarityLabelNameList() throws Exception {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
            String createUserId = PrivilegeServiceUtil.getUserId();
            this.createLabelInfo.setCreateUserId(createUserId);
            List labelList = this.ciLabelInfoService.querySimilarityLabelNameList(this.createLabelInfo);
            HttpServletResponse response = this.getResponse();

            try {
                this.sendJson(response, JsonUtil.toJson(labelList, e));
            } catch (Exception var6) {
                this.log.error("发送json串异常", var6);
                throw new CIServiceException(var6);
            }
        } catch (Exception var7) {
            this.log.error("CiLabelInfoAction.findSimilarityLabelNameList method 发生异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public void del() throws Exception {
        boolean success = false;
        String msg = "";
        HashMap returnMsg = new HashMap();
        ArrayList labelIdList = new ArrayList();
        ArrayList labelNameList = new ArrayList();

        try {
            String[] response = this.labelIds.trim().split(",");
            if(response != null && response.length > 0) {
                for(int e = 0; e < response.length; ++e) {
                    labelIdList.add(Integer.valueOf(Integer.parseInt(response[e])));
                    labelNameList.add(this.ciLabelInfoService.queryCiLabelInfoById(Integer.valueOf(Integer.parseInt(response[e]))).getLabelName());
                }
            }

            this.ciLabelInfoService.delete(labelIdList);
            success = true;
            msg = "删除成功";
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_DELETE", labelIdList.toString(), labelNameList.toString(), "删除标签成功，其中标签Id:" + labelIdList + ",标签名称:" + labelNameList, OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var9) {
            success = false;
            msg = "删除失败";
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_DELETE", labelIdList.toString(), labelNameList.toString(), "删除标签成功,其中标签Id:" + labelIdList + ",标签名称:" + labelNameList, OperResultEnum.Failure, LogLevelEnum.Medium);
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

    public void stopUseAndOffLineLabel() {
        HashMap returnMsg = new HashMap();
        StringBuffer msg = new StringBuffer();
        boolean success = false;
        String flag = "";
        byte statusId = 4;
        if("1".equals(this.searchType.trim())) {
            flag = "停用";
        } else {
            if(!"2".equals(this.searchType.trim())) {
                this.log.error("停用或下线参数searchType传递异常");
                throw new CIServiceException("停用或下线参数searchType传递异常");
            }

            flag = "下线";
            statusId = 5;
        }

        try {
            CiLabelInfo response = this.ciLabelInfoService.queryCiLabelInfoByIdFullLoad(Integer.valueOf(this.labelId));
            this.ciLabelInfoService.stopUseAndOffLineLabel(response, Integer.valueOf(statusId));
            String e = Configure.getInstance().getProperty("VERSION_FLAG");
            if(e.equals("true")) {
                List rels = this.marketService.queryCustomLabelSceneRelsByType(response.getLabelId() + "", Integer.valueOf(ServiceConstants.RECOM_SHOW_TYPE_LABEL));
                Iterator i$ = rels.iterator();

                while(i$.hasNext()) {
                    CiCustomLabelSceneRel ciCustomLabelSceneRel = (CiCustomLabelSceneRel)i$.next();
                    ciCustomLabelSceneRel.setStatus(Integer.valueOf(ServiceConstants.RECOM_STATUS_INVALIDATE));
                    this.marketService.saveCustomOrLabelSceneRel(ciCustomLabelSceneRel);
                }
            }

            msg.append("标签< ").append(response.getLabelName()).append(" >已").append(flag).append("成功");
            success = true;
            if("1".equals(this.searchType.trim())) {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_STOP", response.getLabelId().toString(), response.getLabelName(), "标签管理->停用成功,【标签Id:" + response.getLabelId() + ",标签名称:" + response.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            } else if("2".equals(this.searchType.trim())) {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_OFF", response.getLabelId().toString(), response.getLabelName(), "标签管理->下线成功,【标签Id:" + response.getLabelId() + ",标签名称:" + response.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var12) {
            if("1".equals(this.searchType.trim())) {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_STOP", this.ciLabelInfo.getLabelId().toString(), this.ciLabelInfo.getLabelName(), "标签管理->停用失败,【标签Id:" + this.ciLabelInfo.getLabelId() + ",标签名称:" + this.ciLabelInfo.getLabelName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            } else if("2".equals(this.searchType.trim())) {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_OFF", this.ciLabelInfo.getLabelId().toString(), this.ciLabelInfo.getLabelName(), "标签管理->下线失败,【标签Id:" + this.ciLabelInfo.getLabelId() + ",标签名称:" + this.ciLabelInfo.getLabelName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            }

            success = false;
            msg.append(flag).append("标签失败");
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    public void onLineClassesLabel() {
        HashMap returnMsg = new HashMap();
        StringBuffer msg = new StringBuffer();
        boolean success = false;

        try {
            CiLabelInfo response = this.ciLabelInfoService.queryCiLabelInfoByIdFullLoad(Integer.valueOf(this.labelId));
            this.ciLabelInfoService.onLineClassesLabel(response);
            msg.append("标签< ").append(response.getLabelName()).append(" >已生效").append("成功");
            success = true;
        } catch (Exception var7) {
            success = false;
            msg.append("上线标签失败");
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

    public void onLineLabel() {
        HashMap returnMsg = new HashMap();
        String msg = "";
        boolean success = false;

        try {
            this.ciLabelInfo = this.ciLabelInfoService.queryCiLabelInfoByIdFullLoad(Integer.valueOf(this.labelId));
            this.ciLabelInfoService.onLineLabel(this.ciLabelInfo);
            msg = "标签< " + this.ciLabelInfo.getLabelName() + " >启用成功";
            success = true;
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_ENABLE", this.ciLabelInfo.getLabelId().toString(), this.ciLabelInfo.getLabelName(), "标签管理->启用成功,【标签Id:" + this.ciLabelInfo.getLabelId() + ",标签名称:" + this.ciLabelInfo.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var7) {
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_ENABLE", this.ciLabelInfo.getLabelId().toString(), this.ciLabelInfo.getLabelName(), "标签管理->启用失败,【标签Id:" + this.ciLabelInfo.getLabelId() + ",标签名称:" + this.ciLabelInfo.getLabelName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            msg = "标签启用失败";
        }

        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void find() throws Exception {
        HashMap returnLabel = new HashMap();
        HttpServletResponse response = this.getResponse();

        try {
            new CiLabelInfo();
            CiLabelInfo e = this.ciLabelInfoService.queryCiLabelInfoById(Integer.getInteger(this.labelId));
            returnLabel.put("ciLabelInfo", e);
        } catch (Exception var5) {
            this.log.error("CiLabelInfoAction.find() method 发生异常", var5);
            throw new CIServiceException(var5);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnLabel));
        } catch (Exception var4) {
            this.log.error("发送json串异常", var4);
            throw new CIServiceException(var4);
        }
    }

    public void getLabelDetailInfo() throws Exception {
        HashMap labelDetail = new HashMap();
        new CiApproveStatus();
        String currApproveStatusId = null;
        HttpServletResponse response = this.getResponse();
        String showWhichContent = this.getRequest().getParameter("showWhichContent");
        String showConfigContent = this.getRequest().getParameter("showConfigContent");

        try {
            new CiLabelInfo();
            CiLabelInfo e;
            if(StringUtils.isNotBlank(showConfigContent) && "true".equals(showConfigContent)) {
                e = this.ciLabelInfoService.queryCiLabelInfoByIdFullLoad(Integer.valueOf(this.labelId.trim()));
                CiConfigLabeInfo ciLabelHistory = new CiConfigLabeInfo();
                CiLabelExtInfo iter = new CiLabelExtInfo();
                if(e.getParentId() != null) {
                    if(e.getParentId().intValue() != -1) {
                        CiLabelInfo approveHistoryList = this.ciLabelInfoService.queryCiLabelInfoById(e.getParentId());
                        if(approveHistoryList != null) {
                            ciLabelHistory.setParentName(approveHistoryList.getLabelName());
                        }
                    } else {
                        ciLabelHistory.setParentName("");
                    }

                    iter = e.getCiLabelExtInfo();
                }

                BeanUtils.copyProperties(e, ciLabelHistory);
                if(iter != null) {
                    String approveHistoryList1 = iter.getCountRulesCode();
                    DimCocLabelCountRules nextOperName = this.ciLabelInfoService.queryLabelCountRules(approveHistoryList1);
                    if(nextOperName != null) {
                        BeanUtils.copyProperties(nextOperName, ciLabelHistory);
                    }

                    CiMdaSysTableColumn approveUserList = iter.getCiMdaSysTableColumn();
                    if(approveUserList != null) {
                        BeanUtils.copyProperties(approveUserList, ciLabelHistory);
                        CiMdaSysTable i$ = approveUserList.getCiMdaSysTable();
                        BeanUtils.copyProperties(i$, ciLabelHistory);
                    }

                    labelDetail.put("configLabelInfo", ciLabelHistory);
                }
            } else {
                e = this.ciLabelInfoService.queryCiLabelInfoById(Integer.valueOf(this.labelId.trim()));
            }

            labelDetail.put("ciLabelInfo", e);
            CiApproveStatus ciApproveStatus = this.ciApproveService.queryApproveStatusByResourceIdAndProcessId(this.labelId.trim(), "1");
            if(StringUtil.isNotEmpty(ciApproveStatus)) {
                currApproveStatusId = ciApproveStatus.getCurrApproveStatusId();
            }

            labelDetail.put("currApproveStatusId", currApproveStatusId);
            List ciLabelHistory1 = this.ciApproveService.queryApproveHistoryByResourceIdAndProcessId(this.labelId.trim(), "1");
            Iterator iter1 = ciLabelHistory1.iterator();
            ArrayList approveHistoryList2 = new ArrayList();

            while(iter1.hasNext()) {
                HashMap nextOperName1 = new HashMap();
                CiApproveHistory approveUserList1 = (CiApproveHistory)iter1.next();
                String i$1 = PrivilegeServiceUtil.getUserById(approveUserList1.getApproveUserId()).getUsername();
                String userInfo = approveUserList1.getApproveResult().intValue() == 1?"审核通过":"审核不通过";
                nextOperName1.put("approveUserName", i$1);
                nextOperName1.put("approveResult", userInfo);
                nextOperName1.put("approveTime", approveUserList1.getApproveTime());
                nextOperName1.put("approveOpinion", approveUserList1.getApproveOpinion());
                approveHistoryList2.add(nextOperName1);
            }

            labelDetail.put("ciApproveHistroList", approveHistoryList2);
            String nextOperName2 = "";
            List approveUserList2 = this.ciApproveService.queryApproveUserInfoByStatusIdAndDeptId(currApproveStatusId, e.getDeptId());
            String userName;
            if(CollectionUtils.isNotEmpty(approveUserList2)) {
                for(Iterator i$2 = approveUserList2.iterator(); i$2.hasNext(); nextOperName2 = nextOperName2 + userName + ",") {
                    CiApproveUserInfo userInfo1 = (CiApproveUserInfo)i$2.next();
                    userName = PrivilegeServiceUtil.getUserById(userInfo1.getApproveUserId()).getUsername();
                }
            }

            if(nextOperName2.length() > 0) {
                nextOperName2 = nextOperName2.substring(0, nextOperName2.length() - 1);
            }

            labelDetail.put("nextOperName", nextOperName2);
            if("both".equals(showWhichContent)) {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_DETAIL_VIEW_MANAGE", e.getLabelId().toString(), e.getLabelName(), "标签管理->查看标签详细信息成功,【标签Id:" + e.getLabelId() + ",标签名称:" + e.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            } else {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_PROCESS_VIEW", e.getLabelId().toString(), e.getLabelName(), "标签管理->查看标签详细信息成功,【标签Id:" + e.getLabelId() + ",标签名称:" + e.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var17) {
            this.log.error("CiLabelInfoAction.getLabelDetailInfo() method 发生异常", var17);
            if("both".equals(showWhichContent)) {
                if(this.ciLabelInfo != null) {
                    CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_DETAIL_VIEW_MANAGE", this.ciLabelInfo.getLabelId().toString(), this.ciLabelInfo.getLabelName(), "标签管理->查看标签详细失败,【标签Id:" + this.ciLabelInfo.getLabelId() + ",标签名称:" + this.ciLabelInfo.getLabelName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                } else {
                    CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_DETAIL_VIEW_MANAGE", "", "", "标签管理->查看标签详细失败,【标签Id:,标签名称:】", OperResultEnum.Failure, LogLevelEnum.Medium);
                }
            } else if(this.ciLabelInfo != null) {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_PROCESS_VIEW", this.ciLabelInfo.getLabelId().toString(), this.ciLabelInfo.getLabelName(), "标签管理->标签审批流程查看失败,【标签Id:" + this.ciLabelInfo.getLabelId() + ",标签名称:" + this.ciLabelInfo.getLabelName() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            } else {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_APPROVE_PROCESS_VIEW", "", "", "标签管理->标签审批流程查看失败,【标签Id:,标签名称:】", OperResultEnum.Failure, LogLevelEnum.Medium);
            }

            throw new CIServiceException(var17);
        }

        try {
            SimpleDateFormat e1 = new SimpleDateFormat("yyyy-MM-dd");
            this.sendJson(response, JsonUtil.toJson(labelDetail, e1));
        } catch (Exception var16) {
            this.log.error("发送json串异常", var16);
            throw new CIServiceException(var16);
        }
    }

    public void getLabelInfo() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        HashMap returnLabel = new HashMap();
        HttpServletResponse response = this.getResponse();
        new CiLabelInfo();

        try {
            CiLabelInfo ciLabelInfo = this.ciLabelInfoService.queryCiLabelInfoById(Integer.valueOf(this.labelId));
            returnLabel.put("ciLabelInfo", ciLabelInfo);
        } catch (Exception var7) {
            this.log.error("CiLabelInfoAction.getLabelInfo() method 发生异常", var7);
            throw new CIServiceException(var7);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnLabel, df));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void submitApprove() throws Exception {
        boolean success = false;
        String msg = "";
        HashMap returnMsg = new HashMap();
        String labelName = null;
        CiLabelInfo label = this.ciLabelInfoService.queryCiLabelInfoById(Integer.valueOf(Integer.parseInt(this.labelId.trim())));
        labelName = label.getLabelName();

        try {
            this.insertOrSubmitApprove(label, 2);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_SUBMIT", this.labelId, labelName, "标签提交审批成功【标签Id:" + this.labelId + ",标签名称:" + labelName + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var9) {
            this.log.error("提交审批异常", var9);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_SUBMIT", this.labelId, labelName, "标签提交审批失败【标签Id:" + this.labelId + ",标签名称:" + labelName + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new CIServiceException(var9);
        }

        success = true;
        msg = "已提交审批";
        returnMsg.put("msg", msg);
        returnMsg.put("success", Boolean.valueOf(success));
        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public String userAttentionLabelRecord() throws Exception {
        this.ciLabelInfoService.userAttentionLabelRecord(Integer.valueOf(2), "admin");
        return this.init();
    }

    public String delUserAttentionLabelRecord() throws Exception {
        this.ciLabelInfoService.deleteUserAttentionLabelRecord(Integer.valueOf(2), "admin");
        return this.init();
    }

    public void saveAndSubmitApprove() throws Exception {
        String msg = "";
        HashMap returnMsg = new HashMap();
        HttpServletResponse response = this.getResponse();

        try {
            String e = PrivilegeServiceUtil.getUserId();
            String deptId = Integer.toString(PrivilegeServiceUtil.getUserDeptId(e).intValue());
            this.createLabelInfo.setCreateTime(new Date());
            this.createLabelInfo.setCreateUserId(e);
            this.createLabelInfo.setDeptId(deptId);
            this.createLabelInfo.setDataStatusId(Integer.valueOf(1));
            Boolean hasSameLabel = this.ciLabelInfoService.hasSameLabel(this.createLabelInfo);
            if(hasSameLabel.booleanValue()) {
                msg = "标签有重名";
                returnMsg.put("msgType", "sameLabel");
            } else {
                this.ciLabelInfoService.add(this.createLabelInfo);
                this.insertOrSubmitApprove(this.createLabelInfo, 2);
                msg = "已提交审批";
                returnMsg.put("msgType", "successSave");
                returnMsg.put("labelId", this.createLabelInfo.getLabelId());
                returnMsg.put("labelName", this.createLabelInfo.getLabelName());
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_ADD_SUBMIT", this.createLabelInfo.getLabelId().toString(), this.createLabelInfo.getLabelName(), "新增标签保存并提交审批成功【标签名称 :" + this.createLabelInfo.getLabelName() + ",业务口径:" + this.createLabelInfo.getBusiCaliber() + ",更新周期:" + this.createLabelInfo.getUpdateCycle() + ",失效日期:" + DateUtil.date2String(this.createLabelInfo.getFailTime(), this.createLabelInfo.getUpdateCycle().intValue() == 1?"yyyy-MM-dd":"yyyy-MM") + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            }

            returnMsg.put("msgContent", msg);
        } catch (Exception var8) {
            this.log.error("标签保存并提交审批出错", var8);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_MANAGE_ADD_SUBMIT", "-1", "标签保存并提交审批出错", "新增标签保存并提交审批失败【标签名称 :" + this.createLabelInfo.getLabelName() + ",业务口径:" + this.createLabelInfo.getBusiCaliber() + ",更新周期:" + this.createLabelInfo.getUpdateCycle() + ",失效日期:" + DateUtil.date2String(this.createLabelInfo.getFailTime(), this.createLabelInfo.getUpdateCycle().intValue() == 1?"yyyy-MM-dd":"yyyy-MM") + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            throw new CIServiceException(var8);
        }

        try {
            this.sendJson(response, JsonUtil.toJson(returnMsg));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public void findMyLabelTree() throws Exception {
        String createUserId = PrivilegeServiceUtil.getUserId();
        boolean success = false;
        ArrayList treeList = new ArrayList();
        HashMap result = new HashMap();
        if(this.searchConditionInfo == null) {
            this.searchConditionInfo = new CiLabelInfo();
        }

        this.searchConditionInfo.setCreateUserId(createUserId);
        this.searchConditionInfo.setIsStatUserNum(String.valueOf(1));

        try {
            List response = this.ciLabelInfoService.queryPageList(1, 2147483647, this.searchConditionInfo);
            if(response != null) {
                boolean e1 = Boolean.valueOf(Configure.getInstance().getProperty("NEED_AUTHORITY").toLowerCase().trim()).booleanValue();
                boolean hasAuthority = false;
                if(e1) {
                    this.userId = PrivilegeServiceUtil.getUserId();
                    hasAuthority = e1 && !PrivilegeServiceUtil.isAdminUser(this.userId);
                }

                CacheBase cache = CacheBase.getInstance();
                Iterator i$ = response.iterator();

                while(i$.hasNext()) {
                    CiLabelInfo labelInfo = (CiLabelInfo)i$.next();
                    TreeNode treeNode = new TreeNode();
                    treeNode.setId(labelInfo.getLabelId().toString());
                    treeNode.setpId(labelInfo.getParentId() == null?"-1":labelInfo.getParentId().toString());
                    treeNode.setName(labelInfo.getLabelName());
                    CiLabelInfo ciLabelInfo = null;
                    if(hasAuthority) {
                        ciLabelInfo = cache.getEffectiveLabelByUser(labelInfo.getLabelId().toString(), this.userId);
                    } else {
                        ciLabelInfo = cache.getEffectiveLabel(labelInfo.getLabelId().toString());
                    }

                    treeNode.setParam(ciLabelInfo);
                    treeList.add(treeNode);
                }
            }

            success = true;
            result.put("treeList", treeList);
            result.put("success", Boolean.valueOf(success));
        } catch (CIServiceException var14) {
            String e = "获得标签树失败";
            this.log.error(e, var14);
            success = false;
            result.put("msg", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var13) {
            this.log.error("发送json串异常", var13);
            throw new CIServiceException(var13);
        }
    }

    public void findLabelTree() throws Exception {
        List treeList = null;
        boolean success = false;
        HashMap result = new HashMap();

        try {
            treeList = this.ciLabelInfoService.queryLabelTree();
            success = true;
            result.put("treeList", treeList);
            result.put("success", Boolean.valueOf(success));
        } catch (CIServiceException var7) {
            String e = "获得标签树失败";
            this.log.error(e, var7);
            success = false;
            result.put("msg", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void findLabelTreeByName() throws Exception {
        List treeList = null;
        boolean success = false;
        HashMap result = new HashMap();

        try {
            treeList = this.ciLabelInfoService.queryLabelTreeByName(this.labelName.trim());
            success = true;
            result.put("treeList", treeList);
            result.put("success", Boolean.valueOf(success));
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_TREE_SELECT", "", "", "查询标签树成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (CIServiceException var7) {
            String e = "获得标签库模糊查询失败";
            this.log.error(e, var7);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_TREE_SELECT", "", "", "查询标签树失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            success = false;
            result.put("msg", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public String findSysRecommendLabelNum() {
        String dataScope = this.getRequest().getParameter("dataScope");
        if(StringUtil.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;
        int labelNum = 0;

        try {
            byte response = 5;
            long e = this.ciLabelInfoService.querySysRecommendLabelNum().longValue();
            labelNum = (int)e;
            totalPage = (int)Math.ceil((double)e / (double)response);
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(response);
            success = true;
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_SYS_HOT_LABEL_SELECT", "", "", "系统热门标签查询，查询条件：【时间周期：" + dataScope + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var11) {
            msg = "查询系统推荐标签发生异常";
            this.log.error(msg, var11);
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_SYS_HOT_LABEL_SELECT", "-1", "", "系统热门标签查询，查询条件：【时间周期：" + dataScope + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        result.put("success", Boolean.valueOf(success));
        if(success) {
            result.put("totalPage", Integer.valueOf(totalPage));
            result.put("totalSize", Integer.valueOf(labelNum));
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
            return null;
        } catch (Exception var10) {
            this.log.error("发送json串异常", var10);
            throw new CIServiceException(var10);
        }
    }

    public String findSysRecommendLabel() {
        String dataScope = this.getRequest().getParameter("dataScope");
        if(StringUtil.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        try {
            int e = this.pager.getTotalPage();
            int msg1 = Integer.valueOf(this.getRequest().getParameter("currentPage")).intValue();
            if(msg1 <= e) {
                byte pageSize = 5;
                this.labelDetailList = this.ciLabelInfoService.querySysRecommendLabel(msg1, pageSize, dataScope);
                this.pager.setPageSize(pageSize);
                this.pager.setTotalPage(e);
            }

            return "querySearchMoreLabel";
        } catch (Exception var5) {
            String msg = "系统推荐标签查询错误";
            this.log.error(msg, var5);
            throw new CIServiceException(var5);
        }
    }

    public String findSysHotRankLabel() {
        this.rankingListType = 2;
        String dataScope = this.getRequest().getParameter("dataScope");
        if(StringUtil.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        try {
            this.labelDetailList = this.ciLabelInfoService.querySysRecommendLabel(1, ServiceConstants.SHOW_RECOMMEND_LABEL_NUM, dataScope);
            this.pager.setResult(this.labelDetailList);
            return "sysHotLabelRankingList";
        } catch (Exception var4) {
            String msg = "系统热门标签排行查询错误";
            this.log.error(msg, var4);
            throw new CIServiceException(var4);
        }
    }

    public String findUserUsedLabelNum() {
        String dataScope = this.getRequest().getParameter("dataScope");
        if(StringUtil.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;
        long totalNum = 0L;

        try {
            String response = PrivilegeServiceUtil.getUserId();
            byte e = 5;
            long totalSize = this.ciLabelInfoService.queryUserUsedLabelNum(response, dataScope);
            totalNum = totalSize;
            totalPage = (int)Math.ceil((double)totalSize / (double)e);
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(e);
            success = true;
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_MY_USED_LABEL_SELECT", "", "", "用户使用标签查询，查询条件：【时间周期：" + dataScope + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var13) {
            msg = "用户使用标签列表查询错误";
            this.log.error(msg, var13);
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_MY_USED_LABEL_SELECT", "", "", "用户使用标签查询，查询条件：【时间周期：" + dataScope + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        result.put("success", Boolean.valueOf(success));
        if(success) {
            result.put("totalPage", Integer.valueOf(totalPage));
            result.put("totalSize", Long.valueOf(totalNum));
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
            return null;
        } catch (Exception var12) {
            this.log.error("发送json串异常", var12);
            throw new CIServiceException(var12);
        }
    }

    public String findUserUsedLabel() {
        String dataScope = this.getRequest().getParameter("dataScope");
        if(StringUtil.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        String msg;
        try {
            String e = PrivilegeServiceUtil.getUserId();
            msg = "time";
            int totalPage = this.pager.getTotalPage();
            int currentPage = Integer.valueOf(this.getRequest().getParameter("currentPage")).intValue();
            if(currentPage <= totalPage) {
                byte pageSize = 5;
                this.labelDetailList = this.ciLabelInfoService.queryUserUsedLabel(currentPage, pageSize, e, msg, dataScope);
                this.pager.setPageSize(pageSize);
                this.pager.setTotalPage(totalPage);
            }

            return "querySearchMoreLabel";
        } catch (Exception var7) {
            msg = "用户使用标签列表查询错误";
            this.log.error(msg, var7);
            throw new CIServiceException(var7);
        }
    }

    public String findUserAttentionLabelNum() {
        String dataScope = this.getRequest().getParameter("dataScope");
        if(StringUtil.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;
        long totalNum = 0L;

        try {
            String response = PrivilegeServiceUtil.getUserId();
            byte e = 5;
            long totalSize = this.ciLabelInfoService.queryUserAttentionLabelNum(response, dataScope);
            totalNum = totalSize;
            totalPage = (int)Math.ceil((double)totalSize / (double)e);
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(e);
            success = true;
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_ATTENTION_LABEL_SELECT", "", "", "用户关注标签查询，查询条件：【时间周期：" + dataScope + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var13) {
            msg = "用户关注标签列表查询发生异常";
            this.log.error(msg, var13);
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_ATTENTION_LABEL_SELECT", "-1", "", "用户关注标签查询，查询条件：【时间周期：" + dataScope + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        result.put("success", Boolean.valueOf(success));
        if(success) {
            result.put("totalPage", Integer.valueOf(totalPage));
            result.put("totalSize", Long.valueOf(totalNum));
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
            return null;
        } catch (Exception var12) {
            this.log.error("发送json串异常", var12);
            throw new CIServiceException(var12);
        }
    }

    public String findUserAttentionLabel() {
        String dataScope = this.getRequest().getParameter("dataScope");
        if(StringUtil.isEmpty(dataScope)) {
            dataScope = "all";
        }

        this.searchType = "userAttentionSearch";
        this.log.debug("dataScope->" + dataScope);
        if(this.pager == null) {
            this.pager = new Pager();
        }

        String msg;
        try {
            String e = PrivilegeServiceUtil.getUserId();
            msg = "date";
            int totalPage = this.pager.getTotalPage();
            int currentPage = Integer.valueOf(this.getRequest().getParameter("currentPage")).intValue();
            if(currentPage <= totalPage) {
                byte pageSize = 5;
                this.labelDetailList = this.ciLabelInfoService.queryUserAttentionLabel(currentPage, pageSize, e, msg, dataScope);
                this.pager.setPageSize(pageSize);
                this.pager.setTotalPage(totalPage);
            }

            return "querySearchMoreLabel";
        } catch (Exception var7) {
            msg = "用户关注标签列表查询发生异常";
            this.log.error(msg, var7);
            throw new CIServiceException(var7);
        }
    }

    public String findNewPublishLabelNum() {
        String dataScope = this.getRequest().getParameter("dataScope");
        if(StringUtil.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        boolean success = false;
        HashMap result = new HashMap();
        String msg = "";
        int totalPage = 0;
        long labelNum = 0L;

        try {
            byte response = 5;
            long e = this.ciLabelInfoService.queryNewPublishLabelNum(dataScope);
            labelNum = e;
            totalPage = (int)Math.ceil((double)e / (double)response);
            this.pager.setTotalPage(totalPage);
            this.pager.setPageSize(response);
            success = true;
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_NEW_PUBLISH_SELECT", "", "", "最新发布标签查询，查询条件：【时间周期：" + dataScope + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var12) {
            msg = "最新发布标签查询发生异常";
            this.log.error(msg, var12);
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_HOME_NEW_PUBLISH_SELECT", "-1", "", "最新发布标签查询出错，查询条件：【时间周期：" + dataScope + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        result.put("success", Boolean.valueOf(success));
        if(success) {
            result.put("totalPage", Integer.valueOf(totalPage));
            result.put("totalSize", Long.valueOf(labelNum));
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
            return null;
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    public String findlastPublishRankLabel() {
        this.rankingListType = 1;

        try {
            String e = "date";
            this.labelDetailList = this.ciLabelInfoService.queryNewPublishLabel(1, ServiceConstants.SHOW_RECOMMEND_LABEL_NUM, e, "all");
        } catch (Exception var3) {
            String msg = "最新发布标签排行查询发生异常";
            this.log.error(msg, var3);
            throw new CIServiceException(var3);
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        this.pager.setResult(this.labelDetailList);
        return "sysHotLabelRankingList";
    }

    public String findNewPublishLabel() {
        String dataScope = this.getRequest().getParameter("dataScope");
        if(StringUtil.isEmpty(dataScope)) {
            dataScope = "all";
        }

        if(this.pager == null) {
            this.pager = new Pager();
        }

        try {
            String e = "time";
            int msg1 = this.pager.getTotalPage();
            int currentPage = Integer.valueOf(this.getRequest().getParameter("currentPage")).intValue();
            if(currentPage <= msg1) {
                byte pageSize = 5;
                this.labelDetailList = this.ciLabelInfoService.queryNewPublishLabel(currentPage, pageSize, e, dataScope);
                this.pager.setPageSize(pageSize);
                this.pager.setTotalPage(msg1);
            }

            return "querySearchMoreLabel";
        } catch (Exception var6) {
            String msg = "最新发布标签查询发生异常";
            this.log.error(msg, var6);
            throw new CIServiceException(var6);
        }
    }

    public String findUserLabelTree() {
        String type = this.getRequest().getParameter("type");
        String mainLabelId = this.getRequest().getParameter("mainLabelId");
        boolean success = true;
        HashMap result = new HashMap();
        String msg = "我的标签库数据封装OK";

        try {
            String response = PrivilegeServiceUtil.getUserId();
            List e = this.labelQueryService.queryMyLabelTree(response, "");
            List relSysRecommend;
            if(StringUtil.isNotEmpty(type) && type.equals("contrast")) {
                relSysRecommend = this.labelQueryService.queryContrastSysRecommend(mainLabelId, "");
                if(null != relSysRecommend && relSysRecommend.size() > 0) {
                    e.addAll(relSysRecommend);
                }
            }

            if(StringUtil.isNotEmpty(type) && type.equals("rel")) {
                relSysRecommend = this.labelQueryService.queryRelSysRecommend(mainLabelId, "");
                if(null != relSysRecommend && relSysRecommend.size() > 0) {
                    e.addAll(relSysRecommend);
                }
            }

            result.put("treeList", e);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_TREE_SELECT", "", "", "查询标签树成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var10) {
            msg = "我的标签库数据封装发生异常";
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_TREE_SELECT", "", "", "查询标签树失败", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error(msg, var10);
            success = false;
        }

        HttpServletResponse response1 = this.getResponse();
        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
            return null;
        } catch (Exception var9) {
            this.log.error("发送json串异常", var9);
            throw new CIServiceException(var9);
        }
    }

    public String findUserLabelTreeByName() {
        String labelName = this.getRequest().getParameter("labelName");
        String type = this.getRequest().getParameter("type");
        String mainLabelId = this.getRequest().getParameter("mainLabelId");
        boolean success = true;
        HashMap result = new HashMap();
        String msg = "我的标签库数据封装OK";

        try {
            String response = PrivilegeServiceUtil.getUserId();
            List e = this.labelQueryService.queryMyLabelTree(response, labelName.trim());
            List relSysRecommend;
            if(StringUtil.isNotEmpty(type) && type.equals("contrast")) {
                relSysRecommend = this.labelQueryService.queryContrastSysRecommend(mainLabelId, labelName.trim());
                if(null != relSysRecommend && relSysRecommend.size() > 0) {
                    e.addAll(relSysRecommend);
                }
            }

            if(StringUtil.isNotEmpty(type) && type.equals("rel")) {
                relSysRecommend = this.labelQueryService.queryRelSysRecommend(mainLabelId, labelName.trim());
                if(null != relSysRecommend && relSysRecommend.size() > 0) {
                    e.addAll(relSysRecommend);
                }
            }

            result.put("treeList", e);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_TREE_SELECT", "", "", "查询标签树成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var11) {
            msg = "我的标签库数据封装发生异常";
            this.log.error(msg, var11);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_TREE_SELECT", "", "", "查询标签树成功", OperResultEnum.Failure, LogLevelEnum.Medium);
            success = false;
        }

        HttpServletResponse response1 = this.getResponse();
        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
            return null;
        } catch (Exception var10) {
            this.log.error("发送json串异常", var10);
            throw new CIServiceException(var10);
        }
    }

    public String findUserLabelTreeHaveTemplate() {
        boolean success = true;
        HashMap result = new HashMap();
        String msg = "我的标签库数据封装OK";

        try {
            String response = PrivilegeServiceUtil.getUserId();
            List e = this.labelQueryService.queryMyLabelTree(response, "");
            List templateTreeNodes = this.labelQueryService.queryMyTemplateTree(response, "");
            e.addAll(templateTreeNodes);
            result.put("treeList", e);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_TREE_SELECT", "", "", "查询标签树成功", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var8) {
            msg = "我的标签库数据封装发生异常";
            this.log.error(msg, var8);
            success = false;
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_TREE_SELECT", "", "", "查询标签树成功", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse response1 = this.getResponse();
        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
            return null;
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public String findUserLabelTreeHaveTemplateByName() {
        String labelName = this.getRequest().getParameter("labelName");
        boolean success = true;
        HashMap result = new HashMap();
        String msg = "我的标签库数据封装OK";

        try {
            String response = PrivilegeServiceUtil.getUserId();
            List e = this.labelQueryService.queryMyLabelTree(response, labelName.trim());
            List templateTreeNodes = this.labelQueryService.queryMyTemplateTree(response, labelName.trim());
            e.addAll(templateTreeNodes);
            result.put("treeList", e);
        } catch (Exception var9) {
            msg = "我的标签库数据封装发生异常";
            this.log.error(msg, var9);
            success = false;
        }

        HttpServletResponse response1 = this.getResponse();
        result.put("success", Boolean.valueOf(success));
        result.put("msg", msg);

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
            return null;
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
            throw new CIServiceException(var8);
        }
    }

    public String labelDifferential() {
        this.ciLabelInfo = CacheBase.getInstance().getEffectiveLabel(String.valueOf(this.ciLabelInfo.getLabelId()));
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_DIFFERENTIAL_LINK", this.ciLabelInfo.getLabelId().toString(), this.ciLabelInfo.getLabelName(), "标签分析->标签微分页查看成功,【标签Id:" + this.ciLabelInfo.getLabelId() + "；标签name:" + this.ciLabelInfo.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Normal);
        return "labelDifferential";
    }

    public String useTimesDetail() {
        String enterType = this.getRequest().getParameter("enterType");
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager == null) {
            this.pager = new Pager();
            this.pager.setPageSize(10);
        }

        if(this.pager.getTotalSize() == 0L) {
            this.pager.setTotalSize(this.ciLabelUserUseService.getLabelUseTimesByID(this.labelId));
        }

        this.pager = this.pager.pagerFlip();
        this.labelUseLogInfo = this.ciLabelUserUseService.queryLabelUseLogInfos(this.pager.getPageNum(), 10, this.labelId);
        this.pager.setResult(this.labelUseLogInfo);
        if("labelAnalysis".equals(enterType)) {
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_USED_LIST_SELECT", this.labelId, "", "标签分析->标签使用记录列表查询成功,【标签Id:" + (StringUtil.isEmpty(this.labelId)?"无":this.labelId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } else {
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_USED_LIST_SELECT_INDEX", this.labelId, "", "标签使用记录列表查询，查询条件：【标签ID：" + (StringUtil.isEmpty(this.labelId)?"无":this.labelId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        }

        return "labelUseNumDetail";
    }

    /** @deprecated */
    @Deprecated
    public void findDimTree() {
        HashMap returnMap = new HashMap();
        boolean success = false;

        String json;
        try {
            List response = this.ciLabelInfoService.queryDimTree(this.labelId, this.dimName);
            returnMap.put("dimTree", response);
            success = true;
            returnMap.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            json = "查询维表错误";
            this.log.error(json, var7);
            returnMap.put("msg", json);
            returnMap.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();
        json = null;

        try {
            json = JsonUtil.toJson(returnMap);
            this.sendJson(response1, json);
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public String findLabelDimValue() {
        try {
            if(!StringUtil.isEmpty(this.labelId)) {
                if(this.pager == null) {
                    this.pager = new Pager();
                }

                String e = PrivilegeServiceUtil.getUserId();
                this.pager.setTotalSize(this.ciLabelInfoService.queryLabelDimValueCount(e, Integer.valueOf(this.labelId), this.dimName, this.enumCategoryId));
                int totalpage = (int)Math.ceil((double)this.pager.getTotalSize() / (double)this.pager.getPageSize());
                this.pager.setTotalPage(totalpage);
                this.pager = this.pager.pagerFlip();
                List list = this.ciLabelInfoService.queryLabelDimValue(Integer.valueOf(this.labelId), this.pager, this.dimName, e, this.enumCategoryId);
                this.pager.setResult(list);
            }
        } catch (Exception var4) {
            this.log.error("查询枚举标签异常", var4);
        }

        return "labeldimvaluelist";
    }

    public void findAllLabelDimValue() {
        boolean success = false;
        String msg = "";
        HashMap result = new HashMap();
        List list = null;

        try {
            if(!StringUtil.isEmpty(this.labelId)) {
                if(this.pager == null) {
                    this.pager = new Pager();
                }

                String response = PrivilegeServiceUtil.getUserId();
                long e = this.ciLabelInfoService.queryLabelDimValueCount(response, Integer.valueOf(this.labelId), this.dimName, this.enumCategoryId);
                this.pager.setTotalSize(e);
                this.pager.setTotalPage(1);
                this.pager.setPageSize((int)e);
                list = this.ciLabelInfoService.queryLabelDimValue(Integer.valueOf(this.labelId), this.pager, this.dimName, response, this.enumCategoryId);
                success = true;
            }
        } catch (Exception var9) {
            success = false;
            this.log.error("查询枚举所有标签异常", var9);
        }

        result.put("success", Boolean.valueOf(success));
        if(success) {
            result.put("total", Long.valueOf(this.pager.getTotalSize()));
            result.put("dimValueList", list);
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
        }

    }

    public String findLabelDimValueByColumnId() {
        try {
            if(!StringUtil.isEmpty(this.columnId)) {
                if(this.pager == null) {
                    this.pager = new Pager();
                }

                String e = PrivilegeServiceUtil.getUserId();
                this.pager.setTotalSize(this.ciLabelInfoService.queryLabelDimValueCountByColumnId(e, Integer.valueOf(this.columnId), this.dimName, this.enumCategoryId));
                int totalpage = (int)Math.ceil((double)this.pager.getTotalSize() / (double)this.pager.getPageSize());
                this.pager.setTotalPage(totalpage);
                this.pager = this.pager.pagerFlip();
                List list = this.ciLabelInfoService.queryLabelDimValueByColumnId(Integer.valueOf(this.columnId), this.pager, this.dimName, e, this.enumCategoryId);
                this.pager.setResult(list);
            }
        } catch (Exception var4) {
            this.log.error("根据columnId查询多维值异常", var4);
        }

        return "itemChooseList4VerticalLabel";
    }

    public void findAllLabelDimValueByColumnId() {
        boolean success = false;
        String msg = "";
        HashMap result = new HashMap();
        List list = null;

        try {
            if(!StringUtil.isEmpty(this.columnId)) {
                if(this.pager == null) {
                    this.pager = new Pager();
                }

                String response = PrivilegeServiceUtil.getUserId();
                long e = this.ciLabelInfoService.queryLabelDimValueCountByColumnId(response, Integer.valueOf(this.columnId), this.dimName, this.enumCategoryId);
                this.pager.setTotalSize(e);
                this.pager.setTotalPage(1);
                this.pager.setPageSize((int)e);
                list = this.ciLabelInfoService.queryLabelDimValueByColumnId(Integer.valueOf(this.columnId), this.pager, this.dimName, response, this.enumCategoryId);
                success = true;
            }
        } catch (Exception var9) {
            success = false;
            this.log.error("根据columnId查询所有多维值异常", var9);
        }

        result.put("success", Boolean.valueOf(success));
        if(success) {
            result.put("total", Long.valueOf(this.pager.getTotalSize()));
            result.put("dimValueList", list);
        } else {
            result.put("msg", msg);
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var8) {
            this.log.error("发送json串异常", var8);
        }

    }

    public void changeRecommendLabel() {
        HashMap returnMap = new HashMap();
        boolean success = false;

        String json;
        try {
            CacheBase response = CacheBase.getInstance();
            CiLabelInfo json1 = response.getEffectiveLabel(this.labelId);
            if(json1 == null) {
                throw new CIServiceException("缓存中未查询到标签信息！");
            }

            String e = "";
            if(json1.getIsSysRecom() != null && json1.getIsSysRecom().intValue() != ServiceConstants.IS_NOT_SYS_RECOM) {
                if(json1.getIsSysRecom() != null && json1.getIsSysRecom().intValue() == ServiceConstants.IS_SYS_RECOM) {
                    json1.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_NOT_SYS_RECOM));
                    e = "取消推荐设置成功！";
                }
            } else {
                json1.setIsSysRecom(Integer.valueOf(ServiceConstants.IS_SYS_RECOM));
                e = "系统推荐设置成功！";
            }

            this.ciLabelInfoService.modify(json1);
            response.modifyEffectiveLabel(json1.getLabelId() + "", json1);
            success = true;
            returnMap.put("msg", e);
            returnMap.put("success", Boolean.valueOf(success));
        } catch (Exception var7) {
            success = false;
            json = "系统推荐设置失败！";
            this.log.error(json, var7);
            returnMap.put("msg", json);
            returnMap.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();
        json = null;

        try {
            json = JsonUtil.toJson(returnMap);
            this.sendJson(response1, json);
        } catch (Exception var6) {
            this.log.error("发送json串异常", var6);
            throw new CIServiceException(var6);
        }
    }

    public void initEnumCategorys() {
        HashMap returnMap = new HashMap();
        boolean success = false;

        String json;
        try {
            String response = PrivilegeServiceUtil.getCityIdFromSession();
            Integer json1 = Integer.valueOf(-1);
            if(StringUtil.isNotEmpty(this.columnId)) {
                json1 = Integer.valueOf(this.columnId);
            } else {
                CacheBase e = CacheBase.getInstance();
                CiLabelInfo num = e.getEffectiveLabel(this.labelId);
                json1 = num.getCiLabelExtInfo().getCiMdaSysTableColumn().getColumnId();
            }

            List e1 = this.ciLabelInfoService.queryEnumCategoryInfoList(response, json1);
            returnMap.put("enumCategory", e1);
            int num1 = e1 != null?e1.size():0;
            returnMap.put("categoryNum", Integer.valueOf(num1));
            success = true;
        } catch (Exception var8) {
            json = "获取枚举分类失败！";
            this.log.error(json, var8);
            returnMap.put("msg", json);
        }

        returnMap.put("success", Boolean.valueOf(success));
        HttpServletResponse response1 = this.getResponse();
        json = null;

        try {
            json = JsonUtil.toJson(returnMap);
            this.sendJson(response1, json);
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public String findVertAndEnumLabelIndex() {
        return "vertAndEnumLabelIndex";
    }

    public String findVertAndEnumLabelList() {
        try {
            if(this.pager == null) {
                this.pager = new Pager();
            }

            Pager e = new Pager((long)this.ciLabelInfoService.queryVertAndEnumLabelCount(this.searchConditionInfo));
            this.pager.setTotalPage(e.getTotalPage());
            this.pager.setTotalSize(e.getTotalSize());
            this.pager = this.pager.pagerFlip();
            List ciLabelInfoList = this.ciLabelInfoService.queryVertAndEnumLabelList(this.pager, this.searchConditionInfo);
            this.pager.setResult(ciLabelInfoList);
        } catch (Exception var3) {
            this.log.error("获取枚举及纵表标签列表失败", var3);
        }

        return "vertAndEnumLabelList";
    }

    public String findEnumCategoryInfoIndex() {
        this.ciLabelInfoList = this.ciLabelInfoService.queryEnumLabelOrColumnListByLabelIdAndType(this.labelId);
        if(this.ciLabelInfoList.size() > 0) {
            this.ciLabelInfo = (CiLabelInfo)this.ciLabelInfoList.get(0);
        }

        return "enumCategoryInfoIndex";
    }

    public String findEnumCategoryInfoList() {
        try {
            if(this.pager == null) {
                this.pager = new Pager();
            }

            String e = this.getUserId();
            String cityId = PrivilegeServiceUtil.getCityIdFromSession();
            Pager p2 = new Pager((long)this.ciLabelInfoService.queryEnumCategoryInfoTotalCountByColumnId(e, cityId, Integer.valueOf(this.columnId)));
            this.pager.setTotalPage(p2.getTotalPage());
            this.pager.setTotalSize(p2.getTotalSize());
            this.pager = this.pager.pagerFlip();
            List ciEnumCategoryInfos = this.ciLabelInfoService.queryEnumCategoryInfoPagerListByColumnId(this.pager, e, cityId, Integer.valueOf(this.columnId));
            this.pager.setResult(ciEnumCategoryInfos);
        } catch (Exception var5) {
            this.log.error("根据属性ID获取对应的分类列表失败", var5);
        }

        return "enumCategoryInfoList";
    }

    public void saveCategoryInfo() throws Exception {
        Map returnMap = null;

        String e;
        try {
            if(null != this.ciEnumCategoryInfo.getColumnId() && StringUtil.isNotEmpty(this.ciEnumCategoryInfo.getEnumCategoryName())) {
                String response = this.getUserId();
                e = PrivilegeServiceUtil.getCityIdFromSession();
                this.ciEnumCategoryInfo.setUserId(response);
                this.ciEnumCategoryInfo.setCityId(e);
                this.ciEnumCategoryInfo.setStatus(Integer.valueOf(ServiceConstants.STATUS_VAL));
                this.ciEnumCategoryInfo.setEnumNum(Integer.valueOf(0));
                returnMap = this.ciLabelInfoService.isCategoryInfoNameExist(this.ciEnumCategoryInfo);
                if(((Boolean)returnMap.get("success")).booleanValue()) {
                    this.ciLabelInfoService.addEnumCategoryInfo(this.ciEnumCategoryInfo);
                    String msg = "保存成功！";
                    returnMap.put("msg", msg);
                }
            }
        } catch (Exception var6) {
            e = "创建枚举维值分类";
            this.log.error(e + " : " + this.ciEnumCategoryInfo.getEnumCategoryName() + " 失败", var6);
            returnMap.put("msg", e + "失败");
            returnMap.put("success", Boolean.valueOf(false));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMap));
        } catch (Exception var5) {
            this.log.error("发送json串异常", var5);
            throw new CIServiceException(var5);
        }
    }

    public void delCategoryInfo() throws Exception {
        boolean success = false;
        String msg = "";
        HashMap returnMsg = new HashMap();
        ArrayList enumCategoryIdList = new ArrayList();

        try {
            String[] response = this.enumCategoryIds.trim().split(",");
            if(response != null && response.length > 0) {
                for(int e = 0; e < response.length; ++e) {
                    enumCategoryIdList.add(response[e]);
                }
            }

            this.ciLabelInfoService.deleteCategoryInfo(enumCategoryIdList);
            success = true;
            msg = "删除成功";
        } catch (Exception var8) {
            success = false;
            msg = "删除失败";
            this.log.error("删除枚举维值分类失败", var8);
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

    public void uploadEnumListByImport() {
        Object result = new HashMap();
        String resultStr = "";
        boolean success = true;
        String msg = null;

        try {
            String e = this.getUserId();
            result = this.ciLabelInfoService.importEnumListFile(e, Integer.valueOf(this.columnId), this.enumCategoryId, this.file, this.fileFileName);
            msg = "导入成功";
        } catch (Exception var16) {
            this.log.error("上传文件解析异常", var16);
            success = false;
            msg = "上传文件数据类型错误，请检查上传文件！";
        } finally {
            ((Map)result).put("data", resultStr);
            ((Map)result).put("success", Boolean.valueOf(success));
            ((Map)result).put("msg", msg);
            HttpServletResponse response = this.getResponse();

            try {
                this.sendJson(response, JsonUtil.toJson(result));
            } catch (Exception var15) {
                this.log.error("数据传输异常！", var15);
                throw new CIServiceException();
            }
        }

    }

    public void findAllEnumData() {
        FileInputStream fileInputStream = null;
        String fileName = "label_dim_value.csv";
        String localFilePath = "";
        ServletOutputStream os = null;
        List datas = null;
        PrintWriter pw = null;
        FileOutputStream fos = null;

        try {
            datas = this.ciLabelInfoService.queryLabelAllDimValue(Integer.valueOf(this.columnId));
        } catch (Exception var40) {
            this.log.error("查询维值异常！", var40);
        }

        try {
            HttpServletResponse e = this.getResponse();
            HttpServletRequest request = this.getRequest();
            String mpmPath = Configure.getInstance().getProperty("SYS_COMMON_DOWN_PATH");
            if(!mpmPath.endsWith(File.separator)) {
                mpmPath = mpmPath + File.separator;
            }

            mpmPath = request.getSession().getServletContext().getRealPath("/") + File.separator + mpmPath;
            File pathFile = new File(mpmPath);
            if(pathFile.exists() || pathFile.mkdirs()) {
                localFilePath = mpmPath + fileName;
                fos = new FileOutputStream(localFilePath, false);
                OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
                byte[] bs = new byte[]{-17, -69, -65};
                fos.write(bs);
                fos.flush();
                pw = new PrintWriter(out);
                String fileNameEncode;
                if(datas != null) {
                    Iterator clientLanguage = datas.iterator();

                    while(clientLanguage.hasNext()) {
                        Map guessCharset = (Map)clientLanguage.next();
                        fileNameEncode = guessCharset.get("V_KEY").toString();
                        String bytesRead = guessCharset.get("V_NAME").toString();
                        pw.write((fileNameEncode + "," + bytesRead + "\r").toCharArray());
                    }
                } else {
                    pw.write("");
                }

                pw.flush();
                String clientLanguage1 = request.getHeader("Accept-Language");
                String guessCharset1 = ResponseEncodingUtil.getGuessCharset(clientLanguage1);
                fileNameEncode = ResponseEncodingUtil.encodingFileName(fileName, guessCharset1);
                e.addHeader("Content-Disposition", "attachment; filename=" + fileNameEncode);
                this.log.debug("offline download from web server================the file path is: " + localFilePath);
                e.setContentType("application/octet-stream;charset=" + guessCharset1);
                os = e.getOutputStream();
                fileInputStream = new FileInputStream(localFilePath);
                boolean bytesRead1 = false;
                byte[] buffer = new byte[1024];

                int bytesRead2;
                while((bytesRead2 = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                    os.write(buffer, 0, bytesRead2);
                }

                return;
            }
        } catch (Exception var41) {
            this.log.error("下载标签枚举值文件错误！", var41);
            return;
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException var39) {
                    var39.printStackTrace();
                }
            }

            if(os != null) {
                try {
                    os.close();
                    os = null;
                } catch (IOException var38) {
                    var38.printStackTrace();
                }
            }

            if(pw != null) {
                try {
                    pw.close();
                    pw = null;
                } catch (Exception var37) {
                    var37.printStackTrace();
                }
            }

        }

    }

    public String findAllLabelDimValueCategory() {
        try {
            if(!StringUtil.isEmpty(this.columnId)) {
                if(this.pager == null) {
                    this.pager = new Pager();
                }

                String e = PrivilegeServiceUtil.getUserId();
                long count = 0L;
                if(this.labelTypeId.intValue() == 8) {
                    count = this.ciLabelInfoService.queryLabelDimValueCountByColumnId(e, Integer.valueOf(this.columnId), this.dimName, this.enumCategoryId);
                } else {
                    count = this.ciLabelInfoService.queryLabelDimValueCount(e, Integer.valueOf(this.labelId), this.dimName, this.enumCategoryId);
                }

                this.pager.setTotalSize(count);
                int totalpage = (int)Math.ceil((double)this.pager.getTotalSize() / (double)this.pager.getPageSize());
                this.pager.setTotalPage(totalpage);
                this.pager = this.pager.pagerFlip();
                List list;
                if(this.labelTypeId.intValue() == 8) {
                    list = this.ciLabelInfoService.queryLabelDimValueByColumnId(Integer.valueOf(this.columnId), this.pager, this.dimName, e, this.enumCategoryId);
                    this.pager.setResult(list);
                } else {
                    list = this.ciLabelInfoService.queryLabelDimValue(Integer.valueOf(this.labelId), this.pager, this.dimName, e, this.enumCategoryId);
                    this.pager.setResult(list);
                }
            }
        } catch (Exception var6) {
            this.log.error("查询枚举所有标签异常", var6);
        }

        return "enumValueListView";
    }

    private void insertOrSubmitApprove(CiLabelInfo ciLabelInfo, int flag) {
        try {
            CiApproveStatus e = new CiApproveStatus();
            e.setResourceCreateUserId(ciLabelInfo.getCreateUserId());
            e.setDeptId(ciLabelInfo.getDeptId());
            e.setProcessId("1");
            e.setResourceId(Integer.toString(ciLabelInfo.getLabelId().intValue()));
            e.setResourceName(ciLabelInfo.getLabelName());
            this.ciApproveService.insertOrSubmitApprove(e, flag);
        } catch (Exception var4) {
            this.log.error("保存或提交审批时异常", var4);
        }

    }

    public String saveLabelInfoInit() throws Exception {
        CacheBase cache = CacheBase.getInstance();
        this.dimScenes = (List<DimScene>)cache.getObjectList("DIM_SCENE");
        if(this.labelId != null) {
            List sceneList = this.ciLabelInfoService.queryLabelSceneByLabelId(this.labelId);
            StringBuffer sceneSb = new StringBuffer();

            for(int i = 0; i < sceneList.size(); ++i) {
                CiLabelSceneRel item = (CiLabelSceneRel)sceneList.get(i);
                sceneSb.append(item.getId().getSceneId());
                if(i < sceneList.size() - 1) {
                    sceneSb.append(",");
                }
            }

            this.sceneIds = sceneSb.toString();
        }

        return "addLabel";
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public CiLabelInfo getCreateLabelInfo() {
        return this.createLabelInfo;
    }

    public void setCreateLabelInfo(CiLabelInfo createLabelInfo) {
        this.createLabelInfo = createLabelInfo;
    }

    public CiLabelInfo getSearchConditionInfo() {
        return this.searchConditionInfo;
    }

    public void setSearchConditionInfo(CiLabelInfo searchConditionInfo) {
        this.searchConditionInfo = searchConditionInfo;
    }

    public String getLabelIds() {
        return this.labelIds;
    }

    public void setLabelIds(String labelIds) {
        this.labelIds = labelIds;
    }

    public String getLabelId() {
        return this.labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public List<LabelDetailInfo> getLabelDetailList() {
        return this.labelDetailList;
    }

    public void setLabelDetailList(List<LabelDetailInfo> labelDetailList) {
        this.labelDetailList = labelDetailList;
    }

    public CiLabelInfo getCiLabelInfo() {
        return this.ciLabelInfo;
    }

    public void setCiLabelInfo(CiLabelInfo ciLabelInfo) {
        this.ciLabelInfo = ciLabelInfo;
    }

    public List<LabelUseLogInfo> getLabelUseLogInfo() {
        return this.labelUseLogInfo;
    }

    public void setLabelUseLogInfo(List<LabelUseLogInfo> labelUseLogInfo) {
        this.labelUseLogInfo = labelUseLogInfo;
    }

    public String getSearchType() {
        return this.searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public boolean isApprover() {
        return this.approver;
    }

    public void setApprover(boolean approver) {
        this.approver = approver;
    }

    public String getLabelName() {
        return this.labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getDimName() {
        return this.dimName;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName;
    }

    public List<LabelOperManager> getLabelOperManagerList() {
        return this.labelOperManagerList;
    }

    public void setLabelOperManagerList(List<LabelOperManager> labelOperManagerList) {
        this.labelOperManagerList = labelOperManagerList;
    }

    public String getColumnId() {
        return this.columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public int getRankingListType() {
        return this.rankingListType;
    }

    public void setRankingListType(int rankingListType) {
        this.rankingListType = rankingListType;
    }

    public String getShowType() {
        return this.showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
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

    public String getEnumCategoryId() {
        return this.enumCategoryId;
    }

    public void setEnumCategoryId(String enumCategoryId) {
        this.enumCategoryId = enumCategoryId;
    }

    public List<CiLabelInfo> getCiLabelInfoList() {
        return this.ciLabelInfoList;
    }

    public void setCiLabelInfoList(List<CiLabelInfo> ciLabelInfoList) {
        this.ciLabelInfoList = ciLabelInfoList;
    }

    public CiEnumCategoryInfo getCiEnumCategoryInfo() {
        return this.ciEnumCategoryInfo;
    }

    public void setCiEnumCategoryInfo(CiEnumCategoryInfo ciEnumCategoryInfo) {
        this.ciEnumCategoryInfo = ciEnumCategoryInfo;
    }

    public String getEnumCategoryIds() {
        return this.enumCategoryIds;
    }

    public void setEnumCategoryIds(String enumCategoryIds) {
        this.enumCategoryIds = enumCategoryIds;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileFileName() {
        return this.fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public String getEnumCategoryInfoId() {
        return this.enumCategoryInfoId;
    }

    public void setEnumCategoryInfoId(String enumCategoryInfoId) {
        this.enumCategoryInfoId = enumCategoryInfoId;
    }

    public Integer getLabelTypeId() {
        return this.labelTypeId;
    }

    public void setLabelTypeId(Integer labelTypeId) {
        this.labelTypeId = labelTypeId;
    }

    public List<DimScene> getDimScenes() {
        return this.dimScenes;
    }

    public void setDimScenes(List<DimScene> dimScenes) {
        this.dimScenes = dimScenes;
    }

    public String getSceneIds() {
        return this.sceneIds;
    }

    public void setSceneIds(String sceneIds) {
        this.sceneIds = sceneIds;
    }

    public int getIsSysRecom() {
        return this.IsSysRecom;
    }

    public void setIsSysRecom(int isSysRecom) {
        this.IsSysRecom = isSysRecom;
    }
}

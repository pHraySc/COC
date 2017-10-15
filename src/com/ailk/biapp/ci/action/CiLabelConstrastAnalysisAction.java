package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.LabelContrastDetailInfo;
import com.ailk.biapp.ci.model.LabelDetailInfo;
import com.ailk.biapp.ci.model.LabelShortInfo;
import com.ailk.biapp.ci.service.ICiLabelContrastAnalysisService;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICiLabelUserUseService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.service.ILabelQueryService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.CiUtil;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiLabelConstrastAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(this.getClass());
    private LabelShortInfo mainLabelInfo;
    private LabelDetailInfo labelDetailInfo;
    private List<LabelShortInfo> contrastLabelInfos;
    private LabelContrastDetailInfo contrastDetailInfo;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private String hasContrastLabel;
    @Autowired
    private ICiLabelContrastAnalysisService ciLabelContrastAnalysisService;
    @Autowired
    private ICustomersManagerService customersManagerService;
    @Autowired
    private ICiLabelUserUseService ciLabelUserUseService;
    @Autowired
    private ILabelQueryService labelQueryService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;

    public CiLabelConstrastAnalysisAction() {
    }

    public String initLabelContrastAnalysis() {
        String labelId = this.getRequest().getParameter("labelId");
        String dataDate = this.getRequest().getParameter("dataDate");
        CacheBase cache = CacheBase.getInstance();
        if(StringUtil.isEmpty(dataDate)) {
            dataDate = cache.getNewLabelMonth();
        }

        String userId = PrivilegeServiceUtil.getUserId();
        this.mainLabelInfo = new LabelShortInfo();
        this.mainLabelInfo.setDataDate(dataDate);
        this.mainLabelInfo.setLabelId(labelId);
        this.mainLabelInfo.setLabelName(cache.getEffectiveLabel(labelId).getLabelName());
        this.mainLabelInfo.setUpdateCycle(cache.getEffectiveLabel(labelId).getUpdateCycle().toString());
        Long mainCustomNum = this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(labelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, userId);
        this.mainLabelInfo.setCustomNum(mainCustomNum.toString());
        this.contrastLabelInfos = this.ciLabelContrastAnalysisService.queryCiUserLabelContrastList(userId, labelId, dataDate);
        this.hasContrastLabel = "false";
        if(null != this.contrastLabelInfos && this.contrastLabelInfos.size() > 0) {
            this.hasContrastLabel = "true";
            this.contrastDetailInfo = new LabelContrastDetailInfo();
            this.contrastDetailInfo.setMainLabelId(labelId);
            String day = CacheBase.getInstance().getNewLabelDay();
            Iterator contrastLabel = this.contrastLabelInfos.iterator();

            while(contrastLabel.hasNext()) {
                LabelShortInfo e = (LabelShortInfo)contrastLabel.next();
                String contrastLabelUserNum = e.getUpdateCycle();
                if(1 == Integer.valueOf(contrastLabelUserNum).intValue()) {
                    Long labelIntersectionNum = this.ciLabelInfoService.getCustomNum(day, Integer.valueOf(e.getLabelId()), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, userId);
                    e.setCustomNum(labelIntersectionNum.toString());
                }
            }

            LabelShortInfo contrastLabel1 = (LabelShortInfo)this.contrastLabelInfos.get(0);
            this.contrastDetailInfo.setContrastLabelId(contrastLabel1.getLabelId());
            this.contrastDetailInfo.setContrastLabelName(contrastLabel1.getLabelName());

            try {
                Integer e1 = Integer.valueOf(this.mainLabelInfo.getCustomNum());
                Integer contrastLabelUserNum1 = Integer.valueOf(contrastLabel1.getCustomNum());
                boolean labelIntersectionNum1 = false;
                int labelIntersectionNum2;
                if(e1.intValue() != 0 && contrastLabelUserNum1.intValue() != 0) {
                    List sumUserNum = this.getLabelIntersectionRules(labelId, contrastLabel1.getLabelId());
                    String labelUnionNum = this.customersManagerService.getCountSqlStr(sumUserNum, dataDate, (String)null, this.getUserId());
                    this.log.debug("labelIntersectionCountSql->" + labelUnionNum);
                    labelIntersectionNum2 = this.customersManagerService.queryCount(labelUnionNum);
                } else {
                    labelIntersectionNum2 = 0;
                }

                this.contrastDetailInfo.setIntersectionNum((new Integer(labelIntersectionNum2)).toString());
                Integer sumUserNum1 = Integer.valueOf(e1.intValue() + contrastLabelUserNum1.intValue());
                int labelUnionNum1 = sumUserNum1.intValue() - labelIntersectionNum2;
                this.contrastDetailInfo.setUnionNum((new Integer(labelUnionNum1)).toString());
                if(labelIntersectionNum2 == 0) {
                    this.contrastDetailInfo.setIntersectionRate("0.00%");
                } else {
                    this.contrastDetailInfo.setIntersectionRate(CiUtil.percentStr(labelIntersectionNum2, labelUnionNum1, "0.00%"));
                }

                byte contrastLabelDifferenceNum;
                int contrastLabelDifferenceNum1;
                if(e1.intValue() == 0) {
                    contrastLabelDifferenceNum = 0;
                    this.contrastDetailInfo.setMainDifferenceNum((new Integer(contrastLabelDifferenceNum)).toString());
                    this.contrastDetailInfo.setMainDifferenceRate("0.00%");
                } else {
                    contrastLabelDifferenceNum1 = e1.intValue() - labelIntersectionNum2;
                    this.contrastDetailInfo.setMainDifferenceNum((new Integer(contrastLabelDifferenceNum1)).toString());
                    this.contrastDetailInfo.setMainDifferenceRate(CiUtil.percentStr(contrastLabelDifferenceNum1, labelUnionNum1, "0.00%"));
                }

                if(contrastLabelUserNum1.intValue() == 0) {
                    contrastLabelDifferenceNum = 0;
                    this.contrastDetailInfo.setContrastDifferenceNum((new Integer(contrastLabelDifferenceNum)).toString());
                    this.contrastDetailInfo.setContrastDifferenceRate("0.00%");
                } else {
                    contrastLabelDifferenceNum1 = contrastLabelUserNum1.intValue() - labelIntersectionNum2;
                    this.contrastDetailInfo.setContrastDifferenceNum((new Integer(contrastLabelDifferenceNum1)).toString());
                    this.contrastDetailInfo.setContrastDifferenceRate(CiUtil.percentStr(contrastLabelDifferenceNum1, labelUnionNum1, "0.00%"));
                }

                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CONTRAST_ANALYSIS_VIEW", labelId, "", "标签分析->标签对比分析图查看成功,【标签Id:" + labelId + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var14) {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CONTRAST_ANALYSIS_VIEW", labelId, "", "标签分析->标签对比分析图查看失败,【标签Id:" + labelId + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                this.log.error("标签对比分析查询失败", var14);
            }

            this.log.debug("contrastDetailInfo->" + this.contrastDetailInfo);
        }

        return "success";
    }

    public String initLabelContrastSave() {
        String labelId = this.getRequest().getParameter("mainLabelId");
        String dataDate = this.getRequest().getParameter("dataDate");
        CacheBase cache = CacheBase.getInstance();
        if(StringUtil.isEmpty(dataDate)) {
            dataDate = cache.getNewLabelMonth();
        }

        String userId = PrivilegeServiceUtil.getUserId();
        this.mainLabelInfo = new LabelShortInfo();
        this.mainLabelInfo.setDataDate(dataDate);
        this.mainLabelInfo.setLabelId(labelId);
        this.mainLabelInfo.setLabelName(cache.getEffectiveLabel(labelId).getLabelName());
        this.mainLabelInfo.setUpdateCycle(cache.getEffectiveLabel(labelId).getUpdateCycle().toString());
        Long mainCustomNum = this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(labelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, userId);
        this.mainLabelInfo.setCustomNum(mainCustomNum.toString());
        this.contrastLabelInfos = this.ciLabelContrastAnalysisService.queryCiUserLabelContrastList(userId, labelId, dataDate);
        if(null != this.contrastLabelInfos && this.contrastLabelInfos.size() > 0) {
            this.contrastDetailInfo = new LabelContrastDetailInfo();
            this.contrastDetailInfo.setMainLabelId(labelId);
            String day = CacheBase.getInstance().getNewLabelDay();
            Iterator contrastLabel = this.contrastLabelInfos.iterator();

            while(contrastLabel.hasNext()) {
                LabelShortInfo e = (LabelShortInfo)contrastLabel.next();
                String labelIntersectionNum = e.getUpdateCycle();
                if(1 == Integer.valueOf(labelIntersectionNum).intValue()) {
                    Long sumUserNum = this.ciLabelInfoService.getCustomNum(day, Integer.valueOf(e.getLabelId()), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, userId);
                    e.setCustomNum(sumUserNum.toString());
                }
            }

            LabelShortInfo contrastLabel1 = (LabelShortInfo)this.contrastLabelInfos.get(0);
            this.contrastDetailInfo.setContrastLabelId(contrastLabel1.getLabelId());
            this.contrastDetailInfo.setContrastLabelName(contrastLabel1.getLabelName());

            try {
                Integer e1 = Integer.valueOf(contrastLabel1.getCustomNum());
                boolean labelIntersectionNum1 = false;
                int labelIntersectionNum2;
                if(mainCustomNum.intValue() != 0 && e1.intValue() != 0) {
                    List sumUserNum1 = this.getLabelIntersectionRules(labelId, contrastLabel1.getLabelId());
                    String labelUnionNum = this.customersManagerService.getCountSqlStr(sumUserNum1, dataDate, (String)null, this.getUserId());
                    this.log.debug("labelIntersectionCountSql->" + labelUnionNum);
                    labelIntersectionNum2 = this.customersManagerService.queryCount(labelUnionNum);
                } else {
                    labelIntersectionNum2 = 0;
                }

                this.contrastDetailInfo.setIntersectionNum((new Integer(labelIntersectionNum2)).toString());
                Integer sumUserNum2 = Integer.valueOf(mainCustomNum.intValue() + e1.intValue());
                int labelUnionNum1 = sumUserNum2.intValue() - labelIntersectionNum2;
                this.contrastDetailInfo.setUnionNum((new Integer(labelUnionNum1)).toString());
                if(labelIntersectionNum2 == 0) {
                    this.contrastDetailInfo.setIntersectionRate("0.00%");
                } else {
                    this.contrastDetailInfo.setIntersectionRate(CiUtil.percentStr(labelIntersectionNum2, labelUnionNum1, "0.00%"));
                }

                byte contrastLabelDifferenceNum;
                int contrastLabelDifferenceNum1;
                if(mainCustomNum.intValue() == 0) {
                    contrastLabelDifferenceNum = 0;
                    this.contrastDetailInfo.setMainDifferenceNum((new Integer(contrastLabelDifferenceNum)).toString());
                    this.contrastDetailInfo.setMainDifferenceRate("0.00%");
                } else {
                    contrastLabelDifferenceNum1 = mainCustomNum.intValue() - labelIntersectionNum2;
                    this.contrastDetailInfo.setMainDifferenceNum((new Integer(contrastLabelDifferenceNum1)).toString());
                    this.contrastDetailInfo.setMainDifferenceRate(CiUtil.percentStr(contrastLabelDifferenceNum1, labelUnionNum1, "0.00%"));
                }

                if(e1.intValue() == 0) {
                    contrastLabelDifferenceNum = 0;
                    this.contrastDetailInfo.setContrastDifferenceNum((new Integer(contrastLabelDifferenceNum)).toString());
                    this.contrastDetailInfo.setContrastDifferenceRate("0.00%");
                } else {
                    contrastLabelDifferenceNum1 = e1.intValue() - labelIntersectionNum2;
                    this.contrastDetailInfo.setContrastDifferenceNum((new Integer(contrastLabelDifferenceNum1)).toString());
                    this.contrastDetailInfo.setContrastDifferenceRate(CiUtil.percentStr(contrastLabelDifferenceNum1, labelUnionNum1, "0.00%"));
                }
            } catch (Exception var13) {
                this.log.error("标签对比分析查询失败", var13);
            }

            this.log.debug("contrastDetailInfo->" + this.contrastDetailInfo);
        }

        return "labelConstrastSave";
    }

    public void findLabelContrastDetail() {
        try {
            String e1 = this.getRequest().getParameter("mainLabelId");
            String contrastLabelId = this.getRequest().getParameter("contrastLabelId");
            String dataDate = this.getRequest().getParameter("dataDate");
            if(StringUtil.isEmpty(dataDate)) {
                dataDate = CacheBase.getInstance().getNewLabelMonth();
            }

            this.log.debug("Param->mainLabelId=" + e1 + ",contrastLabelId=" + contrastLabelId + ",dataDate->" + dataDate);
            boolean success = true;
            String message = "标签对比分析成功！";
            HashMap returnMsg = new HashMap();
            this.contrastDetailInfo = new LabelContrastDetailInfo();
            this.contrastDetailInfo.setMainLabelId(e1);
            this.contrastDetailInfo.setContrastLabelId(contrastLabelId);

            try {
                Integer e = Integer.valueOf(this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(e1), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, this.userId).intValue());
                Integer contrastLabelUserNum = Integer.valueOf(this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(contrastLabelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, this.userId).intValue());
                boolean labelIntersectionNum = false;
                int labelIntersectionNum1;
                if(e.intValue() != 0 && contrastLabelUserNum.intValue() != 0) {
                    List sumUserNum = this.getLabelIntersectionRules(e1, contrastLabelId);
                    String labelUnionNum = this.customersManagerService.getCountSqlStr(sumUserNum, dataDate, (String)null, this.getUserId());
                    this.log.debug("labelIntersectionCountSql->" + labelUnionNum);
                    labelIntersectionNum1 = this.customersManagerService.queryCount(labelUnionNum);
                } else {
                    labelIntersectionNum1 = 0;
                }

                this.contrastDetailInfo.setIntersectionNum((new Integer(labelIntersectionNum1)).toString());
                Integer sumUserNum1 = Integer.valueOf(e.intValue() + contrastLabelUserNum.intValue());
                int labelUnionNum1 = sumUserNum1.intValue() - labelIntersectionNum1;
                this.contrastDetailInfo.setUnionNum((new Integer(labelUnionNum1)).toString());
                if(labelIntersectionNum1 == 0) {
                    this.contrastDetailInfo.setIntersectionRate("0.00%");
                } else {
                    this.contrastDetailInfo.setIntersectionRate(CiUtil.percentStr(labelIntersectionNum1, labelUnionNum1, "0.00%"));
                }

                byte contrastLabelDifferenceNum;
                int contrastLabelDifferenceNum1;
                if(e.intValue() == 0) {
                    contrastLabelDifferenceNum = 0;
                    this.contrastDetailInfo.setMainDifferenceNum((new Integer(contrastLabelDifferenceNum)).toString());
                    this.contrastDetailInfo.setMainDifferenceRate("0.00%");
                } else {
                    contrastLabelDifferenceNum1 = e.intValue() - labelIntersectionNum1;
                    this.contrastDetailInfo.setMainDifferenceNum((new Integer(contrastLabelDifferenceNum1)).toString());
                    this.contrastDetailInfo.setMainDifferenceRate(CiUtil.percentStr(contrastLabelDifferenceNum1, labelUnionNum1, "0.00%"));
                }

                if(contrastLabelUserNum.intValue() == 0) {
                    contrastLabelDifferenceNum = 0;
                    this.contrastDetailInfo.setContrastDifferenceNum((new Integer(contrastLabelDifferenceNum)).toString());
                    this.contrastDetailInfo.setContrastDifferenceRate("0.00%");
                } else {
                    contrastLabelDifferenceNum1 = contrastLabelUserNum.intValue() - labelIntersectionNum1;
                    this.contrastDetailInfo.setContrastDifferenceNum((new Integer(contrastLabelDifferenceNum1)).toString());
                    this.contrastDetailInfo.setContrastDifferenceRate(CiUtil.percentStr(contrastLabelDifferenceNum1, labelUnionNum1, "0.00%"));
                }

                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CONTRAST_ANALYSIS", contrastLabelId, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId), "标签对比分析取数成功！,【主标签ID:" + e1 + ",主标签名称:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", e1) + ",对比标签ID:" + contrastLabelId + ",对比标签名称:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            } catch (Exception var15) {
                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CONTRAST_ANALYSIS", contrastLabelId, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId), "标签对比分析取数失败！,【主标签ID:" + e1 + ",主标签名称:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", e1) + ",对比标签ID:" + contrastLabelId + ",对比标签名称:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId) + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
                this.log.error("标签对比分析查询失败", var15);
                message = "标签对比分析查询失败";
                success = false;
            }

            this.ciLabelUserUseService.addLabelUserUseLog(contrastLabelId, 6);
            this.log.debug("contrastDetailInfo->" + this.contrastDetailInfo);
            HttpServletResponse response = this.getResponse();
            returnMsg.put("contrastDetailInfo", this.contrastDetailInfo);
            returnMsg.put("success", Boolean.valueOf(success));
            returnMsg.put("message", message);

            try {
                this.sendJson(response, JsonUtil.toJson(returnMsg));
            } catch (Exception var14) {
                this.log.error("发送json串异常", var14);
                throw new CIServiceException(var14);
            }
        } catch (CIServiceException var16) {
            this.log.error("", var16);
        }

    }

    public void delUserLabelContrast() {
        String mainLabelId = this.getRequest().getParameter("mainLabelId");
        String contrastLabelId = this.getRequest().getParameter("contrastLabelId");
        String userId = PrivilegeServiceUtil.getUserId();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "标签对比删除成功！";

        try {
            this.ciLabelContrastAnalysisService.deleteUserLabelContrast(userId, mainLabelId, contrastLabelId);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CONTRAST_DELETE", contrastLabelId, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId), "标签对比分析删除标签成功！,【主标签ID:" + mainLabelId + ",主标签名称:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", mainLabelId) + ",被删除的对比标签ID:" + contrastLabelId + ",被删除的对比标签名称:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var10) {
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CONTRAST_DELETE", contrastLabelId, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId), "标签对比分析取数失败！,【主标签ID:" + mainLabelId + ",主标签名称:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", mainLabelId) + ",被删除的对比标签ID:" + contrastLabelId + ",被删除的对比标签名称:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId) + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("标签对比删除异常", var10);
            message = "标签对比删除异常";
            success = false;
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

    public String initLabelConstrast() {
        String mainLabelId = this.getRequest().getParameter("mainLabelId");
        String dataDate = this.getRequest().getParameter("dataDate");
        if(StringUtil.isEmpty(dataDate)) {
            dataDate = CacheBase.getInstance().getNewLabelMonth();
        }

        this.log.debug("mainLabelId->" + mainLabelId + ",dataDate->" + dataDate);
        this.labelDetailInfo = this.labelQueryService.queryLabelDetailInfo(mainLabelId);
        this.labelDetailInfo.setDataDate(dataDate);
        String userNum = this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(mainLabelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, this.userId).toString();
        this.labelDetailInfo.setCustomNum(userNum);
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CONTRAST_ANALYSIS_LINK", mainLabelId, "", "标签分析->标签对比分析页查看成功,【标签Id:" + mainLabelId + "】", OperResultEnum.Success, LogLevelEnum.Normal);
        return "labelConstrast";
    }

    public String findLabelContrast() {
        String mainLabelId = this.getRequest().getParameter("mainLabelId");
        String dataDate = this.getRequest().getParameter("dataDate");
        this.mainLabelInfo = new LabelShortInfo();
        this.mainLabelInfo.setLabelId(mainLabelId);
        this.mainLabelInfo.setDataDate(dataDate);
        this.mainLabelInfo.setLabelName(CacheBase.getInstance().getEffectiveLabel(mainLabelId).getLabelName());
        this.mainLabelInfo.setUpdateCycle(CacheBase.getInstance().getEffectiveLabel(mainLabelId).getUpdateCycle().toString());
        Integer mainLabelUserNum = Integer.valueOf(this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(mainLabelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, this.userId).intValue());
        this.mainLabelInfo.setCustomNum(mainLabelUserNum.toString());
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "标签对比查询成功！";
        this.log.debug("MainLabelId->" + mainLabelId);

        try {
            String response = PrivilegeServiceUtil.getUserId();
            List e = this.ciLabelContrastAnalysisService.queryCiUserLabelContrastList(response, mainLabelId, dataDate);
            Iterator contrastLabelList = e.iterator();

            while(contrastLabelList.hasNext()) {
                LabelShortInfo i$ = (LabelShortInfo)contrastLabelList.next();
                String contrastLabel = i$.getUpdateCycle();
                if(contrastLabel.equals("1")) {
                    String contrastDetailInfo = this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(i$.getLabelId()), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, response).toString();
                    i$.setCustomNum(contrastDetailInfo);
                }
            }

            ArrayList contrastLabelList1 = new ArrayList();
            if(null != e && e.size() > 0) {
                LabelContrastDetailInfo contrastDetailInfo1;
                for(Iterator i$1 = e.iterator(); i$1.hasNext(); contrastLabelList1.add(contrastDetailInfo1)) {
                    LabelShortInfo contrastLabel1 = (LabelShortInfo)i$1.next();
                    contrastDetailInfo1 = new LabelContrastDetailInfo();
                    contrastDetailInfo1.setMainLabelId(mainLabelId);
                    contrastDetailInfo1.setContrastLabelId(contrastLabel1.getLabelId());
                    contrastDetailInfo1.setContrastLabelName(contrastLabel1.getLabelName());
                    Integer contrastLabelUserNum = Integer.valueOf(Integer.parseInt(contrastLabel1.getCustomNum()));
                    boolean labelIntersectionNum = false;
                    int labelIntersectionNum1;
                    if(mainLabelUserNum.intValue() != 0 && contrastLabelUserNum.intValue() != 0) {
                        List sumUserNum = this.getLabelIntersectionRules(mainLabelId, contrastLabel1.getLabelId());
                        String labelUnionNum = this.customersManagerService.getCountSqlStr(sumUserNum, dataDate, (String)null, this.getUserId());
                        this.log.debug("labelIntersectionCountSql->" + labelUnionNum);
                        labelIntersectionNum1 = this.customersManagerService.queryCount(labelUnionNum);
                    } else {
                        labelIntersectionNum1 = 0;
                    }

                    contrastDetailInfo1.setIntersectionNum((new Integer(labelIntersectionNum1)).toString());
                    Integer sumUserNum1 = Integer.valueOf(mainLabelUserNum.intValue() + contrastLabelUserNum.intValue());
                    int labelUnionNum1 = sumUserNum1.intValue() - labelIntersectionNum1;
                    contrastDetailInfo1.setUnionNum((new Integer(labelUnionNum1)).toString());
                    if(labelIntersectionNum1 == 0) {
                        contrastDetailInfo1.setIntersectionRate("0.00%");
                    } else {
                        contrastDetailInfo1.setIntersectionRate(CiUtil.percentStr(labelIntersectionNum1, labelUnionNum1, "0.00%"));
                    }

                    byte contrastLabelDifferenceNum;
                    int contrastLabelDifferenceNum1;
                    if(mainLabelUserNum.intValue() == 0) {
                        contrastLabelDifferenceNum = 0;
                        contrastDetailInfo1.setMainDifferenceNum((new Integer(contrastLabelDifferenceNum)).toString());
                        contrastDetailInfo1.setMainDifferenceRate("0.00%");
                    } else {
                        contrastLabelDifferenceNum1 = mainLabelUserNum.intValue() - labelIntersectionNum1;
                        contrastDetailInfo1.setMainDifferenceNum((new Integer(contrastLabelDifferenceNum1)).toString());
                        contrastDetailInfo1.setMainDifferenceRate(CiUtil.percentStr(contrastLabelDifferenceNum1, labelUnionNum1, "0.00%"));
                    }

                    if(contrastLabelUserNum.intValue() == 0) {
                        contrastLabelDifferenceNum = 0;
                        contrastDetailInfo1.setContrastDifferenceNum((new Integer(contrastLabelDifferenceNum)).toString());
                        contrastDetailInfo1.setContrastDifferenceRate("0.00%");
                    } else {
                        contrastLabelDifferenceNum1 = contrastLabelUserNum.intValue() - labelIntersectionNum1;
                        contrastDetailInfo1.setContrastDifferenceNum((new Integer(contrastLabelDifferenceNum1)).toString());
                        contrastDetailInfo1.setContrastDifferenceRate(CiUtil.percentStr(contrastLabelDifferenceNum1, labelUnionNum1, "0.00%"));
                    }
                }

                returnMsg.put("contrastLabelList", contrastLabelList1);
            }
        } catch (Exception var19) {
            this.log.error("标签对比分析查询失败", var19);
            message = "标签对比分析查询失败";
            success = false;
        }

        HttpServletResponse response1 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
            return null;
        } catch (Exception var18) {
            this.log.error("发送json串异常", var18);
            throw new CIServiceException(var18);
        }
    }

    public String findLabelContrastList() {
        String mainLabelId = this.getRequest().getParameter("mainLabelId");
        String dataDate = this.getRequest().getParameter("dataDate");
        String userId = PrivilegeServiceUtil.getUserId();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "标签对比查询成功！";

        try {
            List response = this.ciLabelContrastAnalysisService.queryCiUserLabelContrastList(userId, mainLabelId, dataDate);
            returnMsg.put("contrastLabelInfos", response);
        } catch (Exception var10) {
            this.log.error("标签对比分析查询失败", var10);
            message = "标签对比分析查询失败";
            success = false;
        }

        HttpServletResponse response1 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(response1, JsonUtil.toJson(returnMsg));
            return null;
        } catch (Exception var9) {
            this.log.error("发送json串异常", var9);
            throw new CIServiceException(var9);
        }
    }

    public String findLabelContrastTemp() {
        String mainLabelId = this.getRequest().getParameter("mainLabelId");
        String dataDate = this.getRequest().getParameter("dataDate");
        String contrastLables = this.getRequest().getParameter("contrastLabelstr");
        Integer mainLabelUserNum = Integer.valueOf(this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(mainLabelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, this.userId).intValue());
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "标签对比查询成功！";
        this.log.debug("MainLabelId->" + mainLabelId);

        try {
            if(StringUtil.isNotEmpty(contrastLables)) {
                String[] response = contrastLables.split(",");
                if(null != response && response.length > 0) {
                    ArrayList e = new ArrayList();

                    for(int i = 0; i < response.length; ++i) {
                        String contrastLabelId = response[i];
                        LabelContrastDetailInfo contrastDetailInfo = new LabelContrastDetailInfo();
                        contrastDetailInfo.setMainLabelId(mainLabelId);
                        contrastDetailInfo.setContrastLabelId(contrastLabelId);
                        contrastDetailInfo.setContrastLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId));
                        Integer contrastLabelUserNum = Integer.valueOf(this.ciLabelInfoService.getCustomNum(dataDate, Integer.valueOf(contrastLabelId), Integer.valueOf(-1), Integer.valueOf(-1), (Long)null, this.userId).intValue());
                        boolean labelIntersectionNum = false;
                        int var21;
                        if(mainLabelUserNum.intValue() != 0 && contrastLabelUserNum.intValue() != 0) {
                            List sumUserNum = this.getLabelIntersectionRules(mainLabelId, contrastLabelId);
                            String labelUnionNum = this.customersManagerService.getCountSqlStr(sumUserNum, dataDate, (String)null, this.getUserId());
                            this.log.debug("labelIntersectionCountSql->" + labelUnionNum);
                            var21 = this.customersManagerService.queryCount(labelUnionNum);
                        } else {
                            var21 = 0;
                        }

                        contrastDetailInfo.setIntersectionNum((new Integer(var21)).toString());
                        Integer var22 = Integer.valueOf(mainLabelUserNum.intValue() + contrastLabelUserNum.intValue());
                        int var23 = var22.intValue() - var21;
                        contrastDetailInfo.setUnionNum((new Integer(var23)).toString());
                        if(var21 == 0) {
                            contrastDetailInfo.setIntersectionRate("0.00%");
                        } else {
                            contrastDetailInfo.setIntersectionRate(CiUtil.percentStr(var21, var23, "0.00%"));
                        }

                        byte contrastLabelDifferenceNum;
                        int var24;
                        if(mainLabelUserNum.intValue() == 0) {
                            contrastLabelDifferenceNum = 0;
                            contrastDetailInfo.setMainDifferenceNum((new Integer(contrastLabelDifferenceNum)).toString());
                            contrastDetailInfo.setMainDifferenceRate("0.00%");
                        } else {
                            var24 = mainLabelUserNum.intValue() - var21;
                            contrastDetailInfo.setMainDifferenceNum((new Integer(var24)).toString());
                            contrastDetailInfo.setMainDifferenceRate(CiUtil.percentStr(var24, var23, "0.00%"));
                        }

                        if(contrastLabelUserNum.intValue() == 0) {
                            contrastLabelDifferenceNum = 0;
                            contrastDetailInfo.setContrastDifferenceNum((new Integer(contrastLabelDifferenceNum)).toString());
                            contrastDetailInfo.setContrastDifferenceRate("0.00%");
                        } else {
                            var24 = contrastLabelUserNum.intValue() - var21;
                            contrastDetailInfo.setContrastDifferenceNum((new Integer(var24)).toString());
                            contrastDetailInfo.setContrastDifferenceRate(CiUtil.percentStr(var24, var23, "0.00%"));
                        }

                        e.add(contrastDetailInfo);
                    }

                    returnMsg.put("contrastLabelList", e);
                }
            }
        } catch (Exception var19) {
            this.log.error("标签对比分析查询失败", var19);
            message = "标签对比分析查询失败";
            success = false;
        }

        HttpServletResponse var20 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(var20, JsonUtil.toJson(returnMsg));
            return null;
        } catch (Exception var18) {
            this.log.error("发送json串异常", var18);
            throw new CIServiceException(var18);
        }
    }

    public String findLabelContrastTempList() {
        String mainLabelId = this.getRequest().getParameter("mainLabelId");
        String contrastLables = this.getRequest().getParameter("contrastLabelstr");
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "标签对比查询成功！";
        this.log.debug("MainLabelId->" + mainLabelId);

        try {
            if(StringUtil.isNotEmpty(contrastLables)) {
                String[] response = contrastLables.split(",");
                if(null != response && response.length > 0) {
                    ArrayList e = new ArrayList();

                    for(int i = 0; i < response.length; ++i) {
                        String contrastLabelId = response[i];
                        LabelContrastDetailInfo contrastDetailInfo = new LabelContrastDetailInfo();
                        contrastDetailInfo.setMainLabelId(mainLabelId);
                        contrastDetailInfo.setContrastLabelId(contrastLabelId);
                        contrastDetailInfo.setContrastLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId));
                        e.add(contrastDetailInfo);
                    }

                    returnMsg.put("contrastLabelList", e);
                }
            }
        } catch (Exception var12) {
            this.log.error("标签对比分析查询失败", var12);
            message = "标签对比分析查询失败";
            success = false;
        }

        HttpServletResponse var13 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(var13, JsonUtil.toJson(returnMsg));
            return null;
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    public String saveLabelContrast() {
        String mainLabelId = this.getRequest().getParameter("mainLabelId");
        String contrastLables = this.getRequest().getParameter("contrastLabelstr");
        String userId = PrivilegeServiceUtil.getUserId();
        HashMap returnMsg = new HashMap();
        boolean success = true;
        String message = "标签对比保存成功！";
        String str = "";

        try {
            if(StringUtil.isNotEmpty(contrastLables)) {
                String[] response = contrastLables.split(",");
                if(null != response && response.length > 0) {
                    this.ciLabelContrastAnalysisService.deleteCiUserLabelContrast(userId, mainLabelId);

                    for(int e = 0; e < response.length; ++e) {
                        String contrastLabelId = response[e];
                        this.log.debug(contrastLabelId);
                        this.ciLabelContrastAnalysisService.addCiUserLabelContrast(userId, mainLabelId, contrastLabelId);
                        str = str + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", response[e]) + ",";
                    }
                }

                CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CONTRAST_SAVE", mainLabelId, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", mainLabelId), "标签对比分析，保存关系成功！,【主标签ID:" + mainLabelId + ",主标签名称:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", mainLabelId) + ",对比标签ID:" + contrastLables + ",对比标签名称:" + str + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            }
        } catch (Exception var12) {
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CONTRAST_SAVE", mainLabelId, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", mainLabelId), "标签对比分析，保存关系失败！,【主标签ID:" + mainLabelId + ",主标签名称:" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", mainLabelId) + ",对比标签ID:" + contrastLables + ",对比标签名称:" + str + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            this.log.error("标签对比保存异常", var12);
            message = "标签对比保存异常";
            success = false;
        }

        HttpServletResponse var13 = this.getResponse();
        returnMsg.put("success", Boolean.valueOf(success));
        returnMsg.put("message", message);

        try {
            this.sendJson(var13, JsonUtil.toJson(returnMsg));
            return null;
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    public void saveLabelContrastCustomGroup() throws Exception {
        HashMap returnMap = new HashMap();
        String type = this.getRequest().getParameter("type");
        String userId = PrivilegeServiceUtil.getUserId();
        this.ciCustomGroupInfo.setCustomGroupName(this.ciCustomGroupInfo.getCustomGroupName());
        this.ciCustomGroupInfo.setCustomGroupDesc(this.ciCustomGroupInfo.getCustomGroupDesc());
        this.log.debug("客户群名称：" + this.ciCustomGroupInfo.getCustomGroupName());
        this.ciCustomGroupInfo.setUpdateCycle(Integer.valueOf(1));
        this.ciCustomGroupInfo.setCreateTypeId(Integer.valueOf(3));
        this.ciCustomGroupInfo.setCreateUserId(userId);
        this.ciCustomGroupInfo.setStatus(Integer.valueOf(1));
        this.ciCustomGroupInfo.setDataStatus(Integer.valueOf(1));
        String mainLabelId = this.contrastDetailInfo.getMainLabelId();
        String contrastLabelId = this.contrastDetailInfo.getContrastLabelId();
        List ciLabelRuleList = null;
        String optRule = "";

        try {
            if(type.equals("intersection")) {
                ciLabelRuleList = this.getLabelIntersectionRules(mainLabelId, contrastLabelId);
                optRule = IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", mainLabelId) + " 且 " + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId);
            }

            if(type.equals("mainDifference")) {
                ciLabelRuleList = this.getLabelDifferenceRules(mainLabelId, contrastLabelId);
                optRule = IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", mainLabelId) + " 且  (非  " + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId) + ")";
            }

            if(type.equals("contrastDifference")) {
                ciLabelRuleList = this.getLabelDifferenceRules(contrastLabelId, mainLabelId);
                optRule = "(非  " + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", mainLabelId) + " ) 且 " + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", contrastLabelId);
            }

            if(null != ciLabelRuleList) {
                this.ciCustomGroupInfo.setLabelOptRuleShow(optRule);
                this.customersManagerService.addCiCustomGroupInfo(this.ciCustomGroupInfo, ciLabelRuleList, (List)null, (CiTemplateInfo)null, userId, false, (List)null);
                this.ciLabelUserUseService.addLabelUserUseLog(mainLabelId, 2);
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_CUS_ANALYSIS", this.ciCustomGroupInfo.getCustomGroupId(), this.ciCustomGroupInfo.getCustomGroupName(), "标签对比分析，保存客户群成功！【客户群ID:" + this.ciCustomGroupInfo.getCustomGroupId() + ",客户群名称:" + this.ciCustomGroupInfo.getCustomGroupName() + "客户群描述:" + this.ciCustomGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
                returnMap.put("cmsg", "保存客户群成功");
                returnMap.put("success", Boolean.valueOf(true));
            } else {
                returnMap.put("cmsg", "保存客户群失败");
                returnMap.put("success", Boolean.valueOf(false));
            }
        } catch (Exception var12) {
            Log.error("保存客户群失败", var12);
            returnMap.put("cmsg", "创建客户群失败");
            returnMap.put("success", Boolean.valueOf(false));
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_LABEL_ANALYSIS", "-1", this.ciCustomGroupInfo.getCustomGroupName(), "标签对比分析，保存客户群失败！【客户群ID:" + this.ciCustomGroupInfo.getCustomGroupId() + ",客户群名称:" + this.ciCustomGroupInfo.getCustomGroupName() + "客户群描述:" + this.ciCustomGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        HttpServletResponse response = this.getResponse();
        String customerName = this.ciCustomGroupInfo.getCustomGroupName();
        returnMap.put("customerName", customerName);

        try {
            this.sendJson(response, JsonUtil.toJson(returnMap));
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    private List<CiLabelRule> getLabelIntersectionRules(String mainLabelId, String contrastLabelId) {
        ArrayList ruleList = new ArrayList();
        CiLabelInfo ciLabelInfoMain = CacheBase.getInstance().getEffectiveLabel(mainLabelId);
        CiLabelInfo c1LabelInfoContrast = CacheBase.getInstance().getEffectiveLabel(contrastLabelId);
        CiLabelRule rule1 = new CiLabelRule();
        rule1.setSortNum(new Long(1L));
        rule1.setElementType(Integer.valueOf(2));
        rule1.setCalcuElement(mainLabelId);
        rule1.setMaxVal(new Double("1"));
        rule1.setMinVal(new Double("0"));
        rule1.setLabelFlag(new Integer("1"));
        rule1.setAttrVal(ciLabelInfoMain.getCiLabelExtInfo().getAttrVal());
        ruleList.add(rule1);
        CiLabelRule rule2 = new CiLabelRule();
        rule2.setSortNum(new Long(2L));
        rule2.setElementType(Integer.valueOf(1));
        rule2.setCalcuElement("and");
        ruleList.add(rule2);
        CiLabelRule rule3 = new CiLabelRule();
        rule3.setSortNum(new Long(3L));
        rule3.setElementType(Integer.valueOf(2));
        rule3.setCalcuElement(contrastLabelId);
        rule3.setMaxVal(new Double("1"));
        rule3.setMinVal(new Double("0"));
        rule3.setLabelFlag(new Integer("1"));
        rule3.setAttrVal(c1LabelInfoContrast.getCiLabelExtInfo().getAttrVal());
        ruleList.add(rule3);
        return ruleList;
    }

    private List<CiLabelRule> getLabelDifferenceRules(String mainLabelId, String contrastLabelId) {
        ArrayList ruleList = new ArrayList();
        CiLabelInfo ciLabelInfoMain = CacheBase.getInstance().getEffectiveLabel(mainLabelId);
        CiLabelInfo c1LabelInfoContrast = CacheBase.getInstance().getEffectiveLabel(contrastLabelId);
        CiLabelRule rule1 = new CiLabelRule();
        rule1.setSortNum(new Long(1L));
        rule1.setElementType(Integer.valueOf(2));
        rule1.setCalcuElement(mainLabelId);
        rule1.setMaxVal(new Double("1"));
        rule1.setMinVal(new Double("0"));
        rule1.setLabelFlag(new Integer("1"));
        rule1.setAttrVal(ciLabelInfoMain.getCiLabelExtInfo().getAttrVal());
        ruleList.add(rule1);
        CiLabelRule rule2 = new CiLabelRule();
        rule2.setSortNum(new Long(2L));
        rule2.setElementType(Integer.valueOf(1));
        rule2.setCalcuElement("and");
        ruleList.add(rule2);
        CiLabelRule rule3 = new CiLabelRule();
        rule3.setSortNum(new Long(3L));
        rule3.setElementType(Integer.valueOf(2));
        rule3.setCalcuElement(contrastLabelId);
        rule3.setMaxVal(new Double("1"));
        rule3.setMinVal(new Double("0"));
        rule3.setLabelFlag(new Integer("0"));
        rule3.setAttrVal(c1LabelInfoContrast.getCiLabelExtInfo().getAttrVal());
        ruleList.add(rule3);
        return ruleList;
    }

    public LabelShortInfo getMainLabelInfo() {
        return this.mainLabelInfo;
    }

    public void setMainLabelInfo(LabelShortInfo mainLabelInfo) {
        this.mainLabelInfo = mainLabelInfo;
    }

    public List<LabelShortInfo> getContrastLabelInfos() {
        return this.contrastLabelInfos;
    }

    public void setContrastLabelInfos(List<LabelShortInfo> contrastLabelInfos) {
        this.contrastLabelInfos = contrastLabelInfos;
    }

    public LabelContrastDetailInfo getContrastDetailInfo() {
        return this.contrastDetailInfo;
    }

    public void setContrastDetailInfo(LabelContrastDetailInfo contrastDetailInfo) {
        this.contrastDetailInfo = contrastDetailInfo;
    }

    public LabelDetailInfo getLabelDetailInfo() {
        return this.labelDetailInfo;
    }

    public void setLabelDetailInfo(LabelDetailInfo labelDetailInfo) {
        this.labelDetailInfo = labelDetailInfo;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public String getHasContrastLabel() {
        return this.hasContrastLabel;
    }

    public void setHasContrastLabel(String hasContrastLabel) {
        this.hasContrastLabel = hasContrastLabel;
    }
}

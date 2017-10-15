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
import com.ailk.biapp.ci.model.CiCustomRelModel;
import com.ailk.biapp.ci.service.ICustomersAnalysisService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CustomersRelAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CustomersRelAnalysisAction.class);
    private CiCustomGroupInfo ciCustomGroupInfo;
    private CiCustomRelModel ciCustomRelModel;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICustomersAnalysisService customersAnalysisService;

    public CustomersRelAnalysisAction() {
    }

    public String customGroupRel() {
        String userId = this.getUserId();
        byte noCustomRel = 0;
        List resultList = this.customersAnalysisService.queryRelCustom(userId, this.ciCustomRelModel);
        if(resultList != null && resultList.size() > 0) {
            noCustomRel = 1;
        }

        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomRelModel.getCustomGroupId());
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_REL_ANALYSIS_VIEW", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群关联分析【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        ActionContext ctx = ActionContext.getContext();
        ctx.put("noCustomRel", Integer.valueOf(noCustomRel));
        return "customGroupRel";
    }

    public String customRelMain() {
        return "customRelMain";
    }

    public void initRel() {
        boolean success = false;
        String userId = this.getUserId();
        List resultList = null;
        HashMap result = new HashMap();

        try {
            resultList = this.customersAnalysisService.queryRelCustom(userId, this.ciCustomRelModel);
            result.put("ciCustomRelList", resultList);
            success = true;
            result.put("success", Boolean.valueOf(success));
        } catch (Exception var8) {
            String e = "获得关联标签与主标签客户群交集用户数失败";
            this.log.error(e, var8);
            success = false;
            result.put("message", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(result));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public String customGroupRelAnalysis() {
        String fromPageFlag = this.getRequest().getParameter("fromPageFlag");
        String COC_CUS_REL_ANALYSIS_LINK = "";
        if("1".equals(fromPageFlag)) {
            COC_CUS_REL_ANALYSIS_LINK = "COC_CUS_REL_ANALYSIS_LINK_INDEX";
        } else {
            COC_CUS_REL_ANALYSIS_LINK = "COC_CUS_REL_ANALYSIS_LINK";
        }

        String customGroupId = this.ciCustomRelModel.getCustomGroupId();
        if(StringUtils.isBlank(this.ciCustomRelModel.getDataDate())) {
            this.ciCustomRelModel.setDataDate(CacheBase.getInstance().getNewLabelMonth());
        }

        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        this.ciCustomRelModel.setCustomGroupName(customGroupInfo.getCustomGroupName());
        CiCustomListInfo customListInfo = this.customersService.queryCiCustomListInfoByCGroupIdAndDataDate(customGroupId, this.ciCustomRelModel.getDataDate());
        if(customListInfo != null) {
            this.ciCustomRelModel.setListTableName(customListInfo.getListTableName());
            if(customListInfo.getCustomNum() != null) {
                this.ciCustomRelModel.setMainCustomUserNum(Integer.valueOf(customListInfo.getCustomNum().intValue()));
            } else {
                this.ciCustomRelModel.setMainCustomUserNum(Integer.valueOf(0));
            }
        }

        CILogServiceUtil.getLogServiceInstance().log(COC_CUS_REL_ANALYSIS_LINK, customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群关联分析页【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Normal);
        return "customGroupRelAnalysis";
    }

    public void findRelCustom() throws Exception {
        boolean success = false;
        HashMap result = new HashMap();

        try {
            String response = this.getUserId();
            List e1 = this.customersAnalysisService.queryRelCustom(response, this.ciCustomRelModel);
            success = true;
            result.put("success", Boolean.valueOf(success));
            result.put("ciCustomRelModelList", e1);
        } catch (Exception var6) {
            String e = "获得关联标签与主标签客户群交集用户数失败";
            this.log.error(e, var6);
            success = false;
            result.put("message", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response1 = this.getResponse();

        try {
            this.sendJson(response1, JsonUtil.toJson(result));
        } catch (Exception var5) {
            this.log.error("发送json串异常", var5);
            throw new CIServiceException(var5);
        }
    }

    public void findRelCustomGroupData() throws Exception {
        boolean success = false;
        HashMap result = new HashMap();
        String dataDate = this.ciCustomRelModel.getDataDate();
        String relLabelId = String.valueOf(this.ciCustomRelModel.getRelLabelId());
        String customGroupId = String.valueOf(this.ciCustomRelModel.getCustomGroupId());
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        String tableName = this.ciCustomRelModel.getListTableName();

        String e;
        try {
            if(StringUtils.isNotBlank(tableName)) {
                success = true;
                List response = this.getLabelRuleList(relLabelId);
                e = this.getNewLabelDay();
                String fromSql = this.customersService.getWithColumnSqlStr(response, dataDate, e, this.getUserId(), (List)null, (Integer)null, (Integer)null);
                this.customersAnalysisService.queryRelAnalysis(fromSql, tableName, this.ciCustomRelModel);
                result.put("ciCustomRelModel", this.ciCustomRelModel);
                result.put("success", Boolean.valueOf(success));
                CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_REL_ANALYSIS", relLabelId, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", relLabelId), "主标签客户群与关联标签的分析取数成功,【主标签客户群ID：" + customGroupId + ",主标签客户群名称：" + customGroupInfo.getCustomGroupName() + ",关联标签ID：" + relLabelId + ",关联标签名称：" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", relLabelId), OperResultEnum.Success, LogLevelEnum.Medium);
            } else {
                String response1 = "客户群没有清单数据";
                result.put("message", response1);
                result.put("success", Boolean.valueOf(success));
            }
        } catch (Exception var12) {
            e = "获得关联标签与主标签客户群交集用户数失败";
            this.log.error(e, var12);
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_REL_ANALYSIS", relLabelId, IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", relLabelId), "主标签客户群与关联标签的分析取数失败,【主标签客户群ID：" + customGroupId + ",主标签客户群名称：" + customGroupInfo.getCustomGroupName() + ",关联标签ID：" + relLabelId + ",关联标签名称：" + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", relLabelId), OperResultEnum.Failure, LogLevelEnum.Medium);
            success = false;
            result.put("message", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse response2 = this.getResponse();

        try {
            this.sendJson(response2, JsonUtil.toJson(result));
        } catch (Exception var11) {
            this.log.error("发送json串异常", var11);
            throw new CIServiceException(var11);
        }
    }

    public void findRelCustomGroupDataByDate() throws Exception {
        boolean success = false;
        String listTableName = this.ciCustomRelModel.getListTableName();
        Integer mainCustomUserNum = this.ciCustomRelModel.getMainCustomUserNum();
        String dataDate = this.ciCustomRelModel.getDataDate();
        String relLabelIds = this.ciCustomRelModel.getRelLabelIds();
        ArrayList customRelModelList = new ArrayList();
        HashMap result = new HashMap();

        try {
            if(StringUtils.isNotBlank(listTableName)) {
                success = true;
                String[] response = relLabelIds.split(",");
                String[] var21 = response;
                int len$ = response.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    String relLabelId = var21[i$];
                    CiCustomRelModel relMode = new CiCustomRelModel();
                    relMode.setRelLabelId(Integer.valueOf(relLabelId));
                    relMode.setMainCustomUserNum(mainCustomUserNum);
                    List ciLabelRuleList = this.getLabelRuleList(relLabelId);
                    String lastDate = this.getNewLabelDay();
                    String fromSql = this.customersService.getWithColumnSqlStr(ciLabelRuleList, dataDate, lastDate, this.getUserId(), (List)null, (Integer)null, (Integer)null);
                    this.customersAnalysisService.queryRelAnalysis(fromSql, listTableName, relMode);
                    customRelModelList.add(relMode);
                }

                result.put("customRelModelList", customRelModelList);
                result.put("success", Boolean.valueOf(success));
            } else {
                String var19 = "客户群没有清单数据";
                result.put("message", var19);
                result.put("success", Boolean.valueOf(success));
            }
        } catch (Exception var18) {
            String e = "获得关联标签与主标签客户群交集用户数失败";
            this.log.error(e, var18);
            success = false;
            result.put("message", e);
            result.put("success", Boolean.valueOf(success));
        }

        HttpServletResponse var20 = this.getResponse();

        try {
            this.sendJson(var20, JsonUtil.toJson(result));
        } catch (Exception var17) {
            this.log.error("发送json串异常", var17);
            throw new CIServiceException(var17);
        }
    }

    public String saveUserRelLabel() throws Exception {
        String userId = this.getUserId();
        String customGroupId = this.ciCustomRelModel.getCustomGroupId();
        String labelIds = this.ciCustomRelModel.getRelLabelIds();
        String dataDate = this.ciCustomRelModel.getDataDate();
        int customNum = this.ciCustomRelModel.getMainCustomUserNum().intValue();
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        this.ciCustomRelModel.setCustomGroupName(customGroupInfo.getCustomGroupName());
        this.customersAnalysisService.saveAssociationAnalysis(customGroupId, labelIds, userId, dataDate);
        String relLabelIdStr = "";
        ArrayList customRelList = new ArrayList();

        try {
            String[] e = StringUtil.isEmpty(this.ciCustomRelModel.getRelLabelIds())?null:this.ciCustomRelModel.getRelLabelIds().split(",");
            String[] overlapUserNum = StringUtil.isEmpty(this.ciCustomRelModel.getOverlapUserNumStr())?null:this.ciCustomRelModel.getOverlapUserNumStr().split(",");
            if(null != e && e.length > 0) {
                for(int random = 0; random < e.length; ++random) {
                    CiCustomRelModel firstAngle = new CiCustomRelModel();
                    firstAngle.setCustomGroupId(customGroupId);
                    firstAngle.setMainCustomUserNum(Integer.valueOf(customNum));
                    firstAngle.setDataDate(dataDate);
                    firstAngle.setRelLabelId(Integer.valueOf(e[random]));
                    firstAngle.setRelLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", firstAngle.getRelLabelId()));
                    firstAngle.setOverlapUserNum(Integer.valueOf(overlapUserNum[random]));
                    customRelList.add(firstAngle);
                    relLabelIdStr = relLabelIdStr + IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", e[random]) + ",";
                }
            }

            Random var21 = new Random();
            Double var22 = Double.valueOf(0.0D);
            Double angle = Double.valueOf(0.0D);
            ArrayList _40PerRelList = new ArrayList();
            ArrayList _70PerRelList = new ArrayList();
            ArrayList _100PerRelList = new ArrayList();

            for(int ctx = 0; ctx < customRelList.size(); ++ctx) {
                CiCustomRelModel ciLabelRelModel = (CiCustomRelModel)customRelList.get(ctx);
                Double proportion = Double.valueOf(ciLabelRelModel.getProportion());
                if(ctx == 0) {
                    var22 = angle = Double.valueOf(6.283185307179586D * var21.nextDouble());
                } else {
                    angle = Double.valueOf(var22.doubleValue() + 6.283185307179586D / (double)customRelList.size() * (double)ctx);
                }

                if(proportion.doubleValue() <= 40.0D) {
                    ciLabelRelModel.setRadius(Integer.valueOf(50));
                    this.setCoordinate(ciLabelRelModel, Double.valueOf(0.4D), angle);
                    _40PerRelList.add(ciLabelRelModel);
                }

                if(proportion.doubleValue() <= 70.0D && proportion.doubleValue() > 40.0D) {
                    ciLabelRelModel.setRadius(Integer.valueOf(50));
                    this.setCoordinate(ciLabelRelModel, Double.valueOf(0.6D), angle);
                    _70PerRelList.add(ciLabelRelModel);
                }

                if(proportion.doubleValue() <= 100.0D && proportion.doubleValue() > 70.0D) {
                    ciLabelRelModel.setRadius(Integer.valueOf(50));
                    this.setCoordinate(ciLabelRelModel, Double.valueOf(0.8D), angle);
                    _100PerRelList.add(ciLabelRelModel);
                }
            }

            ActionContext var23 = ActionContext.getContext();
            var23.put("_40PerRelList", _40PerRelList);
            var23.put("_70PerRelList", _70PerRelList);
            var23.put("_100PerRelList", _100PerRelList);
            var23.put("customRelList", customRelList);
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_REL_SAVE", customGroupId, customGroupInfo.getCustomGroupName(), "保存客户群与关联标签的关联关系成功,【客户群ID：" + customGroupId + ",客户群名称：" + customGroupInfo.getCustomGroupName() + ",关联标签ID：" + labelIds + ",关联标签名称：" + relLabelIdStr, OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var20) {
            this.log.error(var20);
            var20.printStackTrace();
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_REL_SAVE", customGroupId, customGroupInfo.getCustomGroupName(), "保存客户群与关联标签的关联关系失败,【客户群ID：" + customGroupId + ",客户群名称：" + customGroupInfo.getCustomGroupName() + ",关联标签ID：" + labelIds + ",关联标签名称：" + relLabelIdStr, OperResultEnum.Failure, LogLevelEnum.Medium);
        }

        return "relCustomByCustomGroup";
    }

    public String customRelChar() throws Exception {
        String customGroupId = this.ciCustomRelModel.getCustomGroupId();
        String dataDate = this.ciCustomRelModel.getDataDate();
        String userId = this.getUserId();
        Random random = new Random();
        Double firstAngle = Double.valueOf(0.0D);
        Double angle = Double.valueOf(0.0D);
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(customGroupId);
        CiCustomListInfo customListInfo = this.customersService.queryCiCustomListInfoByCGroupIdAndDataDate(customGroupId, dataDate);
        this.ciCustomRelModel.setCustomGroupName(customGroupInfo.getCustomGroupName());
        String listTableName = "";
        if(customListInfo != null) {
            listTableName = customListInfo.getListTableName();
            this.ciCustomRelModel.setMainCustomUserNum(Integer.valueOf(customListInfo.getCustomNum().intValue()));
        } else {
            this.ciCustomRelModel.setMainCustomUserNum(Integer.valueOf(0));
        }

        ArrayList ciCustomRelList = new ArrayList();
        List resultList = this.customersAnalysisService.queryRelCustom(userId, this.ciCustomRelModel);
        Iterator _40PerRelList = resultList.iterator();

        while(_40PerRelList.hasNext()) {
            CiCustomRelModel _70PerRelList = (CiCustomRelModel)_40PerRelList.next();
            List _100PerRelList = this.getLabelRuleList(_70PerRelList.getRelLabelId() + "");
            String ctx = this.getNewLabelDay();
            String ciCustomRelModel = this.customersService.getWithColumnSqlStr(_100PerRelList, dataDate, ctx, this.getUserId(), (List)null, (Integer)null, (Integer)null);
            CiCustomRelModel proportion = new CiCustomRelModel();
            proportion.setOverlapUserNum(Integer.valueOf(this.customersAnalysisService.queryAssociationAnalysis(ciCustomRelModel, listTableName)));
            proportion.setRelLabelId(_70PerRelList.getRelLabelId());
            proportion.setRelLabelName(IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", proportion.getRelLabelId()));
            proportion.setCustomGroupId(_70PerRelList.getCustomGroupId());
            proportion.setMainCustomUserNum(Integer.valueOf(customListInfo.getCustomNum().intValue()));
            proportion.setCustomGroupName(customGroupInfo.getCustomGroupName());
            proportion.setDataDate(this.ciCustomRelModel.getDataDate());
            ciCustomRelList.add(proportion);
        }

        ArrayList var18 = new ArrayList();
        ArrayList var19 = new ArrayList();
        ArrayList var20 = new ArrayList();

        for(int var21 = 0; var21 < ciCustomRelList.size(); ++var21) {
            CiCustomRelModel var23 = (CiCustomRelModel)ciCustomRelList.get(var21);
            Double var24 = Double.valueOf(var23.getProportion());
            if(var21 == 0) {
                firstAngle = angle = Double.valueOf(6.283185307179586D * random.nextDouble());
            } else {
                angle = Double.valueOf(firstAngle.doubleValue() + 6.283185307179586D / (double)ciCustomRelList.size() * (double)var21);
            }

            if(var24.doubleValue() <= 40.0D) {
                var23.setRadius(Integer.valueOf(50));
                this.setCoordinate(var23, Double.valueOf(0.4D), angle);
                var18.add(var23);
            }

            if(var24.doubleValue() <= 70.0D && var24.doubleValue() > 40.0D) {
                var23.setRadius(Integer.valueOf(50));
                this.setCoordinate(var23, Double.valueOf(0.6D), angle);
                var19.add(var23);
            }

            if(var24.doubleValue() <= 100.0D && var24.doubleValue() > 70.0D) {
                var23.setRadius(Integer.valueOf(50));
                this.setCoordinate(var23, Double.valueOf(0.8D), angle);
                var20.add(var23);
            }
        }

        ActionContext var22 = ActionContext.getContext();
        var22.put("_40PerRelList", var18);
        var22.put("_70PerRelList", var19);
        var22.put("_100PerRelList", var20);
        var22.put("customGroupRelList", ciCustomRelList);
        return "customRelChar";
    }

    public void saveRelLabelAsCustomGroup() throws Exception {
        String userId = this.getUserId();
        HashMap returnMap = new HashMap();
        this.ciCustomGroupInfo.setStatus(Integer.valueOf(1));
        this.ciCustomGroupInfo.setCustomGroupName(URLDecoder.decode(URLDecoder.decode(this.ciCustomGroupInfo.getCustomGroupName(), "UTF-8"), "UTF-8"));
        this.ciCustomGroupInfo.setCustomGroupDesc(URLDecoder.decode(URLDecoder.decode(this.ciCustomGroupInfo.getCustomGroupDesc(), "UTF-8"), "UTF-8"));
        this.ciCustomGroupInfo.setCreateTypeId(Integer.valueOf(5));
        String relLabelId = String.valueOf(this.ciCustomRelModel.getRelLabelId());
        String listTableName = this.ciCustomRelModel.getListTableName();
        ArrayList ciCustomSourceRelList = new ArrayList();
        List ciLabelRuleList = this.getLabelRuleList(relLabelId);
        String optRule = "";
        String labelName = IdToName.getLabelName("ALL_EFFECTIVE_LABEL_MAP", relLabelId);
        CiCustomSourceRel customSourceRel = new CiCustomSourceRel();
        customSourceRel.setElementType(Integer.valueOf(1));
        customSourceRel.setCalcuElement(listTableName);
        CiCustomSourceRel customSourceRelAnd = new CiCustomSourceRel();
        customSourceRelAnd.setElementType(Integer.valueOf(0));
        customSourceRelAnd.setCalcuElement("AND");
        ciCustomSourceRelList.add(customSourceRel);
        ciCustomSourceRelList.add(customSourceRelAnd);
        optRule = this.ciCustomGroupInfo.getCustomGroupName() + "且" + labelName;
        this.ciCustomGroupInfo.setCustomOptRuleShow(optRule);
        this.ciCustomGroupInfo.setUpdateCycle(Integer.valueOf(1));

        try {
            this.customersService.addCiCustomGroupInfo(this.ciCustomGroupInfo, ciLabelRuleList, ciCustomSourceRelList, (CiTemplateInfo)null, userId, false, (List)null);
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_CUS_ANALYSIS", this.ciCustomGroupInfo.getCustomGroupId().toString(), this.ciCustomGroupInfo.getCustomGroupName().toString(), "客户群分析->关联分析保存成功,【客户群名称 :" + this.ciCustomGroupInfo.getCustomGroupName() + ",客户群描述:" + this.ciCustomGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            returnMap.put("success", Boolean.valueOf(true));
        } catch (Exception var14) {
            this.log.error("保存客户群分析->关联分析异常", var14);
            String e = "保存客户群";
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_MANAGE_ADD_CUS_ANALYSIS", "-1", this.ciCustomGroupInfo.getCustomGroupName().toString(), "客户群分析->关联分析保存失败,【客户群名称 :" + this.ciCustomGroupInfo.getCustomGroupName() + ",客户群描述:" + this.ciCustomGroupInfo.getCustomGroupDesc() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            returnMap.put("cmsg", e + "失败");
            returnMap.put("success", Boolean.valueOf(false));
            throw new CIServiceException(var14);
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(returnMap));
        } catch (Exception var13) {
            this.log.error("发送json串异常", var13);
            throw new CIServiceException(var13);
        }
    }

    protected void setCoordinate(CiCustomRelModel ciCustomRelModel, Double percent, Double angle) {
        double offset = 10.0D;
        double x = 0.0D;
        double y = 0.0D;
        x = (40.0D * (1.0D - percent.doubleValue()) + offset) * Math.cos(angle.doubleValue()) + 50.0D;
        y = (40.0D * (1.0D - percent.doubleValue()) + offset) * Math.sin(angle.doubleValue()) + 50.0D;
        ciCustomRelModel.setX(Integer.valueOf((int)Math.ceil(x)));
        ciCustomRelModel.setY(Integer.valueOf((int)Math.ceil(y)));
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

    public CiCustomRelModel getCiCustomRelModel() {
        return this.ciCustomRelModel;
    }

    public void setCiCustomRelModel(CiCustomRelModel ciCustomRelModel) {
        this.ciCustomRelModel = ciCustomRelModel;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }
}

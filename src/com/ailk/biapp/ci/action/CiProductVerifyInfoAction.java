package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiProductCategory;
import com.ailk.biapp.ci.entity.CiProductInfo;
import com.ailk.biapp.ci.entity.CiVerifyFilterProcess;
import com.ailk.biapp.ci.entity.CiVerifyMm;
import com.ailk.biapp.ci.entity.CiVerifyRuleInfo;
import com.ailk.biapp.ci.entity.CiVerifyRuleRel;
import com.ailk.biapp.ci.entity.DimBrand;
import com.ailk.biapp.ci.entity.DimVerifyIndexShow;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.service.ICiProductVerifyInfoService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.JsonUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiProductVerifyInfoAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiProductVerifyInfoAction.class);
    private CiProductInfo productInfo;
    private CiVerifyRuleRel ciVerifyRuleRel;
    private List<CiVerifyMm> ciVerifyMmList;
    private List<CiVerifyRuleRel> ciVerifyRuleRelList;
    private List<DimVerifyIndexShow> verifyIndexShowList;
    private List<CiVerifyFilterProcess> labelProcess;
    private List<CiVerifyFilterProcess> labelProcessL1;
    private List<CiVerifyFilterProcess> labelProcessL2;
    private List<CiVerifyFilterProcess> labelProcessL3;
    private List<CiVerifyFilterProcess> indexProcess;
    private List<CiVerifyFilterProcess> indexProcessI1;
    private List<CiVerifyFilterProcess> indexProcessI2;
    private CiVerifyRuleRel finalPotentialUserRule;
    private List<CiVerifyRuleInfo> indexRuleDescribe;
    private List<CiVerifyRuleInfo> labelRule;
    @Autowired
    private ICiProductVerifyInfoService ciProductVerifyInfoService;

    public CiProductVerifyInfoAction() {
    }

    public void getVefifyResult() throws Exception {
        CacheBase cache = CacheBase.getInstance();
        CiProductInfo ciProductInfo = cache.getEffectiveProduct(this.productInfo.getProductId() + "");
        String offerId = ciProductInfo.getCiMdaSysTableColumn().getColumnName().substring(2);
        this.ciVerifyMmList = this.ciProductVerifyInfoService.queryVerifyMmResultList(offerId);
        int length = this.ciVerifyMmList.size();
        if(length > 3) {
            this.ciVerifyMmList = this.ciVerifyMmList.subList(length - 3, length);
        }

        HttpServletResponse response = this.getResponse();

        try {
            this.sendJson(response, JsonUtil.toJson(this.ciVerifyMmList));
        } catch (Exception var7) {
            this.log.error("发送json串异常", var7);
            throw new CIServiceException(var7);
        }
    }

    public String getAnalysisAndDesignProcess() throws Exception {
        String offerId = this.productInfo.getOfferId();
        String productId = this.productInfo.getProductId().toString();

        try {
            CacheBase e = CacheBase.getInstance();
            this.productInfo = e.getEffectiveProduct(productId);
            this.labelProcess = this.ciProductVerifyInfoService.queryLabelProcess(offerId);
            this.labelProcessL1 = this.ciProductVerifyInfoService.queryLabelProcessLevel(offerId, "L1");
            this.labelProcessL2 = this.ciProductVerifyInfoService.queryLabelProcessLevel(offerId, "L2");
            this.labelProcessL3 = this.ciProductVerifyInfoService.queryLabelProcessLevel(offerId, "L3");
            this.indexProcess = this.ciProductVerifyInfoService.queryIndexProcess(offerId);
            this.indexProcessI1 = this.ciProductVerifyInfoService.queryIndexProcessLevel(offerId, "I1");
            this.indexProcessI2 = this.ciProductVerifyInfoService.queryIndexProcessLevel(offerId, "I2");
            List list = this.ciProductVerifyInfoService.queryFinalPotentialUserRule(offerId);
            if(list != null && list.size() > 0) {
                this.finalPotentialUserRule = (CiVerifyRuleRel)list.get(0);
            }

            this.indexRuleDescribe = this.ciProductVerifyInfoService.queryIndexRuleDescribe(offerId);
            this.labelRule = this.ciProductVerifyInfoService.queryLabelRule(offerId);
            this.verifyIndexShowList = this.ciProductVerifyInfoService.queryIndexList();
            Iterator i$ = this.labelRule.iterator();

            while(i$.hasNext()) {
                CiVerifyRuleInfo ruleInfo = (CiVerifyRuleInfo)i$.next();
                ruleInfo.setProductName(this.productInfo.getProductName());
            }

            CILogServiceUtil.getLogServiceInstance().log("COC_PRODUCT_SET_PROCESS_VIEW", productId, this.productInfo.getProductName(), "首页觅产品产品分析与设定过程查看成功,查询条件：【产品ID:" + (productId == null?"":productId) + "，OFFERID：" + offerId + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            return "analysisProcess";
        } catch (Exception var7) {
            this.log.error("查询产品规则校验报告出错", var7);
            CILogServiceUtil.getLogServiceInstance().log("COC_PRODUCT_SET_PROCESS_VIEW", "-1", "", "首页觅产品产品分析与设定过程查看失败,查询条件：【产品ID:" + (productId == null?"":productId) + "，OFFERID：" + offerId + "】", OperResultEnum.Success, LogLevelEnum.Medium);
            throw new CIServiceException(var7);
        }
    }

    public String verifyIndex() throws Exception {
        try {
            String e = this.productInfo.getProductId().toString();
            CacheBase cache = CacheBase.getInstance();
            this.productInfo = cache.getEffectiveProduct(e);
            String brandIds = this.productInfo.getBrandId();
            String brandNames = this.getBrandNamesById(brandIds);
            StringBuffer brandNameBuf = new StringBuffer();
            if(StringUtils.isNotBlank(brandNames)) {
                String[] categoryId = brandNames.split(",");
                String[] caregory = categoryId;
                int offerId = categoryId.length;

                for(int ruleDesc = 0; ruleDesc < offerId; ++ruleDesc) {
                    String brandName = caregory[ruleDesc];
                    brandNameBuf.append(brandName).append("|");
                }

                brandNames = brandNameBuf.substring(0, brandNameBuf.length() - 1);
            }

            this.productInfo.setBrandNames(brandNames);
            Integer var12 = this.productInfo.getCategoryId();
            CiProductCategory var13 = cache.getProductCategory(String.valueOf(var12));
            this.productInfo.setCategoryName(var13.getCategoryName());
            String var14 = this.productInfo.getCiMdaSysTableColumn().getColumnName().replace("P_", "");
            this.productInfo.setOfferId(var14);
            String var15 = this.ciProductVerifyInfoService.queryRuleDesc(var14);
            this.productInfo.setRuleDesc(var15);
            return "verifyIndex";
        } catch (Exception var11) {
            this.log.error("查询产品规则校验报告出错", var11);
            throw new CIServiceException(var11);
        }
    }

    public String assessResult() throws Exception {
        String offerId = this.ciVerifyRuleRel.getOfferId();
        this.ciVerifyRuleRelList = this.ciProductVerifyInfoService.queryVerifyRuleRelList(offerId);
        CILogServiceUtil.getLogServiceInstance().log("COC_PRODUCT_EVALUATE_RESULT_VIEW", offerId, "", "首页觅产品产品评定结果查看成功,查询条件：【OFFERID:" + (offerId == null?"":offerId) + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "assessResult";
    }

    public String initProductHisInfo() throws Exception {
        return "initProductHisInfo";
    }

    public String initUserNumHisInfo() throws Exception {
        return "initUserNumHisInfo";
    }

    public String productHisInfo() throws Exception {
        String offerId = this.ciVerifyRuleRel.getOfferId();
        this.ciVerifyMmList = this.ciProductVerifyInfoService.queryVerifyMmList(offerId);
        return "productHisInfo";
    }

    public String userNumHisInfo() throws Exception {
        String offerId = this.ciVerifyRuleRel.getOfferId();
        this.ciVerifyMmList = this.ciProductVerifyInfoService.queryVerifyMmList(offerId);
        return "userNumHisInfo";
    }

    private String getBrandNamesById(String ids) {
        String[] idsArray = new String[0];
        StringBuffer brandNames = new StringBuffer();
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList dimBrand = cache.getObjectList("DIM_BRAND");
        if(StringUtil.isNotEmpty(ids)) {
            idsArray = ids.split(",");
        }

        String[] arr$ = idsArray;
        int len$ = idsArray.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String id = arr$[i$];

            for(int i = 0; i < dimBrand.size(); ++i) {
                if(id.equals(((DimBrand)dimBrand.get(i)).getBrandId().toString())) {
                    brandNames.append(((DimBrand)dimBrand.get(i)).getBrandName().toString() + ",");
                }
            }
        }

        if(brandNames.length() > 0) {
            return brandNames.toString().substring(0, brandNames.toString().length() - 1);
        } else {
            return "";
        }
    }

    public CiVerifyRuleRel getCiVerifyRuleRel() {
        return this.ciVerifyRuleRel;
    }

    public void setCiVerifyRuleRel(CiVerifyRuleRel ciVerifyRuleRel) {
        this.ciVerifyRuleRel = ciVerifyRuleRel;
    }

    public List<CiVerifyMm> getCiVerifyMmList() {
        return this.ciVerifyMmList;
    }

    public void setCiVerifyMmList(List<CiVerifyMm> ciVerifyMmList) {
        this.ciVerifyMmList = ciVerifyMmList;
    }

    public List<CiVerifyFilterProcess> getLabelProcessL3() {
        return this.labelProcessL3;
    }

    public void setLabelProcessL3(List<CiVerifyFilterProcess> labelProcessL3) {
        this.labelProcessL3 = labelProcessL3;
    }

    public List<CiVerifyFilterProcess> getLabelProcess() {
        return this.labelProcess;
    }

    public void setLabelProcess(List<CiVerifyFilterProcess> labelProcess) {
        this.labelProcess = labelProcess;
    }

    public List<CiVerifyFilterProcess> getLabelProcessL1() {
        return this.labelProcessL1;
    }

    public void setLabelProcessL1(List<CiVerifyFilterProcess> labelProcessL1) {
        this.labelProcessL1 = labelProcessL1;
    }

    public List<CiVerifyFilterProcess> getLabelProcessL2() {
        return this.labelProcessL2;
    }

    public void setLabelProcessL2(List<CiVerifyFilterProcess> labelProcessL2) {
        this.labelProcessL2 = labelProcessL2;
    }

    public List<CiVerifyFilterProcess> getIndexProcess() {
        return this.indexProcess;
    }

    public List<CiVerifyRuleRel> getCiVerifyRuleRelList() {
        return this.ciVerifyRuleRelList;
    }

    public void setCiVerifyRuleRelList(List<CiVerifyRuleRel> ciVerifyRuleRelList) {
        this.ciVerifyRuleRelList = ciVerifyRuleRelList;
    }

    public CiProductInfo getProductInfo() {
        return this.productInfo;
    }

    public void setProductInfo(CiProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public void setIndexProcess(List<CiVerifyFilterProcess> indexProcess) {
        this.indexProcess = indexProcess;
    }

    public List<CiVerifyFilterProcess> getIndexProcessI1() {
        return this.indexProcessI1;
    }

    public void setIndexProcessI1(List<CiVerifyFilterProcess> indexProcessI1) {
        this.indexProcessI1 = indexProcessI1;
    }

    public List<CiVerifyFilterProcess> getIndexProcessI2() {
        return this.indexProcessI2;
    }

    public void setIndexProcessI2(List<CiVerifyFilterProcess> indexProcessI2) {
        this.indexProcessI2 = indexProcessI2;
    }

    public List<CiVerifyRuleInfo> getIndexRuleDescribe() {
        return this.indexRuleDescribe;
    }

    public void setIndexRuleDescribe(List<CiVerifyRuleInfo> indexRuleDescribe) {
        this.indexRuleDescribe = indexRuleDescribe;
    }

    public List<CiVerifyRuleInfo> getLabelRule() {
        return this.labelRule;
    }

    public void setLabelRule(List<CiVerifyRuleInfo> labelRule) {
        this.labelRule = labelRule;
    }

    public List<DimVerifyIndexShow> getVerifyIndexShowList() {
        return this.verifyIndexShowList;
    }

    public void setVerifyIndexShowList(List<DimVerifyIndexShow> verifyIndexShowList) {
        this.verifyIndexShowList = verifyIndexShowList;
    }

    public CiVerifyRuleRel getFinalPotentialUserRule() {
        return this.finalPotentialUserRule;
    }

    public void setFinalPotentialUserRule(CiVerifyRuleRel finalPotentialUserRule) {
        this.finalPotentialUserRule = finalPotentialUserRule;
    }
}

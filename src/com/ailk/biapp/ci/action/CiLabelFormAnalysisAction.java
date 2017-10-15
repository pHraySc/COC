package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.model.CiLabelFormModel;
import com.ailk.biapp.ci.model.CiLabelFormTrendModel;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiLabelFormAnalysisService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.FileUtil;
import com.ailk.biapp.ci.util.IdToName;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiLabelFormAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(CiLabelFormAnalysisAction.class);
    private CiLabelFormModel ciLabelFormModel;
    private List<CiLabelFormModel> labelFormList;
    private List<CiLabelFormTrendModel> labelFormTrendList;
    private String caption;
    private Pager pager;
    @Autowired
    private ICiLabelFormAnalysisService ciLabelFormAnalysisService;

    public CiLabelFormAnalysisAction() {
    }

    public String showLabelForm() {
        this.log.info("Enter CiLabelFormAnalysisAction.showLabelForm() method");
        this.log.info("Exit CiLabelFormAnalysisAction.showLabelForm() method");
        return "labelForm";
    }

    public String findBrandFormChartData() throws Exception {
        String province = Configure.getInstance().getProperty("PROVINCE");
        String aBrandCitys = Configure.getInstance().getProperty("A_BRAND_CITYS");
        boolean aBrandFlag = false;
        this.ciLabelFormModel.setaBrandFlag(aBrandFlag);
        if(StringUtil.isNotEmpty(aBrandCitys)) {
            String[] aBrandCityArr = aBrandCitys.split(",");
            String[] arr$ = aBrandCityArr;
            int len$ = aBrandCityArr.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String aBrandCity = arr$[i$];
                if(aBrandCity.equals(province)) {
                    aBrandFlag = true;
                    this.ciLabelFormModel.setaBrandFlag(aBrandFlag);
                }
            }
        }

        this.labelFormList = this.ciLabelFormAnalysisService.queryBrandFormChartData(this.ciLabelFormModel);
        return "brandForm";
    }

    public String findSubBrandFormChartData() throws Exception {
        this.caption = IdToName.getName("DIM_BRAND", this.ciLabelFormModel.getBrandId()) + "子品牌构成";
        this.labelFormList = this.ciLabelFormAnalysisService.querySubBrandFormChartData(this.ciLabelFormModel);
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_BRAND_CHART_VIEW", this.ciLabelFormModel.getLabelId().toString(), "", "标签分析->标签子品牌构成图查看（柱状图）,【标签Id:" + this.ciLabelFormModel.getLabelId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "subBrandForm";
    }

    public String findCityFormChartData() throws Exception {
        this.labelFormList = this.ciLabelFormAnalysisService.queryCityFormChartData(this.ciLabelFormModel);
        return "cityForm";
    }

    public String findMoreCityFormChartData() throws Exception {
        this.labelFormList = this.ciLabelFormAnalysisService.queryCityFormChartData(this.ciLabelFormModel);
        return "moreCityForm";
    }

    public String findVipFormChartData() throws Exception {
        this.labelFormList = this.ciLabelFormAnalysisService.queryVipFormChartData(this.ciLabelFormModel);
        return "vipForm";
    }

    public String findBrandTrendChartData() throws Exception {
        CiLabelInfo labelInfo = CacheBase.getInstance().getEffectiveLabel(this.ciLabelFormModel.getLabelId().toString());
        this.labelFormTrendList = this.ciLabelFormAnalysisService.queryBrandTrendChartData(this.ciLabelFormModel);
        this.caption = "品牌历史构成";
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_BRAND_CHART_VIEW", this.ciLabelFormModel.getLabelId().toString(), labelInfo.getLabelName(), "标签分析->标签品牌构成图查看（柱状图）,【标签Id:" + this.ciLabelFormModel.getLabelId() + "；标签名称：" + labelInfo.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "brandTrend";
    }

    public String findVipTrendChartData() throws Exception {
        this.labelFormTrendList = this.ciLabelFormAnalysisService.queryVipTrendChartData(this.ciLabelFormModel);
        this.caption = "VIP等级历史构成";
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_VIP_CHART_VIEW", this.ciLabelFormModel.getLabelId().toString(), "", "标签分析->标签VIP等级构成图查看（堆积柱状图）,【标签Id:" + this.ciLabelFormModel.getLabelId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "vipTrend";
    }

    public String findCityTrendChartData() throws Exception {
        this.labelFormTrendList = this.ciLabelFormAnalysisService.queryCityTrendChartData(this.ciLabelFormModel);
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CITY_CHART_VIEW", this.ciLabelFormModel.getLabelId().toString(), "", "标签分析->标签地域构成图查看（地域历史趋势分析图，堆积柱状图）,【标签Id:" + this.ciLabelFormModel.getLabelId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "cityTrend";
    }

    public String findCityTrendChartDataByCityId() throws Exception {
        String captionDesc = "";
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(this.ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        if(updateCycle == 1) {
            captionDesc = "近六天用户数趋势";
        } else if(updateCycle == 2) {
            captionDesc = "近六个月用户数趋势";
        }

        this.caption = IdToName.getName("DIM_CITY", this.ciLabelFormModel.getCityId()) + captionDesc;
        this.labelFormList = this.ciLabelFormAnalysisService.queryCityTrendChartDataByCityId(this.ciLabelFormModel);
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CITY_CHART_VIEW", this.ciLabelFormModel.getLabelId().toString(), "", "标签分析->标签地域构成图查看（地域历史趋势分析图，折线图）,【标签Id:" + this.ciLabelFormModel.getLabelId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "cityTrendByCityId";
    }

    public String findMoreCityTrendChartDataByCityId() throws Exception {
        String captionDesc = "";
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(this.ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        if(updateCycle == 1) {
            captionDesc = "近一个月用户数趋势";
        } else if(updateCycle == 2) {
            captionDesc = "近一年用户数趋势";
        }

        this.caption = IdToName.getName("DIM_CITY", this.ciLabelFormModel.getCityId()) + captionDesc;
        this.labelFormList = this.ciLabelFormAnalysisService.queryMoreCityTrendChartDataByCityId(this.ciLabelFormModel);
        return "cityTrendByCityId";
    }

    public String findVipTrendChartDataByVipLevelId() throws Exception {
        String captionDesc = "";
        int updateCycle = CacheBase.getInstance().getEffectiveLabel(this.ciLabelFormModel.getLabelId().toString()).getUpdateCycle().intValue();
        if(updateCycle == 1) {
            captionDesc = "近六天用户数趋势";
        } else if(updateCycle == 2) {
            captionDesc = "近六个月用户数趋势";
        }

        this.caption = IdToName.getName("DIM_VIP_LEVEL", this.ciLabelFormModel.getVipLevelId()) + captionDesc;
        this.labelFormList = this.ciLabelFormAnalysisService.queryVipTrendChartDataByVipLevelId(this.ciLabelFormModel);
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CITY_CHART_VIEW", this.ciLabelFormModel.getLabelId().toString(), "", "标签分析->标签地域构成图查看（柱状图）,【标签Id:" + this.ciLabelFormModel.getLabelId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "vipTrendByVipLevelId";
    }

    public String findBrandHistoryData() throws Exception {
        try {
            List dateList = DateUtil.getAssignFrontDate(6, this.ciLabelFormModel.getDataDate(), this.ciLabelFormModel.getUpdateCycle().intValue());
            if(this.pager == null) {
                this.pager = new Pager();
            }

            if(this.pager.getTotalSize() == 0L) {
                this.pager.setTotalSize(this.ciLabelFormAnalysisService.queryBrandHistoryDataCount(this.ciLabelFormModel));
            }

            this.pager.setPageSize(6);
            List brandHisModelList = this.ciLabelFormAnalysisService.queryBrandHistoryData(this.pager.getPageNum(), this.pager.getPageSize(), dateList, this.ciLabelFormModel);
            this.pager = this.pager.pagerFlip();
            this.pager.setResult(brandHisModelList);
            ActionContext e = ActionContext.getContext();
            e.put("brandHisModelList", brandHisModelList);
            e.put("dateList", dateList);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_BRAND_TABEL_VIEW", this.ciLabelFormModel.getLabelId().toString(), "", "标签分析->标签品牌构成表格查看成功,【标签Id:" + this.ciLabelFormModel.getLabelId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var4) {
            this.log.error(var4);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_BRAND_TABEL_VIEW", this.ciLabelFormModel.getLabelId().toString(), "", "标签分析->标签品牌构成表格查看失败,【标签Id:" + this.ciLabelFormModel.getLabelId() + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
            var4.printStackTrace();
        }

        return "brandHisTable";
    }

    public void export() {
        CiLabelInfo labelInfo = CacheBase.getInstance().getEffectiveLabel(this.ciLabelFormModel.getLabelId().toString());
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(labelInfo.getLabelName());
        String lname = m.replaceAll("");
        this.log.debug("标签构成分析表格下载，标签id：" + labelInfo.getLabelId() + "; 标签名称：" + labelInfo.getLabelName());
        String fname = "标签_" + lname + "_构成分析表格数据" + this.ciLabelFormModel.getDataDate() + ".xls";

        try {
            HttpServletRequest e = this.getRequest();
            HttpServletResponse response = this.getResponse();
            e.setCharacterEncoding("UTF-8");
            List dateList = DateUtil.getAssignFrontDate(6, this.ciLabelFormModel.getDataDate(), this.ciLabelFormModel.getUpdateCycle().intValue());
            List brandHisModelList = this.ciLabelFormAnalysisService.queryBrandHistoryData(0, 0, dateList, this.ciLabelFormModel);
            List cityHisModelList = this.ciLabelFormAnalysisService.queryCityHistoryData(dateList, this.ciLabelFormModel);
            List vipHisModelList = this.ciLabelFormAnalysisService.queryVipHistoryData(dateList, this.ciLabelFormModel);
            FileUtil.downLoadFile(response, fname);
            FileUtil.exportToExcel(e, response, brandHisModelList, cityHisModelList, vipHisModelList, dateList);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ANALYSIS_DOWNLOAD", this.ciLabelFormModel.getLabelId().toString(), labelInfo.getLabelName(), "标签构成分析表格下载成功,【导出文件名：" + fname + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var12) {
            this.log.error("标签构成分析表格下载错误", var12);
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_ANALYSIS_DOWNLOAD", this.ciLabelFormModel.getLabelId().toString(), labelInfo.getLabelName(), "标签构成分析表格下载失败,【导出文件名：" + fname + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

    }

    public String findVipHistoryData() throws Exception {
        List dateList = DateUtil.getAssignFrontDate(6, this.ciLabelFormModel.getDataDate(), this.ciLabelFormModel.getUpdateCycle().intValue());
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getTotalSize() == 0L) {
            this.pager.setTotalSize(this.ciLabelFormAnalysisService.queryVipHistoryDataCount(this.ciLabelFormModel));
        }

        this.pager.setPageSize(6);
        List vipHisModelList = this.ciLabelFormAnalysisService.queryVipHistoryData(dateList, this.ciLabelFormModel);
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(vipHisModelList);
        ActionContext ctx = ActionContext.getContext();
        ctx.put("vipHisModelList", vipHisModelList);
        ctx.put("dateList", dateList);
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_VIP_TABEL_VIEW", this.ciLabelFormModel.getLabelId().toString(), "", "标签分析->标签VIP等级构成表格查看,【标签Id:" + this.ciLabelFormModel.getLabelId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "vipHisTable";
    }

    public String findCityHistoryData() throws Exception {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getTotalSize() == 0L) {
            this.pager.setTotalSize(this.ciLabelFormAnalysisService.queryCityHistoryDataCount(this.ciLabelFormModel));
        }

        this.pager.setPageSize(6);
        List dateList = DateUtil.getAssignFrontDate(6, this.ciLabelFormModel.getDataDate(), this.ciLabelFormModel.getUpdateCycle().intValue());
        List cityHisModelList = this.ciLabelFormAnalysisService.queryCityHistoryData(dateList, this.ciLabelFormModel);
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(cityHisModelList);
        ActionContext ctx = ActionContext.getContext();
        ctx.put("cityHisModelList", cityHisModelList);
        ctx.put("dateList", dateList);
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CITY_TABEL_VIEW", this.ciLabelFormModel.getLabelId().toString(), "", "标签分析->标签地域构成表格查看,【标签Id:" + this.ciLabelFormModel.getLabelId() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "cityHisTable";
    }

    public CiLabelFormModel getCiLabelFormModel() {
        return this.ciLabelFormModel;
    }

    public void setCiLabelFormModel(CiLabelFormModel ciLabelFormModel) {
        this.ciLabelFormModel = ciLabelFormModel;
    }

    public List<CiLabelFormModel> getLabelFormList() {
        return this.labelFormList;
    }

    public List<CiLabelFormTrendModel> getLabelFormTrendList() {
        return this.labelFormTrendList;
    }

    public String getCaption() {
        return this.caption;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }
}

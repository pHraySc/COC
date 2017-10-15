package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiCustomGroupForm;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.model.CiCustomersFormTrendModel;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICustomersAnalysisService;
import com.ailk.biapp.ci.service.ICustomersManagerService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.FileUtil;
import com.ailk.biapp.ci.util.IdToName;
import com.asiainfo.biframe.log.OperResultEnum;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiCustomersFormAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(this.getClass());
    private String caption;
    private Pager pager;
    private CiCustomGroupForm ciCustomGroupForm;
    private List<CiCustomGroupForm> customGroupFormList;
    private List<CiCustomersFormTrendModel> customGroupTrendList;
    @Autowired
    private ICustomersManagerService customersService;
    @Autowired
    private ICustomersAnalysisService customersAnalysisService;

    public CiCustomersFormAnalysisAction() {
    }

    public String customersForm() throws Exception {
        if(this.ciCustomGroupForm == null) {
            throw new Exception("客户群参数不正确");
        } else {
            return "customersForm";
        }
    }

    public String customersBrandFormChartData() throws Exception {
        String province = Configure.getInstance().getProperty("PROVINCE");
        String aBrandCitys = Configure.getInstance().getProperty("A_BRAND_CITYS");
        boolean aBrandFlag = false;
        this.ciCustomGroupForm.setaBrandFlag(aBrandFlag);
        if(StringUtil.isNotEmpty(aBrandCitys)) {
            String[] customGroupInfo = aBrandCitys.split(",");
            String[] arr$ = customGroupInfo;
            int len$ = customGroupInfo.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String aBrandCity = arr$[i$];
                if(aBrandCity.equals(province)) {
                    aBrandFlag = true;
                    this.ciCustomGroupForm.setaBrandFlag(aBrandFlag);
                }
            }
        }

        CiCustomGroupInfo var9 = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupForm.getCustomGroupId());
        this.customGroupFormList = this.customersAnalysisService.queryBrandFormChartData(this.ciCustomGroupForm);
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_BRAND_CHART_VIEW", var9.getCustomGroupId(), var9.getCustomGroupName(), "客户群品牌构成图查看【客户群ID：" + var9.getCustomGroupId() + "名称：" + var9.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customersBrandFormChartData";
    }

    public String customersBrandTrendChartData() throws Exception {
        this.customGroupTrendList = this.customersAnalysisService.queryBrandTrendChartData(this.ciCustomGroupForm);
        return "customersBrandTrendChartData";
    }

    public String customersSubBrandFormChartData() throws Exception {
        this.caption = IdToName.getName("DIM_BRAND", this.ciCustomGroupForm.getBrandId()) + "子品牌构成";
        this.customGroupFormList = this.customersAnalysisService.querySubBrandFormChartData(this.ciCustomGroupForm);
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupForm.getCustomGroupId());
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_BRAND_CHART_VIEW", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群子品牌构成图查看【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customersSubBrandFormChartData";
    }

    public String customersBrandHistoryData() throws Exception {
        List dateList = DateUtil.getAssignFrontDate(6, this.ciCustomGroupForm.getDataDate(), 2);
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getTotalSize() == 0L) {
            this.pager.setTotalSize((long)this.customersAnalysisService.queryBrandHistoryDataCount(this.ciCustomGroupForm));
        }

        this.pager.setPageSize(6);
        this.pager = this.pager.pagerFlip();
        List brandHisModelList = this.customersAnalysisService.selectBrandHistoryData(this.pager.getPageNum(), this.pager.getPageSize(), dateList, this.ciCustomGroupForm);
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupForm.getCustomGroupId());
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_BRAND_TABEL_VIEW", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群品牌构成表格查看【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        this.pager.setResult(brandHisModelList);
        ActionContext ctx = ActionContext.getContext();
        ctx.put("brandHisModelList", brandHisModelList);
        ctx.put("dateList", dateList);
        return "customersBrandHistoryData";
    }

    public String customersCityFormChartData() throws Exception {
        this.customGroupFormList = this.customersAnalysisService.queryCityFormChartData(this.ciCustomGroupForm);
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupForm.getCustomGroupId());
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_CITY_CHART_VIEW", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "查看客户群地域构成分析【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customersCityFormChartData";
    }

    public String customersMoreCityFormChartData() throws Exception {
        this.customGroupFormList = this.customersAnalysisService.queryCityFormChartData(this.ciCustomGroupForm);
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupForm.getCustomGroupId());
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_CITY_CHART_VIEW", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群地域构成图查看【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customersMoreCityFormChartData";
    }

    public String customersCityTrendFormChartData() throws Exception {
        this.customGroupTrendList = this.customersAnalysisService.queryCityTrendChartData(this.ciCustomGroupForm);
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupForm.getCustomGroupId());
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_CITY_CHART_VIEW", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群地域构成图查看【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "customersCityTrendFormChartData";
    }

    public String customersCityTrendFormChartDataById() throws Exception {
        this.caption = IdToName.getName("DIM_CITY", this.ciCustomGroupForm.getCityId()) + "近六个月用户数趋势";
        this.customGroupFormList = this.customersAnalysisService.queryCityTrendChartDataByCityId(this.ciCustomGroupForm);
        return "customersCityTrendFormChartDataById";
    }

    public String customersMoreCityTrendFormChartDataById() throws Exception {
        this.caption = IdToName.getName("DIM_CITY", this.ciCustomGroupForm.getCityId()) + "近一年用户数趋势";
        this.customGroupFormList = this.customersAnalysisService.queryMoreCityTrendChartDataByCityId(this.ciCustomGroupForm);
        return "customersCityTrendFormChartDataById";
    }

    public String cityHistoryData() throws Exception {
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getTotalSize() == 0L) {
            this.pager.setTotalSize((long)this.customersAnalysisService.queryCityHistoryDataCount(this.ciCustomGroupForm));
        }

        this.pager.setPageSize(6);
        List dateList = DateUtil.getAssignFrontDate(6, this.ciCustomGroupForm.getDataDate(), 2);
        List cityHisModelList = this.customersAnalysisService.queryCityHistoryData(dateList, this.ciCustomGroupForm);
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(cityHisModelList);
        ActionContext ctx = ActionContext.getContext();
        ctx.put("cityHisModelList", cityHisModelList);
        ctx.put("dateList", dateList);
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupForm.getCustomGroupId());
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_CITY_TABEL_VIEW", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群地域构成表格查看【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "cityHistoryData";
    }

    public String vipFormChartData() throws Exception {
        this.customGroupFormList = this.customersAnalysisService.queryVipFormChartData(this.ciCustomGroupForm);
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupForm.getCustomGroupId());
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_VIP_CHART_VIEW", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群VIP等级构成图查看【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "vipFormChartData";
    }

    public String vipTrendChartData() throws Exception {
        this.customGroupTrendList = this.customersAnalysisService.queryVipTrendChartData(this.ciCustomGroupForm);
        return "vipTrendChartData";
    }

    public String vipHistoryData() throws Exception {
        List dateList = DateUtil.getAssignFrontDate(6, this.ciCustomGroupForm.getDataDate(), 2);
        if(this.pager == null) {
            this.pager = new Pager();
        }

        if(this.pager.getTotalSize() == 0L) {
            this.pager.setTotalSize((long)this.customersAnalysisService.queryVipHistoryDataCount(this.ciCustomGroupForm));
        }

        this.pager.setPageSize(6);
        List vipHisModelList = this.customersAnalysisService.queryVipHistoryData(dateList, this.ciCustomGroupForm);
        this.pager = this.pager.pagerFlip();
        this.pager.setResult(vipHisModelList);
        ActionContext ctx = ActionContext.getContext();
        ctx.put("vipHisModelList", vipHisModelList);
        ctx.put("dateList", dateList);
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupForm.getCustomGroupId());
        CILogServiceUtil.getLogServiceInstance().log("COC_CUS_VIP_TABEL_VIEW", customGroupInfo.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群VIP等级构成表格查看【客户群ID：" + customGroupInfo.getCustomGroupId() + "名称：" + customGroupInfo.getCustomGroupName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return "vipHistoryData";
    }

    public String vipTrendChartDataByVipLevelId() throws Exception {
        this.caption = IdToName.getName("DIM_VIP_LEVEL", this.ciCustomGroupForm.getVipLevelId()) + "近六个月用户数趋势";
        this.customGroupFormList = this.customersAnalysisService.queryVipTrendChartDataByVipLevelId(this.ciCustomGroupForm);
        return "vipTrendChartDataByVipLevelId";
    }


    public void export() {
        CiCustomGroupInfo customGroupInfo = this.customersService.queryCiCustomGroupInfo(this.ciCustomGroupForm.getCustomGroupId());
        String fname = "客户群_" + customGroupInfo.getCustomGroupName() + "_构成分析表格数据" + this.ciCustomGroupForm.getDataDate() + ".xls";

        try {
            HttpServletRequest e = this.getRequest();
            HttpServletResponse response = this.getResponse();
            e.setCharacterEncoding("UTF-8");
            List dateList = DateUtil.getAssignFrontDate(6, this.ciCustomGroupForm.getDataDate(), 2);
            List brandHisModelList = this.customersAnalysisService.selectBrandHistoryData(0, 2147483647, dateList, this.ciCustomGroupForm);
            List cityHisModelList = this.customersAnalysisService.queryCityHistoryData(dateList, this.ciCustomGroupForm);
            List vipHisModelList = this.customersAnalysisService.queryVipHistoryData(dateList, this.ciCustomGroupForm);
            FileUtil.downLoadFile(response, fname);
            FileUtil.exportToExcel(e, response, brandHisModelList, cityHisModelList, vipHisModelList, dateList);
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_ANALYSIS_DOWNLOAD", this.ciCustomGroupForm.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群构成分析表格下载成功,【导出文件名：" + fname + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } catch (Exception var9) {
            this.log.error(var9);
            CILogServiceUtil.getLogServiceInstance().log("COC_CUSTOMER_ANALYSIS_DOWNLOAD", this.ciCustomGroupForm.getCustomGroupId(), customGroupInfo.getCustomGroupName(), "客户群构成分析表格下载失败,【导出文件名：" + fname + "】", OperResultEnum.Failure, LogLevelEnum.Medium);
        }

    }

    public CiCustomGroupForm getCiCustomGroupForm() {
        return this.ciCustomGroupForm;
    }

    public void setCiCustomGroupForm(CiCustomGroupForm ciCustomGroupForm) {
        this.ciCustomGroupForm = ciCustomGroupForm;
    }

    public List<CiCustomGroupForm> getCustomGroupFormList() {
        return this.customGroupFormList;
    }

    public void setCustomGroupFormList(List<CiCustomGroupForm> customGroupFormList) {
        this.customGroupFormList = customGroupFormList;
    }

    public List<CiCustomersFormTrendModel> getCustomGroupTrendList() {
        return this.customGroupTrendList;
    }

    public void setCustomGroupTrendList(List<CiCustomersFormTrendModel> customGroupTrendList) {
        this.customGroupTrendList = customGroupTrendList;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }
}

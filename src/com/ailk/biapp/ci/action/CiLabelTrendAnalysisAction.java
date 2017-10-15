package com.ailk.biapp.ci.action;

import com.ailk.biapp.ci.action.CIBaseAction;
import com.ailk.biapp.ci.constant.LogLevelEnum;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.model.LabelTrendInfo;
import com.ailk.biapp.ci.model.Pager;
import com.ailk.biapp.ci.service.ICiLabelInfoService;
import com.ailk.biapp.ci.service.ICiLabelTrendAnalysisService;
import com.ailk.biapp.ci.service.ICiLabelUserUseService;
import com.ailk.biapp.ci.util.CILogServiceUtil;
import com.ailk.biapp.ci.util.WeekUtil;
import com.asiainfo.biframe.chart.AIChart;
import com.asiainfo.biframe.log.OperResultEnum;
import com.opensymphony.xwork2.ActionContext;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CiLabelTrendAnalysisAction extends CIBaseAction {
    private Logger log = Logger.getLogger(this.getClass());
    private AIChart labelTrendChart;
    private Pager pager;
    @Autowired
    ICiLabelTrendAnalysisService ciLabelTrendAnalysisService;
    @Autowired
    private ICiLabelInfoService ciLabelInfoService;
    @Autowired
    private ICiLabelUserUseService ciLabelUserUseService;
    private List<LabelTrendInfo> trendInfos;

    public CiLabelTrendAnalysisAction() {
    }

    public String initLabelTrendAnalysis() {
        String labelId = this.getRequest().getParameter("labelId");
        this.log.debug("Label ID->" + labelId);
        return "success";
    }

    public String findLabelUserNumTrend() {
        String labelId = super.getRequest().getParameter("labelId");
        String dataDate = super.getRequest().getParameter("dataDate");
        CiLabelInfo labelInfo = this.ciLabelInfoService.queryCiLabelInfoById(new Integer(labelId));
        String labelType = labelInfo.getUpdateCycle().toString();
        List trendInfos = this.ciLabelTrendAnalysisService.queryLabelUserNumsTrendInfo(labelId, dataDate, labelType);
        this.labelTrendChart = new AIChart();
        if(null != trendInfos) {
            LinkedHashMap trendData = new LinkedHashMap();
            Iterator i$ = trendInfos.iterator();

            while(i$.hasNext()) {
                LabelTrendInfo info = (LabelTrendInfo)i$.next();
                trendData.put(info.getDataDate(), info.getValue());
            }

            this.labelTrendChart.addPYData("用户数", trendData);
        }

        this.ciLabelUserUseService.addLabelUserUseLog(labelId, 3);
        return "trendChart";
    }

    public String findLabelUserTimesTrend() {
        String labelId = super.getRequest().getParameter("labelId");
        String dataDate = super.getRequest().getParameter("dataDate");
        CiLabelInfo labelInfo = this.ciLabelInfoService.queryCiLabelInfoById(new Integer(labelId));
        String labelType = labelInfo.getUpdateCycle().toString();
        List trendInfos = this.ciLabelTrendAnalysisService.queryLabelUseTimesTrendInfo(labelId, dataDate, labelType);
        this.labelTrendChart = new AIChart();
        if(null != trendInfos) {
            LinkedHashMap trendData = new LinkedHashMap();
            Iterator i$ = trendInfos.iterator();

            while(i$.hasNext()) {
                LabelTrendInfo info = (LabelTrendInfo)i$.next();
                trendData.put(info.getDataDate(), info.getValue());
            }

            this.labelTrendChart.addPYData("使用次数", trendData);
        }

        this.ciLabelUserUseService.addLabelUserUseLog(labelId, 3);
        return "trendChart";
    }

    public String findLabelUserNumTrendChartData() {
        String labelId = super.getRequest().getParameter("labelId");
        String dataDate = super.getRequest().getParameter("dataDate");
        String returnStr = "";
        this.log.debug("labelId->" + labelId + ",dataDate->" + dataDate);
        CiLabelInfo labelInfo = this.ciLabelInfoService.queryCiLabelInfoById(new Integer(labelId));
        String labelType = labelInfo.getUpdateCycle().toString();
        this.trendInfos = this.ciLabelTrendAnalysisService.queryLabelUserNumsTrendInfo(labelId, dataDate, labelType);
        if(null != this.trendInfos && this.trendInfos.size() > 0) {
            Iterator ctx = this.trendInfos.iterator();

            while(ctx.hasNext()) {
                LabelTrendInfo info = (LabelTrendInfo)ctx.next();
                if(labelType.equals("1")) {
                    String date = info.getDataDate();
                    boolean isWeek = WeekUtil.isWeekDay(date, "yyyyMMdd");
                    info.setWeek(isWeek);
                } else {
                    info.setWeek(false);
                }
            }

            returnStr = "labelTrend";
        } else {
            returnStr = null;
        }

        ActionContext ctx1 = ActionContext.getContext();
        ctx1.put("labelType", labelInfo.getUpdateCycle());
        ctx1.put("caption", "标签用户数趋势分析");
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CUS_TREND_CHART_VIEW", labelInfo.getLabelId().toString(), labelInfo.getLabelName(), "标签分析->标签用户数趋势图查看,【标签Id:" + labelInfo.getLabelId() + ",标签名称:" + labelInfo.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return returnStr;
    }

    public String findLabelUserTimesTrendChartData() {
        String labelId = super.getRequest().getParameter("labelId");
        String dataDate = super.getRequest().getParameter("dataDate");
        String returnStr = "";
        CiLabelInfo labelInfo = this.ciLabelInfoService.queryCiLabelInfoById(new Integer(labelId));
        String labelType = labelInfo.getUpdateCycle().toString();
        this.trendInfos = this.ciLabelTrendAnalysisService.queryLabelUseTimesTrendInfo(labelId, dataDate, labelType);
        if(null != this.trendInfos && this.trendInfos.size() > 0) {
            Iterator ctx = this.trendInfos.iterator();

            while(ctx.hasNext()) {
                LabelTrendInfo info = (LabelTrendInfo)ctx.next();
                if(labelType.equals("1")) {
                    String date = info.getDataDate();
                    boolean isWeek = WeekUtil.isWeekDay(date, "yyyyMMdd");
                    info.setWeek(isWeek);
                } else {
                    info.setWeek(false);
                }
            }

            returnStr = "labelTrend";
        } else {
            returnStr = null;
        }

        ActionContext ctx1 = ActionContext.getContext();
        ctx1.put("labelType", labelInfo.getUpdateCycle());
        ctx1.put("caption", "标签使用次数趋势分析");
        CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_USE_TREND_CHART_VIEW", labelInfo.getLabelId().toString(), labelInfo.getLabelName(), "标签分析->标签使用次数趋势图查看,【标签Id:" + labelInfo.getLabelId() + ",标签名称:" + labelInfo.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        return returnStr;
    }

    public String findLabelTrendTable() {
        if(null != this.pager) {
            this.log.info("Pager.Num->" + this.pager.getPageNum() + ",Pager.Size->" + this.pager.getPageSize());
        }

        String labelId = super.getRequest().getParameter("labelId");
        String dataDate = super.getRequest().getParameter("dataDate");
        String type = super.getRequest().getParameter("type");
        ActionContext ctx = ActionContext.getContext();
        ctx.put("labelId", labelId);
        ctx.put("dataDate", dataDate);
        CiLabelInfo labelInfo = this.ciLabelInfoService.queryCiLabelInfoById(new Integer(labelId));
        String labelType = labelInfo.getUpdateCycle().toString();
        if(this.pager == null) {
            this.pager = new Pager();
            this.pager.setPageSize(6);
        }

        if(this.pager.getTotalSize() == 0L) {
            this.pager.setTotalSize(this.ciLabelTrendAnalysisService.queryLabelTrednTableInfoNums(labelId, dataDate, labelType));
        }

        this.pager = this.pager.pagerFlip();
        List trendTableInfos = this.ciLabelTrendAnalysisService.queryLabelTrendTableInfo(this.pager.getPageNum(), 6, labelId, dataDate, labelType);
        this.pager.setResult(trendTableInfos);
        if("number".equals(type)) {
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_CUS_TREND_TABEL_VIEW", labelInfo.getLabelId().toString(), labelInfo.getLabelName(), "标签分析->标签用户数趋势表格查看,【标签Id:" + labelInfo.getLabelId() + ",标签名称:" + labelInfo.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        } else {
            CILogServiceUtil.getLogServiceInstance().log("COC_LABEL_USE_TREND_TABEL_VIEW", labelInfo.getLabelId().toString(), labelInfo.getLabelName(), "标签分析->标签使用次数趋势表格查看,【标签Id:" + labelInfo.getLabelId() + ",标签名称:" + labelInfo.getLabelName() + "】", OperResultEnum.Success, LogLevelEnum.Medium);
        }

        return "trendTable";
    }

    public AIChart getLabelTrendChart() {
        return this.labelTrendChart;
    }

    public void setLabelTrendChart(AIChart labelTrendChart) {
        this.labelTrendChart = labelTrendChart;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<LabelTrendInfo> getTrendInfos() {
        return this.trendInfos;
    }
}

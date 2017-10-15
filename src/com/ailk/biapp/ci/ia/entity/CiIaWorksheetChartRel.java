package com.ailk.biapp.ci.ia.entity;

import java.io.Serializable;

public class CiIaWorksheetChartRel implements Serializable {
    private static final long serialVersionUID = -6022097751486640123L;
    private Integer worksheetChartRelId;
    private Integer worksheetId;
    private String chartId;
    private String chartDataFileName;
    private String chartRuleDesc;

    public CiIaWorksheetChartRel() {
    }

    public CiIaWorksheetChartRel(Integer worksheetId, String chartId) {
        this.worksheetId = worksheetId;
        this.chartId = chartId;
    }

    public CiIaWorksheetChartRel(Integer worksheetId, String chartId, String chartDataFileName, String chartRuleDesc) {
        this.worksheetId = worksheetId;
        this.chartId = chartId;
        this.chartDataFileName = chartDataFileName;
        this.chartRuleDesc = chartRuleDesc;
    }

    public Integer getWorksheetChartRelId() {
        return this.worksheetChartRelId;
    }

    public void setWorksheetChartRelId(Integer worksheetChartRelId) {
        this.worksheetChartRelId = worksheetChartRelId;
    }

    public Integer getWorksheetId() {
        return this.worksheetId;
    }

    public void setWorksheetId(Integer worksheetId) {
        this.worksheetId = worksheetId;
    }

    public String getChartId() {
        return this.chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public String getChartDataFileName() {
        return this.chartDataFileName;
    }

    public void setChartDataFileName(String chartDataFileName) {
        this.chartDataFileName = chartDataFileName;
    }

    public String getChartRuleDesc() {
        return this.chartRuleDesc;
    }

    public void setChartRuleDesc(String chartRuleDesc) {
        this.chartRuleDesc = chartRuleDesc;
    }
}

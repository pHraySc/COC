package com.ailk.biapp.ci.ia.entity;

import java.io.Serializable;

public class DimChartInfo implements Serializable {
    private static final long serialVersionUID = -3840553104424823950L;
    private String chartId;
    private String chartName;
    private String chartDesc;

    public DimChartInfo() {
    }

    public DimChartInfo(String chartName, String chartDesc) {
        this.chartName = chartName;
        this.chartDesc = chartDesc;
    }

    public String getChartId() {
        return this.chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public String getChartName() {
        return this.chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getChartDesc() {
        return this.chartDesc;
    }

    public void setChartDesc(String chartDesc) {
        this.chartDesc = chartDesc;
    }
}

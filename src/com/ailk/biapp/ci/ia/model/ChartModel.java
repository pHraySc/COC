package com.ailk.biapp.ci.ia.model;

import com.ailk.biapp.ci.ia.model.Guideline;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ChartModel {
    private List<Guideline> guidelines;
    private List<Object> dimCols;
    private String text;
    private String subtext;
    private String xAxisName = "";
    private String xAxisUnit = "";
    private String yAxisName0 = "";
    private String yAxisName1 = "";
    private String yAxisUnit0 = "";
    private String yAxisUnit1 = "";
    private int valueAxises = 1;
    private String symbol;

    public ChartModel() {
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getxAxisName() {
        return this.xAxisName;
    }

    public void setxAxisName(String xAxisName) {
        this.xAxisName = xAxisName;
    }

    public String getyAxisName0() {
        return this.yAxisName0;
    }

    public void setyAxisName0(String yAxisName0) {
        this.yAxisName0 = yAxisName0;
    }

    public String getyAxisName1() {
        return this.yAxisName1;
    }

    public void setyAxisName1(String yAxisName1) {
        this.yAxisName1 = yAxisName1;
    }

    public String getyAxisUnit0() {
        return this.yAxisUnit0;
    }

    public void setyAxisUnit0(String yAxisUnit0) {
        this.yAxisUnit0 = yAxisUnit0;
    }

    public String getyAxisUnit1() {
        return this.yAxisUnit1;
    }

    public void setyAxisUnit1(String yAxisUnit1) {
        this.yAxisUnit1 = yAxisUnit1;
    }

    public int getValueAxises() {
        return this.valueAxises;
    }

    public void setValueAxises(int valueAxises) {
        this.valueAxises = valueAxises;
    }

    public String getxAxisUnit() {
        return this.xAxisUnit;
    }

    public List<Guideline> getGuidelines() {
        return this.guidelines;
    }

    public void setGuidelines(List<Guideline> guidelines) {
        this.guidelines = guidelines;
    }

    public List<Object> getDimCols() {
        return this.dimCols;
    }

    public void setDimCols(List<Object> dimCols) {
        this.dimCols = dimCols;
    }

    public String getText() {
        return StringUtils.isEmpty(this.text)?"":this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubtext() {
        return StringUtils.isEmpty(this.subtext)?"":this.subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public void setxAxisUnit(String xAxisUnit) {
        this.xAxisUnit = xAxisUnit;
    }
}

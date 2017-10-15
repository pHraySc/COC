package com.ailk.biapp.ci.ia.model;

import com.ailk.biapp.ci.ia.model.AttributeModel;
import java.util.List;

public class WorkSheetModel {
    private String workSheetId;
    private String workSheetName;
    private List<AttributeModel> row;
    private List<AttributeModel> column;
    private List<AttributeModel> symbol;
    private List<AttributeModel> filterInclude;
    private List<AttributeModel> filterExclude;
    private String listTableName;
    private String dataDate;
    private String chartType;
    private String chartData;

    public WorkSheetModel() {
    }

    public List<AttributeModel> getRow() {
        return this.row;
    }

    public void setRow(List<AttributeModel> row) {
        this.row = row;
    }

    public List<AttributeModel> getColumn() {
        return this.column;
    }

    public void setColumn(List<AttributeModel> column) {
        this.column = column;
    }

    public String getChartType() {
        return this.chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public List<AttributeModel> getSymbol() {
        return this.symbol;
    }

    public void setSymbol(List<AttributeModel> symbol) {
        this.symbol = symbol;
    }

    public String getListTableName() {
        return this.listTableName;
    }

    public void setListTableName(String listTableName) {
        this.listTableName = listTableName;
    }

    public String getWorkSheetId() {
        return this.workSheetId;
    }

    public void setWorkSheetId(String workSheetId) {
        this.workSheetId = workSheetId;
    }

    public String getWorkSheetName() {
        return this.workSheetName;
    }

    public void setWorkSheetName(String workSheetName) {
        this.workSheetName = workSheetName;
    }

    public String getChartData() {
        return this.chartData;
    }

    public void setChartData(String chartData) {
        this.chartData = chartData;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public List<AttributeModel> getFilterInclude() {
        return this.filterInclude;
    }

    public void setFilterInclude(List<AttributeModel> filterInclude) {
        this.filterInclude = filterInclude;
    }

    public List<AttributeModel> getFilterExclude() {
        return this.filterExclude;
    }

    public void setFilterExclude(List<AttributeModel> filterExclude) {
        this.filterExclude = filterExclude;
    }
}

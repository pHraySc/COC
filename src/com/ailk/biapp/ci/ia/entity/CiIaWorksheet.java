package com.ailk.biapp.ci.ia.entity;

import java.io.Serializable;

public class CiIaWorksheet implements Serializable {
    private static final long serialVersionUID = -3867946918992506270L;
    private Integer worksheetId;
    private String worksheetName;
    private Integer reportId;
    private Integer status;

    public CiIaWorksheet() {
    }

    public CiIaWorksheet(String worksheetName, Integer reportId, Integer status) {
        this.worksheetName = worksheetName;
        this.reportId = reportId;
        this.status = status;
    }

    public Integer getWorksheetId() {
        return this.worksheetId;
    }

    public void setWorksheetId(Integer worksheetId) {
        this.worksheetId = worksheetId;
    }

    public String getWorksheetName() {
        return this.worksheetName;
    }

    public void setWorksheetName(String worksheetName) {
        this.worksheetName = worksheetName;
    }

    public Integer getReportId() {
        return this.reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

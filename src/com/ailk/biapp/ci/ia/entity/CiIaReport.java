package com.ailk.biapp.ci.ia.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class CiIaReport implements Serializable {
    private static final long serialVersionUID = 3382644413220458826L;
    private Integer reportId;
    private String reportName;
    private String reportDesc;
    private String createUserId;
    private Timestamp createTime;
    private String listTableName;
    private String dataDate;
    private String customGroupId;
    private String customGroupName;
    private Integer status;
    private String reportFileName;

    public CiIaReport() {
    }

    public CiIaReport(String listTableName) {
        this.listTableName = listTableName;
    }

    public CiIaReport(String reportName, String reportDesc, String createUserId, Timestamp createTime, String listTableName, String dataDate, String customGroupId, String customGroupName, Integer status, String reportFileName) {
        this.reportName = reportName;
        this.reportDesc = reportDesc;
        this.createUserId = createUserId;
        this.createTime = createTime;
        this.listTableName = listTableName;
        this.dataDate = dataDate;
        this.customGroupId = customGroupId;
        this.customGroupName = customGroupName;
        this.status = status;
        this.reportFileName = reportFileName;
    }

    public Integer getReportId() {
        return this.reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return this.reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportDesc() {
        return this.reportDesc;
    }

    public void setReportDesc(String reportDesc) {
        this.reportDesc = reportDesc;
    }

    public String getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getListTableName() {
        return this.listTableName;
    }

    public void setListTableName(String listTableName) {
        this.listTableName = listTableName;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getCustomGroupId() {
        return this.customGroupId;
    }

    public void setCustomGroupId(String customGroupId) {
        this.customGroupId = customGroupId;
    }

    public String getCustomGroupName() {
        return this.customGroupName;
    }

    public void setCustomGroupName(String customGroupName) {
        this.customGroupName = customGroupName;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReportFileName() {
        return this.reportFileName;
    }

    public void setReportFileName(String reportFileName) {
        this.reportFileName = reportFileName;
    }
}

package com.ailk.biapp.ci.ia.model;

import com.ailk.biapp.ci.ia.model.WorkSheetModel;
import java.util.List;

public class WorkBookModel {
    private String reportName;
    private String reportDesc;
    private String groupId;
    private String groupName;
    
    private List<WorkSheetModel> wroksheet;

    public WorkBookModel() {
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

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<WorkSheetModel> getWroksheet() {
        return this.wroksheet;
    }

    public void setWroksheet(List<WorkSheetModel> wroksheet) {
        this.wroksheet = wroksheet;
    }
}

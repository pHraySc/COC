package com.ailk.biapp.ci.model;

import java.io.Serializable;
import java.util.Date;

public class LabelUseLogInfo implements Serializable {
    private static final long serialVersionUID = -3169832335333235231L;
    private String userId;
    private String userName;
    private String deptId;
    private String deptName;
    private Date useTime;
    private String useTimeStr;
    private String labelUseTypeId;
    private String labelUseTypeName;
    private String labelUseTypeDesc;
    private String labelId;
    private String labelName;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLabelUseTypeName() {
        return this.labelUseTypeName;
    }

    public void setLabelUseTypeName(String labelUseTypeName) {
        this.labelUseTypeName = labelUseTypeName;
    }

    public String getLabelUseTypeDesc() {
        return this.labelUseTypeDesc;
    }

    public void setLabelUseTypeDesc(String labelUseTypeDesc) {
        this.labelUseTypeDesc = labelUseTypeDesc;
    }

    public String getLabelId() {
        return this.labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return this.labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelUseTypeId() {
        return this.labelUseTypeId;
    }

    public void setLabelUseTypeId(String labelUseTypeId) {
        this.labelUseTypeId = labelUseTypeId;
    }

    public Date getUseTime() {
        return this.useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public String getUseTimeStr() {
        return this.useTimeStr;
    }

    public void setUseTimeStr(String useTimeStr) {
        this.useTimeStr = useTimeStr;
    }

    public LabelUseLogInfo() {
    }

    public LabelUseLogInfo(String userId, String userName, String deptId, String deptName, Date useTime, String useTimeStr, String labelUseTypeId, String labelUseTypeName, String labelUseTypeDesc, String labelId, String labelName) {
        this.userId = userId;
        this.userName = userName;
        this.deptId = deptId;
        this.deptName = deptName;
        this.useTime = useTime;
        this.useTimeStr = useTimeStr;
        this.labelUseTypeId = labelUseTypeId;
        this.labelUseTypeName = labelUseTypeName;
        this.labelUseTypeDesc = labelUseTypeDesc;
        this.labelId = labelId;
        this.labelName = labelName;
    }

    public String toString() {
        return "LabelUseLogInfo [deptId=" + this.deptId + ", deptName=" + this.deptName + ", labelId=" + this.labelId + ", labelName=" + this.labelName + ", labelUseTypeDesc=" + this.labelUseTypeDesc + ", labelUseTypeId=" + this.labelUseTypeId + ", labelUseTypeName=" + this.labelUseTypeName + ", useTime=" + this.useTime + ", useTimeStr=" + this.useTimeStr + ", userId=" + this.userId + ", userName=" + this.userName + "]";
    }
}

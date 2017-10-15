package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class ReturnMessageModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String interfaceAddress;
    private String sendTime;
    private String syncTaskId;
    private String returnCode;
    private String errorMsg;

    public ReturnMessageModel() {
    }

    public String getInterfaceAddress() {
        return this.interfaceAddress;
    }

    public void setInterfaceAddress(String interfaceAddress) {
        this.interfaceAddress = interfaceAddress;
    }

    public String getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSyncTaskId() {
        return this.syncTaskId;
    }

    public void setSyncTaskId(String syncTaskId) {
        this.syncTaskId = syncTaskId;
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String toString() {
        return "ReturnMessageModel [interfaceAddress=" + this.interfaceAddress + ", sendTime=" + this.sendTime + ", syncTaskId=" + this.syncTaskId + ", returnCode=" + this.returnCode + ", errorMsg=" + this.errorMsg + "]";
    }
}

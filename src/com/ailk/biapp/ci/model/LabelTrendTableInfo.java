package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class LabelTrendTableInfo implements Serializable {
    private static final long serialVersionUID = -1056754622875760129L;
    private String labelId;
    private String dataDate;
    private String customNum;
    private String useTimes;
    private String alarm;

    public String getLabelId() {
        return this.labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getCustomNum() {
        return this.customNum;
    }

    public void setCustomNum(String customNum) {
        this.customNum = customNum;
    }

    public String getUseTimes() {
        return this.useTimes;
    }

    public void setUseTimes(String useTimes) {
        this.useTimes = useTimes;
    }

    public String getAlarm() {
        return this.alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public LabelTrendTableInfo(String labelId, String dataDate, String customNum, String useTimes, String alarm) {
        this.labelId = labelId;
        this.dataDate = dataDate;
        this.customNum = customNum;
        this.useTimes = useTimes;
        this.alarm = alarm;
    }

    public LabelTrendTableInfo() {
    }

    public String toString() {
        return "LabelTrendTableInfo [alarm=" + this.alarm + ", customNum=" + this.customNum + ", dataDate=" + this.dataDate + ", labelId=" + this.labelId + ", useTimes=" + this.useTimes + "]";
    }
}

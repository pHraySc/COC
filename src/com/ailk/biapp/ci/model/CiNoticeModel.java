package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.entity.CiSysAnnouncement;
import java.util.Map;

public class CiNoticeModel {
    private int sysCount;
    private int personNoticeCount;
    private int labelAlarmCount;
    private int customerAlarmCount;
    private int recentNoticeCount;
    private String userName;
    private Map<String, CiPersonNotice> recentNoticeMap;
    private Map<String, CiSysAnnouncement> sysNoticeMap;

    public CiNoticeModel() {
    }

    public int getRecentNoticeCount() {
        return this.recentNoticeCount;
    }

    public void setRecentNoticeCount(int recentNoticeCount) {
        this.recentNoticeCount = recentNoticeCount;
    }

    public int getSysCount() {
        return this.sysCount;
    }

    public void setSysCount(int sysCount) {
        this.sysCount = sysCount;
    }

    public int getPersonNoticeCount() {
        return this.personNoticeCount;
    }

    public void setPersonNoticeCount(int personNoticeCount) {
        this.personNoticeCount = personNoticeCount;
    }

    public int getLabelAlarmCount() {
        return this.labelAlarmCount;
    }

    public void setLabelAlarmCount(int labelAlarmCount) {
        this.labelAlarmCount = labelAlarmCount;
    }

    public int getCustomerAlarmCount() {
        return this.customerAlarmCount;
    }

    public void setCustomerAlarmCount(int customerAlarmCount) {
        this.customerAlarmCount = customerAlarmCount;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, CiPersonNotice> getRecentNoticeMap() {
        return this.recentNoticeMap;
    }

    public void setRecentNoticeMap(Map<String, CiPersonNotice> recentNoticeMap) {
        this.recentNoticeMap = recentNoticeMap;
    }

    public Map<String, CiSysAnnouncement> getSysNoticeMap() {
        return this.sysNoticeMap;
    }

    public void setSysNoticeMap(Map<String, CiSysAnnouncement> sysNoticeMap) {
        this.sysNoticeMap = sysNoticeMap;
    }
}

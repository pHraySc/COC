package com.ailk.biapp.ci.entity;

import java.io.Serializable;

/**
 * Created by admin on 2017/4/24.
 */
public class CilabelCount implements Serializable{

    private String fLevelSortId;    //一级分类ID
    private String fLevelSortName;  //一级分类名
    private String fLevelSortStatus;    //一级分类状态

    private String sLevelSortId;    //二级分类ID
    private String sLevelSortName;  //二级分类名
    private String sLevelSortStatus;    //二级分类状态

    private String dadLabelId;    //父标签ID
    private String dadLabelName;  //父标签名
    private String dadLabelType;    //父标签类型
    private String dadLabelBusiCaliber;  //父标签业务口径
    private String dadLabelUpdateCycle;     //父标签更新周期
    private String dadLabelDataDate;        //父标签数据日期
    private String dadLabelCustomNum;       //父标签用户数
    private String dadLabelCreateTime;      //父标签创建时间

    private String sonLabelId;    //子标签ID
    private String sonLabelName;  //子标签名
    private String sonLabelType;    //子标签类型
    private String sonLabelBusiCaliber;  //子标签业务口径
    private String sonLabelUpdateCycle;     //子标签更新周期
    private String sonLabelDataDate;        //子标签数据日期
    private String sonLabelCustomNum;       //子标签用户数
    private String sonLabelCreateTime;      //子标签创建时间

    public CilabelCount() {
    }

    public CilabelCount(String fLevelSortId, String fLevelSortName, String fLevelSortStatus, String sLevelSortId, String sLevelSortName, String sLevelSortStatus, String dadLabelId, String dadLabelName, String dadLabelType, String dadLabelBusiCaliber, String dadLabelUpdateCycle, String dadLabelDataDate, String dadLabelCustomNum, String dadLabelCreateTime, String sonLabelId, String sonLabelName, String sonLabelType, String sonLabelBusiCaliber, String sonLabelUpdateCycle, String sonLabelDataDate, String sonLabelCustomNum, String sonLabelCreateTime) {
        this.fLevelSortId = fLevelSortId;
        this.fLevelSortName = fLevelSortName;
        this.fLevelSortStatus = fLevelSortStatus;
        this.sLevelSortId = sLevelSortId;
        this.sLevelSortName = sLevelSortName;
        this.sLevelSortStatus = sLevelSortStatus;
        this.dadLabelId = dadLabelId;
        this.dadLabelName = dadLabelName;
        this.dadLabelType = dadLabelType;
        this.dadLabelBusiCaliber = dadLabelBusiCaliber;
        this.dadLabelUpdateCycle = dadLabelUpdateCycle;
        this.dadLabelDataDate = dadLabelDataDate;
        this.dadLabelCustomNum = dadLabelCustomNum;
        this.dadLabelCreateTime = dadLabelCreateTime;
        this.sonLabelId = sonLabelId;
        this.sonLabelName = sonLabelName;
        this.sonLabelType = sonLabelType;
        this.sonLabelBusiCaliber = sonLabelBusiCaliber;
        this.sonLabelUpdateCycle = sonLabelUpdateCycle;
        this.sonLabelDataDate = sonLabelDataDate;
        this.sonLabelCustomNum = sonLabelCustomNum;
        this.sonLabelCreateTime = sonLabelCreateTime;
    }

    public String getfLevelSortId() {
        return fLevelSortId;
    }

    public void setfLevelSortId(String fLevelSortId) {
        this.fLevelSortId = fLevelSortId;
    }

    public String getfLevelSortName() {
        return fLevelSortName;
    }

    public void setfLevelSortName(String fLevelSortName) {
        this.fLevelSortName = fLevelSortName;
    }

    public String getfLevelSortStatus() {
        return fLevelSortStatus;
    }

    public void setfLevelSortStatus(String fLevelSortStatus) {
        this.fLevelSortStatus = fLevelSortStatus;
    }

    public String getsLevelSortId() {
        return sLevelSortId;
    }

    public void setsLevelSortId(String sLevelSortId) {
        this.sLevelSortId = sLevelSortId;
    }

    public String getsLevelSortName() {
        return sLevelSortName;
    }

    public void setsLevelSortName(String sLevelSortName) {
        this.sLevelSortName = sLevelSortName;
    }

    public String getsLevelSortStatus() {
        return sLevelSortStatus;
    }

    public void setsLevelSortStatus(String sLevelSortStatus) {
        this.sLevelSortStatus = sLevelSortStatus;
    }

    public String getDadLabelId() {
        return dadLabelId;
    }

    public void setDadLabelId(String dadLabelId) {
        this.dadLabelId = dadLabelId;
    }

    public String getDadLabelName() {
        return dadLabelName;
    }

    public void setDadLabelName(String dadLabelName) {
        this.dadLabelName = dadLabelName;
    }

    public String getDadLabelType() {
        return dadLabelType;
    }

    public void setDadLabelType(String dadLabelType) {
        this.dadLabelType = dadLabelType;
    }

    public String getDadLabelBusiCaliber() {
        return dadLabelBusiCaliber;
    }

    public void setDadLabelBusiCaliber(String dadLabelBusiCaliber) {
        this.dadLabelBusiCaliber = dadLabelBusiCaliber;
    }

    public String getDadLabelUpdateCycle() {
        return dadLabelUpdateCycle;
    }

    public void setDadLabelUpdateCycle(String dadLabelUpdateCycle) {
        this.dadLabelUpdateCycle = dadLabelUpdateCycle;
    }

    public String getDadLabelDataDate() {
        return dadLabelDataDate;
    }

    public void setDadLabelDataDate(String dadLabelDataDate) {
        this.dadLabelDataDate = dadLabelDataDate;
    }

    public String getDadLabelCustomNum() {
        return dadLabelCustomNum;
    }

    public void setDadLabelCustomNum(String dadLabelCustomNum) {
        this.dadLabelCustomNum = dadLabelCustomNum;
    }

    public String getDadLabelCreateTime() {
        return dadLabelCreateTime;
    }

    public void setDadLabelCreateTime(String dadLabelCreateTime) {
        this.dadLabelCreateTime = dadLabelCreateTime;
    }

    public String getSonLabelId() {
        return sonLabelId;
    }

    public void setSonLabelId(String sonLabelId) {
        this.sonLabelId = sonLabelId;
    }

    public String getSonLabelName() {
        return sonLabelName;
    }

    public void setSonLabelName(String sonLabelName) {
        this.sonLabelName = sonLabelName;
    }

    public String getSonLabelType() {
        return sonLabelType;
    }

    public void setSonLabelType(String sonLabelType) {
        this.sonLabelType = sonLabelType;
    }

    public String getSonLabelBusiCaliber() {
        return sonLabelBusiCaliber;
    }

    public void setSonLabelBusiCaliber(String sonLabelBusiCaliber) {
        this.sonLabelBusiCaliber = sonLabelBusiCaliber;
    }

    public String getSonLabelUpdateCycle() {
        return sonLabelUpdateCycle;
    }

    public void setSonLabelUpdateCycle(String sonLabelUpdateCycle) {
        this.sonLabelUpdateCycle = sonLabelUpdateCycle;
    }

    public String getSonLabelDataDate() {
        return sonLabelDataDate;
    }

    public void setSonLabelDataDate(String sonLabelDataDate) {
        this.sonLabelDataDate = sonLabelDataDate;
    }

    public String getSonLabelCustomNum() {
        return sonLabelCustomNum;
    }

    public void setSonLabelCustomNum(String sonLabelCustomNum) {
        this.sonLabelCustomNum = sonLabelCustomNum;
    }

    public String getSonLabelCreateTime() {
        return sonLabelCreateTime;
    }

    public void setSonLabelCreateTime(String sonLabelCreateTime) {
        this.sonLabelCreateTime = sonLabelCreateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CilabelCount)) return false;

        CilabelCount that = (CilabelCount) o;

        if (!getfLevelSortId().equals(that.getfLevelSortId())) return false;
        if (!getfLevelSortName().equals(that.getfLevelSortName())) return false;
        if (!getfLevelSortStatus().equals(that.getfLevelSortStatus())) return false;
        if (!getsLevelSortId().equals(that.getsLevelSortId())) return false;
        if (!getsLevelSortName().equals(that.getsLevelSortName())) return false;
        if (!getsLevelSortStatus().equals(that.getsLevelSortStatus())) return false;
        if (!getDadLabelId().equals(that.getDadLabelId())) return false;
        if (!getDadLabelName().equals(that.getDadLabelName())) return false;
        if (!getDadLabelType().equals(that.getDadLabelType())) return false;
        if (!getDadLabelBusiCaliber().equals(that.getDadLabelBusiCaliber())) return false;
        if (!getDadLabelUpdateCycle().equals(that.getDadLabelUpdateCycle())) return false;
        if (!getDadLabelDataDate().equals(that.getDadLabelDataDate())) return false;
        if (getDadLabelCustomNum() != null ? !getDadLabelCustomNum().equals(that.getDadLabelCustomNum()) : that.getDadLabelCustomNum() != null)
            return false;
        if (getDadLabelCreateTime() != null ? !getDadLabelCreateTime().equals(that.getDadLabelCreateTime()) : that.getDadLabelCreateTime() != null)
            return false;
        if (getSonLabelId() != null ? !getSonLabelId().equals(that.getSonLabelId()) : that.getSonLabelId() != null)
            return false;
        if (getSonLabelName() != null ? !getSonLabelName().equals(that.getSonLabelName()) : that.getSonLabelName() != null)
            return false;
        if (getSonLabelType() != null ? !getSonLabelType().equals(that.getSonLabelType()) : that.getSonLabelType() != null)
            return false;
        if (getSonLabelBusiCaliber() != null ? !getSonLabelBusiCaliber().equals(that.getSonLabelBusiCaliber()) : that.getSonLabelBusiCaliber() != null)
            return false;
        if (getSonLabelUpdateCycle() != null ? !getSonLabelUpdateCycle().equals(that.getSonLabelUpdateCycle()) : that.getSonLabelUpdateCycle() != null)
            return false;
        if (getSonLabelDataDate() != null ? !getSonLabelDataDate().equals(that.getSonLabelDataDate()) : that.getSonLabelDataDate() != null)
            return false;
        if (getSonLabelCustomNum() != null ? !getSonLabelCustomNum().equals(that.getSonLabelCustomNum()) : that.getSonLabelCustomNum() != null)
            return false;
        return getSonLabelCreateTime() != null ? getSonLabelCreateTime().equals(that.getSonLabelCreateTime()) : that.getSonLabelCreateTime() == null;
    }

    @Override
    public int hashCode() {
        int result = getfLevelSortId().hashCode();
        result = 31 * result + getfLevelSortName().hashCode();
        result = 31 * result + getfLevelSortStatus().hashCode();
        result = 31 * result + getsLevelSortId().hashCode();
        result = 31 * result + getsLevelSortName().hashCode();
        result = 31 * result + getsLevelSortStatus().hashCode();
        result = 31 * result + getDadLabelId().hashCode();
        result = 31 * result + getDadLabelName().hashCode();
        result = 31 * result + getDadLabelType().hashCode();
        result = 31 * result + getDadLabelBusiCaliber().hashCode();
        result = 31 * result + getDadLabelUpdateCycle().hashCode();
        result = 31 * result + getDadLabelDataDate().hashCode();
        result = 31 * result + (getDadLabelCustomNum() != null ? getDadLabelCustomNum().hashCode() : 0);
        result = 31 * result + (getDadLabelCreateTime() != null ? getDadLabelCreateTime().hashCode() : 0);
        result = 31 * result + (getSonLabelId() != null ? getSonLabelId().hashCode() : 0);
        result = 31 * result + (getSonLabelName() != null ? getSonLabelName().hashCode() : 0);
        result = 31 * result + (getSonLabelType() != null ? getSonLabelType().hashCode() : 0);
        result = 31 * result + (getSonLabelBusiCaliber() != null ? getSonLabelBusiCaliber().hashCode() : 0);
        result = 31 * result + (getSonLabelUpdateCycle() != null ? getSonLabelUpdateCycle().hashCode() : 0);
        result = 31 * result + (getSonLabelDataDate() != null ? getSonLabelDataDate().hashCode() : 0);
        result = 31 * result + (getSonLabelCustomNum() != null ? getSonLabelCustomNum().hashCode() : 0);
        result = 31 * result + (getSonLabelCreateTime() != null ? getSonLabelCreateTime().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CilabelCount{" +
                "fLevelSortId='" + fLevelSortId + '\'' +
                ", fLevelSortName='" + fLevelSortName + '\'' +
                ", fLevelSortStatus='" + fLevelSortStatus + '\'' +
                ", sLevelSortId='" + sLevelSortId + '\'' +
                ", sLevelSortName='" + sLevelSortName + '\'' +
                ", sLevelSortStatus='" + sLevelSortStatus + '\'' +
                ", dadLabelId='" + dadLabelId + '\'' +
                ", dadLabelName='" + dadLabelName + '\'' +
                ", dadLabelType='" + dadLabelType + '\'' +
                ", dadLabelBusiCaliber='" + dadLabelBusiCaliber + '\'' +
                ", dadLabelUpdateCycle='" + dadLabelUpdateCycle + '\'' +
                ", dadLabelDataDate='" + dadLabelDataDate + '\'' +
                ", dadLabelCustomNum='" + dadLabelCustomNum + '\'' +
                ", dadLabelCreateTime='" + dadLabelCreateTime + '\'' +
                ", sonLabelId='" + sonLabelId + '\'' +
                ", sonLabelName='" + sonLabelName + '\'' +
                ", sonLabelType='" + sonLabelType + '\'' +
                ", sonLabelBusiCaliber='" + sonLabelBusiCaliber + '\'' +
                ", sonLabelUpdateCycle='" + sonLabelUpdateCycle + '\'' +
                ", sonLabelDataDate='" + sonLabelDataDate + '\'' +
                ", sonLabelCustomNum='" + sonLabelCustomNum + '\'' +
                ", sonLabelCreateTime='" + sonLabelCreateTime + '\'' +
                '}';
    }
}

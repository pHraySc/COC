package com.ailk.biapp.ci.entity;

import java.io.Serializable;

/**
 * Created by Sc on 2017/4/21.
 */
public class CiDataSource implements Serializable{

    private static final long serialVersionUID = 1L;
    private String labelName;   //标签名
    private String labelId;     //标签ID
    private String dataSrcCode;      //数据源ID
    private String dataSrcTabName;     //数据源表名
    private String contactTodataSourceTable;        //数据源表对应的提供方或联系人
    private String isMissing;       //是否缺失

    public CiDataSource() {
    }

    public CiDataSource(String labelName, String labelId, String dataSourceCode, String dataSourceTableName, String contactTodataSourceTable, String isMissing) {
        this.labelName = labelName;
        this.labelId = labelId;
        this.dataSrcCode = dataSourceCode;
        this.contactTodataSourceTable = dataSourceTableName;
        this.contactTodataSourceTable = contactTodataSourceTable;
        this.isMissing = isMissing;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getContactTodataSourceTable() {
        return contactTodataSourceTable;
    }

    public void setContactTodataSourceTable(String contactTodataSourceTable) {
        this.contactTodataSourceTable = contactTodataSourceTable;
    }

    public String getIsMissing() {
        return isMissing;
    }

    public void setIsMissing(String isMissing) {
        this.isMissing = isMissing;
    }

    public String getDataSrcCode() {
        return dataSrcCode;
    }

    public void setDataSrcCode(String dataSrcCode) {
        this.dataSrcCode = dataSrcCode;
    }

    public String getDataSrcTabName() {
        return dataSrcTabName;
    }

    public void setDataSrcTabName(String dataSrcTabName) {
        this.dataSrcTabName = dataSrcTabName;
    }

    @Override
    public String toString() {
        return "CiDataSource{" +
                "labelName='" + labelName + '\'' +
                ", labelId='" + labelId + '\'' +
                ", dataSourceCode='" + dataSrcCode + '\'' +
                ", dataSourceTableName='" + dataSrcTabName + '\'' +
                ", contactTodataSourceTable='" + contactTodataSourceTable + '\'' +
                ", isMissing='" + isMissing + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CiDataSource)) return false;

        CiDataSource that = (CiDataSource) o;

        if (!getLabelName().equals(that.getLabelName())) return false;
        if (!getLabelId().equals(that.getLabelId())) return false;
        if (!getDataSrcCode().equals(that.getDataSrcCode())) return false;
        if (!getDataSrcTabName().equals(that.getDataSrcTabName())) return false;
        if (!getContactTodataSourceTable().equals(that.getContactTodataSourceTable())) return false;
        return getIsMissing().equals(that.getIsMissing());
    }

    @Override
    public int hashCode() {
        int result = getLabelName().hashCode();
        result = 31 * result + getLabelId().hashCode();
        result = 31 * result + getDataSrcCode().hashCode();
        result = 31 * result + getDataSrcTabName().hashCode();
        result = 31 * result + getContactTodataSourceTable().hashCode();
        result = 31 * result + getIsMissing().hashCode();
        return result;
    }
}

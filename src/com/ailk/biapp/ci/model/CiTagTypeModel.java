package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.entity.CiTagInfo;
import java.util.List;

public class CiTagTypeModel {
    private Integer id;
    private String typeName;
    private String icon;
    private List<CiTagInfo> ciTagInfoList;

    public CiTagTypeModel() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<CiTagInfo> getCiTagInfoList() {
        return this.ciTagInfoList;
    }

    public void setCiTagInfoList(List<CiTagInfo> ciTagInfoList) {
        this.ciTagInfoList = ciTagInfoList;
    }
}

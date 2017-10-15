package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.model.CrmLabelOrIndexModel;
import java.util.ArrayList;
import java.util.List;

public class CrmTypeModel {
    private String typeId;
    private String typeName;
    private String typeDesc;
    private String icon;
    private List<String> elementIdList;
    private List<CrmLabelOrIndexModel> displayLabelOrIndexList = new ArrayList();

    public CrmTypeModel() {
    }

    public String getTypeId() {
        return this.typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<String> getElementIdList() {
        return this.elementIdList;
    }

    public void setElementIdList(List<String> elementIdList) {
        this.elementIdList = elementIdList;
    }

    public List<CrmLabelOrIndexModel> getDisplayLabelOrIndexList() {
        return this.displayLabelOrIndexList;
    }

    public void setDisplayLabelOrIndexList(List<CrmLabelOrIndexModel> displayLabelOrIndexList) {
        this.displayLabelOrIndexList = displayLabelOrIndexList;
    }

    public void addDisplayLabelOrIndex(CrmLabelOrIndexModel model) {
        this.displayLabelOrIndexList.add(model);
    }

    public String toString() {
        return "CrmTypeModel [typeId=" + this.typeId + ", typeName=" + this.typeName + ", typeDesc=" + this.typeDesc + ", icon=" + this.icon + ", elementIdList=" + this.elementIdList + ", displayLabelOrIndexList=" + this.displayLabelOrIndexList + "]";
    }

    public String getTypeDesc() {
        return this.typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

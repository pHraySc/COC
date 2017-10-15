package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class PeopleLabelInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String labelId;
    private String labelName;
    private Integer typeId;
    private Integer isImportant;

    public PeopleLabelInfo() {
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

    public Integer getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getIsImportant() {
        return this.isImportant;
    }

    public void setIsImportant(Integer isImportant) {
        this.isImportant = isImportant;
    }
}

package com.ailk.biapp.ci.model;

public class CrmLabelOrIndexModel {
    private String id;
    private String name;
    private String value;
    private boolean isHighLight;
    private boolean isLabel;
    private String displayName;
    private String busiCaliber;

    public CrmLabelOrIndexModel() {
    }

    public String getBusiCaliber() {
        return this.busiCaliber;
    }

    public void setBusiCaliber(String busiCaliber) {
        this.busiCaliber = busiCaliber;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHighLight() {
        return this.isHighLight;
    }

    public void setHighLight(boolean highLight) {
        this.isHighLight = highLight;
    }

    public boolean isLabel() {
        return this.isLabel;
    }

    public void setLabel(boolean label) {
        this.isLabel = label;
    }

    public String getDisplayName() {
        String displayName = this.name;
        if(!this.isLabel) {
            displayName = this.name + ":" + this.value;
        }

        return displayName;
    }
}

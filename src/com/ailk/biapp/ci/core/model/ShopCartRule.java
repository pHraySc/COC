package com.ailk.biapp.ci.core.model;

import java.io.Serializable;

public class ShopCartRule implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private String operatorElement;
    private String lableOrCustomName;
    private String attrValue;
    private Boolean isSetValue = Boolean.valueOf(true);

    public ShopCartRule(String operatorElement, String lableOrCustomName, String attrValue, Boolean isSetValue) {
        this.operatorElement = operatorElement;
        this.lableOrCustomName = lableOrCustomName;
        this.attrValue = attrValue;
        this.isSetValue = isSetValue;
    }

    public ShopCartRule() {
    }

    public String getOperatorElement() {
        return this.operatorElement;
    }

    public void setOperatorElement(String operatorElement) {
        this.operatorElement = operatorElement;
    }

    public String getLableOrCustomName() {
        return this.lableOrCustomName;
    }

    public void setLableOrCustomName(String lableOrCustomName) {
        this.lableOrCustomName = lableOrCustomName;
    }

    public String getAttrValue() {
        return this.attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public Boolean getIsSetValue() {
        return this.isSetValue;
    }

    public void setIsSetValue(Boolean isSetValue) {
        this.isSetValue = isSetValue;
    }
}

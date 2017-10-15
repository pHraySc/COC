package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.model.CiLabelFormModel;
import java.util.List;

public class CiLabelFormTrendModel {
    private Integer brandId;
    private String brandName;
    private Integer vipLevelId;
    private String vipLevelName;
    private Integer cityId;
    private String cityName;
    private String color;
    private List<CiLabelFormModel> labelFormList;

    public CiLabelFormTrendModel() {
    }

    public String getVipLevelName() {
        return this.vipLevelName;
    }

    public void setVipLevelName(String vipLevelName) {
        this.vipLevelName = vipLevelName;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getCityId() {
        return this.cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getVipLevelId() {
        return this.vipLevelId;
    }

    public void setVipLevelId(Integer vipLevelId) {
        this.vipLevelId = vipLevelId;
    }

    public Integer getBrandId() {
        return this.brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public List<CiLabelFormModel> getLabelFormList() {
        return this.labelFormList;
    }

    public void setLabelFormList(List<CiLabelFormModel> labelFormList) {
        this.labelFormList = labelFormList;
    }
}

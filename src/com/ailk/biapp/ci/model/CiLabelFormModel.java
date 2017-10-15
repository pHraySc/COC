package com.ailk.biapp.ci.model;

public class CiLabelFormModel {
    private String dataDate;
    private Integer labelId;
    private Integer cityId;
    private String cityName;
    private Integer vipLevelId;
    private String vipLevelName;
    private Integer brandId;
    private String brand_name;
    private Integer customNum;
    private Integer ringNum;
    private Double proportion;
    private Integer updateCycle;
    private boolean aBrandFlag;
    private String color;

    public CiLabelFormModel() {
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getVipLevelName() {
        return this.vipLevelName;
    }

    public void setVipLevelName(String vipLevelName) {
        this.vipLevelName = vipLevelName;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getUpdateCycle() {
        return this.updateCycle;
    }

    public void setUpdateCycle(Integer updateCycle) {
        this.updateCycle = updateCycle;
    }

    public String getBrand_name() {
        return this.brand_name;
    }

    public void setBrand_name(String brandName) {
        this.brand_name = brandName;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public Integer getLabelId() {
        return this.labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
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

    public Integer getCustomNum() {
        return this.customNum;
    }

    public void setCustomNum(Integer customNum) {
        this.customNum = customNum;
    }

    public Integer getRingNum() {
        return this.ringNum;
    }

    public void setRingNum(Integer ringNum) {
        this.ringNum = ringNum;
    }

    public Double getProportion() {
        return this.proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }

    public boolean isaBrandFlag() {
        return this.aBrandFlag;
    }

    public void setaBrandFlag(boolean aBrandFlag) {
        this.aBrandFlag = aBrandFlag;
    }
}

package com.ailk.biapp.ci.model;

public class MapLimit {
    public static final String COMPARE_TYPE_1 = "1";
    public static final String COMPARE_TYPE_2 = "2";
    public static final String COMPARE_TYPE_3 = "3";
    public static final String COMPARE_TYPE_4 = "4";
    private Double maxValue;
    private Double minValue;
    private String color;
    private String seriesName;
    private String compareType;

    public MapLimit() {
    }

    public Double getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Double getMinValue() {
        return this.minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSeriesName() {
        return this.seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getCompareType() {
        return this.compareType;
    }

    public void setCompareType(String compareType) {
        this.compareType = compareType;
    }

    public int compare(Double value) {
        byte rs = 0;
        if(this.minValue == null) {
            this.minValue = Double.valueOf(4.9E-324D);
        }

        if(this.maxValue == null) {
            this.maxValue = Double.valueOf(1.7976931348623157E308D);
        }

        if("1".equals(this.compareType)) {
            if(value.doubleValue() >= this.minValue.doubleValue() && value.doubleValue() < this.maxValue.doubleValue()) {
                rs = 1;
            }
        } else if("2".equals(this.compareType)) {
            if(value.doubleValue() > this.minValue.doubleValue() && value.doubleValue() <= this.maxValue.doubleValue()) {
                rs = 1;
            }
        } else if("3".equals(this.compareType)) {
            if(value.doubleValue() > this.minValue.doubleValue() && value.doubleValue() < this.maxValue.doubleValue()) {
                rs = 1;
            }
        } else if("4".equals(this.compareType)) {
            if(value.doubleValue() >= this.minValue.doubleValue() && value.doubleValue() <= this.maxValue.doubleValue()) {
                rs = 1;
            }
        } else if(value.doubleValue() >= this.minValue.doubleValue() && value.doubleValue() < this.maxValue.doubleValue()) {
            rs = 1;
        }

        return rs;
    }
}

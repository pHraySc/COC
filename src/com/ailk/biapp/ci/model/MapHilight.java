package com.ailk.biapp.ci.model;

import java.util.Map;

public class MapHilight {
    private String id;
    private String shape;
    private Boolean fill;
    private String fillColor;
    private String fillOpacity;
    private Boolean stroke;
    private String strokeColor;
    private String strokeOpacity;
    private String strokeWidth;
    private Boolean fade;
    private Map<String, String> toolTip;
    private String cityName;
    private String coords;
    private String herf = "javascript:void(0)";

    public MapHilight() {
    }

    public String getShape() {
        return this.shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Boolean getFill() {
        return this.fill;
    }

    public void setFill(Boolean fill) {
        this.fill = fill;
    }

    public String getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public String getFillOpacity() {
        return this.fillOpacity;
    }

    public void setFillOpacity(String fillOpacity) {
        this.fillOpacity = fillOpacity;
    }

    public Boolean getStroke() {
        return this.stroke;
    }

    public void setStroke(Boolean stroke) {
        this.stroke = stroke;
    }

    public String getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public String getStrokeOpacity() {
        return this.strokeOpacity;
    }

    public void setStrokeOpacity(String strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
    }

    public String getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(String strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public Boolean getFade() {
        return this.fade;
    }

    public void setFade(Boolean fade) {
        this.fade = fade;
    }

    public String getCoords() {
        return this.coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getHerf() {
        return this.herf;
    }

    public void setHerf(String herf) {
        this.herf = herf;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getToolTip() {
        return this.toolTip;
    }

    public void setToolTip(Map<String, String> toolTip) {
        this.toolTip = toolTip;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}

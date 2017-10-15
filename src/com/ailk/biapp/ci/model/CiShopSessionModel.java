package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiTemplateInfo;

public class CiShopSessionModel {
    private CiLabelInfo ciLabelInfo;
    private CiCustomGroupInfo ciCustomGroupInfo;
    private CiTemplateInfo ciTemplateInfo;
    private String calculationsTypeId;
    private Integer calculationsSort;
    private String isEditCustomFlag;

    public CiShopSessionModel() {
    }

    public CiShopSessionModel(CiLabelInfo ciLabelInfo, CiCustomGroupInfo ciCustomGroupInfo, CiTemplateInfo ciTemplateInfo, String calculationsTypeId, Integer calculationsSort, String isEditCustomFlag) {
        this.ciLabelInfo = ciLabelInfo;
        this.ciCustomGroupInfo = ciCustomGroupInfo;
        this.ciTemplateInfo = ciTemplateInfo;
        this.calculationsTypeId = calculationsTypeId;
        this.calculationsSort = calculationsSort;
        this.isEditCustomFlag = isEditCustomFlag;
    }

    public CiShopSessionModel(CiCustomGroupInfo ciCustomGroupInfo, String calculationsTypeId, Integer calculationsSort, String isEditCustomFlag) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
        this.calculationsTypeId = calculationsTypeId;
        this.calculationsSort = calculationsSort;
        this.isEditCustomFlag = isEditCustomFlag;
    }

    public CiShopSessionModel(CiTemplateInfo ciTemplateInfo, String calculationsTypeId, Integer calculationsSort, String isEditCustomFlag) {
        this.ciTemplateInfo = ciTemplateInfo;
        this.calculationsTypeId = calculationsTypeId;
        this.calculationsSort = calculationsSort;
        this.isEditCustomFlag = isEditCustomFlag;
    }

    public CiShopSessionModel(CiLabelInfo ciLabelInfo, String calculationsTypeId, Integer calculationsSort, String isEditCustomFlag) {
        this.ciLabelInfo = ciLabelInfo;
        this.calculationsTypeId = calculationsTypeId;
        this.calculationsSort = calculationsSort;
        this.isEditCustomFlag = isEditCustomFlag;
    }

    public CiLabelInfo getCiLabelInfo() {
        return this.ciLabelInfo;
    }

    public void setCiLabelInfo(CiLabelInfo ciLabelInfo) {
        this.ciLabelInfo = ciLabelInfo;
    }

    public CiCustomGroupInfo getCiCustomGroupInfo() {
        return this.ciCustomGroupInfo;
    }

    public void setCiCustomGroupInfo(CiCustomGroupInfo ciCustomGroupInfo) {
        this.ciCustomGroupInfo = ciCustomGroupInfo;
    }

    public CiTemplateInfo getCiTemplateInfo() {
        return this.ciTemplateInfo;
    }

    public void setCiTemplateInfo(CiTemplateInfo ciTemplateInfo) {
        this.ciTemplateInfo = ciTemplateInfo;
    }

    public String getCalculationsTypeId() {
        return this.calculationsTypeId;
    }

    public void setCalculationsTypeId(String calculationsTypeId) {
        this.calculationsTypeId = calculationsTypeId;
    }

    public Integer getCalculationsSort() {
        return this.calculationsSort;
    }

    public void setCalculationsSort(Integer calculationsSort) {
        this.calculationsSort = calculationsSort;
    }

    public String getIsEditCustomFlag() {
        return this.isEditCustomFlag;
    }

    public void setIsEditCustomFlag(String isEditCustomFlag) {
        this.isEditCustomFlag = isEditCustomFlag;
    }
}

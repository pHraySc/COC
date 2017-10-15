package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import java.io.Serializable;
import java.util.Date;

public class ProductDetailInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer productId;
    private String productName;
    private Integer parentId;
    private String descTxt;
    private Integer categoryId;
    private String brandId;
    private String deptId;
    private String createUserId;
    private Date createTime;
    private String dutyUser;
    private String productMean;
    private Integer priority;
    private Integer status;
    private Date effecTime;
    private Date failTime;
    private String mutexRule;
    private String busiCaliber;
    private CiMdaSysTableColumn ciMdaSysTableColumn;
    private String effectDate;
    private String brandNames;
    private String ancestorNames;

    public ProductDetailInfo() {
    }

    public ProductDetailInfo(String productName, Integer parentId, String descTxt, Integer categoryId, String brandId, String deptId, String createUserId, Date createTime, String dutyUser, String productMean, Integer priority, Integer status, Date effecTime, Date failTime, String mutexRule, Integer columnId) {
        this.productName = productName;
        this.parentId = parentId;
        this.descTxt = descTxt;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.deptId = deptId;
        this.createUserId = createUserId;
        this.createTime = createTime;
        this.dutyUser = dutyUser;
        this.productMean = productMean;
        this.priority = priority;
        this.status = status;
        this.effecTime = effecTime;
        this.failTime = failTime;
        this.mutexRule = mutexRule;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getDescTxt() {
        return this.descTxt;
    }

    public void setDescTxt(String descTxt) {
        this.descTxt = descTxt;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getBrandId() {
        return this.brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDutyUser() {
        return this.dutyUser;
    }

    public void setDutyUser(String dutyUser) {
        this.dutyUser = dutyUser;
    }

    public String getProductMean() {
        return this.productMean;
    }

    public void setProductMean(String productMean) {
        this.productMean = productMean;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getEffecTime() {
        return this.effecTime;
    }

    public void setEffecTime(Date effecTime) {
        this.effecTime = effecTime;
    }

    public Date getFailTime() {
        return this.failTime;
    }

    public void setFailTime(Date failTime) {
        this.failTime = failTime;
    }

    public String getMutexRule() {
        return this.mutexRule;
    }

    public void setMutexRule(String mutexRule) {
        this.mutexRule = mutexRule;
    }

    public CiMdaSysTableColumn getCiMdaSysTableColumn() {
        return this.ciMdaSysTableColumn;
    }

    public void setCiMdaSysTableColumn(CiMdaSysTableColumn ciMdaSysTableColumn) {
        this.ciMdaSysTableColumn = ciMdaSysTableColumn;
    }

    public String getEffectDate() {
        return this.effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    public String getBusiCaliber() {
        return this.busiCaliber;
    }

    public void setBusiCaliber(String busiCaliber) {
        this.busiCaliber = busiCaliber;
    }

    public String toString() {
        return "CiProductInfo [productId=" + this.productId + ", productName=" + this.productName + ", parentId=" + this.parentId + ", descTxt=" + this.descTxt + ", categoryId=" + this.categoryId + ", brandId=" + this.brandId + ", deptId=" + this.deptId + ", createUserId=" + this.createUserId + ", createTime=" + this.createTime + ", dutyUser=" + this.dutyUser + ", productMean=" + this.productMean + ", priority=" + this.priority + ", status=" + this.status + ", effecTime=" + this.effecTime + ", failTime=" + this.failTime + ", mutexRule=" + this.mutexRule + "]";
    }

    public String getBrandNames() {
        return this.brandNames;
    }

    public void setBrandNames(String brandNames) {
        this.brandNames = brandNames;
    }

    public String getAncestorNames() {
        return this.ancestorNames;
    }

    public void setAncestorNames(String ancestorNames) {
        this.ancestorNames = ancestorNames;
    }
}

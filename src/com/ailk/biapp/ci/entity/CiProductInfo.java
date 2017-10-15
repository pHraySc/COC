// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiProductInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiMdaSysTableColumn

public class CiProductInfo
	implements Serializable
{

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
	private Integer isProduct;
	private CiMdaSysTableColumn ciMdaSysTableColumn;
	private String effectDate;
	private String brandNames;
	private String effectPeriod;
	private String offerId;
	private String categoryName;
	private String ruleDesc;

	public CiProductInfo()
	{
	}

	public CiProductInfo(String productName, Integer parentId, String descTxt, Integer categoryId, String brandId, String deptId, String createUserId, 
			Date createTime, String dutyUser, String productMean, Integer priority, Integer status, Date effecTime, Date failTime, 
			String mutexRule, Integer columnId)
	{
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

	public Integer getProductId()
	{
		return productId;
	}

	public void setProductId(Integer productId)
	{
		this.productId = productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public Integer getParentId()
	{
		return parentId;
	}

	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}

	public String getDescTxt()
	{
		return descTxt;
	}

	public void setDescTxt(String descTxt)
	{
		this.descTxt = descTxt;
	}

	public Integer getCategoryId()
	{
		return categoryId;
	}

	public void setCategoryId(Integer categoryId)
	{
		this.categoryId = categoryId;
	}

	public String getBrandId()
	{
		return brandId;
	}

	public void setBrandId(String brandId)
	{
		this.brandId = brandId;
	}

	public String getDeptId()
	{
		return deptId;
	}

	public void setDeptId(String deptId)
	{
		this.deptId = deptId;
	}

	public String getCreateUserId()
	{
		return createUserId;
	}

	public void setCreateUserId(String createUserId)
	{
		this.createUserId = createUserId;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public String getDutyUser()
	{
		return dutyUser;
	}

	public void setDutyUser(String dutyUser)
	{
		this.dutyUser = dutyUser;
	}

	public String getProductMean()
	{
		return productMean;
	}

	public void setProductMean(String productMean)
	{
		this.productMean = productMean;
	}

	public Integer getPriority()
	{
		return priority;
	}

	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Date getEffecTime()
	{
		return effecTime;
	}

	public void setEffecTime(Date effecTime)
	{
		this.effecTime = effecTime;
	}

	public Date getFailTime()
	{
		return failTime;
	}

	public void setFailTime(Date failTime)
	{
		this.failTime = failTime;
	}

	public String getMutexRule()
	{
		return mutexRule;
	}

	public void setMutexRule(String mutexRule)
	{
		this.mutexRule = mutexRule;
	}

	public CiMdaSysTableColumn getCiMdaSysTableColumn()
	{
		return ciMdaSysTableColumn;
	}

	public void setCiMdaSysTableColumn(CiMdaSysTableColumn ciMdaSysTableColumn)
	{
		this.ciMdaSysTableColumn = ciMdaSysTableColumn;
	}

	public String getEffectDate()
	{
		return effectDate;
	}

	public void setEffectDate(String effectDate)
	{
		this.effectDate = effectDate;
	}

	public String getBusiCaliber()
	{
		return busiCaliber;
	}

	public void setBusiCaliber(String busiCaliber)
	{
		this.busiCaliber = busiCaliber;
	}

	public String getBrandNames()
	{
		return brandNames;
	}

	public void setBrandNames(String brandNames)
	{
		this.brandNames = brandNames;
	}

	public String getEffectPeriod()
	{
		return effectPeriod;
	}

	public void setEffectPeriod(String effectPeriod)
	{
		this.effectPeriod = effectPeriod;
	}

	public Integer getIsProduct()
	{
		return isProduct;
	}

	public void setIsProduct(Integer isProduct)
	{
		this.isProduct = isProduct;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiProductInfo [productId=").append(productId).append(", productName=").append(productName).append(", parentId=").append(parentId).append(", descTxt=").append(descTxt).append(", categoryId=").append(categoryId).append(", brandId=").append(brandId).append(", deptId=").append(deptId).append(", createUserId=").append(createUserId).append(", createTime=").append(createTime).append(", dutyUser=").append(dutyUser).append(", productMean=").append(productMean).append(", priority=").append(priority).append(", status=").append(status).append(", effecTime=").append(effecTime).append(", failTime=").append(failTime).append(", mutexRule=").append(mutexRule).append("]").toString();
	}

	public String getOfferId()
	{
		return offerId;
	}

	public void setOfferId(String offerId)
	{
		this.offerId = offerId;
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	public String getRuleDesc()
	{
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc)
	{
		this.ruleDesc = ruleDesc;
	}
}

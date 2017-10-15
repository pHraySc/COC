// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiProductLableRuleId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiProductLableRuleId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String productNo;
	private String calcuElement;
	private String productName;
	private Long sortNum;
	private Integer customType;
	private Integer elementType;

	public CiProductLableRuleId()
	{
	}

	public CiProductLableRuleId(String productNo, String calcuElement, String productName, Long sortNum, Integer customType, Integer elementType)
	{
		this.productNo = productNo;
		this.calcuElement = calcuElement;
		this.productName = productName;
		this.sortNum = sortNum;
		this.customType = customType;
		this.elementType = elementType;
	}

	public String getProductNo()
	{
		return productNo;
	}

	public void setProductNo(String productNo)
	{
		this.productNo = productNo;
	}

	public String getCalcuElement()
	{
		return calcuElement;
	}

	public void setCalcuElement(String calcuElement)
	{
		this.calcuElement = calcuElement;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public Long getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Long sortNum)
	{
		this.sortNum = sortNum;
	}

	public Integer getCustomType()
	{
		return customType;
	}

	public void setCustomType(Integer customType)
	{
		this.customType = customType;
	}

	public Integer getElementType()
	{
		return elementType;
	}

	public void setElementType(Integer elementType)
	{
		this.elementType = elementType;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiProductLableRuleId))
		{
			return false;
		} else
		{
			CiProductLableRuleId castOther = (CiProductLableRuleId)other;
			return (getProductNo() == castOther.getProductNo() || getProductNo() != null && castOther.getProductNo() != null && getProductNo().equals(castOther.getProductNo())) && (getCalcuElement() == castOther.getCalcuElement() || getCalcuElement() != null && castOther.getCalcuElement() != null && getCalcuElement().equals(castOther.getCalcuElement())) && (getProductName() == castOther.getProductName() || getProductName() != null && castOther.getProductName() != null && getProductName().equals(castOther.getProductName())) && (getSortNum() == castOther.getSortNum() || getSortNum() != null && castOther.getSortNum() != null && getSortNum().equals(castOther.getSortNum())) && (getCustomType() == castOther.getCustomType() || getCustomType() != null && castOther.getCustomType() != null && getCustomType().equals(castOther.getCustomType())) && (getElementType() == castOther.getElementType() || getElementType() != null && castOther.getElementType() != null && getElementType().equals(castOther.getElementType()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getProductNo() != null ? getProductNo().hashCode() : 0);
		result = 37 * result + (getCalcuElement() != null ? getCalcuElement().hashCode() : 0);
		result = 37 * result + (getProductName() != null ? getProductName().hashCode() : 0);
		result = 37 * result + (getSortNum() != null ? getSortNum().hashCode() : 0);
		result = 37 * result + (getCustomType() != null ? getCustomType().hashCode() : 0);
		result = 37 * result + (getElementType() != null ? getElementType().hashCode() : 0);
		return result;
	}
}

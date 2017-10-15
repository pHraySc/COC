// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomSourceRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiCustomSourceRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String indexId;
	private String customGroupId;
	private String calcuElement;
	private Long sortNum;
	private Integer elementType;

	public CiCustomSourceRel()
	{
	}

	public CiCustomSourceRel(String customGroupId, String calcuElement, Long sortNum, Integer elementType)
	{
		this.customGroupId = customGroupId;
		this.calcuElement = calcuElement;
		this.sortNum = sortNum;
		this.elementType = elementType;
	}

	public String getIndexId()
	{
		return indexId;
	}

	public void setIndexId(String indexId)
	{
		this.indexId = indexId;
	}

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
	}

	public String getCalcuElement()
	{
		return calcuElement;
	}

	public void setCalcuElement(String calcuElement)
	{
		this.calcuElement = calcuElement;
	}

	public Long getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Long sortNum)
	{
		this.sortNum = sortNum;
	}

	public Integer getElementType()
	{
		return elementType;
	}

	public void setElementType(Integer elementType)
	{
		this.elementType = elementType;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiCustomSourceRel [indexId=").append(indexId).append(", customGroupId=").append(customGroupId).append(", calcuElement=").append(calcuElement).append(", sortNum=").append(sortNum).append(", elementType=").append(elementType).append("]").toString();
	}
}

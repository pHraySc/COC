// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimApproveResourceType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimApproveResourceType
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String resourceTypeId;
	private String resourceTypeName;
	private String resourceTypeDesc;
	private String resourceDetailLink;

	public DimApproveResourceType()
	{
	}

	public DimApproveResourceType(String resourceTypeName, String resourceTypeDesc, String resourceDetailLink)
	{
		this.resourceTypeName = resourceTypeName;
		this.resourceTypeDesc = resourceTypeDesc;
		this.resourceDetailLink = resourceDetailLink;
	}

	public String getResourceTypeId()
	{
		return resourceTypeId;
	}

	public void setResourceTypeId(String resourceTypeId)
	{
		this.resourceTypeId = resourceTypeId;
	}

	public String getResourceTypeName()
	{
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName)
	{
		this.resourceTypeName = resourceTypeName;
	}

	public String getResourceTypeDesc()
	{
		return resourceTypeDesc;
	}

	public void setResourceTypeDesc(String resourceTypeDesc)
	{
		this.resourceTypeDesc = resourceTypeDesc;
	}

	public String getResourceDetailLink()
	{
		return resourceDetailLink;
	}

	public void setResourceDetailLink(String resourceDetailLink)
	{
		this.resourceDetailLink = resourceDetailLink;
	}
}

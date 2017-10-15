// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomCampsegRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiCustomCampsegRelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String customGroupId;
	private String campsegId;

	public CiCustomCampsegRelId()
	{
	}

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
	}

	public String getCampsegId()
	{
		return campsegId;
	}

	public void setCampsegId(String campsegId)
	{
		this.campsegId = campsegId;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiCustomCampsegRelId [customGroupId=").append(customGroupId).append(", campsegId=").append(campsegId).append("]").toString();
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimNoticeSendMode.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimNoticeSendMode
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String sendModeId;
	private String sendModeName;
	private String descTxt;
	private Integer sortNum;

	public DimNoticeSendMode()
	{
	}

	public String getSendModeId()
	{
		return sendModeId;
	}

	public void setSendModeId(String sendModeId)
	{
		this.sendModeId = sendModeId;
	}

	public String getSendModeName()
	{
		return sendModeName;
	}

	public void setSendModeName(String sendModeName)
	{
		this.sendModeName = sendModeName;
	}

	public String getDescTxt()
	{
		return descTxt;
	}

	public void setDescTxt(String descTxt)
	{
		this.descTxt = descTxt;
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   VCiLabelCustomList.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class VCiLabelCustomList
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Date attentionTime;
	private String userId;
	private String labelCustomId;
	private Integer typeId;
	private String labelCustomName;

	public VCiLabelCustomList()
	{
	}

	public VCiLabelCustomList(Date attentionTime, String userId, String labelCustomId, Integer typeId)
	{
		this.attentionTime = attentionTime;
		this.userId = userId;
		this.labelCustomId = labelCustomId;
		this.typeId = typeId;
	}

	public Date getAttentionTime()
	{
		return attentionTime;
	}

	public void setAttentionTime(Date attentionTime)
	{
		this.attentionTime = attentionTime;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getLabelCustomId()
	{
		return labelCustomId;
	}

	public void setLabelCustomId(String labelCustomId)
	{
		this.labelCustomId = labelCustomId;
	}

	public Integer getTypeId()
	{
		return typeId;
	}

	public void setTypeId(Integer typeId)
	{
		this.typeId = typeId;
	}

	public String getLabelCustomName()
	{
		return labelCustomName;
	}

	public void setLabelCustomName(String labelCustomName)
	{
		this.labelCustomName = labelCustomName;
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserAttentionLabel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiUserAttentionLabelId

public class CiUserAttentionLabel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiUserAttentionLabelId id;
	private Date attentionTime;

	public CiUserAttentionLabel()
	{
	}

	public CiUserAttentionLabel(CiUserAttentionLabelId id)
	{
		this.id = id;
	}

	public CiUserAttentionLabel(CiUserAttentionLabelId id, Date attentionTime)
	{
		this.id = id;
		this.attentionTime = attentionTime;
	}

	public CiUserAttentionLabelId getId()
	{
		return id;
	}

	public void setId(CiUserAttentionLabelId id)
	{
		this.id = id;
	}

	public Date getAttentionTime()
	{
		return attentionTime;
	}

	public void setAttentionTime(Date attentionTime)
	{
		this.attentionTime = attentionTime;
	}
}

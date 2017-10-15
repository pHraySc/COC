// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserAttentionCustom.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiUserAttentionCustomId

public class CiUserAttentionCustom
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiUserAttentionCustomId id;
	private Date attentionTime;

	public CiUserAttentionCustom()
	{
	}

	public CiUserAttentionCustom(CiUserAttentionCustomId id)
	{
		this.id = id;
	}

	public CiUserAttentionCustom(CiUserAttentionCustomId id, Date attentionTime)
	{
		this.id = id;
		this.attentionTime = attentionTime;
	}

	public CiUserAttentionCustomId getId()
	{
		return id;
	}

	public void setId(CiUserAttentionCustomId id)
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

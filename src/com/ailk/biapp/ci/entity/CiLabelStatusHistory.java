// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelStatusHistory.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiLabelStatusHistoryId

public class CiLabelStatusHistory
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiLabelStatusHistoryId id;
	private Integer hasSendNotice;

	public CiLabelStatusHistory()
	{
	}

	public CiLabelStatusHistory(CiLabelStatusHistoryId id, Integer hasSendNotice)
	{
		this.id = id;
		this.hasSendNotice = hasSendNotice;
	}

	public Integer getHasSendNotice()
	{
		return hasSendNotice;
	}

	public void setHasSendNotice(Integer hasSendNotice)
	{
		this.hasSendNotice = hasSendNotice;
	}

	public CiLabelStatusHistoryId getId()
	{
		return id;
	}

	public void setId(CiLabelStatusHistoryId id)
	{
		this.id = id;
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserReadInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiUserReadInfoId

public class CiUserReadInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiUserReadInfoId id;
	private Date readTime;
	private Integer status;

	public CiUserReadInfo()
	{
	}

	public CiUserReadInfo(CiUserReadInfoId id, Date readTime, Integer status)
	{
		this.id = id;
		this.readTime = readTime;
		this.status = status;
	}

	public CiUserReadInfoId getId()
	{
		return id;
	}

	public void setId(CiUserReadInfoId id)
	{
		this.id = id;
	}

	public Date getReadTime()
	{
		return readTime;
	}

	public void setReadTime(Date readTime)
	{
		this.readTime = readTime;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}
}

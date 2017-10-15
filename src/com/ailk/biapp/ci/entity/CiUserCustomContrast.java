// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserCustomContrast.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.sql.Timestamp;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiUserCustomContrastId

public class CiUserCustomContrast
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiUserCustomContrastId id;
	private String createTime;
	private Timestamp delTime;
	private Integer status;

	public CiUserCustomContrast()
	{
	}

	public CiUserCustomContrast(CiUserCustomContrastId id)
	{
		this.id = id;
	}

	public CiUserCustomContrast(CiUserCustomContrastId id, String createTime, Timestamp delTime, Integer status)
	{
		this.id = id;
		this.createTime = createTime;
		this.delTime = delTime;
		this.status = status;
	}

	public CiUserCustomContrastId getId()
	{
		return id;
	}

	public void setId(CiUserCustomContrastId id)
	{
		this.id = id;
	}

	public Timestamp getDelTime()
	{
		return delTime;
	}

	public void setDelTime(Timestamp delTime)
	{
		this.delTime = delTime;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
}

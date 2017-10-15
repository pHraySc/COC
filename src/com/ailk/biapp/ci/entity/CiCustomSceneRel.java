// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomSceneRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiCustomSceneRelId

public class CiCustomSceneRel
	implements Serializable, Cloneable
{

	private static final long serialVersionUID = 1L;
	private CiCustomSceneRelId id;
	private int status;
	private Date modifyTime;

	public CiCustomSceneRel()
	{
	}

	public CiCustomSceneRel(CiCustomSceneRelId id)
	{
		this.id = id;
	}

	public CiCustomSceneRel(CiCustomSceneRelId id, int status, Date modifyTime)
	{
		this.id = id;
		this.status = status;
		this.modifyTime = modifyTime;
	}

	public CiCustomSceneRelId getId()
	{
		return id;
	}

	public void setId(CiCustomSceneRelId id)
	{
		this.id = id;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public Date getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}
}

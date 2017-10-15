// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiExternalSysLabelRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiExternalSysLabelRelId, CiLabelInfo

public class CiExternalSysLabelRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiExternalSysLabelRelId id;
	private CiLabelInfo ciLabelInfo;
	private String externalLabelName;

	public CiExternalSysLabelRel()
	{
	}

	public CiExternalSysLabelRel(CiExternalSysLabelRelId id)
	{
		this.id = id;
	}

	public CiExternalSysLabelRelId getId()
	{
		return id;
	}

	public void setId(CiExternalSysLabelRelId id)
	{
		this.id = id;
	}

	public CiLabelInfo getCiLabelInfo()
	{
		return ciLabelInfo;
	}

	public void setCiLabelInfo(CiLabelInfo ciLabelInfo)
	{
		this.ciLabelInfo = ciLabelInfo;
	}

	public String getExternalLabelName()
	{
		return externalLabelName;
	}

	public void setExternalLabelName(String externalLabelName)
	{
		this.externalLabelName = externalLabelName;
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserUseCustom.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiUserUseCustomId

public class CiUserUseCustom
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiUserUseCustomId id;
	private Integer useTypeId;
	private Integer isTemplate;

	public CiUserUseCustom()
	{
	}

	public CiUserUseCustom(CiUserUseCustomId id)
	{
		this.id = id;
	}

	public CiUserUseCustom(CiUserUseCustomId id, Integer useTypeId, Integer isTemplate)
	{
		this.id = id;
		this.useTypeId = useTypeId;
		this.isTemplate = isTemplate;
	}

	public CiUserUseCustomId getId()
	{
		return id;
	}

	public void setId(CiUserUseCustomId id)
	{
		this.id = id;
	}

	public Integer getUseTypeId()
	{
		return useTypeId;
	}

	public void setUseTypeId(Integer useTypeId)
	{
		this.useTypeId = useTypeId;
	}

	public Integer getIsTemplate()
	{
		return isTemplate;
	}

	public void setIsTemplate(Integer isTemplate)
	{
		this.isTemplate = isTemplate;
	}
}

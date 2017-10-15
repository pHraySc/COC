// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserUseLabel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiUserUseLabelId

public class CiUserUseLabel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiUserUseLabelId id;
	private Integer labelUseTypeId;

	public CiUserUseLabel()
	{
	}

	public CiUserUseLabel(CiUserUseLabelId id)
	{
		this.id = id;
	}

	public CiUserUseLabel(CiUserUseLabelId id, Integer labelUseTypeId)
	{
		this.id = id;
		this.labelUseTypeId = labelUseTypeId;
	}

	public CiUserUseLabelId getId()
	{
		return id;
	}

	public void setId(CiUserUseLabelId id)
	{
		this.id = id;
	}

	public Integer getLabelUseTypeId()
	{
		return labelUseTypeId;
	}

	public void setLabelUseTypeId(Integer labelUseTypeId)
	{
		this.labelUseTypeId = labelUseTypeId;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiUserUseLabel [id=").append(id).append(", labelUseTypeId=").append(labelUseTypeId).append("]").toString();
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiUserNoticeSet.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiUserNoticeSetId

public class CiUserNoticeSet
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiUserNoticeSetId id;
	private Integer isReceive;
	private String typeName;
	private String typeDesc;

	public CiUserNoticeSet()
	{
	}

	public CiUserNoticeSet(CiUserNoticeSetId id)
	{
		this.id = id;
	}

	public CiUserNoticeSet(CiUserNoticeSetId id, Integer isReceive)
	{
		this.id = id;
		this.isReceive = isReceive;
	}

	public CiUserNoticeSetId getId()
	{
		return id;
	}

	public void setId(CiUserNoticeSetId id)
	{
		this.id = id;
	}

	public Integer getIsReceive()
	{
		return isReceive;
	}

	public void setIsReceive(Integer isReceive)
	{
		this.isReceive = isReceive;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public String getTypeDesc()
	{
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc)
	{
		this.typeDesc = typeDesc;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiUserNoticeSet [id=").append(id).append(", isReceive=").append(isReceive).append(", typeDesc=").append(typeDesc).append(", typeName=").append(typeName).append("]").toString();
	}
}

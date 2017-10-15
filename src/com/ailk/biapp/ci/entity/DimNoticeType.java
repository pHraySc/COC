// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimNoticeType.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimNoticeType
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer noticeTypeId;
	private String noticeTypeName;
	private String noticeTypeDesc;
	private Integer hasSuccessFail;

	public DimNoticeType()
	{
	}

	public Integer getNoticeTypeId()
	{
		return noticeTypeId;
	}

	public DimNoticeType(Integer noticeTypeId, String noticeTypeName, String noticeTypeDesc, Integer hasSuccessFail)
	{
		this.noticeTypeId = noticeTypeId;
		this.noticeTypeName = noticeTypeName;
		this.noticeTypeDesc = noticeTypeDesc;
		this.hasSuccessFail = hasSuccessFail;
	}

	public void setNoticeTypeId(Integer noticeTypeId)
	{
		this.noticeTypeId = noticeTypeId;
	}

	public String getNoticeTypeName()
	{
		return noticeTypeName;
	}

	public void setNoticeTypeName(String noticeTypeName)
	{
		this.noticeTypeName = noticeTypeName;
	}

	public String getNoticeTypeDesc()
	{
		return noticeTypeDesc;
	}

	public void setNoticeTypeDesc(String noticeTypeDesc)
	{
		this.noticeTypeDesc = noticeTypeDesc;
	}

	public Integer getHasSuccessFail()
	{
		return hasSuccessFail;
	}

	public void setHasSuccessFail(Integer hasSuccessFail)
	{
		this.hasSuccessFail = hasSuccessFail;
	}

	public String toString()
	{
		return (new StringBuilder()).append("DimNoticeType [hasSuccessFail=").append(hasSuccessFail).append(", noticeTypeDesc=").append(noticeTypeDesc).append(", noticeTypeId=").append(noticeTypeId).append(", noticeTypeName=").append(noticeTypeName).append("]").toString();
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelViewRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiLabelViewRelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String labelId;
	private String cviewId;

	public CiLabelViewRelId()
	{
	}

	public CiLabelViewRelId(String labelId, String cviewId)
	{
		this.labelId = labelId;
		this.cviewId = cviewId;
	}

	public String getLabelId()
	{
		return labelId;
	}

	public void setLabelId(String labelId)
	{
		this.labelId = labelId;
	}

	public String getCviewId()
	{
		return cviewId;
	}

	public void setCviewId(String cviewId)
	{
		this.cviewId = cviewId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiLabelViewRelId))
		{
			return false;
		} else
		{
			CiLabelViewRelId castOther = (CiLabelViewRelId)other;
			return (getLabelId() == castOther.getLabelId() || getLabelId() != null && castOther.getLabelId() != null && getLabelId().equals(castOther.getLabelId())) && (getCviewId() == castOther.getCviewId() || getCviewId() != null && castOther.getCviewId() != null && getCviewId().equals(castOther.getCviewId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getLabelId() != null ? getLabelId().hashCode() : 0);
		result = 37 * result + (getCviewId() != null ? getCviewId().hashCode() : 0);
		return result;
	}
}

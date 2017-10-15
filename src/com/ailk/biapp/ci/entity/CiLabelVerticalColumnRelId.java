// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelVerticalColumnRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiLabelVerticalColumnRelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer labelId;
	private Integer columnId;

	public CiLabelVerticalColumnRelId()
	{
	}

	public CiLabelVerticalColumnRelId(Integer labelId, Integer columnId)
	{
		this.labelId = labelId;
		this.columnId = columnId;
	}

	public Integer getLabelId()
	{
		return labelId;
	}

	public void setLabelId(Integer labelId)
	{
		this.labelId = labelId;
	}

	public Integer getColumnId()
	{
		return columnId;
	}

	public void setColumnId(Integer columnId)
	{
		this.columnId = columnId;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiLabelVerticalColumnRelId))
		{
			return false;
		} else
		{
			CiLabelVerticalColumnRelId castOther = (CiLabelVerticalColumnRelId)other;
			return (getLabelId() == castOther.getLabelId() || getLabelId() != null && castOther.getLabelId() != null && getLabelId().equals(castOther.getLabelId())) && (getColumnId() == castOther.getColumnId() || getColumnId() != null && castOther.getColumnId() != null && getColumnId().equals(castOther.getColumnId()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getLabelId() != null ? getLabelId().hashCode() : 0);
		result = 37 * result + (getColumnId() != null ? getColumnId().hashCode() : 0);
		return result;
	}
}

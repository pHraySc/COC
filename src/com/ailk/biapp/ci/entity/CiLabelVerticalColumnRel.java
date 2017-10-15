// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelVerticalColumnRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiLabelVerticalColumnRelId, CiMdaSysTableColumn

public class CiLabelVerticalColumnRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiLabelVerticalColumnRelId id;
	private CiMdaSysTableColumn ciMdaSysTableColumn;
	private Integer labelTypeId;
	private Integer isMustColumn;
	private Integer sortNum;
	private String labelTypeName;
	private Integer childLabelId;
	private String unit;
	private String columnCnName;

	public CiLabelVerticalColumnRel()
	{
	}

	public CiLabelVerticalColumnRel(CiLabelVerticalColumnRelId id, CiMdaSysTableColumn ciMdaSysTableColumn, Integer labelTypeId)
	{
		this.id = id;
		this.ciMdaSysTableColumn = ciMdaSysTableColumn;
		this.labelTypeId = labelTypeId;
	}

	public CiLabelVerticalColumnRel(CiLabelVerticalColumnRelId id, CiMdaSysTableColumn ciMdaSysTableColumn, Integer labelTypeId, Integer isMustColumn, Integer sortNum)
	{
		this.id = id;
		this.ciMdaSysTableColumn = ciMdaSysTableColumn;
		this.labelTypeId = labelTypeId;
		this.isMustColumn = isMustColumn;
		this.sortNum = sortNum;
	}

	public CiLabelVerticalColumnRelId getId()
	{
		return id;
	}

	public void setId(CiLabelVerticalColumnRelId id)
	{
		this.id = id;
	}

	public CiMdaSysTableColumn getCiMdaSysTableColumn()
	{
		return ciMdaSysTableColumn;
	}

	public void setCiMdaSysTableColumn(CiMdaSysTableColumn ciMdaSysTableColumn)
	{
		this.ciMdaSysTableColumn = ciMdaSysTableColumn;
	}

	public Integer getLabelTypeId()
	{
		return labelTypeId;
	}

	public void setLabelTypeId(Integer labelTypeId)
	{
		this.labelTypeId = labelTypeId;
	}

	public Integer getIsMustColumn()
	{
		return isMustColumn;
	}

	public void setIsMustColumn(Integer isMustColumn)
	{
		this.isMustColumn = isMustColumn;
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}

	public String getLabelTypeName()
	{
		return labelTypeName;
	}

	public void setLabelTypeName(String labelTypeName)
	{
		this.labelTypeName = labelTypeName;
	}

	public Integer getChildLabelId()
	{
		return childLabelId;
	}

	public void setChildLabelId(Integer childLabelId)
	{
		this.childLabelId = childLabelId;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public String getColumnCnName()
	{
		return columnCnName;
	}

	public void setColumnCnName(String columnCnName)
	{
		this.columnCnName = columnCnName;
	}
}

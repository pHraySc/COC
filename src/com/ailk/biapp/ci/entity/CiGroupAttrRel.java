// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiGroupAttrRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiGroupAttrRelId

public class CiGroupAttrRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiGroupAttrRelId id;
	private String attrColName;
	private String attrColType;
	private int attrSource;
	private String labelOrCustomId;
	private String labelOrCustomColumn;
	private int isVerticalAttr;
	private Integer status;
	private String attrVal;
	private String tableName;
	private String sortType;
	private Integer sortNum;
	private String customName;
	private String customId;
	private String columnName;
	private String attrName;
	private String dataDateStr;

	public CiGroupAttrRel()
	{
	}

	public CiGroupAttrRel(CiGroupAttrRelId id)
	{
		this.id = id;
	}

	public CiGroupAttrRel(CiGroupAttrRelId id, String attrColName, String attrColType, int attrSource, String labelOrCustomId, String labelOrCustomColumn, String customName, 
			String customId, String columnName)
	{
		this.id = id;
		this.attrColName = attrColName;
		this.attrColType = attrColType;
		this.attrSource = attrSource;
		this.labelOrCustomId = labelOrCustomId;
		this.labelOrCustomColumn = labelOrCustomColumn;
		this.customName = customName;
		this.customId = customId;
		this.columnName = columnName;
	}

	public CiGroupAttrRelId getId()
	{
		return id;
	}

	public void setId(CiGroupAttrRelId id)
	{
		this.id = id;
	}

	public String getAttrColName()
	{
		return attrColName;
	}

	public void setAttrColName(String attrColName)
	{
		this.attrColName = attrColName;
	}

	public String getAttrColType()
	{
		return attrColType;
	}

	public void setAttrColType(String attrColType)
	{
		this.attrColType = attrColType;
	}

	public String getCustomName()
	{
		return customName;
	}

	public void setCustomName(String customName)
	{
		this.customName = customName;
	}

	public String getLabelOrCustomId()
	{
		return labelOrCustomId;
	}

	public void setLabelOrCustomId(String labelOrCustomId)
	{
		this.labelOrCustomId = labelOrCustomId;
	}

	public String getCustomId()
	{
		return customId;
	}

	public void setCustomId(String customId)
	{
		this.customId = customId;
	}

	public String getColumnName()
	{
		return columnName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	public String getLabelOrCustomColumn()
	{
		return labelOrCustomColumn;
	}

	public void setLabelOrCustomColumn(String labelOrCustomColumn)
	{
		this.labelOrCustomColumn = labelOrCustomColumn;
	}

	public int getAttrSource()
	{
		return attrSource;
	}

	public void setAttrSource(int attrSource)
	{
		this.attrSource = attrSource;
	}

	public int getIsVerticalAttr()
	{
		return isVerticalAttr;
	}

	public void setIsVerticalAttr(int isVerticalAttr)
	{
		this.isVerticalAttr = isVerticalAttr;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getAttrVal()
	{
		return attrVal;
	}

	public void setAttrVal(String attrVal)
	{
		this.attrVal = attrVal;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getAttrName()
	{
		return attrName;
	}

	public void setAttrName(String attrName)
	{
		this.attrName = attrName;
	}

	public String getSortType()
	{
		return sortType;
	}

	public void setSortType(String sortType)
	{
		this.sortType = sortType;
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}

	public String getDataDateStr()
	{
		return dataDateStr;
	}

	public void setDataDateStr(String dataDateStr)
	{
		this.dataDateStr = dataDateStr;
	}
}

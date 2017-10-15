package com.ailk.biapp.ci.entity.base;

import java.io.Serializable;

public class LabelVerticalColumn
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer labelTypeId;
	private Integer isMustColumn;
	private Integer sortNum;
	private String columnCnName;

	public LabelVerticalColumn()
	{
	}

	public Integer getLabelTypeId()
	{
		return labelTypeId;
	}

	public Integer getIsMustColumn()
	{
		return isMustColumn;
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public String getColumnCnName()
	{
		return columnCnName;
	}

	public void setLabelTypeId(Integer labelTypeId)
	{
		this.labelTypeId = labelTypeId;
	}

	public void setIsMustColumn(Integer isMustColumn)
	{
		this.isMustColumn = isMustColumn;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}

	public void setColumnCnName(String columnCnName)
	{
		this.columnCnName = columnCnName;
	}
}

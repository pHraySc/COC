package com.ailk.biapp.ci.model;


public class DimColumnDataType
{

	private Integer columnDataTypeId;
	private Integer labelTypeId;
	private String columnDataTypeName;
	private String columnDataLength;
	private Integer columnDataPrecision;
	private String descTxt;

	public DimColumnDataType()
	{
	}

	public Integer getColumnDataTypeId()
	{
		return columnDataTypeId;
	}

	public void setColumnDataTypeId(Integer columnDataTypeId)
	{
		this.columnDataTypeId = columnDataTypeId;
	}

	public Integer getLabelTypeId()
	{
		return labelTypeId;
	}

	public void setLabelTypeId(Integer labelTypeId)
	{
		this.labelTypeId = labelTypeId;
	}

	public String getColumnDataTypeName()
	{
		return columnDataTypeName;
	}

	public void setColumnDataTypeName(String columnDataTypeName)
	{
		this.columnDataTypeName = columnDataTypeName;
	}

	public String getColumnDataLength()
	{
		return columnDataLength;
	}

	public void setColumnDataLength(String columnDataLength)
	{
		this.columnDataLength = columnDataLength;
	}

	public Integer getColumnDataPrecision()
	{
		return columnDataPrecision;
	}

	public void setColumnDataPrecision(Integer columnDataPrecision)
	{
		this.columnDataPrecision = columnDataPrecision;
	}

	public String getDescTxt()
	{
		return descTxt;
	}

	public void setDescTxt(String descTxt)
	{
		this.descTxt = descTxt;
	}
}

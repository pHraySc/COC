package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class ImportColumn
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String ColumnName;
	private String ColumnCnName;
	private String ColumnType;

	public ImportColumn()
	{
	}

	public String getColumnName()
	{
		return ColumnName;
	}

	public void setColumnName(String columnName)
	{
		ColumnName = columnName;
	}

	public String getColumnCnName()
	{
		return ColumnCnName;
	}

	public void setColumnCnName(String columnCnName)
	{
		ColumnCnName = columnCnName;
	}

	public String getColumnType()
	{
		return ColumnType;
	}

	public void setColumnType(String columnType)
	{
		ColumnType = columnType;
	}

	public String toString()
	{
		return (new StringBuilder()).append("ImportColumn [ColumnName=").append(ColumnName).append(", ColumnCnName=").append(ColumnCnName).append(", ColumnType=").append(ColumnType).append("]").toString();
	}
}

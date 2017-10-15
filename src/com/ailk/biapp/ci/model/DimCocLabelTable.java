package com.ailk.biapp.ci.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class DimCocLabelTable
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String targetTableCode;
	private Short targetTableType;
	private String labelTableName;
	private Short tableDataCycle;
	private Date effectiveTime;
	private Date invalidTime;
	private Short statusId;
	private Timestamp statusTime;
	private String notes;

	public DimCocLabelTable()
	{
	}

	public String getTargetTableCode()
	{
		return targetTableCode;
	}

	public void setTargetTableCode(String targetTableCode)
	{
		this.targetTableCode = targetTableCode;
	}

	public Short getTargetTableType()
	{
		return targetTableType;
	}

	public void setTargetTableType(Short targetTableType)
	{
		this.targetTableType = targetTableType;
	}

	public String getLabelTableName()
	{
		return labelTableName;
	}

	public void setLabelTableName(String labelTableName)
	{
		this.labelTableName = labelTableName;
	}

	public Short getTableDataCycle()
	{
		return tableDataCycle;
	}

	public void setTableDataCycle(Short tableDataCycle)
	{
		this.tableDataCycle = tableDataCycle;
	}

	public Date getEffectiveTime()
	{
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime)
	{
		this.effectiveTime = effectiveTime;
	}

	public Date getInvalidTime()
	{
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime)
	{
		this.invalidTime = invalidTime;
	}

	public Short getStatusId()
	{
		return statusId;
	}

	public void setStatusId(Short statusId)
	{
		this.statusId = statusId;
	}

	public Timestamp getStatusTime()
	{
		return statusTime;
	}

	public void setStatusTime(Timestamp statusTime)
	{
		this.statusTime = statusTime;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}
}

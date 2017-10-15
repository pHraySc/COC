package com.ailk.biapp.ci.model;

import java.io.Serializable;
import java.util.Date;

public class DimCocLabelModelTable
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String targetTableCode;
	private String labelCode;
	private String dataType;
	private String labelDesc;
	private String countRulesCode;
	private Date effectiveTime;
	private Date invalidTime;

	public DimCocLabelModelTable()
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

	public String getLabelCode()
	{
		return labelCode;
	}

	public void setLabelCode(String labelCode)
	{
		this.labelCode = labelCode;
	}

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public String getLabelDesc()
	{
		return labelDesc;
	}

	public void setLabelDesc(String labelDesc)
	{
		this.labelDesc = labelDesc;
	}

	public String getCountRulesCode()
	{
		return countRulesCode;
	}

	public void setCountRulesCode(String countRulesCode)
	{
		this.countRulesCode = countRulesCode;
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
}

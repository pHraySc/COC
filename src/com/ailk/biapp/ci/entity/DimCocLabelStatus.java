// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCocLabelStatus.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class DimCocLabelStatus
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer labelId;
	private String dataDate;
	private Integer dataStatus;
	private Date dataInsertTime;
	private Integer dataBatch;

	public DimCocLabelStatus()
	{
	}

	public DimCocLabelStatus(Integer labelId, String dataDate, Integer dataStatus, Date dataInsertTime, Integer dataBatch)
	{
		this.labelId = labelId;
		this.dataDate = dataDate;
		this.dataStatus = dataStatus;
		this.dataInsertTime = dataInsertTime;
		this.dataBatch = dataBatch;
	}

	public Integer getLabelId()
	{
		return labelId;
	}

	public void setLabelId(Integer labelId)
	{
		this.labelId = labelId;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public Integer getDataStatus()
	{
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus)
	{
		this.dataStatus = dataStatus;
	}

	public Date getDataInsertTime()
	{
		return dataInsertTime;
	}

	public void setDataInsertTime(Date dataInsertTime)
	{
		this.dataInsertTime = dataInsertTime;
	}

	public Integer getDataBatch()
	{
		return dataBatch;
	}

	public void setDataBatch(Integer dataBatch)
	{
		this.dataBatch = dataBatch;
	}

	public String toString()
	{
		return (new StringBuilder()).append("DimCocLabelStatus [labelId=").append(labelId).append(", dataDate=").append(dataDate).append(", dataStatus=").append(dataStatus).append(", dataInsertTime=").append(dataInsertTime).append(", dataBatch=").append(dataBatch).append("]").toString();
	}
}

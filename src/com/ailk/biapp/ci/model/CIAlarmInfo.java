package com.ailk.biapp.ci.model;

import java.util.List;

public class CIAlarmInfo
{

	private List thresholdIdList;
	private String busiName;
	private String columnId;
	private String startTime;
	private String endTime;

	public CIAlarmInfo()
	{
	}

	public List getThresholdIdList()
	{
		return thresholdIdList;
	}

	public void setThresholdIdList(List thresholdIdList)
	{
		this.thresholdIdList = thresholdIdList;
	}

	public String getBusiName()
	{
		return busiName;
	}

	public void setBusiName(String busiName)
	{
		this.busiName = busiName;
	}

	public String getColumnId()
	{
		return columnId;
	}

	public void setColumnId(String columnId)
	{
		this.columnId = columnId;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
}

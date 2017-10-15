package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class LabelTrendInfo
	implements Serializable
{

	private static final long serialVersionUID = 0x718d61da1e81d112L;
	private String dataDate;
	private String value;
	private String alarm;
	private String listTableName;
	private Integer fileCreateStatus;
	private boolean isWeek;

	public boolean getIsWeek()
	{
		return isWeek;
	}

	public void setWeek(boolean isWeek)
	{
		this.isWeek = isWeek;
	}

	public String getListTableName()
	{
		return listTableName;
	}

	public void setListTableName(String listTableName)
	{
		this.listTableName = listTableName;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public LabelTrendInfo(String dataDate, String value, String alarm)
	{
		this.dataDate = dataDate;
		this.value = value;
		this.alarm = alarm;
	}

	public LabelTrendInfo()
	{
	}

	public String getAlarm()
	{
		return alarm;
	}

	public void setAlarm(String alarm)
	{
		this.alarm = alarm;
	}

	public String toString()
	{
		return (new StringBuilder()).append("LabelTrendInfo [dataDate=").append(dataDate).append(", value=").append(value).append(", alarm=").append(alarm).append(", listTableName=").append(listTableName).append("]").toString();
	}

	public Integer getFileCreateStatus()
	{
		return fileCreateStatus;
	}

	public void setFileCreateStatus(Integer fileCreateStatus)
	{
		this.fileCreateStatus = fileCreateStatus;
	}
}

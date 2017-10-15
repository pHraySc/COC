package com.ailk.biapp.ci.model;

import java.util.List;

public class CityMap
{

	private String cityid;
	private String fileName;
	private String width;
	private String height;
	private String centerTop;
	private String centerLeft;
	private List maphilights;
	private Boolean alwaysOn;
	private String info;
	private String tag;
	private String gridInfo;

	public CityMap()
	{
	}

	public String getTag()
	{
		return tag;
	}

	public void setTag(String tag)
	{
		this.tag = tag;
	}

	public String getCityid()
	{
		return cityid;
	}

	public void setCityid(String cityid)
	{
		this.cityid = cityid;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getWidth()
	{
		return width;
	}

	public void setWidth(String width)
	{
		this.width = width;
	}

	public String getHeight()
	{
		return height;
	}

	public void setHeight(String height)
	{
		this.height = height;
	}

	public List getMaphilights()
	{
		return maphilights;
	}

	public void setMaphilights(List maphilights)
	{
		this.maphilights = maphilights;
	}

	public Boolean getAlwaysOn()
	{
		return alwaysOn;
	}

	public void setAlwaysOn(Boolean alwaysOn)
	{
		this.alwaysOn = alwaysOn;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}

	public String getCenterTop()
	{
		return centerTop;
	}

	public void setCenterTop(String centerTop)
	{
		this.centerTop = centerTop;
	}

	public String getCenterLeft()
	{
		return centerLeft;
	}

	public void setCenterLeft(String centerLeft)
	{
		this.centerLeft = centerLeft;
	}

	public String getGridInfo()
	{
		return gridInfo;
	}

	public void setGridInfo(String gridInfo)
	{
		this.gridInfo = gridInfo;
	}
}

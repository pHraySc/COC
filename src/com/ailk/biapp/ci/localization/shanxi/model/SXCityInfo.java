package com.ailk.biapp.ci.localization.shanxi.model;

import com.asiainfo.biframe.privilege.ICity;

public class SXCityInfo
	implements ICity
{

	private String cityId;
	private String parentId;
	private String dmCityId;
	private String dmDeptId;
	private String dmCountyId;
	private String cityName;
	private int sortNum;
	private String dmTypeId;
	private String dmTypeCode;

	public SXCityInfo()
	{
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public String getDmCityId()
	{
		return dmCityId;
	}

	public void setDmCityId(String dmCityId)
	{
		this.dmCityId = dmCityId;
	}

	public String getDmDeptId()
	{
		return dmDeptId;
	}

	public void setDmDeptId(String dmDeptId)
	{
		this.dmDeptId = dmDeptId;
	}

	public String getDmCountyId()
	{
		return dmCountyId;
	}

	public void setDmCountyId(String dmCountyId)
	{
		this.dmCountyId = dmCountyId;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public int getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(int sortNum)
	{
		this.sortNum = sortNum;
	}

	public String getDmTypeId()
	{
		return dmTypeId;
	}

	public void setDmTypeId(String dmTypeId)
	{
		this.dmTypeId = dmTypeId;
	}

	public String getDmTypeCode()
	{
		return dmTypeCode;
	}

	public void setDmTypeCode(String dmTypeCode)
	{
		this.dmTypeCode = dmTypeCode;
	}

	public SXCityInfo buildCenterCity()
	{
		cityId = "290";
		cityName = "Ê¡¹«Ë¾";
		dmCityId = "290";
		dmCountyId = "-1";
		dmDeptId = "-1";
		parentId = "290";
		return this;
	}

	public String toString()
	{
		return String.format("City id:%s, City name:%s, dmCityId:%s, dmCountyId:%s, Parent id:%s", new Object[] {
			cityId, cityName, dmCityId, dmCountyId, parentId
		});
	}
}

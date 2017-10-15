// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomGroupForm.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiCustomGroupFormId

public class CiCustomGroupForm
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiCustomGroupFormId id;
	private String dataDate;
	private String customGroupId;
	private Integer customNum;
	private Integer ringNum;
	private String listTableName;
	private Integer cityId;
	private Integer vipLevelId;
	private String vipLevelName;
	private Integer brandId;
	private String brand_name;
	private String cityName;
	private String color;
	private Integer updateCycle;
	private boolean aBrandFlag;
	private int useTimes;

	public String getListTableName()
	{
		return listTableName;
	}

	public void setListTableName(String listTableName)
	{
		this.listTableName = listTableName;
	}

	public Integer getCityId()
	{
		return cityId;
	}

	public void setCityId(Integer cityId)
	{
		this.cityId = cityId;
	}

	public Integer getVipLevelId()
	{
		return vipLevelId;
	}

	public void setVipLevelId(Integer vipLevelId)
	{
		this.vipLevelId = vipLevelId;
	}

	public Integer getBrandId()
	{
		return brandId;
	}

	public void setBrandId(Integer brandId)
	{
		this.brandId = brandId;
	}

	public CiCustomGroupForm()
	{
	}

	public CiCustomGroupForm(CiCustomGroupFormId id)
	{
		this.id = id;
	}

	public CiCustomGroupForm(CiCustomGroupFormId id, String dataDate, String customGroupId, Integer customNum, Integer ringNum)
	{
		this.id = id;
		this.dataDate = dataDate;
		this.customGroupId = customGroupId;
		this.customNum = customNum;
		this.ringNum = ringNum;
	}

	public CiCustomGroupFormId getId()
	{
		return id;
	}

	public void setId(CiCustomGroupFormId id)
	{
		this.id = id;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
	}

	public Integer getCustomNum()
	{
		return customNum;
	}

	public void setCustomNum(Integer customNum)
	{
		this.customNum = customNum;
	}

	public Integer getRingNum()
	{
		return ringNum;
	}

	public void setRingNum(Integer ringNum)
	{
		this.ringNum = ringNum;
	}

	public Integer getUpdateCycle()
	{
		return updateCycle;
	}

	public void setUpdateCycle(Integer updateCycle)
	{
		this.updateCycle = updateCycle;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

	public String getBrand_name()
	{
		return brand_name;
	}

	public void setBrand_name(String brandName)
	{
		brand_name = brandName;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public String getVipLevelName()
	{
		return vipLevelName;
	}

	public void setVipLevelName(String vipLevelName)
	{
		this.vipLevelName = vipLevelName;
	}

	public boolean isaBrandFlag()
	{
		return aBrandFlag;
	}

	public void setaBrandFlag(boolean aBrandFlag)
	{
		this.aBrandFlag = aBrandFlag;
	}

	public int getUseTimes()
	{
		return useTimes;
	}

	public void setUseTimes(int useTimes)
	{
		this.useTimes = useTimes;
	}
}

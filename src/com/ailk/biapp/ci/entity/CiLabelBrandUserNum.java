// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelBrandUserNum.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiLabelBrandUserNumId

public class CiLabelBrandUserNum
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiLabelBrandUserNumId id;
	private Long customNum;
	private Long ringNum;
	private Double proportion;
	private String dataDate;
	private Integer labelId;
	private Integer cityId;
	private Integer vipLevelId;
	private Integer brandId;

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public Integer getLabelId()
	{
		return labelId;
	}

	public void setLabelId(Integer labelId)
	{
		this.labelId = labelId;
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

	public CiLabelBrandUserNum()
	{
	}

	public CiLabelBrandUserNum(CiLabelBrandUserNumId id)
	{
		this.id = id;
	}

	public CiLabelBrandUserNum(CiLabelBrandUserNumId id, Long customNum, Long ringNum, Double proportion)
	{
		this.id = id;
		this.customNum = customNum;
		this.ringNum = ringNum;
		this.proportion = proportion;
	}

	public CiLabelBrandUserNumId getId()
	{
		return id;
	}

	public void setId(CiLabelBrandUserNumId id)
	{
		this.id = id;
	}

	public Long getCustomNum()
	{
		return customNum;
	}

	public void setCustomNum(Long customNum)
	{
		this.customNum = customNum;
	}

	public Long getRingNum()
	{
		return ringNum;
	}

	public void setRingNum(Long ringNum)
	{
		this.ringNum = ringNum;
	}

	public Double getProportion()
	{
		return proportion;
	}

	public void setProportion(Double proportion)
	{
		this.proportion = proportion;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiLabelBrandUserNum [customNum=").append(customNum).append(", id=").append(id).append(", proportion=").append(proportion).append(", ringNum=").append(ringNum).append("]").toString();
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimVerifyStars.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimVerifyStars
	implements Serializable
{

	private static final long serialVersionUID = 0xf6c54433af1e676bL;
	private Short starsId;
	private String recallDesc;
	private String upgradeDesc;
	private String marketingMode;
	private Short sortNum;

	public DimVerifyStars()
	{
	}

	public DimVerifyStars(String recallDesc, String upgradeDesc, String marketingMode, Short sortNum)
	{
		this.recallDesc = recallDesc;
		this.upgradeDesc = upgradeDesc;
		this.marketingMode = marketingMode;
		this.sortNum = sortNum;
	}

	public Short getStarsId()
	{
		return starsId;
	}

	public void setStarsId(Short starsId)
	{
		this.starsId = starsId;
	}

	public String getRecallDesc()
	{
		return recallDesc;
	}

	public void setRecallDesc(String recallDesc)
	{
		this.recallDesc = recallDesc;
	}

	public String getUpgradeDesc()
	{
		return upgradeDesc;
	}

	public void setUpgradeDesc(String upgradeDesc)
	{
		this.upgradeDesc = upgradeDesc;
	}

	public String getMarketingMode()
	{
		return marketingMode;
	}

	public void setMarketingMode(String marketingMode)
	{
		this.marketingMode = marketingMode;
	}

	public Short getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Short sortNum)
	{
		this.sortNum = sortNum;
	}
}

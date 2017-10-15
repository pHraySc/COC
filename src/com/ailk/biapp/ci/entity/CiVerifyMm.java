// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiVerifyMm.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiVerifyMmId

public class CiVerifyMm
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiVerifyMmId id;
	private Double recall;
	private Integer potentialUserNum;
	private Double upgradeMultiple;
	private Short starsId;
	private Integer newOrderUserNum;
	private Integer lastNewIntersectionNum;
	private Integer lastPotentialUserNum;
	private Integer lastNotOrderUserNum;
	private String offerId;
	private String dataDate;
	private String countRulesCode;
	private String starsHtml;
	private Integer recallInt;
	private String ruleDesc;
	private String recallStr;
	private String upgradeMultipleStr;
	private int startRise;

	public CiVerifyMm()
	{
		startRise = 0;
	}

	public CiVerifyMm(CiVerifyMmId id)
	{
		startRise = 0;
		this.id = id;
	}

	public CiVerifyMm(CiVerifyMmId id, Double recall, Integer potentialUserNum, Double upgradeMultiple, Short starsId, Integer newOrderUserNum, Integer lastNewIntersectionNum, 
			Integer lastPotentialUserNum, Integer lastNotOrderUserNum)
	{
		startRise = 0;
		this.id = id;
		this.recall = recall;
		this.potentialUserNum = potentialUserNum;
		this.upgradeMultiple = upgradeMultiple;
		this.starsId = starsId;
		this.newOrderUserNum = newOrderUserNum;
		this.lastNewIntersectionNum = lastNewIntersectionNum;
		this.lastPotentialUserNum = lastPotentialUserNum;
		this.lastNotOrderUserNum = lastNotOrderUserNum;
	}

	public CiVerifyMmId getId()
	{
		return id;
	}

	public void setId(CiVerifyMmId id)
	{
		this.id = id;
	}

	public Double getRecall()
	{
		return recall;
	}

	public void setRecall(Double recall)
	{
		this.recall = recall;
	}

	public Integer getPotentialUserNum()
	{
		return potentialUserNum;
	}

	public void setPotentialUserNum(Integer potentialUserNum)
	{
		this.potentialUserNum = potentialUserNum;
	}

	public Double getUpgradeMultiple()
	{
		return upgradeMultiple;
	}

	public void setUpgradeMultiple(Double upgradeMultiple)
	{
		this.upgradeMultiple = upgradeMultiple;
	}

	public Short getStarsId()
	{
		return starsId;
	}

	public void setStarsId(Short starsId)
	{
		this.starsId = starsId;
	}

	public Integer getNewOrderUserNum()
	{
		return newOrderUserNum;
	}

	public void setNewOrderUserNum(Integer newOrderUserNum)
	{
		this.newOrderUserNum = newOrderUserNum;
	}

	public Integer getLastNewIntersectionNum()
	{
		return lastNewIntersectionNum;
	}

	public void setLastNewIntersectionNum(Integer lastNewIntersectionNum)
	{
		this.lastNewIntersectionNum = lastNewIntersectionNum;
	}

	public Integer getLastPotentialUserNum()
	{
		return lastPotentialUserNum;
	}

	public void setLastPotentialUserNum(Integer lastPotentialUserNum)
	{
		this.lastPotentialUserNum = lastPotentialUserNum;
	}

	public Integer getLastNotOrderUserNum()
	{
		return lastNotOrderUserNum;
	}

	public void setLastNotOrderUserNum(Integer lastNotOrderUserNum)
	{
		this.lastNotOrderUserNum = lastNotOrderUserNum;
	}

	public String getOfferId()
	{
		return offerId;
	}

	public void setOfferId(String offerId)
	{
		this.offerId = offerId;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public String getCountRulesCode()
	{
		return countRulesCode;
	}

	public void setCountRulesCode(String countRulesCode)
	{
		this.countRulesCode = countRulesCode;
	}

	public String getStarsHtml()
	{
		return starsHtml;
	}

	public void setStarsHtml(String starsHtml)
	{
		this.starsHtml = starsHtml;
	}

	public Integer getRecallInt()
	{
		return recallInt;
	}

	public void setRecallInt(Integer recallInt)
	{
		this.recallInt = recallInt;
	}

	public String getRuleDesc()
	{
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc)
	{
		this.ruleDesc = ruleDesc;
	}

	public int getStartRise()
	{
		return startRise;
	}

	public void setStartRise(int startRise)
	{
		this.startRise = startRise;
	}

	public String getRecallStr()
	{
		return recallStr;
	}

	public void setRecallStr(String recallStr)
	{
		this.recallStr = recallStr;
	}

	public String getUpgradeMultipleStr()
	{
		return upgradeMultipleStr;
	}

	public void setUpgradeMultipleStr(String upgradeMultipleStr)
	{
		this.upgradeMultipleStr = upgradeMultipleStr;
	}
}

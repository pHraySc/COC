// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiVerifyRuleRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.List;

public class CiVerifyRuleRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String countRulesCode;
	private String offerId;
	private Short effectFlag;
	private String ruleDesc;
	private String datumMonth;
	private Integer datumNewUserNum;
	private Double datumRecall;
	private Double datumUpgradeMultiple;
	private String indexSelectConclusion;
	private String labelSelectConclusion;
	private String labelRuleConclusion;
	private String finalRuleConclusion;
	private List verifyMmList;
	private Integer datumRecallInt;

	public CiVerifyRuleRel()
	{
	}

	public CiVerifyRuleRel(String offerId, Short effectFlag, String ruleDesc, String datumMonth, Integer datumNewUserNum, Double datumRecall, Double datumUpgradeMultiple, 
			String indexSelectConclusion, String labelSelectConclusion, String labelRuleConclusion, String finalRuleConclusion)
	{
		this.offerId = offerId;
		this.effectFlag = effectFlag;
		this.ruleDesc = ruleDesc;
		this.datumMonth = datumMonth;
		this.datumNewUserNum = datumNewUserNum;
		this.datumRecall = datumRecall;
		this.datumUpgradeMultiple = datumUpgradeMultiple;
		this.indexSelectConclusion = indexSelectConclusion;
		this.labelSelectConclusion = labelSelectConclusion;
		this.labelRuleConclusion = labelRuleConclusion;
		this.finalRuleConclusion = finalRuleConclusion;
	}

	public String getCountRulesCode()
	{
		return countRulesCode;
	}

	public void setCountRulesCode(String countRulesCode)
	{
		this.countRulesCode = countRulesCode;
	}

	public String getOfferId()
	{
		return offerId;
	}

	public void setOfferId(String offerId)
	{
		this.offerId = offerId;
	}

	public Short getEffectFlag()
	{
		return effectFlag;
	}

	public void setEffectFlag(Short effectFlag)
	{
		this.effectFlag = effectFlag;
	}

	public String getRuleDesc()
	{
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc)
	{
		this.ruleDesc = ruleDesc;
	}

	public String getDatumMonth()
	{
		return datumMonth;
	}

	public void setDatumMonth(String datumMonth)
	{
		this.datumMonth = datumMonth;
	}

	public Integer getDatumNewUserNum()
	{
		return datumNewUserNum;
	}

	public void setDatumNewUserNum(Integer datumNewUserNum)
	{
		this.datumNewUserNum = datumNewUserNum;
	}

	public Double getDatumRecall()
	{
		return datumRecall;
	}

	public void setDatumRecall(Double datumRecall)
	{
		this.datumRecall = datumRecall;
	}

	public Double getDatumUpgradeMultiple()
	{
		return datumUpgradeMultiple;
	}

	public void setDatumUpgradeMultiple(Double datumUpgradeMultiple)
	{
		this.datumUpgradeMultiple = datumUpgradeMultiple;
	}

	public String getIndexSelectConclusion()
	{
		return indexSelectConclusion;
	}

	public void setIndexSelectConclusion(String indexSelectConclusion)
	{
		this.indexSelectConclusion = indexSelectConclusion;
	}

	public String getLabelSelectConclusion()
	{
		return labelSelectConclusion;
	}

	public void setLabelSelectConclusion(String labelSelectConclusion)
	{
		this.labelSelectConclusion = labelSelectConclusion;
	}

	public String getLabelRuleConclusion()
	{
		return labelRuleConclusion;
	}

	public void setLabelRuleConclusion(String labelRuleConclusion)
	{
		this.labelRuleConclusion = labelRuleConclusion;
	}

	public String getFinalRuleConclusion()
	{
		return finalRuleConclusion;
	}

	public void setFinalRuleConclusion(String finalRuleConclusion)
	{
		this.finalRuleConclusion = finalRuleConclusion;
	}

	public List getVerifyMmList()
	{
		return verifyMmList;
	}

	public void setVerifyMmList(List verifyMmList)
	{
		this.verifyMmList = verifyMmList;
	}

	public Integer getDatumRecallInt()
	{
		return datumRecallInt;
	}

	public void setDatumRecallInt(Integer datumRecallInt)
	{
		this.datumRecallInt = datumRecallInt;
	}
}

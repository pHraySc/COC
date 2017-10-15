// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiVerifyRuleInfo.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiVerifyRuleInfo
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer mainKeyId;
	private String countRulesCode;
	private String labelIndexFlag;
	private String ruleCaliber;
	private String ruleCondition;
	private Double datumRecall;
	private Double datumUpgradeMultiple;
	private Short optimalFlag;
	private String productName;
	private String datumMonth;
	private String labelRuleConclusion;
	private Integer datumRecallInt;

	public CiVerifyRuleInfo()
	{
	}

	public CiVerifyRuleInfo(String countRulesCode, String labelIndexFlag, String ruleCaliber, String ruleCondition, Double datumRecall, Double datumUpgradeMultiple, Short optimalFlag)
	{
		this.countRulesCode = countRulesCode;
		this.labelIndexFlag = labelIndexFlag;
		this.ruleCaliber = ruleCaliber;
		this.ruleCondition = ruleCondition;
		this.datumRecall = datumRecall;
		this.datumUpgradeMultiple = datumUpgradeMultiple;
		this.optimalFlag = optimalFlag;
	}

	public Integer getMainKeyId()
	{
		return mainKeyId;
	}

	public void setMainKeyId(Integer mainKeyId)
	{
		this.mainKeyId = mainKeyId;
	}

	public String getCountRulesCode()
	{
		return countRulesCode;
	}

	public void setCountRulesCode(String countRulesCode)
	{
		this.countRulesCode = countRulesCode;
	}

	public String getLabelIndexFlag()
	{
		return labelIndexFlag;
	}

	public void setLabelIndexFlag(String labelIndexFlag)
	{
		this.labelIndexFlag = labelIndexFlag;
	}

	public String getRuleCaliber()
	{
		return ruleCaliber;
	}

	public void setRuleCaliber(String ruleCaliber)
	{
		this.ruleCaliber = ruleCaliber;
	}

	public String getRuleCondition()
	{
		return ruleCondition;
	}

	public void setRuleCondition(String ruleCondition)
	{
		this.ruleCondition = ruleCondition;
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

	public Short getOptimalFlag()
	{
		return optimalFlag;
	}

	public void setOptimalFlag(Short optimalFlag)
	{
		this.optimalFlag = optimalFlag;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getDatumMonth()
	{
		return datumMonth;
	}

	public void setDatumMonth(String datumMonth)
	{
		this.datumMonth = datumMonth;
	}

	public String getLabelRuleConclusion()
	{
		return labelRuleConclusion;
	}

	public void setLabelRuleConclusion(String labelRuleConclusion)
	{
		this.labelRuleConclusion = labelRuleConclusion;
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

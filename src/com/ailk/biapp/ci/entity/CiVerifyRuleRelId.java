// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiVerifyRuleRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiVerifyRuleRelId
	implements Serializable
{

	private static final long serialVersionUID = 0x65b68ab09cd23963L;
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

	public CiVerifyRuleRelId()
	{
	}

	public CiVerifyRuleRelId(String countRulesCode)
	{
		this.countRulesCode = countRulesCode;
	}

	public CiVerifyRuleRelId(String countRulesCode, String offerId, Short effectFlag, String ruleDesc, String datumMonth, Integer datumNewUserNum, Double datumRecall, 
			Double datumUpgradeMultiple, String indexSelectConclusion, String labelSelectConclusion, String labelRuleConclusion, String finalRuleConclusion)
	{
		this.countRulesCode = countRulesCode;
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

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiVerifyRuleRelId))
		{
			return false;
		} else
		{
			CiVerifyRuleRelId castOther = (CiVerifyRuleRelId)other;
			return (getCountRulesCode() == castOther.getCountRulesCode() || getCountRulesCode() != null && castOther.getCountRulesCode() != null && getCountRulesCode().equals(castOther.getCountRulesCode())) && (getOfferId() == castOther.getOfferId() || getOfferId() != null && castOther.getOfferId() != null && getOfferId().equals(castOther.getOfferId())) && (getEffectFlag() == castOther.getEffectFlag() || getEffectFlag() != null && castOther.getEffectFlag() != null && getEffectFlag().equals(castOther.getEffectFlag())) && (getRuleDesc() == castOther.getRuleDesc() || getRuleDesc() != null && castOther.getRuleDesc() != null && getRuleDesc().equals(castOther.getRuleDesc())) && (getDatumMonth() == castOther.getDatumMonth() || getDatumMonth() != null && castOther.getDatumMonth() != null && getDatumMonth().equals(castOther.getDatumMonth())) && (getDatumNewUserNum() == castOther.getDatumNewUserNum() || getDatumNewUserNum() != null && castOther.getDatumNewUserNum() != null && getDatumNewUserNum().equals(castOther.getDatumNewUserNum())) && (getDatumRecall() == castOther.getDatumRecall() || getDatumRecall() != null && castOther.getDatumRecall() != null && getDatumRecall().equals(castOther.getDatumRecall())) && (getDatumUpgradeMultiple() == castOther.getDatumUpgradeMultiple() || getDatumUpgradeMultiple() != null && castOther.getDatumUpgradeMultiple() != null && getDatumUpgradeMultiple().equals(castOther.getDatumUpgradeMultiple())) && (getIndexSelectConclusion() == castOther.getIndexSelectConclusion() || getIndexSelectConclusion() != null && castOther.getIndexSelectConclusion() != null && getIndexSelectConclusion().equals(castOther.getIndexSelectConclusion())) && (getLabelSelectConclusion() == castOther.getLabelSelectConclusion() || getLabelSelectConclusion() != null && castOther.getLabelSelectConclusion() != null && getLabelSelectConclusion().equals(castOther.getLabelSelectConclusion())) && (getLabelRuleConclusion() == castOther.getLabelRuleConclusion() || getLabelRuleConclusion() != null && castOther.getLabelRuleConclusion() != null && getLabelRuleConclusion().equals(castOther.getLabelRuleConclusion())) && (getFinalRuleConclusion() == castOther.getFinalRuleConclusion() || getFinalRuleConclusion() != null && castOther.getFinalRuleConclusion() != null && getFinalRuleConclusion().equals(castOther.getFinalRuleConclusion()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getCountRulesCode() != null ? getCountRulesCode().hashCode() : 0);
		result = 37 * result + (getOfferId() != null ? getOfferId().hashCode() : 0);
		result = 37 * result + (getEffectFlag() != null ? getEffectFlag().hashCode() : 0);
		result = 37 * result + (getRuleDesc() != null ? getRuleDesc().hashCode() : 0);
		result = 37 * result + (getDatumMonth() != null ? getDatumMonth().hashCode() : 0);
		result = 37 * result + (getDatumNewUserNum() != null ? getDatumNewUserNum().hashCode() : 0);
		result = 37 * result + (getDatumRecall() != null ? getDatumRecall().hashCode() : 0);
		result = 37 * result + (getDatumUpgradeMultiple() != null ? getDatumUpgradeMultiple().hashCode() : 0);
		result = 37 * result + (getIndexSelectConclusion() != null ? getIndexSelectConclusion().hashCode() : 0);
		result = 37 * result + (getLabelSelectConclusion() != null ? getLabelSelectConclusion().hashCode() : 0);
		result = 37 * result + (getLabelRuleConclusion() != null ? getLabelRuleConclusion().hashCode() : 0);
		result = 37 * result + (getFinalRuleConclusion() != null ? getFinalRuleConclusion().hashCode() : 0);
		return result;
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiVerifyFilterProcess.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiVerifyFilterProcess
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer mainKeyId;
	private String countRulesCode;
	private String processTypeId;
	private String labelIndexCode;
	private String labelIndexName;
	private String indexInterval;
	private Integer lastUserNum;
	private Double lastPropotion;
	private Integer notLastUserNum;
	private Double notLastPropotion;
	private Double eigenvalue;
	private String datumMonth;
	private Integer lastPropotionInt;
	private Integer notLastPropotionInt;
	private Integer datumNewUserNum;
	private String labelSelectConclusion;
	private String indexSelectConclusion;

	public CiVerifyFilterProcess()
	{
	}

	public CiVerifyFilterProcess(String countRulesCode, String processTypeId, String labelIndexCode, String labelIndexName, String indexInterval, Integer lastUserNum, Double lastPropotion, 
			Integer notLastUserNum, Double notLastPropotion, Double eigenvalue)
	{
		this.countRulesCode = countRulesCode;
		this.processTypeId = processTypeId;
		this.labelIndexCode = labelIndexCode;
		this.labelIndexName = labelIndexName;
		this.indexInterval = indexInterval;
		this.lastUserNum = lastUserNum;
		this.lastPropotion = lastPropotion;
		this.notLastUserNum = notLastUserNum;
		this.notLastPropotion = notLastPropotion;
		this.eigenvalue = eigenvalue;
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

	public String getProcessTypeId()
	{
		return processTypeId;
	}

	public void setProcessTypeId(String processTypeId)
	{
		this.processTypeId = processTypeId;
	}

	public String getLabelIndexCode()
	{
		return labelIndexCode;
	}

	public void setLabelIndexCode(String labelIndexCode)
	{
		this.labelIndexCode = labelIndexCode;
	}

	public String getLabelIndexName()
	{
		return labelIndexName;
	}

	public void setLabelIndexName(String labelIndexName)
	{
		this.labelIndexName = labelIndexName;
	}

	public String getIndexInterval()
	{
		return indexInterval;
	}

	public void setIndexInterval(String indexInterval)
	{
		this.indexInterval = indexInterval;
	}

	public Integer getLastUserNum()
	{
		return lastUserNum;
	}

	public void setLastUserNum(Integer lastUserNum)
	{
		this.lastUserNum = lastUserNum;
	}

	public Double getLastPropotion()
	{
		return lastPropotion;
	}

	public void setLastPropotion(Double lastPropotion)
	{
		this.lastPropotion = lastPropotion;
	}

	public Integer getNotLastUserNum()
	{
		return notLastUserNum;
	}

	public void setNotLastUserNum(Integer notLastUserNum)
	{
		this.notLastUserNum = notLastUserNum;
	}

	public Double getNotLastPropotion()
	{
		return notLastPropotion;
	}

	public void setNotLastPropotion(Double notLastPropotion)
	{
		this.notLastPropotion = notLastPropotion;
	}

	public Double getEigenvalue()
	{
		return eigenvalue;
	}

	public void setEigenvalue(Double eigenvalue)
	{
		this.eigenvalue = eigenvalue;
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

	public String getLabelSelectConclusion()
	{
		return labelSelectConclusion;
	}

	public void setLabelSelectConclusion(String labelSelectConclusion)
	{
		this.labelSelectConclusion = labelSelectConclusion;
	}

	public Integer getLastPropotionInt()
	{
		return lastPropotionInt;
	}

	public void setLastPropotionInt(Integer lastPropotionInt)
	{
		this.lastPropotionInt = lastPropotionInt;
	}

	public Integer getNotLastPropotionInt()
	{
		return notLastPropotionInt;
	}

	public void setNotLastPropotionInt(Integer notLastPropotionInt)
	{
		this.notLastPropotionInt = notLastPropotionInt;
	}

	public String getIndexSelectConclusion()
	{
		return indexSelectConclusion;
	}

	public void setIndexSelectConclusion(String indexSelectConclusion)
	{
		this.indexSelectConclusion = indexSelectConclusion;
	}
}

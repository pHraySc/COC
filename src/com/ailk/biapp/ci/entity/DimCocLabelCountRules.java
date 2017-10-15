// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimCocLabelCountRules.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class DimCocLabelCountRules
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String countRulesCode;
	private String dependIndex;
	private String countRules;
	private String countRulesDesc;
	private String labelTableConf;
	private Date effectiveTime;
	private Date invalidTime;

	public DimCocLabelCountRules()
	{
	}

	public DimCocLabelCountRules(String dependIndex, String countRules, String countRulesDesc, String labelTableConf, Date effectiveTime, Date invalidTime)
	{
		this.dependIndex = dependIndex;
		this.countRules = countRules;
		this.countRulesDesc = countRulesDesc;
		this.labelTableConf = labelTableConf;
		this.effectiveTime = effectiveTime;
		this.invalidTime = invalidTime;
	}

	public String getCountRulesCode()
	{
		return countRulesCode;
	}

	public void setCountRulesCode(String countRulesCode)
	{
		this.countRulesCode = countRulesCode;
	}

	public String getDependIndex()
	{
		return dependIndex;
	}

	public void setDependIndex(String dependIndex)
	{
		this.dependIndex = dependIndex;
	}

	public String getCountRules()
	{
		return countRules;
	}

	public void setCountRules(String countRules)
	{
		this.countRules = countRules;
	}

	public String getCountRulesDesc()
	{
		return countRulesDesc;
	}

	public void setCountRulesDesc(String countRulesDesc)
	{
		this.countRulesDesc = countRulesDesc;
	}

	public String getLabelTableConf()
	{
		return labelTableConf;
	}

	public void setLabelTableConf(String labelTableConf)
	{
		this.labelTableConf = labelTableConf;
	}

	public Date getEffectiveTime()
	{
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime)
	{
		this.effectiveTime = effectiveTime;
	}

	public Date getInvalidTime()
	{
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime)
	{
		this.invalidTime = invalidTime;
	}
}

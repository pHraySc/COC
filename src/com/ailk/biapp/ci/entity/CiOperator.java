// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiOperator.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiOperator
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer operatorId;
	private String operatorValue;
	private String operatorDesc;

	public CiOperator()
	{
	}

	public CiOperator(String operatorValue, String operatorDesc)
	{
		this.operatorValue = operatorValue;
		this.operatorDesc = operatorDesc;
	}

	public Integer getOperatorId()
	{
		return operatorId;
	}

	public void setOperatorId(Integer operatorId)
	{
		this.operatorId = operatorId;
	}

	public String getOperatorValue()
	{
		return operatorValue;
	}

	public void setOperatorValue(String operatorValue)
	{
		this.operatorValue = operatorValue;
	}

	public String getOperatorDesc()
	{
		return operatorDesc;
	}

	public void setOperatorDesc(String operatorDesc)
	{
		this.operatorDesc = operatorDesc;
	}
}

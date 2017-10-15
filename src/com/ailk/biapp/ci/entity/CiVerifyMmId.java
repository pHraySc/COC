// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiVerifyMmId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiVerifyMmId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String countRulesCode;
	private String dataDate;

	public CiVerifyMmId()
	{
	}

	public CiVerifyMmId(String countRulesCode, String dataDate)
	{
		this.countRulesCode = countRulesCode;
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

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof CiVerifyMmId))
		{
			return false;
		} else
		{
			CiVerifyMmId castOther = (CiVerifyMmId)other;
			return (getCountRulesCode() == castOther.getCountRulesCode() || getCountRulesCode() != null && castOther.getCountRulesCode() != null && getCountRulesCode().equals(castOther.getCountRulesCode())) && (getDataDate() == castOther.getDataDate() || getDataDate() != null && castOther.getDataDate() != null && getDataDate().equals(castOther.getDataDate()));
		}
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + (getCountRulesCode() != null ? getCountRulesCode().hashCode() : 0);
		result = 37 * result + (getDataDate() != null ? getDataDate().hashCode() : 0);
		return result;
	}
}

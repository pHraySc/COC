// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CustomProductMatch.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CustomProductMatch
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String matchId;
	private String dataDate;
	private Integer productId;
	private String listTableName;
	private Integer matchCustomNum;
	private Double matchPropotion;
	private String productName;

	public CustomProductMatch()
	{
	}

	public int hashCode()
	{
		int prime = 31;
		int result = 1;
		result = 31 * result + (dataDate != null ? dataDate.hashCode() : 0);
		result = 31 * result + (listTableName != null ? listTableName.hashCode() : 0);
		result = 31 * result + (matchCustomNum != null ? matchCustomNum.hashCode() : 0);
		result = 31 * result + (matchId != null ? matchId.hashCode() : 0);
		result = 31 * result + (matchPropotion != null ? matchPropotion.hashCode() : 0);
		result = 31 * result + (productId != null ? productId.hashCode() : 0);
		result = 31 * result + (productName != null ? productName.hashCode() : 0);
		return result;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomProductMatch other = (CustomProductMatch)obj;
		if (dataDate == null)
		{
			if (other.dataDate != null)
				return false;
		} else
		if (!dataDate.equals(other.dataDate))
			return false;
		if (listTableName == null)
		{
			if (other.listTableName != null)
				return false;
		} else
		if (!listTableName.equals(other.listTableName))
			return false;
		if (matchCustomNum == null)
		{
			if (other.matchCustomNum != null)
				return false;
		} else
		if (!matchCustomNum.equals(other.matchCustomNum))
			return false;
		if (matchId == null)
		{
			if (other.matchId != null)
				return false;
		} else
		if (!matchId.equals(other.matchId))
			return false;
		if (matchPropotion == null)
		{
			if (other.matchPropotion != null)
				return false;
		} else
		if (!matchPropotion.equals(other.matchPropotion))
			return false;
		if (productId == null)
		{
			if (other.productId != null)
				return false;
		} else
		if (!productId.equals(other.productId))
			return false;
		if (productName == null)
		{
			if (other.productName != null)
				return false;
		} else
		if (!productName.equals(other.productName))
			return false;
		return true;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CustomProductMatch [matchId=").append(matchId).append(", dataDate=").append(dataDate).append(", productId=").append(productId).append(", listTableName=").append(listTableName).append(", matchCustomNum=").append(matchCustomNum).append(", matchPropotion=").append(matchPropotion).append(", productName=").append(productName).append("]").toString();
	}

	public String getMatchId()
	{
		return matchId;
	}

	public void setMatchId(String matchId)
	{
		this.matchId = matchId;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public Integer getProductId()
	{
		return productId;
	}

	public void setProductId(Integer productId)
	{
		this.productId = productId;
	}

	public String getListTableName()
	{
		return listTableName;
	}

	public void setListTableName(String listTableName)
	{
		this.listTableName = listTableName;
	}

	public Integer getMatchCustomNum()
	{
		return matchCustomNum;
	}

	public void setMatchCustomNum(Integer matchCustomNum)
	{
		this.matchCustomNum = matchCustomNum;
	}

	public Double getMatchPropotion()
	{
		return matchPropotion;
	}

	public void setMatchPropotion(Double matchPropotion)
	{
		this.matchPropotion = matchPropotion;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}
}

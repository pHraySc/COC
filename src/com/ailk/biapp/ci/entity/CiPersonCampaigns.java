// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiPersonCampaigns.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiPersonCampaigns
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String productNo;
	private String camp0;
	private String camp1;
	private String camp2;
	private String camp3;
	private String camp4;
	private String camp5;
	private String camp6;
	private String camp7;
	private String camp8;
	private String camp9;

	public CiPersonCampaigns()
	{
	}

	public String getProductNo()
	{
		return productNo;
	}

	public void setProductNo(String productNo)
	{
		this.productNo = productNo;
	}

	public String getCamp0()
	{
		return camp0;
	}

	public void setCamp0(String camp0)
	{
		this.camp0 = camp0;
	}

	public String getCamp1()
	{
		return camp1;
	}

	public void setCamp1(String camp1)
	{
		this.camp1 = camp1;
	}

	public String getCamp2()
	{
		return camp2;
	}

	public void setCamp2(String camp2)
	{
		this.camp2 = camp2;
	}

	public String getCamp3()
	{
		return camp3;
	}

	public void setCamp3(String camp3)
	{
		this.camp3 = camp3;
	}

	public String getCamp4()
	{
		return camp4;
	}

	public void setCamp4(String camp4)
	{
		this.camp4 = camp4;
	}

	public String getCamp5()
	{
		return camp5;
	}

	public void setCamp5(String camp5)
	{
		this.camp5 = camp5;
	}

	public String getCamp6()
	{
		return camp6;
	}

	public void setCamp6(String camp6)
	{
		this.camp6 = camp6;
	}

	public String getCamp7()
	{
		return camp7;
	}

	public void setCamp7(String camp7)
	{
		this.camp7 = camp7;
	}

	public String getCamp8()
	{
		return camp8;
	}

	public void setCamp8(String camp8)
	{
		this.camp8 = camp8;
	}

	public String getCamp9()
	{
		return camp9;
	}

	public void setCamp9(String camp9)
	{
		this.camp9 = camp9;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiPersonCampaigns [productNo=").append(productNo).append(", camp0=").append(camp0).append(", camp1=").append(camp1).append(", camp2=").append(camp2).append(", camp3=").append(camp3).append(", camp4=").append(camp4).append(", camp5=").append(camp5).append(", camp6=").append(camp6).append(", camp7=").append(camp7).append(", camp8=").append(camp8).append(", camp9=").append(camp9).append("]").toString();
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelProductRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiLabelProductRelId

public class CiLabelProductRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiLabelProductRelId id;
	private String marketCase;

	public CiLabelProductRel()
	{
	}

	public CiLabelProductRel(CiLabelProductRelId id)
	{
		this.id = id;
	}

	public CiLabelProductRel(CiLabelProductRelId id, String marketCase)
	{
		this.id = id;
		this.marketCase = marketCase;
	}

	public CiLabelProductRelId getId()
	{
		return id;
	}

	public void setId(CiLabelProductRelId id)
	{
		this.id = id;
	}

	public String getMarketCase()
	{
		return marketCase;
	}

	public void setMarketCase(String marketCase)
	{
		this.marketCase = marketCase;
	}
}

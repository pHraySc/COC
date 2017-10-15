// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiProductLableRule.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiProductLableRuleId

public class CiProductLableRule
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiProductLableRuleId id;

	public CiProductLableRule()
	{
	}

	public CiProductLableRule(CiProductLableRuleId id)
	{
		this.id = id;
	}

	public CiProductLableRuleId getId()
	{
		return id;
	}

	public void setId(CiProductLableRuleId id)
	{
		this.id = id;
	}
}

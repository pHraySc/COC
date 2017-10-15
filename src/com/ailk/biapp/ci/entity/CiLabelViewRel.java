// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelViewRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiLabelViewRelId

public class CiLabelViewRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiLabelViewRelId id;

	public CiLabelViewRel()
	{
	}

	public CiLabelViewRel(CiLabelViewRelId id)
	{
		this.id = id;
	}

	public CiLabelViewRelId getId()
	{
		return id;
	}

	public void setId(CiLabelViewRelId id)
	{
		this.id = id;
	}
}

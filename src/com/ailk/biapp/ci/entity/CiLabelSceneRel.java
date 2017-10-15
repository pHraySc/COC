// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelSceneRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiLabelSceneRelId

public class CiLabelSceneRel
	implements Serializable, Cloneable
{

	private static final long serialVersionUID = 1L;
	private CiLabelSceneRelId id;

	public CiLabelSceneRel()
	{
	}

	public CiLabelSceneRelId getId()
	{
		return id;
	}

	public void setId(CiLabelSceneRelId id)
	{
		this.id = id;
	}
}

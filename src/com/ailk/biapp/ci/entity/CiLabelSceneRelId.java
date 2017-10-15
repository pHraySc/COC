// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelSceneRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiLabelSceneRelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer labelId;
	private String sceneId;

	public CiLabelSceneRelId()
	{
	}

	public Integer getLabelId()
	{
		return labelId;
	}

	public void setLabelId(Integer labelId)
	{
		this.labelId = labelId;
	}

	public String getSceneId()
	{
		return sceneId;
	}

	public void setSceneId(String sceneId)
	{
		this.sceneId = sceneId;
	}
}

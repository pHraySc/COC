// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomSceneRelId.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiCustomSceneRelId
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String customGroupId;
	private String sceneId;

	public CiCustomSceneRelId()
	{
	}

	public CiCustomSceneRelId(String userId, String customGroupId, String sceneId)
	{
		this.userId = userId;
		this.customGroupId = customGroupId;
		this.sceneId = sceneId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
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

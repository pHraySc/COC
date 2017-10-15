// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DimScene.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class DimScene
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String sceneId;
	private String sceneName;
	private String sceneDesc;
	private Integer sortNum;

	public DimScene()
	{
	}

	public DimScene(String sceneId)
	{
		this.sceneId = sceneId;
	}

	public DimScene(String sceneId, String sceneName, String sceneDesc, Integer sortNum)
	{
		this.sceneId = sceneId;
		this.sceneName = sceneName;
		this.sceneDesc = sceneDesc;
		this.sortNum = sortNum;
	}

	public String getSceneId()
	{
		return sceneId;
	}

	public void setSceneId(String sceneId)
	{
		this.sceneId = sceneId;
	}

	public String getSceneName()
	{
		return sceneName;
	}

	public void setSceneName(String sceneName)
	{
		this.sceneName = sceneName;
	}

	public String getSceneDesc()
	{
		return sceneDesc;
	}

	public void setSceneDesc(String sceneDesc)
	{
		this.sceneDesc = sceneDesc;
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}
}

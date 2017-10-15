// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiLabelCvDefine.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class CiLabelCvDefine
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String cviewId;
	private String cviewName;
	private String creator;
	private Timestamp createTime;
	private Timestamp lastModifyTime;
	private String descTxt;
	private String mainTableName;
	private String cityId;
	private String parentId;
	private String relaPrimaryKey;
	private Integer cviewTypeId;

	public CiLabelCvDefine()
	{
	}

	public CiLabelCvDefine(String cviewName, String creator, Timestamp createTime, Timestamp lastModifyTime, String descTxt, String mainTableName, String cityId, 
			String parentId, String relaPrimaryKey, Integer cviewTypeId)
	{
		this.cviewName = cviewName;
		this.creator = creator;
		this.createTime = createTime;
		this.lastModifyTime = lastModifyTime;
		this.descTxt = descTxt;
		this.mainTableName = mainTableName;
		this.cityId = cityId;
		this.parentId = parentId;
		this.relaPrimaryKey = relaPrimaryKey;
		this.cviewTypeId = cviewTypeId;
	}

	public String getCviewId()
	{
		return cviewId;
	}

	public void setCviewId(String cviewId)
	{
		this.cviewId = cviewId;
	}

	public String getCviewName()
	{
		return cviewName;
	}

	public void setCviewName(String cviewName)
	{
		this.cviewName = cviewName;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

	public Timestamp getLastModifyTime()
	{
		return lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime)
	{
		this.lastModifyTime = lastModifyTime;
	}

	public String getDescTxt()
	{
		return descTxt;
	}

	public void setDescTxt(String descTxt)
	{
		this.descTxt = descTxt;
	}

	public String getMainTableName()
	{
		return mainTableName;
	}

	public void setMainTableName(String mainTableName)
	{
		this.mainTableName = mainTableName;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public String getRelaPrimaryKey()
	{
		return relaPrimaryKey;
	}

	public void setRelaPrimaryKey(String relaPrimaryKey)
	{
		this.relaPrimaryKey = relaPrimaryKey;
	}

	public Integer getCviewTypeId()
	{
		return cviewTypeId;
	}

	public void setCviewTypeId(Integer cviewTypeId)
	{
		this.cviewTypeId = cviewTypeId;
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiCustomCampsegRel.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

// Referenced classes of package com.ailk.biapp.ci.entity:
//			CiCustomCampsegRelId

public class CiCustomCampsegRel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiCustomCampsegRelId relId;
	private String campsegName;
	private String campsegCreateUser;
	private Date approveEndTime;

	public CiCustomCampsegRel()
	{
	}

	public CiCustomCampsegRelId getRelId()
	{
		return relId;
	}

	public void setRelId(CiCustomCampsegRelId relId)
	{
		this.relId = relId;
	}

	public String getCampsegName()
	{
		return campsegName;
	}

	public void setCampsegName(String campsegName)
	{
		this.campsegName = campsegName;
	}

	public String getCampsegCreateUser()
	{
		return campsegCreateUser;
	}

	public void setCampsegCreateUser(String campsegCreateUser)
	{
		this.campsegCreateUser = campsegCreateUser;
	}

	public Date getApproveEndTime()
	{
		return approveEndTime;
	}

	public void setApproveEndTime(Date approveEndTime)
	{
		this.approveEndTime = approveEndTime;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CiCustomCampsegRel [relId=").append(relId).append(", campsegName=").append(campsegName).append(", campsegCreateUser=").append(campsegCreateUser).append(", approveEndTime=").append(approveEndTime).append("]").toString();
	}
}

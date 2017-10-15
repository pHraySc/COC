package com.ailk.biapp.ci.model;

import java.util.Date;

public class CiCustomCampsegRel
{

	private String campsegName;
	private String campsegCreateUser;
	private Date approveEndTime;
	private String customGroupId;
	private String campsegId;

	public CiCustomCampsegRel()
	{
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

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
	}

	public String getCampsegId()
	{
		return campsegId;
	}

	public void setCampsegId(String campsegId)
	{
		this.campsegId = campsegId;
	}
}

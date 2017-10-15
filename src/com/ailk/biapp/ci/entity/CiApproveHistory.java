// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiApproveHistory.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;

public class CiApproveHistory
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer historyId;
	private String processId;
	private String resourceId;
	private String resourceName;
	private String resourceTypeId;
	private String resourceCreateUserId;
	private String approveRoleId;
	private String approveUserId;
	private Date approveTime;
	private String approveOpinion;
	private Integer approveResult;
	private String resourceCreateUserName;
	private Date startDate;
	private Date endDate;
	private String userId;
	private String resourceTypeName;
	private String resourceDetailLink;

	public CiApproveHistory()
	{
	}

	public CiApproveHistory(Integer labelId, String approveRoleId, String approveUserId, Date approveTime, String approveOpinion, Integer approveResult)
	{
		this.approveRoleId = approveRoleId;
		this.approveUserId = approveUserId;
		this.approveTime = approveTime;
		this.approveOpinion = approveOpinion;
		this.approveResult = approveResult;
	}

	public Integer getHistoryId()
	{
		return historyId;
	}

	public String getResourceCreateUserId()
	{
		return resourceCreateUserId;
	}

	public void setResourceCreateUserId(String resourceCreateUserId)
	{
		this.resourceCreateUserId = resourceCreateUserId;
	}

	public void setHistoryId(Integer historyId)
	{
		this.historyId = historyId;
	}

	public String getProcessId()
	{
		return processId;
	}

	public void setProcessId(String processId)
	{
		this.processId = processId;
	}

	public String getResourceId()
	{
		return resourceId;
	}

	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}

	public String getResourceName()
	{
		return resourceName;
	}

	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}

	public String getResourceTypeId()
	{
		return resourceTypeId;
	}

	public void setResourceTypeId(String resourceTypeId)
	{
		this.resourceTypeId = resourceTypeId;
	}

	public String getApproveRoleId()
	{
		return approveRoleId;
	}

	public void setApproveRoleId(String approveRoleId)
	{
		this.approveRoleId = approveRoleId;
	}

	public String getApproveUserId()
	{
		return approveUserId;
	}

	public void setApproveUserId(String approveUserId)
	{
		this.approveUserId = approveUserId;
	}

	public Date getApproveTime()
	{
		return approveTime;
	}

	public void setApproveTime(Date approveTime)
	{
		this.approveTime = approveTime;
	}

	public String getApproveOpinion()
	{
		return approveOpinion;
	}

	public void setApproveOpinion(String approveOpinion)
	{
		this.approveOpinion = approveOpinion;
	}

	public Integer getApproveResult()
	{
		return approveResult;
	}

	public void setApproveResult(Integer approveResult)
	{
		this.approveResult = approveResult;
	}

	public String getResourceCreateUserName()
	{
		return resourceCreateUserName;
	}

	public void setResourceCreateUserName(String resourceCreateUserName)
	{
		this.resourceCreateUserName = resourceCreateUserName;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getResourceTypeName()
	{
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName)
	{
		this.resourceTypeName = resourceTypeName;
	}

	public String getResourceDetailLink()
	{
		return resourceDetailLink;
	}

	public void setResourceDetailLink(String resourceDetailLink)
	{
		this.resourceDetailLink = resourceDetailLink;
	}
}

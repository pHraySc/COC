// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiApproveStatus.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class CiApproveStatus
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer statusId;
	private String processId;
	private String resourceId;
	private String resourceName;
	private String resourceTypeId;
	private String resourceCreateUserId;
	private String deptId;
	private String lastApproveRoleId;
	private String lastApproveUserId;
	private Date lastApproveTime;
	private String lastApproveOpinion;
	private String currApproveStatusId;
	private String resourceCreateUserName;
	private String lastApproveUserName;
	private Date startDate;
	private Date endDate;
	private String userId;
	private String resourceTypeName;
	private String approveStatusName;
	private Integer sortNum;
	private String resourceDetailLink;
	private Map approveRoleIdContainer;

	public CiApproveStatus()
	{
	}

	public CiApproveStatus(String lastApproveRoleId, String lastApproveUserId, Timestamp lastApproveTime, String lastApproveOpinion, String currApproveStatusId)
	{
		this.lastApproveRoleId = lastApproveRoleId;
		this.lastApproveUserId = lastApproveUserId;
		this.lastApproveTime = lastApproveTime;
		this.lastApproveOpinion = lastApproveOpinion;
		this.currApproveStatusId = currApproveStatusId;
	}

	public String getLastApproveRoleId()
	{
		return lastApproveRoleId;
	}

	public String getResourceCreateUserId()
	{
		return resourceCreateUserId;
	}

	public void setResourceCreateUserId(String resourceCreateUserId)
	{
		this.resourceCreateUserId = resourceCreateUserId;
	}

	public Integer getStatusId()
	{
		return statusId;
	}

	public void setStatusId(Integer statusId)
	{
		this.statusId = statusId;
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

	public String getDeptId()
	{
		return deptId;
	}

	public void setDeptId(String deptId)
	{
		this.deptId = deptId;
	}

	public void setLastApproveRoleId(String lastApproveRoleId)
	{
		this.lastApproveRoleId = lastApproveRoleId;
	}

	public String getLastApproveUserId()
	{
		return lastApproveUserId;
	}

	public void setLastApproveUserId(String lastApproveUserId)
	{
		this.lastApproveUserId = lastApproveUserId;
	}

	public Date getLastApproveTime()
	{
		return lastApproveTime;
	}

	public void setLastApproveTime(Date lastApproveTime)
	{
		this.lastApproveTime = lastApproveTime;
	}

	public String getLastApproveOpinion()
	{
		return lastApproveOpinion;
	}

	public void setLastApproveOpinion(String lastApproveOpinion)
	{
		this.lastApproveOpinion = lastApproveOpinion;
	}

	public String getCurrApproveStatusId()
	{
		return currApproveStatusId;
	}

	public void setCurrApproveStatusId(String currApproveStatusId)
	{
		this.currApproveStatusId = currApproveStatusId;
	}

	public String getResourceCreateUserName()
	{
		return resourceCreateUserName;
	}

	public void setResourceCreateUserName(String resourceCreateUserName)
	{
		this.resourceCreateUserName = resourceCreateUserName;
	}

	public String getLastApproveUserName()
	{
		return lastApproveUserName;
	}

	public void setLastApproveUserName(String lastApproveUserName)
	{
		this.lastApproveUserName = lastApproveUserName;
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

	public String getApproveStatusName()
	{
		return approveStatusName;
	}

	public void setApproveStatusName(String approveStatusName)
	{
		this.approveStatusName = approveStatusName;
	}

	public Integer getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}

	public String getResourceDetailLink()
	{
		return resourceDetailLink;
	}

	public void setResourceDetailLink(String resourceDetailLink)
	{
		this.resourceDetailLink = resourceDetailLink;
	}

	public Map getApproveRoleIdContainer()
	{
		return approveRoleIdContainer;
	}

	public void setApproveRoleIdContainer(Map approveRoleIdContainer)
	{
		this.approveRoleIdContainer = approveRoleIdContainer;
	}
}

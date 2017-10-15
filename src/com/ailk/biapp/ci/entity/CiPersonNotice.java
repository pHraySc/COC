// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CiPersonNotice.java

package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class CiPersonNotice
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String noticeId;
	private String noticeName;
	private String noticeDetail;
	private Integer noticeTypeId;
	private Date noticeSendTime;
	private Integer labelId;
	private String customerGroupId;
	private Integer status;
	private String releaseUserId;
	private Integer isSuccess;
	private String receiveUserId;
	private Integer readStatus;
	private Integer isShowTip;
	private Date startDate;
	private Date endDate;

	public CiPersonNotice()
	{
	}

	public CiPersonNotice(String noticeName, String noticeDetail, Integer noticeTypeId, Timestamp noticeSendTime, Integer labelId, Integer status, Integer isShowTip)
	{
		this.noticeName = noticeName;
		this.noticeDetail = noticeDetail;
		this.noticeTypeId = noticeTypeId;
		this.noticeSendTime = noticeSendTime;
		this.labelId = labelId;
		this.status = status;
		this.isShowTip = isShowTip;
	}

	public String getNoticeId()
	{
		return noticeId;
	}

	public void setNoticeId(String noticeId)
	{
		this.noticeId = noticeId;
	}

	public String getNoticeName()
	{
		return noticeName;
	}

	public void setNoticeName(String noticeName)
	{
		this.noticeName = noticeName;
	}

	public String getNoticeDetail()
	{
		return noticeDetail;
	}

	public void setNoticeDetail(String noticeDetail)
	{
		this.noticeDetail = noticeDetail;
	}

	public Integer getNoticeTypeId()
	{
		return noticeTypeId;
	}

	public void setNoticeTypeId(Integer noticeTypeId)
	{
		this.noticeTypeId = noticeTypeId;
	}

	public Date getNoticeSendTime()
	{
		return noticeSendTime;
	}

	public void setNoticeSendTime(Date noticeSendTime)
	{
		this.noticeSendTime = noticeSendTime;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getCustomerGroupId()
	{
		return customerGroupId;
	}

	public void setCustomerGroupId(String customerGroupId)
	{
		this.customerGroupId = customerGroupId;
	}

	public String getReleaseUserId()
	{
		return releaseUserId;
	}

	public void setReleaseUserId(String releaseUserId)
	{
		this.releaseUserId = releaseUserId;
	}

	public String getReceiveUserId()
	{
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId)
	{
		this.receiveUserId = receiveUserId;
	}

	public Integer getReadStatus()
	{
		return readStatus;
	}

	public void setReadStatus(Integer readStatus)
	{
		this.readStatus = readStatus;
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

	public void setLabelId(Integer labelId)
	{
		this.labelId = labelId;
	}

	public Integer getLabelId()
	{
		return labelId;
	}

	public Integer getIsSuccess()
	{
		return isSuccess;
	}

	public void setIsSuccess(Integer isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public Integer getIsShowTip()
	{
		return isShowTip;
	}

	public void setIsShowTip(Integer isShowTip)
	{
		this.isShowTip = isShowTip;
	}
}

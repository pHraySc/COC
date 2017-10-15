package com.ailk.biapp.ci.entity;

import java.io.Serializable;
import java.util.Date;


public class CiCustomGroupPushCycle
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CiCustomGroupPushCycleId id;
	private Integer status;
	private Integer pushCycle;
	private Integer isPushed;
	private Date modifyTime;

	public CiCustomGroupPushCycle()
	{
	}

	public CiCustomGroupPushCycle(CiCustomGroupPushCycleId id, Integer status)
	{
		this.id = id;
		this.status = status;
	}

	public CiCustomGroupPushCycleId getId()
	{
		return id;
	}

	public void setId(CiCustomGroupPushCycleId id)
	{
		this.id = id;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Integer getPushCycle()
	{
		return pushCycle;
	}

	public void setPushCycle(Integer pushCycle)
	{
		this.pushCycle = pushCycle;
	}

	public Integer getIsPushed()
	{
		return isPushed;
	}

	public void setIsPushed(Integer isPushed)
	{
		this.isPushed = isPushed;
	}

	public Date getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}
}

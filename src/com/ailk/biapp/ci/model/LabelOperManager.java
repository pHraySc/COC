package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class LabelOperManager
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer stopFlag;
	private Integer onLineFlag;
	private Integer offLineFlag;
	private Integer modifyFlag;
	private Integer deleteFlag;

	public LabelOperManager()
	{
		stopFlag = Integer.valueOf(0);
		onLineFlag = Integer.valueOf(0);
		offLineFlag = Integer.valueOf(0);
		modifyFlag = Integer.valueOf(0);
		deleteFlag = Integer.valueOf(0);
	}

	public LabelOperManager(Integer stopFlag, Integer onLineFlag, Integer offLineFlag, Integer modifyFlag, Integer deleteFlag)
	{
		this.stopFlag = Integer.valueOf(0);
		this.onLineFlag = Integer.valueOf(0);
		this.offLineFlag = Integer.valueOf(0);
		this.modifyFlag = Integer.valueOf(0);
		this.deleteFlag = Integer.valueOf(0);
		this.stopFlag = stopFlag;
		this.onLineFlag = onLineFlag;
		this.offLineFlag = offLineFlag;
		this.modifyFlag = modifyFlag;
		this.deleteFlag = deleteFlag;
	}

	public Integer getStopFlag()
	{
		return stopFlag;
	}

	public void setStopFlag(Integer stopFlag)
	{
		this.stopFlag = stopFlag;
	}

	public Integer getOnLineFlag()
	{
		return onLineFlag;
	}

	public void setOnLineFlag(Integer onLineFlag)
	{
		this.onLineFlag = onLineFlag;
	}

	public Integer getOffLineFlag()
	{
		return offLineFlag;
	}

	public void setOffLineFlag(Integer offLineFlag)
	{
		this.offLineFlag = offLineFlag;
	}

	public Integer getModifyFlag()
	{
		return modifyFlag;
	}

	public void setModifyFlag(Integer modifyFlag)
	{
		this.modifyFlag = modifyFlag;
	}

	public Integer getDeleteFlag()
	{
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag)
	{
		this.deleteFlag = deleteFlag;
	}
}

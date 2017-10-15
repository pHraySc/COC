package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class LabelShortInfo
	implements Serializable
{

	private static final long serialVersionUID = 0xc7229284698d9414L;
	public String labelId;
	public String labelName;
	public String updateCycle;
	public String customNum;
	public String dataDate;

	public String getLabelId()
	{
		return labelId;
	}

	public void setLabelId(String labelId)
	{
		this.labelId = labelId;
	}

	public String getLabelName()
	{
		return labelName;
	}

	public void setLabelName(String labelName)
	{
		this.labelName = labelName;
	}

	public String getCustomNum()
	{
		return customNum;
	}

	public void setCustomNum(String customNum)
	{
		this.customNum = customNum;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public String getUpdateCycle()
	{
		return updateCycle;
	}

	public void setUpdateCycle(String updateCycle)
	{
		this.updateCycle = updateCycle;
	}

	public LabelShortInfo()
	{
	}

	public LabelShortInfo(String labelId, String labelName, String updateCycle, String customNum, String dataDate)
	{
		this.labelId = labelId;
		this.labelName = labelName;
		this.updateCycle = updateCycle;
		this.customNum = customNum;
		this.dataDate = dataDate;
	}

	public String toString()
	{
		return (new StringBuilder()).append("LabelShortInfo [customNum=").append(customNum).append(", dataDate=").append(dataDate).append(", labelId=").append(labelId).append(", labelName=").append(labelName).append(", updateCycle=").append(updateCycle).append("]").toString();
	}
}

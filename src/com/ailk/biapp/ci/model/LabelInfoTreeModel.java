package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class LabelInfoTreeModel
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer updateCycle;
	private Integer labelTypeId;
	private Integer labelLevel;
	private String ctrlTypeId;
	private Double minVal;
	private Double maxVal;
	private Integer isStatUserNum;
	private Integer isLeaf;
	private Integer customNum;
	private String effectDate;
	private String effectTime;
	private String failTime;
	private Integer isCanDrag;
	private String attrVal;

	public LabelInfoTreeModel()
	{
	}

	public String getEffectTime()
	{
		return effectTime;
	}

	public void setEffectTime(String effectTime)
	{
		this.effectTime = effectTime;
	}

	public String getFailTime()
	{
		return failTime;
	}

	public void setFailTime(String failTime)
	{
		this.failTime = failTime;
	}

	public Integer getUpdateCycle()
	{
		return updateCycle;
	}

	public void setUpdateCycle(Integer updateCycle)
	{
		this.updateCycle = updateCycle;
	}

	public Integer getLabelTypeId()
	{
		return labelTypeId;
	}

	public void setLabelTypeId(Integer labelTypeId)
	{
		this.labelTypeId = labelTypeId;
	}

	public Integer getLabelLevel()
	{
		return labelLevel;
	}

	public void setLabelLevel(Integer labelLevel)
	{
		this.labelLevel = labelLevel;
	}

	public String getCtrlTypeId()
	{
		return ctrlTypeId;
	}

	public void setCtrlTypeId(String ctrlTypeId)
	{
		this.ctrlTypeId = ctrlTypeId;
	}

	public Double getMinVal()
	{
		return minVal;
	}

	public void setMinVal(Double minVal)
	{
		this.minVal = minVal;
	}

	public Double getMaxVal()
	{
		return maxVal;
	}

	public void setMaxVal(Double maxVal)
	{
		this.maxVal = maxVal;
	}

	public Integer getIsStatUserNum()
	{
		return isStatUserNum;
	}

	public void setIsStatUserNum(Integer isStatUserNum)
	{
		this.isStatUserNum = isStatUserNum;
	}

	public Integer getIsLeaf()
	{
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf)
	{
		this.isLeaf = isLeaf;
	}

	public Integer getIsCanDrag()
	{
		return isCanDrag;
	}

	public void setIsCanDrag(Integer isCanDrag)
	{
		this.isCanDrag = isCanDrag;
	}

	public Integer getCustomNum()
	{
		return customNum;
	}

	public void setCustomNum(Integer customNum)
	{
		this.customNum = customNum;
	}

	public String getEffectDate()
	{
		return effectDate;
	}

	public void setEffectDate(String effectDate)
	{
		this.effectDate = effectDate;
	}

	public String getAttrVal()
	{
		return attrVal;
	}

	public void setAttrVal(String attrVal)
	{
		this.attrVal = attrVal;
	}

	public String toString()
	{
		return (new StringBuilder()).append("LabelInfoTreeModel [updateCycle=").append(updateCycle).append(", labelTypeId=").append(labelTypeId).append(", labelLevel=").append(labelLevel).append(", ctrlTypeId=").append(ctrlTypeId).append(", minVal=").append(minVal).append(", maxVal=").append(maxVal).append(", isStatUserNum=").append(isStatUserNum).append(", isLeaf=").append(isLeaf).append(", customNum=").append(customNum).append(", effectDate=").append(effectDate).append(", effectTime=").append(effectTime).append(", failTime=").append(failTime).append(", isCanDrag=").append(isCanDrag).append(", attrVal=").append(attrVal).append("]").toString();
	}
}

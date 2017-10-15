package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class LabelContrastDetailInfo
	implements Serializable
{

	private static final long serialVersionUID = 0x2d54cc19e6f99be5L;
	private String mainLabelId;
	private String contrastLabelId;
	private String contrastLabelName;
	private String intersectionNum;
	private String mainDifferenceNum;
	private String contrastDifferenceNum;
	private String unionNum;
	private String intersectionRate;
	private String mainDifferenceRate;
	private String contrastDifferenceRate;

	public String getMainLabelId()
	{
		return mainLabelId;
	}

	public void setMainLabelId(String mainLabelId)
	{
		this.mainLabelId = mainLabelId;
	}

	public String getContrastLabelId()
	{
		return contrastLabelId;
	}

	public void setContrastLabelId(String contrastLabelId)
	{
		this.contrastLabelId = contrastLabelId;
	}

	public String getContrastLabelName()
	{
		return contrastLabelName;
	}

	public void setContrastLabelName(String contrastLabelName)
	{
		this.contrastLabelName = contrastLabelName;
	}

	public String getIntersectionNum()
	{
		return intersectionNum;
	}

	public void setIntersectionNum(String intersectionNum)
	{
		this.intersectionNum = intersectionNum;
	}

	public String getMainDifferenceNum()
	{
		return mainDifferenceNum;
	}

	public void setMainDifferenceNum(String mainDifferenceNum)
	{
		this.mainDifferenceNum = mainDifferenceNum;
	}

	public String getContrastDifferenceNum()
	{
		return contrastDifferenceNum;
	}

	public void setContrastDifferenceNum(String contrastDifferenceNum)
	{
		this.contrastDifferenceNum = contrastDifferenceNum;
	}

	public String getUnionNum()
	{
		return unionNum;
	}

	public void setUnionNum(String unionNum)
	{
		this.unionNum = unionNum;
	}

	public String getIntersectionRate()
	{
		return intersectionRate;
	}

	public void setIntersectionRate(String intersectionRate)
	{
		this.intersectionRate = intersectionRate;
	}

	public String getMainDifferenceRate()
	{
		return mainDifferenceRate;
	}

	public void setMainDifferenceRate(String mainDifferenceRate)
	{
		this.mainDifferenceRate = mainDifferenceRate;
	}

	public String getContrastDifferenceRate()
	{
		return contrastDifferenceRate;
	}

	public void setContrastDifferenceRate(String contrastDifferenceRate)
	{
		this.contrastDifferenceRate = contrastDifferenceRate;
	}

	public LabelContrastDetailInfo()
	{
		intersectionNum = "0";
		mainDifferenceNum = "0";
		contrastDifferenceNum = "0";
		unionNum = "0";
		intersectionRate = "0%";
		mainDifferenceRate = "0%";
		contrastDifferenceRate = "0%";
	}

	public LabelContrastDetailInfo(String mainLabelId, String contrastLabelId, String contrastLabelName, String intersectionNum, String mainDifferenceNum, String contrastDifferenceNum, String unionNum, 
			String intersectionRate, String mainDifferenceRate, String contrastDifferenceRate)
	{
		this.intersectionNum = "0";
		this.mainDifferenceNum = "0";
		this.contrastDifferenceNum = "0";
		this.unionNum = "0";
		this.intersectionRate = "0%";
		this.mainDifferenceRate = "0%";
		this.contrastDifferenceRate = "0%";
		this.mainLabelId = mainLabelId;
		this.contrastLabelId = contrastLabelId;
		this.contrastLabelName = contrastLabelName;
		this.intersectionNum = intersectionNum;
		this.mainDifferenceNum = mainDifferenceNum;
		this.contrastDifferenceNum = contrastDifferenceNum;
		this.unionNum = unionNum;
		this.intersectionRate = intersectionRate;
		this.mainDifferenceRate = mainDifferenceRate;
		this.contrastDifferenceRate = contrastDifferenceRate;
	}

	public String toString()
	{
		return (new StringBuilder()).append("LabelContrastDetailInfo [contrastDifferenceNum=").append(contrastDifferenceNum).append(", contrastDifferenceRate=").append(contrastDifferenceRate).append(", contrastLabelId=").append(contrastLabelId).append(", contrastLabelName=").append(contrastLabelName).append(", intersectionNum=").append(intersectionNum).append(", intersectionRate=").append(intersectionRate).append(", mainDifferenceNum=").append(mainDifferenceNum).append(", mainDifferenceRate=").append(mainDifferenceRate).append(", mainLabelId=").append(mainLabelId).append(", unionNum=").append(unionNum).append("]").toString();
	}
}

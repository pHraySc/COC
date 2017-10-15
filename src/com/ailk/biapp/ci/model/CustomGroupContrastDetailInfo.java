package com.ailk.biapp.ci.model;


public class CustomGroupContrastDetailInfo
{

	private String dataDate;
	private String customGroupId;
	private String customGroupName;
	private String customGroupNum;
	private String listTableName;
	private String contrastLabelId;
	private String contrastLabelName;
	private Integer contrastLabelNum;
	private String contrastLabelstr;
	private String intersectionNum;
	private String mainDifferenceNum;
	private String contrastDifferenceNum;
	private String unionNum;
	private String intersectionRate;
	private String mainDifferenceRate;
	private String contrastDifferenceRate;

	public CustomGroupContrastDetailInfo()
	{
		intersectionNum = "0";
		mainDifferenceNum = "0";
		contrastDifferenceNum = "0";
		unionNum = "0";
		intersectionRate = "0%";
		mainDifferenceRate = "0%";
		contrastDifferenceRate = "0%";
	}

	public String getCustomGroupId()
	{
		return customGroupId;
	}

	public void setCustomGroupId(String customGroupId)
	{
		this.customGroupId = customGroupId;
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

	public String getCustomGroupName()
	{
		return customGroupName;
	}

	public void setCustomGroupName(String customGroupName)
	{
		this.customGroupName = customGroupName;
	}

	public String getCustomGroupNum()
	{
		return customGroupNum;
	}

	public void setCustomGroupNum(String customGroupNum)
	{
		this.customGroupNum = customGroupNum;
	}

	public String getListTableName()
	{
		return listTableName;
	}

	public void setListTableName(String listTableName)
	{
		this.listTableName = listTableName;
	}

	public String getDataDate()
	{
		return dataDate;
	}

	public void setDataDate(String dataDate)
	{
		this.dataDate = dataDate;
	}

	public String getContrastLabelstr()
	{
		return contrastLabelstr;
	}

	public void setContrastLabelstr(String contrastLabelstr)
	{
		this.contrastLabelstr = contrastLabelstr;
	}

	public Integer getContrastLabelNum()
	{
		return contrastLabelNum;
	}

	public void setContrastLabelNum(Integer contrastLabelNum)
	{
		this.contrastLabelNum = contrastLabelNum;
	}
}

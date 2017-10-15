package com.ailk.biapp.ci.model;


public class GridDemoModel
{

	private long topicId;
	private String topicName;
	private String createUserId;
	private String categoryName;
	private String cycleTypeId;
	private String createDate;

	public GridDemoModel()
	{
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	public String getCreateUserId()
	{
		return createUserId;
	}

	public void setCreateUserId(String createUserId)
	{
		this.createUserId = createUserId;
	}

	public String getCycleTypeId()
	{
		return cycleTypeId;
	}

	public void setCycleTypeId(String cycleTypeId)
	{
		this.cycleTypeId = cycleTypeId;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	public long getTopicId()
	{
		return topicId;
	}

	public void setTopicId(long topicId)
	{
		this.topicId = topicId;
	}

	public String getTopicName()
	{
		return topicName;
	}

	public void setTopicName(String topicName)
	{
		this.topicName = topicName;
	}
}

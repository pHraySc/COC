package com.ailk.biapp.ci.core.model;

import java.io.Serializable;
import java.util.List;

public class CiLabelCategories
	implements Serializable, Cloneable
{

	private static final long serialVersionUID = 1L;
	private Integer labelId;
	private String labelName;
	private Integer parentId;
	private Integer labelLevel;
	private String labelIdLevelDesc;
	private String labelLevelDesc;
	private List children;

	public CiLabelCategories(Integer labelId, String labelName, Integer parentId, Integer labelLevel, String labelIdLevelDesc, String labelLevelDesc)
	{
		this.labelId = labelId;
		this.labelName = labelName;
		this.parentId = parentId;
		this.labelLevel = labelLevel;
		this.labelIdLevelDesc = labelIdLevelDesc;
		this.labelLevelDesc = labelLevelDesc;
	}

	public CiLabelCategories()
	{
	}

	public Integer getLabelId()
	{
		return labelId;
	}

	public void setLabelId(Integer labelId)
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

	public Integer getParentId()
	{
		return parentId;
	}

	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}

	public Integer getLabelLevel()
	{
		return labelLevel;
	}

	public void setLabelLevel(Integer labelLevel)
	{
		this.labelLevel = labelLevel;
	}

	public String getLabelLevelDesc()
	{
		return labelLevelDesc;
	}

	public void setLabelLevelDesc(String labelLevelDesc)
	{
		this.labelLevelDesc = labelLevelDesc;
	}

	public List getChildren()
	{
		return children;
	}

	public void setChildren(List children)
	{
		this.children = children;
	}

	public String getLabelIdLevelDesc()
	{
		return labelIdLevelDesc;
	}

	public void setLabelIdLevelDesc(String labelIdLevelDesc)
	{
		this.labelIdLevelDesc = labelIdLevelDesc;
	}
}

package com.ailk.biapp.ci.model;

import java.io.Serializable;
import java.util.List;

public class LabelAndIndexType
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String class_name;
	private List element_ids;

	public LabelAndIndexType()
	{
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getClass_name()
	{
		return class_name;
	}

	public void setClass_name(String class_name)
	{
		this.class_name = class_name;
	}

	public List getElement_ids()
	{
		return element_ids;
	}

	public void setElement_ids(List element_ids)
	{
		this.element_ids = element_ids;
	}
}

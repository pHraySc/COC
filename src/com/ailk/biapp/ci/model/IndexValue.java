package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class IndexValue
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String id;
	private String value;

	public IndexValue()
	{
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}

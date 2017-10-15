package com.ailk.biapp.ci.entity.base;

import java.io.Serializable;

public class EnumValue implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;

	public EnumValue() {
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}

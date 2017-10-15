package com.ailk.biapp.ci.entity.base;

import java.io.Serializable;

public class SysInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String SYS_VGOP = "VGOP";
	public static final String SYS_CRM = "CRM";
	public static final String SYS_MCD = "MCD";
	private String sysId;
	private String sysName;
	private String userId;

	public SysInfo() {
	}

	public String getSysId() {
		return this.sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSysName() {
		return this.sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
}

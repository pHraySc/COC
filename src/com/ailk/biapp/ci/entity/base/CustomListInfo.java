//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ailk.biapp.ci.entity.base;

import java.io.Serializable;

public class CustomListInfo implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private String listTableName;
	private String dataDate;
	private Long customNum;
	private Long duplicateNum;

	public CustomListInfo() {
	}

	public String getListTableName() {
		return this.listTableName;
	}

	public void setListTableName(String listTableName) {
		this.listTableName = listTableName;
	}

	public String getDataDate() {
		return this.dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public Long getCustomNum() {
		return this.customNum;
	}

	public void setCustomNum(Long customNum) {
		this.customNum = customNum;
	}

	public Long getDuplicateNum() {
		return this.duplicateNum;
	}

	public void setDuplicateNum(Long duplicateNum) {
		this.duplicateNum = duplicateNum;
	}
}

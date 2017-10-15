//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ailk.biapp.ci.entity.base;

import java.io.Serializable;
import java.util.Date;

public class CustomGroupInfo implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private String customGroupId;
	private String customGroupName;
	private String customGroupDesc;
	private Integer updateCycle;
	private String labelOptRuleShow;
	private Date startDate;
	private Date endDate;
	private Long customNum;
	private String monthLabelDate;
	private String dayLabelDate;
	private Long duplicateNum;
	private int isPush;

	public CustomGroupInfo() {
	}

	public String getCustomGroupId() {
		return this.customGroupId;
	}

	public void setCustomGroupId(String customGroupId) {
		this.customGroupId = customGroupId;
	}

	public String getCustomGroupName() {
		return this.customGroupName;
	}

	public void setCustomGroupName(String customGroupName) {
		this.customGroupName = customGroupName;
	}

	public String getCustomGroupDesc() {
		return this.customGroupDesc;
	}

	public void setCustomGroupDesc(String customGroupDesc) {
		this.customGroupDesc = customGroupDesc;
	}

	public Integer getUpdateCycle() {
		return this.updateCycle;
	}

	public void setUpdateCycle(Integer updateCycle) {
		this.updateCycle = updateCycle;
	}

	public String getLabelOptRuleShow() {
		return this.labelOptRuleShow;
	}

	public void setLabelOptRuleShow(String labelOptRuleShow) {
		this.labelOptRuleShow = labelOptRuleShow;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getCustomNum() {
		return this.customNum;
	}

	public void setCustomNum(Long customNum) {
		this.customNum = customNum;
	}

	public String getMonthLabelDate() {
		return this.monthLabelDate;
	}

	public void setMonthLabelDate(String monthLabelDate) {
		this.monthLabelDate = monthLabelDate;
	}

	public String getDayLabelDate() {
		return this.dayLabelDate;
	}

	public void setDayLabelDate(String dayLabelDate) {
		this.dayLabelDate = dayLabelDate;
	}

	public Long getDuplicateNum() {
		return this.duplicateNum;
	}

	public void setDuplicateNum(Long duplicateNum) {
		this.duplicateNum = duplicateNum;
	}

	public int getIsPush() {
		return this.isPush;
	}

	public void setIsPush(int isPush) {
		this.isPush = isPush;
	}
}

package com.ailk.biapp.ci.entity.base;

import com.ailk.biapp.ci.entity.base.EnumValue;
import com.ailk.biapp.ci.entity.base.LabelVerticalColumn;
import java.io.Serializable;
import java.util.List;

public class LabelInfo implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private String labelId;
	private String labelName;
	private Integer updateCycle;
	private String parentId;
	private Integer labelTypeId;
	private String busiLegend;
	private String applySuggest;
	private String busiCaliber;
	private int isCategory;
	private List<EnumValue> enumValueList;
	private List<LabelVerticalColumn> labelVerticalColumnList;
	private String dataDate;

	public List<LabelVerticalColumn> getLabelVerticalColumnList() {
		return this.labelVerticalColumnList;
	}

	public LabelInfo() {
	}

	public String getLabelId() {
		return this.labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getLabelName() {
		return this.labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public Integer getUpdateCycle() {
		return this.updateCycle;
	}

	public void setUpdateCycle(Integer updateCycle) {
		this.updateCycle = updateCycle;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getLabelTypeId() {
		return this.labelTypeId;
	}

	public void setLabelTypeId(Integer labelTypeId) {
		this.labelTypeId = labelTypeId;
	}

	public String getBusiCaliber() {
		return this.busiCaliber;
	}

	public void setBusiCaliber(String busiCaliber) {
		this.busiCaliber = busiCaliber;
	}

	public String getBusiLegend() {
		return this.busiLegend;
	}

	public void setBusiLegend(String busiLegend) {
		this.busiLegend = busiLegend;
	}

	public String getApplySuggest() {
		return this.applySuggest;
	}

	public void setApplySuggest(String applySuggest) {
		this.applySuggest = applySuggest;
	}

	public int getIsCategory() {
		return this.isCategory;
	}

	public void setIsCategory(int isCategory) {
		this.isCategory = isCategory;
	}

	public List<EnumValue> getEnumValueList() {
		return this.enumValueList;
	}

	public void setEnumValueList(List<EnumValue> enumValueList) {
		this.enumValueList = enumValueList;
	}

	public void setLabelVerticalColumnList(List<LabelVerticalColumn> labelVerticalColumnList) {
		this.labelVerticalColumnList = labelVerticalColumnList;
	}

	public String getDataDate() {
		return this.dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
}

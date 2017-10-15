package com.ailk.biapp.ci.model;

import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import java.util.Date;
import java.util.List;

public class CiConfigLabeInfo
{

	private Integer labelId;
	private Integer parentId;
	private Integer labelTypeId;
	private String labelName;
	private String parentName;
	private String createUserId;
	private Integer updateCycle;
	private Date createTime;
	private Date effecTime;
	private Date failTime;
	private String busiCaliber;
	private String createDesc;
	private Integer labelPriority;
	private String deptId;
	private String countRulesCode;
	private String dependIndex;
	private String countRules;
	private String countRulesDesc;
	private Date effectiveTime;
	private Date invalidTime;
	private String createTableType;
	private Integer tableId;
	private String tableName;
	private Integer columnId;
	private String columnName;
	private String columnCnName;
	private String dimTransId;
	private String dataType;
	private Integer isNeedAuthority;
	private Integer labelLevel;
	private DimTableDefine define;
	private List dimTransList;
	private String userId;
	private Integer labelCreateType;
	private String publishDesc;
	private Integer parentIdIsLeaf;
	private List labelSceneList;

	public CiConfigLabeInfo()
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

	public Integer getParentId()
	{
		return parentId;
	}

	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}

	public Integer getLabelTypeId()
	{
		return labelTypeId;
	}

	public void setLabelTypeId(Integer labelTypeId)
	{
		this.labelTypeId = labelTypeId;
	}

	public String getParentName()
	{
		return parentName;
	}

	public void setParentName(String parentName)
	{
		this.parentName = parentName;
	}

	public String getCreateUserId()
	{
		return createUserId;
	}

	public void setCreateUserId(String createUserId)
	{
		this.createUserId = createUserId;
	}

	public Integer getUpdateCycle()
	{
		return updateCycle;
	}

	public void setUpdateCycle(Integer updateCycle)
	{
		this.updateCycle = updateCycle;
	}

	public String getDeptId()
	{
		return deptId;
	}

	public void setDeptId(String deptId)
	{
		this.deptId = deptId;
	}

	public Date getEffecTime()
	{
		return effecTime;
	}

	public void setEffecTime(Date effecTime)
	{
		this.effecTime = effecTime;
	}

	public Date getFailTime()
	{
		return failTime;
	}

	public void setFailTime(Date failTime)
	{
		this.failTime = failTime;
	}

	public String getBusiCaliber()
	{
		return busiCaliber;
	}

	public void setBusiCaliber(String busiCaliber)
	{
		this.busiCaliber = busiCaliber;
	}

	public String getCreateDesc()
	{
		return createDesc;
	}

	public void setCreateDesc(String createDesc)
	{
		this.createDesc = createDesc;
	}

	public String getCountRulesCode()
	{
		return countRulesCode;
	}

	public void setCountRulesCode(String countRulesCode)
	{
		this.countRulesCode = countRulesCode;
	}

	public String getDependIndex()
	{
		return dependIndex;
	}

	public void setDependIndex(String dependIndex)
	{
		this.dependIndex = dependIndex;
	}

	public String getCountRules()
	{
		return countRules;
	}

	public void setCountRules(String countRules)
	{
		this.countRules = countRules;
	}

	public String getCountRulesDesc()
	{
		return countRulesDesc;
	}

	public void setCountRulesDesc(String countRulesDesc)
	{
		this.countRulesDesc = countRulesDesc;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getEffectiveTime()
	{
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime)
	{
		this.effectiveTime = effectiveTime;
	}

	public Date getInvalidTime()
	{
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime)
	{
		this.invalidTime = invalidTime;
	}

	public Integer getTableId()
	{
		return tableId;
	}

	public void setTableId(Integer tableId)
	{
		this.tableId = tableId;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public Integer getColumnId()
	{
		return columnId;
	}

	public void setColumnId(Integer columnId)
	{
		this.columnId = columnId;
	}

	public String getColumnName()
	{
		return columnName;
	}

	public String getLabelName()
	{
		return labelName;
	}

	public void setLabelName(String labelName)
	{
		this.labelName = labelName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	public String getColumnCnName()
	{
		return columnCnName;
	}

	public void setColumnCnName(String columnCnName)
	{
		this.columnCnName = columnCnName;
	}

	public String getDimTransId()
	{
		return dimTransId;
	}

	public void setDimTransId(String dimTransId)
	{
		this.dimTransId = dimTransId;
	}

	public String getPublishDesc()
	{
		return publishDesc;
	}

	public void setPublishDesc(String publishDesc)
	{
		this.publishDesc = publishDesc;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Integer getLabelCreateType()
	{
		return labelCreateType;
	}

	public void setLabelCreateType(Integer labelCreateType)
	{
		this.labelCreateType = labelCreateType;
	}

	public Integer getLabelPriority()
	{
		return labelPriority;
	}

	public void setLabelPriority(Integer labelPriority)
	{
		this.labelPriority = labelPriority;
	}

	public List getDimTransList()
	{
		return dimTransList;
	}

	public void setDimTransList(List dimTransList)
	{
		this.dimTransList = dimTransList;
	}

	public DimTableDefine getDefine()
	{
		return define;
	}

	public void setDefine(DimTableDefine define)
	{
		this.define = define;
	}

	public Integer getLabelLevel()
	{
		return labelLevel;
	}

	public void setLabelLevel(Integer labelLevel)
	{
		this.labelLevel = labelLevel;
	}

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public Integer getIsNeedAuthority()
	{
		return isNeedAuthority;
	}

	public void setIsNeedAuthority(Integer isNeedAuthority)
	{
		this.isNeedAuthority = isNeedAuthority;
	}

	public String getCreateTableType()
	{
		return createTableType;
	}

	public void setCreateTableType(String createTableType)
	{
		this.createTableType = createTableType;
	}

	public Integer getParentIdIsLeaf()
	{
		return parentIdIsLeaf;
	}

	public void setParentIdIsLeaf(Integer parentIdIsLeaf)
	{
		this.parentIdIsLeaf = parentIdIsLeaf;
	}

	public List getLabelSceneList()
	{
		return labelSceneList;
	}

	public void setLabelSceneList(List labelSceneList)
	{
		this.labelSceneList = labelSceneList;
	}
}

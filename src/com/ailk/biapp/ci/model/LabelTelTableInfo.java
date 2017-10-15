package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class LabelTelTableInfo
	implements Serializable
{

	private static final long serialVersionUID = 0x9adfd4fbb944fba8L;
	private String table_name;
	private String column_name;
	private int update_cycle;
	private int label_id;
	private String label_name;
	private String busi_caliber;
	private String type_name;
	private int type_id;
	private String type_desc;
	private String icon;

	public String getTable_name()
	{
		return table_name;
	}

	public void setTable_name(String tableName)
	{
		table_name = tableName;
	}

	public String getColumn_name()
	{
		return column_name;
	}

	public void setColumn_name(String columnName)
	{
		column_name = columnName;
	}

	public int getUpdate_cycle()
	{
		return update_cycle;
	}

	public void setUpdate_cycle(int updateCycle)
	{
		update_cycle = updateCycle;
	}

	public int getLabel_id()
	{
		return label_id;
	}

	public void setLabel_id(int labelId)
	{
		label_id = labelId;
	}

	public String getLabel_name()
	{
		return label_name;
	}

	public void setLabel_name(String labelName)
	{
		label_name = labelName;
	}

	public String getBusi_caliber()
	{
		return busi_caliber;
	}

	public void setBusi_caliber(String busiCaliber)
	{
		busi_caliber = busiCaliber;
	}

	public String getType_name()
	{
		return type_name;
	}

	public void setType_name(String typeName)
	{
		type_name = typeName;
	}

	public int getType_id()
	{
		return type_id;
	}

	public void setType_id(int typeId)
	{
		type_id = typeId;
	}

	public String getType_desc()
	{
		return type_desc;
	}

	public void setType_desc(String typeDesc)
	{
		type_desc = typeDesc;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public LabelTelTableInfo()
	{
	}

	public LabelTelTableInfo(String tableName, String columnName, int updateCycle, int labelId, String labelName, String busiCaliber, String typeName, 
			int typeId, String typeDesc, String icon)
	{
		table_name = tableName;
		column_name = columnName;
		update_cycle = updateCycle;
		label_id = labelId;
		label_name = labelName;
		busi_caliber = busiCaliber;
		type_name = typeName;
		type_id = typeId;
		type_desc = typeDesc;
		this.icon = icon;
	}

	public String toString()
	{
		return (new StringBuilder()).append("LabelTelTableInfo [busi_caliber=").append(busi_caliber).append(", column_name=").append(column_name).append(", icon=").append(icon).append(", label_id=").append(label_id).append(", label_name=").append(label_name).append(", table_name=").append(table_name).append(", type_desc=").append(type_desc).append(", type_id=").append(type_id).append(", type_name=").append(type_name).append(", update_cycle=").append(update_cycle).append("]").toString();
	}
}

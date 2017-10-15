package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiMdaSysTable
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;
  private Integer tableId;
  private String tableName;
  private String tableCnName;
  private String tableDesc;
  private String tablePostfix;
  private String tableSchema;
  private Integer tableType;
  private Integer updateCycle;
  private String targetTableCode;

  public CiMdaSysTable()
  {
  }

  protected CiMdaSysTable clone()
    throws CloneNotSupportedException
  {
    return ((CiMdaSysTable)super.clone());
  }

  public CiMdaSysTable(String tableName, String tableCnName)
  {
    this.tableName = tableName;
    this.tableCnName = tableCnName;
  }

  public CiMdaSysTable(String tableName, String tableCnName, String tableDesc, String tablePostfix, String tableSchema)
  {
    this.tableName = tableName;
    this.tableCnName = tableCnName;
    this.tableDesc = tableDesc;
    this.tablePostfix = tablePostfix;
    this.tableSchema = tableSchema;
  }

  public Integer getTableId()
  {
    return this.tableId;
  }

  public void setTableId(Integer tableId) {
    this.tableId = tableId;
  }

  public String getTableName() {
    return this.tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getTableCnName() {
    return this.tableCnName;
  }

  public void setTableCnName(String tableCnName) {
    this.tableCnName = tableCnName;
  }

  public String getTableDesc() {
    return this.tableDesc;
  }

  public void setTableDesc(String tableDesc) {
    this.tableDesc = tableDesc;
  }

  public String getTablePostfix() {
    return this.tablePostfix;
  }

  public void setTablePostfix(String tablePostfix) {
    this.tablePostfix = tablePostfix;
  }

  public String getTableSchema() {
    return this.tableSchema;
  }

  public void setTableSchema(String tableSchema) {
    this.tableSchema = tableSchema;
  }

  public Integer getTableType() {
    return this.tableType;
  }

  public void setTableType(Integer tableType) {
    this.tableType = tableType;
  }

  public Integer getUpdateCycle() {
    return this.updateCycle;
  }

  public void setUpdateCycle(Integer updateCycle) {
    this.updateCycle = updateCycle;
  }

  public String getTargetTableCode() {
    return this.targetTableCode;
  }

  public void setTargetTableCode(String targetTableCode) {
    this.targetTableCode = targetTableCode;
  }

  public String toString()
  {
    return "CiMdaSysTable [tableId=" + this.tableId + ", tableName=" + this.tableName + ", tableCnName=" + this.tableCnName + ", tableDesc=" + this.tableDesc + ", tablePostfix=" + this.tablePostfix + ", tableSchema=" + this.tableSchema + "]";
  }
}
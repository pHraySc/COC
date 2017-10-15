package com.ailk.biapp.ci.entity;

import java.io.Serializable;

public class CiMdaSysTableColumn
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;
  private Integer columnId;
  private String columnName;
  private String columnCnName;
  private Integer columnDataTypeId;
  private Long columnDataLength;
  private Long columnDataPrecision;
  private Integer isColumnValueBeNull;
  private Integer isPrimaryKey;
  private Integer isForeignKey;
  private String columnDesc;
  private String dimTransId;
  private String unit;
  private Integer isNeedAuthority;
  private String dataType;
  private Integer vertLabelTypeId;
  private Integer isMustColumn;
  private CiMdaSysTable ciMdaSysTable;

  protected CiMdaSysTableColumn clone()
    throws CloneNotSupportedException
  {
    CiMdaSysTableColumn ciMdaSysTableColumn = (CiMdaSysTableColumn)super.clone();
    if (null != this.ciMdaSysTable)
      ciMdaSysTableColumn.setCiMdaSysTable(this.ciMdaSysTable.clone());

    return ciMdaSysTableColumn;
  }

  public Integer getColumnId()
  {
    return this.columnId;
  }

  public void setColumnId(Integer columnId) {
    this.columnId = columnId;
  }

  public String getColumnName() {
    return this.columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public String getColumnCnName() {
    return this.columnCnName;
  }

  public void setColumnCnName(String columnCnName) {
    this.columnCnName = columnCnName;
  }

  public Integer getColumnDataTypeId() {
    return this.columnDataTypeId;
  }

  public void setColumnDataTypeId(Integer columnDataTypeId) {
    this.columnDataTypeId = columnDataTypeId;
  }

  public Long getColumnDataLength() {
    return this.columnDataLength;
  }

  public void setColumnDataLength(Long columnDataLength) {
    this.columnDataLength = columnDataLength;
  }

  public Long getColumnDataPrecision() {
    return this.columnDataPrecision;
  }

  public void setColumnDataPrecision(Long columnDataPrecision) {
    this.columnDataPrecision = columnDataPrecision;
  }

  public Integer getIsColumnValueBeNull() {
    return this.isColumnValueBeNull;
  }

  public void setIsColumnValueBeNull(Integer isColumnValueBeNull) {
    this.isColumnValueBeNull = isColumnValueBeNull;
  }

  public Integer getIsPrimaryKey() {
    return this.isPrimaryKey;
  }

  public void setIsPrimaryKey(Integer isPrimaryKey) {
    this.isPrimaryKey = isPrimaryKey;
  }

  public Integer getIsForeignKey() {
    return this.isForeignKey;
  }

  public void setIsForeignKey(Integer isForeignKey) {
    this.isForeignKey = isForeignKey;
  }

  public String getColumnDesc() {
    return this.columnDesc;
  }

  public void setColumnDesc(String columnDesc) {
    this.columnDesc = columnDesc;
  }

  public String getDimTransId() {
    return this.dimTransId;
  }

  public void setDimTransId(String dimTransId) {
    this.dimTransId = dimTransId;
  }

  public CiMdaSysTable getCiMdaSysTable() {
    return this.ciMdaSysTable;
  }

  public void setCiMdaSysTable(CiMdaSysTable ciMdaSysTable) {
    this.ciMdaSysTable = ciMdaSysTable;
  }

  public String getUnit() {
    return this.unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public Integer getIsNeedAuthority() {
    return this.isNeedAuthority;
  }

  public void setIsNeedAuthority(Integer isNeedAuthority) {
    this.isNeedAuthority = isNeedAuthority;
  }

  public String getDataType() {
    return this.dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String toString()
  {
    return "CiMdaSysTableColumn [columnId=" + this.columnId + ", columnName=" + this.columnName + ", columnCnName=" + this.columnCnName + ", columnDataTypeId=" + this.columnDataTypeId + ", columnDataLength=" + this.columnDataLength + ", columnDataPrecision=" + this.columnDataPrecision + ", isColumnValueBeNull=" + this.isColumnValueBeNull + ", isPrimaryKey=" + this.isPrimaryKey + ", isForeignKey=" + this.isForeignKey + ", columnDesc=" + this.columnDesc + ", dimTransId=" + this.dimTransId + ", ciMdaSysTable=" + this.ciMdaSysTable + "]";
  }

  public Integer getIsMustColumn()
  {
    return this.isMustColumn;
  }

  public void setIsMustColumn(Integer isMustColumn) {
    this.isMustColumn = isMustColumn;
  }

  public Integer getVertLabelTypeId() {
    return this.vertLabelTypeId;
  }

  public void setVertLabelTypeId(Integer vertLabelTypeId) {
    this.vertLabelTypeId = vertLabelTypeId;
  }
}
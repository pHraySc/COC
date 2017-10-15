package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class Column implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ColumnName;
    private String ColumnType;
    private String ColumnLength;

    public Column() {
    }

    public String getColumnName() {
        return this.ColumnName;
    }

    public void setColumnName(String columnName) {
        this.ColumnName = columnName;
    }

    public String getColumnType() {
        return this.ColumnType;
    }

    public void setColumnType(String columnType) {
        this.ColumnType = columnType;
    }

    public String getColumnLength() {
        return this.ColumnLength;
    }

    public void setColumnLength(String columnLength) {
        this.ColumnLength = columnLength;
    }

    public String toString() {
        return "Column [ColumnName=" + this.ColumnName + ", ColumnType=" + this.ColumnType + ", ColumnLength=" + this.ColumnLength + "]";
    }
}

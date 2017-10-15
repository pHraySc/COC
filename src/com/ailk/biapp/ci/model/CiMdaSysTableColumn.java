package com.ailk.biapp.ci.model;

public class CiMdaSysTableColumn {
    private String columnName;
    private String tableName;
    private Integer updateCycle;

    public CiMdaSysTableColumn() {
    }

    public Integer getUpdateCycle() {
        return this.updateCycle;
    }

    public void setUpdateCycle(Integer updateCycle) {
        this.updateCycle = updateCycle;
    }

    public String toString() {
        return "CiMdaSysTableColumn [columnName=" + this.columnName + ", tableName=" + this.tableName + ", updateCycle=" + this.updateCycle + "]";
    }

    public int hashCode() {
        boolean prime = true;
        byte result = 1;
        int result1 = 31 * result + (this.columnName == null?0:this.columnName.hashCode());
        result1 = 31 * result1 + (this.tableName == null?0:this.tableName.hashCode());
        result1 = 31 * result1 + (this.updateCycle == null?0:this.updateCycle.hashCode());
        return result1;
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null) {
            return false;
        } else if(this.getClass() != obj.getClass()) {
            return false;
        } else {
            CiMdaSysTableColumn other = (CiMdaSysTableColumn)obj;
            if(this.columnName == null) {
                if(other.columnName != null) {
                    return false;
                }
            } else if(!this.columnName.equals(other.columnName)) {
                return false;
            }

            if(this.tableName == null) {
                if(other.tableName != null) {
                    return false;
                }
            } else if(!this.tableName.equals(other.tableName)) {
                return false;
            }

            if(this.updateCycle == null) {
                if(other.updateCycle != null) {
                    return false;
                }
            } else if(!this.updateCycle.equals(other.updateCycle)) {
                return false;
            }

            return true;
        }
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

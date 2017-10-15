package com.ailk.biapp.ci.market.entity;

import java.io.Serializable;
import java.util.List;

public class CiMarketTask implements Serializable {
    private static final long serialVersionUID = 1L;
    private String marketTaskId;
    private String marketTaskName;
    private String marketTaskDesc;
    private String parentId;
    private Integer sortNum;
    private String iconName;
    private List<CiMarketTask> childCiMarketTaskList;
    private String childrenJson;

    public CiMarketTask() {
    }

    public CiMarketTask(String marketTaskId, String marketTaskName, String marketTaskDesc, String parentId, Integer sortNum, String iconName) {
        this.marketTaskId = marketTaskId;
        this.marketTaskName = marketTaskName;
        this.marketTaskDesc = marketTaskDesc;
        this.parentId = parentId;
        this.sortNum = sortNum;
        this.iconName = iconName;
    }

    public String getMarketTaskId() {
        return this.marketTaskId;
    }

    public void setMarketTaskId(String marketTaskId) {
        this.marketTaskId = marketTaskId;
    }

    public String getMarketTaskName() {
        return this.marketTaskName;
    }

    public void setMarketTaskName(String marketTaskName) {
        this.marketTaskName = marketTaskName;
    }

    public String getMarketTaskDesc() {
        return this.marketTaskDesc;
    }

    public void setMarketTaskDesc(String marketTaskDesc) {
        this.marketTaskDesc = marketTaskDesc;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getSortNum() {
        return this.sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getIconName() {
        return this.iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public List<CiMarketTask> getChildCiMarketTaskList() {
        return this.childCiMarketTaskList;
    }

    public void setChildCiMarketTaskList(List<CiMarketTask> childCiMarketTaskList) {
        this.childCiMarketTaskList = childCiMarketTaskList;
    }

    public String getChildrenJson() {
        return this.childrenJson;
    }

    public void setChildrenJson(String childrenJson) {
        this.childrenJson = childrenJson;
    }
}

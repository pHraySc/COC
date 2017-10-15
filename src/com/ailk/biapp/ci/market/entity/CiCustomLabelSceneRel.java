package com.ailk.biapp.ci.market.entity;

import java.io.Serializable;

public class CiCustomLabelSceneRel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer primaryKeyId;
    private String sceneId;
    private String marketTaskId;
    private Integer labelId;
    private String customGroupId;
    private String labelCustomName;
    private Double avgScore;
    private String newestTime;
    private Integer useTimes;
    private Integer sortNum;
    private Integer showType;
    private Integer status;
    private String sceneName;
    private Integer updateCycle;
    private String dataDate;
    private String marketTaskName;

    public CiCustomLabelSceneRel() {
    }

    public CiCustomLabelSceneRel(String sceneId, String marketTaskId, Integer labelId, String customGroupId, String labelCustomName, Double avgScore, String newestTime, Integer useTimes, Integer sortNum, Integer showType) {
        this.sceneId = sceneId;
        this.marketTaskId = marketTaskId;
        this.labelId = labelId;
        this.customGroupId = customGroupId;
        this.labelCustomName = labelCustomName;
        this.avgScore = avgScore;
        this.newestTime = newestTime;
        this.useTimes = useTimes;
        this.sortNum = sortNum;
        this.showType = showType;
    }

    public Integer getPrimaryKeyId() {
        return this.primaryKeyId;
    }

    public void setPrimaryKeyId(Integer primaryKeyId) {
        this.primaryKeyId = primaryKeyId;
    }

    public String getSceneId() {
        return this.sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getLabelId() {
        return this.labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public String getCustomGroupId() {
        return this.customGroupId;
    }

    public void setCustomGroupId(String customGroupId) {
        this.customGroupId = customGroupId;
    }

    public Double getAvgScore() {
        return this.avgScore;
    }

    public void setAvgScore(Double avgScore) {
        this.avgScore = avgScore;
    }

    public String getNewestTime() {
        return this.newestTime;
    }

    public void setNewestTime(String newestTime) {
        this.newestTime = newestTime;
    }

    public Integer getUseTimes() {
        return this.useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }

    public Integer getSortNum() {
        return this.sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getShowType() {
        return this.showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public String getMarketTaskId() {
        return this.marketTaskId;
    }

    public void setMarketTaskId(String marketTaskId) {
        this.marketTaskId = marketTaskId;
    }

    public String getLabelCustomName() {
        return this.labelCustomName;
    }

    public void setLabelCustomName(String labelCustomName) {
        this.labelCustomName = labelCustomName;
    }

    public String getSceneName() {
        return this.sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getMarketTaskName() {
        return this.marketTaskName;
    }

    public void setMarketTaskName(String marketTaskName) {
        this.marketTaskName = marketTaskName;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUpdateCycle() {
        return this.updateCycle;
    }

    public void setUpdateCycle(Integer updateCycle) {
        this.updateCycle = updateCycle;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }
}

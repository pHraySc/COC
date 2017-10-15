package com.ailk.biapp.ci.market.entity;

import java.io.Serializable;

public class CiMarketScene implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sceneId;
    private String parentId;
    private String sceneName;
    private String sceneDesc;
    private Integer sortNum;
    private String defaultOp;
    private String iconCls;

    public CiMarketScene() {
    }

    public CiMarketScene(String parentId, String sceneName, String sceneDesc, Integer sortNum, String defaultOp) {
        this.parentId = parentId;
        this.sceneName = sceneName;
        this.sceneDesc = sceneDesc;
        this.sortNum = sortNum;
        this.defaultOp = defaultOp;
    }

    public String getSceneId() {
        return this.sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSceneName() {
        return this.sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneDesc() {
        return this.sceneDesc;
    }

    public void setSceneDesc(String sceneDesc) {
        this.sceneDesc = sceneDesc;
    }

    public Integer getSortNum() {
        return this.sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getIconCls() {
        return this.iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getDefaultOp() {
        return this.defaultOp;
    }

    public void setDefaultOp(String defaultOp) {
        this.defaultOp = defaultOp;
    }
}

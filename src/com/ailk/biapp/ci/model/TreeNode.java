package com.ailk.biapp.ci.model;

import java.io.Serializable;

public class TreeNode implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String pId;
    private Boolean isParent;
    private String name;
    private Boolean open;
    private String tip;
    private String icon;
    private Boolean nocheck;
    private Boolean click;
    private Object param;

    public TreeNode() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return this.pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public Boolean getIsParent() {
        return this.isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOpen() {
        return this.open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public String getTip() {
        return this.tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getNocheck() {
        return this.nocheck;
    }

    public void setNocheck(Boolean nocheck) {
        this.nocheck = nocheck;
    }

    public Boolean getClick() {
        return this.click;
    }

    public void setClick(Boolean click) {
        this.click = click;
    }

    public Object getParam() {
        return this.param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public String toString() {
        return "TreeNode [id=" + this.id + ", pId=" + this.pId + ", isParent=" + this.isParent + ", name=" + this.name + ", open=" + this.open + ", tip=" + this.tip + ", icon=" + this.icon + ", nocheck=" + this.nocheck + ", click=" + this.click + ", param=" + this.param + "]";
    }
}

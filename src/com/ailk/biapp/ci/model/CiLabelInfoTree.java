package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.entity.CiLabelInfo;
import java.util.List;

public class CiLabelInfoTree {
    public CiLabelInfo ciLabelInfo;
    public List<CiLabelInfoTree> ciLabelInfoTree;
    public Boolean isLeaf = Boolean.valueOf(false);
    public int depth;

    public CiLabelInfoTree() {
    }

    public Boolean getIsLeaf() {
        return this.isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public CiLabelInfo getCiLabelInfo() {
        return this.ciLabelInfo;
    }

    public void setCiLabelInfo(CiLabelInfo ciLabelInfo) {
        this.ciLabelInfo = ciLabelInfo;
    }

    public List<CiLabelInfoTree> getCiLabelInfoTree() {
        return this.ciLabelInfoTree;
    }

    public void setCiLabelInfoTree(List<CiLabelInfoTree> ciLabelInfoTree) {
        this.ciLabelInfoTree = ciLabelInfoTree;
    }
}

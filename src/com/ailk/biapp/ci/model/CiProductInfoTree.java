package com.ailk.biapp.ci.model;

import com.ailk.biapp.ci.entity.CiProductInfo;
import java.util.List;

public class CiProductInfoTree {
    public CiProductInfo ciProductInfo;
    public List<CiProductInfoTree> ciProductInfoTree;
    public Boolean isLeaf = Boolean.valueOf(false);
    public int depth;

    public CiProductInfoTree() {
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

    public CiProductInfo getCiProductInfo() {
        return this.ciProductInfo;
    }

    public void setCiProductInfo(CiProductInfo ciProductInfo) {
        this.ciProductInfo = ciProductInfo;
    }

    public List<CiProductInfoTree> getCiProductInfoTree() {
        return this.ciProductInfoTree;
    }

    public void setCiProductInfoTree(List<CiProductInfoTree> ciProductInfoTree) {
        this.ciProductInfoTree = ciProductInfoTree;
    }
}

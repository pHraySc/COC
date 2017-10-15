package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.VCiLabelCustomList;
import com.ailk.biapp.ci.model.Pager;
import java.util.List;

public interface IVCiLabelCustomListJDao {
    List<VCiLabelCustomList> selectLabelCustomAttentionList(String var1, Pager var2);
}

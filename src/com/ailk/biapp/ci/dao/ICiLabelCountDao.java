package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CilabelCount;
import com.ailk.biapp.ci.model.Pager;

import java.util.List;

/**
 * Created by admin on 2017/4/24.
 */
public interface ICiLabelCountDao {
    List<CilabelCount> getLabelCount(Pager pager);

    int getLabelCountNum();
}

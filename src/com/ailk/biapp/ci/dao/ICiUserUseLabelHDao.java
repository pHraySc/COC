package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiUserUseLabel;
import java.util.List;

public interface ICiUserUseLabelHDao {
    CiUserUseLabel selectById(String var1);

    List<CiUserUseLabel> select();

    void insertOrUpdateCiUserUseLabel(CiUserUseLabel var1);
}

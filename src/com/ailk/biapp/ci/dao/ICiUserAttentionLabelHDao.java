package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiUserAttentionLabel;
import com.ailk.biapp.ci.entity.CiUserAttentionLabelId;
import java.util.List;

public interface ICiUserAttentionLabelHDao {
    CiUserAttentionLabel selectById(CiUserAttentionLabelId var1);

    List<CiUserAttentionLabel> select();

    void insertOrUpdateCiUserAttentionLabel(CiUserAttentionLabel var1);

    void deleteCiUserAttentionLabel(CiUserAttentionLabel var1);
}

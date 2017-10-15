package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiUserAttentionCustom;
import com.ailk.biapp.ci.entity.CiUserAttentionCustomId;
import java.util.List;

public interface ICiUserAttentionCustomHDao {
    CiUserAttentionCustom selectById(CiUserAttentionCustomId var1);

    List<CiUserAttentionCustom> select();

    void insertOrUpdateCiUserAttentionCustom(CiUserAttentionCustom var1);

    void deleteCiUserAttentionCustom(CiUserAttentionCustom var1);
}

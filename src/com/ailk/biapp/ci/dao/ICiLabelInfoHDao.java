package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiLabelInfo;
import java.util.List;

public interface ICiLabelInfoHDao {
    void insertCiLabelInfo(CiLabelInfo var1);

    void updateCiLabelInfo(CiLabelInfo var1);

    CiLabelInfo selectCiLabelInfoById(Integer var1);

    void delete(List<Integer> var1);

    List<CiLabelInfo> selectEffectiveCiLabelInfo();

    List<CiLabelInfo> selectEffectiveCiLabelInfoByParentId(Integer var1);

    List<CiLabelInfo> selectEffectiveFirstLevelLabel();

    List<CiLabelInfo> selectToEffectChildrenLabelsById(Integer var1);
}

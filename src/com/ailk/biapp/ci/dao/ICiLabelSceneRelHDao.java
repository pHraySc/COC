package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiLabelSceneRel;
import java.util.List;

public interface ICiLabelSceneRelHDao {
    void insertCiLabelSceneRel(CiLabelSceneRel var1);

    void deleteLabelSceneByLabelId(String var1);

    List<CiLabelSceneRel> selectLabelSceneByLabelId(String var1);
}

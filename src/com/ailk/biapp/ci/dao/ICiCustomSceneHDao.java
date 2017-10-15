package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiCustomSceneRel;
import java.util.List;

public interface ICiCustomSceneHDao {
    List<CiCustomSceneRel> getCustomScenesByCustomId(String var1);

    void insertCiCustomSceneRel(CiCustomSceneRel var1);
}

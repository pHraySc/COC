package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiCustomSceneRel;
import java.util.List;

public interface ICiCustomSceneService {
    List<CiCustomSceneRel> queryCustomScenesListByCustomId(String var1);
}

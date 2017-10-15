package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiExternalSysLabelRel;
import java.util.List;

public interface ICiExternalLabelHDao {
    List<CiExternalSysLabelRel> selectExternalLabelRelList() throws Exception;
}

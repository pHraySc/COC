package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiTagInfo;
import com.ailk.biapp.ci.model.CiTagTypeModel;
import java.util.List;
import java.util.Map;

public interface ICiTagInfoJDao {
    Integer selectPhoneNumExists(String var1);

    Map<String, Object> selectProductNoTag(String var1);

    List<CiTagInfo> selectAllTag();

    List<CiTagTypeModel> selectAllTagType();
}

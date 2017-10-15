package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiTemplateInfo;
import java.util.List;

public interface ICiTemplateInfoHDao {
    CiTemplateInfo selectTemplateInfoById(String var1);

    void deleteTemplateInfo(CiTemplateInfo var1);

    void insertTemplateInfo(CiTemplateInfo var1);

    List<CiTemplateInfo> selectTemplateInfoListByName(String var1, String var2);

    List<CiTemplateInfo> selectTemplateInfoListByTemplateName(String var1);
}

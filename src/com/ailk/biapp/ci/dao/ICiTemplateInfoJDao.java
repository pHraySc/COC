package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.DimScene;
import com.ailk.biapp.ci.model.Pager;
import java.util.List;

public interface ICiTemplateInfoJDao {
    int selectTemplateInfoCount(CiTemplateInfo var1) throws Exception;

    List<CiTemplateInfo> selectTemplateInfoList(Pager var1, CiTemplateInfo var2) throws Exception;

    List<CiTemplateInfo> selectTemplateInfoList(String var1) throws Exception;

    List<CiTemplateInfo> selectTemplateInfoList(String var1, String var2) throws Exception;

    DimScene selectSceneInfoById(String var1);

    List<CiTemplateInfo> indexQueryTemplateName(String var1, String var2) throws Exception;
}

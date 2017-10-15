package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiTemplateInfo;
import com.ailk.biapp.ci.entity.DimScene;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.Pager;
import java.util.List;
import java.util.Map;

public interface ICiTemplateInfoService {
    int queryTemplateInfoCount(CiTemplateInfo var1) throws CIServiceException;

    List<CiTemplateInfo> queryTemplateInfoList(Pager var1, CiTemplateInfo var2) throws CIServiceException;

    CiTemplateInfo queryTemplateInfoById(String var1) throws CIServiceException;

    void addTemplateInfo(CiTemplateInfo var1, List<CiLabelRule> var2, String var3) throws CIServiceException;

    boolean deleteTemplateInfo(CiTemplateInfo var1) throws CIServiceException;

    void modifyTemplateInfo(CiTemplateInfo var1, List<CiLabelRule> var2, String var3) throws CIServiceException;

    List<CiTemplateInfo> queryTemplateInfoListByName(String var1, String var2) throws CIServiceException;

    Map<String, Object> isNameExist(CiTemplateInfo var1, String var2) throws CIServiceException;

    List<CiTemplateInfo> queryTemplateInfoListByTemplateName(String var1) throws CIServiceException;

    void modifyTemplateInfoPublic(CiTemplateInfo var1, String var2) throws CIServiceException;

    DimScene querySceneInfoById(String var1);

    List<CiTemplateInfo> indexQueryTemplateName(String var1, String var2);
}

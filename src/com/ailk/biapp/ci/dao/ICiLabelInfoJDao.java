package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.DimCocLabelStatus;
import com.ailk.biapp.ci.model.Pager;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import java.util.List;
import java.util.Map;

public interface ICiLabelInfoJDao {
    int getCountBySql(CiLabelInfo var1, String var2) throws Exception;

    List<CiLabelInfo> getPageListBySql(int var1, int var2, CiLabelInfo var3, String var4) throws Exception;

    CiLabelInfo selectCiLabelInfoById(Integer var1);

    List<CiLabelInfo> selectSimilarityLabelNameList(CiLabelInfo var1);

    String selectDimTransIdByLabelId(Integer var1);

    List<Map<String, Object>> getAllDimDataByDimTableDefine(DimTableDefine var1, int var2, int var3, String var4, String var5);

    List<Map<String, Object>> getAllDimDataByDimTableDefine(DimTableDefine var1);

    long getAllDimDataCountByDimTableDefine(DimTableDefine var1, String var2, String var3);

    List<Map<String, Object>> selectDimValueByImport(String var1, DimTableDefine var2, String var3) throws Exception;

    int updateCiLabelInfoDataDate(List<DimCocLabelStatus> var1, String var2);

    List<CiLabelInfo> selectVertAndEnumLabelList(Pager var1, CiLabelInfo var2);

    int selectVertAndEnumLabelCount(CiLabelInfo var1);

    int getLabelInfoListCountBySql(CiLabelInfo var1, String var2) throws Exception;

    List<CiLabelInfo> getPageLabelInfoListBySql(int var1, int var2, CiLabelInfo var3, String var4) throws Exception;

    List<CiLabelInfo> selectEffectAndPublAndNoEffectLabel() throws Exception;

    CiLabelInfo selectCiLabelInfoByLabelId(Integer var1);

    List<Map<String, Object>> findLabelAllEnumValue(String var1, String var2);

    List<CiLabelInfo> findRecommendLabelInfoList();

    List<CiLabelInfo> selectEffectLabel() throws Exception;

    List<Map<String, Object>> getAllDimDataByDefine(DimTableDefine var1, String var2);
}

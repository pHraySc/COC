package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.model.CiLabelRelModel;
import java.util.List;

public interface ICiLabelRelAnalysisJDao {
    void selectMainLabelUserNum(CiLabelRelModel var1) throws Exception;

    List<CiLabelRelModel> selectRelLabelByMainLabel(String var1, CiLabelRelModel var2) throws Exception;

    Long getCustomNumByLabelId(Integer var1, String var2) throws Exception;

    String[] getColumnAndTableNameByLabelId(Integer var1) throws Exception;

    void deleteUserLabelRel(String var1, Integer var2, String var3);
}

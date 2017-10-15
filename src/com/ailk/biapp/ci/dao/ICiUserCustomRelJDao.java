package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.model.CiCustomRelModel;
import java.util.List;

public interface ICiUserCustomRelJDao {
    List<CiCustomRelModel> selectRelCustom(String var1, CiCustomRelModel var2) throws Exception;

    void deleteRelCustom(String var1, String var2, String var3) throws Exception;
}

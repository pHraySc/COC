package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiVerifyMm;
import java.util.List;

public interface ICiProductVerifyJDao {
    List<CiVerifyMm> selectVerifyMmList(String var1) throws Exception;
}

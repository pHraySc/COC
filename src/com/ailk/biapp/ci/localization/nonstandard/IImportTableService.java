package com.ailk.biapp.ci.localization.nonstandard;

import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface IImportTableService {
    boolean importTable(CiCustomGroupInfo var1, String var2, String var3, List<CiGroupAttrRel> var4, String var5) throws CIServiceException;

    boolean deleteCustomGroup(CiCustomGroupInfo var1) throws CIServiceException;
}

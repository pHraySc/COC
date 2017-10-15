package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.Date;
import java.util.List;

public interface ICiGroupAttrRelService {
    void saveCiGroupAttrRel(CiGroupAttrRel var1) throws CIServiceException;

    List<CiGroupAttrRel> queryGroupAttrRelList(String var1, Date var2) throws CIServiceException;

    List<CiGroupAttrRel> queryNewestGroupAttrRelList(String var1) throws CIServiceException;

    List<CiGroupAttrRel> queryGroupAttrRelListBySourceId(String var1, String var2) throws CIServiceException;

    void deleteGroupAttrRelListBySourceId(String var1) throws CIServiceException;

    void modifyAllGroupAttrRel(List<CiGroupAttrRel> var1, String var2) throws CIServiceException;

    void updateCiGroupAttrRelListModifyTime(String var1, Date var2) throws Exception;

    void updateCiGroupAttrRelStatusByGroupInfoId(String var1) throws Exception;

    void deleteGroupAttrRelList(String var1, Date var2);

    List<CiGroupAttrRel> queryNewestGroupAttrRelListBySort(String var1);
}

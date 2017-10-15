package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiPersonNotice;
import com.ailk.biapp.ci.exception.CIServiceException;
import java.util.List;

public interface ICiPersonNoticeService {
    void addPersonNotice(CiPersonNotice var1) throws CIServiceException;

    void readPersonNotice(String var1) throws CIServiceException;

    void delete(List<String> var1) throws CIServiceException;

    int getCountOfPersonNotice(CiPersonNotice var1) throws CIServiceException;

    List<CiPersonNotice> queryPagePersonNoticeList(int var1, int var2, CiPersonNotice var3) throws CIServiceException;

    List<CiPersonNotice> queryPersonNoticeList(CiPersonNotice var1) throws CIServiceException;

    void modifyShowTipStatus(String var1) throws CIServiceException;

    void delete(String var1) throws CIServiceException;

    void readPersonNoticeAll(CiPersonNotice var1) throws CIServiceException;

    void deleteAll(CiPersonNotice var1) throws CIServiceException;
}

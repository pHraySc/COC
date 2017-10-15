package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiPersonNotice;
import java.util.List;

public interface ICiPersonNoticeJDao {
    int getCountOfPersonNotice(CiPersonNotice var1) throws Exception;

    List<CiPersonNotice> queryPagePersonNoticeList(int var1, int var2, CiPersonNotice var3) throws Exception;

    List<CiPersonNotice> queryPersonNoticeList(CiPersonNotice var1) throws Exception;
}

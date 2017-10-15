package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiPersonNotice;
import java.util.List;

public interface ICiPersonNoticeHDao {
    void save(CiPersonNotice var1);

    void update(CiPersonNotice var1);

    List<CiPersonNotice> selectByExample(CiPersonNotice var1);

    CiPersonNotice selectById(String var1);

    void updateCiPersonNoticeListRead(CiPersonNotice var1);

    void updateCiPersonNoticeListDelete(CiPersonNotice var1);
}

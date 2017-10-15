package com.ailk.biapp.ci.service;

import com.ailk.biapp.ci.entity.CiDataSource;
import com.ailk.biapp.ci.model.Pager;

import java.text.ParseException;
import java.util.List;

/**
 * Created by admin on 2017/4/24.
 */
public interface ICiDataSourceService {
    List<CiDataSource> getDataSource(Pager pager);

    int getDataCount();

    int getDataCountByDataSrcCode(CiDataSource dataSource);

    String getTheNewestDataDate(String labelId) throws ParseException;

    List selectInfluencedLabelByDataSrcCode(Pager pager, CiDataSource dataSource);

    List getDataSourceIncludeDate();
}

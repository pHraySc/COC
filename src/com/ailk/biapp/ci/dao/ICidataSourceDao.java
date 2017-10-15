package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiDataSource;
import com.ailk.biapp.ci.model.Pager;

import java.util.List;

/**
 * Created by admin on 2017/4/24.
 */
public interface ICidataSourceDao {
    List<CiDataSource> getDataSource(Pager pager);

    int getDataCount();

    int getDataCountByDataSrcCode(CiDataSource dataSource);

    String getTheNewestDataDate(String labelId);

    List selectInfluencedLabelByDataSrcCode(Pager pager, CiDataSource dataSource);

    List getDataSourceIncludeDate();

}

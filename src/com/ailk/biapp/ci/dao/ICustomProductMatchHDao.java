package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CustomProductMatch;
import java.util.List;

public interface ICustomProductMatchHDao {
    List<CustomProductMatch> selectByListTableName(String var1);

    void insertOrUpdate(CustomProductMatch var1);

    void delete(CustomProductMatch var1);

    void deleteByListTableName(String var1);

    List<CustomProductMatch> selectByCustomProductMatch(CustomProductMatch var1);
}

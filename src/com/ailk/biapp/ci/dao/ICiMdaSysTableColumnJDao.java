package com.ailk.biapp.ci.dao;

import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import java.util.List;

public interface ICiMdaSysTableColumnJDao {
    List<CiMdaSysTableColumn> selectCiMdaSysTableColumnList(Integer var1) throws Exception;
}

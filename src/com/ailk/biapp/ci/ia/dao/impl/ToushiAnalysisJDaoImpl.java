package com.ailk.biapp.ci.ia.dao.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.ia.dao.IToushiAnalysisJDao;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ToushiAnalysisJDaoImpl extends JdbcBaseDao implements IToushiAnalysisJDao {
    public ToushiAnalysisJDaoImpl() {
    }

    public List<Map<String, Object>> getData(String sql) {
        List dataList = this.getBackSimpleJdbcTemplate().queryForList(sql, new Object[0]);
        return dataList;
    }

    public List<Map<String, Object>> getData(String sql, Object[] params) {
        List dataList = this.getBackSimpleJdbcTemplate().queryForList(sql, params);
        return dataList;
    }
}

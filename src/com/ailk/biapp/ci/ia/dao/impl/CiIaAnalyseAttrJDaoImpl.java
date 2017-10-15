package com.ailk.biapp.ci.ia.dao.impl;

import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.ia.dao.ICiIaAnalyseAttrJDao;
import org.springframework.stereotype.Repository;

@Repository
public class CiIaAnalyseAttrJDaoImpl extends JdbcBaseDao implements ICiIaAnalyseAttrJDao {
    public CiIaAnalyseAttrJDaoImpl() {
    }

    public int select01LabelCount(String listTableName, String attrCol) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ").append(listTableName).append(" where ").append(attrCol).append(" =0 or ").append(attrCol).append(" =1 or ").append(attrCol).append(" = null");
        int count = this.getBackSimpleJdbcTemplate().queryForInt(sb.toString(), new Object[0]);
        return count;
    }

    public int selectListTableCount(String listTableName) {
        StringBuffer sb = new StringBuffer("select count(*) from ");
        sb.append(listTableName);
        int count = this.getBackSimpleJdbcTemplate().queryForInt(sb.toString(), new Object[0]);
        return count;
    }
}

package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiCustomListExeInfoJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomListExeInfo;
import com.ailk.biapp.ci.model.Pager;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiCustomListExeInfoJDaoImpl extends JdbcBaseDao implements ICiCustomListExeInfoJDao {
    private Logger log = Logger.getLogger(CiCustomListExeInfoJDaoImpl.class);

    public CiCustomListExeInfoJDaoImpl() {
    }

    public int getListExeInfoNumByTableName(String listTableName) {
        String countSql = "select count(*) from CI_CUSTOM_LIST_EXE_INFO l where l.LIST_TABLE_NAME = ?";
        this.log.debug("客户群清单扩展信息列表sql=" + countSql);
        int count = this.getSimpleJdbcTemplate().queryForInt(countSql, new Object[]{listTableName});
        return count;
    }

    public List<CiCustomListExeInfo> queryListExeInfosByTableName(Pager pager, String listTableName) {
        String sql = "select * from CI_CUSTOM_LIST_EXE_INFO l where l.LIST_TABLE_NAME = ? order by l.START_TIME desc, l.END_TIME desc";
        String sqlPage = "";
        if(pager.getPageSize() != 0) {
            sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), pager.getPageNum(), pager.getPageSize());
        } else {
            sqlPage = sql;
        }

        this.log.debug("sql=" + sqlPage);
        return this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiCustomListExeInfo.class), new Object[]{listTableName});
    }
}

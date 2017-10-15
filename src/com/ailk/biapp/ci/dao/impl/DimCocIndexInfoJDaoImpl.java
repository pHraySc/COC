package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.IDimCocIndexInfoJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.DimCocIndexInfo;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.HashMap;
import java.util.List;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DimCocIndexInfoJDaoImpl extends JdbcBaseDao implements IDimCocIndexInfoJDao {
    public DimCocIndexInfoJDaoImpl() {
    }

    public int getDimCocIndexInfoCount(HashMap<String, Object> bean) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1) ").append("FROM DIM_COC_INDEX_INFO  T1, DIM_COC_INDEX_TABLE_INFO T2 ");
        sql.append(" WHERE T1.DATA_SRC_CODE = T2.DATA_SRC_CODE ");
        if(StringUtil.isNotEmpty(bean.get("indexName"))) {
            sql.append("AND LOWER(T1.INDEX_CODE||T1.DATA_SRC_COL_NAME||T2.DATA_SRC_TAB_NAME) LIKE LOWER(\'%").append(bean.get("indexName")).append("%\') ");
        }

        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), bean);
    }

    public List<DimCocIndexInfo> getDimCocIndexInfoList(int currPage, int pageSize, HashMap<String, Object> bean) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T1.*,T2.DATA_SRC_TAB_NAME dataSrcName  ").append("FROM DIM_COC_INDEX_INFO  T1, DIM_COC_INDEX_TABLE_INFO T2 ").append(" WHERE T1.DATA_SRC_CODE = T2.DATA_SRC_CODE ");
        if(StringUtil.isNotEmpty(bean.get("indexName"))) {
            sql.append("AND LOWER( T1.INDEX_CODE||T1.DATA_SRC_COL_NAME||T2.DATA_SRC_TAB_NAME) LIKE LOWER(\'%").append(bean.get("indexName")).append("%\') ");
        }

        sql.append("ORDER BY T1.EFFECTIVE_TIME DESC ");
        System.out.println("sql:" + sql.toString());
        String sqlPage;
        if(pageSize != 0) {
            sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), currPage, pageSize);
        } else {
            sqlPage = sql.toString();
        }

        return this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(DimCocIndexInfo.class), bean);
    }
}

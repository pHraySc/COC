package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiEnumConditionInfoJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiEnumConditionInfo;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.asiainfo.biframe.dimtable.model.DimTableDefine;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class CiEnumConditionInfoJDaoImpl extends JdbcBaseDao implements ICiEnumConditionInfoJDao {
    private Logger log = Logger.getLogger(CiEnumConditionInfoJDaoImpl.class);

    public CiEnumConditionInfoJDaoImpl() {
    }

    public long selectCiEnumConditionInfoCount(CiEnumConditionInfo searchBean, String cityAuthorityWhereStr, DimTableDefine define, CiMdaSysTableColumn column) throws Exception {
        String sql = "select count(T.ENUM_ID) " + this.getFromSql(searchBean, cityAuthorityWhereStr, define, column);
        this.log.debug("count sql is : " + sql);
        long count = 0L;
        if(StringUtil.isNotEmpty(searchBean.getEnumName())) {
            count = this.getSimpleJdbcTemplate().queryForLong(sql, new Object[]{searchBean.getEnumName()});
        } else {
            count = this.getSimpleJdbcTemplate().queryForLong(sql, new Object[0]);
        }

        return count;
    }

    public List<Map<String, Object>> selectCiEnumConditionInfoList(int currPage, int pageSize, CiEnumConditionInfo searchBean, String cityAuthorityWhereStr, DimTableDefine define, CiMdaSysTableColumn column) throws Exception {
        String sql = "select T.ENUM_ID V_KEY, T.ENUM_NAME V_NAME, T.SORT_NUM " + this.getFromSql(searchBean, cityAuthorityWhereStr, define, column) + " order by T.SORT_NUM asc ";
        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql, currPage, pageSize);
        this.log.debug("select list sqlPage is : " + sqlPage);
        new ArrayList();
        List list;
        if(StringUtil.isNotEmpty(searchBean.getEnumName())) {
            list = this.getSimpleJdbcTemplate().queryForList(sqlPage, new Object[]{searchBean.getEnumName()});
        } else {
            list = this.getSimpleJdbcTemplate().queryForList(sqlPage, new Object[0]);
        }

        return list;
    }

    public String getFromSql(CiEnumConditionInfo searchBean, String cityAuthorityWhereStr, DimTableDefine define, CiMdaSysTableColumn column) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("from CI_ENUM_CONDITION_INFO T ");
        if(StringUtil.isNotEmpty(cityAuthorityWhereStr)) {
            String dimCodeCol = define.getDimTablename() + "." + define.getDimCodeCol();
            if(1 == column.getColumnDataTypeId().intValue()) {
                dimCodeCol = this.getDataBaseAdapter().getColumnToChar(dimCodeCol);
            }

            sql.append(" inner join ");
            sql.append(define.getDimTablename());
            sql.append(" on T.enum_id=").append(dimCodeCol);
        }

        sql.append(" where T.ENUM_CATEGORY_ID=\'");
        sql.append(searchBean.getEnumCategoryId()).append("\' ");
        if(StringUtil.isNotEmpty(searchBean.getEnumName())) {
            searchBean.setEnumName("%" + searchBean.getEnumName().replace("|", "||").replace("%", "|%").replace("_", "|_").toLowerCase() + "%");
            sql.append(" and LOWER(T.ENUM_NAME) like ? escape \'|\' ");
        }

        if(StringUtil.isNotEmpty(cityAuthorityWhereStr)) {
            sql.append(" and ").append(cityAuthorityWhereStr);
        }

        return sql.toString();
    }
}

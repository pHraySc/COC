package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICustomProductMatchJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiCustomGroupInfo;
import com.ailk.biapp.ci.entity.CustomProductMatch;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CustomProductMatchJDaoImpl extends JdbcBaseDao implements ICustomProductMatchJDao {
    public CustomProductMatchJDaoImpl() {
    }

    public boolean isCustomProductMatch(CiCustomGroupInfo bean) throws Exception {
        boolean count = false;
        boolean isCustomProductMath = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) FROM CUSTOM_PRODUCT_MATCH_TOP10 WHERE 1=1");
        if(StringUtils.isNotBlank(bean.getListTableName())) {
            sql.append(" AND LIST_TABLE_NAME = :listTableName");
        }

        if(StringUtils.isNotBlank(bean.getDataDate())) {
            sql.append(" AND DATA_DATE = :dataDate");
        }

        int count1 = this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(bean));
        if(count1 > 0) {
            isCustomProductMath = true;
        }

        return isCustomProductMath;
    }

    public List<CustomProductMatch> selectSysProductMatch(CiCustomGroupInfo bean) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT(P.PRODUCT_ID),P.PRODUCT_NAME,T.MATCH_CUSTOM_NUM,T.MATCH_PROPOTION").append(" FROM CUSTOM_PRODUCT_MATCH_TOP10 T,CI_PRODUCT_INFO P WHERE T.PRODUCT_ID = P.PRODUCT_ID").append(" AND T.LIST_TABLE_NAME = :listTableName AND T.DATA_DATE = :productDate ORDER BY T.MATCH_PROPOTION DESC");
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CustomProductMatch.class), new BeanPropertySqlParameterSource(bean));
    }
}

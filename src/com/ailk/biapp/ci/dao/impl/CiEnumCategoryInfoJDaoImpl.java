package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiEnumCategoryInfoJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiEnumCategoryInfo;
import com.ailk.biapp.ci.model.Pager;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiEnumCategoryInfoJDaoImpl extends JdbcBaseDao implements ICiEnumCategoryInfoJDao {
    private Logger log = Logger.getLogger(CiEnumCategoryInfoJDaoImpl.class);

    public CiEnumCategoryInfoJDaoImpl() {
    }

    public int selectEnumCategoryInfoTotalCountByColumnId(String userId, String cityId, Integer columnId) throws Exception {
        HashMap paramMap = new HashMap();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT COUNT(1) FROM CI_ENUM_CATEGORY_INFO where STATUS = ").append(ServiceConstants.PUBLIC_STATUS_VAL);
        if(StringUtil.isNotEmpty(userId)) {
            sql.append(" AND USER_ID = :userId");
            paramMap.put("userId", userId);
        }

        if(StringUtil.isNotEmpty(cityId)) {
            sql.append(" AND CITY_ID = :cityId");
            paramMap.put("cityId", cityId);
        }

        if(StringUtil.isNotEmpty(columnId)) {
            sql.append(" AND COLUMN_ID = :columnId");
            paramMap.put("columnId", columnId);
        }

        this.log.debug("查询分类列表总数SQL" + sql.toString());
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), paramMap);
    }

    public List<CiEnumCategoryInfo> selectEnumCategoryInfoPagerListByColumnId(Pager pager, String userId, String cityId, Integer columnId) throws Exception {
        HashMap paramMap = new HashMap();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM CI_ENUM_CATEGORY_INFO where STATUS = ").append(ServiceConstants.PUBLIC_STATUS_VAL);
        if(StringUtil.isNotEmpty(userId)) {
            sql.append(" AND USER_ID = :userId");
            paramMap.put("userId", userId);
        }

        if(StringUtil.isNotEmpty(cityId)) {
            sql.append(" AND CITY_ID = :cityId");
            paramMap.put("cityId", cityId);
        }

        if(StringUtil.isNotEmpty(columnId)) {
            sql.append(" AND COLUMN_ID = :columnId");
            paramMap.put("columnId", columnId);
        }

        String sqlPage = this.getDataBaseAdapter().getPagedSql(sql.toString(), pager.getPageNum(), pager.getPageSize());
        this.log.debug("查询分类分页列表SQL" + sqlPage);
        return this.getSimpleJdbcTemplate().query(sqlPage, ParameterizedBeanPropertyRowMapper.newInstance(CiEnumCategoryInfo.class), paramMap);
    }

    public List<CiEnumCategoryInfo> queryEnumCategoryInfoListByCityIdAndColumnId(String cityId, Integer columnId, String enumCategoryName) {
        HashMap paramMap = new HashMap();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM CI_ENUM_CATEGORY_INFO where STATUS = ").append(ServiceConstants.PUBLIC_STATUS_VAL);
        if(StringUtil.isNotEmpty(cityId)) {
            String root = Configure.getInstance().getProperty("CENTER_CITYID");
            if(!root.equals(cityId) && root != cityId) {
                sql.append(" AND ( CITY_ID = :cityId");
                sql.append(" OR CITY_ID = \'").append(root).append("\' ) ");
                paramMap.put("cityId", cityId);
            }
        }

        if(StringUtil.isNotEmpty(columnId)) {
            sql.append(" AND COLUMN_ID = :columnId");
            paramMap.put("columnId", columnId);
        }

        if(StringUtil.isNotEmpty(enumCategoryName)) {
            sql.append(" AND ENUM_CATEGORY_NAME = :enumCategoryName");
            paramMap.put("enumCategoryName", enumCategoryName);
        }

        this.log.debug("根据columnId与cityId查询分类列表SQL" + sql);
        return this.getSimpleJdbcTemplate().query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(CiEnumCategoryInfo.class), paramMap);
    }

    public void batchUpdateValueList(final List<String> valueList, String tabName, String columns) {
        long start = System.currentTimeMillis();
        if(!this.getBackDataBaseAdapter().getDbType().equalsIgnoreCase("TERA")) {
            String valuePosition = columns.replaceAll("\\w+,", "?,");
            valuePosition = valuePosition.replaceAll("\\w+$", "?");
            if("DB2".equalsIgnoreCase(this.getBackDataBaseAdapter().getDbType())) {
                String sql = "ALTER TABLE " + tabName + " ACTIVATE NOT LOGGED INITIALLY";
                this.log.debug("alter sql:" + sql);
                this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(sql);
                this.log.debug("alter table cost:" + (System.currentTimeMillis() - start) + "ms");
            }

            StringBuffer sql1 = new StringBuffer();
            sql1.append("INSERT INTO ").append(tabName).append("(").append(columns).append(") VALUES(").append(valuePosition).append(")");
            this.getBackSimpleJdbcTemplate().getJdbcOperations().batchUpdate(sql1.toString(), new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String tempArr = (String)valueList.get(i);
                    ps.setString(1, tempArr);
                }

                public int getBatchSize() {
                    return valueList.size();
                }
            });
            this.getBackSimpleJdbcTemplate().getJdbcOperations().execute("COMMIT");
        }

        this.log.debug("batchUpdateValueList cost:" + (System.currentTimeMillis() - start) + "ms");
    }
}

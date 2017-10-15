package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiTagInfoJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiTagInfo;
import com.ailk.biapp.ci.model.CiTagTypeModel;
import com.asiainfo.biframe.utils.config.Configure;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiTagInfoJDao extends JdbcBaseDao implements ICiTagInfoJDao {
    private Logger log = Logger.getLogger(CiTagInfoJDao.class);

    public CiTagInfoJDao() {
    }

    public Integer selectPhoneNumExists(String phoneNum) {
        String tableName = Configure.getInstance().getProperty("PEOPLE_BASE_TAG_TABLE");
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1) FROM ").append(tableName).append(" WHERE PRODUCT_NO=?");
        this.log.debug(sql.toString());
        return Integer.valueOf((int)this.getBackSimpleJdbcTemplate().queryForLong(sql.toString(), new Object[]{phoneNum}));
    }

    public Map<String, Object> selectProductNoTag(String phoneNum) {
        String tableName = Configure.getInstance().getProperty("PEOPLE_BASE_TAG_TABLE");
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append(tableName).append(" WHERE PRODUCT_NO=?");
        this.log.debug(sql.toString());
        return this.getBackSimpleJdbcTemplate().queryForMap(sql.toString(), new Object[]{phoneNum});
    }

    public List<CiTagInfo> selectAllTag() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TAG_ID,TAG_NAME ,IS_IDENTIFY,TAG_TYPE,TAG_UNIT  ");
        sql.append("FROM CI_TAG_INFO  ");
        this.log.debug(sql.toString());
        return this.getSimpleJdbcTemplate().query(sql.toString(), new ParameterizedBeanPropertyRowMapper() {
            public CiTagInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                CiTagInfo item = new CiTagInfo();
                item.setTagId(rs.getString("TAG_ID"));
                item.setTagName(rs.getString("TAG_NAME"));
                item.setIsIdentify(rs.getString("IS_IDENTIFY"));
                item.setTagType(Integer.valueOf(rs.getInt("TAG_TYPE")));
                item.setTagUnit(rs.getString("TAG_UNIT"));
                return item;
            }
        }, new Object[0]);
    }

    public List<CiTagTypeModel> selectAllTagType() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ID,TYPE_NAME,ICON FROM CI_TAG_TYPE ORDER BY ID");
        this.log.debug(sql.toString());
        return this.getSimpleJdbcTemplate().query(sql.toString(), new ParameterizedBeanPropertyRowMapper() {
            public CiTagTypeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                CiTagTypeModel item = new CiTagTypeModel();
                item.setId(Integer.valueOf(rs.getInt("ID")));
                item.setTypeName(rs.getString("TYPE_NAME"));
                item.setIcon(rs.getString("ICON"));
                item.setCiTagInfoList(new ArrayList());
                return item;
            }
        }, new Object[0]);
    }
}

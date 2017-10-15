package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiGroupAttrRelJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiGroupAttrRelId;
import com.ailk.biapp.ci.util.DateUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CiGroupAttrRelJDaoImpl extends JdbcBaseDao implements ICiGroupAttrRelJDao {
    protected Logger log = Logger.getLogger(CiGroupAttrRelJDaoImpl.class);

    public CiGroupAttrRelJDaoImpl() {
    }

    public List<CiGroupAttrRel> selectCiGroupAttrRelList(String groupInfoId, Date listCreateTime) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from CI_GROUP_ATTR_REL where CUSTOM_GROUP_ID = ? and MODIFY_TIME = ").append("(select min(MODIFY_TIME) from CI_GROUP_ATTR_REL where MODIFY_TIME >= ? and  CUSTOM_GROUP_ID = ?) order by ATTR_COL");
        this.log.debug("groupInfoId: " + groupInfoId + ", listCreateTime :" + DateUtil.date2String(listCreateTime, "yyyy-MM-dd HH:mm:ss") + " ,取得客户群属性列列表的sql：" + sql.toString());
        List ciGroupAttrRelList = this.getSimpleJdbcTemplate().query(sql.toString(), new ParameterizedBeanPropertyRowMapper() {
            public CiGroupAttrRel mapRow(ResultSet rs, int rowNum) throws SQLException {
                CiGroupAttrRel ciGroupAttrRel = new CiGroupAttrRel();
                CiGroupAttrRelId ciGroupAttrRelId = new CiGroupAttrRelId();
                ciGroupAttrRelId.setAttrCol(rs.getString("ATTR_COL"));
                ciGroupAttrRelId.setCustomGroupId(rs.getString("CUSTOM_GROUP_ID"));
                ciGroupAttrRelId.setModifyTime(rs.getTimestamp("MODIFY_TIME"));
                ciGroupAttrRel.setId(ciGroupAttrRelId);
                ciGroupAttrRel.setAttrColName(rs.getString("ATTR_COL_NAME"));
                ciGroupAttrRel.setAttrColType(rs.getString("ATTR_COL_TYPE"));
                ciGroupAttrRel.setAttrSource(rs.getInt("ATTR_SOURCE"));
                ciGroupAttrRel.setLabelOrCustomId(rs.getString("LABEL_OR_CUSTOM_ID"));
                ciGroupAttrRel.setLabelOrCustomColumn(rs.getString("LABEL_OR_CUSTOM_COLUMN"));
                ciGroupAttrRel.setIsVerticalAttr(rs.getInt("IS_VERTICAL_ATTR"));
                ciGroupAttrRel.setStatus(Integer.valueOf(rs.getInt("STATUS")));
                ciGroupAttrRel.setAttrVal(rs.getString("ATTR_VAL"));
                ciGroupAttrRel.setTableName(rs.getString("TABLE_NAME"));
                ciGroupAttrRel.setSortType(rs.getString("SORT_TYPE"));
                if(null == rs.getObject("SORT_NUM")) {
                    ciGroupAttrRel.setSortNum((Integer)null);
                } else {
                    ciGroupAttrRel.setSortNum(Integer.valueOf(rs.getInt("SORT_NUM")));
                }

                return ciGroupAttrRel;
            }
        }, new Object[]{groupInfoId, listCreateTime, groupInfoId});
        this.log.debug("客户群属性列列表的size：" + ciGroupAttrRelList.size());
        return ciGroupAttrRelList;
    }

    public List<CiGroupAttrRel> selectNewestCiGroupAttrRelList(String groupInfoId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from CI_GROUP_ATTR_REL where CUSTOM_GROUP_ID = ? and MODIFY_TIME = ").append("(select max(MODIFY_TIME) from CI_GROUP_ATTR_REL where CUSTOM_GROUP_ID = ?) and STATUS = ").append(0).append(" order by ATTR_COL");
        this.log.debug("groupInfoId: " + groupInfoId + " ,取得客户群属性列列表的sql：" + sql.toString());
        List ciGroupAttrRelList = this.getSimpleJdbcTemplate().query(sql.toString(), new ParameterizedBeanPropertyRowMapper() {
            public CiGroupAttrRel mapRow(ResultSet rs, int rowNum) throws SQLException {
                CiGroupAttrRel ciGroupAttrRel = new CiGroupAttrRel();
                CiGroupAttrRelId ciGroupAttrRelId = new CiGroupAttrRelId();
                ciGroupAttrRelId.setAttrCol(rs.getString("ATTR_COL"));
                ciGroupAttrRelId.setCustomGroupId(rs.getString("CUSTOM_GROUP_ID"));
                ciGroupAttrRelId.setModifyTime(rs.getTimestamp("MODIFY_TIME"));
                ciGroupAttrRel.setId(ciGroupAttrRelId);
                ciGroupAttrRel.setAttrColName(rs.getString("ATTR_COL_NAME"));
                ciGroupAttrRel.setAttrColType(rs.getString("ATTR_COL_TYPE"));
                ciGroupAttrRel.setAttrSource(rs.getInt("ATTR_SOURCE"));
                ciGroupAttrRel.setLabelOrCustomId(rs.getString("LABEL_OR_CUSTOM_ID"));
                ciGroupAttrRel.setLabelOrCustomColumn(rs.getString("LABEL_OR_CUSTOM_COLUMN"));
                ciGroupAttrRel.setIsVerticalAttr(rs.getInt("IS_VERTICAL_ATTR"));
                ciGroupAttrRel.setStatus(Integer.valueOf(rs.getInt("STATUS")));
                ciGroupAttrRel.setAttrVal(rs.getString("ATTR_VAL"));
                ciGroupAttrRel.setTableName(rs.getString("TABLE_NAME"));
                ciGroupAttrRel.setSortType(rs.getString("SORT_TYPE"));
                if(null == rs.getObject("SORT_NUM")) {
                    ciGroupAttrRel.setSortNum((Integer)null);
                } else {
                    ciGroupAttrRel.setSortNum(Integer.valueOf(rs.getInt("SORT_NUM")));
                }

                return ciGroupAttrRel;
            }
        }, new Object[]{groupInfoId, groupInfoId});
        this.log.debug("客户群最新的属性列列表的size：" + ciGroupAttrRelList.size());
        return ciGroupAttrRelList;
    }

    public List<CiGroupAttrRel> selectNewestCiGroupAttrRelBySort(String groupInfoId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from CI_GROUP_ATTR_REL where CUSTOM_GROUP_ID = ? and sort_num>=0 and MODIFY_TIME = ").append("(select max(MODIFY_TIME) from CI_GROUP_ATTR_REL where CUSTOM_GROUP_ID = ?) and STATUS = ").append(0).append(" order by sort_num");
        this.log.debug("groupInfoId: " + groupInfoId + " ,取得客户群属性列列表的sql：" + sql.toString());
        List ciGroupAttrRelList = this.getSimpleJdbcTemplate().query(sql.toString(), new ParameterizedBeanPropertyRowMapper() {
            public CiGroupAttrRel mapRow(ResultSet rs, int rowNum) throws SQLException {
                CiGroupAttrRel ciGroupAttrRel = new CiGroupAttrRel();
                CiGroupAttrRelId ciGroupAttrRelId = new CiGroupAttrRelId();
                ciGroupAttrRelId.setAttrCol(rs.getString("ATTR_COL"));
                ciGroupAttrRelId.setCustomGroupId(rs.getString("CUSTOM_GROUP_ID"));
                ciGroupAttrRelId.setModifyTime(rs.getTimestamp("MODIFY_TIME"));
                ciGroupAttrRel.setId(ciGroupAttrRelId);
                ciGroupAttrRel.setAttrColName(rs.getString("ATTR_COL_NAME"));
                ciGroupAttrRel.setAttrColType(rs.getString("ATTR_COL_TYPE"));
                ciGroupAttrRel.setAttrSource(rs.getInt("ATTR_SOURCE"));
                ciGroupAttrRel.setLabelOrCustomId(rs.getString("LABEL_OR_CUSTOM_ID"));
                ciGroupAttrRel.setLabelOrCustomColumn(rs.getString("LABEL_OR_CUSTOM_COLUMN"));
                ciGroupAttrRel.setIsVerticalAttr(rs.getInt("IS_VERTICAL_ATTR"));
                ciGroupAttrRel.setStatus(Integer.valueOf(rs.getInt("STATUS")));
                ciGroupAttrRel.setAttrVal(rs.getString("ATTR_VAL"));
                ciGroupAttrRel.setTableName(rs.getString("TABLE_NAME"));
                ciGroupAttrRel.setSortType(rs.getString("SORT_TYPE"));
                if(null == rs.getObject("SORT_NUM")) {
                    ciGroupAttrRel.setSortNum((Integer)null);
                } else {
                    ciGroupAttrRel.setSortNum(Integer.valueOf(rs.getInt("SORT_NUM")));
                }

                return ciGroupAttrRel;
            }
        }, new Object[]{groupInfoId, groupInfoId});
        this.log.debug("客户群最新的属性列列表的size：" + ciGroupAttrRelList.size());
        return ciGroupAttrRelList;
    }

    public void updateCiGroupAttrRelListModifyTime(String groupInfoId, Date listCreateTime) throws Exception {
        Date d = new Date();
        StringBuffer updateSql = new StringBuffer();
        updateSql.append("update CI_GROUP_ATTR_REL set MODIFY_TIME = ? where CUSTOM_GROUP_ID = ? and MODIFY_TIME = ").append("(select max(MODIFY_TIME) from CI_GROUP_ATTR_REL where CUSTOM_GROUP_ID = ?)");
        int count = this.getSimpleJdbcTemplate().update(updateSql.toString(), new Object[]{d, groupInfoId, groupInfoId});
        this.log.debug("updateCiGroupAttrRelListModifyTime,groupInfoId: " + groupInfoId + ", listCreateTime :" + DateUtil.date2String(listCreateTime, "yyyy-MM-dd HH:mm:ss") + ", seccuss count:" + count + "  sql:" + updateSql);
    }

    public void updateCiGroupAttrRelStatusByGroupInfoId(String groupInfoId) throws Exception {
        StringBuffer updateSql = new StringBuffer();
        updateSql.append("update CI_GROUP_ATTR_REL set STATUS = ").append(1).append(" where CUSTOM_GROUP_ID = ? and STATUS=").append(0);
        int count = this.getSimpleJdbcTemplate().update(updateSql.toString(), new Object[]{groupInfoId});
        this.log.debug("updateCiGroupAttrRelStatusByGroupInfoId,groupInfoId: " + groupInfoId + ", seccuss count:" + count + "  sql:" + updateSql);
    }

    public void deleteGroupAttrRelList(String customGroupId, Date attrModifyTime) {
        String sql = "delete from CI_GROUP_ATTR_REL where CUSTOM_GROUP_ID=? and MODIFY_TIME>?";
        this.log.debug(sql);
        this.getSimpleJdbcTemplate().update(sql, new Object[]{customGroupId, attrModifyTime});
    }
}

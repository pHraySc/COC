package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelVerticalColumnRelJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRel;
import com.ailk.biapp.ci.entity.CiLabelVerticalColumnRelId;
import common.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository("ciLabelVerticalColumnRelJDao")
public class CiLabelVerticalColumnRelJDaoImpl extends JdbcBaseDao implements ICiLabelVerticalColumnRelJDao {
    private Logger log = Logger.getLogger(CiLabelVerticalColumnRelJDaoImpl.class);

    public CiLabelVerticalColumnRelJDaoImpl() {
    }

    public List<CiLabelVerticalColumnRel> selectCiVerticalColumnRelByLabelId(Integer labelId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.LABEL_ID,A.COLUMN_ID,A.LABEL_TYPE_ID,A.IS_MUST_COLUMN,SORT_NUM,B.LABEL_TYPE_NAME,C.UNIT,C.COLUMN_CN_NAME ");
        sql.append("FROM CI_LABEL_VERTICAL_COLUMN_REL A,DIM_LABEL_TYPE B ,CI_MDA_SYS_TABLE_COLUMN C ");
        sql.append("WHERE A.LABEL_TYPE_ID=B.LABEL_TYPE_ID AND A.COLUMN_ID=C.COLUMN_ID AND A.LABEL_ID=? ");
        sql.append("ORDER BY A.SORT_NUM");
        this.log.debug(sql);
        return this.getSimpleJdbcTemplate().query(sql.toString(), new ParameterizedBeanPropertyRowMapper() {
            public CiLabelVerticalColumnRel mapRow(ResultSet rs, int rowNum) throws SQLException {
                CiLabelVerticalColumnRel item = new CiLabelVerticalColumnRel();
                CiLabelVerticalColumnRelId id = new CiLabelVerticalColumnRelId();
                id.setLabelId(Integer.valueOf(rs.getInt("LABEL_ID")));
                id.setColumnId(Integer.valueOf(rs.getInt("COLUMN_ID")));
                item.setId(id);
                item.setLabelTypeId(Integer.valueOf(rs.getInt("LABEL_TYPE_ID")));
                item.setIsMustColumn(Integer.valueOf(rs.getInt("IS_MUST_COLUMN")));
                item.setSortNum(Integer.valueOf(rs.getInt("SORT_NUM")));
                item.setLabelTypeName(rs.getString("LABEL_TYPE_NAME"));
                item.setUnit(rs.getString("UNIT"));
                item.setColumnCnName(rs.getString("COLUMN_CN_NAME"));
                return item;
            }
        }, new Object[]{labelId});
    }
}

package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLabelRuleJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.dao.impl.CustomGroupInfoJDaoImpl;
import com.ailk.biapp.ci.util.PrivilegeServiceUtil;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

@Repository
public class CiLabelRuleJDaoImpl extends JdbcBaseDao implements ICiLabelRuleJDao {
    private Logger log = Logger.getLogger(CustomGroupInfoJDaoImpl.class);

    public CiLabelRuleJDaoImpl() {
    }

    public int getIsUsingCustomCount(String listTableNameStrs) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(1) ");
        sql.append("FROM CI_LABEL_RULE r, CI_CUSTOM_LIST_INFO l, CI_CUSTOM_GROUP_INFO g ");
        sql.append("WHERE r.ELEMENT_TYPE =  5 AND r.CALCU_ELEMENT = l.LIST_TABLE_NAME AND l.LIST_TABLE_NAME in (" + listTableNameStrs + ")  " + "AND g.CREATE_USER_ID !=\'" + PrivilegeServiceUtil.getUserId() + "\' " + "AND r.CUSTOM_ID = g.CUSTOM_GROUP_ID " + "AND g.STATUS = " + 1);
        this.log.debug("sql=" + sql.toString());
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[0]);
    }

    public int getIsUsingCustomCount(String listTableNameStrs, String createUserId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(1) ");
        sql.append("FROM CI_LABEL_RULE r, CI_CUSTOM_LIST_INFO l, CI_CUSTOM_GROUP_INFO g ");
        sql.append("WHERE r.ELEMENT_TYPE =  5 AND r.CALCU_ELEMENT = l.LIST_TABLE_NAME AND l.LIST_TABLE_NAME in (\'" + listTableNameStrs + "\')  " + "AND r.CUSTOM_ID = g.CUSTOM_GROUP_ID " + "AND g.STATUS = " + 1);
        this.log.debug("sql=" + sql.toString());
        return this.getSimpleJdbcTemplate().queryForInt(sql.toString(), new Object[0]);
    }

    public void batchUpdateValueList(final List<String> valueList, String tabName, String columns) {
        long start = System.currentTimeMillis();
        if(!this.getBackDataBaseAdapter().getDbType().equalsIgnoreCase("TERA")) {
            String valuePosition = columns.replaceAll("\\w+,", "?,");
            valuePosition = valuePosition.replaceAll("\\w+$", "?");
            if("DB2".equalsIgnoreCase(this.getBackDataBaseAdapter().getDbType())) {
                String sqlBuf = "ALTER TABLE " + tabName + " ACTIVATE NOT LOGGED INITIALLY";
                this.log.debug("alter sql:" + sqlBuf);
                this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(sqlBuf);
                this.log.debug("alter table cost:" + (System.currentTimeMillis() - start) + "ms");
            }

            StringBuffer sqlBuf1 = new StringBuffer();
            sqlBuf1.append("INSERT INTO ").append(tabName).append("(").append(columns).append(") VALUES(").append(valuePosition).append(")");
            String sql = sqlBuf1.toString();
            this.getBackSimpleJdbcTemplate().getJdbcOperations().batchUpdate(sql, new BatchPreparedStatementSetter() {
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

    public List<Map<String, Object>> findValueListData(String valueTableName) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("SELECT * FROM ").append(valueTableName);
        List list = this.getBackSimpleJdbcTemplate().queryForList(sqlBuf.toString(), new Object[0]);
        return list;
    }
}

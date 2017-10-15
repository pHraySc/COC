package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICiLoadCustomGroupFileJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.entity.CiGroupAttrRel;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

@Repository
public class CiLoadCustomGroupFileJDaoImpl extends JdbcBaseDao implements ICiLoadCustomGroupFileJDao {
    private Logger log = Logger.getLogger(CiLoadCustomGroupFileJDaoImpl.class);

    public CiLoadCustomGroupFileJDaoImpl() {
    }

    public String createTable(String newTab, String tmpTable, String column) {
        String createSql = this.getBackDataBaseAdapter().getSqlCreateAsTable(newTab, tmpTable, column);
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(createSql);
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute("COMMIT");
        return createSql;
    }

    public void batchUpdateMobileList(final List<String[]> mobileList, String tabName, String columns) {
        long start = System.currentTimeMillis();
        if(!this.getBackDataBaseAdapter().getDbType().equalsIgnoreCase("TERA")) {
            String valuePosition = columns.replaceAll("\\w+,", "?,");
            valuePosition = valuePosition.replaceAll("\\w+$", "?");
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("INSERT INTO ").append(tabName).append("(").append(columns).append(") ").append("VALUES(").append(valuePosition).append(")");
            this.getBackSimpleJdbcTemplate().getJdbcOperations().batchUpdate(sqlBuf.toString(), new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String[] tempArr = (String[])mobileList.get(i);

                    for(int j = 0; j < tempArr.length; ++j) {
                        ps.setString(j + 1, tempArr[j]);
                    }

                }

                public int getBatchSize() {
                    return mobileList.size();
                }
            });
            this.getBackSimpleJdbcTemplate().getJdbcOperations().execute("COMMIT");
        }

        this.log.debug("batchUpdateMobileList cost:" + (System.currentTimeMillis() - start) + "ms");
    }

    public int batchInsert2CustListTab(String tabName, String tabNameTmp, String column) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ").append(tabName).append("(").append(column).append(")").append(" SELECT distinct ").append(column).append(" FROM ").append(tabNameTmp);
        this.log.info("copy data sql: " + sql.toString());
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(sql.toString());
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute("COMMIT");
        return this.getBackSimpleJdbcTemplate().queryForInt(" SELECT COUNT(*) FROM " + tabName, new Object[0]);
    }

    public List<String> addAttr2TmpTable(String tempTableName, List<CiGroupAttrRel> ciGroupAttrRelList) {
        ArrayList alterSqlList = new ArrayList();
        if(ciGroupAttrRelList != null) {
            CiGroupAttrRel item = null;
            String dbType = Configure.getInstance().getProperty("CI_BACK_DBTYPE");
            CacheBase cache = CacheBase.getInstance();

            for(int i = 0; i < ciGroupAttrRelList.size(); ++i) {
                item = (CiGroupAttrRel)ciGroupAttrRelList.get(i);
                if(item.getAttrSource() == 2) {
                    CiLabelInfo sql = cache.getEffectiveLabel(item.getLabelOrCustomId());
                    if(sql != null && sql.getDataStatusId().intValue() == 2) {
                        StringBuffer sql1 = (new StringBuffer("ALTER TABLE ")).append(tempTableName).append(" ADD ");
                        if("ORACLE".equalsIgnoreCase(dbType)) {
                            sql1.append(item.getId().getAttrCol()).append(" ");
                            sql1.append(item.getAttrColType());
                        } else {
                            sql1.append("COLUMN ").append(item.getId().getAttrCol()).append(" ");
                            sql1.append(item.getAttrColType());
                        }

                        this.log.debug("add column sql:" + sql1.toString());
                        alterSqlList.add(sql1.toString());
                        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(sql1.toString());
                    }
                } else {
                    StringBuffer var10 = (new StringBuffer("ALTER TABLE ")).append(tempTableName).append(" ADD ");
                    if("ORACLE".equalsIgnoreCase(dbType)) {
                        var10.append(item.getId().getAttrCol()).append(" ");
                        var10.append(item.getAttrColType());
                    } else {
                        var10.append("COLUMN ").append(item.getId().getAttrCol()).append(" ");
                        var10.append(item.getAttrColType());
                    }

                    this.log.debug("add column sql:" + var10.toString());
                    alterSqlList.add(var10.toString());
                    this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(var10.toString());
                }
            }
        }

        return alterSqlList;
    }

    public void deleteAllData(String tableName) {
        this.log.info("deleteAllData  " + tableName);
        if(this.tableExists(tableName)) {
            this.getBackSimpleJdbcTemplate().getJdbcOperations().execute("DROP TABLE " + tableName);
        }

    }

    public boolean tableExists(String tableName) {
        boolean flag = true;

        try {
            StringBuffer e = new StringBuffer();
            e.append("SELECT COUNT(1) FROM ").append(tableName);
            this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(e.toString());
        } catch (Exception var4) {
            this.log.warn("table " + tableName + " is not exists!");
            flag = false;
        }

        return flag;
    }
}

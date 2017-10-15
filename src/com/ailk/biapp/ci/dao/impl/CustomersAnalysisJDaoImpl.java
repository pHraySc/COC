package com.ailk.biapp.ci.dao.impl;

import com.ailk.biapp.ci.dao.ICustomersAnalysisJDao;
import com.ailk.biapp.ci.dao.base.JdbcBaseDao;
import com.ailk.biapp.ci.util.GroupCalcSqlPaser;
import com.asiainfo.biframe.utils.config.Configure;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class CustomersAnalysisJDaoImpl extends JdbcBaseDao implements ICustomersAnalysisJDao {
    private Logger log = Logger.getLogger(CustomersAnalysisJDaoImpl.class);

    public CustomersAnalysisJDaoImpl() {
    }

    public int getCustomersLabelJjCount(String selectPhoneNoSql, String tableName) {
        StringBuffer sql = (new StringBuffer("select count(1) from (")).append((new GroupCalcSqlPaser(this.getBackDataBaseAdapter().getDbType())).getIntersectOfTable(selectPhoneNoSql, tableName)).append(") abc");
        this.log.info("getCustomersLabelJjCount SQL:" + sql.toString());
        return this.selectBackCountBySql(sql.toString());
    }

    public int getCustomersByLabel(String selectPhoneNoSql, String tableName) {
        String column = Configure.getInstance().getProperty("RELATED_COLUMN");
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(" + column + ") FROM ").append(" (").append(selectPhoneNoSql).append(" ) abc ");
        this.log.info("getCustomersByLabel SQL:" + sql.toString());
        return this.selectBackCountBySql(sql.toString());
    }

    public int getCustomersLabelCjCount(String selectPhoneNoSql, String tableName) {
        StringBuffer sql = (new StringBuffer("select count(1) from (")).append((new GroupCalcSqlPaser(this.getBackDataBaseAdapter().getDbType())).getDifferenceOfTable(tableName, selectPhoneNoSql)).append(") abc ");
        this.log.info("getCustomersLabelCjCount SQL:" + sql.toString());
        return this.selectBackCountBySql(sql.toString());
    }

    public int getLableCustomersCjCount(String selectPhoneNoSql, String tableName) {
        StringBuffer sql = (new StringBuffer("select count(1) from (")).append((new GroupCalcSqlPaser(this.getBackDataBaseAdapter().getDbType())).getDifferenceOfTable(selectPhoneNoSql, tableName)).append(") abc");
        this.log.info("getLableCustomersCjCount SQL:" + sql.toString());
        return this.selectBackCountBySql(sql.toString());
    }

    public int getCustomersCount(String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*)").append(" FROM ").append(tableName);
        this.log.info("getCustomersCount SQL:" + sql.toString());
        return this.selectBackCountBySql(sql.toString());
    }

    public void insertCustomersTable(String selectPhoneNoSql, String newTabName, String tableName) {
        String column = Configure.getInstance().getProperty("RELATED_COLUMN");
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ").append(newTabName).append(" (").append(" ").append(column).append(" )").append((new GroupCalcSqlPaser(this.getBackDataBaseAdapter().getDbType())).getIntersectOfTable(selectPhoneNoSql, tableName));
        this.log.info("insertCustomersTable SQL:" + sql.toString());
        this.getBackSimpleJdbcTemplate().getJdbcOperations().execute(sql.toString());
    }
}

package com.ailk.biapp.ci.localization.shanxi.dao;

import com.ailk.biapp.ci.localization.shanxi.dao.SXBIConnectionEx;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.DES;
import java.io.BufferedReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SqlcaSX implements Serializable {
    private static final long serialVersionUID = 574350073511236312L;
    private static Log log = LogFactory.getLog(SqlcaSX.class);
    protected final String NULL_STRING;
    protected boolean unicodeToGB;
    protected boolean gbToUnicode;
    protected int sqlCode;
    protected String sqlNotice;
    protected int sqlRows;
    protected Connection connection;
    protected SXBIConnectionEx connectionEx;
    protected ResultSet sqlResultSet;
    protected boolean sqlAutoCommit;
    protected boolean sqlAutoRollback;
    protected String strSqlType;
    protected String strSQL;
    protected boolean lastNullFlag;
    protected Statement statement;
    protected String strDBMS;
    private static int openCount = 0;
    protected Statement batchStatement;
    static int g_nCount = 0;

    protected SqlcaSX() {
        this.NULL_STRING = "";
        this.unicodeToGB = true;
        this.gbToUnicode = true;
        this.sqlCode = 0;
        this.sqlNotice = null;
        this.sqlRows = -1;
        this.connection = null;
        this.connectionEx = null;
        this.sqlResultSet = null;
        this.sqlAutoCommit = false;
        this.sqlAutoRollback = false;
        this.strSqlType = "UNKNOWN";
        this.strSQL = null;
        this.lastNullFlag = false;
        this.statement = null;
        this.strDBMS = null;
        this.batchStatement = null;
        this.sqlCode = 0;
        this.sqlResultSet = null;
        this.sqlNotice = null;
        this.sqlAutoRollback = false;
        this.sqlAutoCommit = true;
        this.sqlRows = 0;
    }

    public SqlcaSX(Connection newConnection) throws Exception {
        this();
        this.connection = newConnection;

        try {
            if(this.connection != null) {
                this.setAutoCommit(this.sqlAutoCommit);
            }

            if(this.connection != null && this.connection.getTransactionIsolation() < 1) {
                this.connection.setTransactionIsolation(2);
            }
        } catch (Exception var3) {
            throw var3;
        }

        this.gbToUnicode = Boolean.valueOf(Configure.getInstance().getProperty("KPI_DB_CHARSET")).booleanValue();
        this.unicodeToGB = Boolean.valueOf(Configure.getInstance().getProperty("KPI_DB_CHARSET")).booleanValue();
    }

    public SqlcaSX(SXBIConnectionEx conn) throws Exception {
        this();
        this.connection = conn.getConnection();
        this.connectionEx = conn;

        try {
            if(this.connection != null) {
                this.setAutoCommit(this.sqlAutoCommit);
            }

            if(this.connection != null) {
                this.connection.setTransactionIsolation(2);
            }
        } catch (Exception var3) {
            throw var3;
        }

        this.gbToUnicode = Boolean.valueOf(Configure.getInstance().getProperty("KPI_DB_CHARSET")).booleanValue();
        this.unicodeToGB = Boolean.valueOf(Configure.getInstance().getProperty("KPI_DB_CHARSET")).booleanValue();
    }

    public SqlcaSX(SXBIConnectionEx conn, boolean bGBToUnicode, boolean bUnicodeToGB) throws Exception {
        this();
        this.connection = conn.getConnection();
        this.connectionEx = conn;

        try {
            if(this.connection != null) {
                this.setAutoCommit(this.sqlAutoCommit);
            }

            if(this.connection != null) {
                this.connection.setTransactionIsolation(2);
            }
        } catch (Exception var5) {
            throw var5;
        }

        this.gbToUnicode = bGBToUnicode;
        this.unicodeToGB = bUnicodeToGB;
    }

    public void setAutoCommit(boolean bAuto) throws Exception {
        this.sqlAutoCommit = bAuto;
        if(this.connection == null) {
            this.Failure("did not get a connection to the database", 0);
        }

        try {
            this.connection.setAutoCommit(this.sqlAutoCommit);
        } catch (SQLException var3) {
            this.sqlFailure("automatic commit of property failed!", var3, 0);
        }

    }

    public void setAutoRollback(boolean autoRollbackWhensqlFailure) throws Exception {
        this.sqlAutoRollback = autoRollbackWhensqlFailure;
    }

    public void setConnection(Connection newConnection) throws Exception {
        this.connection = newConnection;
    }

    public void setUnicodeToGB(boolean bFlag) {
        this.unicodeToGB = bFlag;
    }

    public void setGBToUnicode(boolean bFlag) {
        this.gbToUnicode = bFlag;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setSql(String sqlStr) throws Exception {
        if(this.connection == null) {
            this.Failure("did not get a connection to the database!", 0);
        }

        if(sqlStr == null || sqlStr.trim().length() < 1) {
            this.Failure("sql statement is empty!", 0);
        }

        this.strSQL = sqlStr;
        this.sqlCode = 0;
        this.sqlRows = 0;
        this.sqlNotice = null;
        sqlStr = sqlStr.trim();
        this.strSqlType = sqlStr.substring(0, 4).toUpperCase();
        if(this.strSqlType.equals("SELE")) {
            this.strSqlType = "SELECT";
        } else if(this.strSqlType.equals("WITH")) {
            this.strSqlType = "SELECT";
        } else if(this.strSqlType.equals("DELE")) {
            this.strSqlType = "DELETE";
        } else if(this.strSqlType.equals("UPDA")) {
            this.strSqlType = "UPDATE";
        } else if(this.strSqlType.equals("INSE")) {
            this.strSqlType = "INSERT";
        } else {
            this.strSqlType = "UNKNOWN";
        }

        this.close();
        ++openCount;
        String strType = this.getDBMSType();
        if(strType.equalsIgnoreCase("SQLSERVER")) {
            this.connection.setAutoCommit(true);
        }

        this.statement = this.connection.createStatement();
    }

    protected int execute() throws Exception {
        this.sqlRows = 0;
        if(this.statement == null) {
            this.sqlRows = -1;
            this.Failure("there is no enforceable SQL!", 0);
        }

        try {
            if(this.strSqlType.equals("SELECT")) {
                this.sqlResultSet = this.statement.executeQuery(this.strSQL);
            } else {
                this.sqlRows = this.statement.executeUpdate(this.strSQL);
            }
        } catch (SQLException var2) {
            this.sqlRows = -1;
            this.sqlFailure("execute SQL:\n  " + this.strSQL + " error!", var2, 0);
            return -1;
        }

        return this.sqlRows;
    }

    public int execute(String strSQL) throws Exception {
        if(this.unicodeToGB) {
            strSQL = this.unicodeToGB(strSQL);
        }

        this.setSql(strSQL);
        return this.execute();
    }

    public void addBatch(String sql) throws Exception {
        try {
            if(this.batchStatement == null) {
                this.batchStatement = this.connection.createStatement();
            }

            this.batchStatement.addBatch(sql);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw var3;
        }
    }

    public int[] executeBatch() throws Exception {
        try {
            if(this.batchStatement == null) {
                return null;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
            throw var2;
        }

        return this.batchStatement.executeBatch();
    }

    public int getSqlRows() {
        return this.sqlRows;
    }

    protected void sqlFailure(String notice, SQLException e, int flag) throws Exception {
        this.sqlCode = -e.getErrorCode();
        this.sqlNotice = "sql statement" + this.strSQL + "\n" + notice + "\n" + "  error code :" + (new Integer(this.sqlCode)).toString() + ";\n" + "  error message:" + this.GBToUnicode(e.getMessage()) + "\n";
        throw new Exception("SQL Error:" + this.sqlNotice + this.GBToUnicode(notice + e.getMessage()));
    }

    public void closeAll() {
        try {
            this.close();
            if(this.batchStatement != null) {
                this.batchStatement.close();
                this.batchStatement = null;
            }

            if(null != this.connectionEx) {
                this.connectionEx.close();
            } else if(null != this.connection) {
                if(!this.connection.isClosed()) {
                    this.connection.close();
                }

                this.connection = null;
            }

            this.connectionEx = null;
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void close() {
        this.sqlNotice = null;

        try {
            if(this.sqlResultSet != null) {
                this.sqlResultSet.close();
                this.sqlResultSet = null;
            }

            if(this.statement != null) {
                --openCount;
                this.statement.close();
                this.statement = null;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private void Failure(String notice, int flag) throws Exception {
        this.sqlCode = 50;
        this.sqlNotice = notice;
        if(flag == 1 && this.sqlAutoRollback && !this.strSqlType.equals("SELECT")) {
            try {
                this.connection.rollback();
            } catch (SQLException var4) {
                ;
            }
        }

        throw new Exception(this.sqlNotice);
    }

    private String unicodeToGB(String strIn) {
        if(!this.unicodeToGB) {
            return strIn;
        } else {
            String strOut = null;
            if(strIn != null && !strIn.trim().equals("")) {
                try {
                    byte[] e = strIn.getBytes("GBK");
                    strOut = new String(e, "ISO8859_1");
                } catch (Exception var4) {
                    strOut = strIn;
                }

                return strOut;
            } else {
                return strIn;
            }
        }
    }

    private String GBToUnicode(String strIn) {
        if(!this.gbToUnicode) {
            return strIn;
        } else {
            String strOut = null;
            if(strIn != null && !strIn.trim().equals("")) {
                try {
                    byte[] e = strIn.getBytes("ISO8859_1");
                    strOut = new String(e, "GBK");
                } catch (Exception var4) {
                    strOut = strIn;
                }

                return strOut;
            } else {
                return strIn;
            }
        }
    }

    public ResultSetMetaData getMetaData() throws Exception {
        return this.sqlResultSet.getMetaData();
    }

    public boolean next() throws Exception {
        return this.sqlResultSet.next();
    }

    public void commit() throws Exception {
        try {
            this.connection.commit();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void rollback() throws Exception {
        this.connection.rollback();
    }

    public void setDBMSType(String dbType) {
        this.strDBMS = dbType;
    }

    public String getDBMSType() throws Exception {
        if(null == this.strDBMS) {
            DatabaseMetaData m = this.connection.getMetaData();
            this.strDBMS = m.getDatabaseProductName();
            this.strDBMS = this.strDBMS.toUpperCase();
            if(this.strDBMS.indexOf("MYSQL") >= 0) {
                this.strDBMS = "MYSQL";
            } else if(this.strDBMS.indexOf("ORACLE") >= 0) {
                this.strDBMS = "ORACLE";
            } else if(this.strDBMS.indexOf("ACCESS") >= 0) {
                this.strDBMS = "ACESS";
            } else if(this.strDBMS.indexOf("SQL SERVER") >= 0) {
                this.strDBMS = "SQLSERVER";
            } else if(this.strDBMS.indexOf("DB2") >= 0) {
                this.strDBMS = "DB2";
            } else if(this.strDBMS.indexOf("TERA") >= 0) {
                this.strDBMS = "TERA";
            } else if(this.strDBMS.indexOf("SYBASE") >= 0) {
                this.strDBMS = "SYBASE";
            } else {
                if(this.strDBMS.indexOf("ADAPTIVE") < 0) {
                    throw new Exception("does not support the database type!");
                }

                this.strDBMS = "SYBASE";
            }
        }

        return this.strDBMS;
    }

    public String getString(int columnIndex) throws Exception {
        String retValue = null;
        if(this.sqlResultSet == null) {
            throw new Exception("take the first " + columnIndex + "value is wrong! because the result set to a null value");
        } else {
            try {
                retValue = this.sqlResultSet.getString(columnIndex);
                this.lastNullFlag = this.sqlResultSet.wasNull();
            } catch (SQLException var4) {
                this.sqlFailure("take the first " + columnIndex + "value is wrong!", var4, 0);
            }

            if(!this.lastNullFlag && null != retValue) {
                retValue = retValue.trim();
            } else {
                retValue = "";
            }

            return this.GBToUnicode(retValue);
        }
    }

    public String getString(String columnName) throws Exception {
        String retValue = null;
        if(this.sqlResultSet == null) {
            throw new Exception("take out " + columnName + "value is wrong! because the result set to a null value");
        } else {
            try {
                retValue = this.sqlResultSet.getString(columnName);
                this.lastNullFlag = this.sqlResultSet.wasNull();
            } catch (SQLException var4) {
                this.sqlFailure("take out " + columnName + " value is wrong!", var4, 0);
            }

            if(!this.lastNullFlag && null != retValue) {
                retValue = retValue.trim();
            } else {
                retValue = "";
            }

            return this.GBToUnicode(retValue);
        }
    }

    public String getClob(int columnIndex) throws Exception {
        String strRet = null;
        Clob retValue = null;
        if(this.sqlResultSet == null) {
            throw new Exception("take the first " + columnIndex + "value is wrong! because the result set to a null value");
        } else {
            try {
                retValue = this.sqlResultSet.getClob(columnIndex);
                this.lastNullFlag = this.sqlResultSet.wasNull();
            } catch (SQLException var5) {
                this.sqlFailure("take the first " + columnIndex + "value is wrong!", var5, 0);
            }

            if(!this.lastNullFlag && null != retValue) {
                strRet = this.clobToString(retValue);
            } else {
                strRet = "";
            }

            return this.GBToUnicode(strRet);
        }
    }

    public String getClob(String columnName) throws Exception {
        String strRet = null;
        Clob retValue = null;
        if(this.sqlResultSet == null) {
            throw new Exception("take out " + columnName + "value is wrong! because the result set to a null value");
        } else {
            try {
                retValue = this.sqlResultSet.getClob(columnName);
                this.lastNullFlag = this.sqlResultSet.wasNull();
            } catch (SQLException var5) {
                this.sqlFailure("take out " + columnName + "value is wrong!", var5, 0);
            }

            if(!this.lastNullFlag && null != retValue) {
                strRet = this.clobToString(retValue);
            } else {
                strRet = "";
            }

            return this.GBToUnicode(strRet);
        }
    }

    private String clobToString(Clob clob) throws Exception {
        StringBuffer content = new StringBuffer();

        try {
            String e = "";
            BufferedReader br = new BufferedReader(clob.getCharacterStream());

            while((e = br.readLine()) != null) {
                content.append(e);
            }

            return content.toString();
        } catch (Exception var5) {
            throw new Exception("-------get value failed--------");
        }
    }

    public int getInt(int columnIndex) throws Exception {
        int retValue = -1;
        if(this.sqlResultSet == null) {
            throw new Exception("take the first " + columnIndex + "value is wrong! because the result set to a null value");
        } else {
            try {
                retValue = this.sqlResultSet.getInt(columnIndex);
                this.lastNullFlag = this.sqlResultSet.wasNull();
            } catch (SQLException var4) {
                this.sqlFailure("take the first " + columnIndex + " value is wrong!", var4, 0);
            }

            return retValue;
        }
    }

    public int getInt(String columnName) throws Exception {
        if(this.sqlResultSet == null) {
            throw new Exception("take out " + columnName + "value is wrong! because the result set to a null value");
        } else {
            try {
                int retValue = this.sqlResultSet.getInt(columnName);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                }

                return retValue;
            } catch (SQLException var4) {
                this.sqlFailure("take out " + columnName + " value error!", var4, 0);
                return 0;
            }
        }
    }

    public Integer getInteger(int columnIndex) throws Exception {
        Integer retValue = null;
        if(this.sqlResultSet == null) {
            throw new Exception("take the first " + columnIndex + "value is wrong! because the result set to a null value");
        } else {
            try {
                int e = this.sqlResultSet.getInt(columnIndex);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                    retValue = null;
                } else {
                    retValue = new Integer(e);
                }
            } catch (SQLException var4) {
                this.sqlFailure("take the first " + columnIndex + " value error!", var4, 0);
            }

            return retValue;
        }
    }

    public Integer getInteger(String columnName) throws Exception {
        Integer retValue = null;
        if(this.sqlResultSet == null) {
            throw new Exception("take out " + columnName + "value is wrong! because the result set to a null value");
        } else {
            try {
                int e = this.sqlResultSet.getInt(columnName);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                    retValue = null;
                } else {
                    retValue = new Integer(e);
                }
            } catch (SQLException var4) {
                this.sqlFailure("take out " + columnName + " value error!", var4, 0);
            }

            return retValue;
        }
    }

    public long getLong(int columnIndex) throws Exception {
        long retValue = 0L;
        if(this.sqlResultSet == null) {
            throw new Exception("take the first " + columnIndex + "value is wrong! because the result set to a null value");
        } else {
            try {
                retValue = this.sqlResultSet.getLong(columnIndex);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                }

                return retValue;
            } catch (SQLException var5) {
                this.sqlFailure("take the first " + columnIndex + " value error!", var5, 0);
                return 0L;
            }
        }
    }

    public long getLong(String columnName) throws Exception {
        if(this.sqlResultSet == null) {
            throw new Exception("take out " + columnName + "value is wrong! because the result set to a null value");
        } else {
            try {
                long retValue = this.sqlResultSet.getLong(columnName);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                }

                return retValue;
            } catch (SQLException var5) {
                this.sqlFailure("take out " + columnName + " value error!", var5, 0);
                return 0L;
            }
        }
    }

    public double getDouble(int columnIndex) throws Exception {
        double retValue = 0.0D;
        if(this.sqlResultSet == null) {
            throw new Exception("take the first " + columnIndex + "value is wrong! because the result set to a null value");
        } else {
            try {
                retValue = this.sqlResultSet.getDouble(columnIndex);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                }
            } catch (SQLException var5) {
                this.sqlFailure("take the first " + columnIndex + " value error!", var5, 0);
            }

            return retValue;
        }
    }

    public double getDouble(String columnName) throws Exception {
        if(this.sqlResultSet == null) {
            throw new Exception("take out " + columnName + "value is wrong! because the result set to a null value");
        } else {
            try {
                double retValue = this.sqlResultSet.getDouble(columnName);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                }

                return retValue;
            } catch (SQLException var5) {
                this.sqlFailure("take out " + columnName + " value error!", var5, 0);
                return 0.0D;
            }
        }
    }

    public boolean getBoolean(int columnIndex) throws Exception {
        boolean retValue = false;
        if(this.sqlResultSet == null) {
            throw new Exception("take the first " + columnIndex + "value is wrong! because the result set to a null value");
        } else {
            try {
                retValue = this.sqlResultSet.getBoolean(columnIndex);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                }
            } catch (SQLException var4) {
                this.sqlFailure("take the first " + columnIndex + " value error!", var4, 0);
            }

            return retValue;
        }
    }

    public boolean getBoolean(String columnName) throws Exception {
        boolean retValue = false;
        if(this.sqlResultSet == null) {
            throw new Exception("take out " + columnName + "value is wrong! because the result set to a null value");
        } else {
            try {
                retValue = this.sqlResultSet.getBoolean(columnName);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                }
            } catch (SQLException var4) {
                this.sqlFailure("take out " + columnName + " value error!", var4, 0);
            }

            return retValue;
        }
    }

    public Date getDate(int columnIndex) throws Exception {
        Date retValue = null;
        if(this.sqlResultSet == null) {
            throw new Exception("take the first " + columnIndex + "value is wrong! because the result set to a null value");
        } else {
            try {
                Timestamp e = this.sqlResultSet.getTimestamp(columnIndex);
                if(e != null) {
                    retValue = new Date(e.getTime());
                } else {
                    retValue = null;
                }

                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    retValue = null;
                    this.lastNullFlag = true;
                }
            } catch (SQLException var4) {
                this.sqlFailure("take the first " + columnIndex + " value error!", var4, 0);
            }

            return retValue;
        }
    }

    public Date getDate(String columnName) throws Exception {
        Date retValue = null;
        if(this.sqlResultSet == null) {
            throw new Exception("take out " + columnName + "value is wrong! because the result set to a null value");
        } else {
            try {
                Timestamp e = this.sqlResultSet.getTimestamp(columnName);
                if(e != null) {
                    retValue = new Date(e.getTime());
                } else {
                    retValue = null;
                }

                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                }
            } catch (SQLException var4) {
                this.sqlFailure("take out " + columnName + " value error!", var4, 0);
            }

            return retValue;
        }
    }

    public Timestamp getTimestamp(int columnIndex) throws Exception {
        Timestamp retValue = null;
        if(this.sqlResultSet == null) {
            throw new Exception("take the first " + columnIndex + "value is wrong! because the result set to a null value");
        } else {
            try {
                retValue = this.sqlResultSet.getTimestamp(columnIndex);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    retValue = null;
                    this.lastNullFlag = true;
                }
            } catch (SQLException var4) {
                this.sqlFailure("take the first " + columnIndex + " value error!", var4, 0);
            }

            return retValue;
        }
    }

    public Timestamp getTimestamp(String columnName) throws Exception {
        Timestamp retValue = null;
        if(this.sqlResultSet == null) {
            throw new Exception("take out " + columnName + "value is wrong! because the result set to a null value");
        } else {
            try {
                retValue = this.sqlResultSet.getTimestamp(columnName);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    retValue = null;
                    this.lastNullFlag = true;
                }
            } catch (SQLException var4) {
                this.sqlFailure("take the first " + columnName + " value error!", var4, 0);
            }

            return retValue;
        }
    }

    public float getFloat(int columnIndex) throws Exception {
        if(this.sqlResultSet == null) {
            throw new Exception("take the first " + columnIndex + "value is wrong! because the result set to a null value");
        } else {
            try {
                float retValue = this.sqlResultSet.getFloat(columnIndex);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                }

                return retValue;
            } catch (SQLException var4) {
                this.sqlFailure("take the first " + columnIndex + " value error!", var4, 0);
                return 0.0F;
            }
        }
    }

    public float getFloat(String columnName) throws Exception {
        if(this.sqlResultSet == null) {
            throw new Exception("take out " + columnName + "value is wrong! because the result set to a null value");
        } else {
            try {
                float retValue = this.sqlResultSet.getFloat(columnName);
                this.lastNullFlag = false;
                if(this.sqlResultSet.wasNull()) {
                    this.lastNullFlag = true;
                }

                return retValue;
            } catch (SQLException var4) {
                this.sqlFailure("take out " + columnName + " value error!!", var4, 0);
                return 0.0F;
            }
        }
    }

    public BigDecimal getBigDecimal(int columnIndex) throws Exception {
        if(this.sqlResultSet == null) {
            throw new Exception("take the first " + columnIndex + "value is wrong! because the result set to a null value");
        } else {
            BigDecimal retValue = this.sqlResultSet.getBigDecimal(columnIndex);
            this.lastNullFlag = false;
            if(this.sqlResultSet.wasNull()) {
                retValue = null;
                this.lastNullFlag = true;
            }

            return retValue;
        }
    }

    public BigDecimal getBigDecimal(String columnName) throws Exception {
        if(this.sqlResultSet == null) {
            throw new Exception("take out " + columnName + "value is wrong! because the result set to a null value");
        } else {
            BigDecimal retValue = this.sqlResultSet.getBigDecimal(columnName);
            this.lastNullFlag = false;
            if(this.sqlResultSet.wasNull()) {
                retValue = null;
                this.lastNullFlag = true;
            }

            return retValue;
        }
    }

    /** @deprecated */
    public String[][] getMatrix() {
        if(this.sqlResultSet == null) {
            return (String[][])((String[][])null);
        } else {
            boolean colnum = false;
            Vector table = new Vector();

            int rowl;
            int var6;
            try {
                var6 = this.sqlResultSet.getMetaData().getColumnCount();

                while(this.next()) {
                    String[] matrix = new String[var6];

                    for(rowl = 0; rowl < var6; ++rowl) {
                        matrix[rowl] = this.getString(rowl + 1);
                    }

                    table.addElement(matrix);
                }
            } catch (Exception var5) {
                System.err.println("Execute err : " + var5.getMessage());
                return (String[][])((String[][])null);
            }

            String[][] var7 = new String[table.size() + 1][var6];

            for(rowl = 0; rowl < var6; ++rowl) {
                var7[0][rowl] = "Filed" + rowl;
            }

            for(rowl = 1; rowl < var7.length; ++rowl) {
                var7[rowl] = (String[])((String[])((String[])table.elementAt(rowl - 1)));
            }

            return var7;
        }
    }

    public String getSql2TimeStamp(String strDateStr, String strH, String strM, String strS) throws Exception {
        if(null != strDateStr && strDateStr.length() >= 1) {
            if(strDateStr.indexOf("0000-00-00") >= 0) {
                return "null";
            } else {
                String strType = this.getDBMSType();
                String strRet = "";
                if(strType.equalsIgnoreCase("MYSQL")) {
                    strRet = "\'" + strDateStr + " " + strH + ":" + strM + ":" + strS + "\'";
                } else if(strType.equalsIgnoreCase("DB2")) {
                    strRet = "timestamp(\'" + strDateStr + " " + strH + ":" + strM + ":" + strS + "\')";
                } else if(strType.equalsIgnoreCase("ORACLE")) {
                    strRet = "to_date(\'" + strDateStr + " " + strH + ":" + strM + ":" + strS + "\',\'YYYY-mm-dd hh24:mi:ss\')";
                } else if(strType.equalsIgnoreCase("ACESS")) {
                    strRet = "\'" + strDateStr + "\'";
                } else if(strType.equalsIgnoreCase("SQLSERVER")) {
                    strRet = "CONVERT(Datetime,\'" + strDateStr + " " + strH + ":" + strM + ":" + strS + "\',20)";
                } else if(strType.equalsIgnoreCase("TERA")) {
                    strRet = strDateStr + " (FORMAT \'YYYY-MM-DD\')";
                } else {
                    if(!strType.equalsIgnoreCase("SYBASE")) {
                        throw new Exception("can\'t get the current date of the function definition");
                    }

                    strRet = "cast(\'" + strDateStr + " " + strH + ":" + strM + ":" + strS + "\' as Datetime)";
                }

                return strRet;
            }
        } else {
            return null;
        }
    }

    public String getSql2TimeStamp(Date date) throws Exception {
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dFormat2 = new SimpleDateFormat("HH");
        SimpleDateFormat dFormat3 = new SimpleDateFormat("mm");
        SimpleDateFormat dFormat4 = new SimpleDateFormat("ss");
        return this.getSql2TimeStamp(dFormat.format(date), dFormat2.format(date), dFormat3.format(date), dFormat4.format(date));
    }

    public String getSql2Date(String strDateStr, String splitStr) throws Exception {
        if(null != strDateStr && strDateStr.length() >= 1) {
            if(strDateStr.indexOf("0000-00-00") >= 0) {
                return "null";
            } else {
                String strType = this.getDBMSType();
                String strRet = "";
                int i = strDateStr.indexOf(" ");
                if(i > 0) {
                    strDateStr = strDateStr.substring(0, i);
                }

                if(strType.equalsIgnoreCase("MYSQL")) {
                    strRet = "\'" + strDateStr + "\'";
                } else if(strType.equalsIgnoreCase("DB2")) {
                    strRet = "date(\'" + strDateStr + "\')";
                } else if(strType.equalsIgnoreCase("ORACLE")) {
                    if("-".equals(splitStr)) {
                        strRet = "to_date(\'" + strDateStr + "\',\'YYYY-mm-dd\')";
                    } else {
                        strRet = "to_date(\'" + strDateStr + "\',\'YYYY/mm/dd\')";
                    }
                } else if(strType.equalsIgnoreCase("ACESS")) {
                    strRet = "\'" + strDateStr + "\'";
                } else if(strType.equalsIgnoreCase("SQLSERVER")) {
                    if("-".equals(splitStr)) {
                        strRet = "convert(varchar(10), convert(datetime,\'" + strDateStr + "\'), 111)";
                    } else {
                        strRet = "CONVERT(Datetime,\'" + strDateStr + "\',20)";
                    }
                } else if(strType.equalsIgnoreCase("TERA")) {
                    if("-".equals(splitStr)) {
                        strRet = "cast(\'" + strDateStr + "\' as date FORMAT \'YYYY-MM-DD\')";
                    } else {
                        strRet = "cast(\'" + strDateStr + "\' as date FORMAT \'YYYY/MM/DD\')";
                    }
                } else {
                    if(!strType.equalsIgnoreCase("SYBASE")) {
                        throw new Exception("can\'t get the current date of the function definition");
                    }

                    strRet = "cast(\'" + strDateStr + "\' as Date)";
                }

                return strRet;
            }
        } else {
            return null;
        }
    }

    public String getSql2DateYYYYMMDD(String strDateStr) throws Exception {
        if(null != strDateStr && strDateStr.length() >= 1) {
            if(strDateStr.indexOf("000000") >= 0) {
                return "null";
            } else {
                String strType = this.getDBMSType();
                String strRet = "";
                int i = strDateStr.indexOf(" ");
                if(i > 0) {
                    strDateStr = strDateStr.substring(0, i);
                }

                if(strType.equalsIgnoreCase("MYSQL")) {
                    strRet = "\'" + strDateStr + "\'";
                } else if(strType.equalsIgnoreCase("DB2")) {
                    strRet = "date(\'" + strDateStr + "\')";
                } else if(strType.equalsIgnoreCase("ORACLE")) {
                    strRet = "to_date(\'" + strDateStr + "\',\'YYYYmmdd\')";
                } else if(strType.equalsIgnoreCase("ACESS")) {
                    strRet = "\'" + strDateStr + "\'";
                } else if(strType.equalsIgnoreCase("SQLSERVER")) {
                    strRet = "convert(varchar, convert(datetime, \'" + strDateStr + "\'), 112)";
                } else if(strType.equalsIgnoreCase("TERA")) {
                    strRet = strDateStr + " (FORMAT \'YYYYMMDD\')";
                } else {
                    if(!strType.equalsIgnoreCase("SYBASE")) {
                        throw new Exception("can\'t get the current date of the function definition");
                    }

                    strRet = "cast(\'" + strDateStr + "\' as Date)";
                }

                return strRet;
            }
        } else {
            return null;
        }
    }

    public String getSql2ColumnName(String colName) throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = "Date_Format(" + colName + ",\'%Y-%m-%d %H:%i:%s\')";
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "to_char(" + colName + ",\'YYYY-mm-dd hh24:mi:ss\')";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "ts_fmt(" + colName + ",\'yyyy-mm-dd hh:mi:ss\')";
        } else if(strType.equalsIgnoreCase("ACESS")) {
            strRet = colName;
        } else if(strType.equalsIgnoreCase("SQLSERVER")) {
            strRet = "CONVERT(Varchar," + colName + ",120)";
        } else if(strType.equalsIgnoreCase("TERA")) {
            strRet = colName + " (FORMAT \'YYYY-MM-DD\')";
        } else {
            if(!strType.equalsIgnoreCase("SYBASE")) {
                throw new Exception("can\'t get the current date of the function definition");
            }

            strRet = "convert(char(10)," + colName + ",23) || \' \' || convert(char(8)," + colName + ",108)";
        }

        return strRet;
    }

    public String getSql2DateTimeNow() throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = "now()";
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "sysdate";
        } else if(strType.equalsIgnoreCase("ACESS")) {
            strRet = "date()+time()";
        } else if(strType.equalsIgnoreCase("SQLSERVER")) {
            strRet = "getdate()";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "current timestamp";
        } else if(strType.equalsIgnoreCase("TERA")) {
            strRet = "cast((date (format \'yyyy-mm-dd\' )) as char(10)) ||\' \'|| time";
        } else {
            if(!strType.equalsIgnoreCase("SYBASE")) {
                throw new Exception("can\'t get the current date of the function definition");
            }

            strRet = "getdate()";
        }

        return strRet;
    }

    public String getSql2DateNow() throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = "curdate()";
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "sysdate";
        } else if(strType.equalsIgnoreCase("ACESS")) {
            strRet = "date()";
        } else if(strType.equalsIgnoreCase("SQLSERVER")) {
            strRet = "getdate()";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "current date";
        } else if(strType.equalsIgnoreCase("TERA")) {
            strRet = "cast((date (format \'yyyy-mm-dd\' )) as char(10)) ||\' \'|| time";
        } else {
            if(!strType.equalsIgnoreCase("SYBASE")) {
                throw new Exception("can\'t get the current date of the function definition");
            }

            strRet = "getdate()";
        }

        return strRet;
    }

    public String escapeString(String str) throws Exception {
        if(str != null && str.length() >= 1) {
            String strType = this.getDBMSType();
            String strRet = "";

            for(int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);
                if(c == 39) {
                    if(strType.equalsIgnoreCase("MYSQL")) {
                        strRet = strRet + "\\\'";
                    } else if(!strType.equalsIgnoreCase("ORACLE") && !strType.equalsIgnoreCase("DB2") && !strType.equalsIgnoreCase("TERA") && !strType.equalsIgnoreCase("SYBASE")) {
                        strRet = strRet + c;
                    } else {
                        strRet = strRet + "\'\'";
                    }
                } else if(c == 34) {
                    if(strType.equalsIgnoreCase("MYSQL")) {
                        strRet = strRet + "\\\"";
                    } else if(!strType.equalsIgnoreCase("ORACLE") && !strType.equalsIgnoreCase("DB2") && !strType.equalsIgnoreCase("TERA") && !strType.equalsIgnoreCase("SYBASE")) {
                        strRet = strRet + c;
                    } else {
                        strRet = strRet + "\"";
                    }
                } else if(c == 92) {
                    if(strType.equalsIgnoreCase("MYSQL")) {
                        strRet = strRet + "\\\\";
                    } else {
                        strRet = strRet + c;
                    }
                } else {
                    strRet = strRet + c;
                }
            }

            return strRet;
        } else {
            return "";
        }
    }

    public String getSqlLimit(String strSql, int limitnum) throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = strSql + " limit " + limitnum;
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            ++limitnum;
            strRet = "select * from (" + strSql + ") where ROWNUM<" + limitnum;
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = strSql + "fetch first " + limitnum + " rows only";
        } else if(strType.equalsIgnoreCase("SYBASE")) {
            strRet = "select top " + limitnum + " * from(" + strSql + ") a";
        } else if(strType.equalsIgnoreCase("SQLSERVER")) {
            strRet = "select top " + limitnum + " * from(" + strSql + ") a";
        } else {
            if(!"TERA".equalsIgnoreCase(strType)) {
                throw new Exception("function definition can not be achieved");
            }

            StringBuffer buffer = new StringBuffer(strSql.length() + 100);
            buffer.append(strSql);
            int orderByIndex = buffer.toString().toLowerCase().lastIndexOf("order by");
            if(orderByIndex > 0) {
                String orderBy = buffer.substring(orderByIndex);
                buffer.insert(orderByIndex, " QUALIFY row_number() OVER( ");
                buffer.append(" ) ");
                buffer.append(" between 1 and " + limitnum);
                buffer.append(orderBy);
            } else {
                buffer.append(" QUALIFY sum(1) over (rows unbounded preceding) between 1 and " + limitnum);
            }

            strRet = buffer.toString();
        }

        return strRet;
    }

    public static String getCountTotalSql(String table, String strPrimaryKey, String condition, String tail) {
        if(table == null) {
            return "";
        } else {
            String sql = "select count(" + strPrimaryKey + ") from ";
            sql = sql + table + "    ";
            if(condition != null && !condition.equals("")) {
                sql = sql + "where " + condition;
            }

            if(tail != null && !tail.equals("")) {
                sql = sql + tail;
            }

            return sql;
        }
    }

    public static String getCountTotalSql(String sql, String primaryKey) {
        StringBuffer countSQL = new StringBuffer();
        if(primaryKey == null || primaryKey.length() < 1) {
            primaryKey = "*";
        }

        countSQL.append("select count(" + primaryKey + ") from ( ");
        countSQL.append(sql);
        countSQL.append(" ) tt");
        return countSQL.toString();
    }

    public String getPagedSql(String sql, String column, String strPrimaryKey, int curpage, int pagesize) throws Exception {
        String strDBType = this.getDBMSType();
        StringBuffer buffer = null;
        buffer = new StringBuffer();
        int orderByIndex;
        if("ORACLE".equalsIgnoreCase(strDBType)) {
            buffer.append("select * from ( ");
            buffer.append("select ").append(column).append(" rownum as my_rownum from( ");
            buffer.append(sql).append(") ");
            orderByIndex = pagesize * curpage + pagesize;
            buffer.append("where rownum <= " + orderByIndex + ") a ");
            buffer.append("where a.my_rownum > " + pagesize * curpage);
        } else if("DB2".equalsIgnoreCase(strDBType)) {
            buffer.append("select * from ( ");
            buffer.append("select ").append(column).append("  rownumber() over (order by " + strPrimaryKey + ") as my_rownum from( ");
            buffer.append(sql).append(") as temp ");
            buffer.append("fetch first " + (pagesize * curpage + pagesize) + " rows only) as a ");
            buffer.append("where a.my_rownum > " + pagesize * curpage);
        } else if("TERA".equalsIgnoreCase(strDBType)) {
            buffer.append(sql);
            orderByIndex = buffer.toString().toLowerCase().lastIndexOf("order by");
            if(orderByIndex > 0) {
                String orderBy = buffer.substring(orderByIndex);
                buffer.insert(orderByIndex, " QUALIFY row_number() OVER( ");
                buffer.append(" ) ");
                buffer.append(" between " + pagesize * curpage + " and " + (pagesize * curpage + pagesize));
                buffer.append(orderBy);
            } else {
                buffer.append(" QUALIFY sum(1) over (rows unbounded preceding) between " + pagesize * curpage + " and " + (pagesize * curpage + pagesize));
            }
        }

        return buffer.toString();
    }

    public String getSqlSubString(String strColName, int pos, int len) throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(!strType.equalsIgnoreCase("MYSQL") && !strType.equalsIgnoreCase("SYBASE")) {
            if(!strType.equalsIgnoreCase("ORACLE") && !strType.equalsIgnoreCase("DB2")) {
                if(!strType.equalsIgnoreCase("TERA")) {
                    throw new Exception("function definition can not be achieved");
                }

                if(len == -1) {
                    strRet = "substring(" + strColName + " form " + pos + ")";
                } else {
                    strRet = "substring(" + strColName + " from " + pos + " for " + len + ")";
                }
            } else if(len == -1) {
                strRet = "substr(" + strColName + "," + pos + ")";
            } else {
                strRet = "substr(" + strColName + "," + pos + "," + len + ")";
            }
        } else if(len == -1) {
            strRet = "substring(" + strColName + "," + pos + ")";
        } else {
            strRet = "substring(" + strColName + "," + pos + "," + len + ")";
        }

        return strRet;
    }

    public String getSqlSubDate(String interv) throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = "SUBDATE(now(),INTERVAL " + interv + " minute)";
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "(sysdate-" + interv + "/(24*60))";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "(current timestamp-" + interv + " minute)";
        } else {
            if(!strType.equalsIgnoreCase("SYBASE")) {
                throw new Exception("can\'t get the current date of the function definition");
            }

            strRet = "dateadd(mi,-" + interv + ",getdate())";
        }

        return strRet;
    }

    public String getSqlAddDate(String interv, String unit) throws Exception {
        String strRet = "";
        String strType = this.getDBMSType();
        unit = unit.trim().toUpperCase();
        interv = interv.trim();
        if(!interv.startsWith("-")) {
            interv = "+" + interv;
        }

        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = "ADDDATE(now(),INTERVAL " + interv + " " + unit + ")";
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            if(unit.compareTo("MINUTE") == 0) {
                strRet = "(sysdate" + interv + "/(24*60))";
            } else if(unit.compareTo("SECOND") == 0) {
                strRet = "(sysdate" + interv + "/(24*60*60))";
            } else if(unit.compareTo("HOUR") == 0) {
                strRet = "(sysdate" + interv + "/24)";
            } else if(unit.compareTo("DAY") == 0) {
                strRet = "(sysdate" + interv + ")";
            } else if(unit.compareTo("MONTH") == 0) {
                strRet = "(add_months(sysdate," + interv + "))";
            } else if(unit.compareTo("YEAR") == 0) {
                strRet = "(add_months(sysdate,(" + interv + "*12)))";
            }
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "(current timestamp " + interv + " " + unit + ")";
        } else {
            if(!strType.equalsIgnoreCase("SYBASE")) {
                throw new Exception("can\'t get the current date of the function definition");
            }

            if(unit.compareTo("MINUTE") == 0) {
                strRet = "dateadd(mi," + interv + ",getdate())";
            } else if(unit.compareTo("SECOND") == 0) {
                strRet = "dateadd(ss," + interv + ",getdate())";
            } else if(unit.compareTo("HOUR") == 0) {
                strRet = "dateadd(hh," + interv + ",getdate())";
            } else if(unit.compareTo("DAY") == 0) {
                strRet = "dateadd(dd," + interv + ",getdate())";
            } else if(unit.compareTo("MONTH") == 0) {
                strRet = "dateadd(mm," + interv + ",getdate())";
            } else if(unit.compareTo("YEAR") == 0) {
                strRet = "dateadd(yy," + interv + ",getdate())";
            }
        }

        return strRet;
    }

    public String getSqlDateAddMonth(String monthNum) throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = "DATE_ADD(curdate(),INTERVAL " + monthNum + " month)";
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "to_char(add_months(sysdate," + monthNum + "),\'YYYY-mm-dd\')";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "char((current date + " + monthNum + " month))";
        } else if(strType.equalsIgnoreCase("SYBASE")) {
            strRet = "dateadd(mm," + monthNum + ",getdate())";
        } else {
            if(!strType.equalsIgnoreCase("TERA")) {
                throw new Exception("can\'t get the current date of the function definition");
            }

            strRet = "add_months(date," + monthNum + ")";
        }

        return strRet;
    }

    public String getSqlEncrypt(String strPwd) throws Exception {
        StringBuffer strRet = new StringBuffer();
        strRet.append("\'");
        strRet.append(DES.encrypt(strPwd));
        strRet.append("\'");
        return strRet.toString();
    }

    public String getSql_intTochar(String strColName) throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = strColName;
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "cast(" + strColName + " as varchar2(32))";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "rtrim(char(" + strColName + "))";
        } else if(strType.equalsIgnoreCase("SYBASE")) {
            strRet = "convert(char," + strColName + ")";
        } else if(strType.equalsIgnoreCase("SQLSERVER")) {
            strRet = "cast(" + strColName + " as varchar(12))";
        } else {
            if(!strType.equalsIgnoreCase("TERA")) {
                throw new Exception("function definition can not be achieved");
            }

            strRet = "cast(" + strColName + " as varchar(12))";
        }

        return strRet;
    }

    public String getSql_charToint(String strColName) throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = strColName;
        } else if(!strType.equalsIgnoreCase("ORACLE") && !strType.equalsIgnoreCase("TERA")) {
            if(strType.equalsIgnoreCase("DB2")) {
                strRet = "int(" + strColName + ")";
            } else if(strType.equalsIgnoreCase("SYBASE")) {
                strRet = "convert(int," + strColName + ")";
            } else {
                if(!strType.equalsIgnoreCase("SQLSERVER")) {
                    throw new Exception("function definition can not be achieved");
                }

                strRet = "cast(" + strColName + " as integer)";
            }
        } else {
            strRet = "cast(" + strColName + " as integer)";
        }

        return strRet;
    }

    public String getSql_charToDouble(String strColName) throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = strColName;
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "cast(" + strColName + " as numeric)";
        } else {
            if(!strType.equalsIgnoreCase("DB2")) {
                throw new Exception("function definition can not be achieved");
            }

            strRet = "double(" + strColName + ")";
        }

        return strRet;
    }

    public String getSqlRound(String str1, String str2) throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("TERA")) {
            strRet = " cast ((" + str1 + ") as decimal(10," + str2 + ")) ";
        } else {
            strRet = " round(" + str1 + "," + str2 + ") ";
        }

        return strRet;
    }

    public String getSqlNotEqual() throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("TERA")) {
            strRet = "<>";
        } else {
            strRet = "!=";
        }

        return strRet;
    }

    public String getSqlNvl(String str1, String str2) throws Exception {
        String strType = this.getDBMSType();
        String strRet = "";
        if(strType.equalsIgnoreCase("DB2")) {
            strRet = "value(" + str1 + "," + str2 + ")";
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "nvl(" + str1 + "," + str2 + ")";
        } else if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = "ifnull(" + str1 + "," + str2 + ")";
        } else if(strType.equalsIgnoreCase("SYBASE")) {
            strRet = "isnull(" + str1 + "," + str2 + ")";
        } else if(strType.equalsIgnoreCase("SQLSERVER")) {
            strRet = "isnull(" + str1 + "," + str2 + ")";
        } else {
            if(!strType.equalsIgnoreCase("TERA")) {
                throw new Exception("function definition can not be achieved");
            }

            strRet = "COALESCE(" + str1 + "," + str2 + ")";
        }

        return strRet;
    }

    public String getCreateAsTableSql(String newtable, String templettable) throws Exception {
        String ss = "";
        String strDBType = this.getDBMSType();
        if("ORACLE".equalsIgnoreCase(strDBType)) {
            ss = "create table " + newtable + " as select * from " + templettable + " where 1=2";
        } else if("DB2".equalsIgnoreCase(strDBType)) {
            ss = "create table " + newtable + " like " + templettable;
        } else if("TERA".equalsIgnoreCase(strDBType)) {
            ss = "create table " + newtable + " as " + templettable + " with no data";
        }

        return ss;
    }

    public String getCreateAsTableSql(String newtable, String templettable, String tableSpace) throws Exception {
        String ss = this.getCreateAsTableSql(newtable, templettable);
        if(tableSpace != null && tableSpace.length() >= 1) {
            String strDBType = this.getDBMSType();
            if("ORACLE".equalsIgnoreCase(strDBType)) {
                ss = ss.replaceAll(newtable, newtable + " tablespace " + tableSpace);
            } else if("DB2".equalsIgnoreCase(strDBType)) {
                ss = ss + " in " + tableSpace;
            }

            return ss;
        } else {
            return ss;
        }
    }

    public String getCreateTableInTableSpaceSql(String tableDDLSql, String tableSpace) throws Exception {
        if(tableSpace != null && tableSpace.length() >= 1) {
            String strDBType = this.getDBMSType();
            if("ORACLE".equalsIgnoreCase(strDBType)) {
                tableDDLSql = tableDDLSql + " tablespace " + tableSpace;
            } else if("DB2".equalsIgnoreCase(strDBType)) {
                tableDDLSql = tableDDLSql + " in " + tableSpace;
            }

            return tableDDLSql;
        } else {
            return tableDDLSql;
        }
    }

    public String getCreateIndexInTableSpaceSql(String createIndexSql, String tableSpace) throws Exception {
        if(tableSpace != null && tableSpace.length() >= 1) {
            String strDBType = this.getDBMSType();
            if("ORACLE".equalsIgnoreCase(strDBType)) {
                createIndexSql = createIndexSql + " using index tablespace " + tableSpace;
            } else if(!"DB2".equalsIgnoreCase(strDBType)) {
                ;
            }

            return createIndexSql;
        } else {
            return createIndexSql;
        }
    }

    public String getCheckTableIsExistSql(String tableName) throws Exception {
        String strSql = "";
        String strDBType = this.getDBMSType();
        if(strDBType.equals("DB2")) {
            strSql = "select count(*) from syscat.tables where tabname=\'" + tableName.toUpperCase() + "\'";
        } else if(strDBType.equals("ORACLE")) {
            strSql = "select count(*) from TAB where tname=\'" + tableName.toUpperCase() + "\'";
        } else if(strDBType.equals("TERA")) {
            strSql = "select count(*) from dbc.tables where tablename=\'" + tableName.toUpperCase() + "\'";
        }

        return strSql;
    }

    public String getSql_dateTochar(String strColName, String mask) throws Exception {
        String strRet = "";
        strRet = "substr(" + this.getSql2ColumnName(strColName) + ",1," + mask.trim().length() + ")";
        return strRet;
    }

    public String getSqlOptimizeStart(String tablename, String cpuParallelSize) throws Exception {
        String strRet = "";
        String strDBType = this.getDBMSType();
        if(strDBType.equalsIgnoreCase("ORACLE")) {
            strRet = "select /*+ parallel( " + tablename + " ," + cpuParallelSize + ") */ ";
        } else {
            strRet = "select ";
        }

        return strRet;
    }

    public String getSqlOptimizeEnd() throws Exception {
        String strRet = "";
        String strDBType = this.getDBMSType();
        if(strDBType.equalsIgnoreCase("ORACLE")) {
            strRet = ",row_number() over( order by 100) ";
        }

        return strRet;
    }

    public String getSqlNolog(String tablename) throws Exception {
        String strRet = "";
        String strDBType = this.getDBMSType();
        if(strDBType.equalsIgnoreCase("ORACLE")) {
            strRet = "alter table " + tablename + " nologging";
        }

        return strRet;
    }

    public String getSqlOptimizeInsert() throws Exception {
        String strRet = "";
        String strDBType = this.getDBMSType();
        if(strDBType.equalsIgnoreCase("ORACLE")) {
            strRet = "insert /*+append*/ into ";
        } else {
            strRet = "insert into ";
        }

        return strRet;
    }

    public String getCreateTableSql(String dataspace, String db2schemasuff, String dataspaceindex, String partitionKey) throws Exception {
        String strRet = "";
        String dbType = this.getDBMSType();
        if(dbType.equalsIgnoreCase("ORACLE")) {
            strRet = " tablespace  " + dataspace;
        } else if(dbType.equalsIgnoreCase("DB2")) {
            strRet = db2schemasuff + dataspace + dataspaceindex;
            if(null != partitionKey && !"".equals(partitionKey)) {
                strRet = strRet + " PARTITIONING KEY (" + partitionKey + ") USING HASHING";
            }

            strRet = strRet + " NOT LOGGED INITIALLY ";
        }

        return strRet;
    }

    public String getSqlIsNull(String strColName, boolean isNull) throws Exception {
        String strRet = "";
        String dbType = this.getDBMSType();
        if(isNull) {
            strRet = strColName + " is null";
            if(dbType.equalsIgnoreCase("DB2")) {
                strRet = strRet + " or " + strColName + "=\'\'";
            }
        } else {
            strRet = strColName + " is not null";
            if(dbType.equalsIgnoreCase("DB2")) {
                strRet = strRet + " and not " + strColName + "=\'\'";
            }
        }

        return strRet;
    }

    public String getSqlSystables() throws Exception {
        String strRet = "select * from(";
        String dbType = this.getDBMSType();
        if(dbType.equalsIgnoreCase("ORACLE")) {
            strRet = strRet + "select owner tabschema,table_name tabname,comments remarks From All_Tab_Comments";
            strRet = strRet + ") where 1=1";
            strRet = strRet + " and tabschema not like \'SYS%\'";
        } else if(dbType.equalsIgnoreCase("DB2")) {
            strRet = strRet + "select tabschema,tabname,remarks from syscat.tables";
            strRet = strRet + ")as ta where 1=1";
            strRet = strRet + " and tabschema not like \'SYSIBM%\'";
        } else if(dbType.equalsIgnoreCase("MYSQL")) {
            strRet = strRet + "select table_schema tabschema,table_name tabname,table_comment remarks from information_schema.tables";
            strRet = strRet + ")as ta where 1=1";
            strRet = strRet + " and tabschema=\'" + this.getDatabaseName() + "\'";
        }

        return strRet;
    }

    public String getDatabaseName() throws Exception {
        String dbname = "";
        String dbType = this.getDBMSType();
        String url = this.connection.getMetaData().getURL();
        if(dbType.equalsIgnoreCase("MYSQL")) {
            int s = url.indexOf("/", 13) + 1;
            int e = url.indexOf("?");
            dbname = url.substring(s, e).toLowerCase();
            log.debug(dbname);
        }

        return dbname;
    }

    public String getSqlConcat(String strColNames) throws Exception {
        String strRet = "";
        String dbType = this.getDBMSType();
        String[] names = strColNames.split(",");
        if(dbType.equalsIgnoreCase("MYSQL")) {
            for(int i = 0; i < names.length; ++i) {
                if(i == 0) {
                    strRet = names[0];
                } else {
                    strRet = "CONCAT(" + strRet + "," + names[i] + ")";
                }
            }
        } else if(dbType.equalsIgnoreCase("DB2")) {
            strRet = strColNames.replaceAll(",", "||");
        } else if(dbType.equalsIgnoreCase("ORACLE")) {
            strRet = strColNames.replaceAll(",", "||");
        }

        return strRet;
    }
}

package com.ailk.biapp.ci.util;

import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.io.BufferedReader;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

public class DataBaseAdapter {
    private static Logger log = Logger.getLogger(DataBaseAdapter.class);
    public static final String DATA_FORMAT1 = "yyyy-MM-dd";
    public static final String DATA_FORMAT2 = "yyyy/MM/dd";
    public static final String DATA_FORMAT3 = "yyyyMMdd";
    public static final String SECOND = "SECOND";
    public static final String MINUTE = "MINUTE";
    public static final String HOUR = "HOUR";
    public static final String DAY = "DAY";
    public static final String MONTH = "MONTH";
    public static final String YEAR = "YEAR";
    private String dbType;
    public static final String DBMS_ORACLE = "ORACLE";
    public static final String DBMS_ODBC = "ODBC";
    public static final String DBMS_ACESS = "ACESS";
    public static final String DBMS_MYSQL = "MYSQL";
    public static final String DBMS_DB2 = "DB2";
    public static final String DBMS_SQLSERVER = "SQLSERVER";
    public static final String DBMS_TERA = "TERA";
    public static final String DBMS_SYBASE = "SYBASE";
    public static final String DBMS_POSTGRE = "POSTGRESQL";
    public static final String DBMS_GBASE = "GBASE";

    public DataBaseAdapter(String dbType) {
        this.dbType = dbType;
    }

    public String getDbType() throws RuntimeException {
        if(this.dbType != null && !this.dbType.trim().equals("")) {
            this.dbType = this.dbType.toUpperCase();
            if(this.dbType.indexOf("MYSQL") >= 0) {
                this.dbType = "MYSQL";
            } else if(this.dbType.indexOf("ORACLE") >= 0) {
                this.dbType = "ORACLE";
            } else if(this.dbType.indexOf("ACCESS") >= 0) {
                this.dbType = "ACESS";
            } else if(this.dbType.indexOf("SQL SERVER") >= 0) {
                this.dbType = "SQLSERVER";
            } else if(this.dbType.indexOf("DB2") >= 0) {
                this.dbType = "DB2";
            } else if(this.dbType.indexOf("TERA") >= 0) {
                this.dbType = "TERA";
            } else if(this.dbType.indexOf("SYBASE") >= 0) {
                this.dbType = "SYBASE";
            } else {
                if(this.dbType.indexOf("POSTGRESQL") < 0) {
                    throw new RuntimeException("不支持的数据库类型！" + this.dbType);
                }

                this.dbType = "POSTGRESQL";
            }

            return this.dbType;
        } else {
            throw new RuntimeException("类DBAdapter的属性dbType必须注入，不能为空");
        }
    }

    public String getTimeStamp(String strDateStr, String strH, String strM, String strS) throws RuntimeException {
        if(null != strDateStr && strDateStr.length() >= 1) {
            if(strDateStr.indexOf("0000-00-00") >= 0) {
                return "null";
            } else {
                String strType = this.getDbType();
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
                } else if(strType.equalsIgnoreCase("SYBASE")) {
                    strRet = "cast(\'" + strDateStr + " " + strH + ":" + strM + ":" + strS + "\' as Datetime)";
                } else {
                    if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                        throw new RuntimeException("不能取得当前日期的函数定义");
                    }

                    strRet = "to_date(\'" + strDateStr + " " + strH + ":" + strM + ":" + strS + "\',\'YYYY-mm-dd hh24:mi:ss\')";
                }

                return strRet;
            }
        } else {
            return null;
        }
    }

    public String getTimeStamp(String strDateStr) throws RuntimeException {
        return this.getTimeStamp(strDateStr, "00", "00", "00");
    }

    public String getTimeStamp(Date date) {
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dFormat2 = new SimpleDateFormat("HH");
        SimpleDateFormat dFormat3 = new SimpleDateFormat("mm");
        SimpleDateFormat dFormat4 = new SimpleDateFormat("ss");
        return this.getTimeStamp(dFormat.format(date), dFormat2.format(date), dFormat3.format(date), dFormat4.format(date));
    }

    public String getDate(String strDateStr) {
        if(null != strDateStr && strDateStr.length() >= 1) {
            if(strDateStr.indexOf("0000-00-00") >= 0) {
                return "null";
            } else {
                String strType = this.getDbType();
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
                    strRet = "to_date(\'" + strDateStr + "\',\'YYYY-mm-dd\')";
                } else if(strType.equalsIgnoreCase("ACESS")) {
                    strRet = "\'" + strDateStr + "\'";
                } else if(strType.equalsIgnoreCase("SQLSERVER")) {
                    strRet = "convert(varchar(10), convert(datetime,\'" + strDateStr + "\'), 111)";
                } else if(strType.equalsIgnoreCase("TERA")) {
                    strRet = "cast(\'" + strDateStr + "\' as date FORMAT \'YYYY-MM-DD\')";
                } else if(strType.equalsIgnoreCase("SYBASE")) {
                    strRet = "cast(\'" + strDateStr + "\' as Date)";
                } else {
                    if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                        throw new RuntimeException("不能取得当前日期的函数定义");
                    }

                    strRet = "to_date(\'" + strDateStr + "\',\'YYYY-mm-dd\')";
                }

                return strRet;
            }
        } else {
            return null;
        }
    }

    public String getDate(String strDateStr, String format) {
        return format.equals("yyyy-MM-dd")?this.getDate(strDateStr):(format.equals("yyyy/MM/dd")?this.getDate2(strDateStr):this.getDate3(strDateStr));
    }

    private String getDate2(String strDateStr) throws RuntimeException {
        if(null != strDateStr && strDateStr.length() >= 1) {
            if(strDateStr.indexOf("000000") >= 0) {
                return "null";
            } else {
                String strType = this.getDbType();
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
                    strRet = "to_date(\'" + strDateStr + "\',\'YYYY/mm/dd\')";
                } else if(strType.equalsIgnoreCase("ACESS")) {
                    strRet = "\'" + strDateStr + "\'";
                } else if(strType.equalsIgnoreCase("SQLSERVER")) {
                    strRet = "CONVERT(Datetime,\'" + strDateStr + "\',20)";
                } else if(strType.equalsIgnoreCase("TERA")) {
                    strRet = "cast(\'" + strDateStr + "\' as date FORMAT \'YYYY/MM/DD\')";
                } else if(strType.equalsIgnoreCase("SYBASE")) {
                    strRet = "cast(\'" + strDateStr + "\' as Date)";
                } else {
                    if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                        throw new RuntimeException("不能取得当前日期的函数定义");
                    }

                    strRet = "to_date(\'" + strDateStr + "\',\'YYYY/mm/dd\')";
                }

                return strRet;
            }
        } else {
            return null;
        }
    }

    private String getDate3(String strDateStr) throws RuntimeException {
        if(null != strDateStr && strDateStr.length() >= 1) {
            if(strDateStr.indexOf("000000") >= 0) {
                return "null";
            } else {
                String strType = this.getDbType();
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
                } else if(strType.equalsIgnoreCase("SYBASE")) {
                    strRet = "cast(\'" + strDateStr + "\' as Date)";
                } else {
                    if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                        throw new RuntimeException("不能取得当前日期的函数定义");
                    }

                    strRet = "to_date(\'" + strDateStr + "\',\'YYYYmmdd\')";
                }

                return strRet;
            }
        } else {
            return null;
        }
    }

    public String getFullDate(String strDateColName) throws RuntimeException {
        String strType = this.getDbType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = "Date_Format(" + strDateColName + ",\'%Y-%m-%d %H:%i:%s\')";
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "to_char(" + strDateColName + ",\'YYYY-mm-dd hh24:mi:ss\')";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "ts_fmt(" + strDateColName + ",\'yyyy-mm-dd hh:mi:ss\')";
        } else if(strType.equalsIgnoreCase("ACESS")) {
            strRet = strDateColName;
        } else if(strType.equalsIgnoreCase("SQLSERVER")) {
            strRet = "CONVERT(Varchar," + strDateColName + ",20)";
            strRet = "CONVERT(Varchar," + strDateColName + ",120)";
        } else if(strType.equalsIgnoreCase("TERA")) {
            strRet = strDateColName + " (FORMAT \'YYYY-MM-DD\')";
        } else if(strType.equalsIgnoreCase("SYBASE")) {
            strRet = "convert(char(10)," + strDateColName + ",23) || \' \' || convert(char(8)," + strDateColName + ",108)";
        } else {
            if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                throw new RuntimeException("不能取得当前日期的函数定义");
            }

            strRet = "to_char(" + strDateColName + ",\'YYYY-mm-dd hh24:mi:ss\')";
        }

        return strRet;
    }

    public String getNow() throws RuntimeException {
        String strType = this.getDbType();
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
        } else if(strType.equalsIgnoreCase("SYBASE")) {
            strRet = "getdate()";
        } else {
            if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                throw new RuntimeException("不能取得当前日期的函数定义");
            }

            strRet = "now()";
        }

        return strRet;
    }

    public String getSqlLimit(String strSql, int limitnum) {
        String strType = this.getDbType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = strSql + " limit " + limitnum;
        } else {
            StringBuffer buffer;
            if(strType.equalsIgnoreCase("ORACLE")) {
                ++limitnum;
                buffer = new StringBuffer();
                buffer.append("select * from (").append(strSql).append(") where ROWNUM<").append(limitnum);
                strRet = buffer.toString();
            } else if(strType.equalsIgnoreCase("DB2")) {
                strRet = strSql + "fetch first " + limitnum + " rows only";
            } else if(strType.equalsIgnoreCase("SYBASE")) {
                strRet = "select top " + limitnum + " * from(" + strSql + ") a";
            } else if(strType.equalsIgnoreCase("SQLSERVER")) {
                strRet = "select top " + limitnum + " * from(" + strSql + ") a";
            } else if("TERA".equalsIgnoreCase(strType)) {
                buffer = new StringBuffer(strSql.length() + 100);
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
            } else {
                if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                    throw new RuntimeException("不能取得函数定义");
                }

                strRet = strSql + "fetch first " + limitnum + " rows only";
            }
        }

        return strRet;
    }

    public String getPagedSql(String srcSql, int currPage, int pageSize) {
        String dbType = this.getDbType();
        int begin = (currPage - 1) * pageSize;
        int end = begin + pageSize;
        String strRet = srcSql;
        if(dbType.equals("MYSQL")) {
            strRet = srcSql + " limit " + begin + "," + pageSize;
        } else {
            StringBuffer rownumber;
            if(dbType.equals("ORACLE")) {
                if(end < 2147483647) {
                    ++end;
                }

                rownumber = new StringBuffer();
                rownumber.append("SELECT * FROM (SELECT ROWNUM AS NUMROW, d.* from (").append(srcSql).append(") d) WHERE NUMROW >").append(begin).append(" AND NUMROW <").append(end);
                strRet = rownumber.toString();
            } else {
                int orderByIndex;
                String[] pagingSelect;
                int i;
                int dotIndex;
                String result;
                StringBuffer var14;
                if(dbType.equals("DB2")) {
                    rownumber = new StringBuffer(" rownumber() over(");
                    orderByIndex = srcSql.toLowerCase().indexOf("order by");
                    if(orderByIndex > 0) {
                        pagingSelect = srcSql.substring(orderByIndex).split("\\.");

                        for(i = 0; i < pagingSelect.length - 1; ++i) {
                            dotIndex = pagingSelect[i].lastIndexOf(",");
                            if(dotIndex < 0) {
                                dotIndex = pagingSelect[i].lastIndexOf(" ");
                            }

                            result = pagingSelect[i].substring(0, dotIndex + 1);
                            rownumber.append(result).append(" temp_.");
                        }

                        rownumber.append(pagingSelect[pagingSelect.length - 1]);
                    }

                    rownumber.append(") as row_,");
                    var14 = (new StringBuffer(srcSql.length() + 100)).append("select * from ( ").append(" select ").append(rownumber.toString()).append("temp_.* from (").append(srcSql).append(" ) as temp_").append(" ) as temp2_").append(" where row_  between " + begin + "+1 and " + end);
                    strRet = var14.toString();
                } else if("TERA".equalsIgnoreCase(dbType)) {
                    rownumber = new StringBuffer(srcSql.length() + 100);
                    rownumber.append(srcSql);
                    orderByIndex = rownumber.toString().toLowerCase().lastIndexOf("order by");
                    if(orderByIndex > 0) {
                        String var15 = rownumber.substring(orderByIndex);
                        rownumber.insert(orderByIndex, " QUALIFY row_number() OVER( ");
                        rownumber.append(" ) ");
                        rownumber.append(" between " + begin + " and " + end);
                        rownumber.append(var15);
                    } else {
                        rownumber.append(" QUALIFY sum(1) over (rows unbounded preceding) between " + begin + " and " + end);
                    }

                    strRet = rownumber.toString();
                } else if("POSTGRESQL".equalsIgnoreCase(dbType)) {
                    rownumber = new StringBuffer(" row_number() over(");
                    orderByIndex = srcSql.toLowerCase().indexOf("order by");
                    if(orderByIndex > 0) {
                        pagingSelect = srcSql.substring(orderByIndex).split("\\.");

                        for(i = 0; i < pagingSelect.length - 1; ++i) {
                            dotIndex = pagingSelect[i].lastIndexOf(",");
                            if(dotIndex < 0) {
                                dotIndex = pagingSelect[i].lastIndexOf(" ");
                            }

                            result = pagingSelect[i].substring(0, dotIndex + 1);
                            rownumber.append(result).append(" temp_.");
                        }

                        rownumber.append(pagingSelect[pagingSelect.length - 1]);
                    }

                    rownumber.append(") as row_,");
                    var14 = (new StringBuffer(srcSql.length() + 100)).append("select * from ( ").append(" select ").append(rownumber.toString()).append("temp_.* from (").append(srcSql).append(" ) as temp_").append(" ) as temp2_").append(" where row_  between " + begin + "+1 and " + end);
                    strRet = var14.toString();
                }
            }
        }

        return strRet;
    }

    public String getPagedSqlTest(String srcSql, int currPage, int pageSize) {
        String dbType = "DB2";
        int begin = (currPage - 1) * pageSize;
        int end = begin + pageSize;
        String strRet = srcSql;
        if(dbType.equals("DB2")) {
            StringBuffer rownumber = new StringBuffer(" rownumber() over(");
            int orderByIndex = srcSql.toLowerCase().indexOf("order by");
            if(orderByIndex > 0) {
                String[] pagingSelect = srcSql.substring(orderByIndex).split("\\.");

                for(int i = 0; i < pagingSelect.length - 1; ++i) {
                    int dotIndex = pagingSelect[i].lastIndexOf(",");
                    if(dotIndex < 0) {
                        dotIndex = pagingSelect[i].lastIndexOf(" ");
                    }

                    String result = pagingSelect[i].substring(0, dotIndex + 1);
                    rownumber.append(result).append(" temp_.");
                }

                rownumber.append(pagingSelect[pagingSelect.length - 1]);
            }

            rownumber.append(") as row_,");
            StringBuffer var14 = (new StringBuffer(srcSql.length() + 100)).append("select * from ( ").append(" select ").append(rownumber.toString()).append("temp_.* from (").append(srcSql).append(" ) as temp_").append(" ) as temp2_").append(" where row_  between " + begin + "+1 and " + end);
            strRet = var14.toString();
        }

        return strRet;
    }

    public static void main(String[] args) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("SELECT * FROM ( ").append("SELECT abc.*, row_number() over(partition by label_custom_name ) rank ").append("from (select a.primary_key_id, ").append("a.scene_id, ").append("c.scene_name, ").append("a.market_task_id, ").append("a.label_id, ").append("a.custom_group_id, ").append("a.label_custom_name, ").append("a.avg_score, ").append("a.newest_time, ").append("a.use_times, ").append("a.sort_num, ").append("a.show_type ").append("from ci_custom_label_scene_rel a ").append("left join ci_market_task b on a.market_task_id = b.market_task_id ").append("left join ci_market_scene c on a.scene_id = c.scene_id ").append("where a.status = 1 ").append(" and a.scene_id = \'1\' ").append(" and a.market_task_id in (\'8\') ").append(" ) abc ) where rank =1 order by sort_num, avg_score desc, newest_time desc");
        String sql = sqlBuf.toString();
        DataBaseAdapter d = new DataBaseAdapter("DB2");
        String pageSql = d.getPagedSqlTest(sql, 0, 10);
        System.out.println(pageSql);
    }

    public String getPagedSql(String sql, String column, String strPrimaryKey, int curpage, int pagesize) throws RuntimeException {
        String strDBType = this.getDbType();
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
        } else if("POSTGRESQL".equalsIgnoreCase(this.dbType)) {
            buffer.append("select * from ( ");
            buffer.append("select ").append(column).append("  row_number() over (order by " + strPrimaryKey + ") as my_rownum from( ");
            buffer.append(sql).append(") as temp ");
            buffer.append("fetch first " + (pagesize * curpage + pagesize) + " rows only) as a ");
            buffer.append("where a.my_rownum > " + pagesize * curpage);
        }

        return buffer.toString();
    }

    public String getSubString(String strColName, int pos, int len) throws RuntimeException {
        String strType = this.getDbType();
        String strRet = "";
        if(!strType.equalsIgnoreCase("MYSQL") && !strType.equalsIgnoreCase("SYBASE")) {
            if(!strType.equalsIgnoreCase("ORACLE") && !strType.equalsIgnoreCase("DB2") && !strType.equalsIgnoreCase("POSTGRESQL")) {
                if(!strType.equalsIgnoreCase("TERA")) {
                    throw new RuntimeException("不能取得函数定义");
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

    public String getSubString(String strColName, int pos) throws RuntimeException {
        return this.getSubString(strColName, pos, -1);
    }

    public String getAddDate(String interv, String unit) throws RuntimeException {
        String strRet = "";
        String strType = this.getDbType();
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
        } else if(strType.equalsIgnoreCase("SYBASE")) {
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
        } else {
            if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                throw new RuntimeException("不能取得当前日期的函数定义");
            }

            if(unit.compareTo("MINUTE") == 0) {
                strRet = "now()+interval \'" + interv + " minute\'";
            } else if(unit.compareTo("SECOND") == 0) {
                strRet = "now()+interval \'" + interv + " second\'";
            } else if(unit.compareTo("HOUR") == 0) {
                strRet = "now()+interval \'" + interv + " hour\'";
            } else if(unit.compareTo("DAY") == 0) {
                strRet = "now()+interval \'" + interv + " day\'";
            } else if(unit.compareTo("MONTH") == 0) {
                strRet = "now()+interval \'" + interv + " month\'";
            } else if(unit.compareTo("YEAR") == 0) {
                strRet = "now()+interval \'" + interv + " year\'";
            }
        }

        return strRet;
    }

    public String getDateAddMonth(String monthNum) throws RuntimeException {
        String strType = this.getDbType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = "DATE_ADD(curdate(),INTERVAL " + monthNum + " month)";
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "to_char(add_months(sysdate," + monthNum + "),\'YYYY-mm-dd\')";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "char((current date + " + monthNum + " month))";
        } else if(strType.equalsIgnoreCase("SYBASE")) {
            strRet = "dateadd(mm," + monthNum + ",getdate())";
        } else if(strType.equalsIgnoreCase("TERA")) {
            strRet = "add_months(date," + monthNum + ")";
        } else {
            if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                throw new RuntimeException("不能取得当前日期的函数定义");
            }

            strRet = "to_char(now()+interval \'" + monthNum + "\' month,\'YYYY-mm-dd\')";
        }

        return strRet;
    }

    public String getIntToChar(String strColName) throws RuntimeException {
        String strType = this.getDbType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = strColName;
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "cast(" + strColName + " as varchar2(12))";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "char(" + strColName + ")";
        } else if(strType.equalsIgnoreCase("SYBASE")) {
            strRet = "convert(char," + strColName + ")";
        } else if(strType.equalsIgnoreCase("SQLSERVER")) {
            strRet = "cast(" + strColName + " as varchar(12))";
        } else if(strType.equalsIgnoreCase("TERA")) {
            strRet = "cast(" + strColName + " as varchar(12))";
        } else {
            if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                throw new RuntimeException("不能取得函数定义");
            }

            strRet = "cast(" + strColName + " as varchar(12))";
        }

        return strRet;
    }

    public String getColumnToChar(String strColName) throws RuntimeException {
        String strType = this.getDbType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = strColName;
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = strColName;
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "char(" + strColName + ")";
        } else if(strType.equalsIgnoreCase("SYBASE")) {
            strRet = "convert(char," + strColName + ")";
        } else if(strType.equalsIgnoreCase("SQLSERVER")) {
            strRet = "cast(" + strColName + " as varchar(12))";
        } else if(strType.equalsIgnoreCase("TERA")) {
            strRet = "cast(" + strColName + " as varchar(12))";
        } else {
            if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                throw new RuntimeException("不能取得函数定义");
            }

            strRet = strColName;
        }

        return strRet;
    }

    public String getCharToInt(String strColName) throws RuntimeException {
        String strType = this.getDbType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = strColName;
        } else if(!strType.equalsIgnoreCase("ORACLE") && !strType.equalsIgnoreCase("TERA") && !strType.equalsIgnoreCase("POSTGRESQL")) {
            if(strType.equalsIgnoreCase("DB2")) {
                strRet = "int(" + strColName + ")";
            } else if(strType.equalsIgnoreCase("SYBASE")) {
                strRet = "convert(int," + strColName + ")";
            } else {
                if(!strType.equalsIgnoreCase("SQLSERVER")) {
                    throw new RuntimeException("不能取得函数定义");
                }

                strRet = "cast(" + strColName + " as integer)";
            }
        } else {
            strRet = "cast(" + strColName + " as integer)";
        }

        return strRet;
    }

    public String getCharToDouble(String strColName) {
        String strType = this.getDbType();
        String strRet = "";
        if(strType.equalsIgnoreCase("MYSQL")) {
            strRet = strColName;
        } else if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "cast(" + strColName + " as numeric)";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "double(" + strColName + ")";
        } else {
            if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                throw new RuntimeException("不能取得函数定义");
            }

            strRet = "cast(" + strColName + " as numeric)";
        }

        return strRet;
    }

    public String getRound(String str1, String str2) throws RuntimeException {
        String strType = this.getDbType();
        String strRet = "";
        if(strType.equalsIgnoreCase("TERA")) {
            strRet = " cast ((" + str1 + ") as decimal(10," + str2 + ")) ";
        } else {
            strRet = " round(" + str1 + "," + str2 + ") ";
        }

        return strRet;
    }

    public String getNotEqual() throws RuntimeException {
        String strType = this.getDbType();
        String strRet = "";
        if(strType.equalsIgnoreCase("TERA")) {
            strRet = "<>";
        } else {
            strRet = "!=";
        }

        return strRet;
    }

    public String getNvl(String str1, String str2) throws RuntimeException {
        String strType = this.getDbType();
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
        } else if(strType.equalsIgnoreCase("TERA")) {
            strRet = "COALESCE(" + str1 + "," + str2 + ")";
        } else {
            if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                throw new RuntimeException("不能取得函数定义");
            }

            strRet = "COALESCE(" + str1 + "," + str2 + ")";
        }

        return strRet;
    }

    public String getCreateAsTableSql(String newtable, String templettable, String tableSpace) throws RuntimeException {
        String ss = "";
        String strDBType = this.getDbType();
        if("ORACLE".equalsIgnoreCase(strDBType)) {
            ss = "create table " + newtable + " tablespace " + tableSpace + " as select * from " + templettable + " where 1=2";
        } else if("DB2".equalsIgnoreCase(strDBType)) {
            ss = "create table " + newtable + " like " + templettable + " in " + tableSpace;
        } else if("TERA".equalsIgnoreCase(strDBType)) {
            ss = "create table " + newtable + " as " + templettable + " with no data";
        } else if("POSTGRESQL".equalsIgnoreCase(strDBType)) {
            ss = "create table " + newtable + " tablespace " + tableSpace + " as select * from " + templettable + " where 1=2";
        }

        return ss;
    }

    public String getCreateTableInTableSpaceSql(String tableDDLSql, String tableSpace) throws RuntimeException {
        if(tableSpace != null && tableSpace.length() >= 1) {
            String strDBType = this.getDbType();
            if("ORACLE".equalsIgnoreCase(strDBType)) {
                tableDDLSql = tableDDLSql + " tablespace " + tableSpace;
            } else if("DB2".equalsIgnoreCase(strDBType)) {
                tableDDLSql = tableDDLSql + " in " + tableSpace;
            } else if("POSTGRESQL".equalsIgnoreCase(strDBType)) {
                tableDDLSql = tableDDLSql + " tablespace " + tableSpace;
            }

            return tableDDLSql;
        } else {
            return tableDDLSql;
        }
    }

    public String getCreateIndexInTableSpaceSql(String createIndexSql, String tableSpace) throws RuntimeException {
        if(tableSpace != null && tableSpace.length() >= 1) {
            String strDBType = this.getDbType();
            if("ORACLE".equalsIgnoreCase(strDBType)) {
                createIndexSql = createIndexSql + " using index tablespace " + tableSpace;
            }

            return createIndexSql;
        } else {
            return createIndexSql;
        }
    }

    public String getCheckTableIsExistSql(String tableName) throws RuntimeException {
        String strSql = "";
        String strDBType = this.getDbType();
        StringBuffer strSqlBuf = new StringBuffer();
        if(strDBType.equals("DB2")) {
            strSqlBuf.append("select * from syscat.tables where tabname=\'").append(tableName.toUpperCase()).append("\'");
            strSql = strSqlBuf.toString();
        } else if(strDBType.equals("ORACLE")) {
            strSqlBuf.setLength(0);
            strSqlBuf.append("select * from TAB where tname=\'").append(tableName.toUpperCase()).append("\'");
            strSql = strSqlBuf.toString();
        } else if(strDBType.equals("TERA")) {
            strSqlBuf.setLength(0);
            strSqlBuf.append("select * from dbc.tables where tablename=\'").append(tableName.toUpperCase()).append("\'");
            strSql = strSqlBuf.toString();
        } else if(strDBType.equals("POSTGRESQL")) {
            strSqlBuf.setLength(0);
            strSqlBuf.append("select * from pg_tables where tablename=\'").append(tableName.toLowerCase()).append("\'");
            strSql = strSqlBuf.toString();
        }

        return strSql;
    }

    public String clobToString(Clob clob) {
        String s = "";
        StringBuffer content = new StringBuffer();

        try {
            BufferedReader e = new BufferedReader(clob.getCharacterStream());

            for(s = e.readLine(); s != null; s = e.readLine()) {
                content.append(s);
            }
        } catch (Exception var8) {
            log.error("convert clob to string error", var8);
        } finally {
            return content.toString();
        }
    }

    public String queryTree(String tableName, String idName, String pidName, Object startId, int orientation, String orderBy, String... args) throws RuntimeException {
        String strSql = null;
        String strDBType = this.getDbType();
        String order = null;
        String start = null;
        if(startId == null) {
            throw new RuntimeException("起始查询条件不能为空，请参考本方法使用文档");
        } else {
            if(startId instanceof String) {
                start = idName + "=\'" + startId + "\' ";
            } else {
                if(!(startId instanceof Number)) {
                    throw new RuntimeException("参数传递错误，请参考本方法使用文档");
                }

                start = idName + "=" + startId + " ";
            }

            if(orderBy != null && orderBy.trim() != null) {
                if(orderBy.trim().equalsIgnoreCase("asc")) {
                    order = "asc";
                } else {
                    if(!orderBy.trim().equalsIgnoreCase("desc")) {
                        throw new RuntimeException("参数传递错误，请参考本方法使用文档");
                    }

                    order = "desc";
                }
            }

            String selectRet = idName + "," + pidName + " ";
            String[] tempRet = args;
            int strSqlBuf = args.length;

            int len$;
            for(len$ = 0; len$ < strSqlBuf; ++len$) {
                String i$ = tempRet[len$];
                selectRet = selectRet + "," + i$ + " ";
            }

            String var18 = "";
            String[] var19 = selectRet.split(",");
            len$ = var19.length;

            for(int var21 = 0; var21 < len$; ++var21) {
                String t = var19[var21];
                var18 = var18 + ",CHILD." + t.trim();
            }

            var18 = var18.substring(1);
            StringBuffer var20 = new StringBuffer();
            if(strDBType.equals("DB2")) {
                if(orientation == 1) {
                    var20.append("WITH TEMP(").append(selectRet).append(") AS (SELECT ").append(selectRet).append("  FROM ").append(tableName).append(" WHERE ").append(start).append("UNION ALL SELECT ").append(var18).append("  FROM TEMP PARENT,  ").append(tableName).append(" CHILD WHERE  PARENT.").append(idName).append("=CHILD.").append(pidName).append(")SELECT ").append(selectRet).append("  FROM TEMP order by ").append(idName).append(" ").append(order);
                    strSql = var20.toString();
                }

                if(orientation == 0) {
                    var20.setLength(0);
                    var20.append("WITH TEMP(").append(selectRet).append(") AS (SELECT ").append(selectRet).append("  FROM ").append(tableName).append(" WHERE ").append(start).append("UNION ALL SELECT ").append(var18).append("  FROM TEMP PARENT,  ").append(tableName).append(" CHILD WHERE  CHILD.").append(idName).append("=PARENT.").append(pidName).append(")SELECT  ").append(selectRet).append("  FROM TEMP order by ").append(idName).append(" ").append(order);
                    strSql = var20.toString();
                }
            } else {
                if(!strDBType.equals("ORACLE")) {
                    throw new RuntimeException("不支持的该数据库类型的树查询！");
                }

                if(orientation == 1) {
                    var20.setLength(0);
                    var20.append("select ").append(selectRet).append(" from ").append(tableName).append(" start with ").append(start).append(" connect by prior ").append(idName).append("=").append(pidName).append(" order by ").append(idName).append(" ").append(order);
                    strSql = var20.toString();
                }

                if(orientation == 0) {
                    var20.setLength(0);
                    var20.append("select ").append(selectRet).append(" from ").append(tableName).append(" start with ").append(start).append(" connect by prior ").append(pidName).append("=").append(idName).append(" order by ").append(idName).append(" ").append(order);
                    strSql = var20.toString();
                }
            }

            return strSql;
        }
    }

    public String getTreeSql(String tableName, String idName, String pidName, Object startId, int orientation, String orderBy, String... args) throws RuntimeException {
        String strSql = null;
        String strDBType = this.getDbType();
        String order = null;
        String start = null;
        if(startId == null) {
            throw new RuntimeException("起始查询条件不能为空，请参考本方法使用文档");
        } else {
            if(startId instanceof String) {
                start = idName + "=\'" + startId + "\' ";
            } else {
                if(!(startId instanceof Number)) {
                    throw new RuntimeException("参数传递错误，请参考本方法使用文档");
                }

                start = idName + "=" + startId + " ";
            }

            if(orderBy != null && orderBy.trim() != null) {
                if(orderBy.trim().equalsIgnoreCase("asc")) {
                    order = "asc";
                } else {
                    if(!orderBy.trim().equalsIgnoreCase("desc")) {
                        throw new RuntimeException("参数传递错误，请参考本方法使用文档");
                    }

                    order = "desc";
                }
            }

            String selectRet = idName + "," + pidName + " ";
            String[] tempRet = args;
            int strSqlBuf = args.length;

            int len$;
            for(len$ = 0; len$ < strSqlBuf; ++len$) {
                String i$ = tempRet[len$];
                selectRet = selectRet + "," + i$ + " ";
            }

            String var18 = "";
            String[] var19 = selectRet.split(",");
            len$ = var19.length;

            for(int var21 = 0; var21 < len$; ++var21) {
                String t = var19[var21];
                var18 = var18 + ",CHILD." + t.trim();
            }

            var18 = var18.substring(1);
            StringBuffer var20 = new StringBuffer();
            if(strDBType.equals("DB2")) {
                if(orientation == 1) {
                    var20.append("WITH TEMP(").append(selectRet).append(") AS ").append("(SELECT ").append(selectRet).append("  FROM ").append(tableName).append(" WHERE ").append(start).append("UNION ALL SELECT").append(var18).append("  FROM TEMP PARENT,  ").append(tableName).append(" CHILD WHERE  PARENT.").append(idName).append("=CHILD.").append(pidName).append(")SELECT ").append(selectRet).append("  FROM TEMP order by ").append(idName).append(" ").append(order);
                    strSql = var20.toString();
                }

                if(orientation == 0) {
                    var20.setLength(0);
                    var20.append("WITH TEMP(").append(selectRet).append(") AS (SELECT ").append(selectRet).append("  FROM ").append(tableName).append(" WHERE ").append(start).append("UNION ALL SELECT ").append(var18).append("  FROM TEMP PARENT,  ").append(tableName).append(" CHILD WHERE  CHILD.").append(idName).append("=PARENT.").append(pidName).append(")SELECT  ").append(selectRet).append("  FROM TEMP order by ").append(idName).append(" ").append(order);
                    strSql = var20.toString();
                }
            } else {
                if(!strDBType.equals("ORACLE")) {
                    throw new RuntimeException("不支持的该数据库类型的树查询！");
                }

                var20.setLength(0);
                if(orientation == 1) {
                    var20.append("select ").append(selectRet).append(" from ").append(tableName).append(" start with ").append(start).append(" connect by prior ").append(idName).append("=").append(pidName).append(" order by ").append(idName).append(" ").append(order);
                    strSql = var20.toString();
                }

                if(orientation == 0) {
                    var20.setLength(0);
                    var20.append("select ").append(selectRet).append(" from ").append(tableName).append(" start with ").append(start).append(" connect by prior ").append(pidName).append("=").append(idName).append(" order by ").append(idName).append(" ").append(order);
                    strSql = var20.toString();
                }
            }

            return strSql;
        }
    }

    public String getStringLen(String strColName) {
        String strType = this.getDbType();
        String strRet = "";
        if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "length(" + strColName + ")";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "length(" + strColName + ")";
        } else if(!strType.equalsIgnoreCase("TERA")) {
            if(strType.equalsIgnoreCase("SYBASE")) {
                strRet = "datalength(" + strColName + ")";
            } else {
                if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                    throw new RuntimeException("不能取得函数定义");
                }

                strRet = "length(" + strColName + ")";
            }
        }

        return strRet;
    }

    public String getPosString(String strColName, String charName) throws RuntimeException {
        String strType = this.getDbType();
        String strRet = "";
        if(strType.equalsIgnoreCase("ORACLE")) {
            strRet = "instr(" + strColName + ",\'" + charName + "\')";
        } else if(strType.equalsIgnoreCase("DB2")) {
            strRet = "locate(\'" + charName + "\'," + strColName + ")";
        } else if(!strType.equalsIgnoreCase("TERA")) {
            if(strType.equalsIgnoreCase("SYBASE")) {
                strRet = "charindex(\'" + charName + "\'," + strColName + ")";
            } else {
                if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                    throw new RuntimeException("不能取得函数定义");
                }

                strRet = "position(\'" + charName + "\' in " + strColName + ")";
            }
        }

        return strRet;
    }

    public String getSubString(String strColName, String pos, String len) throws RuntimeException {
        String strType = this.getDbType();
        String strRet = "";
        if(!strType.equalsIgnoreCase("MYSQL") && !strType.equalsIgnoreCase("SYBASE")) {
            if(!strType.equalsIgnoreCase("ORACLE") && !strType.equalsIgnoreCase("DB2") && !strType.equalsIgnoreCase("POSTGRESQL")) {
                if(strType.equalsIgnoreCase("TERA")) {
                    strRet = "substring(" + strColName + " from " + pos + " for " + len + ")";
                } else {
                    if(!strType.equalsIgnoreCase("SYBASE")) {
                        throw new RuntimeException("不能取得函数定义");
                    }

                    strRet = "substr(" + strColName + "," + pos + "," + len + ")";
                }
            } else {
                strRet = "substr(" + strColName + "," + pos + "," + len + ")";
            }
        } else {
            strRet = "substring(" + strColName + "," + pos + "," + len + ")";
        }

        return strRet;
    }

    public String getSqlCreateAsTable(String newTab, String tmpTable, String priKey) {
        StringBuilder strRet = new StringBuilder();
        String tabSpace = Configure.getInstance().getProperty("CI_TABLESPACE");
        String indexSpace = Configure.getInstance().getProperty("CI_INDEX_TABLESPACE");
        String schema = Configure.getInstance().getProperty("CI_SCHEMA");
        if(this.dbType.equalsIgnoreCase("ORACLE")) {
            if(StringUtil.isNotEmpty(tabSpace)) {
                strRet.append("create table ").append(newTab).append(" tablespace ").append(tabSpace).append(" as select * from ").append(tmpTable).append(" where 1=2 ");
            } else {
                strRet.append("create table ").append(newTab).append(" as select * from ").append(tmpTable).append(" where 1=2 ");
            }
        } else if(this.dbType.equalsIgnoreCase("DB2")) {
            strRet.append("create table ");
            if(StringUtil.isNotEmpty(schema)) {
                strRet.append(schema).append(".");
            }

            strRet.append(newTab).append(" like ").append(tmpTable);
            if(StringUtil.isNotEmpty(tabSpace)) {
                strRet.append(" NOT LOGGED INITIALLY COMPRESS YES DISTRIBUTE BY HASH (").append(priKey).append(")").append(" IN ").append(tabSpace);
                if(StringUtil.isNotEmpty(indexSpace)) {
                    strRet.append(" INDEX IN ").append(indexSpace);
                }
            }
        } else if(this.dbType.equalsIgnoreCase("MYSQL") || this.dbType.equalsIgnoreCase("GBASE") || this.dbType.equalsIgnoreCase("POSTGRESQL")) {
            strRet.append("create table ").append(newTab).append(" as select * from ").append(tmpTable).append(" where 1=2 ");
        }

        log.debug("create sql:" + strRet);
        return strRet.toString();
    }

    public String getTrim(String column) {
        return this.dbType.equalsIgnoreCase("DB2")?"rtrim(ltrim(" + column + "))":"trim(" + column + ")";
    }

    public String getSequenceSql(String sequenceName) throws Exception {
        String strType = this.getDbType();
        String sql = "";
        if(strType.equalsIgnoreCase("ORACLE")) {
            sql = "select " + sequenceName + ".nextval from dual";
        } else if(strType.equalsIgnoreCase("DB2")) {
            sql = "select nextval for " + sequenceName + " from sysibm.sysdummy1";
        } else {
            if(!strType.equalsIgnoreCase("POSTGRESQL")) {
                throw new Exception("数据库类型：" + strType + "不支持sequence！");
            }

            sql = "select nextval(" + sequenceName + ")";
        }

        return sql;
    }

    public String getLimtCountSql(String srcSql, int currPage, int pageSize, String attrSqlStr) {
        String dbType = this.getDbType();
        int begin = (currPage - 1) * pageSize;
        int end = begin + pageSize;
        String strRet = srcSql;
        if(dbType.equals("MYSQL")) {
            strRet = srcSql + " limit " + begin + "," + pageSize;
        } else if(dbType.equals("ORACLE")) {
            if(end < 2147483647) {
                ++end;
            }

            strRet = " SELECT " + attrSqlStr + " FROM (SELECT ROWNUM AS NUMROW, d.* from (" + srcSql + ") d) WHERE NUMROW >" + begin + " AND NUMROW <" + end;
        } else {
            StringBuffer rownumber;
            int orderByIndex;
            String[] pagingSelect;
            int i;
            int dotIndex;
            String result;
            StringBuffer var15;
            if(dbType.equals("DB2")) {
                rownumber = new StringBuffer(" rownumber() over(");
                orderByIndex = srcSql.toLowerCase().indexOf("order by");
                if(orderByIndex > 0) {
                    pagingSelect = srcSql.substring(orderByIndex).split("\\.");

                    for(i = 0; i < pagingSelect.length - 1; ++i) {
                        dotIndex = pagingSelect[i].lastIndexOf(",");
                        if(dotIndex < 0) {
                            dotIndex = pagingSelect[i].lastIndexOf(" ");
                        }

                        result = pagingSelect[i].substring(0, dotIndex + 1);
                        rownumber.append(result).append(" temp_.");
                    }

                    rownumber.append(pagingSelect[pagingSelect.length - 1]);
                }

                rownumber.append(") as row_,");
                var15 = (new StringBuffer(srcSql.length() + 100)).append(" select " + attrSqlStr + " from ( ").append(" select ").append(rownumber.toString()).append("temp_.* from (").append(srcSql).append(" ) as temp_").append(" ) as temp2_").append(" where row_  between " + begin + "+1 and " + end);
                strRet = var15.toString();
            } else if("TERA".equalsIgnoreCase(dbType)) {
                rownumber = new StringBuffer(srcSql.length() + 100);
                rownumber.append(srcSql);
                orderByIndex = rownumber.toString().toLowerCase().lastIndexOf("order by");
                if(orderByIndex > 0) {
                    String var16 = rownumber.substring(orderByIndex);
                    rownumber.insert(orderByIndex, " QUALIFY row_number() OVER( ");
                    rownumber.append(" ) ");
                    rownumber.append(" between " + begin + " and " + end);
                    rownumber.append(var16);
                } else {
                    rownumber.append(" QUALIFY sum(1) over (rows unbounded preceding) between " + begin + " and " + end);
                }

                strRet = rownumber.toString();
            } else if("POSTGRESQL".equalsIgnoreCase(dbType)) {
                rownumber = new StringBuffer(" row_number() over(");
                orderByIndex = srcSql.toLowerCase().indexOf("order by");
                if(orderByIndex > 0) {
                    pagingSelect = srcSql.substring(orderByIndex).split("\\.");

                    for(i = 0; i < pagingSelect.length - 1; ++i) {
                        dotIndex = pagingSelect[i].lastIndexOf(",");
                        if(dotIndex < 0) {
                            dotIndex = pagingSelect[i].lastIndexOf(" ");
                        }

                        result = pagingSelect[i].substring(0, dotIndex + 1);
                        rownumber.append(result).append(" temp_.");
                    }

                    rownumber.append(pagingSelect[pagingSelect.length - 1]);
                }

                rownumber.append(") as row_,");
                var15 = (new StringBuffer(srcSql.length() + 100)).append(" select " + attrSqlStr + " from ( ").append(" select ").append(rownumber.toString()).append("temp_.* from (").append(srcSql).append(" ) as temp_").append(" ) as temp2_").append(" where row_  between " + begin + "+1 and " + end);
                strRet = var15.toString();
            }
        }

        return strRet;
    }

    public String getOverFunPostfix(String column) {
        StringBuffer result = new StringBuffer();
        if(this.dbType.equals("ORACLE")) {
            result.append(" order by ").append(column);
        } else if(this.dbType.equals("DB2")) {
            result.append(" ");
        }

        return result.toString();
    }

    public String getSqlAddColumn(String tableName, String column, String type) throws Exception {
        String schema = Configure.getInstance().getProperty("CI_SCHEMA");
        if(StringUtil.isNotEmpty(schema)) {
            tableName = schema + "." + tableName;
        }

        StringBuffer sql = new StringBuffer("");
        sql.append(" ALTER TABLE ").append(tableName).append(" ADD ").append(column).append(" ").append(type);
        return sql.toString();
    }

    public String getStrColumnType(int length) {
        String dbType = this.getDbType();
        return dbType.equalsIgnoreCase("DB2")?"VARCHAR(" + length + ")":(dbType.equalsIgnoreCase("ORACLE")?"VARCHAR2(" + length + ")":(dbType.equalsIgnoreCase("MYSQL")?"VARCHAR(" + length + ")":(dbType.equalsIgnoreCase("SYBASE")?null:(dbType.equalsIgnoreCase("SQLSERVER")?null:(dbType.equalsIgnoreCase("TERA")?null:(dbType.equalsIgnoreCase("POSTGRESQL")?null:"VARCHAR2(" + length + ")"))))));
    }

    public String getIntDefaultType() {
        String dbType = this.getDbType();
        return dbType.equalsIgnoreCase("DB2")?"INTEGER":(dbType.equalsIgnoreCase("ORACLE")?"INTEGER":(dbType.equalsIgnoreCase("MYSQL")?"INT":(dbType.equalsIgnoreCase("SYBASE")?null:(dbType.equalsIgnoreCase("SQLSERVER")?null:(dbType.equalsIgnoreCase("TERA")?null:(dbType.equalsIgnoreCase("POSTGRESQL")?null:null))))));
    }
}

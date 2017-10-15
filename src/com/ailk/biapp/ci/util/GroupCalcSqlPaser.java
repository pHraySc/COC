package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.constant.CommonConstants;
import com.asiainfo.biframe.utils.config.Configure;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class GroupCalcSqlPaser {
    private Logger log = Logger.getLogger(GroupCalcSqlPaser.class);
    private String UNION_SQL = "select pk from table1 UNION select pk from table2";
    private String INTERSECT_SQL = "select pk from table1 tableName1 where exists ( select pk from table2 tableName2 where tableName1.pk = tableName2.pk)";
    private String EXCEPT_SQL = "select pk from table1 tableName1  where not exists ( select pk from table2 tableName2 where tableName1.pk = tableName2.pk)";
    private static String TABLE_NAME_PARTTERN = "\\w*";
    public static final char UNION = '¡È';
    public static final char INTERSECT = '¡É';
    public static final char EXCEPT = '-';
    public static final char LEFT_Q = '(';
    public static final char RIGHT_Q = ')';
    private int index = 0;

    public GroupCalcSqlPaser(String dbTyp) {
        if("DB2".equalsIgnoreCase(dbTyp)) {
            this.UNION_SQL = "select pk from table1 UNION select pk from table2";
            this.INTERSECT_SQL = "select pk from table1 tableName1 INTERSECT select pk from table2 tableName2 ";
            this.EXCEPT_SQL = "select pk from table1 tableName1 EXCEPT select pk from table2 tableName2 ";
        }

        if("ORACLE".equalsIgnoreCase(dbTyp)) {
            StringBuffer pk = new StringBuffer();
            StringBuffer sb2 = new StringBuffer();
            StringBuffer sb3 = new StringBuffer();
            pk.append("select ").append(CommonConstants.sqlParallel).append(" pk from table1 UNION select ").append(CommonConstants.sqlParallel).append(" pk from table2");
            this.UNION_SQL = pk.toString();
            sb2.append("select ").append(CommonConstants.sqlParallel).append(" pk from table1 tableName1 INTERSECT select ").append(CommonConstants.sqlParallel).append(" pk from table2 tableName2 ");
            this.INTERSECT_SQL = sb2.toString();
            sb3.append("select ").append(CommonConstants.sqlParallel).append(" pk from table1 tableName1 MINUS select ").append(CommonConstants.sqlParallel).append(" pk from table2 tableName2 ");
            this.EXCEPT_SQL = sb3.toString();
        }

        String pk1 = Configure.getInstance().getProperty("RELATED_COLUMN");
        this.UNION_SQL = this.UNION_SQL.replace("pk", pk1);
        this.INTERSECT_SQL = this.INTERSECT_SQL.replace("pk", pk1);
        this.EXCEPT_SQL = this.EXCEPT_SQL.replace("pk", pk1);
    }

    public String getUinionOfTable(String table1, String table2, Map<String, String> labelRuleToSql) {
        if(labelRuleToSql != null) {
            if(labelRuleToSql.containsKey(table1)) {
                table1 = (String)labelRuleToSql.get(table1);
            }

            if(labelRuleToSql.containsKey(table2)) {
                table2 = (String)labelRuleToSql.get(table2);
            }
        }

        return this.UNION_SQL.replace("table1", this.handelTableName(table1)).replace("tableName1", "").replace("table2", this.handelTableName(table2)).replace("tableName2", "");
    }

    public String getUinionOfTable(String table1, String table2) {
        return this.UNION_SQL.replace("table1", this.handelTableName(table1)).replace("tableName1", this.getTableName()).replace("table2", this.handelTableName(table2)).replace("tableName2", this.getTableName());
    }

    public String getTableName() {
        return "temp" + this.index++;
    }

    public String handelTableName(String table) {
        return this.isInnerTable(table)?'(' + table + ')':table;
    }

    public String getIntersectOfTable(String table1, String table2, Map<String, String> labelRuleToSql) {
        if(labelRuleToSql != null) {
            if(labelRuleToSql.containsKey(table1)) {
                table1 = (String)labelRuleToSql.get(table1);
            }

            if(labelRuleToSql.containsKey(table2)) {
                table2 = (String)labelRuleToSql.get(table2);
            }
        }

        return this.INTERSECT_SQL.replace("table1", this.handelTableName(table1)).replace("tableName1", "").replace("table2", this.handelTableName(table2)).replace("tableName2", "");
    }

    public String getIntersectOfTable(String table1, String table2) {
        return this.INTERSECT_SQL.replace("table1", this.handelTableName(table1)).replace("tableName1", this.getTableName()).replace("table2", this.handelTableName(table2)).replace("tableName2", this.getTableName());
    }

    public String getDifferenceOfTable(String table1, String table2, Map<String, String> labelRuleToSql) {
        if(labelRuleToSql != null) {
            if(labelRuleToSql.containsKey(table1)) {
                table1 = (String)labelRuleToSql.get(table1);
            }

            if(labelRuleToSql.containsKey(table2)) {
                table2 = (String)labelRuleToSql.get(table2);
            }
        }

        return this.EXCEPT_SQL.replace("table1", this.handelTableName(table1)).replace("tableName1", "").replace("table2", this.handelTableName(table2)).replace("tableName2", "");
    }

    public String getDifferenceOfTable(String table1, String table2) {
        return this.EXCEPT_SQL.replace("table1", this.handelTableName(table1)).replace("tableName1", this.getTableName()).replace("table2", this.handelTableName(table2)).replace("tableName2", this.getTableName());
    }

    public String parseExprToSql(String expr, Map<String, String> labelRuleToSql) {
        String parsed = "";

        while(expr.length() > 0) {
            String v1 = this.getNextVal(expr);
            expr = expr.substring(v1.length());
            if(this.isExpr(v1)) {
                if(v1.startsWith(String.valueOf('(')) && v1.endsWith(String.valueOf(')'))) {
                    v1 = '(' + this.parseExprToSql(v1.substring(1, v1.length() - 1), labelRuleToSql) + ')';
                }
            } else {
                v1 = v1.replace(String.valueOf('('), "");
                v1 = v1.replace(String.valueOf(')'), "");
            }

            if(expr.length() == 0) {
                return v1;
            }

            String v2 = this.getNextVal(expr);
            expr = expr.substring(v2.length());
            if(this.isOpt(v1)) {
                if(this.isExpr(v2)) {
                    if(v2.startsWith(String.valueOf('(')) && v2.endsWith(String.valueOf(')'))) {
                        v2 = '(' + this.parseExprToSql(v2.substring(1, v2.length() - 1), labelRuleToSql) + ')';
                    }
                } else {
                    v2 = v2.replace(String.valueOf('('), "");
                    v2 = v2.replace(String.valueOf(')'), "");
                }

                parsed = this.toSql(parsed, v1, v2, labelRuleToSql);
            } else {
                String v3 = this.getNextVal(expr);
                if(expr.length() > v3.length()) {
                    expr = expr.substring(v3.length());
                } else if(expr.length() == v3.length()) {
                    expr = expr.replace(v3, "");
                }

                if(this.isExpr(v3)) {
                    if(v3.startsWith(String.valueOf('(')) && v3.endsWith(String.valueOf(')'))) {
                        v3 = '(' + this.parseExprToSql(v3.substring(1, v3.length() - 1), labelRuleToSql) + ')';
                    }
                } else {
                    v3 = v3.replace(String.valueOf('('), "");
                    v3 = v3.replace(String.valueOf(')'), "");
                }

                parsed = this.toSql(v1, v2, v3, labelRuleToSql);
            }
        }

        return parsed;
    }

    public String toSql(String v1, String v2, String v3) {
        return this.toSql(v1, v2, v3, (Map)null);
    }

    public String toSql(String v1, String v2, String v3, Map<String, String> labelRuleToSql) {
        if(8746 == v2.charAt(0)) {
            return this.getUinionOfTable(v1, v3, labelRuleToSql);
        } else if(8745 == v2.charAt(0)) {
            return this.getIntersectOfTable(v1, v3, labelRuleToSql);
        } else if(45 == v2.charAt(0)) {
            return this.getDifferenceOfTable(v1, v3, labelRuleToSql);
        } else {
            throw new RuntimeException("not well format[" + v1 + v2 + v3 + "]");
        }
    }

    public String getNextVal(String expr) {
        char start = expr.charAt(0);
        if(8746 != start && 8745 != start && 45 != start) {
            int rightPos;
            int i;
            if(40 == start && expr.length() > 1) {
                rightPos = 0;
                i = 0;

                for(int i1 = 1; i1 < expr.length(); ++i1) {
                    if(expr.charAt(i1) == 40) {
                        ++rightPos;
                    }

                    if(rightPos == 0 && expr.charAt(i1) == 41) {
                        i = i1 + 1;
                        break;
                    }

                    if(expr.charAt(i1) == 41) {
                        --rightPos;
                    }
                }

                if(i == 0) {
                    throw new RuntimeException("not match quot" + expr);
                } else {
                    return expr.substring(0, i);
                }
            } else {
                rightPos = 0;

                for(i = 1; i < expr.length(); ++i) {
                    rightPos = i + 1;
                    if(41 == expr.charAt(i) || 40 == expr.charAt(i) || 8746 == expr.charAt(i) || 8745 == expr.charAt(i) || 45 == expr.charAt(i)) {
                        rightPos = i;
                        break;
                    }
                }

                return expr.substring(0, rightPos);
            }
        } else {
            return String.valueOf(start);
        }
    }

    public boolean isRealTable(String str) {
        return str.matches(TABLE_NAME_PARTTERN);
    }

    public boolean isInnerTable(String str) {
        return str.startsWith("select ");
    }

    public boolean isExpr(String str) {
        return str.indexOf(8746) > 0 || str.indexOf(8745) > 0 || str.indexOf(45) > 0;
    }

    public boolean isOpt(String str) {
        return str != null && str.length() == 1?8746 == str.charAt(0) || 8745 == str.charAt(0) || 45 == str.charAt(0):false;
    }

    public static void main(String[] args) {
        GroupCalcSqlPaser sp = new GroupCalcSqlPaser("db2");
        System.out.println(sp.parseExprToSql("D_ABC_201304-((D_BCD_201304¡ÈD_EFG_201304))-D_ABC_201304-(D_hij_201304¡ÈD_EFG_201304)", new HashMap()));
    }
}

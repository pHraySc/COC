package com.ailk.biapp.ci.model.sub;

import com.ailk.biapp.ci.entity.CiLabelRule;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.model.LabelElement;
import com.ailk.biapp.ci.util.DataBaseAdapter;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;

public class EnumLabel extends LabelElement {
    public EnumLabel() {
    }

    public String getConditionSql(CiLabelRule ciLabelRule, CiMdaSysTableColumn column, String asName, Integer interval, Integer updateCycle, boolean isValidate) {
        StringBuffer wherelabel = new StringBuffer("");
        int flag = -1;
        if(ciLabelRule.getLabelFlag() != null) {
            flag = ciLabelRule.getLabelFlag().intValue();
        }

        String dbType = Configure.getInstance().getProperty("CI_BACK_DBTYPE");
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(dbType);
        String attrValsStr;
        if(StringUtil.isNotEmpty(ciLabelRule.getTableName())) {
            attrValsStr = ciLabelRule.getTableName();
            String vals = "VALUE_ID";
            String k = "select " + vals + " from " + attrValsStr;
            if(0 == flag) {
                if(1 == column.getColumnDataTypeId().intValue()) {
                    asName = dataBaseAdapter.getColumnToChar(asName);
                }

                wherelabel.append(" ").append(asName).append(" not in (").append(k).append(")");
            } else if(1 == flag) {
                if(1 == column.getColumnDataTypeId().intValue()) {
                    asName = dataBaseAdapter.getColumnToChar(asName);
                }

                wherelabel.append(" ").append(asName).append("  in (").append(k).append(")");
            }
        } else {
            attrValsStr = ciLabelRule.getAttrVal();
            String[] var14 = attrValsStr.split(",");
            int var15;
            if(0 == flag) {
                if(var14.length != 1 && !isValidate) {
                    if(attrValsStr.length() == attrValsStr.lastIndexOf(",")) {
                        attrValsStr = attrValsStr.substring(0, attrValsStr.length() - 1);
                    }

                    if(2 == column.getColumnDataTypeId().intValue()) {
                        wherelabel.append(" ").append(asName).append(" not in ( ");

                        for(var15 = 0; var15 < var14.length; ++var15) {
                            wherelabel.append("\'").append(var14[var15]).append("\'");
                            if(var15 != var14.length - 1) {
                                wherelabel.append(",");
                            }
                        }

                        wherelabel.append(" )");
                    } else if(1 == column.getColumnDataTypeId().intValue()) {
                        wherelabel.append(" ").append(asName).append(" not in (").append(attrValsStr).append(")");
                    }
                } else if(2 == column.getColumnDataTypeId().intValue()) {
                    wherelabel.append(" ").append(asName).append(" ").append("<>").append(" \'").append(var14[0]).append("\'");
                } else if(1 == column.getColumnDataTypeId().intValue()) {
                    wherelabel.append(" ").append(asName).append(" ").append("<>").append(" ").append(var14[0]);
                }
            } else if(1 == flag) {
                if(var14.length != 1 && !isValidate) {
                    if(attrValsStr.length() == attrValsStr.lastIndexOf(",")) {
                        attrValsStr = attrValsStr.substring(0, attrValsStr.length() - 1);
                    }

                    if(2 == column.getColumnDataTypeId().intValue()) {
                        wherelabel.append(" ").append(asName).append(" in ( ");

                        for(var15 = 0; var15 < var14.length; ++var15) {
                            wherelabel.append("\'").append(var14[var15]).append("\'");
                            if(var15 != var14.length - 1) {
                                wherelabel.append(",");
                            }
                        }

                        wherelabel.append(" )");
                    } else if(1 == column.getColumnDataTypeId().intValue()) {
                        wherelabel.append(" ").append(asName).append(" in (").append(attrValsStr).append(")");
                    }
                } else if(2 == column.getColumnDataTypeId().intValue()) {
                    wherelabel.append(" ").append(asName).append(" = \'").append(var14[0]).append("\'");
                } else if(1 == column.getColumnDataTypeId().intValue()) {
                    wherelabel.append(" ").append(asName).append(" = ").append(var14[0]);
                }
            }
        }

        return wherelabel.toString();
    }
}

package com.ailk.biapp.ci.ia.service.impl;

import com.ailk.biapp.ci.constant.ServiceConstants;
import com.ailk.biapp.ci.dao.ICiCustomListInfoHDao;
import com.ailk.biapp.ci.entity.CiCustomListInfo;
import com.ailk.biapp.ci.entity.CiLabelInfo;
import com.ailk.biapp.ci.entity.CiMdaSysTableColumn;
import com.ailk.biapp.ci.ia.chart.ChartFactory;
import com.ailk.biapp.ci.ia.chart.IChart;
import com.ailk.biapp.ci.ia.dao.IToushiAnalysisJDao;
import com.ailk.biapp.ci.ia.entity.CiIaDimIndexGroup;
import com.ailk.biapp.ci.ia.model.AttributeModel;
import com.ailk.biapp.ci.ia.model.ChartModel;
import com.ailk.biapp.ci.ia.model.FilterModel;
import com.ailk.biapp.ci.ia.model.Guideline;
import com.ailk.biapp.ci.ia.model.WorkSheetModel;
import com.ailk.biapp.ci.ia.service.IToushiAnalysisService;
import com.ailk.biapp.ci.ia.utils.Cache;
import com.ailk.biapp.ci.util.cache.CacheBase;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional
@Service
public class ToushiAnalysisServiceImpl implements IToushiAnalysisService {
    private static Logger log = Logger.getLogger(ToushiAnalysisServiceImpl.class);
    @Autowired
    private ICiCustomListInfoHDao ciCustomListInfoHDao;
    @Autowired
    private IToushiAnalysisJDao toushiAnalysisJDao;

    public ToushiAnalysisServiceImpl() {
    }

    public List<CiCustomListInfo> getCustomGroupList(String groupId) {
        Assert.notNull(groupId, "客户群id不能为空");
        log.debug("客户群id：" + groupId);
        return this.ciCustomListInfoHDao.selectSuccessByCustomGroupId(groupId);
    }

    private void handleIsIsometry(List<AttributeModel> row, String listTableName) {
        Iterator i$ = row.iterator();

        while(true) {
            AttributeModel model;
            CiIaDimIndexGroup ciIaDimIndexGroup;
            int isIsometry;
            do {
                List group;
                do {
                    do {
                        if(!i$.hasNext()) {
                            return;
                        }

                        model = (AttributeModel)i$.next();
                        group = model.getGroup();
                    } while(group == null);
                } while(group.size() != 1);

                ciIaDimIndexGroup = (CiIaDimIndexGroup)group.get(0);
                isIsometry = ciIaDimIndexGroup.getIsIsometry().intValue();
            } while(isIsometry != 1);

            String attrId = model.getAttrId();
            int classifications = ciIaDimIndexGroup.getGradeNum().intValue();
            String columnName = "";
            String talbeName = "";
            if(model.getAttrSource().intValue() == 1) {
                CiMdaSysTableColumn sqlBuf = this.getEffectiveColumnByAttrId(attrId);
                columnName = sqlBuf.getColumnName();
                talbeName = sqlBuf.getCiMdaSysTable().getTableName();
                String dataList = this.getTablePostfix(attrId);
                talbeName = talbeName + dataList;
            } else if(model.getAttrSource().intValue() == 2) {
                columnName = attrId;
                talbeName = listTableName;
            }

            StringBuffer var36 = new StringBuffer();
            var36.append("SELECT MAX(").append(columnName).append(") AS MAXVALUE , MIN(").append(columnName).append(") AS MINVALUE FROM ").append(talbeName);
            new ArrayList();
            List var37 = this.toushiAnalysisJDao.getData(var36.toString());
            Map map = (Map)var37.get(0);
            BigDecimal maxBig = (BigDecimal)map.get("MAXVALUE");
            BigDecimal minBig = (BigDecimal)map.get("MINVALUE");
            double max = maxBig.doubleValue();
            double min = minBig.doubleValue();
            int maxValue = (int)max + 1;
            int minValue = (int)min;
            double distance = (double)(maxValue - minValue);
            long step = Math.round(distance / (double)classifications);
            long[] allPoint = new long[classifications + 1];
            allPoint[0] = (long)minValue;
            allPoint[classifications] = (long)maxValue;
            long[] point = new long[classifications - 1];

            for(int groupList = 0; groupList < classifications - 1; ++groupList) {
                point[groupList] = (long)minValue + step * (long)(groupList + 1);
                allPoint[groupList + 1] = point[groupList];
            }

            ArrayList var38 = new ArrayList();

            for(int i = 0; i < classifications; ++i) {
                CiIaDimIndexGroup groupTmep = new CiIaDimIndexGroup();

                try {
                    PropertyUtils.copyProperties(ciIaDimIndexGroup, groupTmep);
                } catch (IllegalAccessException var33) {
                    var33.printStackTrace();
                } catch (InvocationTargetException var34) {
                    var34.printStackTrace();
                } catch (NoSuchMethodException var35) {
                    var35.printStackTrace();
                }

                groupTmep.setGroupName("[" + allPoint[i] + "-" + allPoint[i + 1] + ")");
                groupTmep.setDimBegin(String.valueOf(allPoint[i]));
                groupTmep.setDimEnd(String.valueOf(allPoint[i + 1]));
                groupTmep.setLeftZoneSign(">=");
                groupTmep.setRightZoneSign("<");
                var38.add(groupTmep);
            }

            model.setGroup(var38);
        }
    }

    public Map<String, Object> generateChart(WorkSheetModel workSheetModel) {
        String listTableName = workSheetModel.getListTableName();
        Assert.notNull(listTableName, "清单表不能为空！");
        String chartType = workSheetModel.getChartType();
        List row = workSheetModel.getRow();
        this.handleIsIsometry(row, listTableName);
        List column = workSheetModel.getColumn();
        List symbols = workSheetModel.getSymbol();
        List filterExclude = workSheetModel.getFilterExclude();
        List filterInclude = workSheetModel.getFilterInclude();
        LinkedHashMap mapD = new LinkedHashMap();
        new LinkedHashMap();
        String xAxisName = "";
        String xAxisUnit = "";
        String yAxisName0 = "";
        String yAxisUnit0 = "";
        String yAxisName1 = "";
        String yAxisUnit1 = "";
        boolean valueAxises = true;
        ArrayList guidelines = new ArrayList();
        String symbol = "";
        ArrayList dataList = new ArrayList();
        listTableName = workSheetModel.getListTableName();
        String selectSql = "SELECT ";
        String fromSql = " FROM " + listTableName + " T ";
        String whereSql = " WHERE 1=1 ";
        StringBuffer groupBySB = new StringBuffer(" GROUP BY ");
        StringBuffer operationSB = new StringBuffer();
        LinkedHashMap leftJoinMap = new LinkedHashMap();
        LinkedHashMap gradeMap = new LinkedHashMap();
        StringBuffer groupBySelect = new StringBuffer();
        int index = 0;
        boolean groupIndex = false;

        int filterSB;
        AttributeModel operationSql;
        int end;
        String sb;
        String leftIt;
        String leftJoinSql;
        String chartModel;
        String dimCols;
        String var61;
        String var70;
        String var83;
        for(filterSB = 0; filterSB < row.size(); ++filterSB) {
            operationSql = (AttributeModel)row.get(filterSB);
            if(filterSB == 0) {
                xAxisUnit = operationSql.getGuidelineUnit();
            }

            end = operationSql.getAttrSource().intValue();
            if(end == 1) {
                CiMdaSysTableColumn groupBySql = this.getEffectiveColumnByAttrId(operationSql.getAttrId());
                sb = groupBySql.getColumnName();
                leftIt = groupBySql.getCiMdaSysTable().getTableName();
                leftJoinSql = this.getTablePostfix(operationSql.getAttrId());
                leftIt = leftIt + leftJoinSql;
                List filterSql = operationSql.getGroup();
                chartModel = "D" + filterSB;
                dimCols = "维度_" + operationSql.getAttrName();
                String[] it = new String[]{chartModel, null};
                StringBuffer chart = new StringBuffer();
                chart.append(" JOIN ").append(leftIt).append(" ").append(chartModel).append(" ON T.").append(ServiceConstants.MAINTABLE_KEYCOLUMN).append("=").append(chartModel).append(".").append(ServiceConstants.MAINTABLE_KEYCOLUMN);
                it[1] = chart.toString();
                if(!leftJoinMap.containsKey(leftIt)) {
                    leftJoinMap.put(leftIt, it);
                }

                String[] columnInfo;
                if(filterSql != null && filterSql.size() != 0) {
                    if(leftJoinMap.containsKey(leftIt)) {
                        columnInfo = (String[])leftJoinMap.get(leftIt);
                        chartModel = columnInfo[0];
                    }

                    gradeMap.put(chartModel + "." + sb, filterSql);
                } else {
                    if(leftJoinMap.containsKey(leftIt)) {
                        columnInfo = (String[])leftJoinMap.get(leftIt);
                        chartModel = columnInfo[0];
                    }

                    index = filterSB;
                    var83 = chartModel + "." + sb + " AS " + dimCols + " , ";
                    groupBySelect.append(var83);
                    groupBySB.append(chartModel).append(".").append(sb).append(" , ");
                }

                mapD.put(operationSql.getAttrName(), dimCols);
            } else if(end == 2) {
                var61 = operationSql.getAttrId();
                List var63 = operationSql.getGroup();
                leftIt = "T";
                leftJoinSql = "维度_" + operationSql.getAttrName();
                if(var63 != null && var63.size() != 0) {
                    gradeMap.put(leftIt + "." + var61, var63);
                } else {
                    index = filterSB;
                    var70 = leftIt + "." + var61 + " AS " + leftJoinSql + " , ";
                    groupBySelect.append(var70);
                    groupBySB.append(leftIt).append(".").append(var61).append(" , ");
                }

                mapD.put(operationSql.getAttrName(), leftJoinSql);
            } else if(end == 3) {
                ;
            }
        }

        String newData;
        String option;
        String map;
        int var68;
        String var78;
        String var82;
        String[] var89;
        String[] var103;
        for(filterSB = 0; filterSB < column.size(); ++filterSB) {
            operationSql = (AttributeModel)column.get(filterSB);
            end = 1;
            if(operationSql.getAggregationId() != null) {
                end = operationSql.getAggregationId().intValue();
            }

            var61 = this.getOperationFun(end);
            sb = operationSql.getCustomSystemAttr();
            boolean var66 = false;
            if(StringUtils.isNotEmpty(sb)) {
                var66 = true;
            }

            if(filterSB == 0) {
                yAxisUnit0 = operationSql.getGuidelineUnit();
            }

            var68 = operationSql.getAttrSource().intValue();
            if(var68 == 1 && !var66) {
                CiMdaSysTableColumn var73 = this.getEffectiveColumnByAttrId(operationSql.getAttrId());
                chartModel = var73.getColumnName();
                dimCols = var73.getCiMdaSysTable().getTableName();
                var78 = this.getTablePostfix(operationSql.getAttrId());
                dimCols = dimCols + var78;
                var82 = "G" + filterSB;
                var83 = "指标_" + operationSql.getAttrName();
                var89 = new String[]{var82, null};
                StringBuffer var94 = new StringBuffer();
                var94.append(" JOIN ").append(dimCols).append(" ").append(var82).append(" ON T.").append(ServiceConstants.MAINTABLE_KEYCOLUMN).append("=").append(var82).append(".").append(ServiceConstants.MAINTABLE_KEYCOLUMN);
                var89[1] = var94.toString();
                if(!leftJoinMap.containsKey(dimCols)) {
                    leftJoinMap.put(dimCols, var89);
                }

                if(leftJoinMap.containsKey(dimCols)) {
                    var103 = (String[])leftJoinMap.get(dimCols);
                    var82 = var103[0];
                }

                map = var61 + "(" + var82 + "." + chartModel + ") AS " + var83 + " , ";
                operationSB.append(map);
                Guideline var111 = new Guideline(var83);
                guidelines.add(var111);
            } else {
                Guideline var81;
                if(var68 == 2 && !var66) {
                    var70 = operationSql.getAttrId();
                    chartModel = "T";
                    dimCols = "指标_" + operationSql.getAttrName();
                    var81 = new Guideline(dimCols);
                    guidelines.add(var81);
                    var82 = var61 + "(" + chartModel + "." + var70 + ") AS " + dimCols + " , ";
                    operationSB.append(var82);
                } else if(var68 == 3 || var66) {
                    for(int var71 = 0; sb.indexOf("^") != -1; ++var71) {
                        int[] var77 = this.getPair(sb);
                        dimCols = sb.substring(0, var77[0]);
                        var78 = sb.substring(var77[0] + 1, var77[1]);
                        if(StringUtils.isNumeric(var78)) {
                            CiMdaSysTableColumn var80 = this.getEffectiveColumnByAttrId(var78);
                            var83 = var80.getColumnName();
                            newData = var80.getCiMdaSysTable().getTableName();
                            option = this.getTablePostfix(operationSql.getAttrId());
                            newData = newData + option;
                            map = "G" + filterSB + var71;
                            String[] map1;
                            if(leftJoinMap.containsKey(newData)) {
                                map1 = (String[])leftJoinMap.get(newData);
                                map = map1[0];
                            } else {
                                map1 = new String[]{map, null};
                                StringBuffer iter = new StringBuffer();
                                iter.append(" JOIN ").append(newData).append(" ").append(map).append(" ON T.").append(ServiceConstants.MAINTABLE_KEYCOLUMN).append("=").append(map).append(".").append(ServiceConstants.MAINTABLE_KEYCOLUMN);
                                map1[1] = iter.toString();
                                if(!leftJoinMap.containsKey(newData)) {
                                    leftJoinMap.put(newData, map1);
                                }
                            }

                            var78 = map + "." + var83;
                        } else {
                            var78 = "T." + var78;
                        }

                        var82 = sb.substring(var77[1] + 1);
                        sb = dimCols + var78 + var82;
                    }

                    chartModel = "指标_" + operationSql.getAttrName();
                    dimCols = var61 + "(" + sb + ") AS " + chartModel + " , ";
                    operationSB.append(dimCols);
                    var81 = new Guideline(chartModel);
                    guidelines.add(var81);
                }
            }
        }

        int var64;
        String[] var87;
        for(filterSB = 0; filterSB < symbols.size(); ++filterSB) {
            operationSql = (AttributeModel)symbols.get(filterSB);
            end = operationSql.getAttrSource().intValue();
            if(end == 1) {
                var64 = operationSql.getAggregationId().intValue();
                sb = this.getOperationFun(var64);
                CiMdaSysTableColumn var72 = this.getEffectiveColumnByAttrId(operationSql.getAttrId());
                leftJoinSql = var72.getColumnName();
                var70 = var72.getCiMdaSysTable().getTableName();
                chartModel = this.getTablePostfix(operationSql.getAttrId());
                var70 = var70 + chartModel;
                dimCols = "S" + filterSB;
                var78 = "指标_" + operationSql.getAttrName();
                var87 = new String[]{dimCols, null};
                StringBuffer var93 = new StringBuffer();
                var93.append(" JOIN ").append(var70).append(" ").append(dimCols).append(" ON T.").append(ServiceConstants.MAINTABLE_KEYCOLUMN).append("=").append(dimCols).append(".").append(ServiceConstants.MAINTABLE_KEYCOLUMN);
                var87[1] = var93.toString();
                if(!leftJoinMap.containsKey(var70)) {
                    leftJoinMap.put(var70, var87);
                }

                if(leftJoinMap.containsKey(var70)) {
                    var89 = (String[])leftJoinMap.get(var70);
                    dimCols = var89[0];
                }

                newData = sb + "(" + dimCols + "." + leftJoinSql + ") AS " + var78 + " , ";
                operationSB.append(newData);
                symbol = var78;
            } else if(end == 2) {
                var64 = operationSql.getAggregationId().intValue();
                sb = this.getOperationFun(var64);
                leftIt = operationSql.getAttrId();
                leftJoinSql = "T";
                var70 = "指标_" + operationSql.getAttrName();
                chartModel = sb + "(" + leftJoinSql + "." + leftIt + ") AS " + var70 + " , ";
                operationSB.append(chartModel);
                symbol = var70;
            } else if(end == 3) {
                var61 = operationSql.getCustomSystemAttr();
                int var65 = operationSql.getAggregationId().intValue();
                leftIt = this.getOperationFun(var65);

                for(var68 = 0; var61.indexOf("^") != -1; ++var68) {
                    int[] var75 = this.getPair(var61);
                    chartModel = var61.substring(0, var75[0]);
                    dimCols = var61.substring(var75[0] + 1, var75[1]);
                    if(StringUtils.isNumeric(dimCols)) {
                        CiMdaSysTableColumn var84 = this.getEffectiveColumnByAttrId(dimCols);
                        var82 = var84.getColumnName();
                        var83 = var84.getCiMdaSysTable().getTableName();
                        newData = this.getTablePostfix(operationSql.getAttrId());
                        var83 = var83 + newData;
                        option = "S" + filterSB + var68;
                        if(leftJoinMap.containsKey(var83)) {
                            var103 = (String[])leftJoinMap.get(var83);
                            option = var103[0];
                        } else {
                            var103 = new String[]{option, null};
                            StringBuffer var115 = new StringBuffer();
                            var115.append(" JOIN ").append(var83).append(" ").append(option).append(" ON T.").append(ServiceConstants.MAINTABLE_KEYCOLUMN).append("=").append(option).append(".").append(ServiceConstants.MAINTABLE_KEYCOLUMN);
                            var103[1] = var115.toString();
                            if(!leftJoinMap.containsKey(var83)) {
                                leftJoinMap.put(var83, var103);
                            }
                        }

                        dimCols = option + "." + var82;
                    } else {
                        dimCols = "T." + dimCols;
                    }

                    var78 = var61.substring(var75[1] + 1);
                    var61 = chartModel + dimCols + var78;
                }

                var70 = "指标_" + operationSql.getAttrName();
                chartModel = leftIt + "(" + var61 + ") AS " + var70 + " , ";
                operationSB.append(chartModel);
                symbol = var70;
            }
        }

        StringBuffer var58 = new StringBuffer();

        for(int var59 = 0; var59 < filterExclude.size(); ++var59) {
            AttributeModel var62 = (AttributeModel)filterExclude.get(var59);
            var64 = var62.getAttrSource().intValue();
            if(var64 == 1) {
                CiMdaSysTableColumn var67 = this.getEffectiveColumnByAttrId(var62.getAttrId());
                leftIt = var67.getColumnName();
                leftJoinSql = var67.getCiMdaSysTable().getTableName();
                var70 = this.getTablePostfix(var62.getAttrId());
                leftJoinSql = leftJoinSql + var70;
                chartModel = "F" + var59;
                String[] var85 = new String[]{chartModel, null};
                StringBuffer var88 = new StringBuffer();
                var88.append(" JOIN ").append(leftJoinSql).append(" ").append(chartModel).append(" ON T.").append(ServiceConstants.MAINTABLE_KEYCOLUMN).append("=").append(chartModel).append(".").append(ServiceConstants.MAINTABLE_KEYCOLUMN);
                var85[1] = var88.toString();
                if(!leftJoinMap.containsKey(leftJoinSql)) {
                    leftJoinMap.put(leftJoinSql, var85);
                }

                if(leftJoinMap.containsKey(leftJoinSql)) {
                    var87 = (String[])leftJoinMap.get(leftJoinSql);
                    chartModel = var87[0];
                }

                this.getFilterSql(var62, chartModel, leftIt, var58, "exclude");
            }
        }

        String var60 = "";
        end = operationSB.lastIndexOf(",");
        if(end != -1) {
            var60 = operationSB.substring(0, end - 1);
        } else {
            var60 = operationSB.toString();
        }

        var61 = "";
        end = groupBySB.lastIndexOf(",");
        if(end != -1) {
            var61 = groupBySB.substring(0, end - 1);
        } else {
            var61 = groupBySB.toString();
        }

        StringBuffer var69 = new StringBuffer();
        Iterator var74 = leftJoinMap.entrySet().iterator();

        while(var74.hasNext()) {
            Entry var76 = (Entry)var74.next();
            String[] var79 = (String[])var76.getValue();
            var69.append(var79[1]);
        }

        leftJoinSql = var69.toString();
        var70 = var58.toString();
        String key;
        AttributeModel var86;
        Iterator var97;
        String var119;
        Iterator var132;
        String var141;
        if(row.size() == 1) {
            var86 = (AttributeModel)row.get(0);
            if(gradeMap.size() == 1) {
                Iterator var90 = gradeMap.entrySet().iterator();

                while(var90.hasNext()) {
                    Entry var91 = (Entry)var90.next();
                    var82 = (String)var91.getKey();
                    List var96 = (List)var91.getValue();
                    var97 = var96.iterator();

                    while(var97.hasNext()) {
                        CiIaDimIndexGroup var102 = (CiIaDimIndexGroup)var97.next();
                        map = var102.getGroupName();
                        var119 = this.getSplitSql(var82, var102);
                        String var120 = selectSql + "\'" + map + "\'" + " AS 维度_GROUPNAME, ";
                        var120 = var120 + var60;
                        var120 = var120 + fromSql + leftJoinSql + whereSql + " AND " + var119 + var70;
                        log.info("一个维度，为分档：" + var120);
                        List entry = this.toushiAnalysisJDao.getData(var120);
                        if(entry != null && entry.size() > 0) {
                            dataList.addAll(entry);
                        }
                    }
                }

                mapD.put(var86.getAttrName(), "维度_GROUPNAME");
            } else {
                dimCols = selectSql + groupBySelect.toString();
                dimCols = dimCols + var60;
                dimCols = dimCols + fromSql + leftJoinSql + whereSql + var70 + var61;
                log.info("一个维度，为group by：" + dimCols);
                List var95 = this.toushiAnalysisJDao.getData(dimCols);
                if(var95 != null && var95.size() > 0) {
                    dataList.addAll(var95);
                }
            }
        } else if(row.size() == 2) {
            String str1;
            String groupName2;
            String str2;
            if(gradeMap.size() == 1) {
                var86 = (AttributeModel)row.get(index);
                dimCols = "";
                var78 = "";
                var82 = "";
                if(var86.getAttrSource().intValue() == 2) {
                    dimCols = var86.getAttrId();
                    var78 = listTableName;
                    var82 = " T." + dimCols + "=? ";
                } else {
                    CiMdaSysTableColumn var98 = this.getEffectiveColumnByAttrId(var86.getAttrId());
                    dimCols = var98.getColumnName();
                    var78 = var98.getCiMdaSysTable().getTableName();
                    newData = this.getTablePostfix(var86.getAttrId());
                    var78 = var78 + newData;
                    String[] var106 = (String[])leftJoinMap.get(var78);
                    var82 = var106[0] + "." + dimCols + "=? " + " GROUP BY " + var106[0] + "." + dimCols;
                }

                var83 = " SELECT " + dimCols + " FROM " + var78 + " GROUP BY " + dimCols;
                new ArrayList();
                List var101 = this.toushiAnalysisJDao.getData(var83);
                Iterator var110 = gradeMap.entrySet().iterator();

                while(var110.hasNext()) {
                    Entry var122 = (Entry)var110.next();
                    var119 = (String)var122.getKey();

                    for(int var126 = 0; var126 < row.size(); ++var126) {
                        AttributeModel var127 = (AttributeModel)row.get(var126);
                        key = "";
                        String value = "";
                        if(var127.getAttrSource().intValue() == 2) {
                            key = listTableName;
                            value = var127.getAttrId();
                        } else {
                            CiMdaSysTableColumn str = this.getEffectiveColumnByAttrId(var127.getAttrId());
                            key = str.getCiMdaSysTable().getTableName();
                            value = str.getColumnName();
                            str1 = this.getTablePostfix(var86.getAttrId());
                            key = key + str1;
                        }

                        String[] var138 = (String[])leftJoinMap.get(key);
                        str1 = var138[0] + "." + value;
                        if(var119.equals(str1)) {
                            mapD.put(var127.getAttrName(), "维度_GROUPNAME");
                        }
                    }

                    List var131 = (List)var122.getValue();
                    Iterator var129 = var101.iterator();

                    while(var129.hasNext()) {
                        Map var135 = (Map)var129.next();
                        Iterator var137 = var131.iterator();

                        while(var137.hasNext()) {
                            CiIaDimIndexGroup var140 = (CiIaDimIndexGroup)var137.next();
                            str1 = var140.getGroupName();
                            groupName2 = this.getSplitSql(var119, var140);
                            str2 = selectSql + groupBySelect.toString() + "\'" + str1 + "\'" + " AS 维度_GROUPNAME, ";
                            str2 = str2 + var60;
                            str2 = str2 + fromSql + leftJoinSql + whereSql + " AND " + groupName2 + " AND " + var82 + var70;
                            log.info("两个维度，一为group by,一为分档：" + str2);
                            log.info("参数：" + var135.get(dimCols));
                            List executeSql = this.toushiAnalysisJDao.getData(str2, new Object[]{var135.get(dimCols)});
                            if(executeSql != null && executeSql.size() > 0) {
                                dataList.addAll(executeSql);
                            }
                        }
                    }
                }
            } else if(gradeMap.size() == 2) {
                Iterator var92 = gradeMap.entrySet().iterator();
                Entry var109 = (Entry)var92.next();
                var78 = (String)var109.getKey();
                List var104 = (List)var109.getValue();
                AttributeModel var100 = (AttributeModel)row.get(0);
                Entry var105 = (Entry)var92.next();
                option = (String)var105.getKey();
                List var124 = (List)var105.getValue();
                AttributeModel var125 = (AttributeModel)row.get(1);
                var132 = var104.iterator();

                while(var132.hasNext()) {
                    CiIaDimIndexGroup var130 = (CiIaDimIndexGroup)var132.next();
                    Iterator var136 = var124.iterator();

                    while(var136.hasNext()) {
                        CiIaDimIndexGroup var139 = (CiIaDimIndexGroup)var136.next();
                        var141 = var130.getGroupName();
                        str1 = this.getSplitSql(var78, var130);
                        groupName2 = var139.getGroupName();
                        str2 = this.getSplitSql(option, var139);
                        String var143 = selectSql + "\'" + var141 + "\'" + " AS 维度_GROUPNAME1, " + "\'" + groupName2 + "\'" + " AS 维度_GROUPNAME2, ";
                        var143 = var143 + operationSB.toString();
                        var143 = var143 + fromSql + leftJoinSql + whereSql + " AND " + str1 + " AND " + str2 + var70;
                        log.info("两个维度，都为分档：" + var143);
                        List temp = this.toushiAnalysisJDao.getData(var143);
                        if(temp != null && temp.size() > 0) {
                            dataList.addAll(temp);
                        }
                    }
                }

                mapD.put(var100.getAttrName(), "维度_GROUPNAME1");
                mapD.put(var100.getAttrName(), "维度_GROUPNAME2");
            } else {
                chartModel = selectSql + groupBySelect.toString();
                chartModel = chartModel + var60;
                chartModel = chartModel + fromSql + leftJoinSql + whereSql + var70 + var61;
                log.info("两个维度，都为group by：" + chartModel);
                List var113 = this.toushiAnalysisJDao.getData(chartModel);
                if(var113 != null && var113.size() > 0) {
                    dataList.addAll(var113);
                }
            }
        } else {
            Assert.state(row.size() > 2, "维度个数不能大于2");
        }

        ChartModel var99 = new ChartModel();
        ArrayList var116 = new ArrayList();
        Iterator var112 = mapD.entrySet().iterator();

        while(var112.hasNext()) {
            Entry var108 = (Entry)var112.next();
            var116.add(var108.getValue());
        }

        var99.setDimCols(var116);
        var99.setGuidelines(guidelines);
        var99.setSymbol(symbol);
        if(StringUtils.isNotEmpty(xAxisUnit)) {
            var99.setxAxisUnit(xAxisUnit);
        }

        if(StringUtils.isNotEmpty(yAxisUnit0)) {
            var99.setyAxisUnit0(yAxisUnit0);
        }

        IChart var114 = ChartFactory.getChartInstance(chartType);
        ArrayList var107 = new ArrayList();
        var97 = var116.iterator();

        while(var97.hasNext()) {
            Object var117 = var97.next();
            map = String.valueOf(var117);
            map = map.replaceAll("维度_", "");
            var107.add(map);
        }

        var97 = guidelines.iterator();

        while(var97.hasNext()) {
            Guideline var121 = (Guideline)var97.next();
            map = var121.getName();
            map = map.replaceAll("指标_", "");
            var107.add(map);
        }

        if(StringUtils.isNotEmpty(symbol)) {
            symbol = symbol.replaceAll("指标_", "");
            var107.add(symbol);
        }

        ArrayList var118 = new ArrayList();

        HashMap var134;
        for(int var123 = 0; var123 < dataList.size(); ++var123) {
            var134 = new HashMap();
            Map var128 = (Map)dataList.get(var123);
            var132 = var128.entrySet().iterator();

            while(var132.hasNext()) {
                Entry var133 = (Entry)var132.next();
                key = (String)var133.getKey();
                Object var142 = var133.getValue();
                var141 = var142.toString();
                var134.put(key, var141);
            }

            var118.add(var128);
        }

        option = var114.createChart(var99, dataList);
        log.debug("图形json:" + option);
        var134 = new HashMap();
        var134.put("option", option);
        var134.put("data", var118);
        var134.put("columnInfo", var107);
        return var134;
    }

    private void getFilterSql(AttributeModel attributeModel, String tableAlise, String columnName, StringBuffer sb, String filterType) {
        int lableTypeId = attributeModel.getLabelTypeId().intValue();
        byte lableTypeId1 = 4;
        List excludeList = attributeModel.getExclude();
        FilterModel exclude = null;
        if(excludeList != null && excludeList.size() > 0) {
            exclude = (FilterModel)excludeList.get(0);
        }

        List includeList = attributeModel.getInclude();
        FilterModel include = null;
        if(includeList != null && includeList.size() > 0) {
            include = (FilterModel)includeList.get(0);
        }

        String exactValue;
        String darkValue;
        if(lableTypeId1 == 6) {
            if(filterType.equals("exclude")) {
                exactValue = exclude.getStartTime();
                darkValue = exclude.getEndTime();
                sb.append(" AND ( ").append(tableAlise).append(".").append(columnName).append(" < ").append("\'").append(exactValue).append("\' ");
                sb.append(" OR ").append(tableAlise).append(".").append(columnName).append(" >= ").append("\'").append(darkValue).append("\' ) ");
            } else if(filterType.equals("include")) {
                exactValue = include.getStartTime();
                darkValue = include.getEndTime();
                sb.append(" AND ").append(tableAlise).append(".").append(columnName).append(" >= ").append("\'").append(exactValue).append("\' ");
                sb.append(" AND ").append(tableAlise).append(".").append(columnName).append(" < ").append("\'").append(darkValue).append("\' ");
            }
        } else {
            String tempStr;
            if(lableTypeId1 == 4) {
                if(filterType.equals("exclude")) {
                    exactValue = exclude.getExactValue();
                    darkValue = exclude.getContiueMinVal();
                    tempStr = exclude.getContiueMaxVal();
                    if(StringUtils.isNotEmpty(exactValue)) {
                        sb.append(" AND ").append(tableAlise).append(".").append(columnName).append(" NOT IN ").append("(").append(exactValue).append(") ");
                    } else {
                        sb.append(" AND ( ").append(tableAlise).append(".").append(columnName).append(" < ").append(darkValue).append(" ");
                        sb.append(" OR ").append(tableAlise).append(".").append(columnName).append(" >= ").append(tempStr).append(" ) ");
                    }
                } else if(filterType.equals("include")) {
                    exactValue = exclude.getExactValue();
                    darkValue = exclude.getContiueMinVal();
                    tempStr = exclude.getContiueMaxVal();
                    if(StringUtils.isNotEmpty(exactValue)) {
                        sb.append(" AND ").append(tableAlise).append(".").append(columnName).append(" IN ").append("(").append(exactValue).append(") ");
                    } else {
                        sb.append(" AND ").append(tableAlise).append(".").append(columnName).append(" >= ").append(darkValue).append(" ");
                        sb.append(" AND ").append(tableAlise).append(".").append(columnName).append(" < ").append(tempStr).append(" ");
                    }
                }
            } else if(lableTypeId1 == 7) {
                if(filterType.equals("exclude")) {
                    exactValue = exclude.getExactValue();
                    darkValue = exclude.getDarkValue();
                    if(StringUtils.isNotEmpty(darkValue)) {
                        sb.append(" AND ").append(tableAlise).append(".").append(columnName).append(" NOT LIKE ").append("\'%").append(exactValue).append("%\' ");
                    } else {
                        tempStr = StringUtils.join(exactValue.split(","), "\',\'");
                        sb.append(" AND ").append(tableAlise).append(".").append(columnName).append(" NOT IN ").append("(\'").append(tempStr).append("\') ");
                    }
                } else if(filterType.equals("include")) {
                    exactValue = include.getExactValue();
                    darkValue = include.getDarkValue();
                    if(StringUtils.isNotEmpty(darkValue)) {
                        sb.append(" AND ").append(tableAlise).append(".").append(columnName).append(" LIKE ").append("\'%").append(exactValue).append("%\' ");
                    } else {
                        tempStr = StringUtils.join(exactValue.split(","), "\',\'");
                        sb.append(" AND ").append(tableAlise).append(".").append(columnName).append(" IN ").append("(\'").append(tempStr).append("\') ");
                    }
                }
            }
        }

    }

    private String getOperationFun(int aggregationId) {
        String operation = "";
        switch(aggregationId) {
        case 1:
            operation = "SUM";
            break;
        case 2:
            operation = "AVG";
            break;
        case 3:
            operation = "COUNT";
            break;
        case 4:
            operation = "MAX";
            break;
        case 5:
            operation = "MIN";
            break;
        default:
            operation = "COUNT";
        }

        return operation;
    }

    private String getSplitSql(String key, CiIaDimIndexGroup ciIaDimIndexGroup) {
        StringBuffer sql = new StringBuffer();
        sql.append(key).append(StringUtils.isEmpty(ciIaDimIndexGroup.getLeftZoneSign())?">=":ciIaDimIndexGroup.getLeftZoneSign()).append(ciIaDimIndexGroup.getDimBegin()).append(" AND ").append(key).append(StringUtils.isEmpty(ciIaDimIndexGroup.getRightZoneSign())?"<":ciIaDimIndexGroup.getRightZoneSign()).append(ciIaDimIndexGroup.getDimEnd());
        return sql.toString();
    }

    private CiMdaSysTableColumn getEffectiveColumnByAttrId(String attrId) {
        CiMdaSysTableColumn ciMdaSysTableColumn = null;
        Cache cache = Cache.getInstance();
        if(StringUtils.isNumeric(attrId)) {
            ciMdaSysTableColumn = cache.getEffectiveColumnByAttrId(Integer.parseInt(attrId));
        }

        return ciMdaSysTableColumn;
    }

    private int[] getPair(String customSystemAttr) {
        int[] pair = new int[2];
        int start = customSystemAttr.indexOf("^");
        String subStr = customSystemAttr.substring(start + 1);
        int end = subStr.indexOf("^");
        pair[0] = start;
        pair[1] = start + end + 1;
        return pair;
    }

    private String getTablePostfix(String attrId) {
        String postfix = "_";
        CiLabelInfo ciLabelInfo = Cache.getInstance().getLabelInfo(Integer.parseInt(attrId));
        int cycle = ciLabelInfo.getUpdateCycle().intValue();
        if(1 == cycle) {
            postfix = postfix + CacheBase.getInstance().getNewLabelDay();
        } else if(2 == cycle) {
            postfix = postfix + CacheBase.getInstance().getNewLabelMonth();
        }

        return postfix;
    }

    public static void main(String[] args) {
        String exactValue = "1,2,3";
        String tempStr = StringUtils.join(exactValue.split(","), "\',\'");
        tempStr = "\'" + tempStr + "\'";
        System.out.println(tempStr);
    }
}

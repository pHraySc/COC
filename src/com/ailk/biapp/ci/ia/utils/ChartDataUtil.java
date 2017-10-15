package com.ailk.biapp.ci.ia.utils;

import com.ailk.biapp.ci.ia.model.ChartModel;
import com.ailk.biapp.ci.ia.model.Guideline;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChartDataUtil {
    public ChartDataUtil() {
    }

    public static void processData(ChartModel chartModel, List<Map<String, Object>> data) {
        processPrefix(chartModel, data);
        List dimCols = chartModel.getDimCols();
        List guidelines = chartModel.getGuidelines();
        HashMap newData = new HashMap();
        if(dimCols.size() != 1) {
            String newDimName;
            if(dimCols.size() == 2 && guidelines.size() == 1) {
                newDimName = String.valueOf(dimCols.get(0));
                String newDimCols2 = String.valueOf(dimCols.get(1));
                String rowMap2 = ((Guideline)guidelines.get(0)).getName();
                ArrayList newDimValue1 = new ArrayList();
                ArrayList i$2 = new ArrayList();

                Object i$1;
                String series;
                Object gl;
                Iterator dim1;
                for(dim1 = data.iterator(); dim1.hasNext(); newData.put(i$1 + "&" + series, gl)) {
                    Map newGuidelines = (Map)dim1.next();
                    i$1 = newGuidelines.get(newDimName);
                    series = String.valueOf(newGuidelines.get(newDimCols2));
                    gl = newGuidelines.get(rowMap2);
                    if(!newDimValue1.contains(i$1)) {
                        newDimValue1.add(i$1);
                    }

                    if(!i$2.contains(series)) {
                        i$2.add(series);
                    }
                }

                data.clear();
                dim1 = newDimValue1.iterator();

                while(dim1.hasNext()) {
                    Object newGuidelines1 = dim1.next();
                    LinkedHashMap i$3 = new LinkedHashMap();
                    i$3.put(newDimName, newGuidelines1);
                    Iterator series1 = i$2.iterator();

                    while(series1.hasNext()) {
                        String gl1 = (String)series1.next();
                        Object value = newData.get(newGuidelines1 + "&" + gl1);
                        if(value != null) {
                            i$3.put(gl1, value);
                        } else {
                            i$3.put(gl1, Integer.valueOf(0));
                        }
                    }

                    data.add(i$3);
                }

                ArrayList dim2 = new ArrayList();
                dim2.add(newDimName);
                chartModel.setDimCols(dim2);
                ArrayList newGuidelines2 = new ArrayList();
                Iterator i$4 = i$2.iterator();

                while(i$4.hasNext()) {
                    series = (String)i$4.next();
                    Guideline gl2 = new Guideline(String.valueOf(series));
                    newGuidelines2.add(gl2);
                }

                chartModel.setGuidelines(newGuidelines2);
            } else {
                newDimName = "";

                Iterator newDimCols;
                Object rowMap;
                for(newDimCols = dimCols.iterator(); newDimCols.hasNext(); newDimName = newDimName + rowMap) {
                    rowMap = newDimCols.next();
                }

                newDimCols = data.iterator();

                while(newDimCols.hasNext()) {
                    Map rowMap1 = (Map)newDimCols.next();
                    String newDimValue = "";

                    Object dim;
                    for(Iterator i$ = dimCols.iterator(); i$.hasNext(); newDimValue = newDimValue + "-" + rowMap1.get(dim)) {
                        dim = i$.next();
                    }

                    rowMap1.put(newDimName, newDimValue.substring(1));
                }

                ArrayList newDimCols1 = new ArrayList();
                newDimCols1.add(newDimName);
                chartModel.setDimCols(newDimCols1);
            }

        }
    }

    private static void processPrefix(ChartModel chartModel, List<Map<String, Object>> data) {
        List dimCols = chartModel.getDimCols();
        ArrayList newDims = new ArrayList();
        List guidelines = chartModel.getGuidelines();
        Iterator i$ = dimCols.iterator();

        String oldName;
        String newName;
        Iterator i$1;
        Map rowMap;
        Object value;
        while(i$.hasNext()) {
            Object guideline = i$.next();
            oldName = String.valueOf(guideline);
            newName = oldName.replace("Î¬¶È_", "");
            newDims.add(newName);
            i$1 = data.iterator();

            while(i$1.hasNext()) {
                rowMap = (Map)i$1.next();
                value = rowMap.get(oldName);
                rowMap.put(newName, value);
                rowMap.remove(oldName);
            }
        }

        chartModel.setDimCols(newDims);
        i$ = guidelines.iterator();

        while(i$.hasNext()) {
            Guideline guideline1 = (Guideline)i$.next();
            oldName = guideline1.getName();
            newName = oldName.replace("Ö¸±ê_", "");
            guideline1.setName(newName);
            i$1 = data.iterator();

            while(i$1.hasNext()) {
                rowMap = (Map)i$1.next();
                value = rowMap.get(oldName);
                if(value == null || String.valueOf(value).length() == 0) {
                    value = Integer.valueOf(0);
                }

                rowMap.put(newName, value);
                rowMap.remove(oldName);
            }
        }

    }
}

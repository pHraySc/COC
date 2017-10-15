package com.ailk.biapp.ci.ia.chart.echarts;

import com.ailk.biapp.ci.ia.chart.IChart;
import com.ailk.biapp.ci.ia.model.ChartModel;
import com.ailk.biapp.ci.ia.model.Guideline;
import com.ailk.biapp.ci.ia.utils.ChartDataUtil;
import com.github.abel533.echarts.axis.Axis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Scatter;
import com.github.abel533.echarts.series.Series;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EChartBubbleImpl implements IChart {
    private static final int MAX_BUBBLE_RADIUS = 20;

    public EChartBubbleImpl() {
    }

    public String createChart(ChartModel chartModel, List<Map<String, Object>> data) {
        ChartDataUtil.processData(chartModel, data);
        String dimCol = (String)chartModel.getDimCols().get(0);
        List guideLines = chartModel.getGuidelines();
        String symbol = chartModel.getSymbol();
        double maxSymbolValue = 0.0D;
        ArrayList legendData = new ArrayList();

        for(int seriesDataMap = 0; seriesDataMap < guideLines.size(); ++seriesDataMap) {
            legendData.add(((Guideline)guideLines.get(seriesDataMap)).getName());
        }

        HashMap var18 = new HashMap();
        Iterator option = data.iterator();

        while(option.hasNext()) {
            Map valueAxis0 = (Map)option.next();
            double valueAxis1 = Double.parseDouble(valueAxis0.get(symbol).toString());
            maxSymbolValue = valueAxis1 > maxSymbolValue?valueAxis1:maxSymbolValue;
            Iterator guideLine = guideLines.iterator();

            while(guideLine.hasNext()) {
                Guideline bubble = (Guideline)guideLine.next();
                String guideLineName = bubble.getName();
                if(var18.get(bubble) == null) {
                    var18.put(bubble, new ArrayList());
                }

                Object[] dataPoint = new Object[]{valueAxis0.get(dimCol), valueAxis0.get(guideLineName), valueAxis0.get(symbol)};
                ((List)var18.get(bubble)).add(dataPoint);
            }
        }

        GsonOption var19 = new GsonOption();
        var19.title(chartModel.getText(), chartModel.getSubtext());
        var19.toolbox().show(Boolean.valueOf(true)).feature(new Object[]{Tool.mark, Tool.dataZoom, Tool.dataView, Tool.restore, Tool.saveAsImage});
        ValueAxis var20 = new ValueAxis();
        var20.axisLabel().formatter("{value}" + chartModel.getxAxisUnit());
        var19.xAxis(new Axis[]{var20});
        ValueAxis var21 = new ValueAxis();
        var21.axisLabel().formatter("{value}" + chartModel.getyAxisUnit0());
        var19.yAxis(new Axis[]{var21});
        Iterator i$ = var18.keySet().iterator();

        while(i$.hasNext()) {
            Guideline var22 = (Guideline)i$.next();
            Scatter var23 = new Scatter();
            var23.data(((List)var18.get(var22)).toArray());
            var23.symbolSize(this.getSymbolSizeFunc(maxSymbolValue));
            var19.series(new Series[]{var23});
        }

        return var19.toString();
    }

    private String getSymbolSizeFunc(double maxSymbolValue) {
        int factor = (int)(maxSymbolValue / 20.0D) + 1;
        String function = "function(value){return Math.round(value[2] / " + factor + ");}";
        return function;
    }
}

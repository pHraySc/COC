package com.ailk.biapp.ci.ia.chart.echarts;

import com.ailk.biapp.ci.ia.chart.IChart;
import com.ailk.biapp.ci.ia.model.ChartModel;
import com.ailk.biapp.ci.ia.model.Guideline;
import com.ailk.biapp.ci.ia.utils.ChartDataUtil;
import com.github.abel533.echarts.Label;
import com.github.abel533.echarts.axis.Axis;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Series;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EChartColumnImpl implements IChart {
    public EChartColumnImpl() {
    }

    public String createChart(ChartModel chartModel, List<Map<String, Object>> data) {
        ChartDataUtil.processData(chartModel, data);
        String dimCol = (String)chartModel.getDimCols().get(0);
        List guideLines = chartModel.getGuidelines();
        ArrayList legendData = new ArrayList();

        for(int axisData = 0; axisData < guideLines.size(); ++axisData) {
            legendData.add(((Guideline)guideLines.get(axisData)).getName());
        }

        ArrayList var14 = new ArrayList();
        HashMap seriesDataMap = new HashMap();
        Iterator option = data.iterator();

        while(option.hasNext()) {
            Map categoryAxis = (Map)option.next();
            var14.add(categoryAxis.get(dimCol));

            Guideline i$;
            String guideLine;
            for(Iterator valueAxis = guideLines.iterator(); valueAxis.hasNext(); ((List)seriesDataMap.get(i$)).add(categoryAxis.get(guideLine))) {
                i$ = (Guideline)valueAxis.next();
                guideLine = i$.getName();
                if(seriesDataMap.get(i$) == null) {
                    seriesDataMap.put(i$, new ArrayList());
                }
            }
        }

        GsonOption var15 = new GsonOption();
        var15.title(chartModel.getText(), chartModel.getSubtext());
        var15.toolbox().show(Boolean.valueOf(true)).feature(new Object[]{Tool.mark, Tool.dataView, new MagicType(new Magic[]{Magic.line, Magic.bar}), Tool.restore, Tool.saveAsImage});
        var15.legend(legendData.toArray());
        CategoryAxis var16 = new CategoryAxis();
        var16.axisLine().onZero(Boolean.valueOf(false));
        var16.axisLabel().formatter("{value}" + chartModel.getxAxisUnit());
        var16.boundaryGap(Boolean.valueOf(true));
        var16.data(var14.toArray());
        var15.xAxis(new Axis[]{var16});
        ValueAxis var17 = new ValueAxis();
        var17.axisLabel().formatter("{value}" + chartModel.getyAxisUnit0());
        var15.yAxis(new Axis[]{var17});
        if(chartModel.getValueAxises() == 2) {
            ValueAxis var18 = new ValueAxis();
            var18.axisLabel().formatter("{value}" + chartModel.getyAxisUnit1());
            var15.yAxis(new Axis[]{var18});
        }

        Iterator var19 = seriesDataMap.keySet().iterator();

        while(var19.hasNext()) {
            Guideline var20 = (Guideline)var19.next();
            Bar bar = new Bar(var20.getName());
            bar.yAxisIndex(Integer.valueOf(var20.getValueAxisIndex()));
            bar.data(((List)seriesDataMap.get(var20)).toArray());
            ((Label)bar.itemStyle().normal().label().show(Boolean.valueOf(true))).position(Position.top);
            var15.series(new Series[]{bar});
        }

        return var15.toString();
    }
}

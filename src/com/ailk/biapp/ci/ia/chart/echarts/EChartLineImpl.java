package com.ailk.biapp.ci.ia.chart.echarts;

import com.ailk.biapp.ci.ia.chart.IChart;
import com.ailk.biapp.ci.ia.model.ChartModel;
import com.ailk.biapp.ci.ia.model.Guideline;
import com.ailk.biapp.ci.ia.utils.ChartDataUtil;
import com.github.abel533.echarts.Label;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.Axis;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EChartLineImpl implements IChart {
    public EChartLineImpl() {
    }

    public String createChart(ChartModel chartModel, List<Map<String, Object>> data) {
        ChartDataUtil.processData(chartModel, data);
        List guidelines = chartModel.getGuidelines();
        String[] legendData = new String[guidelines.size()];
        int guidelineNum = guidelines.size();
        HashMap guidelineMap = new HashMap();

        for(int option = 0; option < guidelineNum; ++option) {
            guidelineMap.put(((Guideline)guidelines.get(option)).getName(), guidelines.get(option));
            legendData[option] = ((Guideline)guidelines.get(option)).getName();
        }

        GsonOption var25 = new GsonOption();
        String text = chartModel.getText();
        String subtext = chartModel.getSubtext();
        Title title = new Title();
        title.text(text).subtext(subtext);
        var25.title(title);
        Tooltip tootip = new Tooltip();
        tootip.trigger(Trigger.item);
        var25.tooltip(tootip);
        var25.legend(legendData);
        var25.toolbox().show(Boolean.valueOf(true)).feature(new Object[]{Tool.mark, Tool.dataView, new MagicType(new Magic[]{Magic.line, Magic.bar}), Tool.restore, Tool.saveAsImage});
        CategoryAxis categoryAxis = new CategoryAxis();
        categoryAxis.axisLine().onZero(Boolean.valueOf(false));
        String categoryFormatter = "{value} " + chartModel.getxAxisUnit();
        categoryAxis.axisLabel().formatter(categoryFormatter);
        ArrayList list = new ArrayList();
        List dimCol = chartModel.getDimCols();

        for(int categoryData = 0; categoryData < data.size(); ++categoryData) {
            Map valuAxis = (Map)data.get(categoryData);
            list.add(valuAxis.get(dimCol.get(0)));
        }

        Object[] var26 = list.toArray(new Object[list.size()]);
        categoryAxis.data(var26);
        categoryAxis.boundaryGap(Boolean.valueOf(true));
        var25.xAxis(new Axis[]{categoryAxis});
        ValueAxis var27 = new ValueAxis();
        var27.name(chartModel.getyAxisName0());
        var27.axisLabel().formatter("{value} " + chartModel.getyAxisUnit0());
        var25.yAxis(new Axis[]{var27});
        if(chartModel.getValueAxises() > 1) {
            ValueAxis series = new ValueAxis();
            series.name(chartModel.getyAxisName1());
            series.axisLabel().formatter("{value} " + chartModel.getyAxisUnit1());
            var25.yAxis(new Axis[]{series});
        }

        LinkedHashMap var28 = new LinkedHashMap();

        for(int optionStr = 0; optionStr < legendData.length; ++optionStr) {
            String name = legendData[optionStr];
            ArrayList values = new ArrayList();

            for(int line = 0; line < data.size(); ++line) {
                Map guideline = (Map)data.get(line);
                values.add(guideline.get(name));
            }

            var28.put(name, values);
            Line var30 = new Line();
            Guideline var31 = (Guideline)guidelineMap.get(name);
            ((Line)var30.name((String)name)).yAxisIndex(Integer.valueOf(var31.getValueAxisIndex()));
            ((Label)var30.itemStyle().normal().label().show(Boolean.valueOf(true))).position(Position.top);
            var30.itemStyle().normal().lineStyle().setShadowColor("rgba(0,0,0,0.4)");
            Object[] lineData = values.toArray(new Object[values.size()]);
            var30.data(lineData);
            var25.series(new Series[]{var30});
        }

        String var29 = var25.toPrettyString();
        return var29;
    }
}

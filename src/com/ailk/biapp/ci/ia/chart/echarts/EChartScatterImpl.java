package com.ailk.biapp.ci.ia.chart.echarts;

import com.ailk.biapp.ci.ia.chart.IChart;
import com.ailk.biapp.ci.ia.model.ChartModel;
import com.ailk.biapp.ci.ia.model.Guideline;
import com.ailk.biapp.ci.ia.utils.ChartDataUtil;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.axis.Axis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.AxisType;
import com.github.abel533.echarts.code.LineType;
import com.github.abel533.echarts.code.PointerType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.ScatterData;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Scatter;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.LineStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EChartScatterImpl implements IChart {
    public EChartScatterImpl() {
    }

    public String createChart(ChartModel chartModel, List<Map<String, Object>> data) {
        ChartDataUtil.processData(chartModel, data);
        GsonOption option = new GsonOption();
        String text = chartModel.getText();
        String subtext = chartModel.getSubtext();
        Title title = new Title();
        title.text(text).subtext(subtext);
        option.title(title);
        option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.cross).lineStyle((new LineStyle()).type(LineType.dashed).width(Integer.valueOf(1)));
        ValueAxis valueAxisX = new ValueAxis();
        String valueFormatterX = "{value}" + chartModel.getxAxisUnit();
        ((ValueAxis)((ValueAxis)valueAxisX.name(chartModel.getxAxisName())).type(AxisType.value)).scale(Boolean.valueOf(true)).axisLabel().formatter(valueFormatterX);
        option.xAxis(new Axis[]{valueAxisX});
        ValueAxis valueAxisY = new ValueAxis();
        String valueFormatterY = "{value}" + chartModel.getyAxisUnit0();
        ((ValueAxis)((ValueAxis)valueAxisY.name(chartModel.getyAxisName0())).type(AxisType.value)).scale(Boolean.valueOf(true)).axisLabel().formatter(valueFormatterY);
        option.yAxis(new Axis[]{valueAxisY});
        List dimColList = chartModel.getDimCols();
        List guidelineList = chartModel.getGuidelines();
        ArrayList list = new ArrayList();

        for(int scatter = 0; scatter < data.size(); ++scatter) {
            Map scatterData = (Map)data.get(scatter);
            Object dimCol = dimColList.get(0);
            Object optionStr = scatterData.get(dimCol);
            Guideline guidelineY = (Guideline)guidelineList.get(0);
            String nameY = guidelineY.getName();
            Object Y = scatterData.get(nameY);
            ScatterData scatterData1 = new ScatterData(optionStr, Y);
            list.add(scatterData1);
        }

        Scatter var22 = new Scatter();
        var22.name("");
        var22.tooltip().trigger(Trigger.item);
        Object[] var23 = list.toArray(new Object[list.size()]);
        var22.data(var23);
        option.series(new Series[]{var22});
        String var24 = option.toPrettyString();
        return var24;
    }
}

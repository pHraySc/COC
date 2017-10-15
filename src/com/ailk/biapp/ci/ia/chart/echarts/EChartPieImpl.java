package com.ailk.biapp.ci.ia.chart.echarts;

import com.ailk.biapp.ci.ia.chart.IChart;
import com.ailk.biapp.ci.ia.model.ChartModel;
import com.ailk.biapp.ci.ia.model.Guideline;
import com.ailk.biapp.ci.ia.utils.ChartDataUtil;
import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.code.Y;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.series.Series;
import java.util.List;
import java.util.Map;

public class EChartPieImpl implements IChart {
    public EChartPieImpl() {
    }

    public String createChart(ChartModel chartModel, List<Map<String, Object>> data) {
        ChartDataUtil.processData(chartModel, data);
        List guidelines = chartModel.getGuidelines();
        GsonOption option = new GsonOption();
        String text = chartModel.getText();
        String subtext = chartModel.getSubtext();
        Title title = new Title();
        title.text(text).subtext(subtext);
        title.x(X.center);
        option.title(title);
        option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
        option.toolbox().show(Boolean.valueOf(true)).feature(new Object[]{Tool.mark, Tool.dataView, Tool.restore, Tool.saveAsImage});
        option.calculable(Boolean.valueOf(true));
        String guideLine = ((Guideline)guidelines.get(0)).getName();
        String dimCol = (String)chartModel.getDimCols().get(0);
        Object[] pieData = new Object[data.size()];
        String[] legendData = new String[data.size()];

        for(int pie = 0; pie < data.size(); ++pie) {
            Map optionStr = (Map)data.get(pie);
            Object value = optionStr.get(guideLine);
            Object name = optionStr.get(dimCol);
            PieData pie1 = new PieData(name.toString(), value);
            legendData[pie] = String.valueOf(name);
            pieData[pie] = pie1;
        }

        ((Legend)((Legend)option.legend().orient(Orient.horizontal).x(X.left)).y(Y.bottom)).data(legendData);
        Pie var17 = new Pie();
        var17.name(dimCol);
        var17.data(pieData);
        option.series(new Series[]{var17});
        String var18 = option.toPrettyString();
        return var18;
    }
}

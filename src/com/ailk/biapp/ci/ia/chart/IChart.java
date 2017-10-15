package com.ailk.biapp.ci.ia.chart;

import com.ailk.biapp.ci.ia.model.ChartModel;
import java.util.List;
import java.util.Map;

public interface IChart {
    String createChart(ChartModel var1, List<Map<String, Object>> var2);
}

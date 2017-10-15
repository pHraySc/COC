package com.ailk.biapp.ci.ia.chart;

import com.ailk.biapp.ci.ia.chart.IChart;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ChartFactory implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public ChartFactory() {
    }

    public static IChart getChartInstance(String chartType) {
        IChart chart = (IChart)applicationContext.getBean(chartType);
        return chart;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ChartFactory.applicationContext = applicationContext;
    }
}

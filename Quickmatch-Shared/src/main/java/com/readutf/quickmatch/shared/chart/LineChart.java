package com.readutf.quickmatch.shared.chart;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class LineChart {

    private final List<Double> dataPoints;
    private final List<String> lables;

    public LineChart(List<Double> dataPoints, List<String> lables) {
        if(dataPoints.size() > 500) dataPoints = dataPoints.subList(0, 500);
        if(lables.size() > 500) lables = lables.subList(0, 500);
        if(dataPoints.size() != lables.size()) throw new IllegalArgumentException("Data points and labels must be the same size");

        this.dataPoints = dataPoints;
        this.lables = lables;
    }
    public LineChart(List<Double> dataPoints) {
        if(dataPoints.size() > 500) dataPoints = dataPoints.subList(0, 500);
        this.dataPoints = dataPoints;
        this.lables = dataPoints.stream().map(String::valueOf).toList();
    }

    public String generate() {
        StringBuilder builder = new StringBuilder();
        builder.append("{type:'line',data:{labels:[");
        for(String label : lables) {
            builder.append("'").append(label).append("',");
        }
        builder.append("],datasets:[{label:'Queue Activity',data:[");
        for(Double dataPoint : dataPoints) {
            builder.append(dataPoint).append(",");
        }
        builder.append("]}]}}");
        return "https://quickchart.io/chart?c=" + URLEncoder.encode(builder.toString(), StandardCharsets.UTF_8);
    }

}

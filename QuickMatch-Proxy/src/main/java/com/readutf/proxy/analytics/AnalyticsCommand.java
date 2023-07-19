package com.readutf.proxy.analytics;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.readutf.proxy.config.ConfigManager;
import com.readutf.proxy.utils.ColorUtils;
import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.chart.LineChart;
import com.readutf.quickmatch.shared.utils.RequestHelper;
import com.velocitypowered.api.command.CommandSource;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CommandAlias("analytics")
public class AnalyticsCommand {

    private final AnalyticsService analyticsService;
//    private final YamlConfig yamlConfig;
    private final List<String> periods = List.of("day", "week", "month");

    public AnalyticsCommand(ConfigManager configManager, Retrofit retrofit) {
//        this.yamlConfig = configManager.getYamlConfig();
        analyticsService = retrofit.create(AnalyticsService.class);
    }

    @Subcommand("queue")
    public void queue(CommandSource source, String queueId, String period) {
        if (periods.stream().noneMatch(s -> s.equalsIgnoreCase(period))) {
            source.sendMessage(ColorUtils.colorize("&cInvalid period. Valid periods: " + String.join(", ", periods)));
            return;
        }

        source.sendMessage(ColorUtils.colorize("&aShowing hourly queue activity for the past " + period + "."));

        long duration = switch (period.toLowerCase()) {
            case "week":
                yield TimeUnit.DAYS.toMillis(7);
            case "month":
                yield TimeUnit.DAYS.toMillis(30);
            default:
                yield TimeUnit.DAYS.toMillis(1);
        };
        ResponseData<List<Double>> response = RequestHelper.getResponse(analyticsService.getQueueActivity(queueId, duration));
        if(response.isError()) {
            source.sendMessage(ColorUtils.colorize("&cError: " + response.getErrorReason()));
            return;
        }
        List<Double> values = response.getData();

        String uploadKey = "";
        System.out.println("uploadKey: " + uploadKey);
        if(uploadKey == null) {
            generateGraph(values, values.stream().max(Comparator.comparingInt(Double::intValue)).orElse(20.0).intValue());
        } else {
            analyticsService.uploadImage(uploadKey, new LineChart(values).generate());
        }

    }

    public static List<String> generateGraph(List<Double> values, int max) {
        int yIncrement = max / 8;

        List<String> lines = new ArrayList<>();

        for (int i = 8; i > 0; i--) {
            StringBuilder line = new StringBuilder();
            for (int size = values.size() - 1; size > Math.max(0, values.size() - 34) - 1; size--) {
                Double current = values.get(size);

                if (current >= (yIncrement * i)) {
                    line.append("⬛");
                } else {
                    line.append("⬜");
                }
            }
            lines.add(line.toString());
        }
        return lines;
    }

}

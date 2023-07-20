package com.readutf.proxy.analytics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.readutf.proxy.utils.ChatGraph;
import com.readutf.proxy.utils.ColorUtils;
import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.chart.LineChart;
import com.readutf.quickmatch.shared.utils.RequestHelper;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.event.ClickEvent;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@CommandAlias("analytics")
public class AnalyticsCommand extends BaseCommand {

    private static final String KEY = System.getProperty("KEY1");

    private final AnalyticsService analyticsService;
    private final List<String> durations = List.of("Day", "Week", "Month");

    private final List<String> hours = List.of("1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00",
            "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "0:00");

    public AnalyticsCommand(Retrofit retrofit) {
        this.analyticsService = retrofit.create(AnalyticsService.class);
    }

    @Subcommand("queue")
    public void queue(Player player, String queueId, String duration) {

        if(KEY == null) {
            player.sendMessage(ColorUtils.colorize("&cNo key found."));
            return;
        }

        if (durations.stream().noneMatch(s -> s.equalsIgnoreCase(duration))) {
            player.sendMessage(ColorUtils.colorize("Invalid duration. Valid durations: " + String.join(", ", durations)));
            return;
        }

        long period = switch (duration.toLowerCase()) {
            case "day" -> 86400000;
            case "week" -> 604800000;
            case "month" -> 2628000000L;
            default -> -1;
        };

        player.sendMessage(ColorUtils.colorize("&7Collecting data..."));

        ResponseData<List<Double>> response = RequestHelper.getResponse(analyticsService.getActivity(queueId, period));
        if (response.isError()) {
            player.sendMessage(ColorUtils.colorize("&cError: " + response.getErrorReason()));
            return;
        }
        List<Double> values = response.getData();


        try {
            player.sendMessage(ColorUtils.colorize("&7Generating chart..."));
            Response<Map<String, Object>> response1 = analyticsService.uploadImage(KEY, new LineChart(values, hours).generate()).execute();
            if (response1.isSuccessful()) {
                Map<String, Object> responseData = response1.body();
                Map<String, Object> data = (Map<String, Object>) responseData.get("data");
                if (data != null) {
                    String url = (String) data.get("url_viewer");
                    if (url != null) {
                        player.sendMessage(ColorUtils.colorize("&aChart: " + url).clickEvent(ClickEvent.openUrl(url)));
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(ColorUtils.colorize("&cError: Failed to upload chart"));


    }
}

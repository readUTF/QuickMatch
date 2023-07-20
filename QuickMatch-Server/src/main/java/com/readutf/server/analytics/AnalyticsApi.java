package com.readutf.server.analytics;

import com.readutf.quickmatch.shared.queue.QueueType;
import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.server.queue.queuetype.QueueTypeStore;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/private/analytics")
@AllArgsConstructor
public class AnalyticsApi {

    private final QueueTypeStore queueTypeStore;
    private final AnalyticsManager analyticsManager;

    @GetMapping("/queueActivity")
    public ResponseData<List<Double>> getQueueActivity(String queueId, long durationMillis) {
        QueueType queueType = queueTypeStore.findById(queueId).orElse(null);
        if(queueType == null) return ResponseData.error("Queue not found");

        List<Double> averageHourlyActivity = analyticsManager.getQueueActivityTracker().getAverageHourlyActivity(new Date(), new Date(System.currentTimeMillis() - durationMillis));


        return ResponseData.success(averageHourlyActivity);
    }

}

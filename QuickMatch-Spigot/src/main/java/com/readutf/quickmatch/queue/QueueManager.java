package com.readutf.quickmatch.queue;

import com.readutf.quickmatch.shared.utils.CachedValue;
import com.readutf.quickmatch.shared.queue.QueueType;
import com.readutf.quickmatch.shared.utils.RequestHelper;
import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.utils.JsonWrapper;
import retrofit2.Retrofit;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class QueueManager {

    private final QueueService queueService;
    private final CachedValue<List<QueueType>> queueTypes;

    public QueueManager(Retrofit retrofit) {
        queueService = retrofit.create(QueueService.class);
        queueTypes = new CachedValue<>(() -> RequestHelper.get(queueService.getQueues()), TimeUnit.MINUTES.toMillis(1));
    }

    public List<QueueType> getQueues(boolean ignoreCache) {
        return queueTypes.getValue(ignoreCache);
    }

    public ResponseData<Map<String, Object>> joinQueue(List<UUID> playerIds, String queueId) {
        return RequestHelper.getResponse(queueService.joinQueue(JsonWrapper.of(playerIds), queueId));
    }

    public ResponseData<List<UUID>> leaveQueue(UUID playerId) {
        return RequestHelper.getResponse(queueService.leaveQueue(playerId));
    }

}

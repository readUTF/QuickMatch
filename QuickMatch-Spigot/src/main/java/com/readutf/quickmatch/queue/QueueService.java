package com.readutf.quickmatch.queue;

import com.readutf.quickmatch.shared.QueueType;
import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.utils.JsonWrapper;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface QueueService {

    @GET("queue/list")
    Call<ResponseData<List<QueueType>>> getQueues();

    @PUT("queue/join")
    Call<ResponseData<Map<String, Object>>> joinQueue(@Query("players") JsonWrapper<List<UUID>> players, @Query("queueId") String queueId);

    @DELETE("queue/leave")
    Call<ResponseData<List<UUID>>> leaveQueue(@Query("playerId") UUID playerId);

}

package com.readutf.proxy.analytics;

import com.readutf.quickmatch.shared.ResponseData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface AnalyticsService {

    @GET("analytics/queueActivity")
    Call<ResponseData<List<Double>>> getQueueActivity(@Query("queueId") String queueId, @Query("durationMillis") long durationMillis);

    @GET("https://api.imgbb.com/1/upload")
    void uploadImage(@Query("key") String key, @Query("image") String image);

}

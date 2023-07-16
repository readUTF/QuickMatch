package com.readutf.quickmatch.shared.intent;

import com.readutf.quickmatch.shared.JoinIntent;
import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.utils.JsonWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IntentService {

    @GET("intent/find")
    Call<ResponseData<JoinIntent>> getIntent(@Query("gameId") String gameId);

    @PUT("intent/update")
    Call<ResponseData<JoinIntent>> updateIntent(@Query("intentString") JsonWrapper<JoinIntent> intentString);

}

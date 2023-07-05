package com.readutf.playerclient.server;

import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.Server;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ServerService {

    @GET("servers/list")
    Call<ResponseData<List<Server>>> getServers();

}

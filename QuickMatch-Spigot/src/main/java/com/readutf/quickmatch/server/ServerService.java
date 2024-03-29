package com.readutf.quickmatch.server;

import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.shared.utils.JsonWrapper;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ServerService {

    @PUT("servers/register")
    Call<ResponseData<Server>> registerServer(@Query("address") String ip, @Query("serverType") String serverType, @Query("port") int port);

    @PUT("servers/registerFull")
    Call<ResponseData<Server>> registerServerFull(@Query("object") JsonWrapper<Server> server);

    @DELETE("servers/unregister")
    Call<ResponseData<Server>> unregisterServer(@Query("serverId") Integer serverId);

}

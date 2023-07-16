package com.readutf.proxy.register;

import com.readutf.quickmatch.shared.ProxyInfo;
import com.readutf.quickmatch.shared.ResponseData;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;
import java.util.UUID;

public interface ProxyService {

    @GET("proxy/register")
    Call<ResponseData<ProxyInfo>> registerProxy(@Query("address") String address, @Query("port") int port);

    @DELETE("proxy/unregister")
    Call<ResponseData<Void>> unregisterProxy(@Query("proxyId") UUID proxyId);

    @GET("proxy/list")
    Call<ResponseData<List<ProxyInfo>>> getProxies();

}

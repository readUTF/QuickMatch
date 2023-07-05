package com.readutf.playerclient.plugin;

import com.readutf.quickmatch.shared.RetrofitHelper;
import retrofit2.Retrofit;

public class QuickMatchProxy {

    private final Hooks hooks;
    private final Retrofit retrofit;

    public QuickMatchProxy(Hooks hooks) {
        this.hooks = hooks;
        this.retrofit = RetrofitHelper.getInstance().setupRetrofit();
    }
}

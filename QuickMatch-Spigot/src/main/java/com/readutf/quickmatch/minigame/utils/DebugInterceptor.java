package com.readutf.quickmatch.minigame.utils;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DebugInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        List<String> lines = new ArrayList<>();

        String method = request.method();
        String url = request.url().toString();
        Response response = chain.proceed(request);
        String code = response.message();
        ResponseBody body = response.body();
        String responseData = "";
        if(body != null) {
            responseData = body.source().peek().readString(Charset.defaultCharset());
        }
        long took = System.currentTimeMillis() - response.sentRequestAtMillis();

        lines.add("[REQUEST] [%s] %s".formatted(method, url));
        lines.add("[RESPONSE] %s [%s]".formatted(responseData, took));

        lines.forEach(System.out::println);

        return response;
    }
}

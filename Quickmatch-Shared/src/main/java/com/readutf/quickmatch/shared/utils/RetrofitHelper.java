package com.readutf.quickmatch.shared.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RetrofitHelper {

    public Retrofit setupRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new DebugInterceptor());

        return new Retrofit.Builder()
                .baseUrl("http://localhost:8080/api/private/")
                .client(builder.build())
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build();
    }


    public static RetrofitHelper instance;

    public static RetrofitHelper getInstance() {
        return instance == null ? new RetrofitHelper() : instance;
    }

    public static class DebugInterceptor implements Interceptor {

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
            if (body != null) {
                responseData = body.source().peek().readString(Charset.defaultCharset());
            }
            long took = System.currentTimeMillis() - response.sentRequestAtMillis();

            lines.add("[REQUEST] [%s] %s".formatted(method, url));
            lines.add("[RESPONSE] %s [%s]".formatted(responseData, took));

            lines.forEach(System.out::println);

            return response;
        }
    }

}

package com.readutf.quickmatch.utils;


import com.readutf.quickmatch.shared.ResponseData;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Optional;

public class RequestHelper {

    public static <T> T get(Call<ResponseData<T>> call) {
        Response<ResponseData<T>> response;
        try {
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (!response.isSuccessful()) return null;
        ResponseData<T> body = response.body();
        if (body == null || !body.isSuccessful()) return null;
        return body.getData();
    }

    public static <T> ResponseData<T> getResponse(Call<ResponseData<T>> call) {
        Response<ResponseData<T>> response;
        try {
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(null, e.getMessage());
        }
        if(!response.isSuccessful()) {
            return new ResponseData<>(null, "Response was not successful");
        }
        return response.body();
    }

    public static <T> T getOrDefault(Call<ResponseData<T>> call, T def) {
        return Optional.ofNullable(get(call)).orElse(def);
    }

}

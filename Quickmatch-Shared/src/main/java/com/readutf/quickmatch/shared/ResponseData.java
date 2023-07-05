package com.readutf.quickmatch.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @AllArgsConstructor @NoArgsConstructor
public class ResponseData<T> {

    T data;
    String errorReason;

    @JsonIgnore
    public boolean isSuccessful() {
        return errorReason == null;
    }

    @JsonIgnore
    public boolean isError() {
        return errorReason != null;
    }

    public static <T> ResponseData<T> success(T data) {
        return new ResponseData<>(data, null);
    }

    public static <T> ResponseData<T> error(String errorReason) {
        return new ResponseData<>(null, errorReason);
    }

}

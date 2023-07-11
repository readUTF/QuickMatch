package com.readutf.quickmatch.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonWrapper<T> {

    private static ObjectMapper objectMapper;

    private final T object;

    private JsonWrapper(T object) {
        this.object = object;
    }

    public static <T> JsonWrapper<T> of(T object) {
        if (objectMapper == null) objectMapper = new ObjectMapper();
        return new JsonWrapper<>(object);
    }

    @SneakyThrows
    @Override
    public String toString() {
        String s = objectMapper.writeValueAsString(object);
        System.out.println(s);
        return s;
    }
}

package com.readutf.quickmatch.shared.serializers;

import com.github.readutf.hermes.serializer.StringSerializer;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class ServerIdSerializer implements StringSerializer<Integer> {

    @Override
    public String serialize(Integer severId) {
        return Base64.getEncoder().encodeToString(ByteBuffer.allocate(4).putInt(severId).array());
    }

    @Override
    public Integer deserialize(String s) {
        return ByteBuffer.wrap(Base64.getDecoder().decode(s)).getInt();
    }
}

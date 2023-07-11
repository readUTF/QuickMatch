package com.readutf.quickmatch.shared.serializers;

import com.github.readutf.hermes.serializer.StringSerializer;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class UUIDSerializer implements StringSerializer<UUID> {

    @Override
    public String serialize(UUID uuid) {
        return Base64.getEncoder().encodeToString(ByteBuffer.allocate(16).putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits()).array());
    }

    @Override
    public UUID deserialize(String s) {
        ByteBuffer wrap = ByteBuffer.wrap(Base64.getDecoder().decode(s));
        long most = wrap.getLong();
        long least = wrap.getLong();
        return new UUID(most, least);
    }
}

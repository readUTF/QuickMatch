package com.readutf.quickmatch.shared.serializers;

import com.github.readutf.hermes.serializer.StringSerializer;
import com.readutf.quickmatch.shared.ServerPing;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class ServerPingSerializer implements StringSerializer<ServerPing> {

    @Override
    public String serialize(ServerPing serverPing) {
        ByteBuffer allocate = ByteBuffer.allocate(24);
        allocate.putLong(serverPing.getServerId().getMostSignificantBits());
        allocate.putLong(serverPing.getServerId().getLeastSignificantBits());
        allocate.putInt(serverPing.getPlayerCount());
        allocate.putInt((int) (serverPing.getTps() * 100));
        return Base64.getEncoder().encodeToString(allocate.array());
    }

    @Override
    public ServerPing deserialize(String s) {
        ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(s));
        return new ServerPing(new UUID(buffer.getLong(), buffer.getLong()), buffer.getInt(), (double) buffer.getInt() / 100.0);
    }
}

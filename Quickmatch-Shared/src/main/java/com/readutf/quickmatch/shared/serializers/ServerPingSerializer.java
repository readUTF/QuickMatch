package com.readutf.quickmatch.shared.serializers;

import com.github.readutf.hermes.serializer.StringSerializer;
import com.readutf.quickmatch.shared.ServerPing;

import java.nio.ByteBuffer;
import java.util.Base64;

public class ServerPingSerializer implements StringSerializer<ServerPing> {

    @Override
    public String serialize(ServerPing serverPing) {
        ByteBuffer allocate = ByteBuffer.allocate(12);
        allocate.putInt(serverPing.getServerId());
        allocate.putInt(serverPing.getPlayerCount());
        allocate.putInt((int) (serverPing.getTps() * 100));
        return Base64.getEncoder().encodeToString(allocate.array());
    }

    @Override
    public ServerPing deserialize(String s) {
        ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(s));
        return new ServerPing(buffer.getInt(), buffer.getInt(), (double) buffer.getInt() / 100.0);
    }
}

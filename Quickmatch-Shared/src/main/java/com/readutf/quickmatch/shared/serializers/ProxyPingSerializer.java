package com.readutf.quickmatch.shared.serializers;

import com.github.readutf.hermes.serializer.StringSerializer;
import com.readutf.quickmatch.shared.ProxyPing;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class ProxyPingSerializer implements StringSerializer<ProxyPing> {

    @Override
    public String serialize(ProxyPing proxyPing) {
        UUID proxyId = proxyPing.getProxyId();
        return Base64.getEncoder().encodeToString(
                ByteBuffer
                        .allocate(20)
                        .putLong(proxyId.getLeastSignificantBits())
                        .putLong(proxyId.getMostSignificantBits())
                        .putInt(proxyPing.getOnline()).array()
        );
    }

    @Override
    public ProxyPing deserialize(String s) throws Exception {
        ByteBuffer wrap = ByteBuffer.wrap(Base64.getDecoder().decode(s));
        long leastSignificantBits = wrap.getLong();
        long mostSignificantBits = wrap.getLong();
        int online = wrap.getInt();
        return new ProxyPing(new UUID(mostSignificantBits, leastSignificantBits), online);
    }
}

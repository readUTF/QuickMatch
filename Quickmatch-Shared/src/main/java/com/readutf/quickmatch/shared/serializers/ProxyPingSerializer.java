package com.readutf.quickmatch.shared.serializers;

import com.github.readutf.hermes.serializer.StringSerializer;
import com.readutf.quickmatch.shared.ProxyPing;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class ProxyPingSerializer implements StringSerializer<ProxyPing> {

    @Override
    public String serialize(ProxyPing proxyPing) {
        return Base64.getEncoder().encodeToString(
                ByteBuffer
                        .allocate(20)
                        .putInt(proxyPing.getProxyId())
                        .putInt(proxyPing.getOnline()).array()
        );
    }

    @Override
    public ProxyPing deserialize(String s) throws Exception {
        ByteBuffer wrap = ByteBuffer.wrap(Base64.getDecoder().decode(s));
        return new ProxyPing(wrap.getInt(), wrap.getInt());
    }
}

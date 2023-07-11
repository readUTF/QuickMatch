package com.readutf.proxy.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColorUtils {

    public static Component colorize(String message) {
        return LegacyComponentSerializer.legacy('&').deserialize(message);
    }

}

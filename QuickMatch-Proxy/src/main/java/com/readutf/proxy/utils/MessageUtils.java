package com.readutf.proxy.utils;

import com.readutf.proxy.QuickMatchProxy;
import com.velocitypowered.api.proxy.Player;

public class MessageUtils {

    public static void sendPermissionMessage(String message, String permission) {
        for (Player allPlayer : QuickMatchProxy.getInstance().getProxyServer().getAllPlayers()) {
            if(allPlayer.hasPermission(permission)) {
                allPlayer.sendMessage(ColorUtils.colorize(permission));
            }
        }
    }

}

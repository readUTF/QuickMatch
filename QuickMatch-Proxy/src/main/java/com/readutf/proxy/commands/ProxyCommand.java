package com.readutf.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.readutf.proxy.register.ProxyManager;
import com.readutf.proxy.utils.ColorUtils;
import com.readutf.quickmatch.shared.ProxyInfo;
import com.velocitypowered.api.proxy.Player;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.time.DurationFormatUtils;

@CommandAlias("proxy")
@AllArgsConstructor
public class ProxyCommand extends BaseCommand {

    private final ProxyManager proxyManager;

    @CommandAlias("proxys|proxies")
    @Subcommand("list")
    public void listProxies(Player player) {
        player.sendMessage(ColorUtils.colorize("&9&lProxies&7:"));
        for (ProxyInfo allProxy : proxyManager.getAllProxies()) {
            player.sendMessage(ColorUtils.colorize("&7- &b%s &7(&b%s&7)".formatted(
                    allProxy.getName(),
                    DurationFormatUtils.formatDuration(allProxy.getUptime(), "H:mm:ss", true)
            )));
        }
    }

}

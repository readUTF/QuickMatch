package com.readutf.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import com.readutf.proxy.register.ProxyManager;
import com.readutf.proxy.server.ServerManager;
import com.readutf.proxy.utils.ColorUtils;
import com.readutf.quickmatch.shared.ProxyInfo;
import com.readutf.quickmatch.shared.Server;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.event.ClickEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.Comparator;

@CommandAlias("server")
@AllArgsConstructor
public class ServersCommand extends BaseCommand {

    private final ServerManager serverManager;
    private final ProxyManager proxyManager;

    @Subcommand("list")
    @CommandAlias("servers")
    public void servers(CommandSource commandSource) {

        commandSource.sendMessage(ColorUtils.colorize("&9&lProxies&7:"));
        for (ProxyInfo allProxy : proxyManager.getAllProxies()) {
            commandSource.sendMessage(ColorUtils.colorize("&7- &b%s &7(&b%s&7)".formatted(
                    allProxy.getName(),
                    DurationFormatUtils.formatDuration(allProxy.getUptime(), "H:mm:ss", true)
            )));
        }

        serverManager.getTypeToServers().forEach((s, registeredServers) -> {
            commandSource.sendMessage(ColorUtils.colorize("&6&l" + s));
            for (RegisteredServer registeredServer : registeredServers) {
                Server server = serverManager.getServer(registeredServer);
                String text = " &7* &e%s &7(players: %s : lastPing: %s)".formatted(
                        registeredServer.getServerInfo().getName(),
                        registeredServer.getPlayersConnected().size(),
                        ((System.currentTimeMillis() - server.getLastPing()) / 1000)
                );
                commandSource.sendMessage(ColorUtils.colorize(text).clickEvent(ClickEvent.runCommand("/server info " + registeredServer.getServerInfo().getName())));
            }
        });
    }

    @Subcommand("info")
    @CommandCompletion("@servers")
    public void info(CommandSource commandSource, String serverName) {
        if (serverName.startsWith("server-")) {
            serverName = serverName.replace("server-", "");

            int serverId;
            try {
                serverId = Integer.parseInt(serverName);
            } catch (NumberFormatException e) {
                commandSource.sendMessage(ColorUtils.colorize("&cInvalid server id."));
                return;
            }

            Server server = serverManager.getServerById(serverId);
            if (server == null) {
                commandSource.sendMessage(ColorUtils.colorize("&cServer not found."));
                return;
            }

            commandSource.sendMessage(ColorUtils.colorize("&6&lServer-" + server.getServerId()));
            commandSource.sendMessage(ColorUtils.colorize(" &7* &ePlayers: &7" + server.getPlayerCount()));
            commandSource.sendMessage(ColorUtils.colorize(" &7* &eLast Ping: &7" + ((System.currentTimeMillis() - server.getLastPing()) / 1000) + " seconds ago"));
            commandSource.sendMessage(ColorUtils.colorize(" &7* &eType: &7" + server.getServerType()));
            commandSource.sendMessage(ColorUtils.colorize(" &7* &eTps: &7" + server.getTps()));

        } else if (serverName.startsWith("proxy-")) {
            serverName = serverName.replace("proxy-", "");

            int serverId;
            try {
                serverId = Integer.parseInt(serverName);
            } catch (NumberFormatException e) {
                commandSource.sendMessage(ColorUtils.colorize("&cInvalid server id."));
                return;
            }


            ProxyInfo proxyInfo = proxyManager.getAllProxies().stream().filter(proxyInfo1 -> proxyInfo1.getId() == serverId).findFirst().orElse(null);
            if (proxyInfo == null) {
                commandSource.sendMessage(ColorUtils.colorize("&cServer not found."));
                return;
            }
            commandSource.sendMessage(ColorUtils.colorize("&6&lProxy-" + proxyInfo.getId()));
            commandSource.sendMessage(ColorUtils.colorize(" &7* &eUptime: &7" + DurationFormatUtils.formatDuration(proxyInfo.getUptime(), "**H:mm:ss**", true)));
            commandSource.sendMessage(ColorUtils.colorize(" &7* &eOnline: &7" + proxyInfo.getOnline()));
        }
    }

}

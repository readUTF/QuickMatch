package com.readutf.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import com.readutf.proxy.server.ServerManager;
import com.readutf.proxy.utils.ColorUtils;
import com.readutf.quickmatch.shared.Server;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.event.ClickEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

@CommandAlias("server")
@AllArgsConstructor
public class ServersCommand extends BaseCommand {

    private final ServerManager serverManager;

    @Subcommand("list") @CommandAlias("servers")
    public void servers(CommandSource commandSource) {
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

    @Subcommand("info") @CommandCompletion("@servers")
    public void info(CommandSource commandSource, String serverName) {

        serverManager.getServers(false).stream().min(Comparator.comparingInt(value -> StringUtils.getLevenshteinDistance(serverName, value.getShortName()))).ifPresentOrElse(server -> {
            commandSource.sendMessage(ColorUtils.colorize("&6&l" + server.getShortName()));
            commandSource.sendMessage(ColorUtils.colorize(" &7* &eServer ID: &7" + server.getServerId()));
            commandSource.sendMessage(ColorUtils.colorize(" &7* &ePlayers: &7" + server.getPlayerCount()));
            commandSource.sendMessage(ColorUtils.colorize(" &7* &eLast Ping: &7" + ((System.currentTimeMillis() - server.getLastPing()) / 1000) + " seconds ago"));
            commandSource.sendMessage(ColorUtils.colorize(" &7* &eType: &7" + server.getServerType()));
            commandSource.sendMessage(ColorUtils.colorize(" &7* &eTps: &7" + server.getTps()));
        }, () -> {
            commandSource.sendMessage(ColorUtils.colorize("&cNo server found with that name."));
        });
    }

}

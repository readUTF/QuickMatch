package com.readutf.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.readutf.proxy.server.ServerManager;
import com.readutf.proxy.utils.ColorUtils;
import com.readutf.quickmatch.shared.Server;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.AllArgsConstructor;

@CommandAlias("servers")
@AllArgsConstructor
public class ServersCommand extends BaseCommand {

    private final ServerManager serverManager;

    @Default
    public void servers(CommandSource commandSource) {
        serverManager.getTypeToServers().forEach((s, registeredServers) -> {
            commandSource.sendMessage(ColorUtils.colorize("&6&l" + s));
            for (RegisteredServer registeredServer : registeredServers) {
                Server server = serverManager.getServer(registeredServer);
                commandSource.sendMessage(ColorUtils.colorize(" &7* &e%s &7(players: %s : lastPing: %s)".formatted(
                        registeredServer.getServerInfo().getName(),
                        registeredServer.getPlayersConnected().size(),
                        ((System.currentTimeMillis() - server.getLastPing()) / 1000)
                )));
            }
        });
    }

}

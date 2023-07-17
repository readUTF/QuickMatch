package com.readutf.proxy.activity;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import com.readutf.proxy.utils.ColorUtils;
import com.readutf.proxy.utils.UUIDCache;
import com.readutf.quickmatch.shared.profile.LiveProfile;
import com.readutf.quickmatch.shared.profile.LiveProfileManager;
import com.velocitypowered.api.proxy.Player;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class UserInfoCommand extends BaseCommand {

    private final UUIDCache uuidCache;
    private final LiveProfileManager liveProfileManager;

    @CommandAlias("userinfo")
    @CommandCompletion("@players")
    public void userInfo(Player player, String target) {
        UUID playerId = uuidCache.getPlayerId(target);
        if(playerId == null) {
            player.sendMessage(ColorUtils.colorize("&cThat player has never joined the network! (1)"));
            return;
        }
        Optional<LiveProfile> profile = liveProfileManager.getProfile(playerId);
        if(profile.isEmpty()) {
            player.sendMessage(ColorUtils.colorize("&cThat player has never joined the network! (2)"));
            return;
        }
        LiveProfile liveProfile = profile.get();

        String lastServer = liveProfile.getLastServer();
        String lastProxy = liveProfile.getLastProxy();

        player.sendMessage(ColorUtils.colorize("&7┏ &bUsername: %s● &7%s".formatted(liveProfile.isOnline() ? "&a" : "&c", liveProfile.getUsername())));
        player.sendMessage(ColorUtils.colorize("&7┣ &bServer: &7%s".formatted(lastServer)));
        player.sendMessage(ColorUtils.colorize("&7┣ &bProxy: &7%s".formatted("proxy-" + lastProxy)));
        player.sendMessage(ColorUtils.colorize("&7┗ &bJoin Intent: &7%s".formatted(liveProfile.getJoinIntent())));

    }

}

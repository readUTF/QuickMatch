package com.readutf.quickmatch.shared;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class PlayerMessage {

    private String permission;
    private Collection<UUID> playerIds;
    private Collection<String> messages;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final PlayerMessage playerMessage;

        public Builder() {
            this.playerMessage = new PlayerMessage();
        }

        public Builder setPermission(String permission) {
            playerMessage.setPermission(permission);
            return this;
        }

        public Builder setPlayers(UUID... playerIds) {
            playerMessage.setPlayerIds(Arrays.asList(playerIds));
            return this;
        }

        public Builder setPlayers(Collection<UUID> playerIds) {
            playerMessage.setPlayerIds(playerIds);
            return this;
        }

        public Builder setMessages(String... messages) {
            playerMessage.setMessages(Arrays.asList(messages));
            return this;
        }

        public PlayerMessage build() {
            return playerMessage;
        }


    }
}

package com.readutf.quickmatch.hub.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.readutf.quickmatch.queue.QueueManager;
import com.readutf.quickmatch.shared.queue.QueueType;
import com.readutf.quickmatch.shared.ResponseData;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CommandAlias("queue")
public class QueueCommand extends BaseCommand {

    private final QueueManager queueManager;

    public QueueCommand(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @Default
    @CommandCompletion("@queues")
    public void queue(Player player, String queueName) {
        QueueType queueTypeStream = queueManager.getQueues(false).stream().filter(queueType -> queueType.getId().equalsIgnoreCase(queueName)).findFirst().orElse(null);
        if(queueTypeStream == null) {
            player.sendMessage("§cThat queue does not exist.");
            return;
        }

        ResponseData<Map<String, Object>> response = queueManager.joinQueue(Collections.singletonList(player.getUniqueId()), queueName);
        if(response.isSuccessful()) {
            Map<String, Object> data = response.getData();
            player.sendMessage("§aYou have joined the §e" + data.get("queueName") +" §aqueue. You are in position §e" + data.get("position") + "§a.");
        } else {
            player.sendMessage("§c" + response.getErrorReason());
        }

    }

    @Subcommand("leave")
    public void leave(Player player) {
        ResponseData<List<UUID>> response = queueManager.leaveQueue(player.getUniqueId());
        if(response.isError()) {
            player.sendMessage("§c" + response.getErrorReason());
        } else {
            player.sendMessage("§aYou have left the queue.");
        }
    }

}

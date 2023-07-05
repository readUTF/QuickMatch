package com.readutf.server.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readutf.quickmatch.shared.QueueEntry;
import com.readutf.quickmatch.shared.QueueType;
import com.readutf.quickmatch.shared.ResponseData;
import com.readutf.server.queue.queuetype.QueueTypeStore;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/private/queue")
@AllArgsConstructor
public class QueueApi {

    private final QueueTypeStore queueTypeStore;
    private final ObjectMapper objectMapper;
    private final QueueManager queueManager;

    @PutMapping("/join")
    public ResponseData<Map<String, Object>> joinQueue(@RequestParam("players") String playerString, String queueId) {
        try {
            List<UUID> players = objectMapper.readValue(playerString, new TypeReference<>() {});

            Optional<QueueType> queue = queueTypeStore.findById(queueId);
            if (queue.isEmpty()) return ResponseData.error("Queue not found");
            try {
                QueueType queueType = queue.get();
                int position = queueManager.addToQueue(players, queueType);
                return ResponseData.success(Map.of(
                        "queueId", queueType.getId(),
                        "queueName", queueType.getDisplayName(),
                        "position", position
                ));
            } catch (Exception e) {
                return ResponseData.error(e.getMessage());
            }

        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    }

    @DeleteMapping("/leave")
    public ResponseData<Collection<UUID>> leaveQueue(@RequestParam("playerId") UUID playerId) {
        Queue queue = queueManager.getQueue(playerId);
        if (queue == null) return ResponseData.error("You are not in a queue");

        QueueEntry entry = queue.removePlayer(playerId);
        if (entry == null) return ResponseData.error("You are not in a queue");

        return ResponseData.success(entry.getPlayers());
    }

    @GetMapping("/list")
    public ResponseData<List<QueueType>> getQueues() {
        return ResponseData.success(queueTypeStore.findAll());
    }

    @PutMapping("/update")
    public ResponseData<QueueType> createQueue(String queueType) {
        try {
            return ResponseData.success(queueTypeStore.save(objectMapper.readValue(queueType, QueueType.class)));
        } catch (JsonProcessingException e) {
            return ResponseData.error(e.getMessage());
        }

    }

    @GetMapping("/find")
    public ResponseData<QueueType> findQueue(String queueId) {
        return ResponseData.success(queueTypeStore.findById(queueId).orElse(null));
    }

}

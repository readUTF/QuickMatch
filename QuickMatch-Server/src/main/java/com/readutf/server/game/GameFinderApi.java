package com.readutf.server.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readutf.quickmatch.shared.GameData;
import com.readutf.quickmatch.shared.queue.QueueType;
import com.readutf.quickmatch.shared.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/private/gamefinder")
@AllArgsConstructor
public class GameFinderApi {

    ObjectMapper objectMapper;
    GameFinder gameFinder;

    @GetMapping("/find")
    public ResponseData<List<GameData>> findGame(@RequestParam("queueType") String queueTypeString) throws Exception {
        QueueType queueType = objectMapper.readValue(queueTypeString, QueueType.class);

        try {
            return ResponseData.success(gameFinder.findGames(queueType));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

}

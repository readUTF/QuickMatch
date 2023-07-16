package com.readutf.server.joinintent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readutf.quickmatch.shared.JoinIntent;
import com.readutf.quickmatch.shared.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/private/intent")
@AllArgsConstructor
public class IntentApi {

    private final ObjectMapper objectMapper;
    private final IntentManager intentManager;

    @GetMapping("/find")
    public ResponseData<JoinIntent> findIntent(String gameId) {
        return Optional.ofNullable(intentManager.getIntentById(gameId)).map(ResponseData::success).orElse(ResponseData.error("Intent not found"));
    }

    @PutMapping("/update")
    public ResponseData<JoinIntent> updateIntent(String intentString) {
        JoinIntent intent = objectMapper.convertValue(intentString, JoinIntent.class);
        intentManager.saveIntent(intent);
        return ResponseData.success(intent);
    }

}

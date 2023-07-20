package com.readutf.quickmatch.shared.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Getter
public class QueueInfo {

    private String queueType;
    private Integer position;
    private long queuedAt;
    private int inQueue;



}

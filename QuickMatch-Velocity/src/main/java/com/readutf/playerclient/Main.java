package com.readutf.playerclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readutf.quickmatch.shared.QueueType;

import java.util.List;
import java.util.UUID;

public class Main {


    public static void main(String[] args) {
        try {
            System.out.println(new ObjectMapper().writeValueAsString(new QueueType(
                    "2-4", "2 Player Queue (2 teams)", 1, 1, 4
            )));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}

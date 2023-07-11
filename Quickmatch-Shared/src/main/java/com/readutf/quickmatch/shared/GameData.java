package com.readutf.quickmatch.shared;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class GameData {

    Server server;
    String gameId;
    long creationTime;

}

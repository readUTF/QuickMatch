package com.readutf.quickmatch.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class GameData {

    Server server;
    String gameId;
    long creationTime;

}

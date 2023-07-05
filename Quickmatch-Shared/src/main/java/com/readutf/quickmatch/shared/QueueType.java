package com.readutf.quickmatch.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor @NoArgsConstructor @Setter @Getter
public class QueueType {

    private @Id String id;
    private String displayName;
    private int maxTeamSize;
    private int minTeamSize;
    private int amountOfTeams;

    @Override @JsonIgnore
    public String toString() {
        return "QueueType{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", maxTeamSize=" + maxTeamSize +
                ", minTeamSize=" + minTeamSize +
                ", maxTeams=" + amountOfTeams +
                '}';
    }
}

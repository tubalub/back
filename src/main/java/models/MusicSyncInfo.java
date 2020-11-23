package models;

import lombok.Data;

@Data
public class MusicSyncInfo {
    private String link = "";
    private int time = 0;
    private MusicSource source;
    private boolean paused = false;
}

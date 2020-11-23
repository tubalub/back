package tubalubback.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MusicSyncInfo {
    private String link = "";
    private long time = 0;
    private int index = 0;
    private boolean playing = false;
    private List<String> songQ = new ArrayList<>();
    private List<String> history = new ArrayList<>();
}

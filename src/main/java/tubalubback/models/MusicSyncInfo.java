package tubalubback.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MusicSyncInfo {
    private int time = 0;
    private List<String> songQ = new ArrayList<>();
    private List<String> history = new ArrayList<>();
}

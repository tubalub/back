package tubalubback.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tubalubback.models.MusicSyncInfo;
import tubalubback.services.SyncService;

@Controller
@CrossOrigin
public class MusicController {

    @Autowired
    private SyncService syncService;

    @MessageMapping("/update")
    @SendTo("/topic/music")
    public MusicSyncInfo update(MusicSyncInfo input) {
        System.out.println("SongQ:");
        for (String s : input.getSongQ()) {
            System.out.println(s);
        }
        System.out.println("History:");
        for (String s : input.getHistory()) {
            System.out.println(s);
        }
        SyncService.syncUpdate = input;
        SyncService.currentSongStartTime = System.currentTimeMillis();
        return input;
    }

    @GetMapping("/sync")
    public ResponseEntity<MusicSyncInfo> getSync() {
        int secondsSinceStart = (SyncService.currentSongStartTime < 0) ?
                0 : (int) (System.currentTimeMillis() - SyncService.currentSongStartTime) / 1000;
        SyncService.syncUpdate.setTime(secondsSinceStart);
        return ResponseEntity.ok(SyncService.syncUpdate);
    }

    @PostMapping("/sync")
    public ResponseEntity<MusicSyncInfo> postSync(@RequestBody MusicSyncInfo input) {
        try {
            SyncService.syncUpdate = input;
            return ResponseEntity.ok(SyncService.syncUpdate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

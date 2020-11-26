package tubalubback.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import tubalubback.models.MusicSyncInfo;
import tubalubback.services.SyncService;

@Controller
@CrossOrigin
@Log4j2
public class MusicController {

    @Autowired
    private SyncService syncService;

    @MessageMapping("/update")
    @SendTo("/topic/music")
    public MusicSyncInfo update(MusicSyncInfo input) {
        log.info("SongQ:");
        for (String s : input.getSongQ()) {
            log.info(s);
        }
        System.out.println("History:");
        for (String s : input.getHistory()) {
            log.info(s);
        }
        SyncService.syncUpdate = input;
        SyncService.currentSongStartTime = System.currentTimeMillis();
        log.info("Updated syncUpdate: {}", SyncService.syncUpdate);
        return input;
    }

    @GetMapping("/sync")
    public ResponseEntity<MusicSyncInfo> getSync() {
        int secondsSinceStart = (SyncService.currentSongStartTime < 0) ?
                0 : (int) (System.currentTimeMillis() - SyncService.currentSongStartTime) / 1000;
        SyncService.syncUpdate.setTime(secondsSinceStart);
        log.info("User requested syncUpdate: {}", SyncService.syncUpdate);
        return ResponseEntity.ok(SyncService.syncUpdate);
    }

}

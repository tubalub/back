package tubalubback.controllers;

import lombok.extern.log4j.Log4j2;
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

    @MessageMapping("/update")
    @SendTo("/topic/music")
    public MusicSyncInfo update(MusicSyncInfo input) {
        log.info("SongQ:");
        for (String s : input.getSongQ()) {
            log.info(s);
        }
        log.info("History:");
        for (String s : input.getHistory()) {
            log.info(s);
        }

        // if size of songQ+history is not the same, we know something was uploaded
        // vice versa for going to next song in queue
        // Also want to set time on first upload
        int clientSongCount = input.getSongQ().size() + input.getHistory().size();
        MusicSyncInfo temp = SyncService.getSyncUpdate();
        int serverSongCount = temp.getSongQ().size() + temp.getHistory().size();

        if (serverSongCount == 0 || clientSongCount == serverSongCount) {
            SyncService.currentSongStartTime = System.currentTimeMillis();
        }

        log.info("Current Song Start Time: {}", SyncService.currentSongStartTime);
        MusicSyncInfo ret = SyncService.setSyncUpdate(input);
        log.info("Updated syncUpdate: {}", ret);
        return ret;
    }

    @GetMapping("/sync")
    public ResponseEntity<MusicSyncInfo> getSync() {
        int secondsSinceStart = (SyncService.currentSongStartTime < 0) ?
                0 : (int) (System.currentTimeMillis() - SyncService.currentSongStartTime) / 1000;
        MusicSyncInfo ret = SyncService.setElapsedTime((secondsSinceStart));
        log.info("User requested syncUpdate: {}", ret);
        return ResponseEntity.ok(ret);
    }

}

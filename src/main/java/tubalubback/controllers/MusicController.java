package tubalubback.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    public MusicSyncInfo playPause(MusicSyncInfo input) {
        System.out.println(input.getTime());
        syncService.syncUpdate = input;
        return input;
    }



}

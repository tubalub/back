package tubalubback.controllers;

import tubalubback.models.MusicSyncInfo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MusicController {

    @MessageMapping
    @SendTo("/music")
    public MusicSyncInfo playPause(MusicSyncInfo input) {
        return input;
    }

}

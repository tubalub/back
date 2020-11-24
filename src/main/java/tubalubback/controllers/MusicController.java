package tubalubback.controllers;

import tubalubback.models.MusicSyncInfo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MusicController {


    @MessageMapping("/update")
    @SendTo("/topic/music")
    public MusicSyncInfo playPause(MusicSyncInfo input) {
        return input;
    }

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public String test(String input) {
        System.out.println(input);
        return input;
    }
}

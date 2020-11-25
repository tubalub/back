package tubalubback.controllers;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import tubalubback.models.MusicSyncInfo;

@Controller
@CrossOrigin
public class MusicController {

    @MessageMapping("/update")
    @SendTo("/topic/music")
    public MusicSyncInfo playPause(MusicSyncInfo input) {
        System.out.println(input.getTime());
        return input;
    }

//    @MessageMapping("/test")
//    @SendTo("/topic/music")
//    public String test(String input) {
//        System.out.println(input);
//        return input;
//    }

}

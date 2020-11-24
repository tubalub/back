package tubalubback.controllers;

import tubalubback.models.MusicSyncInfo;
import tubalubback.models.OutputMessage;
import tubalubback.models.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    @SendTo("/test")
    public String test(String input) {
        System.out.println(input);
        return input;
    }
    
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        System.out.println(message);
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }
}

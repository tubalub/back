package tubalubback.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import tubalubback.services.SyncService;
import tubalubback.utils.SyncUtils;

import java.util.Collection;

@Controller
@CrossOrigin
@Log4j2
public class UserController {

    @Autowired
    private SimpMessagingTemplate simp;

    @MessageMapping("/users")
    @SendTo(SyncUtils.USER_WS_DESTINATION)
    public Collection<String> wsUserUpdate() {
        return SyncService.usernames.values();
    }

    @GetMapping("/users")
    public ResponseEntity<Collection<String>> getAllUsers() {
        log.info("Current userlist: {}", SyncService.usernames.values());
        return ResponseEntity.ok(SyncService.usernames.values());
    }

}

package tubalubback.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tubalubback.services.SyncService;

import java.util.Set;

@Controller
@CrossOrigin
@Log4j2
public class UserController {

    private static final String USER_WS_DESTINATION = "/topic/users";

    @Autowired
    private SimpMessagingTemplate simp;

    @MessageMapping("/users")
    @SendTo(USER_WS_DESTINATION)
    public Set<String> wsUserUpdate() {
        log.info("Updating users for clients");
        return SyncService.userSet;
    }

    @PutMapping("/user")
    public ResponseEntity<Set<String>> addUser(@RequestBody String username) {
        if (SyncService.userSet.add(username)) {
            simp.convertAndSend(USER_WS_DESTINATION, SyncService.userSet);
            log.info("User joined: {}", username);
            return ResponseEntity.ok(SyncService.userSet);
        }
        return ResponseEntity.status(409).build();
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<Set<String>> deleteUser(@PathVariable String username) {
        SyncService.userSet.remove(username);
        simp.convertAndSend(USER_WS_DESTINATION, SyncService.userSet);
        log.info("User deleted: {}", username);
        return ResponseEntity.ok(SyncService.userSet);
    }

    @GetMapping("/users")
    public ResponseEntity<Set<String>> getAllUsers() {
        log.info("Current userlist: {}", SyncService.userSet);
        return ResponseEntity.ok(SyncService.userSet);
    }

}

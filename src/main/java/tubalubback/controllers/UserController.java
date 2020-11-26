package tubalubback.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tubalubback.services.SyncService;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private SyncService syncService;

    @PutMapping("/user")
    public ResponseEntity<List<String>> login(@RequestBody String username) {
        if (!SyncService.userList.contains(username)) {
            SyncService.userList.add(username);
            return ResponseEntity.ok(SyncService.userList);
        }
        return ResponseEntity.status(409).build();
    }

}

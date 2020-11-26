package tubalubback.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tubalubback.controllers.MusicController;
import tubalubback.models.MusicSyncInfo;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class SyncService {

    private int users = 0;

    public static long currentSongStartTime = -1;
    public static MusicSyncInfo syncUpdate = new MusicSyncInfo();
    private final RestTemplate restTemplate = new RestTemplate();

    public static List<String> userList = new ArrayList<>();

    @EventListener(SessionConnectEvent.class)
    public void webSocketConnected(SessionConnectEvent event) {
        users++;
        log.info("New user connected. Currently {} users", users);
    }

    @EventListener(SessionDisconnectEvent.class)
    public void webSocketDisconnected(SessionDisconnectEvent event) {
        users--;
        log.info("User disconnected. Currently {} users", users);

        if (users < 1) {
            for (String url : syncUpdate.getSongQ()) {
                if (url.contains("s3.amazonaws")) {
                    log.info("Deleting: {}", url);
                    try {
                        restTemplate.delete(url);
                    } catch (NullPointerException e) {
                        log.info("Null element in songQ");
                    }
                }
            }
            for (String url : syncUpdate.getHistory()) {
                if (url.contains("s3.amazonaws")) {
                    log.info("Deleting: {}", url);
                    try {
                        restTemplate.delete(url);
                    } catch (NullPointerException e) {
                        log.info("Null element in history");
                    }
                }
            }
            // reset when all users have disconnected
            syncUpdate = new MusicSyncInfo();
            currentSongStartTime = -1;
        }

    }

}

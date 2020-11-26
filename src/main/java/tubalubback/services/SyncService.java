package tubalubback.services;

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

@Service
public class SyncService {
    private int users = 0;

    public static long currentSongStartTime = -1;
    public static MusicSyncInfo syncUpdate = new MusicSyncInfo();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private static MusicController musicController;

    @EventListener(SessionConnectEvent.class)
    public void webSocketConnected(SessionConnectEvent event) {
        users++;
        System.out.println("New user connected. Currently " + users + " users");
    }

    @EventListener(SessionDisconnectEvent.class)
    public void webSocketDisconnected(SessionDisconnectEvent event) {
        users--;
        System.out.println("User disconnected. Currently " + users + " users");

        if (users < 1) {
            for (String url : syncUpdate.getSongQ()) {
                if (url.contains("s3.amazonaws")) {
                    System.out.println("Deleting: " + url);
                    try {
                        restTemplate.delete(url);
                    } catch (NullPointerException e) {
                        System.out.println("Null element in songQ");
                    }
                }
            }
            for (String url : syncUpdate.getHistory()) {
                if (url.contains("s3.amazonaws")) {
                    System.out.println("Deleting: " + url);
                    try {
                        restTemplate.delete(url);
                    } catch (NullPointerException e) {
                        System.out.println("Null element in history");
                    }
                }
            }
            // reset when all users have disconnected
            syncUpdate = new MusicSyncInfo();
            currentSongStartTime = -1;
        }

    }

}

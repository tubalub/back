package tubalubback.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tubalubback.models.MusicSyncInfo;
import tubalubback.utils.SyncUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Log4j2
public class SyncService {

    private AtomicInteger users = new AtomicInteger(0);

    public static long currentSongStartTime = -1;
    private static MusicSyncInfo syncUpdate = new MusicSyncInfo();
    private final RestTemplate restTemplate = new RestTemplate();

    public static Set<String> userSet = Collections.synchronizedSet(new HashSet<>());

    @EventListener(SessionConnectEvent.class)
    public void webSocketConnected(SessionConnectEvent event) {
        users.addAndGet(1);
        log.info("New user connected. Currently {} users", users);
    }

    @EventListener(SessionDisconnectEvent.class)
    public void webSocketDisconnected(SessionDisconnectEvent event) {
        users.addAndGet(-1);
        log.info("User disconnected. Currently {} users", users);

        synchronized(SyncUtils.SYNC_STRING) {
            if (users.get() < 1) {
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
                setSyncUpdate(new MusicSyncInfo());
                currentSongStartTime = -1;
            }
        }
    }

    public static MusicSyncInfo setSyncUpdate(MusicSyncInfo input) {
        synchronized(SyncUtils.SYNC_STRING) {
            syncUpdate = input;
            return syncUpdate;
        }
    }

    public static MusicSyncInfo getSyncUpdate() {
        synchronized(SyncUtils.SYNC_STRING) {
            return syncUpdate;
        }
    }

    public static MusicSyncInfo setElapsedTime(int time) {
        synchronized(SyncUtils.SYNC_STRING) {
            syncUpdate.setTime(time);
            return syncUpdate;
        }
    }

}

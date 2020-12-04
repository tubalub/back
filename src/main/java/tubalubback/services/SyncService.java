package tubalubback.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tubalubback.models.MusicSyncInfo;
import tubalubback.utils.SyncUtils;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class SyncService {

    // music sync
    public static long currentSongStartTime = -1;
    private static MusicSyncInfo syncUpdate = new MusicSyncInfo();

    // sessionID key, username values
    public final static Map<String, String> usernames = new HashMap<>();

    @Autowired
    private SimpMessagingTemplate simp;

    @Autowired
    private S3Service s3Service;

    @EventListener(SessionConnectEvent.class)
    public void webSocketConnected(SessionConnectEvent event) {
        Message<byte[]> msg = event.getMessage();
        String username = getUsername(msg);
        String sessionID = (String) event.getMessage().getHeaders().get("simpSessionId");
        usernames.put(sessionID, username);

        simp.convertAndSend("/topic/users", usernames.values());

        log.info("{} connected. Currently {} users", username, usernames.size());
    }

    @EventListener(SessionDisconnectEvent.class)
    public void webSocketDisconnected(SessionDisconnectEvent event) {
        String sessionID = event.getSessionId();
        String username = usernames.get(sessionID);
        usernames.remove(sessionID);

        simp.convertAndSend("/topic/users", usernames.values());

        log.info("{} disconnected. Currently {} users", username, usernames.size());

        if (usernames.size() < 1) {
            clearSongLists();
        }
    }

    private String getUsername(Message<byte[]> msg) {
        Principal princ = (Principal) msg.getHeaders().get("simpUser");
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(msg);
        List<String> nativeHeaders = sha.getNativeHeader("username");
        String username = "default";
        if (nativeHeaders != null) {
            username = nativeHeaders.get(0);
        } else {
            if (princ != null) {
                username = princ.getName();
            }
        }
        return username;
    }

    private void clearSongLists() {
        log.info("Empty lobby. Clearing song lists...");
        synchronized (SyncUtils.SYNC_STRING) {
            if (usernames.size() < 1) {
                for (String url : syncUpdate.getSongQ()) {
                    try {
                        if (url.contains("s3.amazonaws")) {
                            if (s3Service.deleteFromURL(url)) {
                                log.info("Deleted: {}", url);
                            } else {
                                log.error("Error deleting: {}", url);
                            }
                        }
                    } catch (NullPointerException e) {
                        log.error("Null element in songQ");
                    }
                }
                for (String url : syncUpdate.getHistory()) {
                    try {
                        if (url.contains("s3.amazonaws")) {

                            if (s3Service.deleteFromURL(url)) {
                                log.info("Deleted: {}", url);
                            } else {
                                log.error("Error deleting: {}", url);
                            }
                        }
                    } catch (NullPointerException e) {
                        log.error("Null element in songQ");
                    }
                }

                // reset when all users have disconnected
                setSyncUpdate(new MusicSyncInfo());
                currentSongStartTime = -1;
            }
        }
    }

    public static MusicSyncInfo setSyncUpdate(MusicSyncInfo input) {
        synchronized (SyncUtils.SYNC_STRING) {
            syncUpdate = input;
            return syncUpdate;
        }
    }

    public static MusicSyncInfo getSyncUpdate() {
        synchronized (SyncUtils.SYNC_STRING) {
            return syncUpdate;
        }
    }

    public static MusicSyncInfo setElapsedTime(int time) {
        synchronized (SyncUtils.SYNC_STRING) {
            syncUpdate.setTime(time);
            return syncUpdate;
        }
    }

}

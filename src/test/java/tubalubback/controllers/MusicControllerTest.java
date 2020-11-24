package tubalubback.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestDescriptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import tubalubback.models.MusicSyncInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MusicControllerTest {

    private ObjectMapper om = new ObjectMapper();

    private WebSocketStompClient stompClient;
    private BlockingQueue<String> blockingQ = new SynchronousQueue<String>() {
    };
    private StompSession session;

    private static final String URL = "ws://localhost:8089/tubalub";

    @BeforeEach
    public void setup() {
        // create websocket client
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        stompClient = new WebSocketStompClient(new SockJsClient(transports));
    }

    @Test
    public void testConnection() throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {
        session = stompClient.connect(URL + "/connect", new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);

        MusicSyncInfo body = new MusicSyncInfo();
        body.setIndex(10);
        String jsonBody = om.writeValueAsString(body);

        session.subscribe("/topic/music", new DefaultStompFrameHandler());
        session.send("/app/update", jsonBody.getBytes());

        assertEquals(jsonBody, blockingQ.poll(1,TimeUnit.SECONDS));

    }

    class DefaultStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            blockingQ.offer(new String((byte[]) o));
        }
    }
}
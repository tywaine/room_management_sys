package com.hallmanagementsys.hallmanagement.websocket;

import com.hallmanagementsys.hallmanagement.util.Json;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MyWebSocketClient {
    private static final String WEBSOCKET_URL = "wss://rims-backend.tywaine.me/ws";
    private StompSession stompSession;
    private final WebSocketStompClient stompClient;
    private static MyWebSocketClient instance;

    private MyWebSocketClient() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient client = new SockJsClient(transports);
        this.stompClient = new WebSocketStompClient(client);

        // Use your custom ObjectMapper from Json utility class
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(Json.getDefaultObjectMapper());

        this.stompClient.setMessageConverter(converter);
    }

    public static synchronized MyWebSocketClient getInstance() {
        if (instance == null) {
            instance = new MyWebSocketClient();
        }
        return instance;
    }

    public void connect() {
        try {
            stompSession = stompClient.connectAsync(WEBSOCKET_URL, new StompSessionHandlerAdapter() {
                @Override
                public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                    System.out.println("Connected to WebSocket");
                }

                @Override
                public void handleException(StompSession session, StompCommand command,
                                            StompHeaders headers, byte[] payload, Throwable exception) {
                    System.err.println("Error: " + exception.getMessage());
                }
            }).get();
        }
        catch (Exception e) {
            System.err.println("Error connecting to WebSocket: " + e.getMessage());
        }
    }

    public <T> void subscribe(String topic, Class<T> messageType, Consumer<T> messageHandler) {
        if (stompSession != null) {
            stompSession.subscribe(topic, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return messageType;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    messageHandler.accept(messageType.cast(payload));
                }
            });
        }
    }

    public void sendMessage(String destination, Object payload) {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.send("/app" + destination, payload);
            System.out.println("Message sent to " + destination);
        }
        else {
            System.err.println("Not connected to WebSocket");
        }
    }

    public void disconnect() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
            System.out.println("Disconnected from WebSocket");
        }
    }
}

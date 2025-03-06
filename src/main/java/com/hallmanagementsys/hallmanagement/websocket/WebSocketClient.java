package com.hallmanagementsys.hallmanagement.websocket;

import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.function.Consumer;

public class WebSocketClient {
    private final WebSocketStompClient stompClient;
    private StompSession session;

    public WebSocketClient(String serverUrl) {
        this.stompClient = new WebSocketStompClient(new StandardWebSocketClient());

        // Connect asynchronously
        stompClient.connectAsync(serverUrl, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(@NonNull StompSession stompSession, @NonNull StompHeaders connectedHeaders) {
                session = stompSession;
                System.out.println("Connected to WebSocket STOMP server: " + serverUrl);
            }

            @Override
            public void handleException(@NonNull StompSession session, StompCommand command,
                                        @NonNull StompHeaders headers, @NonNull byte[] payload,
                                        @NonNull Throwable exception) {
                exception.printStackTrace();
            }

            @Override
            public void handleTransportError(@NonNull StompSession session, @NonNull Throwable exception) {
                exception.printStackTrace();
            }
        });
    }

    // Subscribe to a topic
    public <T> void subscribe(String topic, Class<T> type, Consumer<T> consumer) {
        if (session != null && session.isConnected()) {
            session.subscribe(topic, new WebSocketStompFrameHandler<>(type, consumer));
        } else {
            System.err.println("STOMP session not connected!");
        }
    }

    // Send a message
    public void send(String destination, Object message) {
        if (session != null && session.isConnected()) {
            session.send(destination, message);
        } else {
            System.err.println("STOMP session not connected!");
        }
    }
}

package com.hallmanagementsys.hallmanagement.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.*;
import java.util.function.Consumer;

public class WebSocketStompFrameHandler<T> implements StompFrameHandler {
    private final Class<T> type;
    private final Consumer<T> messageConsumer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WebSocketStompFrameHandler(Class<T> type, Consumer<T> messageConsumer) {
        this.type = type;
        this.messageConsumer = messageConsumer;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return type;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        try {
            T message = objectMapper.readValue((String) payload, type);
            messageConsumer.accept(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

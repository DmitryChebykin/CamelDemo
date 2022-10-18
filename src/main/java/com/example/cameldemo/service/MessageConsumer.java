package com.example.cameldemo.service;

import org.springframework.messaging.Message;

public interface MessageConsumer {
    void receiveMessage(String payload, Message message);
}

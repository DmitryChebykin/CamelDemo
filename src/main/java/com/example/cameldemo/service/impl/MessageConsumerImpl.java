package com.example.cameldemo.service.impl;

import com.example.cameldemo.config.AppConfig;
import com.example.cameldemo.service.MessageConsumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class MessageConsumerImpl implements MessageConsumer {

    public static final String DELIMITER = "- - - - - - - - - - - - - - - - - - - - - - - -";

    private final ProducerTemplate producerTemplate;

    private final Tracer tracer;

    @JmsListener(destination = AppConfig.APP_QUEUE)
    @Override
    public void receiveMessage(@Payload String payload, Message message) {

        Object traceId = message.getHeaders().get("traceId");

        log.info("traceId <" + traceId + ">");

        log.info("received <" + payload + ">");

        log.info(DELIMITER);
        log.info("######          Message Details           #####");
        log.info(DELIMITER);
        log.info("message headers: " + message.getHeaders());
        log.info(DELIMITER);
        log.info("message payload: " + message.getPayload());
        log.info(DELIMITER);
        log.info("message toString: " + message);
        log.info(DELIMITER);

        List<String> stringList = (List<String>) producerTemplate.requestBody("direct:start", payload);

        log.info("stringList {}", stringList);
    }


}

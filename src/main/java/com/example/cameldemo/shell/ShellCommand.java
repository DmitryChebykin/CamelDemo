package com.example.cameldemo.shell;

import com.example.cameldemo.config.AppConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.ContinueSpan;
import org.springframework.cloud.sleuth.internal.EncodingUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Slf4j
@AllArgsConstructor
@ShellComponent
public class ShellCommand {
    private final JmsTemplate jmsTemplate;

    private final Tracer tracer;

    private final ApplicationContext applicationContext;

    @ShellMethod(value = "Send message")
    @ContinueSpan
    public void send(String myMessage) {

        String[] split = myMessage.split(",");

        CurrentTraceContext currentContext = tracer.currentTraceContext();
        String spanId = tracer.currentSpan().context().spanId();

        String traceId = EncodingUtils.fromLong(RandomUtils.nextLong());
        log.info("parent traceId is <" + traceId + ">");

        TraceContext newTraceContext = tracer.traceContextBuilder()
                .traceId(traceId)
                .parentId(traceId)
                .spanId(spanId)
                .sampled(true)
                .build();

        try (CurrentTraceContext.Scope scope = currentContext.newScope(newTraceContext)) {
            log.info("scope is {}", scope);

            String id = tracer.currentSpan().context().traceId();

            log.info("now traceId is <" + id + ">");

            log.info("sending with convertAndSend() to queue <" + split[0] + ">");

            jmsTemplate.convertAndSend(AppConfig.APP_QUEUE, split[0], message -> {
                message.setStringProperty("traceId", id);
                return message;
            });
        }
    }

    @ShellMethod
    public void stop() {
        SpringApplication.exit(applicationContext, () -> 0);
        System.exit(0);
    }
}

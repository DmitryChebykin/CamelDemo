package com.example.cameldemo.service.impl;

import com.example.cameldemo.dto.ChuckJokeResponseDto;
import com.example.cameldemo.service.ChuckNorrisIO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@AllArgsConstructor
public class ChuckNorrisIOImpl implements ChuckNorrisIO {
    private final WebClient webClient;

    private final Tracer tracer;

    @Override
    public ChuckJokeResponseDto getJoke() {
        String s = tracer.toString();
        String arg = tracer.currentSpan().context().traceId();
        log.info("webClient traceId - {}", arg);
        return webClient
                .get().uri("https://api.chucknorris.io/jokes/random")
                .header("x-b3-traceId", s)
                .exchangeToMono(e -> e.bodyToMono(ChuckJokeResponseDto.class)).block();
    }
}

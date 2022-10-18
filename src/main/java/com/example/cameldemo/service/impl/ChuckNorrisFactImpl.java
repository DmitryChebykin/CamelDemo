package com.example.cameldemo.service.impl;

import com.example.cameldemo.service.ChuckNorrisFact;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.language.bean.Bean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Qualifier("chuckFact")
public class ChuckNorrisFactImpl implements ChuckNorrisFact {
    @Override
    public String getChuckFact(String word) {
        Faker faker = new Faker();
        String fact = faker.chuckNorris().fact();

        log.info("New Chuck Norris fact : {}", fact);

        return word + " : " + fact;
    }
}

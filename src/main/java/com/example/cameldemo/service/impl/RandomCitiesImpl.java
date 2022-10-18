package com.example.cameldemo.service.impl;

import com.example.cameldemo.service.RandomCities;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
@Qualifier("cityGenerate")
public class RandomCitiesImpl implements RandomCities {
    @Override
    public List<String> getCities(String request) {
        Faker faker = new Faker();
        return IntStream.range(0, 4).mapToObj(i -> {
            String name = request + " : " + faker.address().cityName();
            log.info("New City name {}", name);
            return name;
        }).collect(Collectors.toList());
    }
}

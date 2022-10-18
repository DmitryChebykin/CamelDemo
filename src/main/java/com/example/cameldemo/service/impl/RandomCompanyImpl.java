package com.example.cameldemo.service.impl;

import com.example.cameldemo.service.RandomCompany;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RandomCompanyImpl implements RandomCompany {
    @Override
    public String getCompanyName(int i) {
        Faker faker = new Faker();
        String name = faker.company().name();

        log.info("New Company name {}", name);

        return i + " : " + name;
    }
}

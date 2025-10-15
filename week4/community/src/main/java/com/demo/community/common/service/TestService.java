package com.demo.community.common.service;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class TestService {
    public String getTestMessage() {
        return "Hello, daydayHongday!";
    }
    public String getTime() {
        ZonedDateTime now = ZonedDateTime.now();
        return now.getMonth() + "/" + now.getDayOfMonth();
    }
}

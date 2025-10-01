package com.demo.community.common.controller;

import com.demo.community.common.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PingController {
    private final TestService testService;

    // 생성자 1개 → @Autowired 생략 가능
    public PingController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/ping")
    public Map<String, Object> healthCheck() {
        Instant start = Instant.now();

        Map<String, Object> map = new HashMap<>();
        map.put("today", testService.getTime());
        map.put("message", testService.getTestMessage());

        Instant end = Instant.now();
        long latencyMs = Duration.between(start, end).toMillis();
        map.put("ms", latencyMs);

        return map;
    }
}

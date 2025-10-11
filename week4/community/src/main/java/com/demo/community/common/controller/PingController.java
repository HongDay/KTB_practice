package com.demo.community.common.controller;

import com.demo.community.common.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PingController {
    private final TestService testService;

    // 생성자 1개 → @Autowired 생략 가능
    // 혹은 아래를 아예 생략하고 @RequiredArgsController 어노테이션을 클래스에 붙여줘도 됨.
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

    @GetMapping("/status")
    public ResponseEntity<String> getCheck() {
        return ResponseEntity.ok("GET response ok");
    }

    @DeleteMapping("/status")
    public ResponseEntity<String> deleteCheck() {
        return ResponseEntity.ok("DELETE response ok");
    }

    @PostMapping("/status")
    public ResponseEntity<String> postCheck() {
        return ResponseEntity.ok("POST response ok");
    }

    @PutMapping("/status")
    public ResponseEntity<String> putCheck() {
        return ResponseEntity.ok("PUT response ok");
    }
}

package com.demo.community.common.controller;

import com.demo.community.common.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<Void>> getCheck() {
        return ResponseEntity.ok(new ApiResponse<>("GET response ok", null));
    }

    @DeleteMapping("/status")
    public ResponseEntity<ApiResponse<Void>>  deleteCheck() {
        return ResponseEntity.ok(new ApiResponse<>("DELETE response ok", null));
    }

    @PostMapping("/status")
    public ResponseEntity<ApiResponse<Void>>  postCheck() {
        return ResponseEntity.ok(new ApiResponse<>("POST response ok", null));
    }

    @PutMapping("/status")
    public ResponseEntity<ApiResponse<Void>>  putCheck() {
        return ResponseEntity.ok(new ApiResponse<>("PUT response ok", null));
    }
}

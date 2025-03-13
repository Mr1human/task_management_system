package com.timur.taskmanagement.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/healthcheck")
    public ResponseEntity<?> healthcheck() {
        return ResponseEntity.ok("OK");
    }
}

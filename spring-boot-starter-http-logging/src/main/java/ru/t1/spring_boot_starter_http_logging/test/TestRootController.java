package ru.t1.spring_boot_starter_http_logging.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRootController {

    @GetMapping("/")
    public ResponseEntity<String> getRoot() {
        return ResponseEntity.ok("OK");
    }
}
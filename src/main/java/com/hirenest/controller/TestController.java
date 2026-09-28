package com.hirenest.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/protected")
    public Map<String, String> protectedEndpoint(Authentication authentication) {
        return Map.of(
                "message", "Authentication is working",
                "user", authentication.getName()
        );
    }
}

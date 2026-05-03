package com.example.wasteapp.test;

import com.example.wasteapp.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/ping")
    public ResponseEntity<?> ping(Authentication auth) {
        // auth != null samo ako je token prošao i user je autentificiran
        return ResponseEntity.ok("pong, user=" + (auth != null ? auth.getName() : "null"));
    }


}

package com.example.wasteapp.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;

/** UserController sadrži zaštićeni endpoint /api/me koji služi za dohvat
// podataka o trenutno prijavljenom korisniku. Spring Security automatski
// ubacuje Authentication objekt temeljen na JWT tokenu iz Authorization headera.
// Ovaj endpoint frontend koristi kako bi dobio informacije o korisniku nakon prijave.*/

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        return Map.of(
                "email", auth.getName(),
                "authorities", auth.getAuthorities()
        );
    }


    @GetMapping("/listAllUsers")
    public List<User> listAllUsers() {

        return userService.getAllUsers();
    }
}

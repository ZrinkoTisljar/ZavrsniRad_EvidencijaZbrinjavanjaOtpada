package com.example.wasteapp.user;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        var AllUsers = userRepository.findAll();

        return  AllUsers.reversed();
    }

}

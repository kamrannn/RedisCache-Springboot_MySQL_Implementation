package com.example.redis.springbootrediscache.controller;

import com.example.redis.springbootrediscache.model.User;
import com.example.redis.springbootrediscache.repository.UserRepository;
import com.example.redis.springbootrediscache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/add")
    public User save(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/update")
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/redis/{userId}")
    public User getUserByIdRedis(@PathVariable(name = "userId") Integer userId) throws InterruptedException {
        return userService.getUserById(userId);
    }
}
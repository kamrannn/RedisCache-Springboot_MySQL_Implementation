package com.example.redis.springbootrediscache.service;

import com.example.redis.springbootrediscache.model.User;
import com.example.redis.springbootrediscache.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final HashOperations<String, Object, Object> hashOperations;


    public UserService(RedisTemplate<String, Object> redisTemplate, UserRepository userRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        hashOperations = redisTemplate.opsForHash();
    }

    public User create(User user) {
        User savedUser = userRepository.save(user);
        hashOperations.put("USER", savedUser.getId(), savedUser);
        logger.info("User with ID %s saved" + savedUser.getId());
        return savedUser;
    }

    public User update(User user) {
        User updatedUser = userRepository.save(user);
        hashOperations.put("USER", updatedUser.getId(), updatedUser);
        logger.info("User with ID %s updated" + updatedUser.getId());
        return updatedUser;
    }

    public void delete(Integer userId) {
        hashOperations.delete("USER", userId);
        logger.info(String.format("User with ID %s deleted", userId));
    }

    public User getUserById(Integer userId) throws InterruptedException {
        if (redisTemplate.opsForHash().hasKey("USER", userId)) {
            return (User) hashOperations.get("USER", userId);
        } else {
            logger.info("Fetching the user from the database with ID: " + userId);
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                hashOperations.put("USER", user.get().getId(), user.get());
                return user.get();
            } else {
                throw new RuntimeException("User doesn't exists");
            }
        }
    }
}

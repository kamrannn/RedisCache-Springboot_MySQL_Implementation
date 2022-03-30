package com.example.redis.springbootrediscache.repository;

import com.example.redis.springbootrediscache.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}

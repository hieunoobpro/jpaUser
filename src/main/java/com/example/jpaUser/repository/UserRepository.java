package com.example.jpaUser.repository;


import com.example.jpaUser.model.User;
import com.github.javafaker.Faker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByNameContainingIgnoreCase(String name);
    Optional<User> findById(Integer id);
    User save(User user);
    void deleteById(Integer id);
    List<String> findAllAddress();
}

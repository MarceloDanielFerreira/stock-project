package com.app.stockproject.dao;

import com.app.stockproject.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}

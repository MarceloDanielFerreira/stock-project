package com.app.stockproject.dao;

import com.app.stockproject.bean.Role;
import com.app.stockproject.bean.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByActivoIsTrue(Pageable pageable);

    List<User> findAllByRoleAndActivoIsTrue(Role role);

}

package com.asworld.hsms_backend.repository;

import com.asworld.hsms_backend.model.User;
import com.asworld.hsms_backend.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findByRoleAndApprovedFalse(Role role);
}

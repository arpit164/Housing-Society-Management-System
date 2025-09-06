package com.asworld.hsms_backend.service;

import com.asworld.hsms_backend.dto.RegisterRequest;
import com.asworld.hsms_backend.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    List<User> getAllUsers();
    List<User> findAllResidentsPendingApproval();
//    User registerResident(RegisterRequest request);
    User approveResident(Long userId, Long houseId);
}

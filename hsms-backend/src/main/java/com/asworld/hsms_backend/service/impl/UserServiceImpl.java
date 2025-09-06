package com.asworld.hsms_backend.service.impl;

import com.asworld.hsms_backend.dto.RegisterRequest;
import com.asworld.hsms_backend.model.User;
import com.asworld.hsms_backend.model.enums.Role;
import com.asworld.hsms_backend.repository.UserRepository;
import com.asworld.hsms_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllResidentsPendingApproval() {
        return userRepository.findByRoleAndApprovedFalse(Role.RESIDENT);
    }

//    @Override
//    public User registerResident(RegisterRequest request) {
//        User u = User.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .username(request.getUsername())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .mobileNumber(request.getMobileNumber())
//                .role(Role.RESIDENT)
//                .approved(false)
//                .dateOfBirth(request.getDateOfBirth())
//                .build();
//        return userRepository.save(u);
//    }

    @Override
    public User approveResident(Long userId, Long houseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if(!user.getRole().equals(Role.RESIDENT))
            throw new RuntimeException("Only residents can be allocated house");

        user.setHouseId(houseId);
        user.setApproved(true);
        return userRepository.save(user);
    }
}

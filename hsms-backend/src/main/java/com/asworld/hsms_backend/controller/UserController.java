package com.asworld.hsms_backend.controller;

import com.asworld.hsms_backend.dto.RegisterRequest;
import com.asworld.hsms_backend.dto.UserResponse;
import com.asworld.hsms_backend.mapper.UserMapper;
import com.asworld.hsms_backend.model.User;
import com.asworld.hsms_backend.model.enums.Gender;
import com.asworld.hsms_backend.model.enums.Role;
import com.asworld.hsms_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN','MASTER_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MASTER_ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<List<UserResponse>> getPendingResidents() {
        List<UserResponse> pending = userService.findAllResidentsPendingApproval().stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pending);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MASTER_ADMIN')")
    @PutMapping("/approve/{userId}/{houseId}")
    public ResponseEntity<UserResponse> approveResident(@PathVariable Long userId, @PathVariable Long houseId) {
        return ResponseEntity.ok(UserMapper.toResponse(userService.approveResident(userId, houseId)));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }
}

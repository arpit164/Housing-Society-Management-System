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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest req) {
        if(userService.findByEmail(req.getEmail()).isPresent() ||
                userService.findByUsername(req.getUsername()).isPresent()){
            return ResponseEntity.badRequest().build();
        }

        User u = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .mobileNumber(req.getMobileNumber())
                .gender(req.getGender() != null ? Enum.valueOf(Gender.class, req.getGender().toUpperCase()) : null)
                .dateOfBirth(req.getDateOfBirth())
                .role(Role.RESIDENT)
                .approved(false)
                .build();

        return ResponseEntity.ok(UserMapper.toResponse(userService.saveUser(u)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<UserResponse>> getPendingResidents() {
        List<UserResponse> pending = userService.findAllResidentsPendingApproval().stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pending);
    }

    @PutMapping("{userId}/approve/{houseId}")
    public ResponseEntity<UserResponse> approveResident(@PathVariable Long userId, @PathVariable Long houseId) {
        return ResponseEntity.ok(UserMapper.toResponse(userService.approveResident(userId, houseId)));
    }

//    @GetMapping("/{username}")
//    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
//        return userService.findByUsername(username)
//                .map(u -> ResponseEntity.ok(UserResponse.builder()
//                        .id(u.getId())
//                        .firstName(u.getFirstName())
//                        .lastName(u.getLastName())
//                        .email(u.getEmail())
//                        .username(u.getUsername())
//                        .mobileNumber(u.getMobileNumber())
//                        .role(u.getRole())
//                        .gender(u.getGender())
//                        .dateOfBirth(u.getDateOfBirth())
//                        .approved(u.isApproved())
//                        .houseId(u.getHouseId())
//                        .build()))
//                .orElse(ResponseEntity.notFound().build());
//    }
}

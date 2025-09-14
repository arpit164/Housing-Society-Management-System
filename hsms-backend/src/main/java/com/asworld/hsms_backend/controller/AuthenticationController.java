package com.asworld.hsms_backend.controller;

import com.asworld.hsms_backend.dto.AuthResponse;
import com.asworld.hsms_backend.dto.LoginRequest;
import com.asworld.hsms_backend.dto.RegisterRequest;
import com.asworld.hsms_backend.dto.UserResponse;
import com.asworld.hsms_backend.mapper.UserMapper;
import com.asworld.hsms_backend.model.User;
import com.asworld.hsms_backend.model.enums.Gender;
import com.asworld.hsms_backend.model.enums.Role;
import com.asworld.hsms_backend.security.JwtUtil;
import com.asworld.hsms_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isApproved())
            return ResponseEntity.status(403).body("User not approved");

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name()));
    }

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
}

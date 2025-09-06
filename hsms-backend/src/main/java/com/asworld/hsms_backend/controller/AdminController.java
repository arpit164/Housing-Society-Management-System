package com.asworld.hsms_backend.controller;

import com.asworld.hsms_backend.dto.ApproveResidentRequest;
import com.asworld.hsms_backend.model.User;
import com.asworld.hsms_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/pending-residents")
    @PreAuthorize("hasAnyRole('ADMIN','MASTER_ADMIN')")
    public ResponseEntity<List<User>> getPendingResidents() {
        return ResponseEntity.ok(userService.findAllResidentsPendingApproval());
    }

    @PostMapping("/approve-resident")
    @PreAuthorize("hasAnyRole('ADMIN','MASTER_ADMIN')")
    public ResponseEntity<User> approveResident(@RequestBody ApproveResidentRequest request) {
        return ResponseEntity.ok(userService.approveResident(request.getUserId(), request.getHouseId()));
    }
}

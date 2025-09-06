package com.asworld.hsms_backend.dto;

import com.asworld.hsms_backend.model.enums.Gender;
import com.asworld.hsms_backend.model.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String mobileNumber;
    private Role role;
    private Gender gender;
    private LocalDate dateOfBirth;
    private boolean approved;
    private Long houseId;
}

package com.asworld.hsms_backend.mapper;

import com.asworld.hsms_backend.dto.UserResponse;
import com.asworld.hsms_backend.model.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .mobileNumber(user.getMobileNumber())
                .role(user.getRole())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .approved(user.isApproved())
                .houseId(user.getHouseId())
                .build();
    }
}

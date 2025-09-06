package com.asworld.hsms_backend.dto;

import lombok.Data;

@Data
public class ApproveResidentRequest {
    private Long userId;
    private Long houseId;
}

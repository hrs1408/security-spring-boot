package com.hrs1408.springtutorial.models.dtos;

import lombok.Data;

@Data
public class RefreshResponse {
    private String token;

    public RefreshResponse(String token) {
        this.token = token;
    }
}

package com.hrs1408.springtutorial.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    @JsonProperty("refresh_token")
    private String refreshToken;

    public LoginResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}

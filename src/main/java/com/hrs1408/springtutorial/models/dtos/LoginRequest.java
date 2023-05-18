package com.hrs1408.springtutorial.models.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}

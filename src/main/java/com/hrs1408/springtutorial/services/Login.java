package com.hrs1408.springtutorial.services;

import lombok.Getter;
import lombok.extern.java.Log;

public class Login {
    @Getter
    private final Token accessToken;

    @Getter
    private final Token refeshToken;
    private static final Long REFRESH_TOKEN_VALIDITY = 1440L;
    private static final Long ACCESS_TOKEN_VALIDITY = 1L;
    private Login(Token accessToken, Token refeshToken){
        this.accessToken = accessToken;
        this.refeshToken = refeshToken;
    }

    public static Login of(Long userId, String accessSecret, String refeshSecret) {
        return new Login(
                Token.of(userId, ACCESS_TOKEN_VALIDITY, accessSecret),
                Token.of(userId, REFRESH_TOKEN_VALIDITY, refeshSecret)
        );
    }
}

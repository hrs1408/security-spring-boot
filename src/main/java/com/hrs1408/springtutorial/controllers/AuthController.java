package com.hrs1408.springtutorial.controllers;

import com.hrs1408.springtutorial.models.ResponseObject;
import com.hrs1408.springtutorial.models.dtos.LoginRequest;
import com.hrs1408.springtutorial.models.dtos.LoginResponse;
import com.hrs1408.springtutorial.models.dtos.RefreshResponse;
import com.hrs1408.springtutorial.models.dtos.UserRequest;
import com.hrs1408.springtutorial.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AuthController {
    @Autowired
    UserService service;

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseObject> register(@RequestBody UserRequest user) {
        if (!Objects.equals(user.getPassword(), user.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("400", "Password do not match", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("200", "registered", service.insert(user))
        );
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseObject> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        var login = service.login(request);
        Cookie cookie = new Cookie("refresh_token", login.getRefeshToken().getToken());
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("200", "Login success", new LoginResponse(login.getAccessToken().getToken(), login.getRefeshToken().getToken()))
        );
    }

    @GetMapping(value = "/user")
    public ResponseEntity<ResponseObject> user(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization");
        if (refreshToken != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("200", "Success", service.getUserFromToken(refreshToken))
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("404", "User not found", "")
        );
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<ResponseObject> refresh(@CookieValue("refresh_token") String refreshToken) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("200", "Success", new RefreshResponse(service.refresh(refreshToken)))
        );
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<ResponseObject> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("200", "Logout success", "")
        );
    }
}

package com.hrs1408.springtutorial.services;

import com.hrs1408.springtutorial.models.User;
import com.hrs1408.springtutorial.models.dtos.LoginRequest;
import com.hrs1408.springtutorial.models.dtos.UserRequest;
import com.hrs1408.springtutorial.models.dtos.UserResponse;
import com.hrs1408.springtutorial.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final String accessTokenSecret;
    private final String refreshTokenSecret;

    public UserService(
            @Value("${application.security.access-token-secret}") String accessTokenSecret,
            @Value("${application.security.refresh-token-secret}") String refreshTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
    }

    public UserResponse insert(UserRequest request) {
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return modelMapper.map(repository.save(user), UserResponse.class);
    }

    public Login login(LoginRequest request) {
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid email"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid password");
        return Login.of(user.getId(), accessTokenSecret, refreshTokenSecret);
    }

    public UserResponse getUserFromToken(String token) {
        Long id = Token.from(token, refreshTokenSecret);
        return modelMapper.map(repository.findById(id), UserResponse.class);
    }

    public String refresh(String refreshToken) {
        return Token.refresh(refreshToken, refreshTokenSecret);
    }
}

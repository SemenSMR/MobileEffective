package com.example.mobileeffective.controller;


import com.example.mobileeffective.config.UserDetailsImpl;
import com.example.mobileeffective.mappers.UserMapper;
import com.example.mobileeffective.dto.UserLoginRequest;
import com.example.mobileeffective.dto.UserRegistrationRequest;
import com.example.mobileeffective.entity.User;
import com.example.mobileeffective.service.JwtService;
import com.example.mobileeffective.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Validated
@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/auth")
public class AuthController {


    AuthenticationManager authenticationManager;

    UserService userService;

    JwtService jwtService;


    @Operation(summary = "Регистрация пользователя", description = "Позволяет Добавить пользователя в бд")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        User user = UserMapper.toEntityRegister(request);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok("User has successfully registered: " + savedUser.getEmail());
    }

    @Operation(summary = "Авторизация", description = "Позволяет получить токен для дальнейших методов")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest request) {
        User user = UserMapper.toEntityLogin(request);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken((UserDetailsImpl) userDetails);
        return ResponseEntity.ok(token);
    }
}
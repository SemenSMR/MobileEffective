package com.example.mobileeffective.mappers;

import com.example.mobileeffective.dto.UserLoginRequest;
import com.example.mobileeffective.dto.UserRegistrationRequest;
import com.example.mobileeffective.entity.User;

public class UserMapper {
    public static User toEntityRegister(UserRegistrationRequest request) {
        return new User(null, request.getEmail(), request.getPassword(), request.getRole());
    }

    public static User toEntityLogin(UserLoginRequest request) {
        return new User(null, request.getEmail(),request.getPassword(), null);
    }


}

package com.example.mobileeffective.service;


import com.example.mobileeffective.config.UserDetailsImpl;
import com.example.mobileeffective.entity.User;
import com.example.mobileeffective.exception.UserAlreadyExistsException;
import com.example.mobileeffective.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.mobileeffective.exception.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements UserDetailsService {


    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    JwtService jwtService;


    public User saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(user);
    }

    public User getCurrentUserFromToken(String token) {
        Long userId = jwtService.extractUserId(token);
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

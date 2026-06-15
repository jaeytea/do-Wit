package com.dowit.backend.service;

import com.dowit.backend.dto.LoginRequestDTO;
import com.dowit.backend.dto.SignupRequestDTO;
import com.dowit.backend.entity.Users;
import com.dowit.backend.entity.enums.UserRole;
import com.dowit.backend.repository.UserRepository;
import com.dowit.backend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    public Users signup(SignupRequestDTO request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        Users user=new Users();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(UserRole.USER);

        return userRepository.save(user);
    }
    public String login(LoginRequestDTO request){

        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user);
    }

}

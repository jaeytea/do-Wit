package com.dowit.backend.controller;

import com.dowit.backend.dto.LoginRequestDTO;
import com.dowit.backend.dto.SignupRequestDTO;
import com.dowit.backend.entity.Users;
import com.dowit.backend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/signup")
    public Users signup(
            @RequestBody SignupRequestDTO request){

        return userService.signup(request);
    }

    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequestDTO request){

        return userService.login(request);
    }
}

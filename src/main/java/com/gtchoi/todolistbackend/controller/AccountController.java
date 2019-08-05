package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.model.LoginDTO;
import com.gtchoi.todolistbackend.model.LoginResponse;
import com.gtchoi.todolistbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginDTO loginDTO) {
        return accountService.login(loginDTO.getUserId(), loginDTO.getPassword());
    }

    @GetMapping("/token")
    public LoginResponse token(User user) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setLogin(true);
        loginResponse.setMessage("success");
        loginResponse.setUserId(user.getUserId());
        return loginResponse;
    }
}

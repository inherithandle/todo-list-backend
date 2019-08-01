package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.model.LoginResponse;
import com.gtchoi.todolistbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    public LoginResponse login(@RequestParam String userId, @RequestParam String password) {
        return accountService.login(userId, password);
    }

    @PostMapping("/token")
    public LoginResponse token(@RequestParam String accessToken) throws IllegalArgumentException {
        return accountService.isValidAccessToken(accessToken);
    }
}

package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.model.*;
import com.gtchoi.todolistbackend.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AccountController {

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginDTO loginDTO) {
        return accountService.login(loginDTO.getUserId(), loginDTO.getPassword());
    }

    @PostMapping("/login-with-third-party")
    public LoginResponse loginWithThirdParty(@Valid @RequestBody LoginWithThirdPartyDTO loginDTO) {
        LoginResponse loginResponse = accountService.loginWithThirdParty(loginDTO.getTokenType(), loginDTO.getAuthorizationCode());
        return loginResponse;
    }

    @GetMapping("/token")
    public LoginResponse token(User user) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setLogin(true);
        loginResponse.setMessage("success");
        loginResponse.setUserId(user.getUserId());
        return loginResponse;
    }

    @PostMapping("/signup")
    public LoginResponse signup(@RequestBody @Valid SignUpDTO signUpDTO) {
        LoginResponse response = accountService.signup(signUpDTO);
        return response;
    }

    @GetMapping("/duplicate")
    public DuplicateIdResponse duplicateId(@RequestParam String userId) {
        return accountService.checkForDuplicate(userId);
    }
}

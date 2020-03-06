package com.gtchoi.todolistbackend.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("oauth")
public class NaverSigninService implements ThirdPartySigninService {
    @Override
    public String getEmail(String authorizationCode) {
        return "not-implemented-yet@naver.com";
    }
}

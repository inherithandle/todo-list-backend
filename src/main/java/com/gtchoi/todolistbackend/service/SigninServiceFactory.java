package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.enums.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class SigninServiceFactory {

    private Logger logger = LoggerFactory.getLogger(SigninServiceFactory.class);

    @Autowired(required = false)
    private GoogleSigninService googleSigninService;

    @Autowired(required = false)
    private NaverSigninService naverSigninService;

    public ThirdPartySigninService getSigninService(TokenType tokenType) {
        if (anyBeansIsNull()) {
            logger.error("SigninService null. check out oauth properties.");
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (tokenType == TokenType.GOOGLE) {
            return googleSigninService;
        } else if (tokenType == tokenType.NAVER) {
            return naverSigninService;
        } else {
            logger.error("SigninServiceFactory: can't find third party service. check out TokenType: {}", tokenType);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private boolean anyBeansIsNull() {
        return googleSigninService == null || naverSigninService == null;
    }


}

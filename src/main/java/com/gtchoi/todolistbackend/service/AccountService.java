package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.entity.UserToken;
import com.gtchoi.todolistbackend.model.LoginResponse;
import com.gtchoi.todolistbackend.repository.UserRepository;
import com.gtchoi.todolistbackend.repository.UserTokenRepository;
import com.gtchoi.todolistbackend.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    @Autowired
    PasswordEncoder pwEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTokenRepository userTokenRepository;

    @Transactional
    public LoginResponse login(String userId, String password) {
        User user = userRepository.findByUserId(userId);
        LoginResponse loginResponse = new LoginResponse();
        if (user == null || !pwEncoder.matches(password, user.getPassword())) {
            loginResponse.setLogin(false);
            loginResponse.setMessage("User credentials are not valid."); // TODO: i18n
        } else {
            String accessToken = RandomUtil.generateString();
            UserToken userToken = new UserToken(accessToken, user);
            userToken.setAccessToken(accessToken);
            userToken.setUser(user);

            userTokenRepository.save(userToken);

            loginResponse.setLogin(true);
            loginResponse.setMessage("logined. check out the access token");
            loginResponse.setAccessToken(accessToken);
        }
        return loginResponse;
    }

    public LoginResponse isValidAccessToken(String accessToken) {
        LoginResponse loginResponse = new LoginResponse();
        UserToken userToken = userTokenRepository.findById(accessToken).orElse(null);

        if (userToken == null) {
            loginResponse.setMessage("fail");
            return loginResponse;
        }

        loginResponse.setLogin(true);
        loginResponse.setMessage("success");
        loginResponse.setUserId(userToken.getUser().getUserId());
        loginResponse.setAccessToken(accessToken);
        return loginResponse;
    }

}

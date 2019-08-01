package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.model.LoginResponse;
import com.gtchoi.todolistbackend.repository.UserRepository;
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

    @Transactional(readOnly = true)
    public LoginResponse login(String userId, String password) {
        User user = userRepository.findByUserId(userId);
        LoginResponse loginResponse = new LoginResponse();
        if (user == null || !pwEncoder.matches(password, user.getPassword())) {
            loginResponse.setLogin(false);
            loginResponse.setMessage("User credentials are not valid."); // TODO: i18n
        } else {
            loginResponse.setLogin(true);
            loginResponse.setMessage("logined. check out the access token");
            loginResponse.setAccessToken(RandomUtil.generateString());
        }
        return loginResponse;
    }

    public LoginResponse isValidAccessToken(String accessToken) {
        // TODO: access token을 DB에서 검증. 유효하면, true, 아니면 false
        LoginResponse loginResponse = new LoginResponse();
        if (accessToken.length() > 0) {
            loginResponse.setLogin(true);
        }
        loginResponse.setMessage("success");
        loginResponse.setUserId("MOCK USER NAME"); // TODO
        loginResponse.setAccessToken(accessToken);
        return loginResponse;
    }

}

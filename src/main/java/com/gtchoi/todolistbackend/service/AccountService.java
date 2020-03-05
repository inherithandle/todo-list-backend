package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.entity.UserToken;
import com.gtchoi.todolistbackend.enums.TokenType;
import com.gtchoi.todolistbackend.exception.UnAuthorizedException;
import com.gtchoi.todolistbackend.model.DuplicateIdResponse;
import com.gtchoi.todolistbackend.model.LoginResponse;
import com.gtchoi.todolistbackend.model.SignUpDTO;
import com.gtchoi.todolistbackend.repository.UserRepository;
import com.gtchoi.todolistbackend.repository.UserTokenRepository;
import com.gtchoi.todolistbackend.util.RandomUtil;
import com.gtchoi.todolistbackend.validator.VariableNamingPolicy;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotBlank;

@Service
@Validated
public class AccountService {

    @Autowired
    private PasswordEncoder pwEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private GoogleSigninService googleSigninService;

    @PersistenceContext
    private EntityManager em;

    Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Transactional
    public LoginResponse login(@NotBlank
                               @VariableNamingPolicy String userId,
                               @NotBlank String password) {

        User user = userRepository.findByUserId(userId);
        LoginResponse loginResponse = new LoginResponse();
        if (user == null || !pwEncoder.matches(password, user.getPassword())) {
            loginResponse.setLogin(false);
            loginResponse.setMessage("User credentials are not valid."); // TODO: i18n
        } else {
            UserToken userToken = addUserToken(user);
            loginResponse.setLogin(true);
            loginResponse.setMessage("logined. check out the access token");
            loginResponse.setAccessToken(userToken.getAccessToken());
        }
        return loginResponse;
    }

    @Transactional
    UserToken addUserToken(User user) {
        String accessToken = RandomUtil.generateString();
        UserToken userToken = new UserToken(accessToken, user);
        userToken.setAccessToken(accessToken);
        userToken.setUser(user);

        return userTokenRepository.save(userToken);
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

    @Transactional
    public LoginResponse signup(SignUpDTO signUpDTO) {
        User user = modelMapper.map(signUpDTO, User.class);
        String encoded = passwordEncoder.encode(signUpDTO.getPassword());
        user.setPassword(encoded);
        user.setDefaultProject(em);
        User savedUser = userRepository.save(user);

        UserToken userToken = addUserToken(savedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setLogin(true);
        loginResponse.setUserId(savedUser.getUserId());
        loginResponse.setAccessToken(userToken.getAccessToken());
        return loginResponse;
    }

    @Transactional
    public LoginResponse loginWithThirdParty(TokenType tokenType, String authorizationCode) {
        String email = null;
        if (tokenType == TokenType.GOOGLE) {
            email = googleSigninService.getGoogleEmail(authorizationCode);
        } else if (tokenType == TokenType.NAVER) {
            email = "mock-id@naver.com";
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = signupWithThirdPartyAccount(email);
        }

        UserToken userToken = addUserToken(user);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setLogin(true);
        loginResponse.setUserId(user.getEmail());
        loginResponse.setMessage("logined. check out the access token");
        loginResponse.setAccessToken(userToken.getAccessToken());
        return loginResponse;
    }

    @Transactional
    public User signupWithThirdPartyAccount(String email) {
        User user = new User();
        user.setEmail(email);
        user.setUserId(email);
        user.setDefaultProject(em);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public DuplicateIdResponse checkForDuplicate(String userId) {
        DuplicateIdResponse duplicateIdResponse = new DuplicateIdResponse();
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            duplicateIdResponse.setDuplicate(false);
        } else {
            duplicateIdResponse.setDuplicate(true);
        }
        return duplicateIdResponse;
    }

    @Transactional
    public void deleteToken(User user, String accessToken) {
        UserToken userToken = userTokenRepository.findByAccessToken(accessToken).orElseThrow(UnAuthorizedException::new);
        if (userToken.getUser().getUserNo() == user.getUserNo()) {
            userTokenRepository.delete(userToken);
        } else {
            throw new UnAuthorizedException();
        }
    }
}

package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.entity.Project;
import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.model.LoginResponse;
import com.gtchoi.todolistbackend.model.SignUpDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class AccountServiceTestWithDB {

    Logger logger = LoggerFactory.getLogger(AccountServiceTestWithDB.class);

    @TestConfiguration
    static class ThisConfig {
        @Bean
        public AccountService accountService() {
            return new AccountService();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(11);
        }
    }

    @MockBean
    GoogleSigninService googleSigninService;

    @Autowired
    AccountService accountService;

    @Test
    public void userShouldHaveOneProject() {
        User user = accountService.signupWithThirdPartyAccount("inherithandle@gmail.com");

        assertThat(user.getUserNo(), greaterThan(0L));
        assertThat(user.getProjects(), is(notNullValue()));

        Project project = user.getProjects().get(0);
        assertThat(project.getProjectName(), is("inbox"));
        assertThat(project.getProjectNo(), greaterThan(0L));
    }

    @Test
    public void checkForDuplicate() {
        boolean joma = accountService.checkForDuplicate("joma").isDuplicate();
        boolean inherithandle = accountService.checkForDuplicate("inherithandle").isDuplicate();
        assertThat(joma, is(true));
        assertThat(inherithandle, is(false));
    }

    @Test
    public void signup() {
        final String userId = "gtchoi";
        SignUpDTO signupDTO = new SignUpDTO();
        signupDTO.setUserId(userId);
        signupDTO.setConfirmPassword("password1234");
        signupDTO.setPassword("password1234");

        LoginResponse response = accountService.signup(signupDTO);
        logger.debug("access token: {}", response.getAccessToken());
        assertThat(response.isLogin(), is(true));
        assertThat(response.getAccessToken().length(), greaterThan(5));
        assertThat(response.getUserId(), is(userId));
    }
}
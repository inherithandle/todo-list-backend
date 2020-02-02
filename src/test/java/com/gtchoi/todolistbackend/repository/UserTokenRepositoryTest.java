package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.entity.UserToken;
import com.gtchoi.todolistbackend.util.RandomUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTokenRepositoryTest {

    @Autowired
    UserTokenRepository userTokenRepository;

    @Autowired
    UserRepository userRepository;

    final String ACCESS_TOKEN = RandomUtil.generateString();
    final String USER_ID = "hello-user";
    final String PASSWORD = "hello";

    @Before
    public void setupDB() {
        User user = new User();
        user.setPassword(PASSWORD);
        user.setUserId(USER_ID);

        userRepository.save(user);

        UserToken userToken = new UserToken();

        userToken.setAccessToken(ACCESS_TOKEN);
        userToken.setUser(user);
        userTokenRepository.save(userToken);

        assert user.getUserNo() > 0;
    }

    @Test
    public void findByToken() {
        UserToken userToken = userTokenRepository.findById(ACCESS_TOKEN).get();

        assertThat(userToken.getUser().getUserId(), is(USER_ID));
        assertThat(userToken.getUser().getPassword(), is(PASSWORD));
        assertThat(userToken.getAccessToken(), is(ACCESS_TOKEN));
    }

    @Test
    public void fidnByTokenJoinFecth() {
        Optional<UserToken> userToken = userTokenRepository.findByAccessToken(ACCESS_TOKEN);
        assertThat(userToken.get().getUser().getUserId(), is(USER_ID));
    }
}
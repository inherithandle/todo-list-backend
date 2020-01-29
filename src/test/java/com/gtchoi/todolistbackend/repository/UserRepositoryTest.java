package com.gtchoi.todolistbackend.repository;

import com.gtchoi.todolistbackend.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void checkIfNullColumnisPossible() {
        User user = new User();
        user.setEmail("inherithandle@gmail.com");

        User save = userRepository.save(user);

        assertThat(save.getUserNo(), greaterThan(0L));
        assertThat(save.getUserId(), is(nullValue()));
        assertThat(save.getPassword(), is(nullValue()));
    }

}
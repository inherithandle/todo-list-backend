package com.gtchoi.todolistbackend.util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RandomUtilTest {

    @Test
    public void generateString() {
        String s = RandomUtil.generateString();
        assertThat(s.length(), is(36));
    }

}
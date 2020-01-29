package com.gtchoi.todolistbackend.util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RegExpTest {

    @Test
    public void variableTest() {
        assertThat("rrjjang2".matches("[a-zA-Z][a-zA-Z0-9_]*"), is(true));
        assertThat("gtchoi".matches("[a-zA-Z][a-zA-Z0-9_]*"), is(true));
        assertThat("a1b".matches("[a-zA-Z][a-zA-Z0-9_]*"), is(true));
        assertThat("1ab".matches("[a-zA-Z][a-zA-Z0-9_]*"), is(false));
        assertThat("inherithandle@gmail.com".matches("[a-zA-Z][a-zA-Z0-9_]*"), is(false));
    }

}
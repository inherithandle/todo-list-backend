package com.gtchoi.todolistbackend.util;

import java.util.UUID;

public class RandomUtil {

    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}

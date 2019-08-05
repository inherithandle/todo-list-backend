package com.gtchoi.todolistbackend.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class JsonTest {

    @Test
    public void jsonString() throws JSONException {
        JSONObject builder = new JSONObject();
        String jsonString = builder.put("accessToken", "abc").toString();
        System.out.println(jsonString);

    }
}

package com.gtchoi.todolistbackend.controller;

import com.gtchoi.todolistbackend.model.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@RestController
public class GreetingController {
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name, HttpServletResponse response) {
        Cookie cookie = new Cookie("access-token", "abc");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new Greeting(1L, String.format("Hello %s", name));
    }
}

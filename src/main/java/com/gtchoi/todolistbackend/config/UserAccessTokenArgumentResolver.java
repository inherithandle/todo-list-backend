package com.gtchoi.todolistbackend.config;

import com.gtchoi.todolistbackend.entity.User;
import com.gtchoi.todolistbackend.entity.UserToken;
import com.gtchoi.todolistbackend.exception.UnAuthorizedException;
import com.gtchoi.todolistbackend.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class UserAccessTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    UserTokenRepository userTokenRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie accessToken = getAccessTokenCookie(request.getCookies());

        if (accessToken == null) {
            throw new UnAuthorizedException();
        }

        Optional<UserToken> userToken = userTokenRepository.findById(accessToken.getValue());
        userToken.orElseThrow(() -> new UnAuthorizedException());

        return userToken.get().getUser();
    }

    private Cookie getAccessTokenCookie(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("access-token".equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }
}

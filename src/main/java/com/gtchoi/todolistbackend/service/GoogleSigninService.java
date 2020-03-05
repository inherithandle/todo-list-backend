package com.gtchoi.todolistbackend.service;

import com.gtchoi.todolistbackend.condition.GoogleSigninCondition;
import com.gtchoi.todolistbackend.model.GoogleTokenResponse;
import com.gtchoi.todolistbackend.model.GoogleUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Conditional(GoogleSigninCondition.class)
public class GoogleSigninService {

    private static final String AUTH_CODE_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_ENDPOINT = "https://openidconnect.googleapis.com/v1/userinfo";
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    @Value("${google.oauth.client-id}")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Value("${google.oauth.client-secret}")
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Value("${google.oauth.redirect-uri}")
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    Logger logger = LoggerFactory.getLogger(GoogleSigninService.class);

    @Autowired
    RestTemplate restTemplate;

    public String getGoogleEmail(String authorizationCode) {
        GoogleTokenResponse googleTokenResponse = callAuthCodeEndpoint(authorizationCode);
        GoogleUserResponse googleUserResponse = callUserInfoEndpoint(googleTokenResponse.getAccessToken());
        return googleUserResponse.getEmail();
    }


    private MultiValueMap<String, String> getParametersForGoogleAuthCodeAPI(String authorizationCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        return params;
    }

    /*
        POST /token HTTP/1.1
        Host: oauth2.googleapis.com
        Content-Type: application/x-www-form-urlencoded
        code=4/P7q7W91a-oMsCeLvIaQm6bTrgtp7&
        client_id=your-client-id&
        client_secret=your-client-secret&
        redirect_uri=https%3A//oauth2.example.com/callback&
        grant_type=authorization_code
    */
    public GoogleTokenResponse callAuthCodeEndpoint(String authorizationCode) {
        MultiValueMap<String, String> params = getParametersForGoogleAuthCodeAPI(authorizationCode);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity(params, headers);

        ResponseEntity<GoogleTokenResponse> res = restTemplate.postForEntity(AUTH_CODE_ENDPOINT, httpEntity, GoogleTokenResponse.class);
        return res.getBody();
    }

    public GoogleUserResponse callUserInfoEndpoint(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity(headers);

        ResponseEntity<GoogleUserResponse> response = restTemplate.exchange(USER_INFO_ENDPOINT, HttpMethod.GET, httpEntity, GoogleUserResponse.class);
        return response.getBody();
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}

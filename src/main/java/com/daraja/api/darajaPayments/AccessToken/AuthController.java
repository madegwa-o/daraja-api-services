package com.daraja.api.darajaPayments.AccessToken;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class AuthController {

    private final AccessTokenGenerator accessTokenGenerator;

    @GetMapping("/access-token")
    public String aceessToken() throws UnsupportedEncodingException {
        return accessTokenGenerator.getAccessToken();
    }

}

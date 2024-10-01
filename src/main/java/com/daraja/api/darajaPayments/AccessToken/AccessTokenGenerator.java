package com.daraja.api.darajaPayments.AccessToken;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.daraja.api.darajaPayments.config.MpesaConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccessTokenGenerator {


    private final MpesaConfiguration mpesaConfiguration;

    public String getAccessToken() throws UnsupportedEncodingException {
        OkHttpClient okHttpClient = new OkHttpClient();

        String keyAndSecret = mpesaConfiguration.getConsumerKey() + ":" + mpesaConfiguration.getConsumerSecret();
        byte[] keyAndSecretBytes = keyAndSecret.getBytes(StandardCharsets.ISO_8859_1);
        String encodedKeyAndSecret = Base64.getEncoder().encodeToString(keyAndSecretBytes);

        Request authRequest = new Request.Builder()
                .url(mpesaConfiguration.getAccessTokenUrl())
                .get()
                .addHeader("Authorization", "Basic " + encodedKeyAndSecret)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        try(Response authResponse = okHttpClient.newCall(authRequest).execute()) {
            if (authResponse.isSuccessful()) {
                String responseBody = authResponse.body().string();
                System.out.println(responseBody);

                JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
                return jsonResponse.get("access_token").getAsString();

            }else {
                throw new IOException("Unexpected code " + authResponse);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }


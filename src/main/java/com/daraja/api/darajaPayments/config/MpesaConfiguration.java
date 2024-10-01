package com.daraja.api.darajaPayments.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mpesa.daraja")
public class MpesaConfiguration {
    private String consumerKey;
    private String consumerSecret;
    private String businessShortCode;
    private String accessTokenUrl;
    private String paymentUrl;
    private String passKey;
    private String callbackUrl;
}

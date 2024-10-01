package com.daraja.api.darajaPayments.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestToMpesa {
    private String businessShortCode;
    private String password;
    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuuMMddHHmmss"));
    private String transactionType = "CustomerPayBillOnline";

    private String amount;
    private String partyA;
    private String partyB;

    private String phoneNumber;
    private String callBackURL;
    private String accountReference;
    private String transactionDesc;
}

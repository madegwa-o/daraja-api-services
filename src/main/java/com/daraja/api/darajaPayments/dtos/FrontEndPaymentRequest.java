package com.daraja.api.darajaPayments.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrontEndPaymentRequest {

    private String phoneNumber;
    private int amount;
}

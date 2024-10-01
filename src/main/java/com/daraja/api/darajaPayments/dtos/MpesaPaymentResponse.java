package com.daraja.api.darajaPayments.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MpesaPaymentResponse {

    private String merchantRequestID;

    private String checkoutRequestID;

    private String responseCode;

    private String responseDescription;

    private String customerMessage;
}

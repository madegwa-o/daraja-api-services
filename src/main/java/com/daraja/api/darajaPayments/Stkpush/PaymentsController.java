package com.daraja.api.darajaPayments.Stkpush;

import com.daraja.api.darajaPayments.config.MpesaConfiguration;
import com.daraja.api.darajaPayments.dtos.FrontEndPaymentRequest;
import com.daraja.api.darajaPayments.dtos.MpesaPaymentResponse;
import com.daraja.api.darajaPayments.dtos.PaymentRequestToMpesa;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/online-payment")
public class PaymentsController {

    private final MpesaConfiguration mpesaConfiguration;
    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<?> makePayment(@RequestBody FrontEndPaymentRequest frontEndPaymentRequest) throws IOException {
        System.out.println("Payment request: " + frontEndPaymentRequest);

        // Extract the phone number from the request
        String phoneNumber = frontEndPaymentRequest.getPhoneNumber();

        // Validate the phone number
        if (!isValidPhoneNumber(phoneNumber)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid phone number format");
        }

        // Convert phone number if it starts with '07'
        phoneNumber = formatPhoneNumber(phoneNumber);

        // Proceed with payment if the phone number is valid
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = time.format(formatter);

        // Create the password
        String password = Base64.getEncoder().encodeToString((mpesaConfiguration.getBusinessShortCode()
                + mpesaConfiguration.getPassKey() + timestamp).getBytes());

        // Now construct the body for the payment request
        PaymentRequestToMpesa requestToMpesa = new PaymentRequestToMpesa(
                mpesaConfiguration.getBusinessShortCode(),
                password,
                timestamp,
                "CustomerPayBillOnline",
                String.valueOf(frontEndPaymentRequest.getAmount()),
                phoneNumber,
                mpesaConfiguration.getBusinessShortCode(),
                phoneNumber,
                mpesaConfiguration.getCallbackUrl(),
                "pay rent",
                "deposit rent"
        );

        // Execute the payment
        MpesaPaymentResponse response = paymentService.executePayment(requestToMpesa);

        return ResponseEntity.ok(response);
    }

    // Method to validate phone number format
    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber.startsWith("0") && phoneNumber.length() == 10) {
            return true; // Valid Kenyan local number format
        } else if (phoneNumber.startsWith("254") && phoneNumber.length() == 12) {
            return true; // Valid Kenyan international number format
        }
        return false;
    }

    // Method to format phone number (convert '07' to '2547')
    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber.startsWith("0")) {
            return phoneNumber.replaceFirst("0", "254");
        }
        return phoneNumber;
    }
}

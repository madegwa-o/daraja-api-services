package com.daraja.api.darajaPayments.Stkpush;

import com.daraja.api.darajaPayments.dtos.Body;
import com.daraja.api.darajaPayments.dtos.CallbackMetadata;
import com.daraja.api.darajaPayments.dtos.Item;
import com.daraja.api.darajaPayments.dtos.ResultBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackController.class);

    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestBody ResultBody resultBody) {
        System.out.println("you called me back");
        // Log the full result body
        LOGGER.info("Received callback result body: {}", resultBody);

        // Extract and log individual fields for clarity
        ResultBody newResultBody = new ResultBody();

        newResultBody.setBody(resultBody.getBody());

        Body subBody = newResultBody.getBody();

        if (subBody.getStkCallback() != null) {
            Body newSubBody = new Body();
            newSubBody.setStkCallback(subBody.getStkCallback());
            LOGGER.info("MerchantRequestID: {}", newSubBody.getStkCallback().getMerchantRequestID());
            LOGGER.info("CheckoutRequestID: {}", newSubBody.getStkCallback().getCheckoutRequestID());
            LOGGER.info("ResultCode: {}", newSubBody.getStkCallback().getResultCode());
            LOGGER.info("ResultDesc: {}", newSubBody.getStkCallback().getResultDesc());

            CallbackMetadata callbackMetadata = newSubBody.getStkCallback().getCallbackMetadata();
            // Log CallbackMetadata details
            if (callbackMetadata != null) {
                for (Item item : callbackMetadata.getItem()) {
                    LOGGER.info("Metadata Item - Name: {}, Value: {}", item.getName(), item.getValue());
                }
            } else {
                LOGGER.warn("No CallbackMetadata available in the callback response.");
            }
        } else {
            LOGGER.warn("No stkCallback available in the callback response.");
        }

        // Acknowledge receipt of the callback
        return ResponseEntity.ok("Callback received");
    }
}

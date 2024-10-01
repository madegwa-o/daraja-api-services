package com.daraja.api.darajaPayments.Stkpush;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.daraja.api.darajaPayments.AccessToken.AccessTokenGenerator;
import com.daraja.api.darajaPayments.config.MpesaConfiguration;
import com.daraja.api.darajaPayments.dtos.MpesaPaymentResponse;
import com.daraja.api.darajaPayments.dtos.PaymentRequestToMpesa;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MpesaConfiguration mpesaConfiguration;
    private final AccessTokenGenerator accessTokenGenerator;

    public MpesaPaymentResponse executePayment(PaymentRequestToMpesa pr) throws IOException {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("BusinessShortCode", pr.getBusinessShortCode());
        jsonObject.addProperty("Password", pr.getPassword());
        jsonObject.addProperty("Timestamp", pr.getTimestamp());
        jsonObject.addProperty("TransactionType", pr.getTransactionType());
        jsonObject.addProperty("Amount", pr.getAmount());
        jsonObject.addProperty("PhoneNumber", pr.getPhoneNumber());
        jsonObject.addProperty("PartyA", pr.getPartyA());
        jsonObject.addProperty("PartyB", pr.getPartyB());
        jsonObject.addProperty("CallBackURL", pr.getCallBackURL());
        jsonObject.addProperty("AccountReference", pr.getAccountReference());
        jsonObject.addProperty("TransactionDesc", pr.getTransactionDesc());


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getPaymentUrl())
                .post(requestBody)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + accessTokenGenerator.getAccessToken())
                .addHeader("cache-control", "no-cache")
                .build();

        OkHttpClient client = new OkHttpClient();
        MpesaPaymentResponse paymentResponse = new MpesaPaymentResponse();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Parse the response body
                String responseBody = response.body().string();
                System.out.println(responseBody);
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

                // Extract the necessary fields from the JSON response
//                JsonObject body = jsonResponse.getAsJsonObject("Body");
//                JsonObject stkCallback = body.getAsJsonObject("stkCallback");

                // Set the fields into PaymentResponse
                paymentResponse.setMerchantRequestID(jsonResponse.get("MerchantRequestID").getAsString());
                paymentResponse.setCheckoutRequestID(jsonResponse.get("CheckoutRequestID").getAsString());
                paymentResponse.setResponseCode(jsonResponse.get("ResponseCode").getAsString());
                paymentResponse.setResponseDescription(jsonResponse.get("ResponseDescription").getAsString());
                paymentResponse.setCustomerMessage(jsonResponse.get("CustomerMessage").getAsString());

                // Handle CallbackMetadata items if necessary
                // Optionally handle customerMessage, etc.
            } else {
                // Handle the case where the response is not successful
                System.out.println("Request failed with status: " + response.code());
                paymentResponse.setResponseCode(String.valueOf(response.code()));
                paymentResponse.setResponseDescription("Payment failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
            paymentResponse.setResponseDescription("Error occurred during payment processing");
        }

        return paymentResponse;
    }
}

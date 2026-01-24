package com.carro.carrorental.api.response;

import com.google.gson.annotations.SerializedName;

// This class EXACTLY matches the JSON response from your server.
public class CreateOrderResponse {

    // Matches: "response": "success"
    @SerializedName("response")
    private String responseStatus;

    // Matches: "id": "order_Qu7HB7yEbmiT7K"
    @SerializedName("id")
    private String orderId;

    // Matches: "amount": 100000
    @SerializedName("amount")
    private int amount;

    // --- Getters ---
    public String getResponseStatus() {
        return responseStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getAmount() {
        return amount;
    }
}

package com.carro.carrorental.api.response;

import com.carro.carrorental.model.SubscriptionPayment;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscriptionPaymentResponse {

    @SerializedName("response")
    private String response;

    @SerializedName("data")
    private List<SubscriptionPayment> data;

    public List<SubscriptionPayment> getData() {
        return data;
    }
}
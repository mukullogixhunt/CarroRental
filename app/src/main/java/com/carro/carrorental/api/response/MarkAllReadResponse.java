package com.carro.carrorental.api.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarkAllReadResponse {

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private int data;

    public String getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public int getData() {
        return data;
    }
}

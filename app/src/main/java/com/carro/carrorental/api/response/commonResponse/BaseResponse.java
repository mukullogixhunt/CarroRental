package com.carro.carrorental.api.response.commonResponse;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * base pojo class for all incoming responses
 * contains only message and code
 * change key if needed.
 */

public class BaseResponse {

    @SerializedName("response")
    @Expose
    private String result;
    @SerializedName("message")
    @Expose
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


//    @Override
//    public String toString() {
//        return "BaseResponse{" +
//                "result='" + result + '\'' +
//                ", message='" + message + '\'' +
//                '}';
//    }

}

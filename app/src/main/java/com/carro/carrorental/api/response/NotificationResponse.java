package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.NotificationModel;


import java.util.List;

public class NotificationResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<NotificationModel> data;

    public List<NotificationModel> getData() {
        return data;
    }

    public void setData(List<NotificationModel> data) {
        this.data = data;
    }
}

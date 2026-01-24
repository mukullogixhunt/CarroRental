package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.BookingModel;

import java.util.List;

public class BookingResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<BookingModel> data;

    public List<BookingModel> getData() {
        return data;
    }

    public void setData(List<BookingModel> data) {
        this.data = data;
    }
}

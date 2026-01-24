package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.BookingDetailModel;

import java.util.List;

public class BookingDetailResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<BookingDetailModel> data;

    public List<BookingDetailModel> getData() {
        return data;
    }

    public void setData(List<BookingDetailModel> data) {
        this.data = data;
    }
}

package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.OfferModel;

import java.util.List;

public class OfferResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<OfferModel> data;

    public List<OfferModel> getData() {
        return data;
    }

    public void setData(List<OfferModel> data) {
        this.data = data;
    }
}

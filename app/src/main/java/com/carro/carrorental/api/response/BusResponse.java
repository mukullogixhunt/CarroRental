package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.BusModel;

import java.util.List;

public class BusResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<BusModel> data;

    public List<BusModel> getData() {
        return data;
    }

    public void setData(List<BusModel> data) {
        this.data = data;
    }
}

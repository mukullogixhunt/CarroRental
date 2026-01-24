package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.CarModel;

import java.util.List;

public class CarResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<CarModel> data;

    public List<CarModel> getData() {
        return data;
    }

    public void setData(List<CarModel> data) {
        this.data = data;
    }
}

package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.CarTypeModel;

public class SelfDetailsResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private CarTypeModel data;

    public CarTypeModel getData() {
        return data;
    }

    public void setData(CarTypeModel data) {
        this.data = data;
    }
}

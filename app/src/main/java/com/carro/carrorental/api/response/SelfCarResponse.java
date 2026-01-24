package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.SelfCarModel;

import java.util.List;

public class SelfCarResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<SelfCarModel> data;

    public List<SelfCarModel> getData() {
        return data;
    }

    public void setData(List<SelfCarModel> data) {
        this.data = data;
    }
}

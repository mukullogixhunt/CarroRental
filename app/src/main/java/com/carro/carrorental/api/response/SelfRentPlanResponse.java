package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.SelfRentPlanModel;

import java.util.List;

public class SelfRentPlanResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<SelfRentPlanModel> data;

    public List<SelfRentPlanModel> getData() {
        return data;
    }

    public void setData(List<SelfRentPlanModel> data) {
        this.data = data;
    }
}

package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.SelfSubPlanModel;

import java.util.List;

public class SelfSubPlanResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<SelfSubPlanModel> data;

    public List<SelfSubPlanModel> getData() {
        return data;
    }

    public void setData(List<SelfSubPlanModel> data) {
        this.data = data;
    }
}

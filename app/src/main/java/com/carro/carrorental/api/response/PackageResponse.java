package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.PackageModel;

import java.util.List;

public class PackageResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<PackageModel> data;

    public List<PackageModel> getData() {
        return data;
    }

    public void setData(List<PackageModel> data) {
        this.data = data;
    }
}

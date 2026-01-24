package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.HomePageModel;

import java.util.List;

public class HomePageResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<HomePageModel> data;

    public List<HomePageModel> getData() {
        return data;
    }

    public void setData(List<HomePageModel> data) {
        this.data = data;
    }
}

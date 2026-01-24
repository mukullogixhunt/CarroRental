package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.SliderModel;

import java.util.List;

public class SliderResponse  extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<SliderModel> data;

    public List<SliderModel> getData() {
        return data;
    }

    public void setData(List<SliderModel> data) {
        this.data = data;
    }
}

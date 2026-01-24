package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.model.PlaceDetailsModel;

public class PlaceDetailsResponse {
    @SerializedName("result")
    @Expose
    private PlaceDetailsModel result;

    public PlaceDetailsModel getResult() {
        return result;
    }

    public void setResult(PlaceDetailsModel result) {
        this.result = result;
    }
}

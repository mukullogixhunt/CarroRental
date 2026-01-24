package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.CouponModel;

public class CouponResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private CouponModel data;

    public CouponModel getData() {
        return data;
    }

    public void setData(CouponModel data) {
        this.data = data;
    }
}

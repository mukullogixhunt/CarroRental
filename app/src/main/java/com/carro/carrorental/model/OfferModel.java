package com.carro.carrorental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferModel {

    @SerializedName("m_offer_id")
    @Expose
    private String mOfferId;
    @SerializedName("m_offer_img")
    @Expose
    private String mOfferImg;

    public String getmOfferId() {
        return mOfferId;
    }

    public void setmOfferId(String mOfferId) {
        this.mOfferId = mOfferId;
    }

    public String getmOfferImg() {
        return mOfferImg;
    }

    public void setmOfferImg(String mOfferImg) {
        this.mOfferImg = mOfferImg;
    }
}

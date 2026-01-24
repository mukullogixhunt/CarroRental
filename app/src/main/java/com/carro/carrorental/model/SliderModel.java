package com.carro.carrorental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderModel {
    @SerializedName("m_slider_id")
    @Expose
    private String mSliderId;
    @SerializedName("m_slider_img")
    @Expose
    private String mSliderImg;
    @SerializedName("m_slider_status")
    @Expose
    private String mSliderStatus;
    @SerializedName("m_slider_addedon")
    @Expose
    private String mSliderAddedon;

    public String getmSliderId() {
        return mSliderId;
    }

    public void setmSliderId(String mSliderId) {
        this.mSliderId = mSliderId;
    }

    public String getmSliderImg() {
        return mSliderImg;
    }

    public void setmSliderImg(String mSliderImg) {
        this.mSliderImg = mSliderImg;
    }

    public String getmSliderStatus() {
        return mSliderStatus;
    }

    public void setmSliderStatus(String mSliderStatus) {
        this.mSliderStatus = mSliderStatus;
    }

    public String getmSliderAddedon() {
        return mSliderAddedon;
    }

    public void setmSliderAddedon(String mSliderAddedon) {
        this.mSliderAddedon = mSliderAddedon;
    }
}

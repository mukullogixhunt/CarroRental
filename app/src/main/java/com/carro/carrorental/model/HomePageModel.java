package com.carro.carrorental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomePageModel {
    @SerializedName("m_hc_id")
    @Expose
    private String mHcId;
    @SerializedName("m_hc_tagline")
    @Expose
    private String mHcTagline;
    @SerializedName("m_hc_icon")
    @Expose
    private String mHcIcon;
    @SerializedName("m_hc_cab_img")
    @Expose
    private String mHcCabImg;
    @SerializedName("m_hc_self_drive_img")
    @Expose
    private String mHcSelfDriveImg;
    @SerializedName("m_hc_bus_service_img")
    @Expose
    private String mHcBusServiceImg;
    @SerializedName("m_hc_luxury_car_img")
    @Expose
    private String mHcLuxuryCarImg;
    @SerializedName("m_hc_referral_img")
    @Expose
    private String mHcReferralImg;
    @SerializedName("m_hc_updatedon")
    @Expose
    private String mHcUpdatedon;
    @SerializedName("m_hc_adv_img")
    @Expose
    private String mHcAdvImg;
    @SerializedName("m_hc_adv_img_sh")
    @Expose
    private String mHcAdvImgSh;

    @SerializedName("m_hc_referral_img_sh")
    @Expose
    private String mHcReferralImgSh;

    public String getmHcId() {
        return mHcId;
    }

    public void setmHcId(String mHcId) {
        this.mHcId = mHcId;
    }

    public String getmHcTagline() {
        return mHcTagline;
    }

    public void setmHcTagline(String mHcTagline) {
        this.mHcTagline = mHcTagline;
    }

    public String getmHcIcon() {
        return mHcIcon;
    }

    public void setmHcIcon(String mHcIcon) {
        this.mHcIcon = mHcIcon;
    }

    public String getmHcCabImg() {
        return mHcCabImg;
    }

    public void setmHcCabImg(String mHcCabImg) {
        this.mHcCabImg = mHcCabImg;
    }

    public String getmHcSelfDriveImg() {
        return mHcSelfDriveImg;
    }

    public void setmHcSelfDriveImg(String mHcSelfDriveImg) {
        this.mHcSelfDriveImg = mHcSelfDriveImg;
    }

    public String getmHcBusServiceImg() {
        return mHcBusServiceImg;
    }

    public void setmHcBusServiceImg(String mHcBusServiceImg) {
        this.mHcBusServiceImg = mHcBusServiceImg;
    }

    public String getmHcLuxuryCarImg() {
        return mHcLuxuryCarImg;
    }

    public void setmHcLuxuryCarImg(String mHcLuxuryCarImg) {
        this.mHcLuxuryCarImg = mHcLuxuryCarImg;
    }

    public String getmHcReferralImg() {
        return mHcReferralImg;
    }

    public void setmHcReferralImg(String mHcReferralImg) {
        this.mHcReferralImg = mHcReferralImg;
    }

    public String getmHcUpdatedon() {
        return mHcUpdatedon;
    }

    public void setmHcUpdatedon(String mHcUpdatedon) {
        this.mHcUpdatedon = mHcUpdatedon;
    }


    public String getmHcAdvImg() {
        return mHcAdvImg;
    }

    public String getmHcAdvImgSh() {
        return mHcAdvImgSh;
    }

    public String getmHcReferralImgSh() {
        return mHcReferralImgSh;
    }
}

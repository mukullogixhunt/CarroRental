package com.carro.carrorental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponModel {
    @SerializedName("m_coupon_id")
    @Expose
    private String mCouponId;
    @SerializedName("m_coupon_title")
    @Expose
    private String mCouponTitle;
    @SerializedName("m_coupon_code")
    @Expose
    private String mCouponCode;
    @SerializedName("m_coupon_type")
    @Expose
    private String mCouponType;
    @SerializedName("m_coupon_peramt")
    @Expose
    private String mCouponPeramt;
    @SerializedName("m_coupon_used")
    @Expose
    private String mCouponUsed;
    @SerializedName("m_coupon_status")
    @Expose
    private String mCouponStatus;
    @SerializedName("m_coupon_addedon")
    @Expose
    private String mCouponAddedon;

    public String getmCouponId() {
        return mCouponId;
    }

    public void setmCouponId(String mCouponId) {
        this.mCouponId = mCouponId;
    }

    public String getmCouponTitle() {
        return mCouponTitle;
    }

    public void setmCouponTitle(String mCouponTitle) {
        this.mCouponTitle = mCouponTitle;
    }

    public String getmCouponCode() {
        return mCouponCode;
    }

    public void setmCouponCode(String mCouponCode) {
        this.mCouponCode = mCouponCode;
    }

    public String getmCouponType() {
        return mCouponType;
    }

    public void setmCouponType(String mCouponType) {
        this.mCouponType = mCouponType;
    }

    public String getmCouponPeramt() {
        return mCouponPeramt;
    }

    public void setmCouponPeramt(String mCouponPeramt) {
        this.mCouponPeramt = mCouponPeramt;
    }

    public String getmCouponUsed() {
        return mCouponUsed;
    }

    public void setmCouponUsed(String mCouponUsed) {
        this.mCouponUsed = mCouponUsed;
    }

    public String getmCouponStatus() {
        return mCouponStatus;
    }

    public void setmCouponStatus(String mCouponStatus) {
        this.mCouponStatus = mCouponStatus;
    }

    public String getmCouponAddedon() {
        return mCouponAddedon;
    }

    public void setmCouponAddedon(String mCouponAddedon) {
        this.mCouponAddedon = mCouponAddedon;
    }
}

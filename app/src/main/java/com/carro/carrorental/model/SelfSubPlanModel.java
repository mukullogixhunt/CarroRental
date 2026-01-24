package com.carro.carrorental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfSubPlanModel {
    @SerializedName("m_subs_id")
    @Expose
    private String mSubsId;
    @SerializedName("m_subs_ctype")
    @Expose
    private String mSubsCtype;
    @SerializedName("m_subs_day")
    @Expose
    private String mSubsDay;
    @SerializedName("m_subs_price")
    @Expose
    private String mSubsPrice;
    @SerializedName("m_subs_order")
    @Expose
    private String mSubsOrder;
    @SerializedName("m_subs_status")
    @Expose
    private String mSubsStatus;
    @SerializedName("m_subs_added_by")
    @Expose
    private String mSubsAddedBy;
    @SerializedName("m_subs_addedon")
    @Expose
    private String mSubsAddedon;
    @SerializedName("m_subs_updatedon")
    @Expose
    private String mSubsUpdatedon;

    public String getmSubsId() {
        return mSubsId;
    }

    public void setmSubsId(String mSubsId) {
        this.mSubsId = mSubsId;
    }

    public String getmSubsCtype() {
        return mSubsCtype;
    }

    public void setmSubsCtype(String mSubsCtype) {
        this.mSubsCtype = mSubsCtype;
    }

    public String getmSubsDay() {
        return mSubsDay;
    }

    public void setmSubsDay(String mSubsDay) {
        this.mSubsDay = mSubsDay;
    }

    public String getmSubsPrice() {
        return mSubsPrice;
    }

    public void setmSubsPrice(String mSubsPrice) {
        this.mSubsPrice = mSubsPrice;
    }

    public String getmSubsOrder() {
        return mSubsOrder;
    }

    public void setmSubsOrder(String mSubsOrder) {
        this.mSubsOrder = mSubsOrder;
    }

    public String getmSubsStatus() {
        return mSubsStatus;
    }

    public void setmSubsStatus(String mSubsStatus) {
        this.mSubsStatus = mSubsStatus;
    }

    public String getmSubsAddedBy() {
        return mSubsAddedBy;
    }

    public void setmSubsAddedBy(String mSubsAddedBy) {
        this.mSubsAddedBy = mSubsAddedBy;
    }

    public String getmSubsAddedon() {
        return mSubsAddedon;
    }

    public void setmSubsAddedon(String mSubsAddedon) {
        this.mSubsAddedon = mSubsAddedon;
    }

    public String getmSubsUpdatedon() {
        return mSubsUpdatedon;
    }

    public void setmSubsUpdatedon(String mSubsUpdatedon) {
        this.mSubsUpdatedon = mSubsUpdatedon;
    }
}

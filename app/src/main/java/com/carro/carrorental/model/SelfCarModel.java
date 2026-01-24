package com.carro.carrorental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfCarModel {
    @SerializedName("m_ctype_id")
    @Expose
    private String mCtypeId;
    @SerializedName("m_ctype_title")
    @Expose
    private String mCtypeTitle;
    @SerializedName("m_ctype_img")
    @Expose
    private String mCtypeImg;
    @SerializedName("p_price")
    @Expose
    private String pPrice;
    @SerializedName("subs_day")
    @Expose
    private String subsDay;
    @SerializedName("subs_price")
    @Expose
    private String subsPrice;

    @SerializedName("is_book")
    @Expose
    private String isBook;

    @SerializedName("avail_date")
    @Expose
    private String availDate;

    public String getmCtypeId() {
        return mCtypeId;
    }

    public void setmCtypeId(String mCtypeId) {
        this.mCtypeId = mCtypeId;
    }

    public String getmCtypeTitle() {
        return mCtypeTitle;
    }

    public void setmCtypeTitle(String mCtypeTitle) {
        this.mCtypeTitle = mCtypeTitle;
    }

    public String getmCtypeImg() {
        return mCtypeImg;
    }

    public void setmCtypeImg(String mCtypeImg) {
        this.mCtypeImg = mCtypeImg;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getSubsDay() {
        return subsDay;
    }

    public void setSubsDay(String subsDay) {
        this.subsDay = subsDay;
    }

    public String getSubsPrice() {
        return subsPrice;
    }

    public void setSubsPrice(String subsPrice) {
        this.subsPrice = subsPrice;
    }

    public String getIsBook() {
        return isBook;
    }

    public void setIsBook(String isBook) {
        this.isBook = isBook;
    }

    public String getAvailDate() {
        return availDate;
    }

    public void setAvailDate(String availDate) {
        this.availDate = availDate;
    }
}

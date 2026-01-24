package com.carro.carrorental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {
    @SerializedName("m_notif_id")
    @Expose
    private String mNotifId;
    @SerializedName("m_notif_title")
    @Expose
    private String mNotifTitle;
    @SerializedName("m_notif_image")
    @Expose
    private String mNotifImage;
    @SerializedName("m_notif_message")
    @Expose
    private String mNotifMessage;
    @SerializedName("m_notif_user")
    @Expose
    private String mNotifUser;
    @SerializedName("m_notif_date")
    @Expose
    private String mNotifDate;
    @SerializedName("m_notif_time")
    @Expose
    private String mNotifTime;

    @SerializedName("m_notif_status")
    @Expose
    private String mNotifStatus;

    public String getmNotifStatus() {
        return mNotifStatus;
    }

    public void setmNotifStatus(String mNotifStatus) {
        this.mNotifStatus = mNotifStatus;
    }

    public String getmNotifId() {
        return mNotifId;
    }

    public void setmNotifId(String mNotifId) {
        this.mNotifId = mNotifId;
    }

    public String getmNotifTitle() {
        return mNotifTitle;
    }

    public void setmNotifTitle(String mNotifTitle) {
        this.mNotifTitle = mNotifTitle;
    }

    public String getmNotifImage() {
        return mNotifImage;
    }

    public void setmNotifImage(String mNotifImage) {
        this.mNotifImage = mNotifImage;
    }

    public String getmNotifMessage() {
        return mNotifMessage;
    }

    public void setmNotifMessage(String mNotifMessage) {
        this.mNotifMessage = mNotifMessage;
    }

    public String getmNotifUser() {
        return mNotifUser;
    }

    public void setmNotifUser(String mNotifUser) {
        this.mNotifUser = mNotifUser;
    }

    public String getmNotifDate() {
        return mNotifDate;
    }

    public void setmNotifDate(String mNotifDate) {
        this.mNotifDate = mNotifDate;
    }

    public String getmNotifTime() {
        return mNotifTime;
    }

    public void setmNotifTime(String mNotifTime) {
        this.mNotifTime = mNotifTime;
    }
}

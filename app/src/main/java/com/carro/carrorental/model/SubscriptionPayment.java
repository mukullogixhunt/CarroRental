package com.carro.carrorental.model;

import com.google.gson.annotations.SerializedName;

public class SubscriptionPayment {

    @SerializedName("m_strans_id")
    private String id;

    @SerializedName("m_strans_bookid")
    private String bookingId;

    @SerializedName("m_strans_date")
    private String date;

    @SerializedName("m_strans_amt")
    private String amount;

    @SerializedName("m_strans_status")
    private String status; // 1 = Paid, 0 = Unpaid

    @SerializedName("m_strans_addedon")
    private String addedOn;

    // ---------- Getters ----------
    public String getId() {
        return id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getAddedOn() {
        return addedOn;
    }

    // ---------- Helpers ----------
    public boolean isPaid() {
        return "1".equals(status);
    }
}


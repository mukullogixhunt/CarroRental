package com.carro.carrorental.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BranchModel {
    @SerializedName("m_branch_id")
    @Expose
    private String mBranchId;
    @SerializedName("m_branch_title")
    @Expose
    private String mBranchTitle;
    @SerializedName("m_branch_address")
    @Expose
    private String mBranchAddress;
    @SerializedName("m_branch_status")
    @Expose
    private String mBranchStatus;
    @SerializedName("m_branch_addedon")
    @Expose
    private String mBranchAddedon;
    @SerializedName("m_branch_updatedon")
    @Expose
    private String mBranchUpdatedon;
    @SerializedName("m_branch_icon")
    @Expose
    private String mBranchIcon;
    @SerializedName("m_branch_latt")
    @Expose
    private double mBranchLatt;
    @SerializedName("m_branch_long")
    @Expose
    private double mBranchLong;
    @SerializedName("m_branch_radius")
    @Expose
    private double mBranchRadius;



    public String getmBranchIcon() {
        return mBranchIcon;
    }

    public void setmBranchIcon(String mBranchIcon) {
        this.mBranchIcon = mBranchIcon;
    }

    @NonNull
    @Override
    public String toString() {
        return mBranchTitle;

    }

    public BranchModel(String mBranchTitle) {
        this.mBranchTitle = mBranchTitle;
    }

    public String getmBranchId() {
        return mBranchId;
    }

    public void setmBranchId(String mBranchId) {
        this.mBranchId = mBranchId;
    }

    public String getmBranchTitle() {
        return mBranchTitle;
    }

    public void setmBranchTitle(String mBranchTitle) {
        this.mBranchTitle = mBranchTitle;
    }

    public String getmBranchAddress() {
        return mBranchAddress;
    }

    public void setmBranchAddress(String mBranchAddress) {
        this.mBranchAddress = mBranchAddress;
    }

    public String getmBranchStatus() {
        return mBranchStatus;
    }

    public void setmBranchStatus(String mBranchStatus) {
        this.mBranchStatus = mBranchStatus;
    }

    public String getmBranchAddedon() {
        return mBranchAddedon;
    }

    public void setmBranchAddedon(String mBranchAddedon) {
        this.mBranchAddedon = mBranchAddedon;
    }

    public String getmBranchUpdatedon() {
        return mBranchUpdatedon;
    }

    public void setmBranchUpdatedon(String mBranchUpdatedon) {
        this.mBranchUpdatedon = mBranchUpdatedon;
    }

    public double getmBranchLatt() {
        return mBranchLatt;
    }

    public double getmBranchLong() {
        return mBranchLong;
    }

    public double getmBranchRadius() {
        return mBranchRadius;
    }
}

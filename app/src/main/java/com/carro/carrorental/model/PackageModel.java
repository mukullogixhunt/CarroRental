package com.carro.carrorental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageModel {

    @SerializedName("m_package_id")
    @Expose
    private String mPackageId;
    @SerializedName("m_package_title")
    @Expose
    private String mPackageTitle;
    @SerializedName("m_package_price")
    @Expose
    private String mPackagePrice;
    @SerializedName("m_package_img")
    @Expose
    private String mPackageImg;
    @SerializedName("m_package_thumbnail")
    @Expose
    private String mPackageThumbnail;
    @SerializedName("m_package_note")
    @Expose
    private String mPackageNote;
    @SerializedName("m_package_tc")
    @Expose
    private String mPackageTc;
    @SerializedName("m_package_details")
    @Expose
    private String mPackageDetails;
    @SerializedName("m_package_status")
    @Expose
    private String mPackageStatus;
    @SerializedName("m_package_addedon")
    @Expose
    private String mPackageAddedon;
    @SerializedName("m_package_updatedon")
    @Expose
    private String mPackageUpdatedon;

    public String getmPackageId() {
        return mPackageId;
    }

    public void setmPackageId(String mPackageId) {
        this.mPackageId = mPackageId;
    }

    public String getmPackageTitle() {
        return mPackageTitle;
    }

    public void setmPackageTitle(String mPackageTitle) {
        this.mPackageTitle = mPackageTitle;
    }

    public String getmPackagePrice() {
        return mPackagePrice;
    }

    public void setmPackagePrice(String mPackagePrice) {
        this.mPackagePrice = mPackagePrice;
    }

    public String getmPackageImg() {
        return mPackageImg;
    }

    public void setmPackageImg(String mPackageImg) {
        this.mPackageImg = mPackageImg;
    }

    public String getmPackageThumbnail() {
        return mPackageThumbnail;
    }

    public void setmPackageThumbnail(String mPackageThumbnail) {
        this.mPackageThumbnail = mPackageThumbnail;
    }

    public String getmPackageNote() {
        return mPackageNote;
    }

    public void setmPackageNote(String mPackageNote) {
        this.mPackageNote = mPackageNote;
    }

    public String getmPackageTc() {
        return mPackageTc;
    }

    public void setmPackageTc(String mPackageTc) {
        this.mPackageTc = mPackageTc;
    }

    public String getmPackageDetails() {
        return mPackageDetails;
    }

    public void setmPackageDetails(String mPackageDetails) {
        this.mPackageDetails = mPackageDetails;
    }

    public String getmPackageStatus() {
        return mPackageStatus;
    }

    public void setmPackageStatus(String mPackageStatus) {
        this.mPackageStatus = mPackageStatus;
    }

    public String getmPackageAddedon() {
        return mPackageAddedon;
    }

    public void setmPackageAddedon(String mPackageAddedon) {
        this.mPackageAddedon = mPackageAddedon;
    }

    public String getmPackageUpdatedon() {
        return mPackageUpdatedon;
    }

    public void setmPackageUpdatedon(String mPackageUpdatedon) {
        this.mPackageUpdatedon = mPackageUpdatedon;
    }
}

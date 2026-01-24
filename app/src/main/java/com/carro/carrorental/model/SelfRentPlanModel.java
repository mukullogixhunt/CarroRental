package com.carro.carrorental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfRentPlanModel {
    @SerializedName("sdh_id")
    @Expose
    private String sdhId;
    @SerializedName("sdh_ctype")
    @Expose
    private String sdhCtype;
    @SerializedName("sdh_hour")
    @Expose
    private String sdhHour;
    @SerializedName("sdh_km")
    @Expose
    private String sdhKm;
    @SerializedName("sdh_price")
    @Expose
    private String sdhPrice;
    @SerializedName("sdh_gper")
    @Expose
    private String sdhGper;
    @SerializedName("sdh_gamt")
    @Expose
    private String sdhGamt;
    @SerializedName("sdh_gtot")
    @Expose
    private String sdhGtot;
    @SerializedName("sdh_deposit_amt_within")
    @Expose
    private String sdhDepositAmtWithin;
    @SerializedName("sdh_deposit_amt_outside")
    @Expose
    private String sdhDepositAmtOutside;
    @SerializedName("sdh_remark")
    @Expose
    private String sdhRemark;
    @SerializedName("sdh_status")
    @Expose
    private String sdhStatus;
    @SerializedName("sdh_addedon")
    @Expose
    private String sdhAddedon;
    @SerializedName("sdh_pickup_amt")
    @Expose
    private String sdhPickupAmt;
    @SerializedName("sdh_drop_amt")
    @Expose
    private String sdhDropAmt;
    @SerializedName("sdh_pd_both")
    @Expose
    private String sdhPdBoth;
    @SerializedName("sdh_extra_km_amt")
    @Expose
    private String sdhExtraKmAmt;

    public String getSdhPickupAmt() {
        return sdhPickupAmt;
    }

    public void setSdhPickupAmt(String sdhPickupAmt) {
        this.sdhPickupAmt = sdhPickupAmt;
    }

    public String getSdhDropAmt() {
        return sdhDropAmt;
    }

    public void setSdhDropAmt(String sdhDropAmt) {
        this.sdhDropAmt = sdhDropAmt;
    }

    public String getSdhPdBoth() {
        return sdhPdBoth;
    }

    public void setSdhPdBoth(String sdhPdBoth) {
        this.sdhPdBoth = sdhPdBoth;
    }

    public String getSdhExtraKmAmt() {
        return sdhExtraKmAmt;
    }

    public void setSdhExtraKmAmt(String sdhExtraKmAmt) {
        this.sdhExtraKmAmt = sdhExtraKmAmt;
    }

    public String getSdhId() {
        return sdhId;
    }

    public void setSdhId(String sdhId) {
        this.sdhId = sdhId;
    }

    public String getSdhCtype() {
        return sdhCtype;
    }

    public void setSdhCtype(String sdhCtype) {
        this.sdhCtype = sdhCtype;
    }

    public String getSdhHour() {
        return sdhHour;
    }

    public void setSdhHour(String sdhHour) {
        this.sdhHour = sdhHour;
    }

    public String getSdhKm() {
        return sdhKm;
    }

    public void setSdhKm(String sdhKm) {
        this.sdhKm = sdhKm;
    }

    public String getSdhPrice() {
        return sdhPrice;
    }

    public void setSdhPrice(String sdhPrice) {
        this.sdhPrice = sdhPrice;
    }

    public String getSdhGper() {
        return sdhGper;
    }

    public void setSdhGper(String sdhGper) {
        this.sdhGper = sdhGper;
    }

    public String getSdhGamt() {
        return sdhGamt;
    }

    public void setSdhGamt(String sdhGamt) {
        this.sdhGamt = sdhGamt;
    }

    public String getSdhGtot() {
        return sdhGtot;
    }

    public void setSdhGtot(String sdhGtot) {
        this.sdhGtot = sdhGtot;
    }

    public String getSdhDepositAmtWithin() {
        return sdhDepositAmtWithin;
    }

    public void setSdhDepositAmtWithin(String sdhDepositAmtWithin) {
        this.sdhDepositAmtWithin = sdhDepositAmtWithin;
    }

    public String getSdhDepositAmtOutside() {
        return sdhDepositAmtOutside;
    }

    public void setSdhDepositAmtOutside(String sdhDepositAmtOutside) {
        this.sdhDepositAmtOutside = sdhDepositAmtOutside;
    }

    public String getSdhRemark() {
        return sdhRemark;
    }

    public void setSdhRemark(String sdhRemark) {
        this.sdhRemark = sdhRemark;
    }

    public String getSdhStatus() {
        return sdhStatus;
    }

    public void setSdhStatus(String sdhStatus) {
        this.sdhStatus = sdhStatus;
    }

    public String getSdhAddedon() {
        return sdhAddedon;
    }

    public void setSdhAddedon(String sdhAddedon) {
        this.sdhAddedon = sdhAddedon;
    }
}

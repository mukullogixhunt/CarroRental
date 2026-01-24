package com.carro.carrorental.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdvertiseModel {

	@SerializedName("data")
	private List<AdvertiseDataItem> data;

	@SerializedName("response")
	private String response;

	public List<AdvertiseDataItem> getData(){
		return data;
	}

	public String getResponse(){
		return response;
	}

    public static class AdvertiseDataItem{

        @SerializedName("m_adv_cust")
        private String mAdvCust;

        @SerializedName("m_adv_image")
        private String mAdvImage;

        @SerializedName("m_adv_id")
        private String mAdvId;

        @SerializedName("m_adv_web")
        private String mAdvWeb;

        @SerializedName("m_adv_status")
        private String mAdvStatus;

        @SerializedName("m_adv_addedon")
        private String mAdvAddedon;

        @SerializedName("m_adv_vendor")
        private String mAdvVendor;

        @SerializedName("m_adv_title")
        private String mAdvTitle;

        @SerializedName("m_adv_driver")
        private String mAdvDriver;

        public String getMAdvCust(){
            return mAdvCust;
        }

        public String getMAdvImage(){
            return mAdvImage;
        }

        public String getMAdvId(){
            return mAdvId;
        }

        public String getMAdvWeb(){
            return mAdvWeb;
        }

        public String getMAdvStatus(){
            return mAdvStatus;
        }

        public String getMAdvAddedon(){
            return mAdvAddedon;
        }

        public String getMAdvVendor(){
            return mAdvVendor;
        }

        public String getMAdvTitle(){
            return mAdvTitle;
        }

        public String getMAdvDriver(){
            return mAdvDriver;
        }
    }
}
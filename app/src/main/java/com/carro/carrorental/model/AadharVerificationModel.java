package com.carro.carrorental.model;

import com.google.gson.annotations.SerializedName;

public class AadharVerificationModel{

	@SerializedName("status_code")
	private int statusCode;

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("request_id")
	private int requestId;

	@SerializedName("status")
	private String status;

	public int getStatusCode(){
		return statusCode;
	}

	public Data getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public int getRequestId(){
		return requestId;
	}

	public String getStatus(){
		return status;
	}

    public static class Address{

        @SerializedName("country")
        private String country;

        @SerializedName("loc")
        private String loc;

        @SerializedName("subdist")
        private String subdist;

        @SerializedName("vtc")
        private String vtc;

        @SerializedName("street")
        private String street;

        @SerializedName("dist")
        private String dist;

        @SerializedName("state")
        private String state;

        @SerializedName("landmark")
        private String landmark;

        @SerializedName("house")
        private String house;

        @SerializedName("po")
        private String po;

        public String getCountry(){
            return country;
        }

        public String getLoc(){
            return loc;
        }

        public String getSubdist(){
            return subdist;
        }

        public String getVtc(){
            return vtc;
        }

        public String getStreet(){
            return street;
        }

        public String getDist(){
            return dist;
        }

        public String getState(){
            return state;
        }

        public String getLandmark(){
            return landmark;
        }

        public String getHouse(){
            return house;
        }

        public String getPo(){
            return po;
        }
    }

    public static class Data{

        @SerializedName("zip")
        private String zip;

        @SerializedName("mobile_hash")
        private String mobileHash;

        @SerializedName("care_of")
        private String careOf;

        @SerializedName("address")
        private Address address;

        @SerializedName("gender")
        private String gender;

        @SerializedName("email_hash")
        private String emailHash;

        @SerializedName("raw_xml")
        private String rawXml;

        @SerializedName("aadhaar_pdf")
        private Object aadhaarPdf;

        @SerializedName("face_status")
        private boolean faceStatus;

        @SerializedName("face_score")
        private int faceScore;

        @SerializedName("share_code")
        private String shareCode;

        @SerializedName("zip_data")
        private String zipData;

        @SerializedName("profile_image")
        private String profileImage;

        @SerializedName("full_name")
        private String fullName;

        @SerializedName("mobile_verified")
        private boolean mobileVerified;

        @SerializedName("has_image")
        private boolean hasImage;

        @SerializedName("aadhaar_number")
        private String aadhaarNumber;

        @SerializedName("dob")
        private String dob;

        @SerializedName("status")
        private String status;

        public String getZip(){
            return zip;
        }

        public String getMobileHash(){
            return mobileHash;
        }

        public String getCareOf(){
            return careOf;
        }

        public Address getAddress(){
            return address;
        }

        public String getGender(){
            return gender;
        }

        public String getEmailHash(){
            return emailHash;
        }

        public String getRawXml(){
            return rawXml;
        }

        public Object getAadhaarPdf(){
            return aadhaarPdf;
        }

        public boolean isFaceStatus(){
            return faceStatus;
        }

        public int getFaceScore(){
            return faceScore;
        }

        public String getShareCode(){
            return shareCode;
        }

        public String getZipData(){
            return zipData;
        }

        public String getProfileImage(){
            return profileImage;
        }

        public String getFullName(){
            return fullName;
        }

        public boolean isMobileVerified(){
            return mobileVerified;
        }

        public boolean isHasImage(){
            return hasImage;
        }

        public String getAadhaarNumber(){
            return aadhaarNumber;
        }

        public String getDob(){
            return dob;
        }

        public String getStatus(){
            return status;
        }
    }
}
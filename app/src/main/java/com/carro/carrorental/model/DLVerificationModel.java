package com.carro.carrorental.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DLVerificationModel{

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

    public static class Data{

        @SerializedName("gender")
        private String gender;

        @SerializedName("temporary_address")
        private String temporaryAddress;

        @SerializedName("sign")
        private String sign;

        @SerializedName("vehicle_classes")
        private List<String> vehicleClasses;

        @SerializedName("initial_doi")
        private String initialDoi;

        @SerializedName("permanent_address")
        private String permanentAddress;

        @SerializedName("father_or_husband_name")
        private String fatherOrHusbandName;

        @SerializedName("profile_image")
        private String profileImage;

        @SerializedName("has_image")
        private boolean hasImage;

        @SerializedName("ola_code")
        private String olaCode;

        @SerializedName("state")
        private String state;

        @SerializedName("permanent_zip")
        private String permanentZip;

        @SerializedName("less_info")
        private boolean lessInfo;

        @SerializedName("citizenship")
        private String citizenship;

        @SerializedName("additional_check")
        private List<Object> additionalCheck;

        @SerializedName("license_number")
        private String licenseNumber;

        @SerializedName("temporary_zip")
        private String temporaryZip;

        @SerializedName("ola_name")
        private String olaName;

        @SerializedName("transport_doi")
        private String transportDoi;

        @SerializedName("dob")
        private String dob;

        @SerializedName("name")
        private String name;

        @SerializedName("transport_doe")
        private String transportDoe;

        @SerializedName("blood_group")
        private String bloodGroup;

        @SerializedName("current_status")
        private String currentStatus;

        @SerializedName("doe")
        private String doe;

        @SerializedName("doi")
        private String doi;

        public String getGender(){
            return gender;
        }

        public String getTemporaryAddress(){
            return temporaryAddress;
        }

        public String getSign(){
            return sign;
        }

        public List<String> getVehicleClasses(){
            return vehicleClasses;
        }

        public String getInitialDoi(){
            return initialDoi;
        }

        public String getPermanentAddress(){
            return permanentAddress;
        }

        public String getFatherOrHusbandName(){
            return fatherOrHusbandName;
        }

        public String getProfileImage(){
            return profileImage;
        }

        public boolean isHasImage(){
            return hasImage;
        }

        public String getOlaCode(){
            return olaCode;
        }

        public String getState(){
            return state;
        }

        public String getPermanentZip(){
            return permanentZip;
        }

        public boolean isLessInfo(){
            return lessInfo;
        }

        public String getCitizenship(){
            return citizenship;
        }

        public List<Object> getAdditionalCheck(){
            return additionalCheck;
        }

        public String getLicenseNumber(){
            return licenseNumber;
        }

        public String getTemporaryZip(){
            return temporaryZip;
        }

        public String getOlaName(){
            return olaName;
        }

        public String getTransportDoi(){
            return transportDoi;
        }

        public String getDob(){
            return dob;
        }

        public String getName(){
            return name;
        }

        public String getTransportDoe(){
            return transportDoe;
        }

        public String getBloodGroup(){
            return bloodGroup;
        }

        public String getCurrentStatus(){
            return currentStatus;
        }

        public String getDoe(){
            return doe;
        }

        public String getDoi(){
            return doi;
        }
    }
}
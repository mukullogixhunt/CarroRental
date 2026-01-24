package com.carro.carrorental.model;

import com.google.gson.annotations.SerializedName;

public class AadharOtpSendModel{

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

        @SerializedName("valid_aadhaar")
        private boolean validAadhaar;

        @SerializedName("otp_sent")
        private boolean otpSent;

        @SerializedName("if_number")
        private boolean ifNumber;

        @SerializedName("status")
        private String status;

        public boolean isValidAadhaar(){
            return validAadhaar;
        }

        public boolean isOtpSent(){
            return otpSent;
        }

        public boolean isIfNumber(){
            return ifNumber;
        }

        public String getStatus(){
            return status;
        }
    }
}
package com.carro.carrorental.utils;


import com.carro.carrorental.BuildConfig;

public class ImagePathDecider {

    public static String getCompanyImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"company/";
    }
    public static String getUserImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"users/";
    }
    public static String getDriverImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"driver/";
    }
    public static String getDocumentImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"users/document/";
    }
    public static String getBranchImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"branch/";
    }
    public static String getCarImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"cars/";
    }
    public static String getNotificationImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"Notification/";
    }
    public static String getSliderImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"slider/";
    }
    public static String getOfferImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"offer/";
    }
    public static String getPackageImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"package/";
    }
    public static String getBusImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"bus/";
    }
    public static String getAdvertisementPath(){
        return BuildConfig.BASE_IMAGE_URL+"apps/";
    }
    public static String getHomePageImagePath(){
        return BuildConfig.BASE_IMAGE_URL+"apps/";
    }

    public static String getGooglePlacePath(){
        return BuildConfig.BASE_IMAGE_URL+"";
    }





}

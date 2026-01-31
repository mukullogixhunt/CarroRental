package com.carro.carrorental.api;

import android.media.tv.CommandResponse;

import com.carro.carrorental.api.response.BookingDetailResponse;
import com.carro.carrorental.api.response.BookingResponse;
import com.carro.carrorental.api.response.BranchResponse;
import com.carro.carrorental.api.response.BusResponse;
import com.carro.carrorental.api.response.CarResponse;
import com.carro.carrorental.api.response.CarTypeResponse;
import com.carro.carrorental.api.response.CityResponse;
import com.carro.carrorental.api.response.CouponResponse;
import com.carro.carrorental.api.response.CreateOrderResponse;
import com.carro.carrorental.api.response.HomePageResponse;
import com.carro.carrorental.api.response.LoginResponse;
import com.carro.carrorental.api.response.MarkAllReadResponse;
import com.carro.carrorental.api.response.MyBookingsResponse;
import com.carro.carrorental.api.response.NotificationResponse;
import com.carro.carrorental.api.response.OfferResponse;
import com.carro.carrorental.api.response.PlaceDetailsResponse;
import com.carro.carrorental.api.response.PackageResponse;
import com.carro.carrorental.api.response.PlacesAutocompleteResponse;
import com.carro.carrorental.api.response.RouteMatrixResponse;
import com.carro.carrorental.api.response.SelfCarResponse;
import com.carro.carrorental.api.response.SelfDetailsResponse;
import com.carro.carrorental.api.response.SelfRentPlanResponse;
import com.carro.carrorental.api.response.SelfSubPlanResponse;
import com.carro.carrorental.api.response.SliderResponse;
import com.carro.carrorental.api.response.StateResponse;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.model.AadharOtpSendModel;
import com.carro.carrorental.model.AadharVerificationModel;
import com.carro.carrorental.model.AdvertiseModel;
import com.carro.carrorental.model.CheckBlockModel;
import com.carro.carrorental.model.DLVerificationModel;
import com.carro.carrorental.utils.Constant;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {


    @FormUrlEncoded
    @POST(Constant.EndPoint.SIGNUP_USER)
    Call<LoginResponse> signUpUser(
            @Field(Constant.ApiKey.USER_MOBILE) String user_mobile
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.SEND_OTP)
    Call<BaseResponse> send_OTP(
            @Field(Constant.ApiKey.USER_MOBILE) String user_mobile
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.VERIFY_OTP)
    Call<BaseResponse> verify_OTP(
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.OTP) String otp
    );

    @Multipart
    @POST(Constant.EndPoint.UPDATE_PROFILE)
    Call<LoginResponse> update_profile(
            @Part(Constant.ApiKey.USER_ID) RequestBody user_id,
            @Part(Constant.ApiKey.USER_NAME) RequestBody user_name,
            @Part(Constant.ApiKey.USER_MOBILE) RequestBody user_mobile,
            @Part(Constant.ApiKey.USER_GENDER) RequestBody user_gender,
            @Part(Constant.ApiKey.USER_EMAIL) RequestBody user_email,
            @Part(Constant.ApiKey.USER_ALT_MOBILE) RequestBody user_alt_mobile,
            @Part(Constant.ApiKey.USER_ADDRESS) RequestBody user_address,
            @Part(Constant.ApiKey.USER_STATE) RequestBody user_state,
            @Part(Constant.ApiKey.USER_CITY) RequestBody user_city,
            @Part MultipartBody.Part user_pic
    );

    @Multipart
    @POST(Constant.EndPoint.UPDATE_DOCUMENT)
    Call<LoginResponse> update_dl(
            @Part(Constant.ApiKey.USER_ID) RequestBody user_id,
            @Part(Constant.ApiKey.USER_LICNO) RequestBody user_dl_no,
            @Part(Constant.ApiKey.USER_LIC_IS_DATE) RequestBody user_dl_issue,
            @Part(Constant.ApiKey.USER_LIC_EX_DATE) RequestBody user_dl_expiry,
            @Part MultipartBody.Part user_dl_front,
            @Part MultipartBody.Part user_dl_back
    );

    @Multipart
    @POST(Constant.EndPoint.UPDATE_DOCUMENT)
    Call<LoginResponse> update_adhar(
            @Part(Constant.ApiKey.USER_ID) RequestBody user_id,
            @Part(Constant.ApiKey.USER_AADHAR_NO) RequestBody user_adhar_no,
            @Part MultipartBody.Part user_adhar_front,
            @Part MultipartBody.Part user_adhar_back
    );

    @Multipart
    @POST(Constant.EndPoint.UPDATE_DOCUMENT)
    Call<LoginResponse> update_work(
            @Part(Constant.ApiKey.USER_ID) RequestBody user_id,
            @Part MultipartBody.Part user_working_proof,
            @Part MultipartBody.Part user_working_proofb
    );

    @Multipart
    @POST(Constant.EndPoint.UPDATE_DOCUMENT)
    Call<LoginResponse> update_other_doc(
            @Part(Constant.ApiKey.USER_ID) RequestBody user_id,
            @Part MultipartBody.Part user_other_doc,
            @Part MultipartBody.Part user_other_docb
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.NOTIFICATION)
    Call<NotificationResponse> notification(
            @Field(Constant.ApiKey.USER_ID) String user_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.MARK_AS_READ_NOTIFICATION) // Add this in your Constant.EndPoint
    Call<MarkAllReadResponse> markAllNotificationsRead(
            @Field(Constant.ApiKey.USER_ID) String user_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.DELETE_NOTIFICATION)
    Call<BaseResponse> deleteNotification(
            @Field(Constant.ApiKey.USER_ID) String user_id
    );


    @POST(Constant.EndPoint.SLIDER)
    Call<SliderResponse> getSlider();


    @POST(Constant.EndPoint.OFFER)
    Call<OfferResponse> getOffers();


    @GET("maps/api/place/autocomplete/json")
    Call<PlacesAutocompleteResponse> getPlaceAutocomplete(
            @Query("input") String input,
            @Query("key") String apiKey
    );


    @GET("maps/api/place/autocomplete/json")
    Call<PlacesAutocompleteResponse> getPlaceAutocompleteNearBy(
            @Query("input") String input,
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("strictbounds") String strictbounds,
            @Query("types") String types,
            @Query("key") String apiKey

    );
    @GET("maps/api/place/autocomplete/json")
    Call<PlacesAutocompleteResponse> getAirports(
            @Query("types") String types,
            @Query("input") String input,
            @Query("key") String apiKey
    );


    @GET("maps/api/place/details/json")
    Call<PlaceDetailsResponse> getPlaceDetails(
            @Query("place_id") String placeId,
            @Query("key") String apiKey
    );


    @GET("maps/api/distancematrix/json")
    Call<RouteMatrixResponse> getRouteMatrix(
            @Query("origins") String origins,
            @Query("destinations") String destinations,
            @Query("mode") String mode,
            @Query("key") String apiKey
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.CREATE_ORDER) // <-- IMPORTANT: USE YOUR ACTUAL SERVER URL
    Call<CreateOrderResponse> createRazorpayOrder(
            @Field("payable_amount") String amount
    );


    @FormUrlEncoded
    @POST(Constant.EndPoint.CREATE_ORDER_TEST) // <-- IMPORTANT: USE YOUR ACTUAL SERVER URL
    Call<CreateOrderResponse> createRazorpayOrderTest(
            @Field("payable_amount") String amount
    );


    @POST(Constant.EndPoint.CAR_TYPE)
    Call<CarTypeResponse> cartype();

    @FormUrlEncoded
    @POST(Constant.EndPoint.CAB_SERVICE)
    Call<CarTypeResponse> cab_service(
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id
    );



    @FormUrlEncoded
    @POST(Constant.EndPoint.CAB_CITY_RIDE)
    Call<CarTypeResponse> cabCityRide(
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id,
            @Field(Constant.ApiKey.HOUR) String hour
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.CAB_ONE_WAY)
    Call<CarTypeResponse> cabOneWay(
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.CAB_OUTSTATION)
    Call<CarTypeResponse> cabOutstation(
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.CAB_FLIGHT)
    Call<CarTypeResponse> cabFlight(
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id
    );


    @FormUrlEncoded
    @POST(Constant.EndPoint.SELF_SERVICE)
    Call<SelfCarResponse> self_service(
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id,
            @Field(Constant.ApiKey.PICKUP_DATE1) String pickup_date,
            @Field(Constant.ApiKey.PICKUP_TIME1) String pickup_time,
            @Field(Constant.ApiKey.DROP_DATE) String drop_date,
            @Field(Constant.ApiKey.DROP_TIME) String drop_time
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.SELF_SERVICE_SUBSCRIPTION)
    Call<SelfCarResponse> self_service_subcription(
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.LUXURY_SERVICE)
    Call<CarTypeResponse> luxury_service(
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.CAR)
    Call<CarResponse> car(
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id
    );
    @FormUrlEncoded
    @POST(Constant.EndPoint.SELF_SERVICE_DETAILS)
    Call<SelfDetailsResponse> self_service_details(
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_BOOKING)
    Call<BookingResponse> insert_booking(
            @Field(Constant.ApiKey.ROAD_TYPE) String road_type,
            @Field(Constant.ApiKey.PICKUP_ADDRESS) String pickup_address,
            @Field(Constant.ApiKey.DROP_ADDRESS) String drop_address,
            @Field(Constant.ApiKey.PICKUP_DATE) String pickup_date,
            @Field(Constant.ApiKey.PICKUP_TIME) String pickup_time,
            @Field(Constant.ApiKey.RETURN_DATE) String return_date,
            @Field(Constant.ApiKey.RETURN_TIME) String return_time,
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id,
            @Field(Constant.ApiKey.PRICE) String price,
            @Field(Constant.ApiKey.KM) String km,
            @Field(Constant.ApiKey.TOTAL) String total,
            @Field(Constant.ApiKey.PAY_TYPE) String pay_type,
            @Field(Constant.ApiKey.PAID_AMT) String paid_amt,
            @Field(Constant.ApiKey.PAYMODE_STATUS) String paymode_status,
            @Field(Constant.ApiKey.PAY_STATUS) String pay_status,
            @Field(Constant.ApiKey.B_STATUS) String b_status,
            @Field(Constant.ApiKey.B_UNAME) String b_uname,
            @Field(Constant.ApiKey.B_UMOBILE) String b_umobile,
            @Field(Constant.ApiKey.B_UEMAIL) String b_uemail,
            @Field(Constant.ApiKey.BKING_HOUR) String bking_hour,
            @Field(Constant.ApiKey.TRANS_ID) String trans_id,
            @Field(Constant.ApiKey.FLIGHT) String flight,
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id
    );




    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_CAB_SERVICE_BOOKING)
    Call<BookingResponse> insert_city_ride_booking(
            @Field(Constant.ApiKey.SERVICE_TYPE_CAT) String service_type_cat, // Should be "1"
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id,
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id,
            @Field(Constant.ApiKey.M_BKING_HOUR) String m_bking_hour,
            @Field(Constant.ApiKey.PICKUP_ADDRESS) String pickup_address,
            @Field(Constant.ApiKey.PICKUP_DATE) String pickup_date,
            @Field(Constant.ApiKey.PICKUP_TIME) String pickup_time,
            @Field(Constant.ApiKey.PRICE) String price,
            @Field(Constant.ApiKey.KM) String km,
            @Field(Constant.ApiKey.TOTAL) String total,
            // Standard Payment & Booking Info
            @Field(Constant.ApiKey.PAY_TYPE) String pay_type,
            @Field(Constant.ApiKey.PAID_AMT) String paid_amt,
            @Field(Constant.ApiKey.REMAIN_AMT) String remain_amt,
            @Field(Constant.ApiKey.TRANS_ID) String trans_id,
            @Field(Constant.ApiKey.PAY_STATUS) String pay_status,
            // Standard Coupon Info
            @Field(Constant.ApiKey.COUPONID) String couponid,
            @Field(Constant.ApiKey.DISCOUNT) String discount,
            @Field(Constant.ApiKey.DISCOUNT_AMT1) String discount_amt
    );




    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_CAB_SERVICE_BOOKING)
    Call<BookingResponse> insert_one_way_booking(
            @Field(Constant.ApiKey.SERVICE_TYPE_CAT) String service_type_cat, // Should be "2"
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id,
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id,
            @Field(Constant.ApiKey.PICKUP_ADDRESS) String pickup_address,
            @Field(Constant.ApiKey.FROM) String from,
            @Field(Constant.ApiKey.TO) String to,
            @Field(Constant.ApiKey.PICKUP_DATE) String pickup_date,
            @Field(Constant.ApiKey.PICKUP_TIME) String pickup_time,
            @Field(Constant.ApiKey.PRICE) String price,
            @Field(Constant.ApiKey.KM) String km,
            @Field(Constant.ApiKey.TOTAL) String total,
            // Standard Payment & Booking Info
            @Field(Constant.ApiKey.PAY_TYPE) String pay_type,
            @Field(Constant.ApiKey.PAID_AMT) String paid_amt,
            @Field(Constant.ApiKey.REMAIN_AMT) String remain_amt,
            @Field(Constant.ApiKey.TRANS_ID) String trans_id,
            @Field(Constant.ApiKey.PAY_STATUS) String pay_status,
            // Standard Coupon Info
            @Field(Constant.ApiKey.COUPONID) String couponid,
            @Field(Constant.ApiKey.DISCOUNT) String discount,
            @Field(Constant.ApiKey.DISCOUNT_AMT1) String discount_amt
    );


    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_CAB_SERVICE_BOOKING)
    Call<BookingResponse> insert_outstation_booking(
            @Field(Constant.ApiKey.SERVICE_TYPE_CAT) String service_type_cat, // Should be "3"
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id,
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id,
            @Field(Constant.ApiKey.FROM) String from,
            @Field(Constant.ApiKey.TO) String to,
            @Field(Constant.ApiKey.PICKUP_DATE) String pickup_date,
            @Field(Constant.ApiKey.PICKUP_TIME) String pickup_time,
            @Field(Constant.ApiKey.RETURN_DATE) String return_date,
            @Field(Constant.ApiKey.RETURN_TIME) String return_time,
            @Field(Constant.ApiKey.PRICE) String price,
            @Field(Constant.ApiKey.KM) String km,
            @Field(Constant.ApiKey.TOTAL) String total,
            // Standard Payment & Booking Info
            @Field(Constant.ApiKey.PAY_TYPE) String pay_type,
            @Field(Constant.ApiKey.PAID_AMT) String paid_amt,
            @Field(Constant.ApiKey.REMAIN_AMT) String remain_amt,
            @Field(Constant.ApiKey.TRANS_ID) String trans_id,
            @Field(Constant.ApiKey.PAY_STATUS) String pay_status,
            // Standard Coupon Info
            @Field(Constant.ApiKey.COUPONID) String couponid,
            @Field(Constant.ApiKey.DISCOUNT) String discount,
            @Field(Constant.ApiKey.DISCOUNT_AMT1) String discount_amt
    );


    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_CAB_SERVICE_BOOKING)
    Call<BookingResponse> insert_airport_booking(
            @Field(Constant.ApiKey.SERVICE_TYPE_CAT) String service_type_cat, // Should be "4"
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id,
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id,
            @Field(Constant.ApiKey.PICKUP_ADDRESS) String pickup_address,
            @Field(Constant.ApiKey.DROP_ADDRESS) String drop_address,
            @Field(Constant.ApiKey.PICKUP_DATE) String pickup_date,
            @Field(Constant.ApiKey.PICKUP_TIME) String pickup_time,
            @Field(Constant.ApiKey.PRICE) String price,
            @Field(Constant.ApiKey.KM) String km,
            @Field(Constant.ApiKey.TOTAL) String total,
            // Standard Payment & Booking Info
            @Field(Constant.ApiKey.PAY_TYPE) String pay_type,
            @Field(Constant.ApiKey.PAID_AMT) String paid_amt,
            @Field(Constant.ApiKey.REMAIN_AMT) String remain_amt,
            @Field(Constant.ApiKey.TRANS_ID) String trans_id,
            @Field(Constant.ApiKey.PAY_STATUS) String pay_status,
            // Standard Coupon Info
            @Field(Constant.ApiKey.COUPONID) String couponid,
            @Field(Constant.ApiKey.DISCOUNT) String discount,
            @Field(Constant.ApiKey.DISCOUNT_AMT1) String discount_amt,
            @Field(Constant.ApiKey.FLIGHT_TYPE) String flight_type
    );








    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_SELFDRIVE_BOOKING)
    Call<BookingResponse> insert_selfdrive_booking(
//            @Field(Constant.ApiKey.PICKUP_ADDRESS) String pickup_address,
            @Field(Constant.ApiKey.PICKUP_DATE) String pickup_date,
            @Field(Constant.ApiKey.PICKUP_TIME) String pickup_time,
            @Field(Constant.ApiKey.RETURN_DATE) String return_date,
            @Field(Constant.ApiKey.RETURN_TIME) String return_time,
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id,
            @Field(Constant.ApiKey.PRICE) String price,
            @Field(Constant.ApiKey.KM) String km,
            @Field(Constant.ApiKey.TOTAL) String total,
            @Field(Constant.ApiKey.PAYMODE_STATUS) String paymode_status,
            @Field(Constant.ApiKey.PAY_STATUS) String pay_status,
            @Field(Constant.ApiKey.B_STATUS) String b_status,
            @Field(Constant.ApiKey.B_UNAME) String b_uname,
            @Field(Constant.ApiKey.B_UMOBILE) String b_umobile,
            @Field(Constant.ApiKey.B_UEMAIL) String b_uemail,
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id,
            @Field(Constant.ApiKey.PACKAGE_ID) String package_id,
            @Field(Constant.ApiKey.TRANS_ID) String trans_id,
            @Field(Constant.ApiKey.INSIDE_OUTSIDE_STATE) String inside_outside_state
    );
    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_LUXURYCAR_BOOKING)
    Call<BookingResponse> insert_luxurycar_booking(
//            @Field(Constant.ApiKey.PICKUP_ADDRESS) String pickup_address,
            @Field(Constant.ApiKey.PICKUP_DATE) String pickup_date,
            @Field(Constant.ApiKey.PICKUP_TIME) String pickup_time,
            @Field(Constant.ApiKey.RETURN_DATE) String return_date,
            @Field(Constant.ApiKey.RETURN_TIME) String return_time,
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id,
            @Field(Constant.ApiKey.PRICE) String price,
            @Field(Constant.ApiKey.TOTAL) String total,
            @Field(Constant.ApiKey.PAYMODE_STATUS) String paymode_status,
            @Field(Constant.ApiKey.PAY_STATUS) String pay_status,
            @Field(Constant.ApiKey.B_STATUS) String b_status,
            @Field(Constant.ApiKey.B_UNAME) String b_uname,
            @Field(Constant.ApiKey.B_UMOBILE) String b_umobile,
            @Field(Constant.ApiKey.B_UEMAIL) String b_uemail,
            @Field(Constant.ApiKey.TRANS_ID) String trans_id,
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_BUS_BOOKING)
    Call<BookingResponse> insert_bus_booking(
//            @Field(Constant.ApiKey.PICKUP_ADDRESS) String pickup_address,
            @Field(Constant.ApiKey.PICKUP_DATE) String pickup_date,
            @Field(Constant.ApiKey.PICKUP_TIME) String pickup_time,
            @Field(Constant.ApiKey.RETURN_DATE) String return_date,
            @Field(Constant.ApiKey.RETURN_TIME) String return_time,
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.BUS_ID) String bus_id,
            @Field(Constant.ApiKey.PRICE) String price,
            @Field(Constant.ApiKey.KM) String km,
            @Field(Constant.ApiKey.TOTAL) String total,
            @Field(Constant.ApiKey.PAYMODE_STATUS) String paymode_status,
            @Field(Constant.ApiKey.PAY_STATUS) String pay_status,
            @Field(Constant.ApiKey.B_STATUS) String b_status,
            @Field(Constant.ApiKey.B_UNAME) String b_uname,
            @Field(Constant.ApiKey.B_UMOBILE) String b_umobile,
            @Field(Constant.ApiKey.B_UEMAIL) String b_uemail,
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.CURRENT_BOOKING)
    Call<MyBookingsResponse> currentBooking(
            @Field(Constant.ApiKey.USER_ID) String user_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.UPCOMING_BOOKING)
    Call<MyBookingsResponse> upcomingBooking(
            @Field(Constant.ApiKey.USER_ID) String user_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.HISTORY_BOOKING)
    Call<MyBookingsResponse> historyBooking(
            @Field(Constant.ApiKey.USER_ID) String user_id
    );

    @POST(Constant.EndPoint.PACKAGE)
    Call<PackageResponse> get_package();

    @POST(Constant.EndPoint.BRANCH)
    Call<BranchResponse> get_branch();

    @FormUrlEncoded
    @POST(Constant.EndPoint.CANCEL_BOOKING)
    Call<BaseResponse> cancelBooking(
            @Field(Constant.ApiKey.BKING_ID) String bking_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.USER_DETAILS)
    Call<LoginResponse> user_details(
            @Field(Constant.ApiKey.USER_ID) String user_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.UPDATE_BOOKING)
    Call<BaseResponse> update_booking(
            @Field(Constant.ApiKey.BKING_ID) String bking_id,
            @Field(Constant.ApiKey.B_UNAME) String b_uname,
            @Field(Constant.ApiKey.B_UMOBILE) String b_umobile,
            @Field(Constant.ApiKey.B_UEMAIL) String b_uemail,
            @Field(Constant.ApiKey.B_PICKUP_ADDRESS) String b_pickup_address,
            @Field(Constant.ApiKey.B_BRANCH) String b_branch
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.UPDATE_BOOKING_PAYMENT)
    Call<BaseResponse> update_booking_payment(
            @Field(Constant.ApiKey.BKING_ID) String bking_id,
            @Field(Constant.ApiKey.PRE_PAID_AMT) String pre_paid_amt,
            @Field(Constant.ApiKey.PAID_AMT) String paid_amt,
            @Field(Constant.ApiKey.TOTAL) String total,
            @Field(Constant.ApiKey.TRANS_ID) String trans_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.GET_COUNTRY_STATE)
    Call<StateResponse> get_country_state(
            @Field(Constant.ApiKey.COUNTRY_CODE) String country_code
    );
    @FormUrlEncoded
    @POST(Constant.EndPoint.GET_STATE_CITIES)
    Call<CityResponse> get_state_cities(
            @Field(Constant.ApiKey.COUNTRY_CODE) String country_code,
            @Field(Constant.ApiKey.STATE_CODE) String state_code
    );
    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_REVIEW)
    Call<BaseResponse> insert_review(
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.RATING) String rating,
            @Field(Constant.ApiKey.REMARK) String remark
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.BUS)
    Call<BusResponse> bus(
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id
    );

    @POST(Constant.EndPoint.HOME_PAGE)
    Call<HomePageResponse> home_page();


    @FormUrlEncoded
    @POST(Constant.EndPoint.UPDATE_FCM)
    Call<BaseResponse> updateFCM(
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.FCM_TOKEN) String fcmtoken
    );


    @FormUrlEncoded
    @POST(Constant.EndPoint.SELF_SERVICE_HOURLY_LIST)
    Call<SelfRentPlanResponse> self_service_hourly_list(
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.SELF_SERVICE_SUBS)
    Call<SelfSubPlanResponse> self_service_subs(
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.BOOKED_LIST)
    Call<BaseResponse> check_booked_list(
            @Field(Constant.ApiKey.C_TYPE_ID) String ctype_id,
            @Field(Constant.ApiKey.FROM_DATE) String from_date,
            @Field(Constant.ApiKey.TO_DATE) String to_date
    );


    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_SELFDRIVE_BOOKING)
    Call<BaseResponse> insert_self_drive_booking(
            @Field(Constant.ApiKey.B_TYPE_CAT) String b_type_cat,
            @Field(Constant.ApiKey.BRANCH_ID) String branch_id,
            @Field(Constant.ApiKey.PACKAGE_ID) String package_id,
            @Field(Constant.ApiKey.SUBS_ID) String subs_id,
            @Field(Constant.ApiKey.PICKUP_DATE) String pickup_date,
            @Field(Constant.ApiKey.PICKUP_TIME) String pickup_time,
            @Field(Constant.ApiKey.RETURN_DATE) String return_date,
            @Field(Constant.ApiKey.RETURN_TIME) String return_time,
            @Field(Constant.ApiKey.INSIDE_OUTSIDE_STATE) String inside_outside_state,
            @Field(Constant.ApiKey.INSIDE_OUTSIDE_STATE_AMT) String inside_outside_state_amt,
            @Field(Constant.ApiKey.PICK_DROP_BOTH) String pick_drop_both,
            @Field(Constant.ApiKey.DELIVERY_LOCATION) String delivery_location,
            @Field(Constant.ApiKey.PICK_DROP_BOTH_AMT) String pick_drop_both_amt,
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.CTYPE_ID) String ctype_id,
            @Field(Constant.ApiKey.PRICE) String price,
            @Field(Constant.ApiKey.KM) String km,
            @Field(Constant.ApiKey.TOTAL) String total,
            @Field(Constant.ApiKey.PAID_AMT) String paid_amt,
            @Field(Constant.ApiKey.REMAIN_AMT) String remain_amt,
            @Field(Constant.ApiKey.PAYMODE_STATUS) String paymode_status,
            @Field(Constant.ApiKey.PAY_STATUS) String pay_status,
            @Field(Constant.ApiKey.FASTAG) String fastag,
            @Field(Constant.ApiKey.BKING_TC) String bking_tc,
            @Field(Constant.ApiKey.COUPONID) String couponid,
            @Field(Constant.ApiKey.DISCOUNT) String discount,
            @Field(Constant.ApiKey.DISCOUNT_AMT) String discount_amt,
            @Field(Constant.ApiKey.MONTH_DUR) String month_dur
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.COUPON_CODE)
    Call<CouponResponse> get_coupon(
            @Field(Constant.ApiKey.COUPON_CODE) String coupon_code
            );



    @POST(Constant.EndPoint.ADVERTISE)
    Call<AdvertiseModel> get_advertise();

    @FormUrlEncoded
    @POST(Constant.EndPoint.ADD_REMARK)
    Call<BaseResponse> addRemark(
            @Field(Constant.ApiKey.BKING_ID) String bking_id,
            @Field(Constant.ApiKey.BKING_REMARK) String bKing_remark
    );
    @FormUrlEncoded
    @POST(Constant.EndPoint.BOOKING_DETAILS)
    Call<BookingDetailResponse> getBookingDetails(
            @Field(Constant.ApiKey.BKING_ID) String bking_id
    );
    @FormUrlEncoded
    @POST(Constant.EndPoint.CHECK_BLOCK_WITH_ID)
    Call<CheckBlockModel> check_block_with_id(
            @Field(Constant.ApiKey.USER_ID) String user_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.UPDATE_ADHAR)
    Call<BaseResponse> update_adhar(
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field(Constant.ApiKey.AADHAR_NO) String aadhar_no,
            @Field("adhar_name") String adhar_name,
            @Field("adhar_dob") String adhar_dob,
            @Field("adhar_add") String adhar_add,
            @Field("adhar_gender") String adhar_gender,
            @Field("adhar_zipcode") String adhar_zipcode
    );
    @FormUrlEncoded
    @POST(Constant.EndPoint.UPDATE_DRIVING_LIC)
    Call<BaseResponse> update_driving_lic(
            @Field(Constant.ApiKey.USER_ID) String user_id,
            @Field("dl_no") String dl_no,
            @Field("dl_name") String dl_name,

            @Field("dl_temp_add") String dl_temp_add,
            @Field("dl_temp_zip") String dl_temp_zip,
            @Field("dl_per_add") String dl_per_add,
            @Field("dl_per_zip") String dl_per_zip,
            @Field("dl_state") String dl_state,
            @Field("dl_olacode") String dl_olacode,
            @Field("dl_olaname") String dl_olaname,
            @Field("dl_gender") String dl_gender,
            @Field("dl_fh_name") String dl_fh_name,
            @Field("dl_dob") String dl_dob,
            @Field("dl_blood") String dl_blood,
            @Field("dl_veh_class") List<String> dl_veh_class,
            @Field("dl_cur_status") String dl_cur_status,
            @Field("dl_isdate") String dl_isdate,
            @Field("dl_exdate") String adhar_zipcode
    );




    @POST(Constant.EndPoint.AADHAAR_GENERATE_OTP)
    Call<AadharOtpSendModel> aadhaar_generate_otp(
            @Body RequestBody body
    );

    @POST(Constant.EndPoint.AADHAAR_SUBMIT_OTP)
    Call<AadharVerificationModel> aadhaar_submit_otp(
            @Body RequestBody body
    );
    @POST(Constant.EndPoint.DRIVING_LICENSE)
    Call<DLVerificationModel> driving_license(
            @Body RequestBody body
    );

    @Multipart
    @POST("update_self_car_image")
    Call<BaseResponse> uploadSelfCarImages(
            @Part("bking_id") RequestBody userId,

            @Part MultipartBody.Part m_bking_side_img1,
            @Part MultipartBody.Part m_bking_side_img2,
            @Part MultipartBody.Part m_bking_side_img3,
            @Part MultipartBody.Part m_bking_side_img4,

            @Part MultipartBody.Part m_bking_int_img1,
            @Part MultipartBody.Part m_bking_int_img2,

            @Part MultipartBody.Part m_bking_meter_img,
            @Part MultipartBody.Part m_bking_toolkit_img,
            @Part MultipartBody.Part m_bking_sphare_tyre,

            @Part List<MultipartBody.Part> m_bking_scratch
    );

}

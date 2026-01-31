package com.carro.carrorental.utils;

public class Constant {

    public static final String CARRO_RENTAL = "carrorental";

    public static final String ddMMyyyy = "dd-MM-yyyy";
    public static final String ddMMMyyyy = "dd-MMM-yyyy";
    public static final String yyyyMMdd = "yyyy-MM-dd";
    public static final String yyyyMMdd_HHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String ddMMyyyy_HHMMSSA = "dd-MM-yyyy hh:mm a";

    public static final String HHMMSSA = "hh:mm a";
    public static final String HHMMSS = "HH:mm:ss";
    public static final String SUCCESS_RESPONSE_CODE = "200";
    public static final String SUCCESS_RESPONSE = "success";

    public static String WEBVIEW_TITLE = "";
    public static String WEBVIEW_URL = "";

    public static String FILENAME = "file";
    public static String DRIVING_MODE = "driving";


//    public static String GOOGLE_MAP_API_KEY = "AIzaSyBgiv49mgm6XjZjSVjJesafwhZMHQsj-DM";
    public static String GOOGLE_MAP_API_KEY = "AIzaSyAdL2M0deALjXXB5WQBDLIdumZmwLDWCr0";
    public static String QUICKEKYC_API_KEY = "d2ebfb20-199d-4ed1-8c82-9cba3f1614a0";

    public static final String CURRENCY_FORMAT = "#,##,##0.00";


    public static String IS_FROM = "is_from";


    public interface BundleExtras {
        String PHONE_NUMBER = "phoneNumber";
        String WAY_TYPE = "wayType";
        String FLIGHT_TYPE = "flightType";
        String CAB_SERVICE_TYPE = "cab_service_type";
        String PICK_ADDRESS = "pick_address";
        String PICK_DATE = "pick_date";
        String AVAIL_DATE = "pick_date";
        String PICK_TIME = "pick_time";
        String RETURN_DATE = "return_date";
        String RETURN_TIME = "return_time";
        String DROP_ADDRESS = "drop_address";
        String DROP_DATE = "drop_date";
        String DROP_TIME = "drop_time";
        String C_TYPE_ID = "cTypeId";
        String C_TYPE_NAME = "cTypeName";
        String CAR_ID = "carId";
        String VENDOR_ID = "vendorId";
        String PRICE = "price";
        String ADDRESS_FROM = "addressFrom";
        String ADDRESS_TYPE = "addressType";
        String SCREEN_TYPE = "screenType";
        String PAGE_TYPE = "pageType";

        String BOOK_TYPE = "bookType";
        String ADDRESS_MAIN_PICK = "addressMainPick";
        String ADDRESS_MAIN_DROP = "addressMainDrop";
        String INPUT_TYPE = "inputType";
        String FROM_FRAGMENT = "fromFragment";
        String B_NAME = "b_name";
        String B_MOBILE = "b_mobile";
        String B_EMAIL = "b_email";


        String LAT_PICK = "latPick";
        String LNG_PICK = "lngPick";
        String LAT_DROP = "latDrop";
        String LNG_DROP = "lngDrop";

        String MAP_DISTANCE = "mapDistance";
        String MAP_DISTANCE_VALUE = "mapDistanceValue";
        String MAP_DURATION = "mapDuration";
        String CAR_DATA = "carData";
        String BUS_DATA = "busData";
        String PACKAGE_DATA = "packageData";
        String BOOKING_ID = "booking_id";
        String BRANCH_ID = "branch_id";
        String BRANCH_NAME = "branch_name";
        String BOOKING_DATA = "booking_data";
        String IN_OUT_SIDE = "in_out_side";


        String TOTAL_MINUTES = "total_minutes";
        String FROM_ACTIVITY = "from_activity";
    }

    public interface PreferenceConstant {

        String FIREBASE_TOKEN = "firebase_token";
        String IS_LOGIN = "isLogin";

        String USER_DATA = "user_data";
        String USER_ID = "user_id";
        String PICKUP_LOCATION = "pickup_location";
        String DROP_LOCATION = "drop_location";

        String PICK_LAT = "pickLat";
        String PICK_LNG = "pickLng";
        String DROP_LAT = "dropLat";
        String DROP_LNG = "DropLng";
        String MAP_DISTANCE = "mapDistance";
        String MAP_DURATION = "mapDuration";
        String KM_PRICE = "km_price";
        String HOUR_TYPE = "hour_type";

        String WEBVIEW_INC = "webIC";
        String WEBVIEW_EXC = "webExc";
        String WEBVIEW_TC = "webTc";
        String BRANCH_ID = "branch_id";
        String BRANCH_NAME = "branch_name";
        String BRANCH_LATT= "branch_latt";
        String BRANCH_LONG = "branch_long";
        String BRANCH_RADIUS = "branch_radius";
        String PACKAGE_ID = "package_id";
        String IS_FROM_AIRPORT = "is_from_airport";
        String AIRPORT_NAME = "airport_name";
        String AIRPORT_LAT = "airport_lat";
        String AIRPORT_LONG = "airport_long";



    }

    public interface ApiKey {

        String USER_MOBILE = "user_mobile";
        String USER_ID = "user_id";
        String AADHAR_NO = "adhar_no";
        String USER_AADHAR_NO = "user_adharno";
        String USER_AADHAR = "user_adhar";
        String USER_AADHAR_B = "user_adharb";
        String USER_LICNO = "user_licno";
        String USER_DRIVER_LIC = "user_drive_lic";
        String USER_DRIVER_LIC_B = "user_drive_licb";
        String USER_LIC_IS_DATE = "user_lic_isdate";
        String USER_LIC_EX_DATE = "user_lic_exdate";
        String USER_WORKING_PROOF = "user_working_proof";
        String USER_WORKING_PROOFB = "user_working_proofb";
        String USER_OTHER_DOC = "user_other_doc";
        String USER_OTHER_DOCB = "user_other_docb";


        String OTP = "otp";
        String USER_NAME = "user_name";
        String USER_GENDER = "user_gender";
        String USER_PIC = "user_pic";
        String CTYPE_ID = "ctype_id";
        String BUS_ID = "bus_id";
        String ROAD_TYPE = "road_type";
        String BKING_TYPE = "bking_type";
        String PICKUP_ADDRESS = "pickup_address";
        String DROP_ADDRESS = "drop_address";
        String PICKUP_DATE = "pickup_date";
        String PICKUP_TIME = "pickup_time";
        String RETURN_DATE = "return_date";
        String RETURN_TIME = "return_time";
        String VENDOR_ID = "vendor_id";
        String CAR_ID = "car_id";
        String DRIVER_ID = "driver_id";
        String PRICE = "price";
        String KM = "km";
        String TOTAL = "total";
        String PAYMODE_STATUS = "paymode_status";
        String PAY_STATUS = "pay_status";
        String COUPONID = "couponid";
        String DISCOUNT  = "discount";
        String DISCOUNT_AMT1 = "discount_amt";
        String FLIGHT_TYPE = "flight_type"; //1 from airport // 2 to airport
        String REMAIN_AMT = "remain_amt";
        String FASTAG = "fastag";
        String BKING_TC = "bking_tc";
        String PAY_TYPE = "pay_type";
        String PAID_AMT = "paid_amt";
        String B_STATUS = "b_status";
        String BKING_ID = "bking_id";
        String BKING_REMARK = "bking_remark";
        String B_UNAME = "b_uname";
        String B_UMOBILE = "b_umobile";
        String B_UEMAIL = "b_uemail";
        String B_PICKUP_ADDRESS = "b_pickup_address";
        String B_BRANCH = "b_branch";
        String BKING_HOUR = "bking_hour";
        String BRANCH_ID = "branch_id";


        String DROP_TIME = "drop_time";
        String DROP_DATE = "drop_date";
        String PICKUP_TIME1 = "pickup_time";
        String PICKUP_DATE1 = "pickup_date";




        String PACKAGE_ID = "package_id";
        String SUBS_ID = "subs_id";
        String COUNTRY_CODE = "country_code";
        String STATE_CODE = "state_code";
        String RATING = "rating";
        String REMARK = "remark";
        String PRE_PAID_AMT = "pre_paid_amt";
        String TRANS_ID = "trans_id";
        String FLIGHT = "flight";
        String CITY = "city";
        String USER_EMAIL = "user_email";
        String USER_ALT_MOBILE = "user_alt_mobile";
        String USER_ADDRESS = "user_address";
        String USER_STATE = "user_state";
        String USER_CITY = "user_city";
        String INSIDE_OUTSIDE_STATE = "inside_outside_state";
        String INSIDE_OUTSIDE_STATE_AMT = "inside_outside_state_amt";
        String PICK_DROP_BOTH = "pick_drop_both";
        String PICK_DROP_BOTH_AMT = "pick_drop_both_amt";
        String DELIVERY_LOCATION = "delivery_location";
        String FCM_TOKEN = "fcmtoken";
        String B_TYPE_CAT = "b_type_cat";
        String C_TYPE_ID = "ctype_id";
        String FROM_DATE = "from_date";
        String TO_DATE = "to_date";
        String COUPON_CODE = "coupon_code";
        String MONTH_DUR = "month_dur";
        String PICKUP_LAT = "pickup_latt";
        String PICKUP_LNG = "pickup_long";
        String DROP_LAT = "drop_latt";
        String DROP_LNG = "drop_long";

        String DISCOUNT_AMT = "discount_amt";

        String SERVICE_TYPE_CAT = "service_type_cat";
        String FROM = "from";
        String TO = "to";
        String M_BKING_HOUR = "m_bking_hour";
        String PAY_TYPE1 = "pay_type";
        String HOUR = "hour";
    }

    public interface EndPoint {

        String SIGNUP_USER = "signup_user";
        String SEND_OTP = "send_otp";
        String VERIFY_OTP = "verify_otp";
        String UPDATE_PROFILE = "update_profile";
        String MARK_AS_READ_NOTIFICATION = "notif_mark_as_read";
        String NOTIFICATION = "notification";
        String DELETE_NOTIFICATION = "delete_notification";
        String SLIDER = "slider";
        String CAR_TYPE = "cartype";
        String OFFER = "offer";
        String CAR = "car";
        String INSERT_BOOKING = "insert_booking";
        String INSERT_SELFDRIVE_BOOKING = "insert_selfdrive_booking";
        String INSERT_LUXURYCAR_BOOKING = "insert_luxurycar_booking";
        String INSERT_BUS_BOOKING = "insert_bus_booking";
        String CURRENT_BOOKING = "currentBooking";
        String UPCOMING_BOOKING = "upcomingBooking";
        String HISTORY_BOOKING = "historyBooking";
        String PACKAGE = "package";
        String CANCEL_BOOKING = "cancelBooking";
        String UPDATE_BOOKING = "update_booking";
        String UPDATE_BOOKING_PAYMENT = "update_booking_payment";
        String BRANCH = "branch";
        String GET_STATE_CITIES = "get_state_cities";
        String GET_COUNTRY_STATE = "get_country_state";
        String BUS = "bus";
        String INSERT_REVIEW = "insert_review";
        String HOME_PAGE = "home_page";
        String CAB_SERVICE = "cab_service";
        String CAB_CITY_RIDE = "cab_city_ride";
        String CAB_ONE_WAY = "cab_one_way";
        String CAB_OUTSTATION = "cab_outstation";
        String CAB_FLIGHT = "cab_flight";
        String SELF_SERVICE = "self_service";
        String SELF_SERVICE_SUBSCRIPTION = "self_service_subcription";
        String SELF_SERVICE_DETAILS = "self_service_details";
        String LUXURY_SERVICE = "luxury_service";
        String UPDATE_FCM = "update_fcm";
        String SELF_SERVICE_HOURLY_LIST = "self_service_hourly_list";
        String SELF_SERVICE_SUBS = "self_service_subs";
        String UPDATE_DOCUMENT = "update_document";
        String UPDATE_ADHAR = "update_adhar";
        String UPDATE_DRIVING_LIC = "update_driving_lic";
        String BOOKED_LIST = "booked_list";
        String USER_DETAILS = "user_details";
        String COUPON_CODE = "coupon_code";
        String BOOKING_DETAILS = "booking_details";


        String CREATE_ORDER = "create_order";
        String CREATE_ORDER_TEST = "create_order_test";
        String INSERT_CAB_SERVICE_BOOKING = "insert_cabservice_booking";


        String ADVERTISE = "advertise";
        String ADD_REMARK = "update_remark";

        String CHECK_BLOCK_WITH_ID = "check_block_with_id";

        String AADHAAR_GENERATE_OTP = "aadhaar-v2/generate-otp";
        String AADHAAR_SUBMIT_OTP = "aadhaar-v2/submit-otp";
        String DRIVING_LICENSE  = "driving-license/driving-license";
    }
}

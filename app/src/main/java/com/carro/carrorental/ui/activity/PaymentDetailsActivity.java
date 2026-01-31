
package com.carro.carrorental.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.RazorPayApiClient;
import com.carro.carrorental.api.response.BookingResponse;
import com.carro.carrorental.api.response.CouponResponse;
import com.carro.carrorental.api.response.CreateOrderResponse;
import com.carro.carrorental.databinding.ActivityPaymentDetailsBinding;
import com.carro.carrorental.model.BookingModel;
import com.carro.carrorental.model.CouponModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.NumberUtils;
import com.carro.carrorental.utils.PreferenceUtils;
import com.carro.carrorental.utils.Utils;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailsActivity extends BaseActivity implements PaymentResultListener {

    private static final String TAG = "PaymentDetailActivity";


    ActivityPaymentDetailsBinding binding;
    List<BookingModel> bookingModelList = new ArrayList<>();
    LoginModel loginModel = new LoginModel();
    Dialog dialog;
    String wayType;
    String cabServiceType;
    String pick_date = "";
    String pick_time = "";
    String pickAddress = "";
    String pickLat = "";
    String pickLng = "";
    String drop_date = "";
    String drop_time = "";
    String drop_address = "";
    String dropLat = "";
    String dropLng = "";
    String airport_name = "";
    String branch = "";
    String userId;
    String cTypeId;
    String carTypeName;
    String carId;
    String vendorId;
    String totalFare;
    String km_price;
    String map_distance;
    String map_duration;
    String b_name;
    String b_mobile;
    String b_email;
    String pick_date2;
    String drop_date2;
    String inclink;
    String excLink;
    String tcLink;
    String hour;
    String paid_amt;
    String remaining_amt;
    String pay_type;
    String pickTimeIn24Hour;
    String returnTimeIn24Hour;
    String flightType;


    CouponModel couponModel = null ;
    String couponAmt = "";
    String couponPercent = "";


    // Change these variables at the top of your class
    private double finalTotalAmt = 0.0;
    private double rest_price = 0.0;
    private double finalCouponAmt = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentDetailsBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getPreferenceData();
    }

    private void getPreferenceData() {
        userId = PreferenceUtils.getString(Constant.PreferenceConstant.USER_ID, PaymentDetailsActivity.this);
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, PaymentDetailsActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);

        map_duration = PreferenceUtils.getString(Constant.PreferenceConstant.MAP_DURATION, PaymentDetailsActivity.this);
        map_distance = PreferenceUtils.getString(Constant.PreferenceConstant.MAP_DISTANCE, PaymentDetailsActivity.this);
        km_price = PreferenceUtils.getString(Constant.PreferenceConstant.KM_PRICE, PaymentDetailsActivity.this);
        hour = PreferenceUtils.getString(Constant.PreferenceConstant.HOUR_TYPE, PaymentDetailsActivity.this);
        wayType = getIntent().getStringExtra(Constant.BundleExtras.WAY_TYPE);
        flightType = getIntent().getStringExtra(Constant.BundleExtras.FLIGHT_TYPE);
        cabServiceType = getIntent().getStringExtra(Constant.BundleExtras.CAB_SERVICE_TYPE);
        pickAddress = getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
        pick_date = getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
        pick_time = getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
        pickLat = getIntent().getStringExtra(Constant.BundleExtras.LAT_PICK);
        pickLng = getIntent().getStringExtra(Constant.BundleExtras.LNG_PICK);
        drop_address = getIntent().getStringExtra(Constant.BundleExtras.DROP_ADDRESS);
        drop_date = getIntent().getStringExtra(Constant.BundleExtras.DROP_DATE);
        drop_time = getIntent().getStringExtra(Constant.BundleExtras.DROP_TIME);
        dropLat = getIntent().getStringExtra(Constant.BundleExtras.LAT_DROP);
        dropLng = getIntent().getStringExtra(Constant.BundleExtras.LNG_DROP);
        cTypeId = getIntent().getStringExtra(Constant.BundleExtras.C_TYPE_ID);
        carTypeName = getIntent().getStringExtra(Constant.BundleExtras.C_TYPE_NAME);
        carId = getIntent().getStringExtra(Constant.BundleExtras.CAR_ID);
        vendorId = getIntent().getStringExtra(Constant.BundleExtras.VENDOR_ID);

        totalFare = getIntent().getStringExtra(Constant.BundleExtras.PRICE);
        //        finalTotalAmt = getIntent().getStringExtra(Constant.BundleExtras.PRICE);
        finalTotalAmt = Double.parseDouble(totalFare != null ? totalFare : "0.0");
        b_name = getIntent().getStringExtra(Constant.BundleExtras.B_NAME);
        b_mobile = getIntent().getStringExtra(Constant.BundleExtras.B_MOBILE);
        b_email = getIntent().getStringExtra(Constant.BundleExtras.B_EMAIL);
        branch = getIntent().getStringExtra(Constant.BundleExtras.BRANCH_ID);

        inclink = PreferenceUtils.getString(Constant.PreferenceConstant.WEBVIEW_INC, PaymentDetailsActivity.this);
        excLink = PreferenceUtils.getString(Constant.PreferenceConstant.WEBVIEW_EXC, PaymentDetailsActivity.this);
        tcLink = PreferenceUtils.getString(Constant.PreferenceConstant.WEBVIEW_TC, PaymentDetailsActivity.this);
        airport_name = PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_NAME, PaymentDetailsActivity.this);
        initialization();
    }

    private void initialization() {
        binding.webView.setText(inclink);
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());

//        rest_price = Float.parseFloat(finalTotalAmt) / 2;
        rest_price = finalTotalAmt / 2.0;

        setCardData();

        binding.rbFullPay.setChecked(true);

        binding.rbHalfPay.setOnClickListener(v -> {
            binding.rbHalfPay.setChecked(true);
            binding.rbFullPay.setChecked(false);
        });

        binding.rbFullPay.setOnClickListener(v -> {
            binding.rbFullPay.setChecked(true);
            binding.rbHalfPay.setChecked(false);
        });

        binding.btnBook.setOnClickListener(v -> {
            binding.btnBook.setEnabled(false); // Disable button
            double amountToPay;
            if (binding.rbHalfPay.isChecked()) {
                amountToPay = rest_price;
                paid_amt = String.valueOf(rest_price);
                pay_type = "2";
            } else if (binding.rbFullPay.isChecked()) {
                amountToPay = finalTotalAmt;
                paid_amt = String.valueOf(finalTotalAmt);
                pay_type = "1";
            } else {
                Toast.makeText(getApplicationContext(), "Please select a payment option", Toast.LENGTH_SHORT).show();
                binding.btnBook.setEnabled(true);
                return;
            }
            startPaymentFlow(amountToPay);
        });

        binding.ivDown.setOnClickListener(v -> {
            binding.ivDown.setVisibility(View.GONE);
            binding.ivUp.setVisibility(View.VISIBLE);
            binding.cvCode.setVisibility(View.VISIBLE);

        });
        binding.ivUp.setOnClickListener(v -> {
            binding.ivDown.setVisibility(View.VISIBLE);
            binding.ivUp.setVisibility(View.GONE);
            binding.cvCode.setVisibility(View.GONE);

        });

        binding.btnApplyCoupon.setOnClickListener(v -> {
            if (binding.etCode.getText().toString().isEmpty()) {
                Toast.makeText(PaymentDetailsActivity.this, "Enter Coupon Code", Toast.LENGTH_SHORT).show();
            } else {
                getCouponApi();
            }
        });

        binding.btnRemoveCoupon.setOnClickListener(v -> removeCoupon());

        if ("1".equals(wayType)) {
            binding.viewB.setVisibility(View.GONE);
            binding.tvC.setVisibility(View.GONE);
            binding.tvAddress3.setVisibility(View.GONE);
        } else {
            binding.viewB.setVisibility(View.VISIBLE);
            binding.tvC.setVisibility(View.VISIBLE);
            binding.tvAddress3.setVisibility(View.VISIBLE);
        }

        binding.tvInclusions.setOnClickListener(v -> {
            binding.tvInclusions.setBackgroundColor(ContextCompat.getColor(PaymentDetailsActivity.this, R.color.primary_field));
            binding.tvExclusions.setBackgroundColor(ContextCompat.getColor(PaymentDetailsActivity.this, R.color.white));
            binding.tvTC.setBackgroundColor(ContextCompat.getColor(PaymentDetailsActivity.this, R.color.white));
            binding.webView.setText(inclink);
        });

        binding.tvExclusions.setOnClickListener(v -> {
            binding.tvExclusions.setBackgroundColor(ContextCompat.getColor(PaymentDetailsActivity.this, R.color.primary_field));
            binding.tvInclusions.setBackgroundColor(ContextCompat.getColor(PaymentDetailsActivity.this, R.color.white));
            binding.tvTC.setBackgroundColor(ContextCompat.getColor(PaymentDetailsActivity.this, R.color.white));
            binding.webView.setText(excLink);
        });
        binding.tvTC.setOnClickListener(v -> {
            binding.tvTC.setBackgroundColor(ContextCompat.getColor(PaymentDetailsActivity.this, R.color.primary_field));
            binding.tvInclusions.setBackgroundColor(ContextCompat.getColor(PaymentDetailsActivity.this, R.color.white));
            binding.tvExclusions.setBackgroundColor(ContextCompat.getColor(PaymentDetailsActivity.this, R.color.white));
            binding.webView.setText(tcLink);
        });

        if(cabServiceType.equals("1")){
            binding.viewA.setVisibility(View.GONE);
            binding.l2.setVisibility(View.GONE);
            binding.viewB.setVisibility(View.GONE);
            binding.l3.setVisibility(View.GONE);
        }
    }

    private void setCardData() {
        binding.tvAddress1.setText(pickAddress);
        binding.tvAddress2.setText(drop_address);
        binding.tvAddress3.setText(pickAddress);
        binding.tvTime1.setText(pick_time);
        binding.tvTime2.setText(drop_time);
        binding.tvPickupDateTime.setText(pick_date + " " + pick_time);
        binding.lldrop.setVisibility(View.GONE);
        if (drop_date != null && !drop_date.isEmpty()) {

            binding.tvDropDate.setText(drop_date);
            binding.lldrop.setVisibility(View.VISIBLE);

        }


        binding.tvTotalFare.setText("Rs. " + totalFare);
        binding.tvFinalAmount.setText("Rs. " + finalTotalAmt);
        binding.tvCarType.setText(carTypeName);
        binding.tvRestAmt.setText("Pay ₹" + rest_price + " now & rest during the trip.");
        binding.tvFinalAmt.setText("Pay full amount ₹" + finalTotalAmt + " now");



        switch (cabServiceType) {
            case "1":
                binding.tvTripType.setText("City Ride ("+ Utils.formatMapDuration(map_duration) + " | "+map_distance+ " Km)");
                break;
            case "2":
                binding.tvTripType.setText("One Way");
                break;
            case "3":
                binding.tvTripType.setText("Out Station");
                break;
            case "4":
                binding.tvTripType.setText("Airport");
                break;

        }

    }

    private void getCouponApi() {
        showLoader();
        String coupon = binding.etCode.getText().toString();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CouponResponse> call = apiInterface.get_coupon(coupon);
        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(@NonNull Call<CouponResponse> call, @NonNull Response<CouponResponse> response) {
                hideLoader();

                if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                    Toast.makeText(PaymentDetailsActivity.this, "Coupon Applied successfully", Toast.LENGTH_SHORT).show();

                    couponModel = response.body().getData();

                    // --- THIS IS THE CORRECT CALCULATION LOGIC ---
                    double originalTotal = Double.parseDouble(totalFare);

                    if ("2".equals(couponModel.getmCouponType())) { // Fixed amount discount
                        finalCouponAmt = Double.parseDouble(couponModel.getmCouponPeramt());
                        couponAmt = couponModel.getmCouponPeramt();
                        couponPercent = "0";
                    } else { // Percentage discount
                        couponPercent = couponModel.getmCouponPeramt();
                        double percentage = Double.parseDouble(couponModel.getmCouponPeramt());
                        finalCouponAmt = (percentage / 100) * originalTotal;
                        couponAmt = String.format(Locale.US, "%.2f", finalCouponAmt);
                    }
                    // --- END OF CALCULATION LOGIC ---

                    // After calculating, update all UI elements
                    updateFinalAmountUI();

                    // And switch the coupon view to the "Applied" state
                    updateCouponUI(true);

                } else {
                    showError("Invalid Coupon");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CouponResponse> call, @NonNull Throwable t) {
                hideLoader();
                Log.e(TAG, "getCouponApi failure", t);
                showError("Something went wrong");
            }
        });
    }

    private void removeCoupon() {
        couponModel = null;
        finalCouponAmt = 0.0;
        couponAmt = "0";
        couponPercent = "0";
        binding.etCode.setText(""); // Clear the EditText
        Toast.makeText(this, "Coupon removed", Toast.LENGTH_SHORT).show();

        // Recalculate the price and update all price-related TextViews
        updateFinalAmountUI();

        // Switch the UI back to the "Apply" state
        updateCouponUI(false);
    }

    private void updateCouponUI(boolean isApplied) {
        if (isApplied) {
            // A coupon is active: hide the input, show the "Applied" state
            binding.layoutApplyCoupon.setVisibility(View.GONE);
            binding.layoutAppliedCoupon.setVisibility(View.VISIBLE);
            String appliedText = String.format("%s APPLIED", binding.etCode.getText().toString().toUpperCase());
            binding.tvAppliedCouponCode.setText(appliedText);
            binding.llCoupon.setVisibility(View.VISIBLE);
            binding.llFinal.setVisibility(View.VISIBLE);
        } else {
            // No coupon is active: show the input, hide the "Applied" state
            binding.layoutApplyCoupon.setVisibility(View.VISIBLE);
            binding.layoutAppliedCoupon.setVisibility(View.GONE);
            binding.llCoupon.setVisibility(View.GONE);
            binding.llFinal.setVisibility(View.GONE);
        }
    }

    // Add this new method to your class
    private void updateFinalAmountUI() {
        double originalTotal = Double.parseDouble(totalFare);
        finalTotalAmt = originalTotal - finalCouponAmt;

        // Ensure final amount is not negative
        if (finalTotalAmt < 0) {
            finalTotalAmt = 0.0;
        }

        rest_price = finalTotalAmt / 2.0;

        String formattedFinalAmount = NumberUtils.formatWithCommas(finalTotalAmt);
        binding.tvFinalAmount.setText("₹ " + formattedFinalAmount);
        binding.tvTotalFare.setText("Rs. " + totalFare);
        binding.tvAppliedCoupon.setText("- ₹ " + NumberUtils.formatWithCommas(finalCouponAmt));
        binding.tvRestAmt.setText("Pay ₹" + NumberUtils.formatWithCommas(rest_price) + " now & rest during the trip.");
        binding.tvFinalAmt.setText("Pay full amount ₹" + formattedFinalAmount + " now");
    }


    private void insertBookingApi(String trans_id) {
        showLoader();

        if (pick_date != null && !pick_date.isEmpty()) {
            pick_date2 = DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, pick_date);
        }

        if (drop_date != null && !drop_date.isEmpty()) {
            drop_date2 = DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, drop_date);
        }

        if (pick_time != null && !pick_time.isEmpty()) {
            pickTimeIn24Hour = DateFormater.formatTo24Hour(pick_time);
        }
        if (drop_time != null && !drop_time.isEmpty()) {
            returnTimeIn24Hour = DateFormater.formatTo24Hour(drop_time);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


//        final double total = Double.parseDouble(finalTotalAmt);
        final double total = finalTotalAmt;
        final double paid = Double.parseDouble(paid_amt);
        final String remaining = String.valueOf(total - paid);

        String couponId = "";
        if (couponModel != null) {
            couponId = couponModel.getmCouponId();
        }


        Call<BookingResponse> call;

        switch (cabServiceType) {
            case "1":
                call = apiInterface.insert_city_ride_booking("1", branch, userId, cTypeId, hour, pickAddress, pick_date2, pickTimeIn24Hour, km_price, map_distance, String.valueOf(finalTotalAmt), pay_type, paid_amt, remaining, trans_id, "1", couponId, couponPercent, couponAmt,pickLat,pickLng,dropLat,dropLng);
                break;
            case "2":
                call = apiInterface.insert_one_way_booking("2", branch, userId, cTypeId, pickAddress, pickAddress, drop_address, pick_date2, pickTimeIn24Hour, km_price, map_distance, String.valueOf(finalTotalAmt), pay_type, paid_amt, remaining, trans_id, "1", couponId, couponPercent, couponAmt,pickLat,pickLng,dropLat,dropLng);
                break;
            case "3":
                call = apiInterface.insert_outstation_booking("3", branch, userId, cTypeId, pickAddress, drop_address, pick_date2, pickTimeIn24Hour, drop_date2, returnTimeIn24Hour, km_price, map_distance, String.valueOf(finalTotalAmt), pay_type, paid_amt, remaining, trans_id, "1", couponId, couponPercent, couponAmt,pickLat,pickLng,dropLat,dropLng);
                break;
            case "4":
                call = apiInterface.insert_airport_booking("4", branch, userId, cTypeId, pickAddress, drop_address, pick_date2, pickTimeIn24Hour, km_price, map_distance, String.valueOf(finalTotalAmt), pay_type, paid_amt, remaining, trans_id, "1", couponId, couponPercent, couponAmt,flightType,pickLat,pickLng,dropLat,dropLng);
                break;
            default:
                call = apiInterface.insert_city_ride_booking("1", branch, userId, cTypeId, hour, pickAddress, pick_date2, pickTimeIn24Hour, km_price, map_distance, String.valueOf(finalTotalAmt), pay_type, paid_amt, remaining, trans_id, "1", couponId, couponPercent, couponAmt,pickLat,pickLng,dropLat,dropLng);
                break;
        }

        call.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            bookingModelList.addAll(response.body().getData());
                            showSuccessDialog("Updating Booking Status", PaymentDetailsActivity.this);

                        } else {
                            hideLoader();
                            showError("Your car has not been booked!");

                        }
                    } else {
                        hideLoader();
                        showError("Your car has not been booked!");

                    }
                } catch (Exception e) {
                    hideLoader();
                    e.printStackTrace();
                    showError("Your car has not been booked!");

                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    public void showSuccessDialog(String message, Context context) {
        dialog = new Dialog(PaymentDetailsActivity.this, R.style.my_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.show();

        TextView textView = dialog.findViewById(R.id.tv_message);
        textView.setText(message);

        new CountDownTimer(2500, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                dialog.cancel();
                Intent intent = new Intent(PaymentDetailsActivity.this, BookingConfirmedActivity.class);
                intent.putExtra(Constant.BundleExtras.WAY_TYPE, wayType);
                intent.putExtra(Constant.BundleExtras.CAB_SERVICE_TYPE, cabServiceType);
                intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
                intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
                intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
                intent.putExtra(Constant.BundleExtras.DROP_ADDRESS, drop_address);
                intent.putExtra(Constant.BundleExtras.DROP_DATE, drop_date);
                intent.putExtra(Constant.BundleExtras.DROP_TIME, drop_time);
                intent.putExtra(Constant.BundleExtras.C_TYPE_NAME, carTypeName);
                intent.putExtra(Constant.BundleExtras.PRICE, paid_amt);
                intent.putExtra(Constant.BundleExtras.BOOKING_ID, bookingModelList.get(0).getmBookingId());
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void setMessageForSnackbar(String msg, boolean flag) {
        if (flag) {
            Snacky.builder()
                    .setActivity(PaymentDetailsActivity.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {
                        //do something
                    })
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .success()
                    .show();
        } else {
            Snacky.builder()
                    .setActivity(PaymentDetailsActivity.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {
                        //do something
                    })
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .error()
                    .show();
        }

    }



    // --- NEW RAZORPAY FLOW METHODS ---

    private void startPaymentFlow(final double amount) {
        ProgressDialog pdLoading = new ProgressDialog(this);
        pdLoading.setMessage("Initializing Payment...");
        pdLoading.setCancelable(false);
        pdLoading.show();

//        RazorPayApiClient.getRazorpayClient().create(ApiInterface.class)
//                .createRazorpayOrder(String.valueOf(amount))
//                .enqueue(new Callback<CreateOrderResponse>() {
//                    @Override
//                    public void onResponse(@NonNull Call<CreateOrderResponse> call, @NonNull Response<CreateOrderResponse> response) {
//                        pdLoading.dismiss();
//                        if (response.isSuccessful() && response.body() != null && "success".equalsIgnoreCase(response.body().getResponseStatus())) {
//                            String razorpayOrderId = response.body().getOrderId();
//                            if (razorpayOrderId != null && !razorpayOrderId.isEmpty()) {
//                                startRazorpayCheckout(amount, razorpayOrderId);
//                            } else {
//                                showError("Server did not provide an Order ID.");
//                                binding.btnBook.setEnabled(true);
//                            }
//                        } else {
//                            showError("Payment initialization failed on server.");
//                            binding.btnBook.setEnabled(true);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<CreateOrderResponse> call, @NonNull Throwable t) {
//                        pdLoading.dismiss();
//                        Log.e("PaymentDetailsActivity", "createRazorpayOrder API call failed", t);
//                        showError("An error occurred. Please check your connection.");
//                        binding.btnBook.setEnabled(true);
//                    }
//                });


        RazorPayApiClient.getRazorpayClient().create(ApiInterface.class)
                .createRazorpayOrderTest(String.valueOf(amount))
                .enqueue(new Callback<CreateOrderResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CreateOrderResponse> call, @NonNull Response<CreateOrderResponse> response) {
                        pdLoading.dismiss();
                        if (response.isSuccessful() && response.body() != null && "success".equalsIgnoreCase(response.body().getResponseStatus())) {
                            String razorpayOrderId = response.body().getOrderId();
                            if (razorpayOrderId != null && !razorpayOrderId.isEmpty()) {
                                startRazorpayCheckout(amount, razorpayOrderId);
                            } else {
                                showError("Server did not provide an Order ID.");
                                binding.btnBook.setEnabled(true);
                            }
                        } else {
                            showError("Payment initialization failed on server.");
                            binding.btnBook.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CreateOrderResponse> call, @NonNull Throwable t) {
                        pdLoading.dismiss();
                        Log.e("PaymentDetailsActivity", "createRazorpayOrder API call failed", t);
                        showError("An error occurred. Please check your connection.");
                        binding.btnBook.setEnabled(true);
                    }
                });
    }

    private void startRazorpayCheckout(double amount, String razorpayOrderId) {
        Checkout checkout = new Checkout();
//        checkout.setKeyID(getString(R.string.razor_pay_key));
        checkout.setKeyID(getString(R.string.razor_pay_key_test));
        checkout.setImage(R.mipmap.ic_launcher);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Booking Reference No. #" + System.currentTimeMillis());
            options.put("order_id", razorpayOrderId);
            options.put("currency", "INR");
            options.put("amount", (double) (amount * 100)); // Amount in paise

            JSONObject prefill = new JSONObject();
            prefill.put("email", loginModel.getmCustEmail());
            prefill.put("contact", loginModel.getmCustMobile());
            options.put("prefill", prefill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("PaymentDetailsActivity", "Error in starting Razorpay Checkout", e);
            showError("Error launching payment screen.");
            binding.btnBook.setEnabled(true);
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentId) {
        insertBookingApi(razorpayPaymentId);
        Checkout.clearUserData(this);
    }

    @Override
    public void onPaymentError(int code, String description) {
        Log.e("PaymentDetailsActivity", "Payment Failed: " + code + " " + description);
        setMessageForSnackbar("Payment Failed: " + description, false);
        binding.btnBook.setEnabled(true);
        Checkout.clearUserData(this);
    }
}
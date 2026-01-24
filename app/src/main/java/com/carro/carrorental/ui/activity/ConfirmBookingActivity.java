//package com.logixhunt.carrorental.ui.activity;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.bumptech.glide.Glide;
//import com.google.gson.Gson;
//import com.logixhunt.carrorental.R;
//import com.logixhunt.carrorental.api.ApiClient;
//import com.logixhunt.carrorental.api.ApiInterface;
//import com.logixhunt.carrorental.api.RazorPayApiClient;
//import com.logixhunt.carrorental.api.response.BookingResponse;
//import com.logixhunt.carrorental.api.response.CreateOrderResponse;
//import com.logixhunt.carrorental.databinding.ActivityConfirmBookingBinding;
//import com.logixhunt.carrorental.model.BookingModel;
//import com.logixhunt.carrorental.model.CarTypeModel;
//import com.logixhunt.carrorental.model.LoginModel;
//import com.logixhunt.carrorental.ui.common.BaseActivity;
//import com.logixhunt.carrorental.utils.Constant;
//import com.logixhunt.carrorental.utils.DateFormater;
//import com.logixhunt.carrorental.utils.ImagePathDecider;
//import com.logixhunt.carrorental.utils.PreferenceUtils;
//import com.razorpay.Checkout;
//import com.razorpay.Order;
//import com.razorpay.PaymentResultListener;
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.mateware.snacky.Snacky;
//import pl.droidsonroids.gif.GifImageView;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ConfirmBookingActivity extends BaseActivity implements PaymentResultListener{
//
//    ActivityConfirmBookingBinding binding;
//    LoginModel loginModel = new LoginModel();
//    String price;
//    CarTypeModel carTypeModel = new CarTypeModel();
//    List<BookingModel> bookingModelList = new ArrayList<>();
//    String pick_date = "";
//    String pick_date2 = "";
//    String pick_time = "";
//    String pickAddress = "";
//    String return_date = "";
//    String return_date2 = "";
//    String return_time = "";
//    String branch_id = "";
//    String package_id = "";
//    String in_out_side = "";
//    Dialog dialog;
//    String screen_type;
//    String bking_type;
//    String pickTimeIn24Hour;
//    String returnTimeIn24Hour;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding=ActivityConfirmBookingBinding.inflate(getLayoutInflater());
//        EdgeToEdge.enable(this);
//        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        getUserPreferences();
//    }
//    private void getUserPreferences() {
//        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, ConfirmBookingActivity.this);
//        loginModel = new Gson().fromJson(userData, LoginModel.class);
//        price=getIntent().getStringExtra(Constant.BundleExtras.PRICE);
//        carTypeModel = new Gson().fromJson(getIntent().getStringExtra(Constant.BundleExtras.CAR_DATA), CarTypeModel.class);
//        branch_id=PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_ID,ConfirmBookingActivity.this);
//        package_id=PreferenceUtils.getString(Constant.PreferenceConstant.PACKAGE_ID,ConfirmBookingActivity.this);
//        pick_date=getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
//        pick_time=getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
//        pickAddress=getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
//        return_date=getIntent().getStringExtra(Constant.BundleExtras.DROP_DATE);
//        return_time=getIntent().getStringExtra(Constant.BundleExtras.DROP_TIME);
//        screen_type=getIntent().getStringExtra(Constant.BundleExtras.SCREEN_TYPE);
//        in_out_side=getIntent().getStringExtra(Constant.BundleExtras.IN_OUT_SIDE);
//        initialization();
//    }
//
//    private void initialization() {
//        binding.ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getOnBackPressedDispatcher().onBackPressed();
//            }
//        });
//
//        Glide.with(ConfirmBookingActivity.this)
//                .load(ImagePathDecider.getUserImagePath() + loginModel.getmCustImg())
//                .error(R.drawable.img_no_profile)
//                .into(binding.ivUser);
//
//        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                double rupeeToPaisa = Integer.parseInt(price) * 100;
//                AsyncCaller asyncCaller = new AsyncCaller(rupeeToPaisa);// Money is being added in Paisa....
//                asyncCaller.execute();
//            }
//        });
//
//        if(screen_type.equals("luxury")){
//            bking_type="3";
//        }else {
//            bking_type="2";
//        }
//    }
//
//    //todo : Razorpay
//
//    class AsyncCaller extends AsyncTask<Void, Void, Void> {
//        double payAmount;
//
//        AsyncCaller(double payAmount) {
//            this.payAmount = payAmount;
//        }
//
//        ProgressDialog pdLoading = new ProgressDialog(ConfirmBookingActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading...");
//            pdLoading.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            //this method will be running on background thread so don't update UI frome here
//            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
//            try {
//                RazorpayClient razorpay = new RazorpayClient(getString(R.string.razor_pay_key), getString(R.string.razor_pay_secret));
//                try {
//                    JSONObject orderRequest = new JSONObject();
//                    orderRequest.put("amount", payAmount); // amount in the smallest currency unit
//                    orderRequest.put("currency", "INR");
//                    orderRequest.put("receipt", "order_rcptid_11");
//                    orderRequest.put("payment_capture", true);
//
//                    Order order = razorpay.Orders.create(orderRequest);
//                    Log.e("Order Id ", "" + order.get("id"));
//                    startPayment(payAmount, "" + order.get("id"));
//                } catch (RazorpayException e) {
//                    // Handle Exception
//                    System.out.println(e.getMessage());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            //this method will be running on UI thread
//            pdLoading.dismiss();
//        }
//
//    }
//
//    private void startPayment(double netPrice, String razorPayOrderId) {
//
//        Checkout checkout = new Checkout();
//        checkout.setKeyID(getString(R.string.razor_pay_key));
//        checkout.setImage(R.mipmap.ic_launcher);
//        final Activity activity = ConfirmBookingActivity.this;
//        try {
//            JSONObject options = new JSONObject();
//            options.put("name", getResources().getString(R.string.app_name));
//            options.put("description", "Reference No. #" + System.currentTimeMillis());
//            options.put("order_id", razorPayOrderId);
//            options.put("currency", "INR");
//            options.put("amount", netPrice);
////            options.put("prefill.email", userModel.getmCustEmail());
////            options.put("prefill.contact", userModel.getmCustMobile());
//            checkout.open(activity, options);
//        } catch (Exception e) {
//            Log.e("TaG", "Error in starting Razorpay Checkout", e);
//        }
//    }
//
//    @Override
//    public void onPaymentSuccess(String s) {
//        //TODO call API for payment
////        insertSubscriptionPlan(s);
//        insertSelfBookingApi(s);
//        Checkout.clearUserData(ConfirmBookingActivity.this);
//    }
//
//    @Override
//    public void onPaymentError(int i, String s) {
//        setMessageForSnackbar("Payment Failed", false);
//        Checkout.clearUserData(ConfirmBookingActivity.this);
//
//      /*  Toast.makeText(getContext(), "Payment Failed", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(CheckoutActivity.this, OrderSuccessActivity.class);
//        intent.putExtra(Constant.BundleExtras.PAYMENT_STATUS, "2");
//        startActivity(intent);*/
//    }
//
//    private void setMessageForSnackbar(String msg, boolean flag) {
//        if (flag) {
//            Snacky.builder()
//                    .setActivity(ConfirmBookingActivity.this)
//                    .setActionText("Ok")
//                    .setActionClickListener(v -> {
//                        //do something
//                    })
//                    .setText(msg)
//                    .setDuration(Snacky.LENGTH_INDEFINITE)
//                    .success()
//                    .show();
//        } else {
//            Snacky.builder()
//                    .setActivity(ConfirmBookingActivity.this)
//                    .setActionText("Ok")
//                    .setActionClickListener(v -> {
//                        //do something
//                    })
//                    .setText(msg)
//                    .setDuration(Snacky.LENGTH_INDEFINITE)
//                    .error()
//                    .show();
//        }
//    }
//
//    private void insertSelfBookingApi(String trans_id) {
//        showLoader();
//
//        if (pick_date!=null&&!pick_date.isEmpty()) {
//            pick_date2= DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd,pick_date);
//        }
//
//        if (return_date!=null&&!return_date.isEmpty()){
//            return_date2= DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd,return_date);
//        }
//        if (pick_time!=null&&!pick_time.isEmpty()){
//             pickTimeIn24Hour = DateFormater.formatTo24Hour(pick_time);
//        }
//        if (return_time!=null&&!return_time.isEmpty()){
//            returnTimeIn24Hour = DateFormater.formatTo24Hour(return_time);
//        }
//
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<BookingResponse> call = apiInterface.insert_selfdrive_booking(pick_date2,pickTimeIn24Hour,return_date2,
//                returnTimeIn24Hour,loginModel.getmCustId(),carTypeModel.getmCtypeId(), price,"",price,"1",
//                "1","1",loginModel.getmCustName(),loginModel.getmCustMobile(),
//                loginModel.getmCustEmail(),branch_id,package_id,trans_id,in_out_side);
//        call.enqueue(new Callback<BookingResponse>() {
//            @Override
//            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
//                hideLoader();
//                try {
//                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
//                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
//                            bookingModelList.addAll(response.body().getData());
//                            showSuccessDialog("Booking Confirmed Successfully....!",ConfirmBookingActivity.this);
//
//                        } else {
//                            hideLoader();
//                            showError("Your car has not been booked!");
//
//                        }
//                    } else {
//                        hideLoader();
//                        showError("Your car has not been booked!");
//
//                    }
//                } catch (Exception e) {
//                    hideLoader();
//                    e.printStackTrace();
//                    showError("Your car has not been booked!");
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BookingResponse> call, Throwable t) {
//                hideLoader();
//                Log.e("Failure", t.toString());
//                showError("Something went wrong");
//            }
//        });
//
//
//    }
//    public void showSuccessDialog(String message, Context context) {
//        dialog = new Dialog(ConfirmBookingActivity.this, R.style.my_dialog);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.loading_dialog);
//        dialog.show();
//
//        TextView textView = dialog.findViewById(R.id.tv_message);
//        textView.setText(message);
//
//        GifImageView gifImageView=dialog.findViewById(R.id.gifImageView);
//        gifImageView.setImageResource(R.drawable.gif_booking_confirmation);
//
//        new CountDownTimer(2500, 100) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//            }
//
//            @Override
//            public void onFinish() {
//                dialog.cancel();
//                Intent intent=new Intent(ConfirmBookingActivity.this, MyBookingsActivity.class);
//                startActivity(intent);
//                finishAffinity();
//            }
//        }.start();
//
//    }
//
//
//
//
//
//}


package com.carro.carrorental.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.RazorPayApiClient;
import com.carro.carrorental.api.response.BookingResponse;
import com.carro.carrorental.api.response.CreateOrderResponse;
import com.carro.carrorental.databinding.ActivityConfirmBookingBinding;
import com.carro.carrorental.model.BookingModel;
import com.carro.carrorental.model.CarTypeModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.PreferenceUtils;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.mateware.snacky.Snacky;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmBookingActivity extends BaseActivity implements PaymentResultListener {

    ActivityConfirmBookingBinding binding;
    LoginModel loginModel = new LoginModel();
    String price;
    CarTypeModel carTypeModel = new CarTypeModel();
    List<BookingModel> bookingModelList = new ArrayList<>();
    String pick_date = "";
    String pick_date2 = "";
    String pick_time = "";
    String pickAddress = "";
    String return_date = "";
    String return_date2 = "";
    String return_time = "";
    String branch_id = "";
    String package_id = "";
    String in_out_side = "";
    Dialog dialog;
    String screen_type;
    String bking_type;
    String pickTimeIn24Hour;
    String returnTimeIn24Hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBookingBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getUserPreferences();
    }

    private void getUserPreferences() {
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, ConfirmBookingActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        price = getIntent().getStringExtra(Constant.BundleExtras.PRICE);
        carTypeModel = new Gson().fromJson(getIntent().getStringExtra(Constant.BundleExtras.CAR_DATA), CarTypeModel.class);
        branch_id = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_ID, ConfirmBookingActivity.this);
        package_id = PreferenceUtils.getString(Constant.PreferenceConstant.PACKAGE_ID, ConfirmBookingActivity.this);
        pick_date = getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
        pick_time = getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
        pickAddress = getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
        return_date = getIntent().getStringExtra(Constant.BundleExtras.DROP_DATE);
        return_time = getIntent().getStringExtra(Constant.BundleExtras.DROP_TIME);
        screen_type = getIntent().getStringExtra(Constant.BundleExtras.SCREEN_TYPE);
        in_out_side = getIntent().getStringExtra(Constant.BundleExtras.IN_OUT_SIDE);
        initialization();
    }

    private void initialization() {
        setUpToolBar(binding.toolbar,this,loginModel.getmCustImg());

        binding.btnContinue.setOnClickListener(v -> {
            binding.btnContinue.setEnabled(false); // Disable button to prevent multiple clicks
            if (price != null && !price.isEmpty()) {
                startPaymentFlow(Double.parseDouble(price));
            } else {
                showError("Invalid amount.");
                binding.btnContinue.setEnabled(true);
            }
        });

        if ("luxury".equals(screen_type)) {
            bking_type = "3";
        } else {
            bking_type = "2";
        }
    }

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
//                                binding.btnContinue.setEnabled(true);
//                            }
//                        } else {
//                            showError("Payment initialization failed on server.");
//                            binding.btnContinue.setEnabled(true);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<CreateOrderResponse> call, @NonNull Throwable t) {
//                        pdLoading.dismiss();
//                        Log.e("ConfirmBookingActivity", "createRazorpayOrder API call failed", t);
//                        showError("An error occurred. Please check your connection.");
//                        binding.btnContinue.setEnabled(true);
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
                                binding.btnContinue.setEnabled(true);
                            }
                        } else {
                            showError("Payment initialization failed on server.");
                            binding.btnContinue.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CreateOrderResponse> call, @NonNull Throwable t) {
                        pdLoading.dismiss();
                        Log.e("ConfirmBookingActivity", "createRazorpayOrder API call failed", t);
                        showError("An error occurred. Please check your connection.");
                        binding.btnContinue.setEnabled(true);
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
            options.put("description", "Booking for " + carTypeModel.getmCtypeTitle());
            options.put("order_id", razorpayOrderId);
            options.put("currency", "INR");
            options.put("amount", (int) (amount * 100)); // Amount in paise

            JSONObject prefill = new JSONObject();
            prefill.put("email", loginModel.getmCustEmail());
            prefill.put("contact", loginModel.getmCustMobile());
            options.put("prefill", prefill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("ConfirmBookingActivity", "Error in starting Razorpay Checkout", e);
            showError("Error launching payment screen.");
            binding.btnContinue.setEnabled(true);
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentId) {
        Checkout.clearUserData(this);
        insertSelfBookingApi(razorpayPaymentId);
    }

    @Override
    public void onPaymentError(int code, String description) {
        Log.e("ConfirmBookingActivity", "Payment Failed: " + code + " " + description);
        setMessageForSnackbar("Payment Failed: " + description, false);
        binding.btnContinue.setEnabled(true);
        Checkout.clearUserData(this);
    }

    private void setMessageForSnackbar(String msg, boolean flag) {
        if (flag) {
            Snacky.builder()
                    .setActivity(ConfirmBookingActivity.this)
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
                    .setActivity(ConfirmBookingActivity.this)
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

    private void insertSelfBookingApi(String trans_id) {
        showLoader();

        if (pick_date!=null&&!pick_date.isEmpty()) {
            pick_date2= DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd,pick_date);
        }

        if (return_date!=null&&!return_date.isEmpty()){
            return_date2= DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd,return_date);
        }
        if (pick_time!=null&&!pick_time.isEmpty()){
            pickTimeIn24Hour = DateFormater.formatTo24Hour(pick_time);
        }
        if (return_time!=null&&!return_time.isEmpty()){
            returnTimeIn24Hour = DateFormater.formatTo24Hour(return_time);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BookingResponse> call = apiInterface.insert_selfdrive_booking(pick_date2,pickTimeIn24Hour,return_date2,
                returnTimeIn24Hour,loginModel.getmCustId(),carTypeModel.getmCtypeId(), price,"",price,"1",
                "1","1",loginModel.getmCustName(),loginModel.getmCustMobile(),
                loginModel.getmCustEmail(),branch_id,package_id,trans_id,in_out_side);
        call.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            bookingModelList.addAll(response.body().getData());
                            showSuccessDialog("Booking Confirmed Successfully....!",ConfirmBookingActivity.this);

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
        dialog = new Dialog(ConfirmBookingActivity.this, R.style.my_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.show();

        TextView textView = dialog.findViewById(R.id.tv_message);
        textView.setText(message);

        GifImageView gifImageView=dialog.findViewById(R.id.gifImageView);
        gifImageView.setImageResource(R.drawable.gif_booking_confirmation);

        new CountDownTimer(2500, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                dialog.cancel();
                Intent intent=new Intent(ConfirmBookingActivity.this, MyBookingsActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        }.start();

    }
}
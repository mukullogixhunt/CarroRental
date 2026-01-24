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
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.bumptech.glide.Glide;
//import com.google.gson.Gson;
//import com.logixhunt.carrorental.R;
//import com.logixhunt.carrorental.api.ApiClient;
//import com.logixhunt.carrorental.api.ApiInterface;
//import com.logixhunt.carrorental.api.response.BookingResponse;
//import com.logixhunt.carrorental.databinding.ActivityBusDetailBinding;
//import com.logixhunt.carrorental.model.BookingModel;
//import com.logixhunt.carrorental.model.BusModel;
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
//public class BusDetailActivity extends BaseActivity implements PaymentResultListener {
//
//    ActivityBusDetailBinding binding;
//    BusModel busModel = new BusModel();
//    List<BookingModel> bookingModelList = new ArrayList<>();
//    LoginModel loginModel = new LoginModel();
//    String pick_date = "";
//    String pick_time = "";
//    String pick_date2 = "";
//    String return_date2 = "";
//    String address = "";
//    String branch = "";
//    String branch_name = "";
//    String bus_id = "";
//    Dialog dialog;
//    String return_date = "";
//    String return_time = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityBusDetailBinding.inflate(getLayoutInflater());
//        EdgeToEdge.enable(this);
//        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        getPreference();
//    }
//
//    private void getPreference() {
//        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, BusDetailActivity.this);
//        loginModel = new Gson().fromJson(userData, LoginModel.class);
//        busModel = new Gson().fromJson(getIntent().getStringExtra(Constant.BundleExtras.BUS_DATA), BusModel.class);
//        address = getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
//        pick_date = getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
//        pick_time = getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
//        branch = getIntent().getStringExtra(Constant.BundleExtras.BRANCH_ID);
//        branch_name = getIntent().getStringExtra(Constant.BundleExtras.BRANCH_NAME);
//        return_date = getIntent().getStringExtra(Constant.BundleExtras.RETURN_DATE);
//        return_time = getIntent().getStringExtra(Constant.BundleExtras.RETURN_TIME);
//        initialization();
//    }
//
//    private void initialization() {
//        setData();
//        binding.ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getOnBackPressedDispatcher().onBackPressed();
//            }
//        });
//
//        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(binding.checkbox.isChecked()){
//                    insertBusBookingApi();
//                }else {
//                    setMessageForSnackbar("Please accept terms and conditions",false);
//                }
//
////                double rupeeToPaisa = Integer.parseInt(busModel.getmBusPrice()) * 100;
////                AsyncCaller asyncCaller = new AsyncCaller(rupeeToPaisa);// Money is being added in Paisa....
////                asyncCaller.execute();
//            }
//        });
//    }
//
//    private void setData() {
//        bus_id = busModel.getmBusId();
//        binding.tvSeat.setText(busModel.getmBusTitle());
//        binding.tvPrice.setText("Rs. " + busModel.getmBusPrice() + " (Without Fuel)");
//        binding.tvTotalFare.setText("Rs. " + busModel.getmBusPrice() + " (Without Fuel)");
//        binding.tvTravelDateTime.setText(pick_date + " " + pick_time);
//        binding.tvPickupDateTime.setText(pick_date + " " + pick_time);
//        binding.tvTime1.setText(pick_time);
//        binding.tvBranch.setText(branch_name);
//        binding.tvAddress.setText(branch_name);
////        binding.tvPickupLocation.setText(address);
//        Glide.with(BusDetailActivity.this)
//                .load(ImagePathDecider.getUserImagePath() + loginModel.getmCustImg())
//                .error(R.drawable.img_no_profile)
//                .into(binding.ivUser);
//
//
//        Glide.with(BusDetailActivity.this)
//                .load(ImagePathDecider.getBusImagePath() + busModel.getmBusImg())
//                .error(R.drawable.img_location_bus)
//                .into(binding.ivCar);
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
//        ProgressDialog pdLoading = new ProgressDialog(BusDetailActivity.this);
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
//        final Activity activity = BusDetailActivity.this;
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
//        insertBusBookingApi();
//        Checkout.clearUserData(BusDetailActivity.this);
//    }
//
//    @Override
//    public void onPaymentError(int i, String s) {
//        setMessageForSnackbar("Payment Failed", false);
//        Checkout.clearUserData(BusDetailActivity.this);
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
//                    .setActivity(BusDetailActivity.this)
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
//                    .setActivity(BusDetailActivity.this)
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
//
//    private void insertBusBookingApi() {
//        showLoader();
//
//        if (pick_date!=null&&!pick_date.isEmpty()) {
//            pick_date2= DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd,pick_date);
//        }
//
//        if (return_date!=null&&!return_date.isEmpty()) {
//            return_date2= DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd,return_date);
//        }
//        String pickTime = DateFormater.formatTo24Hour(pick_time);
//        String returnTime = DateFormater.formatTo24Hour(return_time);
//
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<BookingResponse> call = apiInterface.insert_bus_booking(pick_date2,pickTime,return_date2,returnTime,loginModel.getmCustId(),bus_id, busModel.getmBusPrice(),"",busModel.getmBusPrice(),"1",
//                "0","1",loginModel.getmCustName(),loginModel.getmCustMobile(),loginModel.getmCustEmail(),branch);
//        call.enqueue(new Callback<BookingResponse>() {
//            @Override
//            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
//                hideLoader();
//                try {
//                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
//                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
//                            bookingModelList.addAll(response.body().getData());
//                            showSuccessDialog("Booking Registered Successfully...!",BusDetailActivity.this);
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
//
//    public void showSuccessDialog(String message, Context context) {
//        dialog = new Dialog(BusDetailActivity.this, R.style.my_dialog);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.loading_dialog);
//        dialog.show();
//
//        TextView textView = dialog.findViewById(R.id.tv_message);
//        textView.setText(message);
//
//        GifImageView gifImageView = dialog.findViewById(R.id.gifImageView);
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
//                Intent intent = new Intent(BusDetailActivity.this, MyBookingsActivity.class);
//                startActivity(intent);
//                finishAffinity();
//            }
//        }.start();
//
//    }
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.RazorPayApiClient;
import com.carro.carrorental.api.response.BookingResponse;
import com.carro.carrorental.api.response.CreateOrderResponse;
import com.carro.carrorental.databinding.ActivityBusDetailBinding;
import com.carro.carrorental.model.BookingModel;
import com.carro.carrorental.model.BusModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.ImagePathDecider;
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

public class BusDetailActivity extends BaseActivity implements PaymentResultListener {

    private static final String TAG = "BusDetailActivity";
    ActivityBusDetailBinding binding;
    BusModel busModel = new BusModel();
    List<BookingModel> bookingModelList = new ArrayList<>();
    LoginModel loginModel = new LoginModel();
    String pick_date = "";
    String pick_time = "";
    String pick_date2 = "";
    String return_date2 = "";
    String address = "";
    String branch = "";
    String branch_name = "";
    String bus_id = "";
    Dialog dialog;
    String return_date = "";
    String return_time = "";

    private boolean isPaymentProcessStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusDetailBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Checkout.preload(getApplicationContext());
        getPreference();
    }

    private void getPreference() {
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, BusDetailActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        busModel = new Gson().fromJson(getIntent().getStringExtra(Constant.BundleExtras.BUS_DATA), BusModel.class);
        address = getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
        pick_date = getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
        pick_time = getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
        branch = getIntent().getStringExtra(Constant.BundleExtras.BRANCH_ID);
        branch_name = getIntent().getStringExtra(Constant.BundleExtras.BRANCH_NAME);
        return_date = getIntent().getStringExtra(Constant.BundleExtras.RETURN_DATE);
        return_time = getIntent().getStringExtra(Constant.BundleExtras.RETURN_TIME);
        initialization();
    }

    private void initialization() {
        setData();
//        binding.ivBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        setUpToolBar(binding.toolbar,this,loginModel.getmCustImg());
        binding.btnContinue.setOnClickListener(v -> {
            if (!binding.checkbox.isChecked()) {
                setMessageForSnackbar("Please accept terms and conditions", false);
                return;
            }
            binding.btnContinue.setEnabled(false);
            if (busModel.getmCtypePrice() != null && !busModel.getmCtypePrice().isEmpty()) {
                isPaymentProcessStarted = true;
                startPaymentFlow(Double.parseDouble(busModel.getmCtypePrice()));
            } else {
                showError("Invalid amount for bus booking.");
                binding.btnContinue.setEnabled(true);
            }
        });

        binding.tvDetailedTc.setOnClickListener(v -> {
            Constant.WEBVIEW_TITLE = getString(R.string.terms_condition);
            Constant.WEBVIEW_URL = getString(R.string.terms_and_condition_url);
            startActivity(new Intent(this, WebViewActivity.class));
        });
    }

    private void setData() {
        bus_id = busModel.getmCtypeId();
        binding.tvSeat.setText(busModel.getmCtypeTitle());
        binding.tvPrice.setText("Rs. " + busModel.getmCtypePrice() + " (Without Fuel)");
        binding.tvTotalFare.setText("Rs. " + busModel.getmCtypePrice() + " (Without Fuel)");
        binding.tvTravelDateTime.setText(pick_date + " " + pick_time);
        binding.tvPickupDateTime.setText(pick_date + " " + pick_time);
        binding.tvTime1.setText(pick_time);
        binding.tvBranch.setText(branch_name);
        binding.tvAddress.setText(branch_name);


        Glide.with(BusDetailActivity.this)
                .load(ImagePathDecider.getCarImagePath() + busModel.getmCtypeImg())
                .error(R.drawable.img_location_bus)
                .into(binding.ivCar);
    }

    private void startPaymentFlow(final double amount) {
        ProgressDialog pdLoading = new ProgressDialog(this);
        pdLoading.setMessage("Initializing Payment...");
        pdLoading.setCancelable(false);
        pdLoading.show();





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
                                isPaymentProcessStarted = false;
                                showError("Server did not provide an Order ID.");
                                binding.btnContinue.setEnabled(true);
                            }
                        } else {
                            isPaymentProcessStarted = false;
                            showError("Payment initialization failed on server.");
                            binding.btnContinue.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CreateOrderResponse> call, @NonNull Throwable t) {
                        pdLoading.dismiss();
                        isPaymentProcessStarted = false;
                        Log.e(TAG, "createRazorpayOrder API call failed", t);
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
            options.put("description", "Payment for Bus Booking: " + busModel.getmCtypeTitle());
            options.put("order_id", razorpayOrderId);
            options.put("currency", "INR");
            options.put("amount", (int) (amount * 100)); // Amount in paise

            JSONObject prefill = new JSONObject();
            prefill.put("email", loginModel.getmCustEmail());
            prefill.put("contact", loginModel.getmCustMobile());
            options.put("prefill", prefill);

            checkout.open(activity, options);
        } catch (Exception e) {
            isPaymentProcessStarted = false;
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
            showError("Error launching payment screen.");
            binding.btnContinue.setEnabled(true);
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentId) {
        if (!isPaymentProcessStarted) {
            Log.w(TAG, "Ignoring a stray onPaymentSuccess callback.");
            return;
        }
        isPaymentProcessStarted = false;
        insertBusBookingApi(razorpayPaymentId);
    }

    @Override
    public void onPaymentError(int code, String description) {
        if (!isPaymentProcessStarted) {
            Log.w(TAG, "Ignoring a stray onPaymentError callback.");
            return;
        }
        isPaymentProcessStarted = false;
        Log.e(TAG, "Payment Failed: " + code + " " + description);
        setMessageForSnackbar("Payment Failed: " + description, false);
        binding.btnContinue.setEnabled(true);
    }

    private void setMessageForSnackbar(String msg, boolean flag) {
        if (flag) {
            Snacky.builder()
                    .setActivity(BusDetailActivity.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {})
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .success()
                    .show();
        } else {
            Snacky.builder()
                    .setActivity(BusDetailActivity.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {})
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .error()
                    .show();
        }
    }

    private void insertBusBookingApi(String transId) {
        showLoader();

        if (pick_date != null && !pick_date.isEmpty()) {
            pick_date2 = DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, pick_date);
        }
        if (return_date != null && !return_date.isEmpty()) {
            return_date2 = DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, return_date);
        }
        String pickTime = DateFormater.formatTo24Hour(pick_time);
        String returnTime = DateFormater.formatTo24Hour(return_time);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Corrected API call for online payment
        Call<BookingResponse> call = apiInterface.insert_bus_booking(
                pick_date2,
                pickTime,
                return_date2,
                returnTime,
                loginModel.getmCustId(),
                bus_id,
                busModel.getmCtypePrice(), // price
                "", // discount amount
                busModel.getmCtypePrice(), // total amount
                "1", // pay_status (1 for Paid)
                busModel.getmCtypePrice(), // paid_amt (full amount paid)
                "1", // bking_status (1 for Pending)
                loginModel.getmCustName(),
                loginModel.getmCustMobile(),
                loginModel.getmCustEmail(),
                branch);

        call.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(@NonNull Call<BookingResponse> call, @NonNull Response<BookingResponse> response) {
                hideLoader();
                if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                    bookingModelList.addAll(response.body().getData());
                    showSuccessDialog("Booking Registered Successfully...!", BusDetailActivity.this);
                } else {
                    showError("Your bus has not been booked. Please contact support.");
                    binding.btnContinue.setEnabled(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingResponse> call, @NonNull Throwable t) {
                hideLoader();
                Log.e(TAG, "insertBusBookingApi failed", t);
                showError("Something went wrong");
                binding.btnContinue.setEnabled(true);
            }
        });
    }

    public void showSuccessDialog(String message, Context context) {
        dialog = new Dialog(BusDetailActivity.this, R.style.my_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.show();

        TextView textView = dialog.findViewById(R.id.tv_message);
        textView.setText(message);

        GifImageView gifImageView = dialog.findViewById(R.id.gifImageView);
        gifImageView.setImageResource(R.drawable.gif_booking_confirmation);

        new CountDownTimer(2500, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                dialog.cancel();
                Intent intent = new Intent(BusDetailActivity.this, MyBookingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }
        }.start();
    }
}
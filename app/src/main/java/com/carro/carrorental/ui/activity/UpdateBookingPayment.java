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
//import com.google.gson.Gson;
//import com.logixhunt.carrorental.R;
//import com.logixhunt.carrorental.api.ApiClient;
//import com.logixhunt.carrorental.api.ApiInterface;
//import com.logixhunt.carrorental.api.response.commonResponse.BaseResponse;
//import com.logixhunt.carrorental.databinding.ActivityUpdateBookingPaymentBinding;
//import com.logixhunt.carrorental.model.CarTypeModel;
//import com.logixhunt.carrorental.model.LoginModel;
//import com.logixhunt.carrorental.model.BookingListModel;
//import com.logixhunt.carrorental.ui.adapter.BookingListAdapter;
//import com.logixhunt.carrorental.ui.common.BaseActivity;
//import com.logixhunt.carrorental.utils.Constant;
//import com.logixhunt.carrorental.utils.DateFormater;
//import com.logixhunt.carrorental.utils.PreferenceUtils;
//import com.logixhunt.carrorental.utils.Utils;
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
//public class UpdateBookingPayment extends BaseActivity implements PaymentResultListener {
//
//    ActivityUpdateBookingPaymentBinding binding;
//    String bking_Id;
//    String pre_paid_amt;
//    String paid_amt;
//    String total;
//    LoginModel loginModel=new LoginModel();
//    BookingListModel bookingListModel = new BookingListModel();
//    Dialog dialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding=ActivityUpdateBookingPaymentBinding.inflate(getLayoutInflater());
//        EdgeToEdge.enable(this);
//        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        getPreferenceData();
//    }
//    private void getPreferenceData() {
//        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA,UpdateBookingPayment.this);
//        loginModel = new Gson().fromJson(userData, LoginModel.class);
//        bookingListModel = new Gson().fromJson(getIntent().getStringExtra(Constant.BundleExtras.BOOKING_DATA), BookingListModel.class);
//
//        initialization();
//    }
//
//    private void initialization() {
//        setData();
//        binding.btnPay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                double rupeeToPaisa = Double.parseDouble(paid_amt) * 100;
//                AsyncCaller asyncCaller = new AsyncCaller(rupeeToPaisa);// Money is being added in Paisa....
//                asyncCaller.execute();
//            }
//        });
//    }
//
//    private void setData(){
//        binding.tvArea.setText(bookingListModel.getmBkingPickupAddress());
//        binding.tvTotalAmt.setText("Rs. "+bookingListModel.getmBkingTotal());
//        binding.tvPaidAmt.setText("Rs. "+bookingListModel.getmBkingPaidAmt());
//        binding.tvBalanceAmt.setText("Rs. "+bookingListModel.getmBkingRemainAmt());
//        binding.tvBookingId.setText("Booking Id : "+bookingListModel.getmBookingId());
//        binding.tvPersons.setText(bookingListModel.getmBkingKm()+" Km");
//       binding.tvTime.setText(Utils.formatTimeString(Constant.HHMMSS,Constant.HHMMSSA,bookingListModel.getmBkingPickupAt()));
//       binding.tvDate.setText(DateFormater.changeDateFormat(Constant.yyyyMMdd, Constant.ddMMyyyy, bookingListModel.getmBkingPickup()));
//         bking_Id=bookingListModel.getmBkingId();
//         pre_paid_amt=bookingListModel.getmBkingPaidAmt();
//         paid_amt=bookingListModel.getmBkingRemainAmt();
//         total=bookingListModel.getmBkingTotal();
//    }
//
//    private void updateBookingPaymentApi(String trans_id) {
//        showLoader();
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<BaseResponse> call = apiInterface.update_booking_payment(bking_Id,pre_paid_amt,paid_amt,total,trans_id);
//        call.enqueue(new Callback<BaseResponse>() {
//            @Override
//            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                hideLoader();
//                try {
//                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
//                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
//
//                            showSuccessDialog("Payment Updated Successfully....!",UpdateBookingPayment.this);
//
//                        } else {
//                            hideLoader();
//
//                        }
//                    } else {
//                        hideLoader();
//
//                    }
//                } catch (Exception e) {
//                    hideLoader();
//                    e.printStackTrace();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse> call, Throwable t) {
//                hideLoader();
//                Log.e("Failure", t.toString());
//                showError("Something went wrong");
//            }
//        });
//    }
//
//
//    public void showSuccessDialog(String message, Context context) {
//        dialog = new Dialog(UpdateBookingPayment.this, R.style.my_dialog);
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
//                Intent intent=new Intent(UpdateBookingPayment.this, MyBookingsActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }.start();
//
//    }
//
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
//        ProgressDialog pdLoading = new ProgressDialog(UpdateBookingPayment.this);
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
//        final Activity activity = UpdateBookingPayment.this;
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
//        updateBookingPaymentApi(s);
//        Checkout.clearUserData(UpdateBookingPayment.this);
//    }
//
//    @Override
//    public void onPaymentError(int i, String s) {
//        setMessageForSnackbar("Payment Failed", false);
//        Checkout.clearUserData(UpdateBookingPayment.this);
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
//                    .setActivity(UpdateBookingPayment.this)
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
//                    .setActivity(UpdateBookingPayment.this)
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
import com.carro.carrorental.api.response.CreateOrderResponse;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.databinding.ActivityUpdateBookingPaymentBinding;
import com.carro.carrorental.model.BookingListModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.PreferenceUtils;
import com.carro.carrorental.utils.Utils;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import de.mateware.snacky.Snacky;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateBookingPayment extends BaseActivity implements PaymentResultListener {

    private static final String TAG = "UpdateBookingPayment";
    ActivityUpdateBookingPaymentBinding binding;
    String bking_Id;
    String pre_paid_amt;
    String paid_amt;
    String total;
    LoginModel loginModel = new LoginModel();
    BookingListModel bookingListModel = new BookingListModel();
    Dialog dialog;

    private boolean isPaymentProcessStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBookingPaymentBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Checkout.preload(getApplicationContext());
        getPreferenceData();
    }

    private void getPreferenceData() {
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, UpdateBookingPayment.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        bookingListModel = new Gson().fromJson(getIntent().getStringExtra(Constant.BundleExtras.BOOKING_DATA), BookingListModel.class);
        initialization();
    }

    private void initialization() {
        setData();
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());
        binding.btnPay.setOnClickListener(v -> {
            binding.btnPay.setEnabled(false);
            if (paid_amt != null && !paid_amt.isEmpty() && Double.parseDouble(paid_amt) > 0) {
                isPaymentProcessStarted = true;
                startPaymentFlow(Double.parseDouble(paid_amt));
            } else {
                showError("Invalid amount to pay.");
                binding.btnPay.setEnabled(true);
            }
        });
    }

    private void setData() {
        binding.tvArea.setText(bookingListModel.getmBkingPickupAddress());
        binding.tvTotalAmt.setText("Rs. " + bookingListModel.getmBkingTotal());
        binding.tvPaidAmt.setText("Rs. " + bookingListModel.getmBkingPaidAmt());
        binding.tvBalanceAmt.setText("Rs. " + bookingListModel.getmBkingRemainAmt());
        binding.tvBookingId.setText("Booking Id : " + bookingListModel.getmBookingId());
        binding.tvPersons.setText(bookingListModel.getmBkingKm() + " Km");
        binding.tvTime.setText(Utils.formatTimeString(Constant.HHMMSS, Constant.HHMMSSA, bookingListModel.getmBkingPickupAt()));
        binding.tvDate.setText(DateFormater.changeDateFormat(Constant.yyyyMMdd, Constant.ddMMyyyy, bookingListModel.getmBkingPickup()));
        bking_Id = bookingListModel.getmBookingId();
        pre_paid_amt = bookingListModel.getmBkingPaidAmt();
        paid_amt = bookingListModel.getmBkingRemainAmt();
        total = bookingListModel.getmBkingTotal();
    }

    private void updateBookingPaymentApi(String trans_id) {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiInterface.update_booking_payment(bking_Id, pre_paid_amt, paid_amt, total, trans_id);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                hideLoader();
                if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                    showSuccessDialog("Payment Updated Successfully....!", UpdateBookingPayment.this);
                } else {
                    showError("Failed to update payment. Please contact support.");
                    binding.btnPay.setEnabled(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                hideLoader();
                Log.e(TAG, "updateBookingPaymentApi failed", t);
                showError("Something went wrong");
                binding.btnPay.setEnabled(true);
            }
        });
    }

    public void showSuccessDialog(String message, Context context) {
        dialog = new Dialog(UpdateBookingPayment.this, R.style.my_dialog);
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
                Intent intent = new Intent(UpdateBookingPayment.this, MyBookingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void setMessageForSnackbar(String msg, boolean flag) {
        if (flag) {
            Snacky.builder()
                    .setActivity(UpdateBookingPayment.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {})
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .success()
                    .show();
        } else {
            Snacky.builder()
                    .setActivity(UpdateBookingPayment.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {})
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .error()
                    .show();
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
//                                isPaymentProcessStarted = false;
//                                showError("Server did not provide an Order ID.");
//                                binding.btnPay.setEnabled(true);
//                            }
//                        } else {
//                            isPaymentProcessStarted = false;
//                            showError("Payment initialization failed on server.");
//                            binding.btnPay.setEnabled(true);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<CreateOrderResponse> call, @NonNull Throwable t) {
//                        pdLoading.dismiss();
//                        isPaymentProcessStarted = false;
//                        Log.e(TAG, "createRazorpayOrder API call failed", t);
//                        showError("An error occurred. Please check your connection.");
//                        binding.btnPay.setEnabled(true);
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
                                isPaymentProcessStarted = false;
                                showError("Server did not provide an Order ID.");
                                binding.btnPay.setEnabled(true);
                            }
                        } else {
                            isPaymentProcessStarted = false;
                            showError("Payment initialization failed on server.");
                            binding.btnPay.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CreateOrderResponse> call, @NonNull Throwable t) {
                        pdLoading.dismiss();
                        isPaymentProcessStarted = false;
                        Log.e(TAG, "createRazorpayOrder API call failed", t);
                        showError("An error occurred. Please check your connection.");
                        binding.btnPay.setEnabled(true);
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
            options.put("description", "Payment for Booking ID #" + bking_Id);
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
            binding.btnPay.setEnabled(true);
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentId) {
        if (!isPaymentProcessStarted) {
            Log.w(TAG, "Ignoring a stray onPaymentSuccess callback.");
            return;
        }
        isPaymentProcessStarted = false;
        updateBookingPaymentApi(razorpayPaymentId);
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
        binding.btnPay.setEnabled(true);
    }
}
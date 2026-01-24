package com.carro.carrorental.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.databinding.ActivityOtpBinding;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends BaseActivity {

    ActivityOtpBinding binding;
    String mobile;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getOptPreference();
    }


    private void getOptPreference() {
        mobile = getIntent().getStringExtra(Constant.BundleExtras.PHONE_NUMBER);

        userId = PreferenceUtils.getString(Constant.PreferenceConstant.USER_ID, OtpActivity.this);


        initialization();
    }


    private void initialization() {

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyOtpAPi();
            }
        });
        binding.tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtpAPi();
            }
        });

        sendOtpAPi();
    }


    private void timer() {
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");

                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                binding.tvSendOtpCount.setText("Resend in " + f.format(min) + ":" + f.format(sec));
            }

            public void onFinish() {
                binding.tvResendOTP.setVisibility(View.VISIBLE);
                binding.tvSendOtpCount.setVisibility(View.GONE);
                binding.tvSendOtpCount.setTextColor(Color.parseColor("#FF8585"));
            }
        }.start();
    }

    private void sendOtpAPi() {

        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiInterface.send_OTP(mobile);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            showAlert(response.body().getMessage());
                            binding.tvResendOTP.setVisibility(View.GONE);
                            binding.tvSendOtpCount.setVisibility(View.VISIBLE);
                            timer();
                        } else {
                            hideLoader();
                            showError(response.body().getMessage());
                        }
                    } else {
                        hideLoader();
                        showError(response.message());
                    }
                } catch (Exception e) {
                    hideLoader();
                    e.printStackTrace();
                    showError(response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void verifyOtpAPi() {
        String otp = binding.otpview.getOTP().toString();
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiInterface.verify_OTP(userId, otp);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            showAlert(response.body().getMessage());
                            PreferenceUtils.setBoolean(Constant.PreferenceConstant.IS_LOGIN, true, OtpActivity.this);
                            // checkUserData();

                            Intent intent = new Intent(OtpActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            hideLoader();
                            showError("OTP Not Matched! Please Try Again");
                        }
                    } else {
                        hideLoader();
                        showError("OTP Not Matched! Please Try Again");
                    }
                } catch (Exception e) {
                    hideLoader();
                    e.printStackTrace();
                    showError("OTP Not Matched! Please Try Again");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }


}

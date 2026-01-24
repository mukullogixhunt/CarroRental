package com.carro.carrorental.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.LoginResponse;
import com.carro.carrorental.databinding.ActivityLoginBinding;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });        initiateLogin();
    }

    private void initiateLogin() {
        binding.btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    signUpApi();
                }
            }
        });
    }

    private void signUpApi() {

        String mobile = binding.etMobile.getText().toString().trim();
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.signUpUser(mobile);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            showAlert(response.body().getMessage());

                            PreferenceUtils.setString(Constant.PreferenceConstant.USER_DATA, new Gson().toJson(response.body().getData().get(0)), LoginActivity.this);
                            PreferenceUtils.setString(Constant.PreferenceConstant.USER_ID, response.body().getData().get(0).getmCustId(), LoginActivity.this);

                            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                            intent.putExtra(Constant.BundleExtras.PHONE_NUMBER, mobile);
                            startActivity(intent);
                            finish();

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
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        if (binding.etMobile.getText().toString().isEmpty()) {
            binding.etMobile.setError("Please enter your mobile no..!");
            valid = false;
        } else {
            if (binding.etMobile.getText().toString().length() != 10) {
                binding.etMobile.setError("Please enter valid mobile no..!");
                valid = false;
            } else {
                binding.etMobile.setError(null);
            }
        }
        return valid;
    }
}
package com.carro.carrorental.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.databinding.ActivityRateUsBinding;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.utils.PreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateUsActivity extends BaseActivity {

    ActivityRateUsBinding binding;
    LoginModel loginModel = new LoginModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRateUsBinding.inflate(getLayoutInflater());
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

        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, RateUsActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);

        initialization();
    }

    private void initialization() {
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());

        binding.tvName.setText(loginModel.getmCustName());
        Glide.with(RateUsActivity.this)
                .load(ImagePathDecider.getUserImagePath()+loginModel.getmCustImg())
                .error(R.drawable.img_no_profile)
                .into(binding.ivUserN);

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertReviewApi();
            }
        });


    }

    private void insertReviewApi() {
        String review =binding.etReview.getText().toString();
        float rating = binding.starRatingBar.getRating();
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiInterface.insert_review(loginModel.getmCustId(),String.valueOf(rating),review);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            showError(response.body().getMessage());

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


}
package com.carro.carrorental.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.OfferResponse;
import com.carro.carrorental.databinding.ActivityOffersBinding;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.OfferModel;
import com.carro.carrorental.ui.adapter.OfferAdapter;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersActivity extends BaseActivity {

    ActivityOffersBinding binding;
    private List<OfferModel> offerModelList = new ArrayList<>();
    private OfferAdapter offerAdapter;
    LoginModel loginModel = new LoginModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOffersBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });        getUserPreferences();
    }
    private void getUserPreferences() {

        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, OffersActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);

        initialization();
    }

    private void initialization() {
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());
        getOffers();
    }

    private void getOffers() {

        binding.rvOffers.setVisibility(View.GONE);
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<OfferResponse> call = apiService.getOffers();
        call.enqueue(new Callback<OfferResponse>() {
            @Override
            public void onResponse(Call<OfferResponse> call, Response<OfferResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            binding.rvOffers.setVisibility(View.VISIBLE);

                            offerModelList.clear();
                            offerModelList.addAll(response.body().getData());

                            GridLayoutManager gridLayoutManager = new GridLayoutManager(OffersActivity.this,2);
                            binding.rvOffers.setLayoutManager(gridLayoutManager);
                            offerAdapter = new OfferAdapter(OffersActivity.this, offerModelList);
                            binding.rvOffers.setAdapter(offerAdapter);

                        } else {
                            hideLoader();
                            binding.rvOffers.setVisibility(View.GONE);
                        }
                    } else {
                        hideLoader();
                        binding.rvOffers.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    hideLoader();
//                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                    binding.rvOffers.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<OfferResponse> call, Throwable t) {
                hideLoader();
                // Log error here since request failed
                Log.e("Failure", t.toString());
                binding.rvOffers.setVisibility(View.GONE);
//                showError("Something went wrong");
            }
        });
    }
}
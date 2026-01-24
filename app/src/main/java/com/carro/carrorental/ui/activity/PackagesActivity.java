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
import com.carro.carrorental.api.response.PackageResponse;
import com.carro.carrorental.databinding.ActivityPackagesBinding;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.PackageModel;
import com.carro.carrorental.ui.adapter.PackageAdapter;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackagesActivity extends BaseActivity {

    ActivityPackagesBinding binding;
    private List<PackageModel> packageModelList = new ArrayList<>();
    private PackageAdapter packageAdapter;
    LoginModel loginModel = new LoginModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPackagesBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });        getUserPreferences();

    }
    private void getUserPreferences() {

        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, PackagesActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);

        initialization();
    }

    private void initialization() {
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());
        get_package();
    }
    private void get_package() {

        binding.rvPackages.setVisibility(View.GONE);
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PackageResponse> call = apiService.get_package();
        call.enqueue(new Callback<PackageResponse>() {
            @Override
            public void onResponse(Call<PackageResponse> call, Response<PackageResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            binding.rvPackages.setVisibility(View.VISIBLE);

                            packageModelList.clear();
                            packageModelList.addAll(response.body().getData());

                            GridLayoutManager gridLayoutManager = new GridLayoutManager(PackagesActivity.this,2);
                            binding.rvPackages.setLayoutManager(gridLayoutManager);
                            packageAdapter = new PackageAdapter(PackagesActivity.this, packageModelList);
                            binding.rvPackages.setAdapter(packageAdapter);

                        } else {
                            hideLoader();
                            binding.rvPackages.setVisibility(View.GONE);
                        }
                    } else {
                        hideLoader();
                        binding.rvPackages.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    hideLoader();
//                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                    binding.rvPackages.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<PackageResponse> call, Throwable t) {
                hideLoader();
                // Log error here since request failed
                Log.e("Failure", t.toString());
                binding.rvPackages.setVisibility(View.GONE);
//                showError("Something went wrong");
            }
        });
    }
}
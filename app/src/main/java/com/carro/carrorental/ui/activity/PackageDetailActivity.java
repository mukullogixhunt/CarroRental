package com.carro.carrorental.ui.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.ActivityPackageDetailBinding;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.PackageModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.utils.PreferenceUtils;

public class PackageDetailActivity extends BaseActivity {

    ActivityPackageDetailBinding binding;
    PackageModel packageModel=new PackageModel();
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPackageDetailBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });        initialization();

    }

    private void initialization(){
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, this);
        LoginModel loginModel = new Gson().fromJson(userData, LoginModel.class);
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());
        packageModel = new Gson().fromJson(getIntent().getStringExtra(Constant.BundleExtras.PACKAGE_DATA), PackageModel.class);
//        Glide.with(PackageDetailActivity.this)
//                .load(ImagePathDecider.getPackageImagePath()+packageModel.getmPackageThumbnail())
//                .error(R.drawable.image_self_drive)
//                .into(binding.imgBanner);
        binding.tvTitle.setText(packageModel.getmPackageTitle());
        binding.tvPrice.setText("Rs. "+packageModel.getmPackagePrice());

        date= DateFormater.changeDateFormat(Constant.yyyyMMdd_HHmmss, Constant.ddMMyyyy_HHMMSSA,packageModel.getmPackageAddedon());
        binding.tvDate.setText(date);
        binding.tvNote.setText(packageModel.getmPackageNote());
        binding.tvDetails.setText(packageModel.getmPackageDetails());
        binding.tvTAndC.setText(packageModel.getmPackageTc());
        Glide.with(PackageDetailActivity.this)
                .load(ImagePathDecider.getPackageImagePath()+packageModel.getmPackageImg())
                .error(R.drawable.image_self_drive)
                .into(binding.imgBanner);
    }
}
package com.carro.carrorental.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.ActivityProfileDetailBinding;
import com.carro.carrorental.databinding.LogoutBottomDialogBinding;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.utils.PreferenceUtils;

public class ProfileDetailActivity extends BaseActivity {

    ActivityProfileDetailBinding binding;
    LoginModel loginModel = new LoginModel();
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });        getUserPreferences();
    }

    private void getUserPreferences() {
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, ProfileDetailActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        initialization();
        getAppVersion();

    }

    private void initialization() {
        binding.tvName.setText(loginModel.getmCustName());
        binding.tvNumber.setText(loginModel.getmCustMobile());


        Glide.with(ProfileDetailActivity.this)
                .load(ImagePathDecider.getUserImagePath()+loginModel.getmCustImg())
                .error(R.drawable.img_no_profile)
                .into(binding.ivUserImg);

        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());

        binding.cvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDetailActivity.this, EditProfileActivity.class);
                intent.putExtra(Constant.BundleExtras.PAGE_TYPE,"profile");
                startActivity(intent);
            }
        });

        binding.cvEditDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDetailActivity.this, ProfileVerificationActivity.class);
                intent.putExtra(Constant.BundleExtras.PAGE_TYPE,"profile");
                startActivity(intent);
                finish();
            }
        });
        binding.cvMyBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDetailActivity.this, MyBookingsActivity.class);
                startActivity(intent);
            }
        });
        binding.cvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.WEBVIEW_TITLE = getString(R.string.about_us);
                Constant.WEBVIEW_URL = getString(R.string.about_us_url);
                Intent intent = new Intent(ProfileDetailActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });
        binding.cvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.WEBVIEW_TITLE = getString(R.string.contact_us);
                Constant.WEBVIEW_URL = getString(R.string.contact_us_url);
                Intent intent = new Intent(ProfileDetailActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });
        binding.cvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.WEBVIEW_TITLE = getString(R.string.privacy_policy);
                Constant.WEBVIEW_URL = getString(R.string.privacy_policy_url);
                Intent intent = new Intent(ProfileDetailActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });
        binding.cvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.WEBVIEW_TITLE = getString(R.string.terms_condition);
                Constant.WEBVIEW_URL = getString(R.string.terms_and_condition_url);
                Intent intent = new Intent(ProfileDetailActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

        binding.cvDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.WEBVIEW_TITLE = getString(R.string.delete_account);
                Constant.WEBVIEW_URL = getString(R.string.delete_account_url);
                Intent intent = new Intent(ProfileDetailActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });
        binding.cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutBottomDialog();
            }
        });

    }
    private void getAppVersion() {
        PackageManager packageManager = ProfileDetailActivity.this.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(ProfileDetailActivity.this.getPackageName(), 0);

            // Get version name and version code
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;

            // Display version name and version code in TextView
            String versionInfo = "Version Name: " + versionName + "\nVersion Code: " + versionCode;

            binding.tvVersion.setText(versionInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void logoutBottomDialog() {
        LogoutBottomDialogBinding logoutBottomDialogBinding;
        logoutBottomDialogBinding = LogoutBottomDialogBinding.inflate(getLayoutInflater());

        dialog = new BottomSheetDialog(ProfileDetailActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(logoutBottomDialogBinding.getRoot());
        dialog.show();

        logoutBottomDialogBinding.ivCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        logoutBottomDialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        logoutBottomDialogBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PreferenceUtils.setBoolean(Constant.PreferenceConstant.IS_LOGIN, false, ProfileDetailActivity.this);
                PreferenceUtils.clearAll(ProfileDetailActivity.this);
                Intent intent = new Intent(ProfileDetailActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
package com.carro.carrorental.ui.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.carro.carrorental.BuildConfig;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.LayoutToolbarBinding;
import com.carro.carrorental.ui.activity.NotificationActivity;
import com.carro.carrorental.ui.activity.ProfileDetailActivity;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.widgets.CustomProgressDialog;


public class BaseActivity extends AppCompatActivity {

    Dialog mProgressDialog;
    public static BaseActivity baseActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        //progress dialog
        mProgressDialog = new CustomProgressDialog(this);

    }

    /**
     * show loader
     */
    public void showLoader() {
        try {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {

        }
    }

    /**
     * Hide Loader
     */
    public void hideLoader() {
        try {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }


    /**
     * Show Error
     */
    public void showError(String msg) {
        if (msg == null) return;
        Snackbar snackbar =  Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG) ;

        View snackbarView = snackbar.getView();

// Change position to center
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.CENTER;
        snackbarView.setLayoutParams(params);

// Optional: center text
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        snackbar.show();
    }

    /**
     * Show alert
     */
    public void showAlert(String msg) {
        if (msg == null) return;
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void log_d(String className, String message) {
        if (BuildConfig.DEBUG)
            Log.d(className, "" + message);
    }

    public void log_e(String className, String message, Exception e) {
        if (BuildConfig.DEBUG)
            Log.e(className, "" + message, e);
    }
    /**
     * Setup toolbar
     */
    public void setUpToolBar(LayoutToolbarBinding binding, Activity activity, String image) {

        Glide.with(activity)
                .load(ImagePathDecider.getUserImagePath() + image)
                .error(R.drawable.img_no_profile)
                .into(binding.ivUser);

       /* binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });*/
        binding.ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProfileDetailActivity.class);
                startActivity(intent);
            }
        });
        binding.ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }


}
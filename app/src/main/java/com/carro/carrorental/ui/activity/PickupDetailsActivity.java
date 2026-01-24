package com.carro.carrorental.ui.activity;

import static android.view.View.GONE;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.ActivityPickupDetailsBinding;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

public class PickupDetailsActivity extends BaseActivity {

    ActivityPickupDetailsBinding binding;
    String wayType;
    String cabServiceType;
    String pick_date="";
    String pick_time="";
    String pickAddress="";
    String drop_date="";
    String drop_time="";
    String drop_address="";
    String branch="";
    String cTypeId;
    String carTypeName;
    String carId;
    String vendorId;
    String price;
    String b_name;
    String b_mobile;
    String b_email;
    String inclink;
    String excLink;
    String tcLink;
    LoginModel loginModel = new LoginModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPickupDetailsBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getPreferenceData();
    }

    private void getPreferenceData(){
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, PickupDetailsActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        wayType=getIntent().getStringExtra(Constant.BundleExtras.WAY_TYPE);
        pickAddress=getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
        pick_date=getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
        pick_time=getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
        drop_address=getIntent().getStringExtra(Constant.BundleExtras.DROP_ADDRESS);
        drop_date=getIntent().getStringExtra(Constant.BundleExtras.DROP_DATE);
        drop_time=getIntent().getStringExtra(Constant.BundleExtras.DROP_TIME);
        cTypeId=getIntent().getStringExtra(Constant.BundleExtras.C_TYPE_ID);
        carTypeName=getIntent().getStringExtra(Constant.BundleExtras.C_TYPE_NAME);
        carId=getIntent().getStringExtra(Constant.BundleExtras.CAR_ID);
        vendorId=getIntent().getStringExtra(Constant.BundleExtras.VENDOR_ID);
        price=getIntent().getStringExtra(Constant.BundleExtras.PRICE);
        branch=getIntent().getStringExtra(Constant.BundleExtras.BRANCH_ID);
        cabServiceType = getIntent().getStringExtra(Constant.BundleExtras.CAB_SERVICE_TYPE);


        inclink = PreferenceUtils.getString(Constant.PreferenceConstant.WEBVIEW_INC,PickupDetailsActivity.this);
        excLink = PreferenceUtils.getString(Constant.PreferenceConstant.WEBVIEW_EXC,PickupDetailsActivity.this);
        tcLink = PreferenceUtils.getString(Constant.PreferenceConstant.WEBVIEW_TC,PickupDetailsActivity.this);
        initialization();
    }

    private void initialization(){
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());

//        webView(inclink);
        binding.webView.setText(inclink);



        setCardData();

        if(wayType.equals("1")){
            binding.lvDropLocation.setVisibility(GONE);
            binding.viewB.setVisibility(GONE);
            binding.tvC.setVisibility(GONE);
            binding.tvAddress3.setVisibility(GONE);
        }else {
            binding.lvDropLocation.setVisibility(GONE);
            binding.viewB.setVisibility(View.VISIBLE);
            binding.tvC.setVisibility(View.VISIBLE);
            binding.tvAddress3.setVisibility(View.VISIBLE);
        }




        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_name=binding.etName.getText().toString();
                b_mobile=binding.etMobile.getText().toString();
                b_email=binding.etEmail.getText().toString();
                Intent intent=new Intent(PickupDetailsActivity.this,PaymentDetailsActivity.class);
                intent.putExtra(Constant.BundleExtras.WAY_TYPE,wayType);
                intent.putExtra(Constant.BundleExtras.CAB_SERVICE_TYPE,cabServiceType);
                intent.putExtra(Constant.BundleExtras.PICK_ADDRESS,pickAddress);
                intent.putExtra(Constant.BundleExtras.PICK_DATE,pick_date);
                intent.putExtra(Constant.BundleExtras.PICK_TIME,pick_time);
                intent.putExtra(Constant.BundleExtras.DROP_ADDRESS,drop_address);
                intent.putExtra(Constant.BundleExtras.DROP_DATE,drop_date);
                intent.putExtra(Constant.BundleExtras.DROP_TIME,drop_time);
                intent.putExtra(Constant.BundleExtras.C_TYPE_ID,cTypeId);
                intent.putExtra(Constant.BundleExtras.C_TYPE_NAME,carTypeName);
                intent.putExtra(Constant.BundleExtras.CAR_ID,carId);
                intent.putExtra(Constant.BundleExtras.VENDOR_ID,vendorId);
                intent.putExtra(Constant.BundleExtras.PRICE,price);
                intent.putExtra(Constant.BundleExtras.B_NAME,b_name);
                intent.putExtra(Constant.BundleExtras.B_MOBILE,b_mobile);
                intent.putExtra(Constant.BundleExtras.B_EMAIL,b_email);
                intent.putExtra(Constant.BundleExtras.BRANCH_ID,branch);
                startActivity(intent);
                finish();
            }
        });

        binding.tvInclusions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvInclusions.setBackgroundColor(ContextCompat.getColor(PickupDetailsActivity.this, R.color.primary_field));
                binding.tvExclusions.setBackgroundColor(ContextCompat.getColor(PickupDetailsActivity.this, R.color.white));
                binding.tvTC.setBackgroundColor(ContextCompat.getColor(PickupDetailsActivity.this, R.color.white));
                //webView(inclink);
                binding.webView.setText(inclink);

            }
        });

        binding.tvExclusions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvExclusions.setBackgroundColor(ContextCompat.getColor(PickupDetailsActivity.this, R.color.primary_field));
                binding.tvInclusions.setBackgroundColor(ContextCompat.getColor(PickupDetailsActivity.this, R.color.white));
                binding.tvTC.setBackgroundColor(ContextCompat.getColor(PickupDetailsActivity.this, R.color.white));
//                webView(excLink);
                binding.webView.setText(excLink);

            }
        });
        binding.tvTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvTC.setBackgroundColor(ContextCompat.getColor(PickupDetailsActivity.this, R.color.primary_field));
                binding.tvInclusions.setBackgroundColor(ContextCompat.getColor(PickupDetailsActivity.this, R.color.white));
                binding.tvExclusions.setBackgroundColor(ContextCompat.getColor(PickupDetailsActivity.this, R.color.white));
//                webView(tcLink);
                binding.webView.setText(tcLink);

            }
        });
    }

//    private void webView(String webUrl){
//        binding.webView.getSettings().setJavaScriptEnabled(true);
//        binding.webView.getSettings().setBuiltInZoomControls(true);
//        binding.webView.getSettings().setDisplayZoomControls(false);
//        binding.webView.setWebViewClient(new MyWebViewClient());
//        binding.webView.loadUrl(webUrl);
//        binding.webView.setVerticalScrollBarEnabled(true);
//        binding.webView.setHorizontalScrollBarEnabled(true);
//    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideLoader();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showLoader();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(PickupDetailsActivity.this);
            builder.setMessage(R.string.notification_error_ssl_cert_invalid);
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void setCardData(){


        binding.tvAddress1.setText(pickAddress);
        binding.tvAddress2.setText(drop_address);
        binding.tvAddress3.setText(pickAddress);
        binding.tvTime1.setText(pick_time);
        binding.tvTime2.setText(drop_time);
        binding.tvPickupDateTime.setText(pick_date+" "+pick_time);

        if(drop_date == null || drop_date.isEmpty()){
            binding.llDrop.setVisibility(View.GONE);
        }else{
            binding.llDrop.setVisibility(View.VISIBLE);
            binding.tvDropDate.setText(drop_date);

        }

        binding.tvTotalFare.setText("Rs. "+price);
        binding.tvCarType.setText(carTypeName);
        binding.etName.setText(loginModel.getmCustName());
        binding.etEmail.setText(loginModel.getmCustEmail());
        binding.etMobile.setText(loginModel.getmCustMobile());

//        switch (wayType){
//            case "1":
//                binding.tvTripType.setText("One Way");
//                break;
//            case "2":
//                binding.tvTripType.setText("Round Trip");
//                break;
//            case "3":
//                binding.tvTripType.setText("Hourly Trip");
//                break;
//            case "4":
//                binding.tvTripType.setText("Flight Trip");
//                break;
//
//        }

        switch (cabServiceType){
            case "1":
                binding.tvTripType.setText("City Ride");
                break;
            case "2":
                binding.tvTripType.setText("One Way");
                break;
            case "3":
                binding.tvTripType.setText("Out Station");
                break;
            case "4":
                binding.tvTripType.setText("Airport");
                break;

        }
    }
}
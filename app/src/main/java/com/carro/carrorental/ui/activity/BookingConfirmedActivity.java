package com.carro.carrorental.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.ActivityBookingConfirmedBinding;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;
import com.carro.carrorental.utils.Utils;

public class BookingConfirmedActivity extends BaseActivity {

    ActivityBookingConfirmedBinding binding;
    String wayType;
    String pick_time="";
    String pickAddress="";
    String drop_time="";
    String drop_address="";
    String carTypeName;
    String pick_date="";
    String drop_date="";
    String price;
    String booking_id;
    LoginModel loginModel = new LoginModel();
    String inclink;
    String excLink;
    String tcLink;

    String cabServiceType;
    String map_distance;
    String map_duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBookingConfirmedBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Your old logic from onBackPressed()
                Intent intent = new Intent(BookingConfirmedActivity.this, HomeActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        getPreferenceData();
    }
    private void getPreferenceData(){
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, BookingConfirmedActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);

        wayType=getIntent().getStringExtra(Constant.BundleExtras.WAY_TYPE);
        pickAddress=getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
        pick_date=getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
        pick_time=getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
        drop_address=getIntent().getStringExtra(Constant.BundleExtras.DROP_ADDRESS);
        drop_date=getIntent().getStringExtra(Constant.BundleExtras.DROP_DATE);
        drop_time=getIntent().getStringExtra(Constant.BundleExtras.DROP_TIME);
        carTypeName=getIntent().getStringExtra(Constant.BundleExtras.C_TYPE_NAME);
        price=getIntent().getStringExtra(Constant.BundleExtras.PRICE);
        booking_id=getIntent().getStringExtra(Constant.BundleExtras.BOOKING_ID);


        inclink = PreferenceUtils.getString(Constant.PreferenceConstant.WEBVIEW_INC,BookingConfirmedActivity.this);
        excLink = PreferenceUtils.getString(Constant.PreferenceConstant.WEBVIEW_EXC,BookingConfirmedActivity.this);
        tcLink = PreferenceUtils.getString(Constant.PreferenceConstant.WEBVIEW_TC,BookingConfirmedActivity.this);



        map_duration = PreferenceUtils.getString(Constant.PreferenceConstant.MAP_DURATION, BookingConfirmedActivity.this);
        map_distance = PreferenceUtils.getString(Constant.PreferenceConstant.MAP_DISTANCE, BookingConfirmedActivity.this);
        cabServiceType = getIntent().getStringExtra(Constant.BundleExtras.CAB_SERVICE_TYPE);

        initialization();
    }

    private void initialization(){

        wayType=getIntent().getStringExtra(Constant.BundleExtras.WAY_TYPE);
        if(wayType.equals("1")){
            binding.viewB.setVisibility(View.GONE);
            binding.tvC.setVisibility(View.GONE);
            binding.tvAddress3.setVisibility(View.GONE);
        }else {
            binding.viewB.setVisibility(View.VISIBLE);
            binding.tvC.setVisibility(View.VISIBLE);
            binding.tvAddress3.setVisibility(View.VISIBLE);
        }
        setCardData();
        binding.btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookingConfirmedActivity.this,HomeActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });


        binding.llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialor("18001020802");
            }
        });
    }

    private void openDialor(String phoneNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(dialIntent);
    }



    private void setCardData(){
        binding.tvAddress1.setText(pickAddress);
        binding.tvAddress2.setText(drop_address);
        binding.tvAddress3.setText(pickAddress);
        binding.tvTime1.setText(pick_time);
        binding.tvTime2.setText(drop_time);
        binding.tvPickupDateTime.setText(pick_date+" "+pick_time);
        binding.tvDropDate.setText(drop_date);
        binding.tvTotalFare.setText("Rs. "+price);
        binding.tvCarType.setText(carTypeName);
        binding.tvName.setText("Thankyou , "+loginModel.getmCustName()+" !");
        binding.tvBookingId.setText("Your Booking Id : "+booking_id);


        switch (cabServiceType) {
            case "1":
                binding.tvTripType.setText("City Ride ("+ Utils.formatMapDuration(map_duration) + " | "+map_distance+ " Km)");
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
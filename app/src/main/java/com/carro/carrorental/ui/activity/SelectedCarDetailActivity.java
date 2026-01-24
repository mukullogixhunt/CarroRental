package com.carro.carrorental.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.ActivitySelectedCarDetailBinding;
import com.carro.carrorental.model.CarTypeModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.utils.PreferenceUtils;

import de.mateware.snacky.Snacky;

public class SelectedCarDetailActivity extends BaseActivity {

    ActivitySelectedCarDetailBinding binding;
    LoginModel loginModel = new LoginModel();
    CarTypeModel carTypeModel = new CarTypeModel();
    String pick_date = "";
    String pick_time = "";
    String pickAddress = "";
    String return_date = "";
    String return_time = "";
    String price = "";
    String screen_type = "";
    String in_out_side = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySelectedCarDetailBinding.inflate(getLayoutInflater());
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
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, SelectedCarDetailActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        carTypeModel = new Gson().fromJson(getIntent().getStringExtra(Constant.BundleExtras.CAR_DATA), CarTypeModel.class);
        pick_date=getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
        pick_time=getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
        pickAddress=getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
        return_date=getIntent().getStringExtra(Constant.BundleExtras.DROP_DATE);
        return_time=getIntent().getStringExtra(Constant.BundleExtras.DROP_TIME);
        screen_type=getIntent().getStringExtra(Constant.BundleExtras.SCREEN_TYPE);
        price=getIntent().getStringExtra(Constant.BundleExtras.PRICE);
        initialization();

    }

    private void initialization() {

        setData();

        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());

        if (screen_type.equals("luxury")){
            binding.llRideCar.setVisibility(View.GONE);
            binding.cvData.setVisibility(View.GONE);
            binding.btnExtraKm.setVisibility(View.GONE);
            binding.tvPrice.setVisibility(View.GONE);
        }else {
            binding.llRideCar.setVisibility(View.VISIBLE);
            binding.cvData.setVisibility(View.VISIBLE);
            binding.btnExtraKm.setVisibility(View.VISIBLE);
            binding.tvPrice.setVisibility(View.VISIBLE);

        }

        // Set default selection
        binding.rbWithState.setChecked(true);
        binding.rbOutside.setChecked(false);
        in_out_side = "inside"; // Ensure the default value is set

        binding.rbOutside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure only one radio button is selected
                binding.rbOutside.setChecked(true);
                binding.rbWithState.setChecked(false);
                in_out_side = "outside";
            }
        });

        binding.rbWithState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure only one radio button is selected
                binding.rbWithState.setChecked(true);
                binding.rbOutside.setChecked(false);
                in_out_side = "inside";
            }
        });


        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.checkbox.isChecked()){
                    Intent intent=new Intent(SelectedCarDetailActivity.this,ConfirmBookingActivity.class);
                    intent.putExtra(Constant.BundleExtras.PRICE,price);
                    intent.putExtra(Constant.BundleExtras.PICK_DATE,pick_date);
                    intent.putExtra(Constant.BundleExtras.PICK_TIME,pick_time);
                    intent.putExtra(Constant.BundleExtras.DROP_DATE,return_date);
                    intent.putExtra(Constant.BundleExtras.DROP_TIME,return_time);
                    intent.putExtra(Constant.BundleExtras.PICK_ADDRESS,pickAddress);
                    intent.putExtra(Constant.BundleExtras.SCREEN_TYPE,screen_type);
                    intent.putExtra(Constant.BundleExtras.IN_OUT_SIDE,in_out_side);
                    intent.putExtra(Constant.BundleExtras.CAR_DATA,new Gson().toJson(carTypeModel));
                    startActivity(intent);
                }else {
                    setMessageForSnackbar("Please accept terms and conditions",false);
                }

            }
        });

    }

    private void setData(){

        binding.tvCarName.setText(carTypeModel.getmCtypeTitle());
        binding.tvSeats.setText(carTypeModel.getmCtypeSeat()+" Seater");
        binding.tvFuel.setText("Petrol");
        binding.tvCarType.setText(carTypeModel.getmCtypeTitle());
        binding.tvPrice.setText("Rs. "+price);
        binding.tvTotalFare.setText("Rs. "+price);
        binding.tvManual.setText("Manual");
        binding.tvTravelDateTime.setText(pick_date+" "+pick_time);
        binding.tvPickupDateTime.setText(pick_date+" "+pick_time);
        binding.tvTime1.setText(pick_time);
        binding.tvDropDateTime.setText(return_date+" "+return_time);
        binding.tvDropDate.setText(return_date+" "+return_time);
        binding.tvAddress.setText(pickAddress);

        Glide.with(SelectedCarDetailActivity.this)
                .load(ImagePathDecider.getCarImagePath()+carTypeModel.getmCtypeImg())
                .error(R.drawable.demo_car)
                .into(binding.ivCar);
    }

    private void setMessageForSnackbar(String msg, boolean flag) {
        if (flag) {
            Snacky.builder()
                    .setActivity(SelectedCarDetailActivity.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {
                        //do something
                    })
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .success()
                    .show();
        } else {
            Snacky.builder()
                    .setActivity(SelectedCarDetailActivity.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {
                        //do something
                    })
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .error()
                    .show();
        }
    }
}
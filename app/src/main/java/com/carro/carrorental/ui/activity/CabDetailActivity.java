package com.carro.carrorental.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.CarTypeResponse;
import com.carro.carrorental.databinding.ActivityCabDetailBinding;
import com.carro.carrorental.listener.CarTyprClickListener;
import com.carro.carrorental.model.CarModel;
import com.carro.carrorental.model.CarTypeModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.adapter.CarTypeAdapter;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.utils.PreferenceUtils;
import com.carro.carrorental.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CabDetailActivity extends BaseActivity implements CarTyprClickListener {

    ActivityCabDetailBinding binding;
    String wayType;
    String flightType;
    String carTypeId;
    String userId;
    String carId;
    String carTypeName;
    String vendorId;
    String price;
    float total;
    String distance;
    CarTypeAdapter carTypeAdapter;
    List<CarTypeModel> carTypeModelList = new ArrayList<>();
    List<CarModel> carModelList = new ArrayList<>();

    String pick_date = "";
    String pick_time = "";
    String pickAddress = "";
    String pickLat = "";
    String pickLng = "";
    String drop_date = "";
    String drop_time = "";
    String drop_address = "";
    String dropLat = "";
    String dropLng = "";

    String map_distance = "";
    String map_duration = "";
    String branch = "";
    String hour = "";

    LoginModel loginModel = new LoginModel();

    String cabServiceType = "1";

    int day=1;


    private boolean isFabExpanded = true;
    private final Handler fabHandler = new Handler(Looper.getMainLooper());
//    private Runnable retractFabRunnable;
    private static final long RETRACT_DELAY_MS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCabDetailBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getPreferenceData();
    }

    private void getPreferenceData() {
        userId = PreferenceUtils.getString(Constant.PreferenceConstant.USER_ID, CabDetailActivity.this);
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, CabDetailActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);


        hour = PreferenceUtils.getString(Constant.PreferenceConstant.HOUR_TYPE, CabDetailActivity.this);


        wayType = getIntent().getStringExtra(Constant.BundleExtras.WAY_TYPE);
        flightType = getIntent().getStringExtra(Constant.BundleExtras.FLIGHT_TYPE);
        pickAddress = getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
        pick_date = getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
        pick_time = getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
        pickLat = getIntent().getStringExtra(Constant.BundleExtras.LAT_PICK);
        pickLng = getIntent().getStringExtra(Constant.BundleExtras.LNG_PICK);
        drop_address = getIntent().getStringExtra(Constant.BundleExtras.DROP_ADDRESS);
        drop_date = getIntent().getStringExtra(Constant.BundleExtras.DROP_DATE);
        drop_time = getIntent().getStringExtra(Constant.BundleExtras.DROP_TIME);
        dropLat = getIntent().getStringExtra(Constant.BundleExtras.LAT_DROP);
        dropLng = getIntent().getStringExtra(Constant.BundleExtras.LNG_DROP);

        map_distance = getIntent().getStringExtra(Constant.BundleExtras.MAP_DISTANCE);
        map_duration = getIntent().getStringExtra(Constant.BundleExtras.MAP_DURATION);
        branch = getIntent().getStringExtra(Constant.BundleExtras.BRANCH_ID);
        cabServiceType = getIntent().getStringExtra(Constant.BundleExtras.CAB_SERVICE_TYPE);



        checkUserData();

        initialization();
    }

    private void checkUserData() {

        if (!loginModel.getmCustImg().isEmpty()) {
            setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());
        }
    }

    private void initialization() {

        binding.tvPickAddress.setText(pickAddress + " - " + drop_address);

        getCarTypeApi();

        setupRetractingFab();




        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Intent intent = new Intent(CabDetailActivity.this, PickupDetailsActivity.class);
                intent.putExtra(Constant.BundleExtras.WAY_TYPE, wayType);
                intent.putExtra(Constant.BundleExtras.CAB_SERVICE_TYPE,cabServiceType);
                intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
                intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
                intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
                intent.putExtra(Constant.BundleExtras.DROP_ADDRESS, drop_address);
                intent.putExtra(Constant.BundleExtras.DROP_DATE, drop_date);
                intent.putExtra(Constant.BundleExtras.DROP_TIME, drop_time);
                intent.putExtra(Constant.BundleExtras.C_TYPE_ID, carTypeId);
                intent.putExtra(Constant.BundleExtras.C_TYPE_NAME, carTypeName);
                intent.putExtra(Constant.BundleExtras.CAR_ID, carId);
                intent.putExtra(Constant.BundleExtras.VENDOR_ID, vendorId);
                intent.putExtra(Constant.BundleExtras.PRICE, price);
                intent.putExtra(Constant.BundleExtras.BRANCH_ID, branch);     */


                Intent intent = new Intent(CabDetailActivity.this, PaymentDetailsActivity.class);
                intent.putExtra(Constant.BundleExtras.WAY_TYPE, wayType);
                intent.putExtra(Constant.BundleExtras.CAB_SERVICE_TYPE, cabServiceType);
                intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
                intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
                intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
                intent.putExtra(Constant.BundleExtras.LAT_PICK, pickLat);
                intent.putExtra(Constant.BundleExtras.LNG_PICK, pickLng);
                intent.putExtra(Constant.BundleExtras.DROP_ADDRESS, drop_address);
                intent.putExtra(Constant.BundleExtras.DROP_DATE, drop_date);
                intent.putExtra(Constant.BundleExtras.DROP_TIME, drop_time);
                intent.putExtra(Constant.BundleExtras.LAT_DROP, dropLat);
                intent.putExtra(Constant.BundleExtras.LNG_DROP, dropLng);
                intent.putExtra(Constant.BundleExtras.C_TYPE_ID, carTypeId);
                intent.putExtra(Constant.BundleExtras.C_TYPE_NAME, carTypeName);
                intent.putExtra(Constant.BundleExtras.CAR_ID, carId);
                intent.putExtra(Constant.BundleExtras.VENDOR_ID, vendorId);
                intent.putExtra(Constant.BundleExtras.PRICE, price);
                intent.putExtra(Constant.BundleExtras.B_NAME, loginModel.getmCustName());
                intent.putExtra(Constant.BundleExtras.B_MOBILE, loginModel.getmCustMobile());
                intent.putExtra(Constant.BundleExtras.B_EMAIL, loginModel.getmCustEmail());
                intent.putExtra(Constant.BundleExtras.BRANCH_ID, branch);
                intent.putExtra(Constant.BundleExtras.FLIGHT_TYPE, flightType);

                startActivity(intent);
            }
        });


        binding.tvInclusions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvInclusions.setBackgroundColor(ContextCompat.getColor(CabDetailActivity.this, R.color.primary_field));
                binding.tvExclusions.setBackgroundColor(ContextCompat.getColor(CabDetailActivity.this, R.color.white));
                binding.tvTC.setBackgroundColor(ContextCompat.getColor(CabDetailActivity.this, R.color.white));
//                webView(Html.fromHtml(carTypeModelList.get(0).getmCtypeInclusion()).toString());
                binding.webView.setText(Html.fromHtml(carTypeModelList.get(0).getmCtypeInclusion()).toString());


            }
        });

        binding.tvExclusions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvExclusions.setBackgroundColor(ContextCompat.getColor(CabDetailActivity.this, R.color.primary_field));
                binding.tvInclusions.setBackgroundColor(ContextCompat.getColor(CabDetailActivity.this, R.color.white));
                binding.tvTC.setBackgroundColor(ContextCompat.getColor(CabDetailActivity.this, R.color.white));
//                webView(Html.fromHtml(carTypeModelList.get(0).getmCtypeExclusion()).toString());
                binding.webView.setText(Html.fromHtml(carTypeModelList.get(0).getmCtypeExclusion()).toString());

            }
        });
        binding.tvTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvTC.setBackgroundColor(ContextCompat.getColor(CabDetailActivity.this, R.color.primary_field));
                binding.tvInclusions.setBackgroundColor(ContextCompat.getColor(CabDetailActivity.this, R.color.white));
                binding.tvExclusions.setBackgroundColor(ContextCompat.getColor(CabDetailActivity.this, R.color.white));
//                webView(Html.fromHtml(carTypeModelList.get(0).getmCtypeTc()).toString());
                binding.webView.setText(Html.fromHtml(carTypeModelList.get(0).getmCtypeTc()).toString());

            }
        });


    }

    private void openDialor(String phoneNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(dialIntent);
    }

    private void getCarTypeApi() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CarTypeResponse> call;
        switch (cabServiceType) {
            case "1":
                call = apiInterface.cabCityRide(branch, hour);
                break;
            case "2":
                call = apiInterface.cabOneWay(branch);
                break;
            case "3":
                call = apiInterface.cabOutstation(branch);
                break;
            case "4":
                call = apiInterface.cabFlight(branch);
                break;
            default:
                call = apiInterface.cabCityRide(branch, hour);
                break;
        }


//        Call<CarTypeResponse> call = apiInterface.cab_service(branch);
        call.enqueue(new Callback<CarTypeResponse>() {
            @Override
            public void onResponse(Call<CarTypeResponse> call, Response<CarTypeResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            carTypeModelList.clear();
                            carTypeModelList.addAll(response.body().getData());

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CabDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.rvCabs.setLayoutManager(linearLayoutManager);
                            carTypeAdapter = new CarTypeAdapter(CabDetailActivity.this, carTypeModelList, CabDetailActivity.this);
                            binding.rvCabs.setAdapter(carTypeAdapter);
                            carTypeAdapter.notifyDataSetChanged();

                        } else {
                            hideLoader();
                            binding.lvNoData.setVisibility(View.VISIBLE);
                            binding.lvMain.setVisibility(View.GONE);

                        }
                    } else {
                        hideLoader();
                        binding.lvNoData.setVisibility(View.VISIBLE);
                        binding.lvMain.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    hideLoader();
                    e.printStackTrace();
                    binding.lvNoData.setVisibility(View.VISIBLE);
                    binding.lvMain.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CarTypeResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
                binding.lvNoData.setVisibility(View.VISIBLE);
                binding.lvMain.setVisibility(View.GONE);
            }
        });


    }


    private void setDataOnViews(CarTypeModel carTypeModel) {

        //  webView(Html.fromHtml(carTypeModel.getmCtypeInclusion()).toString());
        binding.webView.setText(Html.fromHtml(carTypeModel.getmCtypeInclusion()).toString());

        PreferenceUtils.setString(Constant.PreferenceConstant.WEBVIEW_INC, Html.fromHtml(carTypeModelList.get(0).getmCtypeInclusion().toString()).toString(), CabDetailActivity.this);
        PreferenceUtils.setString(Constant.PreferenceConstant.WEBVIEW_EXC, Html.fromHtml(carTypeModelList.get(0).getmCtypeExclusion().toString()).toString(), CabDetailActivity.this);
        PreferenceUtils.setString(Constant.PreferenceConstant.WEBVIEW_TC, Html.fromHtml(carTypeModelList.get(0).getmCtypeTc().toString()).toString(), CabDetailActivity.this);


        binding.tvCarName.setText(carTypeModel.getmCtypeTitle());
        binding.tvVehicleSeats.setText(carTypeModel.getmCtypeSeat() + " Seats");
        binding.tvVehicleBags.setText(carTypeModel.getmCtypeLuggage() + " Bags");


        if (map_distance != null && !map_distance.isEmpty()) {

//            String numberStr = map_distance.replace("km", "").trim();
            distance = map_distance.replaceAll("[^\\d.]", "");


            if (cabServiceType.equals("3")) {
                distance = String.valueOf(Float.parseFloat(distance) * 2);
            }

            PreferenceUtils.setString(Constant.PreferenceConstant.MAP_DISTANCE, distance, CabDetailActivity.this);
            PreferenceUtils.setString(Constant.PreferenceConstant.MAP_DURATION, map_duration, CabDetailActivity.this);


            if (cabServiceType.equals("1")) {
                total = Float.parseFloat(carTypeModel.getpPrice());


                PreferenceUtils.setString(Constant.PreferenceConstant.KM_PRICE, String.valueOf(Float.parseFloat(carTypeModel.getpPrice()) / Float.parseFloat(distance)), CabDetailActivity.this);


            } else {
                day=Utils.calculateDays(pick_date,pick_time,drop_date,drop_time);
                total = Float.parseFloat(carTypeModel.getmCtypePrice()) * Float.parseFloat(distance)/**day*/;
                if(wayType.equals("2")){
                    int tDay=calculateDays(pick_date,drop_date);
                    if(tDay>1) {
                        total = Float.parseFloat(carTypeModel.getmCtypePrice()) * (300 * tDay);
                        day=tDay;
                    }
                }
                PreferenceUtils.setString(Constant.PreferenceConstant.KM_PRICE, carTypeModel.getmCtypePrice(), CabDetailActivity.this);
            }


            double offertotal = total + ((total * 10) / 100);


            String formattedPrice = String.format("%.2f", total);
            String formattedOfferPrice = String.format("%.2f", offertotal);
            binding.tvPrice.setText("\u20B9" + formattedPrice);
            binding.tvOfferPrice.setText("\u20B9" + formattedOfferPrice);
            binding.tvOfferPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            price = formattedPrice;


            binding.tvPackageDetails.setText(Utils.formatMapDuration(map_duration) + " | " + distance +" Km");

        } else {
            Toast.makeText(CabDetailActivity.this, "Please Select Valid Destination Range", Toast.LENGTH_SHORT).show();
            getOnBackPressedDispatcher().onBackPressed();
        }


        Glide.with(CabDetailActivity.this)
                .load(ImagePathDecider.getCarImagePath() + carTypeModel.getmCtypeImg())
                .error(R.drawable.demo_car)
                .into(binding.ivVehicle);
    }
    public static int calculateDays(String pickDate, String dropDate) {
        try {
            // Define the date format (dd-MM-yyyy)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate start = LocalDate.parse(pickDate, formatter);
            LocalDate end = LocalDate.parse(dropDate, formatter);

            // Difference in days + 1 (inclusive)
            long days = ChronoUnit.DAYS.between(start, end) + 1;

            return (int) days;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    public void onCarTypeClick(CarTypeModel carTypeModel) {
        carTypeId = carTypeModel.getmCtypeId();
        carTypeName = carTypeModel.getmCtypeTitle();
        binding.tvFuel.setText(carTypeModel.getmCtypeFuel());

        setDataOnViews(carTypeModel);
//        getCarApi();
    }


    private void setupRetractingFab() {

//        retractFabRunnable = this::retractFab;

       /* binding.btnHelp.setOnClickListener(v -> {
            if (isFabExpanded) {

                openDialor("18001020802");

                // We also stop the timer so it doesn't hide right after being clicked
                fabHandler.removeCallbacks(retractFabRunnable);
            } else {
                expandFab();
            }
        });*/

        startFabRetractTimer();
    }

    private void startFabRetractTimer() {
        // Remove any previously scheduled retraction
//        fabHandler.removeCallbacks(retractFabRunnable);
        // Schedule a new retraction
//        fabHandler.postDelayed(retractFabRunnable, RETRACT_DELAY_MS);
    }

    /*private void expandFab() {
        if (isFabExpanded) return;

        isFabExpanded = true;
        startFabRetractTimer();

        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.btnHelp.getLayoutParams();

        ValueAnimator animator = ValueAnimator.ofInt(params.getMarginEnd(), dpToPx(15));
        animator.addUpdateListener(valueAnimator -> {
            params.setMarginEnd((Integer) valueAnimator.getAnimatedValue());
            binding.btnHelp.setLayoutParams(params);
        });
        animator.setDuration(300);
        animator.start();
    }*/

   /* private void retractFab() {
        if (!isFabExpanded) return;
        isFabExpanded = false;
        fabHandler.removeCallbacks(retractFabRunnable);
        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.btnHelp.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.getMarginEnd(), dpToPx(-120));
        animator.addUpdateListener(valueAnimator -> {
            params.setMarginEnd((Integer) valueAnimator.getAnimatedValue());
            binding.btnHelp.setLayoutParams(params);
        });
        animator.setDuration(300);
        animator.start();
    }*/

  /*  private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
*/
    @Override
    protected void onPause() {
        super.onPause();
//        fabHandler.removeCallbacks(retractFabRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFabExpanded) {
            startFabRetractTimer();
        }
    }


}
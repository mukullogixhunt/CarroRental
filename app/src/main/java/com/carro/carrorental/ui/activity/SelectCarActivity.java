package com.carro.carrorental.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.BookingResponse;
import com.carro.carrorental.api.response.CarTypeResponse;
import com.carro.carrorental.api.response.PackageResponse;
import com.carro.carrorental.databinding.ActivitySelectCarBinding;
import com.carro.carrorental.databinding.SelectPackageDialogBinding;
import com.carro.carrorental.listener.CarTyprClickListener;
import com.carro.carrorental.listener.PackageClickListener;
import com.carro.carrorental.model.BookingModel;
import com.carro.carrorental.model.CarTypeModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.PackageModel;
import com.carro.carrorental.ui.adapter.CarAdapter;
import com.carro.carrorental.ui.adapter.SelectPackageAdapter;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.PreferenceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCarActivity extends BaseActivity implements CarTyprClickListener, PackageClickListener {

    ActivitySelectCarBinding binding;
    CarAdapter carAdapter;
    List<CarTypeModel> carTypeModelList = new ArrayList<>();
    Dialog dialog;
    Dialog dialog1;
    CarTypeModel carTypeModel = new CarTypeModel();
    List<BookingModel> bookingModelList = new ArrayList<>();
    LoginModel loginModel = new LoginModel();
    String pick_date = "";
    String pick_date2 = "";
    String return_date2 = "";
    String pick_time = "";
    String pickAddress = "";
    String return_date = "";
    String return_time = "";
    String screen_type = "";
    String branch_id = "";

    private SelectPackageAdapter selectPackageAdapter;
    private List<PackageModel> packageModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectCarBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getUserPreferences();
        updateLastValidDateTime();
    }

    private void getUserPreferences() {
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, SelectCarActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        branch_id = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_ID, SelectCarActivity.this);
        pick_date = getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
        pick_time = getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
        pickAddress = getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
        return_date = getIntent().getStringExtra(Constant.BundleExtras.DROP_DATE);
        return_time = getIntent().getStringExtra(Constant.BundleExtras.DROP_TIME);
        screen_type = getIntent().getStringExtra(Constant.BundleExtras.SCREEN_TYPE);

        initialization();

    }

    private void initialization() {
        if (screen_type.equals("luxury")) {
            getLuxuryCarApi();
            binding.cvData.setVisibility(View.VISIBLE);
            binding.tvTravelDateTime.setText(pick_date + " " + pick_time);
            binding.tvDropDateTime.setText(return_date + " " + return_time);

            binding.llPickDate.setOnClickListener(v -> openDatePickerDialog("pick"));
            binding.llDropDate.setOnClickListener(v -> openDatePickerDialog("drop"));

        } else {
//            getSelfCarApi();
            binding.cvData.setVisibility(View.VISIBLE);
            binding.tvTravelDateTime.setText(pick_date + " " + pick_time);
            binding.tvDropDateTime.setText(return_date + " " + return_time);
//            binding.tvPickupLocation.setText(pickAddress);
        }
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());

    }



    private void getLuxuryCarApi() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CarTypeResponse> call = apiInterface.luxury_service(branch_id);
        call.enqueue(new Callback<CarTypeResponse>() {
            @Override
            public void onResponse(Call<CarTypeResponse> call, Response<CarTypeResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            carTypeModelList.clear();
                            carTypeModelList.addAll(response.body().getData());

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectCarActivity.this, LinearLayoutManager.VERTICAL, false);
                            binding.rvCar.setLayoutManager(linearLayoutManager);
                            carAdapter = new CarAdapter(SelectCarActivity.this, carTypeModelList, SelectCarActivity.this);
                            binding.rvCar.setAdapter(carAdapter);

                            carAdapter.notifyDataSetChanged();

                        } else {
                            hideLoader();
                            binding.lvNoData.setVisibility(View.VISIBLE);
                        }
                    } else {
                        hideLoader();
                        binding.lvNoData.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    hideLoader();
                    e.printStackTrace();
                    binding.lvNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<CarTypeResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
                binding.lvNoData.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public void onCarTypeClick(CarTypeModel carTypeModel) {
        this.carTypeModel = carTypeModel;
        if (screen_type.equals("luxury")) {
            //insertLuxuryBookingApi();
            Intent intent = new Intent(SelectCarActivity.this, SelectedCarDetailActivity.class);
            intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
            intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
            intent.putExtra(Constant.BundleExtras.DROP_DATE, return_date);
            intent.putExtra(Constant.BundleExtras.DROP_TIME, return_time);
            intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
            intent.putExtra(Constant.BundleExtras.SCREEN_TYPE, screen_type);
            intent.putExtra(Constant.BundleExtras.PRICE, carTypeModel.getmCtypePrice());
            intent.putExtra(Constant.BundleExtras.CAR_DATA, new Gson().toJson(carTypeModel));
            startActivity(intent);
        } else {
            selectPackageDialog();
        }

    }

    private void selectPackageDialog() {
        SelectPackageDialogBinding selectPackageDialogBinding;
        selectPackageDialogBinding = SelectPackageDialogBinding.inflate(getLayoutInflater());
        dialog = new Dialog(SelectCarActivity.this, R.style.my_dialog);
        dialog.setContentView(selectPackageDialogBinding.getRoot());
        dialog.setCancelable(true);
        dialog.create();
        dialog.show();

        getPackage();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectCarActivity.this, LinearLayoutManager.VERTICAL, false);
        selectPackageDialogBinding.rvPackage.setLayoutManager(linearLayoutManager);
        selectPackageAdapter = new SelectPackageAdapter(SelectCarActivity.this, packageModelList, SelectCarActivity.this);
        selectPackageDialogBinding.rvPackage.setAdapter(selectPackageAdapter);


    }

    private void getPackage() {

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


                            packageModelList.clear();
                            packageModelList.addAll(response.body().getData());

                            selectPackageAdapter.notifyDataSetChanged();

                        } else {
                            hideLoader();

                        }
                    } else {
                        hideLoader();

                    }
                } catch (Exception e) {
                    hideLoader();
//                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);

                }

            }

            @Override
            public void onFailure(Call<PackageResponse> call, Throwable t) {
                hideLoader();
                // Log error here since request failed
                Log.e("Failure", t.toString());

//                showError("Something went wrong");
            }
        });
    }


    @Override
    public void onPackageClick(PackageModel packageModel) {
        PreferenceUtils.setString(Constant.PreferenceConstant.PACKAGE_ID, packageModel.getmPackageId(), SelectCarActivity.this);
        Intent intent = new Intent(SelectCarActivity.this, SelectedCarDetailActivity.class);
        intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
        intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
        intent.putExtra(Constant.BundleExtras.DROP_DATE, return_date);
        intent.putExtra(Constant.BundleExtras.DROP_TIME, return_time);
        intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
        intent.putExtra(Constant.BundleExtras.SCREEN_TYPE, screen_type);
        intent.putExtra(Constant.BundleExtras.PRICE, packageModel.getmPackagePrice());
        intent.putExtra(Constant.BundleExtras.CAR_DATA, new Gson().toJson(carTypeModel));
        startActivity(intent);
    }

    private final Calendar myCalendar = Calendar.getInstance();

    private void openDatePickerDialog(String type) {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String formattedDate = DateFormater.getDate(myCalendar.getTimeInMillis(), Constant.ddMMyyyy);
            if ("pick".equals(type)) {
                pick_date = formattedDate;
            } else {
                return_date = formattedDate;
            }
            openTimePicker(type);
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void openTimePicker(String type) {
        final Calendar c = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minuteOfHour);
                    SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    String formattedTime = format.format(calendar.getTime());
                    if ("pick".equals(type)) {
                        pick_time = formattedTime;
                    } else {
                        return_time = formattedTime;
                    }
                    validateAndRefresh();
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    private void validateAndRefresh() {
        if (pick_date.isEmpty() || pick_time.isEmpty() || return_date.isEmpty() || return_time.isEmpty()) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);
        try {
            Date pickupDateTime = sdf.parse(pick_date + " " + pick_time);
            Date returnDateTime = sdf.parse(return_date + " " + return_time);

            if (returnDateTime.before(pickupDateTime)) {
                new AlertDialog.Builder(this).setTitle("Invalid Dates").setMessage("Return date must be after the pickup date. Reverting to last valid time.").setPositiveButton("OK", (dialog, which) -> revertToLastValidDateTime()).setCancelable(false).show();
                return;
            }

            long durationInMillis = returnDateTime.getTime() - pickupDateTime.getTime();
            long twelveHoursInMillis = 12 * 60 * 60 * 1000;
            if (durationInMillis < twelveHoursInMillis) {
                new AlertDialog.Builder(this).setTitle("Minimum Duration").setMessage("The minimum booking duration is 12 hours. Reverting to last valid time.").setPositiveButton("OK", (dialog, which) -> revertToLastValidDateTime()).setCancelable(false).show();
                return;
            }

            updateLastValidDateTime();
            updateDateTimeAndDurationUI();

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing dates.", Toast.LENGTH_SHORT).show();
        }
    }
    private String last_valid_pick_date = "";
    private String last_valid_pick_time = "";
    private String last_valid_return_date = "";
    private String last_valid_return_time = "";
    private void updateLastValidDateTime() {
        last_valid_pick_date = pick_date;
        last_valid_pick_time = pick_time;
        last_valid_return_date = return_date;
        last_valid_return_time = return_time;
    }

    private void revertToLastValidDateTime() {
        pick_date = last_valid_pick_date;
        pick_time = last_valid_pick_time;
        return_date = last_valid_return_date;
        return_time = last_valid_return_time;
        updateDateTimeAndDurationUI();
    }

    private void updateDateTimeAndDurationUI() {
        binding.tvTravelDateTime.setText(String.format("%s %s", pick_date, pick_time));
        binding.tvDropDateTime.setText(String.format("%s %s", return_date, return_time));

       /* Utils.DurationResult result = Utils.calculateDuration(pick_date, pick_time, return_date, return_time);
        binding.tvDuration.setText(String.format("Duration : %d Days, %d Hours, %d Minutes", result.days, result.hours, result.minutes));
        totalDurationInMinutes = (result.days * 24 * 60) + (result.hours * 60) + result.minutes;


        refreshCarList();*/
    }
}
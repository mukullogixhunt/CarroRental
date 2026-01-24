//
//
//package com.logixhunt.carrorental.ui.activity;
//
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import com.bumptech.glide.Glide;
//import com.google.gson.Gson;
//import com.logixhunt.carrorental.R;
//import com.logixhunt.carrorental.api.ApiClient;
//import com.logixhunt.carrorental.api.ApiInterface;
//import com.logixhunt.carrorental.api.response.SelfCarResponse;
//import com.logixhunt.carrorental.api.response.commonResponse.BaseResponse;
//import com.logixhunt.carrorental.databinding.ActivitySelectSelfCarBinding;
//import com.logixhunt.carrorental.listener.SelfCarClickListener;
//import com.logixhunt.carrorental.model.LoginModel;
//import com.logixhunt.carrorental.model.SelfCarModel;
//import com.logixhunt.carrorental.ui.adapter.SelfCarAdapter;
//import com.logixhunt.carrorental.ui.common.BaseActivity;
//import com.logixhunt.carrorental.utils.Constant;
//import com.logixhunt.carrorental.utils.DateFormater;
//import com.logixhunt.carrorental.utils.ImagePathDecider;
//import com.logixhunt.carrorental.utils.PreferenceUtils;
//import com.logixhunt.carrorental.utils.Utils;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class SelectSelfCarActivity extends BaseActivity implements SelfCarClickListener {
//
//    private ActivitySelectSelfCarBinding binding;
//    private LoginModel loginModel = new LoginModel();
//    private String pick_date = "";
//    private String pick_time = "";
//    private String return_date = "";
//    private String return_time = "";
//
//    // Variables to store the last known valid state
//    private String last_valid_pick_date = "";
//    private String last_valid_pick_time = "";
//    private String last_valid_return_date = "";
//    private String last_valid_return_time = "";
//
//    private String pickAddress = "";
//    private String book_type = "";
//    private String branch_id = "";
//
//    private String days = "";
//    private String hour = "";
//    private String minutes = "";
//    private SelfCarAdapter selfCarAdapter;
//    private List<SelfCarModel> carTypeModelList = new ArrayList<>();
//    private SelfCarModel selfCarModel = new SelfCarModel();
//    private String finalHour = "";
//
//    private final Calendar myCalendar = Calendar.getInstance();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivitySelectSelfCarBinding.inflate(getLayoutInflater());
//        EdgeToEdge.enable(this);
//        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        getUserPreferences();
//    }
//
//    private void getUserPreferences() {
//        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, SelectSelfCarActivity.this);
//        loginModel = new Gson().fromJson(userData, LoginModel.class);
//        branch_id = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_ID, SelectSelfCarActivity.this);
//        pick_date = getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
//        pick_time = getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
//        pickAddress = getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
//        return_date = getIntent().getStringExtra(Constant.BundleExtras.DROP_DATE);
//        return_time = getIntent().getStringExtra(Constant.BundleExtras.DROP_TIME);
//        book_type = getIntent().getStringExtra(Constant.BundleExtras.BOOK_TYPE);
//
//        // Store initial valid dates
//        updateLastValidDateTime();
//        initialization();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // The list will be refreshed automatically when dates are changed,
//        // so no need for an extra API call here.
//    }
//
//    private void initialization() {
//        updateDateTimeAndDurationUI();
//
//        binding.ivBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
//        binding.ivUser.setOnClickListener(v -> {
//            Intent intent = new Intent(SelectSelfCarActivity.this, ProfileDetailActivity.class);
//            startActivity(intent);
//        });
//
//        binding.llPickDate.setOnClickListener(v -> openDatePickerDialog("pick"));
//        binding.llDropDate.setOnClickListener(v -> openDatePickerDialog("drop"));
//    }
//
//    private void updateDateTimeAndDurationUI() {
//        binding.tvTravelDateTime.setText(String.format("%s %s", pick_date, pick_time));
//        binding.tvDropDateTime.setText(String.format("%s %s", return_date, return_time));
//
//        Utils.DurationResult result = Utils.calculateDuration(pick_date, pick_time, return_date, return_time);
//        days = String.valueOf(result.days);
//        hour = String.valueOf(result.hours);
//        minutes = String.valueOf(result.minutes);
//        binding.tvDuration.setText(String.format("Duration : %s Days, %s Hours, %s Minutes", days, hour, minutes));
//
//        Glide.with(this)
//                .load(ImagePathDecider.getUserImagePath() + loginModel.getmCustImg())
//                .error(R.drawable.img_no_profile)
//                .into(binding.ivUser);
//
//        refreshCarList(); // Call this to load/reload the car list with current dates
//    }
//
//    private void validateAndRefresh() {
//        if (pick_date.isEmpty() || pick_time.isEmpty() || return_date.isEmpty() || return_time.isEmpty()) {
//            return; // Not all data is available yet
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);
//        try {
//            Date pickupDateTime = sdf.parse(pick_date + " " + pick_time);
//            Date returnDateTime = sdf.parse(return_date + " " + return_time);
//
//            // Check if drop-off is before pickup
//            if (returnDateTime.before(pickupDateTime)) {
//                new AlertDialog.Builder(this)
//                        .setTitle("Invalid Dates")
//                        .setMessage("Return date must be after the pickup date. Reverting to last valid time.")
//                        .setPositiveButton("OK", (dialog, which) -> revertToLastValidDateTime())
//                        .setCancelable(false)
//                        .show();
//                return;
//            }
//
//            // Check for minimum 12-hour duration
//            long durationInMillis = returnDateTime.getTime() - pickupDateTime.getTime();
//            long durationInHours = durationInMillis / (60 * 60 * 1000);
//            if (durationInHours < 12) {
//                new AlertDialog.Builder(this)
//                        .setTitle("Minimum Duration")
//                        .setMessage("The minimum booking duration is 12 hours. Reverting to last valid time.")
//                        .setPositiveButton("OK", (dialog, which) -> revertToLastValidDateTime())
//                        .setCancelable(false)
//                        .show();
//                return;
//            }
//
//            // If all checks pass, this is the new valid state. Update and refresh.
//            updateLastValidDateTime();
//            updateDateTimeAndDurationUI();
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error parsing dates.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void updateLastValidDateTime() {
//        last_valid_pick_date = pick_date;
//        last_valid_pick_time = pick_time;
//        last_valid_return_date = return_date;
//        last_valid_return_time = return_time;
//    }
//
//    private void revertToLastValidDateTime() {
//        pick_date = last_valid_pick_date;
//        pick_time = last_valid_pick_time;
//        return_date = last_valid_return_date;
//        return_time = last_valid_return_time;
//        updateDateTimeAndDurationUI();
//    }
//
//    private void refreshCarList() {
//        if ("rent".equals(book_type)) {
//            binding.llTop.setVisibility(View.VISIBLE);
//            getSelfCarApi();
//        } else {
//            binding.llTop.setVisibility(View.GONE);
//            getSelfSubsCarApi();
//        }
//    }
//
//    private void openDatePickerDialog(String type) {
//        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
//            myCalendar.set(Calendar.YEAR, year);
//            myCalendar.set(Calendar.MONTH, monthOfYear);
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            String formattedDate = DateFormater.getDate(myCalendar.getTimeInMillis(), Constant.ddMMyyyy);
//            if ("pick".equals(type)) {
//                pick_date = formattedDate;
//            } else {
//                return_date = formattedDate;
//            }
//            openTimePicker(type);
//        };
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        datePickerDialog.show();
//    }
//
//    private void openTimePicker(String type) {
//        final Calendar c = Calendar.getInstance();
//        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
//                (view, hourOfDay, minuteOfHour) -> {
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                    calendar.set(Calendar.MINUTE, minuteOfHour);
//                    SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
//                    String formattedTime = format.format(calendar.getTime());
//                    if ("pick".equals(type)) {
//                        pick_time = formattedTime;
//                    } else {
//                        return_time = formattedTime;
//                    }
//                    validateAndRefresh();
//                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
//        timePickerDialog.show();
//    }
//
//    private void getSelfCarApi() {
//        showLoader();
//
//        int oneDays = 24;
//        String totalHour = String.valueOf(Integer.parseInt(days) * oneDays);
//        finalHour = String.valueOf(Integer.parseInt(hour) + Integer.parseInt(totalHour));
//
//        String pickDate = Utils.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, pick_date);
//        String pickTime = Utils.changeDateFormat(Constant.HHMMSSA, Constant.HHMMSS, pick_time);
//        String dropDate = Utils.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, return_date);
//        String dropTime = Utils.changeDateFormat(Constant.HHMMSSA, Constant.HHMMSS, return_time);
//
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<SelfCarResponse> call = apiInterface.self_service(branch_id, pickDate, pickTime, dropDate, dropTime);
//        call.enqueue(new Callback<SelfCarResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<SelfCarResponse> call, @NonNull Response<SelfCarResponse> response) {
//                hideLoader();
//                try {
//                    if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
//                        binding.lvNoData.setVisibility(View.GONE);
//                        carTypeModelList.clear();
//                        carTypeModelList.addAll(response.body().getData());
//
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectSelfCarActivity.this, LinearLayoutManager.VERTICAL, false);
//                        binding.rvCar.setLayoutManager(linearLayoutManager);
//                        selfCarAdapter = new SelfCarAdapter(SelectSelfCarActivity.this, carTypeModelList, SelectSelfCarActivity.this, "rent", Integer.parseInt(finalHour));
//                        binding.rvCar.setAdapter(selfCarAdapter);
//                    } else {
//                        binding.lvNoData.setVisibility(View.VISIBLE);
//                        carTypeModelList.clear();
//                        if (selfCarAdapter != null) selfCarAdapter.notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
//                    hideLoader();
//                    e.printStackTrace();
//                    binding.lvNoData.setVisibility(View.VISIBLE);
//                }
//            }
//            @Override
//            public void onFailure(@NonNull Call<SelfCarResponse> call, @NonNull Throwable t) {
//                hideLoader();
//                Log.e("Failure", t.toString());
//                showError("Something went wrong");
//                binding.lvNoData.setVisibility(View.VISIBLE);
//            }
//        });
//    }
//
//    private void getSelfSubsCarApi() {
//        showLoader();
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<SelfCarResponse> call = apiInterface.self_service_subcription(branch_id);
//        call.enqueue(new Callback<SelfCarResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<SelfCarResponse> call, @NonNull Response<SelfCarResponse> response) {
//                hideLoader();
//                try {
//                    if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
//                        binding.lvNoData.setVisibility(View.GONE);
//                        carTypeModelList.clear();
//                        carTypeModelList.addAll(response.body().getData());
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectSelfCarActivity.this, LinearLayoutManager.VERTICAL, false);
//                        binding.rvCar.setLayoutManager(linearLayoutManager);
//                        selfCarAdapter = new SelfCarAdapter(SelectSelfCarActivity.this, carTypeModelList, SelectSelfCarActivity.this, "sub", 1);
//                        binding.rvCar.setAdapter(selfCarAdapter);
//                    } else {
//                        binding.lvNoData.setVisibility(View.VISIBLE);
//                        carTypeModelList.clear();
//                        if (selfCarAdapter != null) selfCarAdapter.notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
//                    hideLoader();
//                    e.printStackTrace();
//                    binding.lvNoData.setVisibility(View.VISIBLE);
//                }
//            }
//            @Override
//            public void onFailure(@NonNull Call<SelfCarResponse> call, @NonNull Throwable t) {
//                hideLoader();
//                Log.e("Failure", t.toString());
//                showError("Something went wrong");
//                binding.lvNoData.setVisibility(View.VISIBLE);
//            }
//        });
//    }
//
//    private void showErrorDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("Oops")
//                .setMessage("This car is not available for the selected duration!")
//                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
//                .create()
//                .show();
//    }
//
//    private void checkCarApi(String carId) {
//        showLoader();
//        String fromDate = Utils.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, pick_date);
//        String toDate = Utils.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, return_date);
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<BaseResponse> call = apiInterface.check_booked_list(carId, fromDate, toDate);
//        call.enqueue(new Callback<BaseResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
//                hideLoader();
//                try {
//                    if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
//                        Intent intent = new Intent(SelectSelfCarActivity.this, SelfCarDetailActivity.class);
//                        intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
//                        intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
//                        intent.putExtra(Constant.BundleExtras.DROP_DATE, return_date);
//                        intent.putExtra(Constant.BundleExtras.DROP_TIME, return_time);
//                        intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
//                        intent.putExtra(Constant.BundleExtras.BOOK_TYPE, book_type);
//                        intent.putExtra(Constant.BundleExtras.CAR_DATA, new Gson().toJson(selfCarModel));
//                        startActivity(intent);
//                    } else {
//                        showErrorDialog();
//                    }
//                } catch (Exception e) {
//                    hideLoader();
//                    e.printStackTrace();
//                    showErrorDialog();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
//                hideLoader();
//                Log.e("Failure", t.toString());
//                showError("Something went wrong");
//                showErrorDialog();
//            }
//        });
//    }
//
//    @Override
//    public void onSelfCarClick(SelfCarModel selfCarModel) {
//        this.selfCarModel = selfCarModel;
//        if ("rent".equals(book_type)) {
////            checkCarApi(selfCarModel.getmCtypeId());
//
//
//            Intent intent = new Intent(SelectSelfCarActivity.this, SelfCarDetailActivity.class);
//            intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
//            intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
//            intent.putExtra(Constant.BundleExtras.DROP_DATE, return_date);
//            intent.putExtra(Constant.BundleExtras.DROP_TIME, return_time);
//            intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
//            intent.putExtra(Constant.BundleExtras.BOOK_TYPE, book_type);
//            intent.putExtra(Constant.BundleExtras.CAR_DATA, new Gson().toJson(selfCarModel));
//            startActivity(intent);
//
//        } else {
//
//
//            String availDate = selfCarModel.getAvailDate();
//            String futureDate = DateFormater.getFutureDateFrom(availDate, 2);
//
//
//            Intent intent = new Intent(SelectSelfCarActivity.this, SelfCarDetailActivity.class);
//            intent.putExtra(Constant.BundleExtras.AVAIL_DATE, futureDate);
//            intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
//            intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
//            intent.putExtra(Constant.BundleExtras.DROP_DATE, return_date);
//            intent.putExtra(Constant.BundleExtras.DROP_TIME, return_time);
//            intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
//            intent.putExtra(Constant.BundleExtras.BOOK_TYPE, book_type);
//            intent.putExtra(Constant.BundleExtras.CAR_DATA, new Gson().toJson(selfCarModel));
//            startActivity(intent);
//        }
//    }
//}




package com.carro.carrorental.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.SelfCarResponse;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.databinding.ActivitySelectSelfCarBinding;
import com.carro.carrorental.listener.SelfCarClickListener;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.SelfCarModel;
import com.carro.carrorental.ui.adapter.SelfCarAdapter;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.PreferenceUtils;
import com.carro.carrorental.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectSelfCarActivity extends BaseActivity implements SelfCarClickListener {

    private ActivitySelectSelfCarBinding binding;
    private LoginModel loginModel = new LoginModel();
    private String pick_date = "";
    private String pick_time = "";
    private String return_date = "";
    private String return_time = "";

    private String last_valid_pick_date = "";
    private String last_valid_pick_time = "";
    private String last_valid_return_date = "";
    private String last_valid_return_time = "";

    private String pickAddress = "";
    private String book_type = "";
    private String branch_id = "";

    private SelfCarAdapter selfCarAdapter;
    private List<SelfCarModel> carTypeModelList = new ArrayList<>();
    private SelfCarModel selfCarModel = new SelfCarModel();
    private long totalDurationInMinutes = 0;

    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectSelfCarBinding.inflate(getLayoutInflater());
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
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        branch_id = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_ID, this);
        pick_date = getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
        pick_time = getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
        pickAddress = getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
        return_date = getIntent().getStringExtra(Constant.BundleExtras.DROP_DATE);
        return_time = getIntent().getStringExtra(Constant.BundleExtras.DROP_TIME);
        book_type = getIntent().getStringExtra(Constant.BundleExtras.BOOK_TYPE);

        updateLastValidDateTime();
        initialization();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initialization() {
        updateDateTimeAndDurationUI();
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());
        binding.llPickDate.setOnClickListener(v -> openDatePickerDialog("pick"));
        binding.llDropDate.setOnClickListener(v -> openDatePickerDialog("drop"));
    }

    private void updateDateTimeAndDurationUI() {
        binding.tvTravelDateTime.setText(String.format("%s %s", pick_date, pick_time));
        binding.tvDropDateTime.setText(String.format("%s %s", return_date, return_time));

        Utils.DurationResult result = Utils.calculateDuration(pick_date, pick_time, return_date, return_time);
        binding.tvDuration.setText(String.format("Duration : %d Days, %d Hours, %d Minutes", result.days, result.hours, result.minutes));
        totalDurationInMinutes = (result.days * 24 * 60) + (result.hours * 60) + result.minutes;


        refreshCarList();
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

    private void refreshCarList() {
        if ("rent".equals(book_type)) {
            binding.llTop.setVisibility(View.VISIBLE);
            getSelfCarApi();
        } else {
            binding.llTop.setVisibility(View.GONE);
            getSelfSubsCarApi();
        }
    }

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

    private void getSelfCarApi() {
        showLoader();
        String pickDate = Utils.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, pick_date);
        String pickTime = Utils.changeDateFormat(Constant.HHMMSSA, Constant.HHMMSS, pick_time);
        String dropDate = Utils.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, return_date);
        String dropTime = Utils.changeDateFormat(Constant.HHMMSSA, Constant.HHMMSS, return_time);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SelfCarResponse> call = apiInterface.self_service(branch_id, pickDate, pickTime, dropDate, dropTime);
        call.enqueue(new Callback<SelfCarResponse>() {
            @Override
            public void onResponse(@NonNull Call<SelfCarResponse> call, @NonNull Response<SelfCarResponse> response) {
                hideLoader();
                try {
                    if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                        binding.lvNoData.setVisibility(View.GONE);
                        carTypeModelList.clear();
                        carTypeModelList.addAll(response.body().getData());
                        binding.rvCar.setLayoutManager(new LinearLayoutManager(SelectSelfCarActivity.this));
                        selfCarAdapter = new SelfCarAdapter(SelectSelfCarActivity.this, carTypeModelList, SelectSelfCarActivity.this, "rent", totalDurationInMinutes);
                        binding.rvCar.setAdapter(selfCarAdapter);
                    } else {
                        binding.lvNoData.setVisibility(View.VISIBLE);
                        carTypeModelList.clear();
                        if (selfCarAdapter != null) selfCarAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    hideLoader(); e.printStackTrace(); binding.lvNoData.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@NonNull Call<SelfCarResponse> call, @NonNull Throwable t) {
                hideLoader(); Log.e("Failure", t.toString()); showError("Something went wrong"); binding.lvNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getSelfSubsCarApi() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SelfCarResponse> call = apiInterface.self_service_subcription(branch_id);
        call.enqueue(new Callback<SelfCarResponse>() {
            @Override
            public void onResponse(@NonNull Call<SelfCarResponse> call, @NonNull Response<SelfCarResponse> response) {
                hideLoader();
                try {
                    if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                        binding.lvNoData.setVisibility(View.GONE);
                        carTypeModelList.clear();
                        carTypeModelList.addAll(response.body().getData());
                        binding.rvCar.setLayoutManager(new LinearLayoutManager(SelectSelfCarActivity.this));
                        selfCarAdapter = new SelfCarAdapter(SelectSelfCarActivity.this, carTypeModelList, SelectSelfCarActivity.this, "sub", 0);
                        binding.rvCar.setAdapter(selfCarAdapter);
                    } else {
                        binding.lvNoData.setVisibility(View.VISIBLE);
                        carTypeModelList.clear();
                        if (selfCarAdapter != null) selfCarAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    hideLoader(); e.printStackTrace(); binding.lvNoData.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@NonNull Call<SelfCarResponse> call, @NonNull Throwable t) {
                hideLoader(); Log.e("Failure", t.toString()); showError("Something went wrong"); binding.lvNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this).setTitle("Oops").setMessage("This car is not available for the selected duration!").setPositiveButton("Close", (dialog, which) -> dialog.dismiss()).create().show();
    }

    private void checkCarApi(String carId) {
        showLoader();
        String fromDate = Utils.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, pick_date);
        String toDate = Utils.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, return_date);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiInterface.check_booked_list(carId, fromDate, toDate);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                hideLoader();
                try {
                    if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                        // All checks passed, navigate to the detail screen
                        navigateToDetailScreen();
                    } else {
                        showErrorDialog();
                    }
                } catch (Exception e) {
                    hideLoader(); e.printStackTrace(); showErrorDialog();
                }
            }
            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                hideLoader(); Log.e("Failure", t.toString()); showError("Something went wrong"); showErrorDialog();
            }
        });
    }

    @Override
    public void onSelfCarClick(SelfCarModel selfCarModel) {
        this.selfCarModel = selfCarModel;
        if ("rent".equals(book_type)) {
            // It's good practice to check availability before proceeding.
            // If you want to skip this check as per your original code,
            // you can directly call navigateToDetailScreen().
            checkCarApi(selfCarModel.getmCtypeId());
        } else {
            // For subscriptions, we navigate directly.
            navigateToDetailScreen();
        }
    }

    private void navigateToDetailScreen() {
        Intent intent = new Intent(SelectSelfCarActivity.this, SelfCarDetailActivity.class);
        intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
        intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
        intent.putExtra(Constant.BundleExtras.DROP_DATE, return_date);
        intent.putExtra(Constant.BundleExtras.DROP_TIME, return_time);
        intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
        intent.putExtra(Constant.BundleExtras.BOOK_TYPE, book_type);
        intent.putExtra(Constant.BundleExtras.CAR_DATA, new Gson().toJson(selfCarModel));

        // --- THIS IS THE CRITICAL ADDITION ---
        // Pass the total duration in minutes for accurate price calculation on the next screen
        intent.putExtra(Constant.BundleExtras.TOTAL_MINUTES, totalDurationInMinutes);

        // This is only relevant for subscriptions
        if ("sub".equals(book_type)) {
            String availDate = selfCarModel.getAvailDate();
            String futureDate = DateFormater.getFutureDateFrom(availDate, 2);
            intent.putExtra(Constant.BundleExtras.AVAIL_DATE, futureDate);
        }

        startActivity(intent);
    }
}
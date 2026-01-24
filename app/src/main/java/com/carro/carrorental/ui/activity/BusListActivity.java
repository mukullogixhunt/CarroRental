package com.carro.carrorental.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.carro.carrorental.api.response.BusResponse;
import com.carro.carrorental.databinding.ActivityBusListBinding;
import com.carro.carrorental.listener.BusClickListener;
import com.carro.carrorental.model.BusModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.adapter.BusAdapter;
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

public class BusListActivity extends BaseActivity implements BusClickListener {

    ActivityBusListBinding binding;
    List<BusModel> busModelList = new ArrayList<>();
    LoginModel loginModel = new LoginModel();
    BusAdapter busAdapter;
    String pickDate = "";
    String pickTime = "";
    String address = "";
    String branchId = "";
    String branchName = "";
    String returnDate = "";
    String returnTime = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusListBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getPreference();
        updateLastValidDateTime();
    }

    private void getPreference(){
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, BusListActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        address = getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
        pickDate = getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
        pickTime = getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
        returnDate = getIntent().getStringExtra(Constant.BundleExtras.RETURN_DATE);
        returnTime = getIntent().getStringExtra(Constant.BundleExtras.RETURN_TIME);
        branchId = getIntent().getStringExtra(Constant.BundleExtras.BRANCH_ID);
        branchName = getIntent().getStringExtra(Constant.BundleExtras.BRANCH_NAME);

        initialization();
    }

    private void initialization() {
        setData();
        getBusApi();
        setUpToolBar(binding.toolbar,this,loginModel.getmCustImg());

    }

    private void setData() {
        binding.tvTravelDateTime.setText(pickDate + " " + pickTime);
        binding.tvDropDateTime.setText(String.format("%s %s", returnDate, returnTime));
//        binding.tvBranch.setText(branchName);
        binding.llPickDate.setOnClickListener(v -> openDatePickerDialog("pick"));
        binding.llDropDate.setOnClickListener(v -> openDatePickerDialog("drop"));
//        binding.tvPickupLocation.setText(address);
    }

    private void getBusApi() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BusResponse> call = apiInterface.bus(branchId);
        call.enqueue(new Callback<BusResponse>() {
            @Override
            public void onResponse(Call<BusResponse> call, Response<BusResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            busModelList.clear();
                            busModelList.addAll(response.body().getData());

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BusListActivity.this, LinearLayoutManager.VERTICAL, false);
                            binding.rvBus.setLayoutManager(linearLayoutManager);
                            busAdapter = new BusAdapter(BusListActivity.this, busModelList, BusListActivity.this);
                            binding.rvBus.setAdapter(busAdapter);

                            busAdapter.notifyDataSetChanged();

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
            public void onFailure(Call<BusResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
                binding.lvNoData.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public void onBusClick(BusModel busModel) {
        Intent intent = new Intent(BusListActivity.this, BusDetailActivity.class);
        intent.putExtra(Constant.BundleExtras.BUS_DATA, new Gson().toJson(busModel));
        intent.putExtra(Constant.BundleExtras.PICK_DATE, pickDate);
        intent.putExtra(Constant.BundleExtras.PICK_TIME, pickTime);
        intent.putExtra(Constant.BundleExtras.RETURN_DATE, returnDate);
        intent.putExtra(Constant.BundleExtras.RETURN_TIME, returnTime);
        intent.putExtra(Constant.BundleExtras.BRANCH_ID, branchId);
        intent.putExtra(Constant.BundleExtras.BRANCH_NAME, branchName);
        intent.putExtra(Constant.BundleExtras.PICK_ADDRESS,address);
        startActivity(intent);

    }
    private final Calendar myCalendar = Calendar.getInstance();
    private void validateAndRefresh() {
        if (pickDate.isEmpty() || pickTime.isEmpty() || returnDate.isEmpty() || returnTime.isEmpty()) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);
        try {
            Date pickupDateTime = sdf.parse(pickDate + " " + pickTime);
            Date returnDateTime = sdf.parse(returnDate + " " + returnTime);

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
        last_valid_pick_date = pickDate;
        last_valid_pick_time = pickTime;
        last_valid_return_date = returnDate;
        last_valid_return_time = returnTime;
    }

    private void revertToLastValidDateTime() {
        pickDate = last_valid_pick_date;
        pickTime = last_valid_pick_time;
        returnDate = last_valid_return_date;
        returnTime = last_valid_return_time;
        updateDateTimeAndDurationUI();
    }

    private void updateDateTimeAndDurationUI() {
        binding.tvTravelDateTime.setText(String.format("%s %s", pickDate, pickTime));
        binding.tvDropDateTime.setText(String.format("%s %s", returnDate, returnTime));

        Utils.DurationResult result = Utils.calculateDuration(pickDate, pickTime, returnDate, returnTime);

    }

    private void openDatePickerDialog(String type) {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String formattedDate = DateFormater.getDate(myCalendar.getTimeInMillis(), Constant.ddMMyyyy);
            if ("pick".equals(type)) {
                pickDate = formattedDate;
            } else {
                returnDate = formattedDate;
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
                        pickTime = formattedTime;
                    } else {
                        returnTime = formattedTime;
                    }
                    validateAndRefresh();
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }
}
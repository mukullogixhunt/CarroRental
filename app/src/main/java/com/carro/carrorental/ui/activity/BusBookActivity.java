package com.carro.carrorental.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.CityResponse;
import com.carro.carrorental.api.response.StateResponse;
import com.carro.carrorental.databinding.ActivityBusBookBinding;
import com.carro.carrorental.model.BranchModel;
import com.carro.carrorental.model.CityModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.StateModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusBookActivity extends BaseActivity {

    ActivityBusBookBinding binding;
    LoginModel loginModel = new LoginModel();
    List<BranchModel> branchModelList = new ArrayList<>();
    private final Calendar myCalendar = Calendar.getInstance();
    List<StateModel> stateModelList = new ArrayList<>();
    List<CityModel> cityModelList = new ArrayList<>();
    String pick_date = "";
    String pick_time = "";
    String return_date = "";
    String return_time = "";
    String branchId = "";
    String branch_name = "";
    String state = "";
    String state_name = "";
    String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBusBookBinding.inflate(getLayoutInflater());
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

        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, BusBookActivity.this);
        branchId = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_ID, BusBookActivity.this);
        branch_name = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_NAME, BusBookActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);

        initialization();
    }

    private void initialization(){
        getStateAPi();
        getCityAPi("CT");
//        getBranchAPi();
        setUpToolBar(binding.toolbar,this,loginModel.getmCustImg());

       /* Glide.with(BusBookActivity.this)
                .load(ImagePathDecider.getUserImagePath()+loginModel.getmCustImg())
                .error(R.drawable.img_no_profile)
                .into(binding.ivUser);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });*/
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick_date=binding.tvTravelDate.getText().toString();
                pick_time=binding.tvTravelTime.getText().toString();
                return_date=binding.tvReturnDate.getText().toString();
                return_time=binding.tvReturnTime.getText().toString();

                if (validate()){
                    Intent intent=new Intent(BusBookActivity.this,BusListActivity.class);
                    intent.putExtra(Constant.BundleExtras.PICK_DATE,pick_date);
                    intent.putExtra(Constant.BundleExtras.PICK_TIME,pick_time);
                    intent.putExtra(Constant.BundleExtras.RETURN_DATE,return_date);
                    intent.putExtra(Constant.BundleExtras.RETURN_TIME,return_time);
                    intent.putExtra(Constant.BundleExtras.BRANCH_ID, branchId);
                    intent.putExtra(Constant.BundleExtras.BRANCH_NAME,branch_name);
                    intent.putExtra(Constant.BundleExtras.PICK_ADDRESS,state_name+","+city);
                    startActivity(intent);
                }

            }
        });

        binding.lvTravelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(binding.tvTravelDate,"pick");

            }
        });

//        binding.llTravelTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openTimePicker(binding.tvTravelTime);
//
//            }
//        });

        binding.lvReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(binding.tvReturnDate,"return");

            }
        });

//        binding.llReturnTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openTimePicker(binding.tvReturnTime);
//
//            }
//        });


//        binding.lvPickLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SelfDriveActivity.this, SearchLocationActivity.class);
//                intent.putExtra(Constant.BundleExtras.SCREEN_TYPE, "self");
//                startActivity(intent);
//            }
//        });
    }

    private void openTimePicker(TextView textView) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(BusBookActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        // Create a Calendar instance to set the hour and minute
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        // Format the time to 12-hour format with AM/PM
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        String formattedTime = format.format(calendar.getTime());

                        // Set the formatted time to the TextView
                        textView.setText(formattedTime);

                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private void openDatePickerDialog(TextView textView,String type) {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                textView.setText(DateFormater.getDate(myCalendar.getTimeInMillis(), Constant.ddMMyyyy));
                if (type.equals("pick")){
                    openTimePicker(binding.tvTravelTime);
                }else {
                    openTimePicker(binding.tvReturnTime);
                }

            }

        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(BusBookActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


    private void getStateAPi() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<StateResponse> call = apiInterface.get_country_state("IN");
        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                //  hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            stateModelList.clear();
                            stateModelList.add(new StateModel("Select State"));
                            stateModelList.addAll(response.body().getData());


                            ArrayAdapter<StateModel> itemAdapter = new ArrayAdapter<>(BusBookActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, stateModelList);
                            itemAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                            binding.spState.setAdapter(itemAdapter);

                            binding.spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    state = stateModelList.get(position).getIso2();
                                    state_name = stateModelList.get(position).getName();
                                    getCityAPi(state);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                        } else {
                        }
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {



            }
        });
    }
    private void getCityAPi(String iso) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CityResponse> call = apiInterface.get_state_cities("IN",iso);
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                //  hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            cityModelList.clear();
                            cityModelList.add(new CityModel("Select City"));
                            cityModelList.addAll(response.body().getData());


                            ArrayAdapter<CityModel> itemAdapter = new ArrayAdapter<>(BusBookActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cityModelList);
                            itemAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                            binding.spCity.setAdapter(itemAdapter);

                            binding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    city = cityModelList.get(position).getName();


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                        } else {
                        }
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {



            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        String pickDate = binding.tvTravelDate.getText().toString();
        String pickTime = binding.tvTravelTime.getText().toString();
        String returnDate = binding.tvTravelTime.getText().toString();
        String returnTime = binding.tvTravelTime.getText().toString();

        // Validate Pick Date
        if (pickDate.isEmpty()) {
            binding.tvTravelDate.setError("Please Enter Pick Date..!");
            valid = false;
        }

        // Validate Pick Time
        if (pickTime.isEmpty()) {
            binding.tvTravelTime.setError("Please Enter Pick Time..!");
            valid = false;
        }

        // Validate return Date
        if (returnDate.isEmpty()) {
            binding.tvReturnDate.setError("Please Enter Return Date..!");
            valid = false;
        }

        // Validate return Time
        if (returnTime.isEmpty()) {
            binding.tvReturnTime.setError("Please Enter Return Time..!");
            valid = false;
        }

//        if (binding.spState.getSelectedItemPosition() == 0) {
//            Toast.makeText(this, "Please select a state", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (binding.spCity.getSelectedItemPosition() == 0) {
//            Toast.makeText(this, "Please select a city", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (binding.spBranch.getSelectedItemPosition() == 0) {
//            Toast.makeText(this, "Please select a branch", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return valid;
    }
}
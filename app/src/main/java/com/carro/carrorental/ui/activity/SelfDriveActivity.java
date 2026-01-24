package com.carro.carrorental.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.BranchResponse;
import com.carro.carrorental.api.response.CityResponse;
import com.carro.carrorental.api.response.StateResponse;
import com.carro.carrorental.databinding.ActivitySelfDriveBinding;
import com.carro.carrorental.databinding.ErrorDialogBinding;
import com.carro.carrorental.model.BranchModel;
import com.carro.carrorental.model.CityModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.StateModel;
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

public class SelfDriveActivity extends BaseActivity {

    ActivitySelfDriveBinding binding;
    LoginModel loginModel = new LoginModel();
    private final Calendar myCalendar = Calendar.getInstance();
    List<BranchModel> branchModelList = new ArrayList<>();
    List<StateModel> stateModelList = new ArrayList<>();
    List<CityModel> cityModelList = new ArrayList<>();
    String pick_date = "";
    String pick_time = "";
    String pickAddress = "";
    String return_date = "";
    String return_time = "";
    String branch = "";
    String state = "";
    String state_name = "";
    String city = "";
    String currentt_date = "";
    String currentt_time = "";
    String bookType="rent";
    Dialog dialog;
    String days = "";
    String hour = "";
    String minutes = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelfDriveBinding.inflate(getLayoutInflater());
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

        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, SelfDriveActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);

        initialization();
    }

    private void initialization() {
        getStateAPi();
        getCityAPi("CT");
        getBranchAPi();
        setCurrentDate();

        setUpToolBar(binding.toolbar,this,loginModel.getmCustImg());

        binding.cardRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookType="rent";
                binding.cardRental.setCardBackgroundColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.primary));
                binding.cardSubscribe.setCardBackgroundColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.white));
                binding.tvRent.setTextColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.white));
                binding.tvRentDesc.setTextColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.white));
                binding.tvSub.setTextColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.black));
                binding.tvSubDesc.setTextColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.black));

            }
        });
        binding.cardSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookType="sub";
                binding.cardRental.setCardBackgroundColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.white));
                binding.cardSubscribe.setCardBackgroundColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.primary));
                binding.tvRent.setTextColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.black));
                binding.tvRentDesc.setTextColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.black));
                binding.tvSub.setTextColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.white));
                binding.tvSubDesc.setTextColor(ContextCompat.getColor(SelfDriveActivity.this, R.color.white));

                Intent intent = new Intent(SelfDriveActivity.this, SelectSelfCarActivity.class);
                intent.putExtra(Constant.BundleExtras.PICK_DATE, "0");
                intent.putExtra(Constant.BundleExtras.PICK_TIME, "0");
                intent.putExtra(Constant.BundleExtras.DROP_DATE, "0");
                intent.putExtra(Constant.BundleExtras.DROP_TIME, "0");
                intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, "0");
                intent.putExtra(Constant.BundleExtras.BOOK_TYPE, bookType);
                startActivity(intent);

            }
        });


        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAddress = city + "," + state_name;
                pick_date = binding.tvTravelDate.getText().toString();
                return_date = binding.tvReturnDate.getText().toString();
                pick_time = binding.tvTravelTime.getText().toString();
                return_time = binding.tvReturnTime.getText().toString();
                if (validate()) {
                    try {
                        // Check if the pick time is between 12 AM and 4 AM
                        if (isPickTimeRestricted(pick_time)) {
                            // Show a dialog if the time is restricted
                            showAlertMessageDialog();
                            return; // Don't proceed if time is restricted
                        }

                        // Check if the pick time is at least 1 hour ahead of the current time
                        if (!isPickTimeValid(pick_date, pick_time)) {
                            showTimeRestrictedDialog();
                            return; // Don't proceed if pick time is invalid
                        }

                        Utils.DurationResult result = Utils.calculateDuration(pick_date, pick_time, return_date, return_time);
                        days=String.valueOf(result.days);
                        hour=String.valueOf(result.hours);
                        minutes=String.valueOf(result.minutes);

                        long totalHours = result.days * 24 + result.hours;
                        long totalMinutes = totalHours * 60 + result.minutes;

                        // Check if total duration is less than 12 hours (720 minutes)
                        if (totalMinutes < 720) {
                            calculateHourDialog(); // Show dialog for insufficient duration
                            return; // Don't proceed if duration is less than 12 hours
                        }

                        Intent intent = new Intent(SelfDriveActivity.this, SelectSelfCarActivity.class);
                        intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
                        intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
                        intent.putExtra(Constant.BundleExtras.DROP_DATE, return_date);
                        intent.putExtra(Constant.BundleExtras.DROP_TIME, return_time);
                        intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
                        intent.putExtra(Constant.BundleExtras.BOOK_TYPE, bookType);
                        startActivity(intent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        binding.lvTravelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(binding.tvTravelDate,"pick");

            }
        });
        binding.lvReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(binding.tvReturnDate,"drop");

            }
        });
        binding.llTravelTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(binding.tvTravelTime);

            }
        });
        binding.llReturnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(binding.tvReturnTime);

            }
        });

    }

    private void setCurrentDate() {
        // Get the current date and time
        Date currentDate = new Date();

        // Format for date: dd-MM-yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);
        currentt_date = formattedDate;

        // Format for time: hh:mma (e.g., 02:30PM)
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = timeFormat.format(currentDate);
        currentt_time = formattedTime;
    }

    private boolean isPickTimeValid(String pickDate, String pickTime) throws ParseException {
        // Combine the picked date and time into one string
        String pickedDateTime = pickDate + " " + pickTime;

        // Format to parse the date and time (e.g., "dd-MM-yyyy hh:mm a")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());

        // Parse the picked date and time into a Date object
        Date pickedDate = dateFormat.parse(pickedDateTime);

        // Get the current date and time
        Date currentDate = new Date();

        // Calculate the difference in milliseconds
        long differenceInMillis = pickedDate.getTime() - currentDate.getTime();

        // Convert the difference to hours
        long differenceInHours = differenceInMillis / (60 * 60 * 1000); // 1 hour = 60 * 60 * 1000 milliseconds

        // Return true if the picked time is at least 1 hour ahead of the current time
        return differenceInHours >= 1;
    }

    private boolean isPickTimeRestricted(String pickTime) throws ParseException {
        // Define the time format
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        // Parse the selected pick time
        Date selectedTime = timeFormat.parse(pickTime);

        // Extract the hours and minutes from the selected time
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTime(selectedTime);
        int selectedHour = selectedCalendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
        int selectedMinute = selectedCalendar.get(Calendar.MINUTE);

        // Check if the selected time is between 12:00 AM and 4:00 AM
        // 12:00 AM is represented as 0 hours, and 4:00 AM is represented as 4 hours in 24-hour format
        if (selectedHour >= 0 && selectedHour < 4) {
            return true; // Restricted time (between 12:00 AM and 4:00 AM)
        }

        return false; // Not restricted
    }


    private void showTimeRestrictedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SelfDriveActivity.this);
        builder.setTitle("Opps");
        builder.setMessage("Pick time must be at least 1 hour ahead of the current time!");
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void calculateHourDialog() {
        String duration=" Selected Duration : "+hour+" Hours";
        AlertDialog.Builder builder = new AlertDialog.Builder(SelfDriveActivity.this);
        builder.setTitle("Opps\n");
        builder.setMessage(duration+"\n\nCan't Book for less then 12 hr!");
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showAlertMessageDialog() {
        ErrorDialogBinding errorDialogBinding;
        errorDialogBinding = ErrorDialogBinding.inflate(getLayoutInflater());
        dialog = new Dialog(SelfDriveActivity.this, R.style.my_dialog);
        dialog.setContentView(errorDialogBinding.getRoot());
        dialog.setCancelable(true);
        dialog.create();
        dialog.show();

        errorDialogBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void openTimePicker(TextView textView) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(SelfDriveActivity.this,
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(SelfDriveActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void getBranchAPi() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BranchResponse> call = apiInterface.get_branch();
        call.enqueue(new Callback<BranchResponse>() {
            @Override
            public void onResponse(Call<BranchResponse> call, Response<BranchResponse> response) {
                //  hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            branchModelList.clear();
                            branchModelList.add(new BranchModel("Select Branch"));
                            branchModelList.addAll(response.body().getData());


                            ArrayAdapter<BranchModel> itemAdapter = new ArrayAdapter<>(SelfDriveActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, branchModelList);
                            itemAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                            binding.spBranch.setAdapter(itemAdapter);

                            binding.spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    branch = branchModelList.get(position).getmBranchId();
                                    PreferenceUtils.setString(Constant.PreferenceConstant.BRANCH_ID, branch, SelfDriveActivity.this);

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
            public void onFailure(Call<BranchResponse> call, Throwable t) {
                showError("something went wrong");


            }
        });
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


                            ArrayAdapter<StateModel> itemAdapter = new ArrayAdapter<>(SelfDriveActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, stateModelList);
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
        Call<CityResponse> call = apiInterface.get_state_cities("IN", iso);
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

                            ArrayAdapter<CityModel> itemAdapter = new ArrayAdapter<>(SelfDriveActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cityModelList);
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
        String dropDate = binding.tvReturnDate.getText().toString();
        String dropTime = binding.tvReturnTime.getText().toString();

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
        // Validate Drop Date
        if (dropDate.isEmpty()) {
            binding.tvReturnDate.setError("Please Enter Return Date..!");
            valid = false;
        }

        // Validate Drop Time
        if (dropTime.isEmpty()) {
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
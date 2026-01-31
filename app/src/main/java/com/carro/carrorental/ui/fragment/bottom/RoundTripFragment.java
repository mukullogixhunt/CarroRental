package com.carro.carrorental.ui.fragment.bottom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.MapsApiClient;
import com.carro.carrorental.api.response.RouteMatrixResponse;
import com.carro.carrorental.databinding.BookCabBottomDialogBinding;
import com.carro.carrorental.databinding.FragmentRoundTripBinding;
import com.carro.carrorental.model.BranchModel;
import com.carro.carrorental.model.DistanceModel;
import com.carro.carrorental.ui.activity.CabDetailActivity;
import com.carro.carrorental.ui.activity.SearchLocationActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.PreferenceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoundTripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoundTripFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final Calendar myCalendar = Calendar.getInstance();
    FragmentRoundTripBinding binding;
    Dialog dialog;
    String pick_date = "";
    String pick_time = "";
    String pickAddress = "";
    String drop_date = "";
    String drop_time = "";
    String drop_address = "";
    String addressType = "";
    String addressMainPick = "";
    String addressMainDrop = "";
    String latPick = "";
    String lngPick = "";
    String latDrop = "";
    String lngDrop = "";
    String mapDistance;
    int mapDistanceValue;
    String mapDuration;
    String branchId = "";
    List<BranchModel> branchModelList = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public RoundTripFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoundTripFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoundTripFragment newInstance(String param1, String param2) {
        RoundTripFragment fragment = new RoundTripFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            addressType = getArguments().getString("address_type");
            addressMainPick = getArguments().getString("address_main_pick");
            addressMainDrop = getArguments().getString("address_main_drop");

            if (getArguments().getString("lat_pick") != null) {
                latPick = getArguments().getString("lat_pick");
                PreferenceUtils.setString(Constant.PreferenceConstant.PICK_LAT, latPick, getContext());
            }

            if (getArguments().getString("lng_pick") != null) {
                lngPick = getArguments().getString("lng_pick");
                PreferenceUtils.setString(Constant.PreferenceConstant.PICK_LNG, lngPick, getContext());
            }

            if (getArguments().getString("lat_drop") != null) {
                latDrop = getArguments().getString("lat_drop");
                PreferenceUtils.setString(Constant.PreferenceConstant.DROP_LAT, latDrop, getContext());
            }

            if (getArguments().getString("lng_drop") != null) {
                lngDrop = getArguments().getString("lng_drop");
                PreferenceUtils.setString(Constant.PreferenceConstant.DROP_LNG, lngDrop, getContext());
            }

            if (addressMainPick != null) {
                PreferenceUtils.setString(Constant.PreferenceConstant.PICKUP_LOCATION, addressMainPick, getContext());
            }
            if (addressMainDrop != null) {
                PreferenceUtils.setString(Constant.PreferenceConstant.DROP_LOCATION, addressMainDrop, getContext());
            }


            // Use the values as needed
            Log.d("FragmentBundle", "Address Type: " + addressType);
            Log.d("FragmentBundle", "Address From: " + addressMainPick);
            Log.d("FragmentBundle", "Address From: " + addressMainDrop);
        }
        // Inflate the layout for this fragment
        binding = FragmentRoundTripBinding.inflate(getLayoutInflater());
        initialization();
        return binding.getRoot();
    }

    private void initialization() {


        branchId = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_ID, requireContext());


//        getBranchAPi();
        binding.tvPickupLocation.setText(PreferenceUtils.getString(Constant.PreferenceConstant.PICKUP_LOCATION, getContext()));
        binding.tvDropLocation.setText(PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LOCATION, getContext()));

        pickAddress = binding.tvPickupLocation.getText().toString();
        drop_address = binding.tvDropLocation.getText().toString();


        binding.lvPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchLocationActivity.class);
                intent.putExtra(Constant.BundleExtras.INPUT_TYPE, "1");
                intent.putExtra(Constant.BundleExtras.FROM_FRAGMENT, "3");
                intent.putExtra(Constant.BundleExtras.SCREEN_TYPE, "frag");
                startActivity(intent);

                // bottomDialogPickLocation();
            }
        });

        binding.llDropLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchLocationActivity.class);
                intent.putExtra(Constant.BundleExtras.INPUT_TYPE, "2");
                intent.putExtra(Constant.BundleExtras.FROM_FRAGMENT, "3");
                intent.putExtra(Constant.BundleExtras.SCREEN_TYPE, "frag");
                startActivity(intent);

                // bottomDialogPickLocation();
            }
        });

        binding.llJourneyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(binding.tvJourneyDate, "pick");


            }
        });


        binding.llJourneyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Only open time picker if a date has been selected
                if (!binding.tvJourneyDate.getText().toString().isEmpty()) {
                    openTimePicker(binding.tvTime);
                } else {
                    Toast.makeText(getContext(), "Please select a pick-up date first.", Toast.LENGTH_SHORT).show();
                }
                openTimePicker(binding.tvTime);

            }
        });

        binding.llReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(binding.tvReturnDate, "return");


            }
        });


        binding.llReturnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(binding.tvReturnTime);

            }
        });

        binding.btnExploreCabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    computeRouteMatrix(
                            PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LAT, getContext()) + "," +
                                    PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LNG, getContext()),
                            PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LAT, getContext()) + "," +
                                    PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LNG, getContext())
                    );
                }

            }
        });

    }

    private void bottomDialogPickLocation() {
        BookCabBottomDialogBinding bottomDialogBinding;
        bottomDialogBinding = BookCabBottomDialogBinding.inflate(getLayoutInflater());

        dialog = new BottomSheetDialog(getContext());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(bottomDialogBinding.getRoot());
        dialog.show();

        bottomDialogBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    private void openTimePicker(TextView textView) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);

                        // Get current date and time
                        Calendar currentTime = Calendar.getInstance();
                        // Get selected pick-up date
                        Calendar pickUpDate = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat(Constant.ddMMyyyy, Locale.getDefault());
                        try {
                            pickUpDate.setTime(sdf.parse(binding.tvJourneyDate.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing pick-up date.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Check if the selected date is today
                        boolean isToday = pickUpDate.get(Calendar.YEAR) == currentTime.get(Calendar.YEAR) &&
                                pickUpDate.get(Calendar.MONTH) == currentTime.get(Calendar.MONTH) &&
                                pickUpDate.get(Calendar.DAY_OF_MONTH) == currentTime.get(Calendar.DAY_OF_MONTH);

                        if (isToday) {
                            // If it's today, ensure the selected time is at least 1 hour from now
                            Calendar minTime = Calendar.getInstance();
                            minTime.add(Calendar.HOUR_OF_DAY, 1); // Add 1 hour to current time

                            if (selectedTime.before(minTime)) {
                                Toast.makeText(getContext(), "Pick-up time must be at least 1 hour from current time.", Toast.LENGTH_LONG).show();
                                return; // Do not set the time
                            }
                        }

                        // Format the time to 12-hour format with AM/PM
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        String formattedTime = format.format(selectedTime.getTime());

                        // Set the formatted time to the TextView
                        textView.setText(formattedTime);
                        pick_time = formattedTime;
                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }


    private void openDatePickerDialog(TextView textView, String type) {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                textView.setText(DateFormater.getDate(myCalendar.getTimeInMillis(), Constant.ddMMyyyy));
                if (type.equals("pick")) {
                    openTimePicker(binding.tvTime);
                } else {
                    openTimePicker(binding.tvReturnTime);
                }

            }

        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private boolean validate() {
        boolean valid = true;
        String pickLocation = binding.tvPickupLocation.getText().toString();
        String dropLocation = binding.tvDropLocation.getText().toString();
        String pickDate = binding.tvJourneyDate.getText().toString();
        String pickTime = binding.tvTime.getText().toString();
        String dropDate = binding.tvReturnDate.getText().toString();
        String dropTime = binding.tvReturnTime.getText().toString();

        // Validate From Location
        if (pickLocation.isEmpty()) {
            binding.tvPickupLocation.setError("Please Enter Pick Location..!");
            valid = false;
        }

        // Validate To Location
        if (dropLocation.isEmpty()) {
            binding.tvDropLocation.setError("Please Enter Drop Location..!");
            valid = false;
        }

        // Validate Pick Date
        if (pickDate.isEmpty()) {
            binding.tvJourneyDate.setError("Please Enter Pick Date..!");
            valid = false;
        }

        // Validate Pick Time
        if (pickTime.isEmpty()) {
            binding.tvTime.setError("Please Enter Pick Time..!");
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

//        if (binding.spBranch.getSelectedItemPosition() == 0) {
//            Toast.makeText(getContext(), "Please select a branch", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return valid;
    }

    private void computeRouteMatrix(String origins, String destinations) {


        ApiInterface service = MapsApiClient.getClient().create(ApiInterface.class);

        // Make the API call
        Call<RouteMatrixResponse> call = service.getRouteMatrix(
                origins,
                destinations,
                Constant.DRIVING_MODE,  // Travel mode, e.g., driving, walking, etc.
                Constant.GOOGLE_MAP_API_KEY // Your Google Maps API key
        );

//  showLoader();  // Optionally show a loader while fetching data

        call.enqueue(new Callback<RouteMatrixResponse>() {
            @Override
            public void onResponse(Call<RouteMatrixResponse> call, Response<RouteMatrixResponse> response) {

                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body() != null) {

                            RouteMatrixResponse routeMatrixResponse = response.body();

                            // Example: Access the distance and duration
                            for (DistanceModel row : routeMatrixResponse.getRows()) {
                                for (DistanceModel.Element element : row.getElements()) {
                                    // Check if the distance object is not null
                                    if (element.getDistance() != null && element.getDuration() != null) {
                                        mapDistance = element.getDistance().getText();
                                        mapDistanceValue = element.getDistance().getValue();
                                        mapDuration = element.getDuration().getText();

                                        if (element.getDistance().getValue() < 150000)
                                            Toast.makeText(getContext(), "Distance must be 300km", Toast.LENGTH_SHORT).show();
                                        else
                                            showSuccessDialog("Finding the best fare for you.", getContext());

                                        // Log or update the UI with the distance and duration
                                        Log.d("RouteMatrix", "Distance: " + mapDistance + ", Duration: " + mapDuration);
                                    } else {
                                        // Handle the case where distance or duration is null
                                        Log.w("RouteMatrix", "Distance or Duration is null for this element.");
                                        Toast.makeText(getContext(), "Please Select Valid Destination Range", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }


                        } else {
                            // Handle the case where the response body is null
                            Log.e("computeRouteMatrix", "Response Body is Null. Response Code: " + response.code());
                        }

                    } else {
                        // Handle the case where the response code is not successful
                        Log.e("computeRouteMatrix", "Failed with Response Code: " + response.code());
                    }
                } catch (Exception e) {
                    Log.e("computeRouteMatrix", "Exception in onResponse", e);
                } finally {
//                hideLoader();  // Optionally hide the loader after getting the response
                }
            }

            @Override
            public void onFailure(Call<RouteMatrixResponse> call, Throwable t) {
//            hideLoader();  // Optionally hide the loader in case of failure
                Log.e("computeRouteMatrix", "onFailure: " + t.toString());
            }
        });

    }

    public void showSuccessDialog(String message, Context context) {
        dialog = new Dialog(getContext(), R.style.my_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.show();

        TextView textView = dialog.findViewById(R.id.tv_message);
        textView.setText(message);

        new CountDownTimer(2500, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                dialog.cancel();
                pick_date = binding.tvJourneyDate.getText().toString();
                drop_date = binding.tvReturnDate.getText().toString();
                pick_time = binding.tvTime.getText().toString();
                drop_time = binding.tvReturnTime.getText().toString();
                Intent intent = new Intent(getContext(), CabDetailActivity.class);
                intent.putExtra(Constant.BundleExtras.WAY_TYPE, "2");
                intent.putExtra(Constant.BundleExtras.CAB_SERVICE_TYPE, "3");
                intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
                intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
                intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
                intent.putExtra(Constant.BundleExtras.DROP_ADDRESS, drop_address);
                intent.putExtra(Constant.BundleExtras.DROP_DATE, drop_date);
                intent.putExtra(Constant.BundleExtras.DROP_TIME, drop_time);
                intent.putExtra(Constant.BundleExtras.LAT_PICK, latPick);
                intent.putExtra(Constant.BundleExtras.LNG_PICK, lngPick);
                intent.putExtra(Constant.BundleExtras.LAT_DROP, latDrop);
                intent.putExtra(Constant.BundleExtras.LNG_DROP, lngDrop);
                intent.putExtra(Constant.BundleExtras.MAP_DISTANCE, mapDistance);
                intent.putExtra(Constant.BundleExtras.MAP_DISTANCE_VALUE, mapDistanceValue);
                intent.putExtra(Constant.BundleExtras.MAP_DURATION, mapDuration);
                intent.putExtra(Constant.BundleExtras.BRANCH_ID, branchId);
                startActivity(intent);
            }
        }.start();

    }


}
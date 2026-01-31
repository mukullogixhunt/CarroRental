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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.MapsApiClient;
import com.carro.carrorental.api.response.RouteMatrixResponse;
import com.carro.carrorental.databinding.FragmentFlightBinding;
import com.carro.carrorental.model.BranchModel;
import com.carro.carrorental.model.DistanceModel;
import com.carro.carrorental.ui.activity.CabDetailActivity;
import com.carro.carrorental.ui.activity.SearchAirportsActivity;
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
 * Use the {@link FlightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlightFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FlightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FlightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FlightFragment newInstance(String param1, String param2) {
        FlightFragment fragment = new FlightFragment();
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

    FragmentFlightBinding binding;
    private final Calendar myCalendar = Calendar.getInstance();
    String pick_date = "";
    String pick_time = "";
    String pickAddress = "";
    String drop_address = "";
    String addressType = "";
    String addressMainPick = "";
    String addressMainDrop = "";

    String latPick = "";
    String lngPick = "";
    String latDrop = "";
    String lngDrop = "";

    String mapDistance;
    String mapDuration;
    String airport_name;
    Dialog dialog;
    String branchId = "";
    List<BranchModel> branchModelList = new ArrayList<>();
    boolean isFromAirport = true;

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
        binding = FragmentFlightBinding.inflate(getLayoutInflater());
        initialization();
        return binding.getRoot();
    }

    private void initialization() {

        branchId = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_ID, requireContext());
        if (PreferenceUtils.contains(Constant.PreferenceConstant.IS_FROM_AIRPORT, getContext())) {
            isFromAirport = PreferenceUtils.getBoolean(Constant.PreferenceConstant.IS_FROM_AIRPORT, requireContext());
        }

//        getBranchAPi();
        initiateFromAir();
        binding.tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromAirport = true;
                PreferenceUtils.setBoolean(Constant.PreferenceConstant.IS_FROM_AIRPORT, true, requireContext());
                binding.tvFrom.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary));
                binding.tvTo.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
                binding.tvFrom.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                binding.tvTo.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));
                binding.lldrop.setVisibility(View.VISIBLE);
                binding.llPickup.setVisibility(View.GONE);
            }
        });
        binding.tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromAirport = false;
                PreferenceUtils.setBoolean(Constant.PreferenceConstant.IS_FROM_AIRPORT, false, requireContext());
                binding.tvFrom.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
                binding.tvTo.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary));
                binding.tvFrom.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));
                binding.tvTo.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                binding.lldrop.setVisibility(View.GONE);
                binding.llPickup.setVisibility(View.VISIBLE);
            }
        });

        if (isFromAirport) {
            binding.tvFrom.performClick();
        } else {
            binding.tvTo.performClick();
        }
    }

    private void initiateFromAir() {

        binding.tvPickup.setText(PreferenceUtils.getString(Constant.PreferenceConstant.PICKUP_LOCATION, getContext()));
        binding.tvDrop.setText(PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LOCATION, getContext()));
        binding.tvAirport.setText(PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_NAME, getContext()));

//        pickAddress = binding.tvPickup.getText().toString();
        pickAddress = binding.tvAirport.getText().toString();
        drop_address = binding.tvDrop.getText().toString();

        binding.llAirport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchAirportsActivity.class);
                startActivity(intent);
            }
        });

        binding.llPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchLocationActivity.class);
                intent.putExtra(Constant.BundleExtras.INPUT_TYPE, "1");
                intent.putExtra(Constant.BundleExtras.FROM_FRAGMENT, "4");
                intent.putExtra(Constant.BundleExtras.SCREEN_TYPE, "frag");
                startActivity(intent);
            }
        });
        binding.lldrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchLocationActivity.class);
                intent.putExtra(Constant.BundleExtras.INPUT_TYPE, "2");
                intent.putExtra(Constant.BundleExtras.FROM_FRAGMENT, "4");
                intent.putExtra(Constant.BundleExtras.SCREEN_TYPE, "frag");
                startActivity(intent);
            }
        });
        binding.llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(binding.tvDate);
            }
        });


        binding.llPickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Only open time picker if a date has been selected
                if (!binding.tvDate.getText().toString().isEmpty()) {
                    openTimePicker(binding.tvTime);
                } else {
                    Toast.makeText(getContext(), "Please select a pick-up date first.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        binding.btnExploreCabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String pickupLat, pickupLng;
                    String dropLat, dropLng;
                    if (isFromAirport) {
                        pickAddress = PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_NAME, getContext());
                        pickupLat = PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_LAT, getContext());
                        pickupLng = PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_LONG, getContext());

                        drop_address = PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LOCATION, getContext());
                        dropLat = PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LAT, getContext());
                        dropLng = PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LNG, getContext());
                    } else {
                        pickAddress = PreferenceUtils.getString(Constant.PreferenceConstant.PICKUP_LOCATION, getContext());
                        pickupLat = PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LAT, getContext());
                        pickupLng = PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LNG, getContext());

                        drop_address = PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_NAME, getContext());
                        dropLat = PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_LAT, getContext());
                        dropLng = PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_LONG, getContext());
                    }
                    computeRouteMatrix(
                            pickupLat + "," + pickupLng,
                            dropLat + "," + dropLng
                    );
                }
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
                            pickUpDate.setTime(sdf.parse(binding.tvDate.getText().toString()));
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


    /*private void openTimePicker(TextView textView) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
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
                        pick_time = formattedTime;
                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }*/

    private void openDatePickerDialog(TextView textView) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                textView.setText(DateFormater.getDate(myCalendar.getTimeInMillis(), Constant.ddMMyyyy));
                pick_date = DateFormater.getDate(myCalendar.getTimeInMillis(), Constant.ddMMyyyy);
                openTimePicker(binding.tvTime);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private boolean validate() {
        boolean valid = true;

        if (isFromAirport) {
            String airportName = binding.tvAirport.getText().toString();
            String airportLat = PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_LAT, getContext());
            String airportLng = PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_LONG, getContext());
            if (airportName.isEmpty() || airportLat.isEmpty() || airportLng.isEmpty()) {
                binding.tvAirport.setError("Please Select Airport...!");
                valid = false;
            }
            String dropAddress = binding.tvDrop.getText().toString();
            String dropLat = PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LAT, getContext());
            String dropLong = PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LNG, getContext());
            if (dropAddress.isEmpty() || dropLat.isEmpty() || dropLong.isEmpty()) {
                binding.tvDrop.setError("Please Enter Drop Location..!");
                valid = false;
            }
        } else {
            String airportName = binding.tvAirport.getText().toString();
            String airportLat = PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_LAT, getContext());
            String airportLng = PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_LONG, getContext());
            if (airportName.isEmpty() || airportLat.isEmpty() || airportLng.isEmpty()) {
                binding.tvAirport.setError("Please Select Airport...!");
                valid = false;
            }
            String pickUpAddress = binding.tvPickup.getText().toString();
            String pickLat = PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LAT, getContext());
            String pickLong = PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LNG, getContext());
            if (pickUpAddress.isEmpty() || pickLat.isEmpty() || pickLong.isEmpty()) {
                binding.tvAirport.setError("Please Enter Pickup Location..!");
                valid = false;
            }
        }

        String date = binding.tvDate.getText().toString();
        String time = binding.tvTime.getText().toString();
        //validate date
        if (date.isEmpty()) {
            binding.tvDate.setError("Please Enter Pick Date..!");
            valid = false;
        }

        // Validate Pick Time
        if (time.isEmpty()) {
            binding.tvTime.setError("Please Enter Pick Time..!");
            valid = false;
        }
        return valid;
    }

    private void computeRouteMatrix(String origins, String destinations) {

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://maps.googleapis.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ApiInterface service = retrofit.create(ApiInterface.class);

        ApiInterface service = MapsApiClient.getClient().create(ApiInterface.class);

        // Define the origins and destinations
        // origins = "37.420761,-122.081356|37.403184,-122.097371";  // Multiple origins separated by |
        //destinations = "37.420999,-122.086894|37.383047,-122.044651";  // Multiple destinations separated by |

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
                                        mapDuration = element.getDuration().getText();
                                        if (element.getDistance().getValue() >= 350000) {
                                            Toast.makeText(getContext(), "Distance must be less than 350 km", Toast.LENGTH_SHORT).show();
                                        } else {
                                            showSuccessDialog("Finding the best fare for you.", getContext());

                                        }
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
                Intent intent = new Intent(getContext(), CabDetailActivity.class);
                intent.putExtra(Constant.BundleExtras.WAY_TYPE, "1");
                intent.putExtra(Constant.BundleExtras.CAB_SERVICE_TYPE, "4");

                intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
                intent.putExtra(Constant.BundleExtras.DROP_ADDRESS, drop_address);
                intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
                intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
                intent.putExtra(Constant.BundleExtras.LAT_PICK, PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LAT, getContext()));
                intent.putExtra(Constant.BundleExtras.LNG_PICK, PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LNG, getContext()));
                intent.putExtra(Constant.BundleExtras.LAT_DROP, PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LAT, getContext()));
                intent.putExtra(Constant.BundleExtras.LNG_DROP, PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LNG, getContext()));
                intent.putExtra(Constant.BundleExtras.MAP_DISTANCE, mapDistance);
                intent.putExtra(Constant.BundleExtras.MAP_DURATION, mapDuration);
                intent.putExtra(Constant.BundleExtras.BRANCH_ID, branchId);
                intent.putExtra(Constant.BundleExtras.FLIGHT_TYPE, isFromAirport ? "1" : "2");
                startActivity(intent);
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.tvPickup.setText(PreferenceUtils.getString(Constant.PreferenceConstant.PICKUP_LOCATION, getContext()));
        binding.tvDrop.setText(PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LOCATION, getContext()));
        binding.tvAirport.setText(PreferenceUtils.getString(Constant.PreferenceConstant.AIRPORT_NAME, getContext()));

    }

//    private void getBranchAPi() {
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<BranchResponse> call = apiInterface.get_branch();
//        call.enqueue(new Callback<BranchResponse>() {
//            @Override
//            public void onResponse(Call<BranchResponse> call, Response<BranchResponse> response) {
//                //  hideLoader();
//                try {
//                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
//                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
//
//                            branchModelList.clear();
//                            branchModelList.add(new BranchModel("Select Branch"));
//                            branchModelList.addAll(response.body().getData());
//
//
//                            ArrayAdapter<BranchModel> itemAdapter = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, branchModelList);
//                            itemAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//                            binding.spBranch.setAdapter(itemAdapter);
//
//                            binding.spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    branch = branchModelList.get(position).getmBranchId();
//                                    PreferenceUtils.setString(Constant.PreferenceConstant.BRANCH_ID, branch, getContext());
//
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> parent) {
//
//                                }
//                            });
//
//
//                        } else {
//                        }
//                    } else {
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BranchResponse> call, Throwable t) {
//                Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
//
//
//
//            }
//        });
//    }
}

package com.carro.carrorental.ui.fragment.bottom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.carro.carrorental.R;
import com.carro.carrorental.databinding.FragmentHourlyBinding;
import com.carro.carrorental.model.BranchModel;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HourlyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HourlyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HourlyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HourlyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HourlyFragment newInstance(String param1, String param2) {
        HourlyFragment fragment = new HourlyFragment();
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

    FragmentHourlyBinding binding;

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

    String mapDistance = "80 Km";
    String mapDuration = "8 hours 0 mins";
    Dialog dialog;

    String branchId = "";
    List<BranchModel> branchModelList = new ArrayList<>();


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
        binding = FragmentHourlyBinding.inflate(getLayoutInflater());
        initialization();
        return binding.getRoot();
    }

    private void initialization() {

        branchId = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_ID, requireContext());


//        getBranchAPi();
        binding.tvPickUp.setText(PreferenceUtils.getString(Constant.PreferenceConstant.PICKUP_LOCATION, getContext()));
        /*binding.tvDrop.setText(PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LOCATION,getContext()));*/

        pickAddress = binding.tvPickUp.getText().toString();
        /*drop_address=binding.tvDrop.getText().toString();*/

        binding.btnExploreCabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
/*                    computeRouteMatrix(
                            PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LAT, getContext())+ "," +
                                    PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LNG, getContext()),
                            PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LAT, getContext()) + "," +
                                    PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LNG, getContext())
                    );*/


                    showSuccessDialog("Finding the best fare for you.", getContext());

                }

            }
        });

        binding.llPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchLocationActivity.class);
                intent.putExtra(Constant.BundleExtras.INPUT_TYPE, "1");
                intent.putExtra(Constant.BundleExtras.FROM_FRAGMENT, "1");
                intent.putExtra(Constant.BundleExtras.SCREEN_TYPE, "frag");
                startActivity(intent);
            }
        });
        /*binding.lldrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SearchLocationActivity.class);
                intent.putExtra(Constant.BundleExtras.INPUT_TYPE, "2");
                intent.putExtra(Constant.BundleExtras.FROM_FRAGMENT, "1");
                intent.putExtra(Constant.BundleExtras.SCREEN_TYPE, "frag");
                startActivity(intent);
            }
        });*/


//        binding.llJourneyTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              openTimePicker(binding.tvJourneyTime);
//            }
//        });
        binding.lvPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(binding.tvPickDate);
            }
        });

        binding.llJourneyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Only open time picker if a date has been selected
                if (!binding.tvPickDate.getText().toString().isEmpty()) {
                    openTimePicker(binding.tvJourneyTime);
                } else {
                    Toast.makeText(getContext(), "Please select a pick-up date first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        PreferenceUtils.setString(Constant.PreferenceConstant.HOUR_TYPE, "1", getContext());

        binding.tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tv4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
                binding.tv8.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                binding.tv12.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                binding.tv4.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                binding.tv8.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                binding.tv12.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                PreferenceUtils.setString(Constant.PreferenceConstant.HOUR_TYPE, "1", getContext());
                mapDistance = "80 Km";
                mapDuration = "8 hours 0 mins";


            }
        });

        binding.tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tv8.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
                binding.tv4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                binding.tv12.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                binding.tv8.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                binding.tv4.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                binding.tv12.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                PreferenceUtils.setString(Constant.PreferenceConstant.HOUR_TYPE, "2", getContext());
                mapDistance = "100 Km";
                mapDuration = "10 hours 0 mins";

            }
        });
        binding.tv12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tv12.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
                binding.tv4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                binding.tv8.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                binding.tv12.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                binding.tv4.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                binding.tv8.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                PreferenceUtils.setString(Constant.PreferenceConstant.HOUR_TYPE, "3", getContext());
                mapDistance = "120 Km";
                mapDuration = "12 hours 0 mins";
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
                            pickUpDate.setTime(sdf.parse(binding.tvPickDate.getText().toString()));
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
                // No longer opening time picker automatically here.
                // The user will explicitly click the time picker.
                binding.tvJourneyTime.setText(""); // Clear previous time if any
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private boolean validate() {
        boolean valid = true;
        String fromLocation = binding.tvPickUp.getText().toString();
        /*String toLocation = binding.tvDrop.getText().toString();*/
        String date = binding.tvPickDate.getText().toString();
        String time = binding.tvJourneyTime.getText().toString();

        // Validate From Location
        if (fromLocation.isEmpty()) {
            binding.tvPickUp.setError("Please Enter Pick Location..!");
            valid = false;
        }

        // Validate To Location
        /*if (toLocation.isEmpty()) {
            binding.tvDrop.setError("Please Enter Drop Location..!");
            valid = false;
        }*/

        // Validate Pick Date
        if (date.isEmpty()) {
            binding.tvPickDate.setError("Please Enter Pick Date..!");
            valid = false;
        }

        // Validate Pick Time
        if (time.isEmpty()) {
            binding.tvJourneyTime.setError("Please Enter Pick Time..!");
            valid = false;
        }

//        if (binding.spBranch.getSelectedItemPosition() == 0) {
//            Toast.makeText(getContext(), "Please select a branch", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return valid;
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
                intent.putExtra(Constant.BundleExtras.WAY_TYPE, "3");
                intent.putExtra(Constant.BundleExtras.CAB_SERVICE_TYPE, "1");
                intent.putExtra(Constant.BundleExtras.PICK_ADDRESS, pickAddress);
                intent.putExtra(Constant.BundleExtras.LAT_PICK, PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LAT, getContext()));
                intent.putExtra(Constant.BundleExtras.LNG_PICK, PreferenceUtils.getString(Constant.PreferenceConstant.PICK_LNG, getContext()));
                intent.putExtra(Constant.BundleExtras.LAT_DROP, PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LAT, getContext()));
                intent.putExtra(Constant.BundleExtras.LNG_DROP, PreferenceUtils.getString(Constant.PreferenceConstant.DROP_LNG, getContext()));
                intent.putExtra(Constant.BundleExtras.DROP_ADDRESS, drop_address);
                intent.putExtra(Constant.BundleExtras.PICK_DATE, pick_date);
                intent.putExtra(Constant.BundleExtras.PICK_TIME, pick_time);
                intent.putExtra(Constant.BundleExtras.MAP_DISTANCE, mapDistance);
                intent.putExtra(Constant.BundleExtras.MAP_DURATION, mapDuration);
                intent.putExtra(Constant.BundleExtras.BRANCH_ID, branchId);
                startActivity(intent);
            }
        }.start();

    }




}
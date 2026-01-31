
package com.carro.carrorental.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.RazorPayApiClient;
import com.carro.carrorental.api.response.CouponResponse;
import com.carro.carrorental.api.response.CreateOrderResponse;
import com.carro.carrorental.api.response.LoginResponse;
import com.carro.carrorental.api.response.SelfDetailsResponse;
import com.carro.carrorental.api.response.SelfRentPlanResponse;
import com.carro.carrorental.api.response.SelfSubPlanResponse;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.databinding.ActivitySelfCarDetailBinding;
import com.carro.carrorental.listener.RentPlanClickListener;
import com.carro.carrorental.listener.SubsPlanClickListener;
import com.carro.carrorental.model.CarTypeModel;
import com.carro.carrorental.model.CouponModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.SelfCarModel;
import com.carro.carrorental.model.SelfRentPlanModel;
import com.carro.carrorental.model.SelfSubPlanModel;
import com.carro.carrorental.ui.adapter.SelfRentPlanAdapter;
import com.carro.carrorental.ui.adapter.SelfSubPlanAdapter;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.utils.NumberUtils;
import com.carro.carrorental.utils.PreferenceUtils;
import com.carro.carrorental.utils.Utils;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.mateware.snacky.Snacky;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelfCarDetailActivity extends BaseActivity implements RentPlanClickListener, SubsPlanClickListener, PaymentResultListener {

    private static final String TAG = "SelfCarDetailActivity";
    private ActivitySelfCarDetailBinding binding;
    private LoginModel loginModel = new LoginModel();

    private String avail_date = "", pick_date = "", pick_time = "", pickAddress = "", return_date = "", return_time = "",
            bookType = "", branch_id = "", bName = "", pick_delivery_address = "", package_id = "", subs_id = "", km = "",
            cTypeId = "", subDays = "", cStatus = "", couponPersent = "";

    private double totalFair = 0.0, basePrice = 0.0, withState = 0.0, outState = 0.0,
            pickAmt = 0.0, dropAmt = 0.0, bothAmt = 0.0, fastag = 0.0,
            finalCouponAmt = 0.0, pdbAmt = 0.0, selectedStateAmt = 0.0;

    private String extraKmAmt = "";

    private SelfCarModel carTypeModel = new SelfCarModel();
    private CarTypeModel carDetails = new CarTypeModel();
    private List<SelfRentPlanModel> rentPlanList = new ArrayList<>();
    private SelfRentPlanAdapter selfRentPlanAdapter;
    private List<SelfSubPlanModel> subPlanList = new ArrayList<>();
    private SelfSubPlanAdapter selfSubPlanAdapter;
    private String selectedState = "1";
    private String selectedPick = "";
    private SelfRentPlanModel currentRentPlan;
    private Dialog dialog;
    private long totalDurationInMinutes = 0;
    private List<LoginModel> userDetailList = new ArrayList<>();
    private CouponModel couponModel = null;
    private boolean isPaymentProcessStarted = false;


    private final Calendar myCalendar = Calendar.getInstance();


    private final ActivityResultLauncher<Intent> addressSelectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    String selectedAddress = result.getData().getStringExtra("selected_address");
                    pick_delivery_address = selectedAddress;
                    binding.tvAddress.setText(pick_delivery_address);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelfCarDetailBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Checkout.preload(getApplicationContext());
        getUserPreferences();
    }

    private void getUserPreferences() {
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        carTypeModel = new Gson().fromJson(getIntent().getStringExtra(Constant.BundleExtras.CAR_DATA), SelfCarModel.class);
        branch_id = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_ID, this);
        bName = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_NAME, this);
        pick_date = getIntent().getStringExtra(Constant.BundleExtras.PICK_DATE);
        pick_time = getIntent().getStringExtra(Constant.BundleExtras.PICK_TIME);
        pickAddress = getIntent().getStringExtra(Constant.BundleExtras.PICK_ADDRESS);
        return_date = getIntent().getStringExtra(Constant.BundleExtras.DROP_DATE);
        return_time = getIntent().getStringExtra(Constant.BundleExtras.DROP_TIME);
        bookType = getIntent().getStringExtra(Constant.BundleExtras.BOOK_TYPE);
        totalDurationInMinutes = getIntent().getLongExtra(Constant.BundleExtras.TOTAL_MINUTES, 0);

        if ("sub".equals(bookType)) {
            avail_date = getIntent().getStringExtra(Constant.BundleExtras.AVAIL_DATE);
        }

        initialization();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.btnContinue.setEnabled(true);
        getCarDetailsApi();
    }

    private void initialization() {
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());

        binding.tvBranchName.setText(bName);

        binding.llSelectStartDate.setOnClickListener(v -> openSubscriptionDatePicker());

        if ("sub".equals(bookType)) {
            avail_date = getIntent().getStringExtra(Constant.BundleExtras.AVAIL_DATE);
            pick_date = avail_date;
            pick_time = "10:00 AM";
            binding.tvSubscriptionStartDate.setText(String.format("%s %s", pick_date, pick_time));
        }


        binding.cvData.setVisibility(View.VISIBLE);
        binding.tvTravelDateTime.setText(String.format("%s %s", pick_date, pick_time));
        binding.tvDropDateTime.setText(String.format("%s %s", return_date, return_time));
        Utils.DurationResult result = Utils.calculateDuration(pick_date, pick_time, return_date, return_time);
        binding.tvDuration.setText(String.format("Duration : %d Days, %d Hours, %d Minutes", result.days, result.hours, result.minutes));


        binding.btnContinue.setOnClickListener(v -> userDetailApi());
        binding.btnApplyCoupon.setOnClickListener(v -> getCouponApi());
        binding.btnRemoveCoupon.setOnClickListener(v -> removeCoupon());

        binding.tvAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchLocationActivity.class);
            intent.putExtra(Constant.BundleExtras.FROM_ACTIVITY, true);
            addressSelectionLauncher.launch(intent);
        });

        binding.ivUp.setOnClickListener(v -> {
            binding.ivUp.setVisibility(View.GONE);
            binding.ivDown.setVisibility(View.VISIBLE);
            binding.cvCode.setVisibility(View.GONE);
        });
        binding.ivDown.setOnClickListener(v -> {
            binding.ivUp.setVisibility(View.VISIBLE);
            binding.ivDown.setVisibility(View.GONE);
            binding.cvCode.setVisibility(View.VISIBLE);
        });

        binding.tvAppliedCoupon.setText("- " + formatCurrency(0.0));

        binding.cbPickDrop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.llPd.setVisibility(View.VISIBLE);
                binding.llPdExtra.setVisibility(View.VISIBLE);
                binding.cardAddress.setVisibility(View.VISIBLE);
                if (selectedPick.isEmpty() && ("rent".equals(bookType) && currentRentPlan != null)) {
                    binding.rbPickUp.setChecked(true);
                } else if (selectedPick.isEmpty() && "sub".equals(bookType)) {
                    binding.rbPickUp.setChecked(true);
                } else {
                    handleAddressCardVisibility();
                }
            } else {
                binding.llPd.setVisibility(View.GONE);
                binding.llPdExtra.setVisibility(View.GONE);
                binding.cardAddress.setVisibility(View.GONE);
                binding.rbGroupPick.clearCheck();
                selectedPick = "";
                pickAmt = 0.0;
                dropAmt = 0.0;
                bothAmt = 0.0;
                pdbAmt = 0.0;
                updateTotalFare();
            }
        });

        binding.rbGroupPick.setOnCheckedChangeListener((group, checkedId) -> {
            if ("rent".equals(bookType) && currentRentPlan == null) return;

            if (checkedId == R.id.rbPickUp) {
                selectedPick = "1";
                if ("rent".equals(bookType)) {
                    pickAmt = safeParseDouble(currentRentPlan.getSdhPickupAmt());
                } else {
                    pickAmt = 0.0;
                }
                dropAmt = 0.0;
                bothAmt = 0.0;
                pdbAmt = pickAmt;
            } else if (checkedId == R.id.rbDrop) {
                selectedPick = "2";

                if ("rent".equals(bookType)) {
                    dropAmt = safeParseDouble(currentRentPlan.getSdhDropAmt());
                } else {
                    dropAmt = 0.0;
                }

                pickAmt = 0.0;
                bothAmt = 0.0;
                pdbAmt = dropAmt;
            } else if (checkedId == R.id.rbBoth) {
                selectedPick = "3";

                if ("rent".equals(bookType)) {
                    bothAmt = safeParseDouble(currentRentPlan.getSdhPdBoth());
                } else {
                    bothAmt = 0.0;
                }

                pickAmt = 0.0;
                dropAmt = 0.0;
                pdbAmt = bothAmt;
            }
            handleAddressCardVisibility();
            updateTotalFare();
        });

        binding.rbInState.setChecked(true);
        binding.llWithState.setVisibility(View.VISIBLE);
        binding.llOutState.setVisibility(View.GONE);
        binding.rbGroupState.setOnCheckedChangeListener((group, checkedId) -> {
            selectedState = (checkedId == R.id.rb_in_state) ? "1" : "2";
            binding.llWithState.setVisibility(selectedState.equals("1") ? View.VISIBLE : View.GONE);
            binding.llOutState.setVisibility(selectedState.equals("2") ? View.VISIBLE : View.GONE);
            updateTotalFare();
        });

      /*    final boolean[] isExpanded = {false}, isExpanded1 = {false}, isExpanded2 = {false};
        binding.ivDropDown.setOnClickListener(v -> {
            isExpanded[0] = !isExpanded[0];
            binding.tvInclusion.setMaxLines(isExpanded[0] ? Integer.MAX_VALUE : 2);
            binding.tvInclusion.setEllipsize(isExpanded[0] ? null : TextUtils.TruncateAt.END);
            binding.ivDropDown.setImageResource(isExpanded[0] ? R.drawable.ic_up_arrow : R.drawable.ic_arrow_down);
        });
        binding.ivDropDown1.setOnClickListener(v -> {
            isExpanded1[0] = !isExpanded1[0];
            binding.tvExclusion.setMaxLines(isExpanded1[0] ? Integer.MAX_VALUE : 2);
            binding.tvExclusion.setEllipsize(isExpanded1[0] ? null : TextUtils.TruncateAt.END);
            binding.ivDropDown1.setImageResource(isExpanded1[0] ? R.drawable.ic_up_arrow : R.drawable.ic_arrow_down);
        });
        binding.ivDropDown2.setOnClickListener(v -> {
            isExpanded2[0] = !isExpanded2[0];
            binding.tvTC.setMaxLines(isExpanded2[0] ? Integer.MAX_VALUE : 2);
            binding.tvTC.setEllipsize(isExpanded2[0] ? null : TextUtils.TruncateAt.END);
            binding.ivDropDown2.setImageResource(isExpanded2[0] ? R.drawable.ic_up_arrow : R.drawable.ic_arrow_down);
        });

/*        binding.tvSaveFastag.setOnClickListener(v -> {
            fastag = safeParseDouble(binding.etFastag.getText().toString());
            updateTotalFare();
        });*/


        binding.tvSaveFastag.setOnClickListener(v -> {
            // Check the current text of the button to decide the action
            if (binding.tvSaveFastag.getText().toString().equalsIgnoreCase(getString(R.string.add))) {
                // Action: ADD FASTAG
                String amountStr = binding.etFastag.getText().toString();
                if (TextUtils.isEmpty(amountStr)) {
                    Toast.makeText(SelfCarDetailActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                fastag = safeParseDouble(amountStr);
                updateTotalFare(); // Recalculate total with new fastag amount
                updateFastagUI();  // Update the UI to "Remove" state
            } else {
                // Action: REMOVE FASTAG
                fastag = 0.0;
                updateTotalFare(); // Recalculate total with zero fastag
                updateFastagUI();  // Update the UI to "Add" state
            }
        });

        // Add this line to set the initial state of the Fastag card when the screen loads
        updateFastagUI();


        binding.tvDetailedTc.setPaintFlags(binding.tvDetailedTc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.tvDetailedTc.setOnClickListener(v -> {
            Constant.WEBVIEW_TITLE = getString(R.string.terms_condition);
            Constant.WEBVIEW_URL = getString(R.string.terms_and_condition_url);
            startActivity(new Intent(this, WebViewActivity.class));
        });
    }

    private void updateFastagUI() {
        if (fastag > 0) {
            // State: Amount is added
            binding.etFastag.setText(String.format(Locale.US, "%.2f", fastag));
            binding.etFastag.setEnabled(false);
            binding.tvSaveFastag.setText(R.string.remove);
            binding.tvSaveFastag.setTextColor(ContextCompat.getColor(this, R.color.red));
        } else {
            // State: No amount, ready to add
            binding.etFastag.setText("");
            binding.etFastag.setEnabled(true);
            binding.etFastag.setHint(R.string.enter_fastag_price);
            binding.tvSaveFastag.setText(R.string.add);
            binding.tvSaveFastag.setTextColor(ContextCompat.getColor(this, R.color.primary));
        }
    }


    private void openSubscriptionDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        // Parse the available date to set as the minimum
        SimpleDateFormat parser = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        long minDateMillis;
        try {
            Date availableDate = parser.parse(avail_date);
            minDateMillis = availableDate.getTime();
        } catch (ParseException | NullPointerException e) {
            minDateMillis = System.currentTimeMillis() - 1000; // Fallback to today
        }

        // Set max date to 1 month from the available date
        Calendar maxDateCalendar = Calendar.getInstance();
        maxDateCalendar.setTimeInMillis(minDateMillis);
        maxDateCalendar.add(Calendar.MONTH, 1);
        long maxDateMillis = maxDateCalendar.getTimeInMillis();

        // Initialize picker with available date
        try {
            Date availableDate = parser.parse(avail_date);
            calendar.setTime(availableDate);
        } catch (ParseException | NullPointerException e) {
            // Use current time
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    pick_date = DateFormater.getDate(myCalendar.getTimeInMillis(), Constant.ddMMyyyy);
                    openSubscriptionTimePicker();
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(minDateMillis);
        datePickerDialog.getDatePicker().setMaxDate(maxDateMillis);
        datePickerDialog.show();
    }

    private void openSubscriptionTimePicker() {
        final Calendar c = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    pick_time = format.format(calendar.getTime());

                    // Update the UI
                    binding.tvSubscriptionStartDate.setText(String.format("%s %s", pick_date, pick_time));

                    // Automatically calculate drop-off date
                    calculateSubscriptionDropOff();
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    private void calculateSubscriptionDropOff() {
        if (pick_date.isEmpty() || pick_time.isEmpty() || subDays == null || subDays.isEmpty()) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);
        try {
            Date pickupDateTime = sdf.parse(pick_date + " " + pick_time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pickupDateTime);

            String[] parts = subDays.toLowerCase().trim().split("\\s+");
            int number = Integer.parseInt(parts[0]);
            String unit = parts[1];

            if (unit.startsWith("day")) {
                calendar.add(Calendar.DAY_OF_YEAR, number);
            } else if (unit.startsWith("month")) {
                calendar.add(Calendar.MONTH, number);
            } else if (unit.startsWith("year")) {
                calendar.add(Calendar.YEAR, number);
            }

            return_date = DateFormater.getDate(calendar.getTimeInMillis(), Constant.ddMMyyyy);
            return_time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleAddressCardVisibility() {
        if ("1".equals(selectedPick)) {
            binding.tvAddressTitle.setText(R.string.pickup_address);
        } else if ("2".equals(selectedPick)) {
            binding.tvAddressTitle.setText(R.string.delivery_address);
        } else if ("3".equals(selectedPick)) {
            binding.tvAddressTitle.setText(R.string.pickup_delivery_address);
        } else {
            binding.cardAddress.setVisibility(View.GONE);
        }
    }

    private void userDetailApi() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.user_details(loginModel.getmCustId());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                hideLoader();
                if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                    userDetailList.clear();
                    userDetailList.addAll(response.body().getData());
                    cStatus = userDetailList.get(0).getmCustStatus();

                    if (!binding.checkbox.isChecked()) {
                        setMessageForSnackbar("Please accept terms and conditions", false);
                        return;
                    }
                    if ("rent".equals(bookType) && selectedPick != null && !selectedPick.isEmpty() && (pick_delivery_address == null || pick_delivery_address.isEmpty())) {
                        setMessageForSnackbar("Please select a pickup/delivery address", false);
                        return;
                    }

                    if ("1".equals(cStatus)) {
                        binding.btnContinue.setEnabled(false);
                        isPaymentProcessStarted = true;
                        startPaymentFlow(totalFair);
                    } else {
                        Intent intent = new Intent(SelfCarDetailActivity.this, ProfileVerificationActivity.class);
                        intent.putExtra(Constant.BundleExtras.PAGE_TYPE, "self");
                        startActivity(intent);
                    }
                } else {
                    showError("Could not verify user status. Please try again.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                hideLoader();
                Log.e(TAG, "userDetailApi failed", t);
                showError("Something went wrong");
            }
        });
    }

    private void startPaymentFlow(final double amount) {
        if (amount < 1.0) {
            showError("Payment amount must be at least ₹1.");
            binding.btnContinue.setEnabled(true);
            isPaymentProcessStarted = false;
            return;
        }

        ProgressDialog pdLoading = new ProgressDialog(this);
        pdLoading.setMessage("Initializing Payment...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        RazorPayApiClient.getRazorpayClient().create(ApiInterface.class)
                .createRazorpayOrderTest(String.valueOf(amount))
                .enqueue(new Callback<CreateOrderResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CreateOrderResponse> call, @NonNull Response<CreateOrderResponse> response) {
                        pdLoading.dismiss();
                        if (response.isSuccessful() && response.body() != null && "success".equalsIgnoreCase(response.body().getResponseStatus())) {
                            String razorpayOrderId = response.body().getOrderId();
                            if (razorpayOrderId != null && !razorpayOrderId.isEmpty()) {
                                startRazorpayCheckout(amount, razorpayOrderId);
                            } else {
                                isPaymentProcessStarted = false;
                                showError("Server did not provide an Order ID.");
                                binding.btnContinue.setEnabled(true);
                            }
                        } else {
                            isPaymentProcessStarted = false;
                            showError("Payment initialization failed on server.");
                            binding.btnContinue.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CreateOrderResponse> call, @NonNull Throwable t) {
                        pdLoading.dismiss();
                        isPaymentProcessStarted = false;
                        Log.e(TAG, "createRazorpayOrder API call failed", t);
                        showError("An error occurred. Please check your connection.");
                        binding.btnContinue.setEnabled(true);
                    }
                });
    }

    private void startRazorpayCheckout(double amount, String razorpayOrderId) {
        final Activity activity = this;
        final Checkout checkout = new Checkout();
        checkout.setKeyID(getString(R.string.razor_pay_key_test));
        checkout.setImage(R.mipmap.ic_launcher);
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Payment for " + carDetails.getmCtypeTitle());
            options.put("order_id", razorpayOrderId);
            options.put("currency", "INR");
            options.put("amount", (long) (amount * 100)); // Amount in paise
            JSONObject prefill = new JSONObject();
            prefill.put("email", loginModel.getmCustEmail());
            prefill.put("contact", loginModel.getmCustMobile());
            options.put("prefill", prefill);
            checkout.open(activity, options);
        } catch (Exception e) {
            isPaymentProcessStarted = false;
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
            Toast.makeText(activity, "Error initializing payment: " + e.getMessage(), Toast.LENGTH_LONG).show();
            binding.btnContinue.setEnabled(true);
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentId) {
        if (!isPaymentProcessStarted) return;
        isPaymentProcessStarted = false;
        insertSelfBookingApi(razorpayPaymentId);
    }

    @Override
    public void onPaymentError(int code, String description) {
        if (!isPaymentProcessStarted) return;
        isPaymentProcessStarted = false;
        Log.e(TAG, "Payment FAILED! Code: " + code + " Description: " + description);
        setMessageForSnackbar("Payment Failed: " + description, false);
        binding.btnContinue.setEnabled(true);
    }

    private void insertSelfBookingApi(String transId) {
        showLoader();
        String pick_date2 = "", return_date2 = "", pickTimeIn24Hour = "", returnTimeIn24Hour = "";

        // The date/time values are now set for both rent and subscription
        if (pick_date != null && !pick_date.isEmpty())
            pick_date2 = DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, pick_date);
        if (return_date != null && !return_date.isEmpty())
            return_date2 = DateFormater.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, return_date);
        if (pick_time != null && !pick_time.isEmpty())
            pickTimeIn24Hour = DateFormater.formatTo24Hour(pick_time);
        if (return_time != null && !return_time.isEmpty())
            returnTimeIn24Hour = DateFormater.formatTo24Hour(return_time);

        String bType = "rent".equals(bookType) ? "1" : "2";

        String couponId = "";
        if (couponModel != null) {
            couponId = couponModel.getmCouponId();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiInterface.insert_self_drive_booking(bType,
                branch_id,
                package_id,
                subs_id,
                pick_date2,
                pickTimeIn24Hour,
                return_date2,
                returnTimeIn24Hour,
                selectedState,
                String.valueOf(selectedStateAmt),
                selectedPick,
                pick_delivery_address,
                String.valueOf(pdbAmt),
                loginModel.getmCustId(),
                carDetails.getmCtypeId(),
                String.valueOf(basePrice),
                km,
                String.valueOf(totalFair),
                String.valueOf(totalFair),
                "0.0",
                "Online",
                "1",
                String.valueOf(fastag),
                "1",
                couponId,
                couponPersent,
                String.valueOf(finalCouponAmt),
                subDays
        );

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                hideLoader();
                if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                    showSuccessDialog("Booking Confirmed Successfully!", SelfCarDetailActivity.this);
                } else {
                    showError("Your car could not be booked. Please contact support.");
                    binding.btnContinue.setEnabled(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                hideLoader();
                Log.e(TAG, "insertSelfBookingApi failed", t);
                showError("Something went wrong");
                binding.btnContinue.setEnabled(true);
            }
        });
    }

    private void setMessageForSnackbar(String msg, boolean flag) {
        Snacky.Builder builder = Snacky.builder().setActivity(this).setText(msg).setDuration(Snacky.LENGTH_SHORT);
        if (flag) builder.success().show();
        else builder.error().show();
    }

    private void setData() {
        binding.tvCarName.setText(carDetails.getmCtypeTitle());
        binding.tvSeats.setText(String.format("%s Seater", carDetails.getmCtypeSeat()));
        binding.tvFuel.setText(carDetails.getmCtypeFuel());
        binding.tvManual.setText("1".equals(carDetails.getmCtypeDrivetype()) ? "Manual" : "Automatic");

        Glide.with(this).load(ImagePathDecider.getCarImagePath() + carDetails.getmCtypeImg()).error(R.drawable.demo_car).into(binding.ivCar);

        boolean isInclusionEmpty = TextUtils.isEmpty(carDetails.getmCtypeInclusion());
        boolean isExclusionEmpty = TextUtils.isEmpty(carDetails.getmCtypeExclusion());
        boolean isTcEmpty = TextUtils.isEmpty(carDetails.getmCtypeTc());

        binding.cardTc.setVisibility((isInclusionEmpty && isExclusionEmpty && isTcEmpty) ? View.GONE : View.VISIBLE);

        // Add tabs
        binding. tabLayout.setBackgroundColor(Color.WHITE);
        binding.tabLayout.removeAllTabs();
        binding.tabLayout.addTab( binding.tabLayout.newTab().setText("Inclusions"));
        binding.tabLayout.addTab( binding.tabLayout.newTab().setText("Exclusions"));
        binding.tabLayout.addTab( binding.tabLayout.newTab().setText("T&C"));

        // Default content
        binding.tvContent.setText(Html.fromHtml(carDetails.getmCtypeInclusion()));

        // Tab selection listener
        binding. tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        binding. tvContent.setText(isInclusionEmpty?"N/A":Html.fromHtml(carDetails.getmCtypeInclusion()));
                        break;
                    case 1:
                        binding.tvContent.setText(isExclusionEmpty?"N/A":Html.fromHtml(carDetails.getmCtypeExclusion()));
                        break;
                    case 2:
                        binding.tvContent.setText(isTcEmpty?"N/A":Html.fromHtml(carDetails.getmCtypeTc()));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

      /*  binding.llInclusion.setVisibility(isInclusionEmpty ? View.GONE : View.VISIBLE);
        binding.tvInclusion.setText(Html.fromHtml(carDetails.getmCtypeInclusion() != null ? carDetails.getmCtypeInclusion() : ""));
        binding.llExclusion.setVisibility(isExclusionEmpty ? View.GONE : View.VISIBLE);
        binding.tvExclusion.setText(Html.fromHtml(carDetails.getmCtypeExclusion() != null ? carDetails.getmCtypeExclusion() : ""));
        binding.llTc.setVisibility(isTcEmpty ? View.GONE : View.VISIBLE);
        binding.tvTC.setText(Html.fromHtml(carDetails.getmCtypeTc() != null ? carDetails.getmCtypeTc() : ""));*/
    }

    private void getCarDetailsApi() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SelfDetailsResponse> call = apiInterface.self_service_details(carTypeModel.getmCtypeId());
        call.enqueue(new Callback<SelfDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SelfDetailsResponse> call, @NonNull Response<SelfDetailsResponse> response) {
                hideLoader();
                if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                    carDetails = response.body().getData();
                    cTypeId = carDetails.getmCtypeId();
                    setData();
                    if ("rent".equals(bookType)) {
                        binding.llTop.setVisibility(View.VISIBLE);
                        binding.cardSubscriptionDate.setVisibility(View.GONE);
                        getRentPlanApi();
                    } else {
                        binding.llTop.setVisibility(View.GONE);
                        binding.cardSubscriptionDate.setVisibility(View.VISIBLE); // Show the new card
                        getSubPlanApi();
                        binding.cardState.setVisibility(View.GONE);
                        binding.cbCardPD.setVisibility(View.VISIBLE);
                        binding.llPriceDetails.setVisibility(View.VISIBLE);
                        binding.cardFasTag.setVisibility(View.GONE);
                        binding.btnExtra.setVisibility(View.GONE);
                        binding.btnLimit.setVisibility(View.VISIBLE);
                    }
                } else {
                    showError("Could not load car details.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SelfDetailsResponse> call, @NonNull Throwable t) {
                hideLoader();
                Log.e(TAG, "getCarDetailsApi failure", t);
                showError("Something went wrong");
            }
        });
    }

    private void getCouponApi() {
        showLoader();
        String coupon = binding.etCode.getText().toString();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CouponResponse> call = apiInterface.get_coupon(coupon);
        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(@NonNull Call<CouponResponse> call, @NonNull Response<CouponResponse> response) {
                hideLoader();
                if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                    Toast.makeText(SelfCarDetailActivity.this, "Coupon Applied successfully", Toast.LENGTH_SHORT).show();
                    couponModel = response.body().getData();
                    updateTotalFare();
                    updateCouponUI(true);
                } else {
                    showError("Invalid Coupon");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CouponResponse> call, @NonNull Throwable t) {
                hideLoader();
                Log.e(TAG, "getCouponApi failure", t);
                showError("Something went wrong");
            }
        });
    }

    private void removeCoupon() {
        couponModel = null;
        finalCouponAmt = 0.0;
        couponPersent = "";
        binding.etCode.setText("");
        Toast.makeText(this, "Coupon removed", Toast.LENGTH_SHORT).show();
        updateTotalFare();
        updateCouponUI(false);
    }

    private void updateCouponUI(boolean isApplied) {
        if (isApplied) {
            binding.layoutApplyCoupon.setVisibility(View.GONE);
            binding.layoutAppliedCoupon.setVisibility(View.VISIBLE);
            String appliedText = String.format("%s APPLIED", binding.etCode.getText().toString().toUpperCase());
            binding.tvAppliedCouponCode.setText(appliedText);
        } else {
            binding.layoutApplyCoupon.setVisibility(View.VISIBLE);
            binding.layoutAppliedCoupon.setVisibility(View.GONE);
        }
    }

    private void getRentPlanApi() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SelfRentPlanResponse> call = apiInterface.self_service_hourly_list(cTypeId);
        call.enqueue(new Callback<SelfRentPlanResponse>() {
            @Override
            public void onResponse(@NonNull Call<SelfRentPlanResponse> call, @NonNull Response<SelfRentPlanResponse> response) {
                hideLoader();
                if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                    rentPlanList.clear();
                    rentPlanList.addAll(response.body().getData());
                    binding.recPlans.setLayoutManager(new LinearLayoutManager(SelfCarDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    selfRentPlanAdapter = new SelfRentPlanAdapter(SelfCarDetailActivity.this, rentPlanList, SelfCarDetailActivity.this, totalDurationInMinutes);
                    binding.recPlans.setAdapter(selfRentPlanAdapter);
                    if (!rentPlanList.isEmpty()) {
                        onRentClick(rentPlanList.get(0));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SelfRentPlanResponse> call, @NonNull Throwable t) {
                hideLoader();
                Log.e(TAG, "getRentPlanApi failure", t);
            }
        });
    }

    private void getSubPlanApi() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SelfSubPlanResponse> call = apiInterface.self_service_subs(cTypeId);
        call.enqueue(new Callback<SelfSubPlanResponse>() {
            @Override
            public void onResponse(@NonNull Call<SelfSubPlanResponse> call, @NonNull Response<SelfSubPlanResponse> response) {
                hideLoader();
                if (response.isSuccessful() && response.body() != null && response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                    subPlanList.clear();
                    subPlanList.addAll(response.body().getData());
                    binding.recPlans.setLayoutManager(new LinearLayoutManager(SelfCarDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    selfSubPlanAdapter = new SelfSubPlanAdapter(SelfCarDetailActivity.this, subPlanList, SelfCarDetailActivity.this);
                    binding.recPlans.setAdapter(selfSubPlanAdapter);
                    if (!subPlanList.isEmpty()) {
                        onSubsClick(subPlanList.get(0));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SelfSubPlanResponse> call, @NonNull Throwable t) {
                hideLoader();
                Log.e(TAG, "getSubPlanApi failure", t);
            }
        });
    }

    @Override
    public void onRentClick(SelfRentPlanModel selfRentPlanModel) {
        package_id = selfRentPlanModel.getSdhId();
        km = selfRentPlanModel.getSdhKm();


        double kmPerHour = Double.parseDouble(selfRentPlanModel.getSdhKm());
        double kmPerMinute = kmPerHour / 60.0;
        km = String.valueOf(kmPerMinute * totalDurationInMinutes);




        currentRentPlan = selfRentPlanModel;
        updateTotalFare();
    }

    @Override
    public void onSubsClick(SelfSubPlanModel selfSubPlanModel) {
        subs_id = selfSubPlanModel.getmSubsId();
        basePrice = safeParseDouble(selfSubPlanModel.getmSubsPrice());
        subDays = selfSubPlanModel.getmSubsDay();
        binding.btnLimit.setText(String.format("%s Limit", calculateKmsFromDurationString(selfSubPlanModel.getmSubsDay())));

        String input = binding.btnLimit.getText().toString();
        String numberOnly = input.replaceAll("[^0-9]", "");

        km = numberOnly;


        // When a new plan is clicked, recalculate the drop-off date
        calculateSubscriptionDropOff();

        updateTotalFare();
    }

    private String formatCurrency(double value) {
        return new DecimalFormat(Constant.CURRENCY_FORMAT).format(value);
    }

    private void updateTotalFare() {
        // --- 1. Calculate Base Price ---
        if ("rent".equals(bookType)) {
            if (currentRentPlan == null) return;
            double pricePerHour = safeParseDouble(currentRentPlan.getSdhPrice());
            double pricePerMinute = pricePerHour / 60.0;
            basePrice = pricePerMinute * totalDurationInMinutes;

            // Apply 30% discount if duration is over 24 hours (1440 minutes)
            if (totalDurationInMinutes >= 1440) {
                basePrice *= 0.70; // Applying 30% discount
            }

        } // For "sub", basePrice is set in onSubsClick

        // --- 2. Calculate Coupon Discount ON BASE PRICE ONLY ---
        if (couponModel != null) {
            if ("2".equals(couponModel.getmCouponType())) { // Fixed amount
                finalCouponAmt = safeParseDouble(couponModel.getmCouponPeramt());
            } else { // Percentage
                couponPersent = couponModel.getmCouponPeramt();
                double percentage = safeParseDouble(couponModel.getmCouponPeramt());
                finalCouponAmt = (percentage / 100.0) * basePrice;
            }
        } else {
            finalCouponAmt = 0.0;
            couponPersent = "";
        }
        double priceAfterDiscount = basePrice - finalCouponAmt;

        // --- 3. Calculate Other Charges ---
//        fastag = safeParseDouble(binding.etFastag.getText().toString());
        pdbAmt = "1".equals(selectedPick) ? pickAmt : ("2".equals(selectedPick) ? dropAmt : ("3".equals(selectedPick) ? bothAmt : 0.0));

        // --- 4. Calculate Refundable Deposit ---
        double refundableDeposit = 0.0;
        if ("rent".equals(bookType)) {
            if (currentRentPlan != null) {
                withState = safeParseDouble(currentRentPlan.getSdhDepositAmtWithin());
                outState = safeParseDouble(currentRentPlan.getSdhDepositAmtOutside());
                refundableDeposit = "1".equals(selectedState) ? withState : outState;
                selectedStateAmt = refundableDeposit;
            }
        } else { // Subscription deposit logic
            if (subDays != null && !subDays.isEmpty()) {
                int durationValue = Integer.parseInt(subDays.replaceAll("[^0-9]", ""));
                if (subDays.toLowerCase().contains("day") || (subDays.toLowerCase().contains("month") && durationValue == 1)) {
                    refundableDeposit = 5000.0;
                } else {
                    refundableDeposit = 2000.0;
                }
            }
            withState = refundableDeposit;
            outState = refundableDeposit;
            selectedStateAmt = refundableDeposit;
        }

        // --- 5. Calculate Final Total ---
        totalFair = priceAfterDiscount + pdbAmt + fastag + refundableDeposit;
        if (totalFair < 0) totalFair = 0.0;

        // --- 6. Update UI ---
        binding.tvBasePrice.setText(formatCurrency(basePrice));
        binding.tvWithinState.setText(formatCurrency(withState));
        binding.tvOutsideState.setText(formatCurrency(outState));
        binding.tvPickAmt.setText(formatCurrency(pickAmt));
        binding.tvDropAmt.setText(formatCurrency(dropAmt));
        binding.tvBothAmt.setText(formatCurrency(bothAmt));
        binding.tvFastag.setText(formatCurrency(fastag));
        binding.tvAppliedCoupon.setText("- " + formatCurrency(finalCouponAmt));
        binding.tvTotalFare.setText("₹ " + formatCurrency(totalFair));

        // --- NEW: Hide rows if their value is zero ---
        binding.llPick.setVisibility(pickAmt > 0 ? View.VISIBLE : View.GONE);
        binding.llDrop.setVisibility(dropAmt > 0 ? View.VISIBLE : View.GONE);
        binding.llBoth.setVisibility(bothAmt > 0 ? View.VISIBLE : View.GONE);
        binding.llFasTag.setVisibility(fastag > 0 ? View.VISIBLE : View.GONE);
        binding.llCoupon.setVisibility(finalCouponAmt > 0 ? View.VISIBLE : View.GONE);

        if (currentRentPlan != null) {
            extraKmAmt = currentRentPlan.getSdhExtraKmAmt();
            binding.btnExtra.setText("Extra km charges ₹ " + (extraKmAmt != null && !extraKmAmt.isEmpty() ? extraKmAmt : "0") + " / km");
        }
    }

    private String calculateKmsFromDurationString(String durationString) {
        if (durationString == null || durationString.trim().isEmpty()) return "";
        try {
            String[] parts = durationString.toLowerCase().trim().split("\\s+");
            if (parts.length < 2) return "";
            int number = Integer.parseInt(parts[0]);
            String unit = parts[1];
            int totalDays;
            if (unit.startsWith("day")) totalDays = number;
            else if (unit.startsWith("month")) totalDays = number * 30;
            else if (unit.startsWith("year")) totalDays = number * 365;
            else return "";
            long totalKms = (long) totalDays * 100;
            return totalKms > 0 ? NumberUtils.formatWithCommas(totalKms) + " km" : "";
        } catch (Exception e) {
            return "";
        }
    }

    private double safeParseDouble(String value) {
        if (value == null || value.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public void showSuccessDialog(String message, Context context) {
        dialog = new Dialog(this, R.style.my_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.show();
        TextView textView = dialog.findViewById(R.id.tv_message);
        textView.setText(message);
        GifImageView gifImageView = dialog.findViewById(R.id.gifImageView);
        gifImageView.setImageResource(R.drawable.gif_booking_confirmation);
        new CountDownTimer(2500, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                dialog.cancel();
                Intent intent = new Intent(SelfCarDetailActivity.this, MyBookingsActivity.class);
                intent.putExtra(Constant.IS_FROM,"1");
                startActivity(intent);
                finishAffinity();
            }
        }.start();
    }
}
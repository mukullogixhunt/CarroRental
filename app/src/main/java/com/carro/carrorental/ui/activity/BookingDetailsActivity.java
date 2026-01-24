package com.carro.carrorental.ui.activity;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.BookingDetailResponse;
import com.carro.carrorental.databinding.ActivityBookingDetailsBinding;
import com.carro.carrorental.model.BookingDetailModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.utils.PreferenceUtils;
import com.carro.carrorental.utils.Utils;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailsActivity extends BaseActivity {

    private ActivityBookingDetailsBinding binding;
    private String bookingId = "";
    private LoginModel loginModel = new LoginModel();
    private BookingDetailModel bookingDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDetailsBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getPreference();
    }

    private void getPreference() {
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        bookingId = getIntent().getStringExtra(Constant.BundleExtras.BOOKING_ID);

        initialization();
    }

    private void initialization() {
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        fetchBookingDetails();
    }

    private void fetchBookingDetails() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BookingDetailResponse> call = apiInterface.getBookingDetails(bookingId);
        call.enqueue(new Callback<BookingDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<BookingDetailResponse> call, @NonNull Response<BookingDetailResponse> response) {
                hideLoader();
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    bookingDetail = response.body().getData().get(0);
                    boolean isCurrents = getIntent().getBooleanExtra("isCurrent",false);
                    if (isCurrents&&bookingDetail.getmBkingType().equals("2")&&bookingDetail.getmBkingTypeCat().equals("2")){
                        binding.btnUploadImages.setVisibility(VISIBLE);
                    }
                    populateUI();
                } else {
                    showError("Failed to load booking details.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingDetailResponse> call, @NonNull Throwable t) {
                hideLoader();
                showError("Something went wrong. Please check your connection.");
            }
        });
    }

    private void populateUI() {
        if (bookingDetail == null) return;

        binding.toolbar.setTitle(getServiceName(bookingDetail));

        // --- Top Card: ID and Status ---
        binding.tvBookingId.setText(String.format("Booking ID: %s", bookingDetail.getmBookingId()));
        setBookingStatus(binding.tvBookingStatus, bookingDetail.getmBkingStatus());

        if ("2".equals(bookingDetail.getmBkingType())) {
            binding.layoutLiveLocation.setVisibility(View.GONE);
            binding.chauffeurDetailsHeader.setVisibility(View.GONE);
            binding.chauffeurDetailsCard.setVisibility(View.GONE);

        } else {
            binding.layoutLiveLocation.setVisibility("2".equals(bookingDetail.getmBkingStatus()) ? VISIBLE : View.GONE);
            binding.chauffeurDetailsHeader.setVisibility(VISIBLE);
            binding.chauffeurDetailsCard.setVisibility(VISIBLE);
        }


        // --- Trip Details ---
        setDetailRow(binding.rowBranchName.getRoot(), "Branch", bookingDetail.getmBranchTitle());
        setDetailRow(binding.rowServiceName.getRoot(), "Service", getServiceName(bookingDetail));
        setDetailRow(binding.rowPickupLocation.getRoot(), "Pickup", getPickupLocation(bookingDetail));
        setDetailRow(binding.rowDropLocation.getRoot(), "Drop-off", getDropoffLocation(bookingDetail));
        setDetailRow(binding.rowPickupDatetime.getRoot(), "Pickup Time", getFormattedDateTime(bookingDetail.getmBkingPickup(), bookingDetail.getmBkingPickupAt()));
        setDetailRow(binding.rowDropDatetime.getRoot(), "Drop-off Time", getFormattedDateTime(bookingDetail.getmBkingReturn(), bookingDetail.getmBkingReturnAt()));

        // --- Fleet Details ---
        Glide.with(this)
                .load(ImagePathDecider.getCarImagePath() + bookingDetail.getmCtypeImg())
                .placeholder(R.drawable.demo_car)
                .error(R.drawable.demo_car)
                .into(binding.ivCarImage);




        if ("2".equals(bookingDetail.getmBkingType())) {
            setDetailRow(binding.rowCarName.getRoot(), "Car Name", bookingDetail.getmCtypeTitle() );
            binding.rowCarType.getRoot().setVisibility(View.GONE);
        }else {
            setDetailRow(binding.rowCarType.getRoot(), "Car Type", bookingDetail.getmCtypeTitle() );
            binding.rowCarType.getRoot().setVisibility(VISIBLE);
            String carName = bookingDetail.getmCarName();
            String carNumber = bookingDetail.getmCarNumber();
            String formattedCarDetails = "";
            if (carName != null && !carName.trim().isEmpty()) {
                // Capitalize the first letter of the car name
                formattedCarDetails = carName.substring(0, 1).toUpperCase() + carName.substring(1);
                // Check if car number is available and has at least 4 digits
                if (carNumber != null && carNumber.trim().length() >= 4) {
                    // Get the last 4 digits
                    String lastFourDigits = carNumber.substring(carNumber.length() - 4);
                    // Append to the string in brackets
                    formattedCarDetails += " " + lastFourDigits;
                }
            }
            setDetailRow(binding.rowCarName.getRoot(), "Car Name", formattedCarDetails );
        }




        String specs = String.format("%s Seater, %s, %s", bookingDetail.getmCtypeSeat(), bookingDetail.getmCtypeFuel(), "1".equals(bookingDetail.getmCtypeDrivetype()) ? "Manual" : "Automatic");
        setDetailRow(binding.rowCarSpecs.getRoot(), "Specifications", specs);

        // --- Chauffeur Details ---


        String driverFullName = bookingDetail.getmDriverName();

// Check if a driver has been assigned (by checking if the name is valid)
        if (driverFullName != null && !driverFullName.trim().isEmpty() && !driverFullName.trim().equalsIgnoreCase("null")) {
            // A driver IS assigned, show the details layout
            binding.layoutDriverAssigned.setVisibility(VISIBLE);
            binding.tvNoDriverAssigned.setVisibility(View.GONE);

            // Format the first name and add "Ji"
            String[] nameParts = driverFullName.trim().split("\\s+");
            String firstName = nameParts[0];
            String formattedDriverName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase() + " Ji";

            setDetailRow(binding.rowDriverName.getRoot(), "Chauffeur Name", formattedDriverName);
            setDetailRow(binding.rowDriverMobile.getRoot(), "Mobile Number", bookingDetail.getmDriverMobile());

            binding.btnCallDriver.setOnClickListener(v -> dialPhoneNumber(bookingDetail.getmDriverMobile()));
            binding.btnMessageDriver.setOnClickListener(v -> openWhatsApp(bookingDetail.getmDriverMobile()));
        } else {
            // No driver is assigned, show the "Not Yet Assigned" message
            binding.layoutDriverAssigned.setVisibility(View.GONE);
            binding.tvNoDriverAssigned.setVisibility(VISIBLE);
        }


        // --- Payment Details ---


        if("2".equals(bookingDetail.getmBkingType())){
            setDetailRow(binding.rowBasePrice.getRoot(), "Base Fare" , formatCurrency(bookingDetail.getmBkingPrice()));
        }else {
            setDetailRow(binding.rowBasePrice.getRoot(), "Base Fare (per km)" , formatCurrency(bookingDetail.getmBkingPrice()));

        }

        setDetailRow(binding.rowBaseDistance.getRoot(), "Distance ", bookingDetail.getmBkingKm() +" Km");
        setDetailRow(binding.rowPickupFee.getRoot(), "Pickup/Drop Fee", formatCurrency(bookingDetail.getmBkingPickDropBothAmt()));
        setDetailRow(binding.rowFastag.getRoot(), "Fastag", formatCurrency(bookingDetail.getmBkingFastag()));
        setDetailRow(binding.rowDeposit.getRoot(), "Refundable Deposit", formatCurrency(bookingDetail.getmBkingInsideOutsideStateAmt()));
        setDetailRow(binding.rowCoupon.getRoot(), "Coupon Discount", "- "+formatCurrency(bookingDetail.getmBkingDamt()));
        setDetailRow(binding.rowTotal.getRoot(), "Grand Total", formatCurrency(bookingDetail.getmBkingTotal()));
        setDetailRow(binding.rowPaid.getRoot(), "Amount Paid", formatCurrency(bookingDetail.getmBkingPaidAmt()));
        setDetailRow(binding.rowRemaining.getRoot(), "Amount Remaining", formatCurrency(bookingDetail.getmBkingRemainAmt()));

        // --- Bottom Buttons ---
        binding.btnEmergencyCall.setOnClickListener(v -> dialPhoneNumber("7566699900"));
        binding.btnViewBill.setOnClickListener(v -> {
            Constant.WEBVIEW_TITLE = getString(R.string.invoice);
            Constant.WEBVIEW_URL ="https://carrorental.in/myadmin/Booking/invoice?view="+bookingId;
            Intent intent = new Intent(BookingDetailsActivity.this, WebViewActivity.class);
            startActivity(intent);
        });
        binding.btnUploadImages.setOnClickListener(v ->
                startActivity(new Intent(BookingDetailsActivity.this,UploadCarImage.class)
                        .putExtra(Constant.BundleExtras.BOOKING_ID,bookingId)));
    }



    private void setDetailRow(View row, String label, String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("null") || value.trim().equals("0.00")|| value.trim().equals("₹ 0.00")|| value.trim().equals("- ₹ 0.00") || value.trim().equals("0")) {
            row.setVisibility(View.GONE);
            return;
        }
        row.setVisibility(VISIBLE);
        TextView tvLabel = row.findViewById(R.id.tvLabel);
        TextView tvValue = row.findViewById(R.id.tvValue);
        tvLabel.setText(label);
        tvValue.setText(value);
    }

    private String formatCurrency(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("null")) {
            return "₹ 0.00";
        }
        try {
            double amount = Double.parseDouble(value);
            return String.format(Locale.US, "₹ %,.2f", amount);
        } catch (NumberFormatException e) {
            return "₹ 0.00";
        }
    }

    private String getServiceName(BookingDetailModel item) {
        switch (item.getmBkingType()) {
            case "1":
                switch (item.getmBkingTypeCat()) {
                    case "1": return "City Ride";
                    case "2": return "One Way Cab Service";
                    case "3": return "Outstation Cab Service";
                    case "4": return "Airport Cab Service";
                    default: return "Cab Service";
                }
            case "2":
                switch (item.getmBkingTypeCat()) {
                    case "1": return "Self Drive Service (Rental)";
                    case "2": return "Self Drive Service (Subscription)";
                    default: return "Self Drive Service";
                }
            case "3": return "Luxury Car Service";
            case "4": return "Bus Booking Service";
            default: return "Booking";
        }
    }

    private String getPickupLocation(BookingDetailModel item) {
        if (item.getmBkingPickupAddress() != null && !item.getmBkingPickupAddress().isEmpty()) {
            return item.getmBkingPickupAddress();
        } else if (item.getmBkingFrom() != null && !item.getmBkingFrom().isEmpty()) {
            return item.getmBkingFrom();
        }
        return null;
    }

    private String getDropoffLocation(BookingDetailModel item) {
        if (item.getmBkingDropAddress() != null && !item.getmBkingDropAddress().isEmpty()) {
            return item.getmBkingDropAddress();
        } else if (item.getmBkingTo() != null && !item.getmBkingTo().isEmpty()) {
            return item.getmBkingTo();
        }
        return null;
    }

    private String getFormattedDateTime(String date, String time) {
        if (date == null || time == null || date.equals("1970-01-01") || date.equals("0000-00-00")) return null;
        String formattedDate = DateFormater.changeDateFormat(Constant.yyyyMMdd, "dd MMM yyyy", date);
        String formattedTime = Utils.formatTimeString(Constant.HHMMSS, Constant.HHMMSSA, time);
        return String.format("%s, %s", formattedDate, formattedTime);
    }

    private void setBookingStatus(TextView textView, String status) {
        if (status == null) {
            textView.setVisibility(View.GONE);
            return;
        }
        textView.setVisibility(VISIBLE);
        switch (status) {
            case "1":
                textView.setText("Pending");
                textView.setTextColor(ContextCompat.getColor(this, R.color.orange));
                textView.setBackgroundResource(R.drawable.bg_status_pending);
                break;
            case "2":
                textView.setText("Accepted");
                textView.setTextColor(ContextCompat.getColor(this, R.color.blue));
                textView.setBackgroundResource(R.drawable.bg_status_accepted);
                break;
            case "3":
                textView.setText("Completed");
                textView.setTextColor(ContextCompat.getColor(this, R.color.green));
                textView.setBackgroundResource(R.drawable.bg_status_completed);
                break;
            case "4":
                textView.setText("Cancelled");
                textView.setTextColor(ContextCompat.getColor(this, R.color.red));
                textView.setBackgroundResource(R.drawable.bg_status_cancelled);
                break;
            default:
                textView.setVisibility(View.GONE);
                break;
        }
    }

    private void dialPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            Toast.makeText(this, "Phone number is not available.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
        startActivity(intent);
    }

    private void openWhatsApp(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            Toast.makeText(this, "Phone number is not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Remove any non-digit characters and add the country code if missing
        String formattedNumber = phoneNumber.replaceAll("[^0-9]", "");
        if (!formattedNumber.startsWith("91")) {
            formattedNumber = "91" + formattedNumber;
        }

        try {
            // Check if WhatsApp is installed
            getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + formattedNumber));
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // WhatsApp is not installed, show a message
            Toast.makeText(this, "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show();
        }
    }
}
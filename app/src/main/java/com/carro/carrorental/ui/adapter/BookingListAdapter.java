/*
package com.logixhunt.carrorental.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.logixhunt.carrorental.R;
import com.logixhunt.carrorental.databinding.MyBookingItemBinding;
import com.logixhunt.carrorental.listener.MyBookingClickListener;
import com.logixhunt.carrorental.model.BookingListModel;
import com.logixhunt.carrorental.ui.activity.MapActivity;
import com.logixhunt.carrorental.ui.activity.UpdateBookingPayment;
import com.logixhunt.carrorental.utils.Constant;
import com.logixhunt.carrorental.utils.DateFormater;
import com.logixhunt.carrorental.utils.Utils;

import java.util.List;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder> {

    Context context;
    List<BookingListModel> items;
    MyBookingClickListener myBookingClickListener;
    String bookType;

    public BookingListAdapter(Context context, List<BookingListModel> items, MyBookingClickListener myBookingClickListener) {
        this.context = context;
        this.items = items;
        this.myBookingClickListener = myBookingClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyBookingItemBinding binding = MyBookingItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingListModel item = items.get(holder.getAdapterPosition());

        holder.binding.lvOther.setVisibility(View.GONE);
        holder.binding.tvPrice.setText("Rs. " + item.getmBkingTotal());
        holder.binding.tvPaidAmt.setText("Paid : Rs. " + item.getmBkingPaidAmt());
        holder.binding.tvRemainAmt.setText("Remain : Rs. " + item.getmBkingRemainAmt());
        holder.binding.tvBookingId.setText("Booking Id : " + item.getmBookingId());
        holder.binding.tvPersons.setText(item.getmBkingKm() + " Km");
        holder.binding.tvTime.setText(Utils.formatTimeString(Constant.HHMMSS, Constant.HHMMSSA, item.getmBkingPickupAt()));
        holder.binding.tvDate.setText(DateFormater.changeDateFormat(Constant.yyyyMMdd, Constant.ddMMyyyy, item.getmBkingPickup()));

        if (item.getmBkingPayStatus().equals("2")) {
            holder.binding.tvUpdatePayment.setVisibility(View.VISIBLE);
            holder.binding.viewUpdate.setVisibility(View.VISIBLE);
            holder.binding.lvRemain.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvUpdatePayment.setVisibility(View.GONE);
            holder.binding.viewUpdate.setVisibility(View.GONE);
            holder.binding.lvRemain.setVisibility(View.GONE);
        }

        switch (item.getmBkingType()) {
            case "1":

                if (!item.getmBkingTypeCat().isEmpty()) {
                    switch (item.getmBkingTypeCat()) {
                        case "1":
                            holder.binding.tvTripType.setText("City Ride");

                            break;
                        case "2":
                            holder.binding.tvTripType.setText("One Way");

                            break;
                        case "3":
                            holder.binding.tvTripType.setText("Out Station");

                            break;
                        case "4":
                            holder.binding.tvTripType.setText("Airport");

                            break;
                    }
                }

                holder.binding.tvArea.setText(item.getmBkingPickupAddress());
                bookType="1";
                break;

            case "2":

                holder.binding.tvTripType.setText("Self Drive Service");
                holder.binding.tvPersons.setVisibility(View.GONE);
                holder.binding.tvView.setVisibility(View.GONE);
                holder.binding.tvArea.setText(item.getmBranchTitle());
                bookType="2";
                break;
            case "3":

                holder.binding.tvTripType.setText("Luxury Cars Service");
                holder.binding.tvPersons.setVisibility(View.GONE);
                holder.binding.tvView.setVisibility(View.GONE);
                holder.binding.tvPrice.setText("Rs. " + item.getmBkingTotal()+" / km");
                holder.binding.tvArea.setText(item.getmBranchTitle());
                bookType="2";
                break;
            case "4":

                holder.binding.tvTripType.setText("Bus Booking Service");
                holder.binding.tvPersons.setVisibility(View.GONE);
                holder.binding.tvView.setVisibility(View.GONE);
                holder.binding.tvArea.setText(item.getmBranchTitle());
                bookType="2";
                break;
        }

        switch (item.getmBkingStatus()) {
            case "1":
                holder.binding.tvBkingStatus.setText("Pending");
                holder.binding.tvBkingStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.yello));
                break;
            case "2":
                holder.binding.tvBkingStatus.setText("Accepted");
                holder.binding.tvBkingStatus.setTextColor(ContextCompat.getColor(context, R.color.orange));
                break;
            case "3":
                holder.binding.tvBkingStatus.setText("Completed");
                holder.binding.tvBkingStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
                break;
            case "4":
                holder.binding.tvBkingStatus.setText("Cancelled");
                holder.binding.tvBkingStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                break;
        }

        holder.binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(item.getmBkingType().equals("1")){
                    myBookingClickListener.onBookingClick(item, "cancel","1");
                }else{
                    myBookingClickListener.onBookingClick(item, "cancel","2");
                }


            }
        });
        holder.binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(item.getmBkingType().equals("1")){
                    myBookingClickListener.onBookingClick(item, "edit","1");
                }else{
                    myBookingClickListener.onBookingClick(item, "edit","2");
                }


                //myBookingClickListener.onBookingClick(item, "edit",bookType);
            }
        });

        holder.binding.tvUpdatePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateBookingPayment.class);
                intent.putExtra(Constant.BundleExtras.BOOKING_DATA, new Gson().toJson(item));
                context.startActivity(intent);

            }
        });
        holder.binding.tvTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MyBookingItemBinding binding;

        public ViewHolder(MyBookingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
*/


package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.carro.carrorental.R;
import com.carro.carrorental.databinding.MyBookingItemBinding;
import com.carro.carrorental.listener.MyBookingClickListener;
import com.carro.carrorental.model.BookingListModel;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.Utils;

import java.util.List;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder> {

    private final Context context;
    private final List<BookingListModel> items;
    private final MyBookingClickListener myBookingClickListener;

    public BookingListAdapter(Context context, List<BookingListModel> items, MyBookingClickListener myBookingClickListener) {
        this.context = context;
        this.items = items;
        this.myBookingClickListener = myBookingClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyBookingItemBinding binding = MyBookingItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingListModel item = items.get(holder.getAdapterPosition());

        // Hide all layouts by default
        holder.binding.layoutPendingUpcoming.setVisibility(View.GONE);
        holder.binding.layoutAcceptedCurrent.setVisibility(View.GONE);
        holder.binding.layoutCompletedCancelled.setVisibility(View.GONE);

        // Determine which layout to show based on status
        switch (item.getmBkingStatus()) {
            case "1": // Pending / Upcoming
                holder.binding.layoutPendingUpcoming.setVisibility(View.VISIBLE);
                bindPendingUpcomingData(holder, item);
                break;
            case "2": // Accepted / Current
                holder.binding.layoutAcceptedCurrent.setVisibility(View.VISIBLE);
                bindAcceptedCurrentData(holder, item);
                break;
            case "3": // Completed
            case "4": // Cancelled
                holder.binding.layoutCompletedCancelled.setVisibility(View.VISIBLE);
                bindCompletedCancelledData(holder, item);
                break;
            default:
                // Fallback to a default view if status is unknown
                holder.binding.layoutPendingUpcoming.setVisibility(View.VISIBLE);
                bindPendingUpcomingData(holder, item);
                break;
        }
    }

    private void bindPendingUpcomingData(ViewHolder holder, BookingListModel item) {
        holder.binding.tvBookingIdPending.setText(String.format("Booking ID: %s", item.getmBookingId()));
        holder.binding.tvStatusPending.setText(R.string.pending);
        holder.binding.tvServiceNamePending.setText(getServiceName(item));
        holder.binding.tvPickupLocationPending.setText(getPickupLocation(item));
        holder.binding.tvDatetimePending.setText(getFormattedDateTime(item.getmBkingPickup(), item.getmBkingPickupAt()));

        String bookingTypeApi = "1".equals(item.getmBkingType()) ? "1" : "2";

        holder.binding.btnCancelPending.setOnClickListener(v ->
                myBookingClickListener.onBookingClick(item, "cancel", bookingTypeApi));

        holder.binding.btnDetailsPending.setOnClickListener(v ->
                myBookingClickListener.onBookingClick(item, "details", bookingTypeApi));
    }

    private void bindAcceptedCurrentData(ViewHolder holder, BookingListModel item) {
        // TODO: Populate data for the "Current" state
        // Example:
        holder.binding.tvBookingIdCurrent.setText(String.format("Booking ID: %s", item.getmBookingId()));

        holder.binding.tvServiceNameCurrent.setText(getServiceName(item));
        holder.binding.tvPickupLocationCurrent.setText(getPickupLocation(item));
        holder.binding.tvDatetimeCurrent.setText(getFormattedDateTime(item.getmBkingPickup(), item.getmBkingPickupAt()));



        if(item.getmBkingType().equals("2")){
            holder.binding.tvFleetDetails.setVisibility(View.GONE);
            holder.binding.ivLiveLocation.setVisibility(View.GONE);
            holder.binding.tvChauffeurDetails.setVisibility(View.GONE);
        }else {
            holder.binding.tvFleetDetails.setVisibility(View.VISIBLE);
            holder.binding.ivLiveLocation.setVisibility(View.VISIBLE);

            holder.binding.tvChauffeurDetails.setVisibility(View.VISIBLE);

        }

        // 1. Format Fleet (Car) Details
        String carName = item.getmCarName();
        String carNumber = item.getmCarNumber();
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
        holder.binding.tvFleetDetails.setText(formattedCarDetails);


        // 2. Format Chauffeur (Driver) Details
        String driverFullName = item.getmDriverName();
        String formattedDriverDetails = "";

        if (driverFullName != null && !driverFullName.trim().isEmpty()) {
            // Split the name by space to get the first name
            String[] nameParts = driverFullName.trim().split("\\s+");
            String firstName = nameParts[0];

            // Capitalize the first letter of the first name
            formattedDriverDetails = firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " Ji";

        }
        holder.binding.tvChauffeurDetails.setText(formattedDriverDetails);

        String bookingTypeApi = "1".equals(item.getmBkingType()) ? "1" : "2";

        holder.binding.btnCallEmgCurrent.setOnClickListener(v ->
                myBookingClickListener.onBookingClick(item, "emergency_call", bookingTypeApi));

        holder.binding.btnDetailsCurrent.setOnClickListener(v ->
                myBookingClickListener.onBookingClick(item, "details", bookingTypeApi));
    }

    private void bindCompletedCancelledData(ViewHolder holder, BookingListModel item) {
        holder.binding.tvBookingIdCompleted.setText(String.format("Booking ID: %s", item.getmBookingId()));
        holder.binding.tvServiceNameCompleted.setText(getServiceName(item));
        holder.binding.tvPickupLocationCompleted.setText(getPickupLocation(item));
        holder.binding.tvDatetimeCompleted.setText(getFormattedDateTime(item.getmBkingPickup(), item.getmBkingPickupAt()));

        if ("3".equals(item.getmBkingStatus())) { // Completed
            holder.binding.tvStatusCompleted.setText(R.string.completed);
            holder.binding.tvStatusCompleted.setTextColor(ContextCompat.getColor(context, R.color.green));
            holder.binding.tvStatusCompleted.setBackgroundResource(R.drawable.bg_status_completed);
        } else { // "4" for Cancelled
            holder.binding.tvStatusCompleted.setText(R.string.cancelled);
            holder.binding.tvStatusCompleted.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.binding.tvStatusCompleted.setBackgroundResource(R.drawable.bg_status_cancelled);
        }

        String bookingTypeApi = "1".equals(item.getmBkingType()) ? "1" : "2";

        holder.binding.btnDetailsCompleted.setOnClickListener(v ->
                myBookingClickListener.onBookingClick(item, "details", bookingTypeApi));
    }

    private String getServiceName(BookingListModel item) {
        switch (item.getmBkingType()) {
            case "1": // Cab Service
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

    private String getPickupLocation(BookingListModel item) {
        if (item.getmBkingPickupAddress() != null && !item.getmBkingPickupAddress().isEmpty()) {
            return item.getmBkingPickupAddress();
        } else if (item.getmBkingFrom() != null && !item.getmBkingFrom().isEmpty()) {
            return item.getmBkingFrom();
        } else {
            return item.getmBranchTitle(); // Fallback to branch title
        }
    }

    private String getFormattedDateTime(String date, String time) {
        if (date == null || time == null) return "N/A";
        String formattedDate = DateFormater.changeDateFormat(Constant.yyyyMMdd, "dd MMM yyyy", date);
        String formattedTime = Utils.formatTimeString(Constant.HHMMSS, Constant.HHMMSSA, time);
        return String.format("%s, %s", formattedDate, formattedTime);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MyBookingItemBinding binding;

        public ViewHolder(MyBookingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
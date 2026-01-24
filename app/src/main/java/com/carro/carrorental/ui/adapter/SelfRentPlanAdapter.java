/*
package com.logixhunt.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.logixhunt.carrorental.R;
import com.logixhunt.carrorental.databinding.SelfPlanItemBinding;
import com.logixhunt.carrorental.listener.RentPlanClickListener;
import com.logixhunt.carrorental.model.SelfRentPlanModel;
import com.logixhunt.carrorental.utils.NumberUtils;

import java.text.DecimalFormat;
import java.util.List;

public class SelfRentPlanAdapter extends RecyclerView.Adapter<SelfRentPlanAdapter.ViewHolder> {

    Context context;
    List<SelfRentPlanModel> items;

    RentPlanClickListener rentPlanClickListener;
    private int selectedPosition = 0;

    int hour;

    public SelfRentPlanAdapter(Context context, List<SelfRentPlanModel> items, RentPlanClickListener rentPlanClickListener, int hour) {
        this.context = context;
        this.items = items;
        this.rentPlanClickListener = rentPlanClickListener;
        this.hour = hour;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelfPlanItemBinding binding = SelfPlanItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelfRentPlanModel item = items.get(holder.getAdapterPosition());

//        String finalKm=String.valueOf(Double.parseDouble(item.getSdhKm())*hour);
//        String finalAmt=String.valueOf(Double.parseDouble(item.getSdhPrice())*hour);
//        holder.binding.tvPrice.setText("Rs. "+NumberUtils.formatWithDCommas(Integer.parseInt(finalAmt)));

        String finalKm = String.valueOf(Double.parseDouble(item.getSdhKm()) * hour);
        double price = Double.parseDouble(item.getSdhPrice()) * hour;
        DecimalFormat formatter = new DecimalFormat("#,##,###.##"); // Allow decimals
        holder.binding.tvDuration.setText(finalKm + " Km");
        holder.binding.tvPrice.setText("₹ " + formatter.format(price));

        holder.binding.tvDuration.setText(finalKm+" Km");

        int backgroundColor = position == selectedPosition
                ? ContextCompat.getColor(context, R.color.primary)
                : ContextCompat.getColor(context, R.color.primary_field);
        holder.binding.getRoot().setCardBackgroundColor(backgroundColor);

        // Set text color based on selection
        int textColor = position == selectedPosition
                ? ContextCompat.getColor(context, R.color.white)
                : ContextCompat.getColor(context, R.color.black);
        holder.binding.tvDuration.setTextColor(textColor);
        holder.binding.tvPrice.setTextColor(textColor);

        // Handle click to change selected item
        holder.binding.getRoot().setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousPosition); // Update previous item
            notifyItemChanged(selectedPosition); // Update new selected item

            rentPlanClickListener.onRentClick(item);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SelfPlanItemBinding binding;

        public ViewHolder(SelfPlanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
*/


package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.carro.carrorental.R;
import com.carro.carrorental.databinding.SelfPlanItemBinding;
import com.carro.carrorental.listener.RentPlanClickListener;
import com.carro.carrorental.model.SelfRentPlanModel;
import com.carro.carrorental.utils.Constant;

import java.text.DecimalFormat;
import java.util.List;

public class SelfRentPlanAdapter extends RecyclerView.Adapter<SelfRentPlanAdapter.ViewHolder> {

    private final Context context;
    private final List<SelfRentPlanModel> items;
    private final RentPlanClickListener rentPlanClickListener;
    private int selectedPosition = 0;
    private final long totalDurationInMinutes;

    public SelfRentPlanAdapter(Context context, List<SelfRentPlanModel> items, RentPlanClickListener rentPlanClickListener, long totalDurationInMinutes) {
        this.context = context;
        this.items = items;
        this.rentPlanClickListener = rentPlanClickListener;
        this.totalDurationInMinutes = totalDurationInMinutes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelfPlanItemBinding binding = SelfPlanItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelfRentPlanModel item = items.get(holder.getAdapterPosition());

        try {
            double pricePerHour = Double.parseDouble(item.getSdhPrice());
            double pricePerMinute = pricePerHour / 60.0;
            double totalPrice = pricePerMinute * totalDurationInMinutes;

            // Apply 30% discount if duration is over 24 hours (1440 minutes)
            if (totalDurationInMinutes >= 1440) {
                totalPrice *= 0.70; // Applying 30% discount
            }


            double kmPerHour = Double.parseDouble(item.getSdhKm());
            double kmPerMinute = kmPerHour / 60.0;
            double totalKm = kmPerMinute * totalDurationInMinutes;

            DecimalFormat priceFormatter = new DecimalFormat(Constant.CURRENCY_FORMAT);
            DecimalFormat kmFormatter = new DecimalFormat("#,##0");

            holder.binding.tvPrice.setText("₹ " + priceFormatter.format(totalPrice));
            holder.binding.tvDuration.setText(kmFormatter.format(totalKm) + " Km");

        } catch (NumberFormatException e) {
            holder.binding.tvPrice.setText("₹ --");
            holder.binding.tvDuration.setText("-- Km");
            e.printStackTrace();
        }

        int backgroundColor = position == selectedPosition
                ? ContextCompat.getColor(context, R.color.primary)
                : ContextCompat.getColor(context, R.color.primary_field);
        holder.binding.getRoot().setCardBackgroundColor(backgroundColor);

        int textColor = position == selectedPosition
                ? ContextCompat.getColor(context, R.color.white)
                : ContextCompat.getColor(context, R.color.black);
        holder.binding.tvDuration.setTextColor(textColor);
        holder.binding.tvPrice.setTextColor(textColor);

        holder.binding.getRoot().setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            if (previousPosition != selectedPosition) {
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
            }
            rentPlanClickListener.onRentClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SelfPlanItemBinding binding;

        public ViewHolder(SelfPlanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
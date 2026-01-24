



package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.SelfDriveCarItemBinding;
import com.carro.carrorental.listener.SelfCarClickListener;
import com.carro.carrorental.model.SelfCarModel;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.ImagePathDecider;

import java.text.DecimalFormat;
import java.util.List;

public class SelfCarAdapter extends RecyclerView.Adapter<SelfCarAdapter.ViewHolder> {

    private final Context context;
    private final List<SelfCarModel> items;
    private final SelfCarClickListener selfCarClickListener;
    private final String type;
    private final long totalDurationInMinutes;

    public SelfCarAdapter(Context context, List<SelfCarModel> items, SelfCarClickListener selfCarClickListener, String type, long totalDurationInMinutes) {
        this.context = context;
        this.items = items;
        this.selfCarClickListener = selfCarClickListener;
        this.type = type;
        this.totalDurationInMinutes = totalDurationInMinutes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelfDriveCarItemBinding binding = SelfDriveCarItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelfCarModel item = items.get(holder.getAdapterPosition());
        holder.binding.tvCarName.setText(item.getmCtypeTitle());
        holder.binding.tvCarType.setText(item.getmCtypeTitle());

        if ("1".equals(item.getIsBook())) {
            holder.binding.btnDone.setVisibility(View.GONE);
            holder.binding.tvPrice.setVisibility(View.INVISIBLE);
            holder.binding.tvLabel.setVisibility(View.INVISIBLE);
            holder.binding.btnBooked.setVisibility(View.VISIBLE);
        } else {
            holder.binding.btnDone.setVisibility(View.VISIBLE);
            holder.binding.tvPrice.setVisibility(View.VISIBLE);
            holder.binding.tvLabel.setVisibility(View.VISIBLE);
            holder.binding.btnBooked.setVisibility(View.GONE);
        }

        if (!"rent".equals(type)) {
            holder.binding.tvAvailFrom.setVisibility(View.VISIBLE);
            String availDate = item.getAvailDate();
            String futureDate = DateFormater.getFutureDateFrom(availDate, 2);
            holder.binding.tvAvailFrom.setText("Next available from: " + futureDate);
        } else {
            holder.binding.tvAvailFrom.setVisibility(View.GONE);
        }

        // --- NEW AND CORRECTED PRICE CALCULATION LOGIC USING DOUBLE ---

        // Use a consistent DecimalFormat for currency
        DecimalFormat currencyFormatter = new DecimalFormat(Constant.CURRENCY_FORMAT);

        if ("rent".equals(type)) {
            try {
                // Parse the price per hour as a double
                double pricePerHour = Double.parseDouble(item.getpPrice());

                // Calculate the price per minute
                double pricePerMinute = pricePerHour / 60.0;

                // Calculate the total price based on total minutes
                double totalPrice = pricePerMinute * totalDurationInMinutes;

                // Apply 30% discount if duration is over 24 hours (1440 minutes)
                if (totalDurationInMinutes >= 1440) {
                    totalPrice *= 0.70; // Applying 30% discount
                }

                // Format the final price to two decimal places with commas
                String formattedPrice = currencyFormatter.format(totalPrice);
                holder.binding.tvPrice.setText(String.format("₹ %s", formattedPrice));

            } catch (NumberFormatException | NullPointerException e) {
                // Handle cases where pPrice is not a valid number or is null
                holder.binding.tvPrice.setText("₹ --");
                e.printStackTrace();
            }
        } else { // For "sub" type
            try {
                // Parse the subscription price as a double
                double price = Double.parseDouble(item.getSubsPrice());

                // Format the final price to two decimal places with commas
                String formattedPrice = currencyFormatter.format(price);
                holder.binding.tvPrice.setText(String.format("₹ %s", formattedPrice));

            } catch (NumberFormatException | NullPointerException e) {
                holder.binding.tvPrice.setText("₹ --");
                e.printStackTrace();
            }
        }

        Glide.with(context)
                .load(ImagePathDecider.getCarImagePath() + item.getmCtypeImg())
                .error(R.drawable.demo_car)
                .into(holder.binding.ivCar);

        holder.binding.btnDone.setOnClickListener(v -> selfCarClickListener.onSelfCarClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SelfDriveCarItemBinding binding;

        public ViewHolder(SelfDriveCarItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
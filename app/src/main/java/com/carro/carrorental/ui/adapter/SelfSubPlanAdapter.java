package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.carro.carrorental.R;
import com.carro.carrorental.databinding.SelfSubPlanItemBinding;
import com.carro.carrorental.listener.SubsPlanClickListener;
import com.carro.carrorental.model.SelfSubPlanModel;
import com.carro.carrorental.utils.NumberUtils;

import java.util.List;

public class SelfSubPlanAdapter extends RecyclerView.Adapter<SelfSubPlanAdapter.ViewHolder> {

    Context context;
    List<SelfSubPlanModel> items;
    SubsPlanClickListener subsPlanClickListener;

    public SelfSubPlanAdapter(Context context, List<SelfSubPlanModel> items, SubsPlanClickListener subsPlanClickListener) {
        this.context = context;
        this.items = items;
        this.subsPlanClickListener = subsPlanClickListener;
    }

    private int selectedPosition = 0;



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelfSubPlanItemBinding binding = SelfSubPlanItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelfSubPlanModel item = items.get(holder.getAdapterPosition());

        holder.binding.tvDuration.setText(item.getmSubsDay());
        holder.binding.tvPrice.setText("₹ "+NumberUtils.formatWithCommas(Integer.parseInt(item.getmSubsPrice())));




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
            subsPlanClickListener.onSubsClick(item);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SelfSubPlanItemBinding binding;

        public ViewHolder(SelfSubPlanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.BusListItemBinding;
import com.carro.carrorental.listener.BusClickListener;
import com.carro.carrorental.model.BusModel;
import com.carro.carrorental.utils.ImagePathDecider;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {

    Context context;
    List<BusModel> items;
    BusClickListener busClickListener;

    public BusAdapter(Context context, List<BusModel> items, BusClickListener busClickListener) {
        this.context = context;
        this.items = items;
        this.busClickListener = busClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BusListItemBinding binding = BusListItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusModel item = items.get(holder.getAdapterPosition());

        Glide.with(context)
                .load(ImagePathDecider.getCarImagePath()+item.getmCtypeImg())
                .error(R.drawable.image_bus_book)
                .into(holder.binding.ivBus);

        holder.binding.tvSeats.setText(item.getmCtypeTitle());
        holder.binding.tvPrice.setText(item.getmCtypePrice()+"/-");

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busClickListener.onBusClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        BusListItemBinding binding;

        public ViewHolder(BusListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

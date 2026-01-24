package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.CarTypeItemBinding;
import com.carro.carrorental.listener.CarTyprClickListener;
import com.carro.carrorental.model.CarTypeModel;
import com.carro.carrorental.utils.ImagePathDecider;

import java.util.List;

public class CarTypeAdapter extends RecyclerView.Adapter<CarTypeAdapter.ViewHolder> {

    Context context;
    List<CarTypeModel> items;
    CarTyprClickListener carTyprClickListener;
    int pos;

    public CarTypeAdapter(Context context, List<CarTypeModel> items, CarTyprClickListener carTyprClickListener) {
        this.context = context;
        this.items = items;
        this.carTyprClickListener = carTyprClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CarTypeItemBinding binding = CarTypeItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarTypeModel item = items.get(holder.getAdapterPosition());
        holder.binding.tvVehicleName.setText(item.getmCtypeTitle());
        holder.binding.tvVehiclePrice.setText(item.getmCtypeSeat()+" Seater");

        Glide.with(context)
                .load(ImagePathDecider.getCarImagePath()+item.getmCtypeImg())
                .error(R.drawable.demo_car)
                .into(holder.binding.ivVehicle);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

        if (pos == holder.getAdapterPosition()) {
            holder.binding.tvVehicleName.setTextColor(context.getResources().getColor(R.color.primary));
            holder.binding.tvVehiclePrice.setTextColor(context.getResources().getColor(R.color.primary));
            carTyprClickListener.onCarTypeClick(item);

        } else {
            holder.binding.tvVehicleName.setTextColor(context.getResources().getColor(R.color.gray_light2));
            holder.binding.tvVehiclePrice.setTextColor(context.getResources().getColor(R.color.gray_light2));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CarTypeItemBinding binding;

        public ViewHolder(CarTypeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

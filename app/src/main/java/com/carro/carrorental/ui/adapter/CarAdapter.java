package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.SelectCarItemBinding;
import com.carro.carrorental.listener.CarTyprClickListener;
import com.carro.carrorental.model.CarTypeModel;
import com.carro.carrorental.utils.ImagePathDecider;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    Context context;
    List<CarTypeModel> items;
    CarTyprClickListener carTyprClickListener;

    public CarAdapter(Context context, List<CarTypeModel> items, CarTyprClickListener carTyprClickListener) {
        this.context = context;
        this.items = items;
        this.carTyprClickListener = carTyprClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelectCarItemBinding binding = SelectCarItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarTypeModel item = items.get(holder.getAdapterPosition());
        holder.binding.tvCarName.setText(item.getmCtypeTitle());
        holder.binding.tvSeats.setText(item.getmCtypeSeat()+" Seater");
        holder.binding.tvFuel.setText(item.getmCtypeFuel());
        holder.binding.tvCarType.setText(item.getmCtypeTitle());
        holder.binding.tvManual.setText(item.getmCtypeDrivetype().equals("1")?"Automatic":"Manual");
        holder.binding.tvPrice.setText("Price: ₹"+item.getmCtypePrice());

        Glide.with(context)
                .load(ImagePathDecider.getCarImagePath()+item.getmCtypeImg())
                .error(R.drawable.demo_car)
                .into(holder.binding.ivCar);

        holder.binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carTyprClickListener.onCarTypeClick(item);
            }
        });



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SelectCarItemBinding binding;

        public ViewHolder(SelectCarItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carro.carrorental.databinding.SelectPackageItemBinding;
import com.carro.carrorental.listener.PackageClickListener;
import com.carro.carrorental.model.PackageModel;

import java.util.List;

public class SelectPackageAdapter extends RecyclerView.Adapter<SelectPackageAdapter.ViewHolder> {

    Context context;
    List<PackageModel> items;
    PackageClickListener packageClickListener;

    public SelectPackageAdapter(Context context, List<PackageModel> items, PackageClickListener packageClickListener) {
        this.context = context;
        this.items = items;
        this.packageClickListener = packageClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelectPackageItemBinding binding = SelectPackageItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PackageModel item = items.get(holder.getAdapterPosition());
        holder.binding.tvPrice.setText("Rs. "+item.getmPackagePrice());
        holder.binding.tvPerKmPrice.setText(item.getmPackageNote());

        holder.binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               packageClickListener.onPackageClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SelectPackageItemBinding binding;

        public ViewHolder(SelectPackageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

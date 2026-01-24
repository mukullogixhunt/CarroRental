package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.PackageItemBinding;
import com.carro.carrorental.model.PackageModel;
import com.carro.carrorental.ui.activity.PackageDetailActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.ImagePathDecider;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {

    Context context;
    List<PackageModel> items;

    public PackageAdapter(Context context, List<PackageModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PackageItemBinding binding = PackageItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PackageModel item = items.get(holder.getAdapterPosition());

        Glide.with(context)
                .load(ImagePathDecider.getPackageImagePath() + item.getmPackageImg())
                .error(R.drawable.img_no_image)
                .into(holder.binding.ivPackage);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PackageDetailActivity.class);
                intent.putExtra(Constant.BundleExtras.PACKAGE_DATA, new Gson().toJson(item));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PackageItemBinding binding;

        public ViewHolder(PackageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

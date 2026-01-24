package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.HomeCabItemBinding;
import com.carro.carrorental.model.OfferModel;
import com.carro.carrorental.utils.ImagePathDecider;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    Context context;
    List<OfferModel> items;

    public OfferAdapter(Context context, List<OfferModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeCabItemBinding binding = HomeCabItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OfferModel item = items.get(holder.getAdapterPosition());

        Glide.with(context)
                .load(ImagePathDecider.getOfferImagePath()+item.getmOfferImg())
                .error(R.drawable.img_no_image)
                .into(holder.binding.ivCab1);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HomeCabItemBinding binding;

        public ViewHolder(HomeCabItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

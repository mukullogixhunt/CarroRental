package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carro.carrorental.databinding.LocationItemBinding;
import com.carro.carrorental.listener.SearchPlaceClickListener;
import com.carro.carrorental.model.PredictionModel;

import java.util.List;

public class SearchLocationAdapter extends RecyclerView.Adapter<SearchLocationAdapter.ViewHolder> {

    Context context;
    List<PredictionModel> items;
    String type;
    String from;
    SearchPlaceClickListener searchPlaceClickListener;

    public SearchLocationAdapter(Context context, List<PredictionModel> items, SearchPlaceClickListener searchPlaceClickListener) {
        this.context = context;
        this.items = items;
        this.searchPlaceClickListener = searchPlaceClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LocationItemBinding binding = LocationItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PredictionModel item = items.get(holder.getAdapterPosition());

        holder.binding.tvLocation.setText(item.getStructuredFormatting().getMainText());
        holder.binding.tvDesc.setText(item.getDescription());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlaceClickListener.onPlaceClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LocationItemBinding binding;

        public ViewHolder(LocationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

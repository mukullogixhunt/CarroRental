package com.carro.carrorental.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carro.carrorental.R;

import java.io.File;
import java.util.ArrayList;

public class ScratchImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int IMAGE = 1;
    private static final int ADD = 2;

    private final ArrayList<File> list;
    private final Runnable onAddClick;

    public ScratchImageAdapter(ArrayList<File> list, Runnable onAddClick) {
        this.list = list;
        this.onAddClick = onAddClick;
    }

    @Override
    public int getItemViewType(int position) {
        // LAST ITEM = ADD TILE
        return position == list.size() ? ADD : IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ADD) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_scratch_add, parent, false);
            return new AddVH(v);
        }

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_scratch_image, parent, false);
        return new ImgVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof AddVH) {
            holder.itemView.setOnClickListener(v -> onAddClick.run());
            return;
        }

        File file = list.get(position);
        ImgVH vh = (ImgVH) holder;

        Glide.with(vh.iv.getContext())
                .load(file)
                .into(vh.iv);

        vh.ivDelete.setOnClickListener(v -> {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        });
    }

    @Override
    public int getItemCount() {
        // images + 1 ADD tile
        return list.size() + 1;
    }

    static class ImgVH extends RecyclerView.ViewHolder {
        ImageView iv, ivDelete;

        ImgVH(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.ivScratch);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

    static class AddVH extends RecyclerView.ViewHolder {
        AddVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}

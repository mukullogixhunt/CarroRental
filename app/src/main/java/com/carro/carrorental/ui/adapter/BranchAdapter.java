package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.BranchItemBinding;
import com.carro.carrorental.listener.BranchClickListener;
import com.carro.carrorental.model.BranchModel;
import com.carro.carrorental.utils.ImagePathDecider;

import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.ViewHolder> {

    Context context;
    List<BranchModel> items;
    
    BranchClickListener branchClickListener;

    public BranchAdapter(Context context, List<BranchModel> items, BranchClickListener branchClickListener) {
        this.context = context;
        this.items = items;
        this.branchClickListener = branchClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BranchItemBinding binding = BranchItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BranchModel item = items.get(holder.getAdapterPosition());
        
        holder.binding.tvBranch.setText(item.getmBranchTitle());
        Glide.with(context)
                .load(ImagePathDecider.getBranchImagePath()+item.getmBranchIcon())
                .error(R.drawable.ic_branch_location)
                .into(holder.binding.ivBranch);


        holder.binding.tvBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchClickListener.onBranchClick(item);
            }
        });
       
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        BranchItemBinding binding;

        public ViewHolder(BranchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

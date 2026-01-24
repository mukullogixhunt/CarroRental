package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.carro.carrorental.databinding.SliderLayoutBinding;
import com.carro.carrorental.model.SliderModel;
import com.carro.carrorental.utils.ImagePathDecider;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder>  {

    private Context context;
    private List<SliderModel> items;
    private ViewPager2 viewPager2;

    public SliderAdapter(Context context, List<SliderModel> items, ViewPager2 viewPager2) {
        this.context = context;
        this.items = items;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      SliderLayoutBinding mBinding = SliderLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SliderModel item = items.get(position);

        Glide.with(context)
                .load(ImagePathDecider.getSliderImagePath() + item.getmSliderImg())
                /*.placeholder(circularProgressDrawable)*/
                .into(holder.mBinding.sliderImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SliderLayoutBinding mBinding;

        MyViewHolder(SliderLayoutBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}

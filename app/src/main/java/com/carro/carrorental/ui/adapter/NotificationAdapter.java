package com.carro.carrorental.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.NotificationItemBinding;
import com.carro.carrorental.model.NotificationModel;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.DateFormater;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.utils.Utils;


import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    List<NotificationModel> items;

    public NotificationAdapter(Context context, List<NotificationModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationItemBinding binding = NotificationItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel item = items.get(holder.getAdapterPosition());
        holder.binding.tvMsg.setText(item.getmNotifMessage());
        holder.binding.tvTime.setText(Utils.formatTimeString(Constant.HHMMSS,Constant.HHMMSSA,item.getmNotifTime()));
        holder.binding.tvDate.setText(DateFormater.changeDateFormat(Constant.yyyyMMdd,Constant.ddMMyyyy,item.getmNotifDate()));
        if (item.getmNotifStatus().equals("Read")) {
            holder.binding.clNotif.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                            holder.itemView.getContext(),
                            R.color.gray_light4
                    )
            );
        } else {
            holder.binding.clNotif.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                            holder.itemView.getContext(),
                            R.color.primary_field

                    )
            );
        }
        if (position != 0) {
            String prevDate = items.get(position - 1).getmNotifDate();
            if (prevDate.equals(item.getmNotifDate())) {
                holder.binding.tvDate.setVisibility(View.GONE);
            } else {
                holder.binding.tvDate.setVisibility(View.VISIBLE);
            }
        } else {
            holder.binding.tvDate.setVisibility(View.VISIBLE);
        }

        Glide.with(context)
                .load(ImagePathDecider.getNotificationImagePath()+item.getmNotifImage())
                .error(R.drawable.img_no_profile)
                .into(holder.binding.ivNotification);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NotificationItemBinding binding;

        public ViewHolder(NotificationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

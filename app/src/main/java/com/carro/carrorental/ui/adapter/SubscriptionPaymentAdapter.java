package com.carro.carrorental.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.carro.carrorental.R;
import com.carro.carrorental.model.SubscriptionPayment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionPaymentAdapter
        extends RecyclerView.Adapter<SubscriptionPaymentAdapter.VH> {

    public interface OnPayClickListener {
        void onPayClick(SubscriptionPayment item);
    }

    private final List<SubscriptionPayment> items = new ArrayList<>();
    private final OnPayClickListener onPayClick;

    public SubscriptionPaymentAdapter(OnPayClickListener onPayClick) {
        this.onPayClick = onPayClick;
    }

    public void submitList(List<SubscriptionPayment> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder {

        TextView tvSno, tvDate, tvPrice, tvStatus;
        MaterialButton btnPay;

        VH(View view) {
            super(view);
            tvSno = view.findViewById(R.id.tvSno);
            tvDate = view.findViewById(R.id.tvDate);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvStatus = view.findViewById(R.id.tvStatus);
            btnPay = view.findViewById(R.id.btnPay);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscription_payment, parent, false);
        return new VH(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        SubscriptionPayment item = items.get(position);

        // Sno
        holder.tvSno.setText(String.valueOf(position + 1));

        // Date
        holder.tvDate.setText(item.getDate());

        // Price
        holder.tvPrice.setText("₹" + item.getAmount());

        // Status: 1 = Paid
        if ("1".equals(item.getStatus())) {

            holder.tvStatus.setText("Paid");
            holder.tvStatus.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.green)
            );
            holder.btnPay.setVisibility(View.INVISIBLE);

        } else {

            holder.tvStatus.setText("Unpaid");
            holder.tvStatus.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.red)
            );
            holder.btnPay.setVisibility(View.VISIBLE);

            holder.btnPay.setOnClickListener(v -> {
                if (onPayClick != null) {
                    onPayClick.onPayClick(item);
                }
            });
        }
    }
}

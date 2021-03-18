package com.xitij.spintoearn.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xitij.spintoearn.Models.WithdrawHistory;
import com.xitij.spintoearn.R;

import java.util.ArrayList;

public class WithdrawalHistoryAdapter extends RecyclerView.Adapter<WithdrawalHistoryAdapter.WithdrawHistoryViewHolder> {

    public ArrayList<WithdrawHistory> withdrawHistoryArrayList;

    public static class WithdrawHistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDescription;
        public TextView tvDate;
        public TextView tvCoin;
        public TextView tvStatus;
        public LinearLayout lnrBg;

        public WithdrawHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.txtDetails);
            tvDate = itemView.findViewById(R.id.txtdate);
            tvCoin = itemView.findViewById(R.id.txtCoin);
            tvStatus = itemView.findViewById(R.id.txtStatus);
            lnrBg = itemView.findViewById(R.id.lnrBg);
        }
    }

    public WithdrawalHistoryAdapter(ArrayList<WithdrawHistory> withdrawHistory) {
        withdrawHistoryArrayList = withdrawHistory;
    }

    @NonNull
    @Override
    public WithdrawHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View V = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_withdrawal_item, viewGroup, false);
        WithdrawalHistoryAdapter.WithdrawHistoryViewHolder withdrawHistoryViewHolder = new WithdrawalHistoryAdapter.WithdrawHistoryViewHolder(V);
        return withdrawHistoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawHistoryViewHolder withdrawHistoryViewHolder, int i) {
        WithdrawHistory withdrawHistory = withdrawHistoryArrayList.get(i);
        withdrawHistoryViewHolder.tvDescription.setText(withdrawHistory.getDescription());
        withdrawHistoryViewHolder.tvDate.setText(withdrawHistory.getDate());
        withdrawHistoryViewHolder.tvCoin.setText(withdrawHistory.getCoin());
        withdrawHistoryViewHolder.tvStatus.setText(withdrawHistory.getStatus());

        if (i % 2 == 0) {
            withdrawHistoryViewHolder.lnrBg.setBackgroundColor(Color.parseColor("#000000"));
        } else {
            withdrawHistoryViewHolder.lnrBg.setBackgroundColor(Color.parseColor("#202020"));
        }
    }

    @Override
    public int getItemCount() {
        return withdrawHistoryArrayList.size();
    }

}

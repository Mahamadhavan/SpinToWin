package com.xitij.spintoearn.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xitij.spintoearn.Models.CoinHistory;
import com.xitij.spintoearn.R;

import java.util.ArrayList;

public class CoinHistoryAdapter extends RecyclerView.Adapter<CoinHistoryAdapter.CoinHistoryViewHolder> {

    private ArrayList<CoinHistory> coinHistoryArrayList;

    public static class CoinHistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDescription;
        public TextView tvDate;
        public TextView tvCoin;
        public LinearLayout lnrBg;



        public CoinHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription=itemView.findViewById(R.id.tVdesc);
            tvDate=itemView.findViewById(R.id.tvdate);
            tvCoin=itemView.findViewById(R.id.tvcoin);
            lnrBg=itemView.findViewById(R.id.lnrBg);

        }

    }
    public CoinHistoryAdapter(ArrayList<CoinHistory> coinHistory) {
        coinHistoryArrayList=coinHistory;
    }
    @NonNull
    @Override
    public CoinHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View V= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_coin_item,viewGroup,false);
        CoinHistoryViewHolder coinHistoryViewHolder= new CoinHistoryViewHolder(V);
        return coinHistoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoinHistoryViewHolder coinHistoryViewHolder, int i) {
        CoinHistory coinHistory=coinHistoryArrayList.get(i);
        coinHistoryViewHolder.tvDescription.setText(coinHistory.getDescription());
        coinHistoryViewHolder.tvDate.setText(coinHistory.getDate());
        coinHistoryViewHolder.tvCoin.setText(coinHistory.getCoin());

        if (i % 2 == 0){
            coinHistoryViewHolder.lnrBg.setBackgroundColor(Color.parseColor("#000000"));
        }else {
            coinHistoryViewHolder.lnrBg.setBackgroundColor(Color.parseColor("#202020"));
        }

    }

    @Override
    public int getItemCount() {
        return coinHistoryArrayList.size();
    }



}

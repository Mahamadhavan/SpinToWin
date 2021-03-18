package com.xitij.spintoearn.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xitij.spintoearn.Models.AdsItems;
import com.xitij.spintoearn.R;

import java.util.ArrayList;

public class AdsItemsAdapter extends RecyclerView.Adapter<AdsItemsAdapter.AdsItemsViewHolder> {

    public ArrayList<AdsItems> adsItemsArrayList;
    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class AdsItemsViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView txt_ads_tittle;
        public TextView txt_ads_description;
        public TextView txt_ads_points;

        public AdsItemsViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_ads_icon);
            txt_ads_tittle=itemView.findViewById(R.id.item_ads_title);
            txt_ads_description=itemView.findViewById(R.id.item_ads_description);
            txt_ads_points=itemView.findViewById(R.id.item_ads_cost);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
    public AdsItemsAdapter(ArrayList<AdsItems> adsItems){
        adsItemsArrayList=adsItems;
    }
    @NonNull
    @Override
    public AdsItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_item,viewGroup,false);
        AdsItemsViewHolder adsItemsViewHolder=new AdsItemsViewHolder(v,mListener);
        return adsItemsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdsItemsViewHolder adsItemsViewHolder, int i) {

        AdsItems adsItems=adsItemsArrayList.get(i);
        adsItemsViewHolder.imageView.setImageResource(adsItems.getAds_icon());
        adsItemsViewHolder.txt_ads_tittle.setText(adsItems.getAds_tittle());
        adsItemsViewHolder.txt_ads_description.setText(adsItems.getAds_description());
        adsItemsViewHolder.txt_ads_points.setText(adsItems.getAds_points());

    }

    @Override
    public int getItemCount() {
        return adsItemsArrayList.size();
    }




}

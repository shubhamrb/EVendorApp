package com.dbcorp.vendorapp.adapter;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.Coupon;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class CuponAdapter extends RecyclerView.Adapter<CuponAdapter.MyViewHolder> {


    ArrayList<Coupon> listData;
    private final OnMeneuClickListnser onMenuListClicklistener;
    static int  getPosition;
    private int row_index;
    Context mContext;
    int select;

    public CuponAdapter(ArrayList<Coupon> list, OnMeneuClickListnser onLiveTestClickListener, Context context) {
        this.listData = list;
        this.onMenuListClicklistener = onLiveTestClickListener;
        this.mContext=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cupon_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Coupon data=listData.get(position);
        holder.tvCoupon.setText(data.getCouponCode());
        holder.tvDate.setText(data.getEnddate());
        holder.tvDes.setText(data.getDescription());
        if(data.getActive().equalsIgnoreCase("1")){
            holder.tvStatus.setText("Not Active");
            holder.tvStatus.setBackgroundResource(R.drawable.red_gredient_rect_bg);
        }else{
            holder.tvStatus.setText("Active");
            holder.tvStatus.setBackgroundResource(R.drawable.green_gredient_rect_bg);

        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tvDes,tvCoupon,tvDate,tvStatus;
         MyViewHolder(View view) {
            super(view);
            tvDate=view.findViewById(R.id.tvDate);
            tvCoupon=view.findViewById(R.id.tvCoupon);
             tvStatus=view.findViewById(R.id.tvStatus);
            tvDes=view.findViewById(R.id.tvDes);

         }
    }
    public interface OnMeneuClickListnser{
        void onOptionClick(String liveTest, int pos);
    }


 }



package com.dbcorp.vendorapp.adapter;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.CustomerServiceBooking;
import com.dbcorp.vendorapp.model.VendorService;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class BookingLayoutAdapter extends RecyclerView.Adapter<BookingLayoutAdapter.MyViewHolder> {

    private ArrayList<CustomerServiceBooking> listData = new ArrayList<>();
    private final OnMeneuClickListnser onMenuListClicklistener;
    Context mContext;


    public BookingLayoutAdapter(ArrayList<CustomerServiceBooking> getListData, OnMeneuClickListnser onLiveTestClickListener, Context context) {
        this.listData = getListData;
        this.onMenuListClicklistener = onLiveTestClickListener;
        this.mContext = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_booking_customer_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CustomerServiceBooking data = listData.get(position);
        holder.tvName.setText(data.getName());

//        if (data.getStatus().equals("1")) {
//            holder.activeStatus.setText("Active");
//            holder.activeStatus.setTextColor(Color.RED);
//        } else {
//            holder.activeStatus.setText("DeActive");
//            holder.activeStatus.setTextColor(Color.GREEN);
//        }
        holder.tvNumber.setText(data.getMobile());
        holder.cardView.setOnClickListener(v -> {
            onMenuListClicklistener.onOptionClick(data, position);
        });


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
        MaterialTextView tvName, tvNumber, activeStatus;

        MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            activeStatus = view.findViewById(R.id.activeStatus);
            tvNumber = view.findViewById(R.id.tvNumber);
            cardView = view.findViewById(R.id.cardView);


        }
    }

    public interface OnMeneuClickListnser {
        void onOptionClick(CustomerServiceBooking data, int pos);
    }


}



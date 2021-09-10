package com.dbcorp.vendorapp.adapter;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.OrderDetails;
import com.google.android.material.textview.MaterialTextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class RecentOrderListAdapter extends RecyclerView.Adapter<RecentOrderListAdapter.MyViewHolder> {

    private String arrItems[];
    private final OnMeneuClickListnser onMenuListClicklistener;
    static int getPosition;
    private int row_index;
    Context mContext;
    int select;

    int viewType;
    ArrayList<OrderDetails> listData;

    public RecentOrderListAdapter(String viewType, ArrayList<OrderDetails> getlistData, OnMeneuClickListnser onLiveTestClickListener, Context context) {
        this.listData = getlistData;
        this.onMenuListClicklistener = onLiveTestClickListener;
        this.viewType = Integer.parseInt(viewType);
        this.mContext = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;



        if(viewType==0){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_order_item, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_order_item_pending, parent, false);

        }


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderDetails data = listData.get(position);


        holder.tvStatus.setText(data.getStatusName());
        holder.tvOrder.setText(data.getOrderNumber());
        holder.orderName.setText(data.getDescription());
        holder.tvStatus.setTextColor(Color.parseColor(data.getStatusColor()));
        holder.Img.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(data.getStatusColor())));

        String splite[] = data.getAddDate().split(" ");
        String dateName[] = splite[0].split("-");
        holder.orderDate.setText("Order Date " + dateName[2] + "-" + new DateFormatSymbols().getMonths()[Integer.parseInt(dateName[1].replace("0", "")) - 1] + "-" + dateName[0]);


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvOrder, orderName, orderDate, tvStatus;

        AppCompatImageView Img;

        MyViewHolder(View view) {
            super(view);
            tvStatus = view.findViewById(R.id.tvStatus);
            Img = view.findViewById(R.id.Img);
            tvOrder = view.findViewById(R.id.tvOrder);
            orderName = view.findViewById(R.id.orderName);
            orderDate = view.findViewById(R.id.orderDate);

        }
    }

    public interface OnMeneuClickListnser {
        void onOptionClick(OrderDetails data, int pos);
    }


}



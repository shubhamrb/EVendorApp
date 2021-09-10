package com.dbcorp.vendorapp.adapter.order;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */
 
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.orderview.CustomerOrderDetails;
import com.dbcorp.vendorapp.model.orderview.OrderParam;
import com.dbcorp.vendorapp.model.orderview.Orders;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


public class OrdersDetailsAdapter extends RecyclerView.Adapter<OrdersDetailsAdapter.MyViewHolder> implements  OrdersAdapter.OnMeneuClickListnser {

    private final  OnMeneuClickListnser onMenuListClicklistener;
    ArrayList<CustomerOrderDetails> listData;
    OrdersAdapter ordersAdapter;
    static int  getPosition;
    private int row_index;
    Context mContext;
    int select;
String status;
    public OrdersDetailsAdapter(ArrayList<CustomerOrderDetails> list,  OnMeneuClickListnser onLiveTestClickListener, Context context,String status) {
        this.listData = list;
        this.onMenuListClicklistener = onLiveTestClickListener;
        this.mContext=context;
this.status=status;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_order_list, parent, false);


        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CustomerOrderDetails data=listData.get(position);
        OrderParam orderParam=data.getOrderDetails();
        holder.tvDate.setText(orderParam.getOrderDate());
        holder.tvOrderNumber.setText(orderParam.getOrderNumber());
        holder.tvCusName.setText(orderParam.getCustomerName());
        holder.tvTotalPrice.setText(orderParam.getTotal());




        String qnty="0";
        if(data.getProductDetails().size()>0 ){
            qnty=String.valueOf(data.getProductDetails().size());
            holder.viewDet.setVisibility(View.VISIBLE);


        }else{

            qnty="0";
            holder.viewDet.setVisibility(View.GONE);
        }


        holder.tvShow.setOnClickListener(v->{
            if (data.getViewList()){
                data.setViewList(false);
                notifyDataSetChanged();
            }else{
                data.setViewList(true);
                notifyDataSetChanged();
            }

        });
        if(data.getViewList()){
            holder.listItem.setVisibility(View.VISIBLE);
            holder.tvShow.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);

        }else {
            holder.listItem.setVisibility(View.GONE);
            holder.tvShow.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);

        }

        holder.tvQnty.setText("Quantity : "+qnty);
        ordersAdapter = new OrdersAdapter(status,data.getProductDetails(), this, mContext);
        holder.listItem.setAdapter(ordersAdapter);
        holder.tvAddress.setText(orderParam.getAddress());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }



    @Override
    public void onOptionClick(ArrayList<Orders> list, int pos) {
         onMenuListClicklistener.selectProduct(listData);
    }

    @Override
    public void orderAssign(Orders data, int pos) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tvAddress,tvDate,tvQnty,tvOrderNumber,tvCusName,tvTotalPrice;
        RecyclerView listItem;
        AppCompatImageView tvShow;
        LinearLayoutCompat viewDet;
         MyViewHolder(View view) {
            super(view);
             listItem = itemView.findViewById(R.id.listItem);
             tvDate=view.findViewById(R.id.tvDate);
             tvShow=view.findViewById(R.id.tvShow);
             tvAddress=view.findViewById(R.id.tvAddress);
             tvQnty=view.findViewById(R.id.tvQnty);
             tvOrderNumber=view.findViewById(R.id.tvOrderNumber);
             viewDet=view.findViewById(R.id.viewDet);
             tvCusName=view.findViewById(R.id.tvCusName);

             tvTotalPrice=view.findViewById(R.id.tvTotalPrice);
         }
    }
    public interface OnMeneuClickListnser{
        void viewDetails(CustomerOrderDetails data, int pos);
        void selectProduct(ArrayList<CustomerOrderDetails> listData);
    }


 }



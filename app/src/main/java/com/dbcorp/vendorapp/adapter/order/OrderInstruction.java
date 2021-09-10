package com.dbcorp.vendorapp.adapter.order;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.orderview.Instruction;
import com.dbcorp.vendorapp.model.orderview.OrderProduct;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.dbcorp.vendorapp.network.ApiService.IMG_PRODUCT_URL;

public class OrderInstruction extends RecyclerView.Adapter<OrderInstruction.MyViewHolder> {


    ArrayList<Instruction> listData;
    private final OnMeneuClickListnser onMenuListClicklistener;
    static int  getPosition;
    private int row_index;
    Context mContext;
    int select;

    public OrderInstruction(ArrayList<Instruction> list, OnMeneuClickListnser onLiveTestClickListener, Context context) {
        this.listData = list;
        this.onMenuListClicklistener = onLiveTestClickListener;
        this.mContext=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Instruction data=listData.get(position);
holder.tvName.setText(data.getName());


holder.cardView.setOnClickListener(v->{
    onMenuListClicklistener.onOptionClick(data,position);
});
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView  tvName;
        MaterialCardView cardView;
         MyViewHolder(View view) {
            super(view);
             tvName=view.findViewById(R.id.tvName);

             cardView=view.findViewById(R.id.cardView);
         }
    }
    public interface OnMeneuClickListnser{
        void onOptionClick(Instruction data, int pos);
    }


 }



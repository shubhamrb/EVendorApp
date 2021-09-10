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
import com.dbcorp.vendorapp.model.Category;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MyViewHolder> {


    private ArrayList<Category> listData;
    private final OnMeneuClickListnser onMenuListClicklistener;
    static int  getPosition;
    private int row_index;
    Context mContext;
    int select;

    public InventoryAdapter(ArrayList<Category> getListData, OnMeneuClickListnser onLiveTestClickListener, Context context) {
        this.listData = getListData;
        this.onMenuListClicklistener = onLiveTestClickListener;

        this.mContext=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category data=listData.get(position);

holder.cardView.setOnClickListener(v->{
    onMenuListClicklistener.onOptionClick(data,position);
});

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
         MyViewHolder(View view) {
            super(view);
             cardView=view.findViewById(R.id.cardView);


        }
    }
    public interface OnMeneuClickListnser{
        void onOptionClick(Category data, int pos);
    }


 }



package com.dbcorp.vendorapp.adapter;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private String arrItems[];
    private final OnMeneuClickListnser onMenuListClicklistener;
    static int  getPosition;
    private int row_index;
    Context mContext;
    int select;
    ArrayList<Product> listData;
    public ProductAdapter(ArrayList<Product> listData, OnMeneuClickListnser onLiveTestClickListener, Context context) {
        this.listData = listData;
        this.onMenuListClicklistener = onLiveTestClickListener;

        this.mContext=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product menuName=listData.get(position);

holder.cardView.setOnClickListener(v->{
    onMenuListClicklistener.onOptionClick(menuName,position);
});

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

       LinearLayoutCompat cardView;
         MyViewHolder(View view) {
            super(view);

             cardView=view.findViewById(R.id.cardView);

        }
    }
    public interface OnMeneuClickListnser{
        void onOptionClick(Product liveTest, int pos);
    }


 }



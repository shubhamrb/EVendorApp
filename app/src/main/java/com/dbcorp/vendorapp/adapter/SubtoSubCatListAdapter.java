package com.dbcorp.vendorapp.adapter;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.SubToSubCategory;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class SubtoSubCatListAdapter extends RecyclerView.Adapter<SubtoSubCatListAdapter.MyViewHolder> {

    private String arrItems[];
    private final OnMeneuClickListnser onMenuListClicklistener;
    static int  getPosition;
    private int row_index;
    Context mContext;
    int select;

    ArrayList<SubToSubCategory> listData;

    public SubtoSubCatListAdapter(ArrayList<SubToSubCategory> listData, OnMeneuClickListnser onLiveTestClickListener, Context context) {
        this.listData = listData;
        this.onMenuListClicklistener = onLiveTestClickListener;

        this.mContext=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.homemenu, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubToSubCategory data=listData.get(position);

        holder.nameText.setText(data.getName());
        //holder.number.setText(ticket.getTicketNumber());


        holder.openpanel.setOnClickListener(v -> {
            select=position;
            onMenuListClicklistener.onOptionClick(data,position);
             notifyDataSetChanged();
        });
if(select==position){
    holder.viewBorder.setVisibility(View.VISIBLE);
}else{
    holder.viewBorder.setVisibility(View.GONE);
}

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number,nameText;
        MaterialCardView openpanel;
        View viewBorder;
        MyViewHolder(View view) {
            super(view);

            openpanel=view.findViewById(R.id.openpanel);
            viewBorder=view.findViewById(R.id.border);
            nameText=view.findViewById(R.id.name);



        }
    }
    public interface OnMeneuClickListnser{
        void onOptionClick(SubToSubCategory liveTest, int pos);
    }


 }



package com.dbcorp.vendorapp.adapter.chating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.BookingServiceChat;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatingRequestAdapter extends RecyclerView.Adapter<ChatingRequestAdapter.ViewHolder> {
    private final OnClickListener onClickListener;
    Context mContext;
    ArrayList<BookingServiceChat> list;

    public ChatingRequestAdapter(ArrayList<BookingServiceChat> list, OnClickListener onClickListener, Context mContext) {
        this.onClickListener = onClickListener;
        this.mContext = mContext;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chating_booking_request_item, parent, false);


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingServiceChat data = list.get(position);
        holder.vendorName.setText(data.getName());
holder.cardView.setOnClickListener(v->{
    onClickListener.onClickCard(data);
});
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView vendorName;
        CircleImageView vendImg;
MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            vendImg = itemView.findViewById(R.id.vendImg);
            vendorName = itemView.findViewById(R.id.vendorName);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public interface OnClickListener {
        void onClickCard(BookingServiceChat list);
    }
}

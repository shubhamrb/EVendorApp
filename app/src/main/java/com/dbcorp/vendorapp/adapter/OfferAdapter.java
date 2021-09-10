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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.OfferModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import static com.dbcorp.vendorapp.network.Constants.IMAGE_OFFER_LATTER;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {

    private String arrItems[];
    private final OnMeneuClickListnser onMenuListClicklistener;
    static int  getPosition;
    private int row_index;
    ArrayList<OfferModel> listData;
    Context mContext;
    int select;

    public OfferAdapter(ArrayList<OfferModel> list, OnMeneuClickListnser onLiveTestClickListener, Context context) {

        this.listData=list;
        this.onMenuListClicklistener = onLiveTestClickListener;

        this.mContext=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OfferModel data=listData.get(position);

//        RequestOptions options = new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo);
//
//        Glide.with(mContext).load(IMAGE_OFFER_LATTER+data.getPic()).apply(options).into(holder.img);

        if(data.getActive().equalsIgnoreCase("1")){
            holder.tvStatus.setText("Not Active");
            holder.tvStatus.setBackgroundResource(R.drawable.red_gredient_rect_bg);
        }else{
            holder.tvStatus.setText("Active");
            holder.tvStatus.setBackgroundResource(R.drawable.green_gredient_rect_bg);

        }
        Glide.with(mContext.getApplicationContext())
                .load(IMAGE_OFFER_LATTER+data.getPhoto())
                .placeholder(R.drawable.logo)
                .into(holder.img);

        holder.tittle.setText(data.getTitle());
        holder.tvDes.setText(data.getDescription());
holder.card_View.setOnClickListener(v->{
    onMenuListClicklistener.onOptionClick(data,position);
});

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tittle,tvDes,tvStatus;
        ShapeableImageView img;
        MaterialCardView card_View;
         MyViewHolder(View view) {
            super(view);
             tvDes=view.findViewById(R.id.tvDes);
             tittle=view.findViewById(R.id.tittle);
             tvStatus=view.findViewById(R.id.tvStatus);
             img=view.findViewById(R.id.img);

             card_View=view.findViewById(R.id.card_view);


        }
    }
    public interface OnMeneuClickListnser{
        void onOptionClick(OfferModel data, int pos);
    }


 }



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

import com.bumptech.glide.Glide;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.WareHouseProduct;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.dbcorp.vendorapp.network.ApiService.IMG_PRODUCT_URL;

public class WarehouseProductAdapter extends RecyclerView.Adapter<WarehouseProductAdapter.MyViewHolder> {


    private final OnMeneuClickListnser onMenuListClicklistener;

    Context mContext;

    ArrayList<WareHouseProduct> listData;
    public WarehouseProductAdapter(ArrayList<WareHouseProduct> listData, OnMeneuClickListnser onLiveTestClickListener, Context context) {
        this.listData = listData;
        this.onMenuListClicklistener = onLiveTestClickListener;

        this.mContext=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_product_view, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WareHouseProduct menuName=listData.get(position);
        holder.productName.setText(menuName.getName());
        holder.catName.setText(menuName.getCategoryName());
        holder.tvTotPrice.setVisibility(View.GONE);
        holder.actualPrice.setVisibility(View.GONE);
        holder.tvTotalPrice.setVisibility(View.GONE);
        holder.tvQuantity.setVisibility(View.GONE);
        holder.subCat.setText(menuName.getSubCategoryName());
        holder.subtosubCat.setText(menuName.getSubSubCategoryName());
        holder.addStore.setVisibility(View.VISIBLE);
        holder.tvVariantValue.setText(menuName.getVariantValue());
        holder.addStore.setOnClickListener(v->{
            onMenuListClicklistener.onOptionClick(menuName,position);
        });
        Glide.with(mContext)
                .load(IMG_PRODUCT_URL+menuName.getPhoto())
                .into(holder.productImg);
holder.cardView.setOnClickListener(v->{
    onMenuListClicklistener.onOptionClick(menuName,position);
});

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView productImg;
        MaterialTextView addStore,productName,tvVariantValue,catName,subCat,subtosubCat,actualPrice,tvQuantity,tvTotPrice,tvTotalPrice;
        MaterialCardView cardView;
         MyViewHolder(View view) {
            super(view);
             productImg=view.findViewById(R.id.productImg);
             productName=view.findViewById(R.id.productName);
             catName=view.findViewById(R.id.catName);
             tvVariantValue=view.findViewById(R.id.tvVariantValue);
             addStore=view.findViewById(R.id.addStore);
             subCat=view.findViewById(R.id.subCat);
             subtosubCat=view.findViewById(R.id.subtosubCat);
             actualPrice=view.findViewById(R.id.actualPrice);
             tvQuantity=view.findViewById(R.id.tvQuantity);
             tvTotPrice=view.findViewById(R.id.tvTotPrice);
             tvTotalPrice=view.findViewById(R.id.tvTotalPrice);
             cardView=view.findViewById(R.id.cardView);

        }
    }
    public interface OnMeneuClickListnser{
        void onOptionClick(WareHouseProduct liveTest, int pos);
    }


 }



package com.dbcorp.vendorapp.adapter;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.VendorShopProduct;
import com.dbcorp.vendorapp.model.WareHouseProduct;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.dbcorp.vendorapp.network.ApiService.IMG_PRODUCT_URL;

public class VendorShopProductAdapter extends RecyclerView.Adapter<VendorShopProductAdapter.MyViewHolder> {


    private final OnMeneuClickListnser onMenuListClicklistener;

    Context mContext;

    ArrayList<VendorShopProduct> listData;

    public VendorShopProductAdapter(ArrayList<VendorShopProduct> listData, OnMeneuClickListnser onLiveTestClickListener, Context context) {
        this.listData = listData;
        this.onMenuListClicklistener = onLiveTestClickListener;

        this.mContext = context;

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
        VendorShopProduct menuName = listData.get(position);
        holder.productName.setText(menuName.getName());
        holder.catName.setText(menuName.getCategoryName());
        holder.tvTotPrice.setVisibility(View.VISIBLE);
        holder.actualPrice.setVisibility(View.VISIBLE);
        holder.tvTotalPrice.setVisibility(View.VISIBLE);
        holder.tvQuantity.setVisibility(View.VISIBLE);
        holder.pdSwitch.setVisibility(View.VISIBLE);
        int tp = Integer.parseInt(menuName.getQuantity()) * Integer.parseInt(menuName.getPrice());

        holder.actualPrice.setText(menuName.getPrice() + "X" + menuName.getQuantity() + " = " + tp);
        //  holder.tvQuantity.setText(menuName.getQuantity());
        holder.tvTotPrice.setText("₹ " + menuName.getPrice());
        holder.subCat.setText(menuName.getSubCategoryName());
        holder.subtosubCat.setText(menuName.getSubSubCategoryName());
        holder.addStore.setVisibility(View.GONE);
        holder.tvVariantValue.setText(menuName.getVariantValue());
        holder.addStore.setVisibility(View.VISIBLE);
        holder.addStore.setText("Edit");

        int imgResource = R.drawable.ic_baseline_edit_24;
        holder.addStore.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
        holder.addStore.setCompoundDrawablePadding(10);
        holder.addStore.setOnClickListener(v -> {
            onMenuListClicklistener.onOptionClick(menuName);
        });


        if(menuName.getActive().equalsIgnoreCase("1")) {
            holder.pdSwitch.setChecked(true);
        } else {
            holder.pdSwitch.setChecked(false);
        }

        holder.pdSwitch.setOnClickListener(v->{
            onMenuListClicklistener.onProductSwitch(menuName);
        });
        Glide.with(mContext)
                .load(IMG_PRODUCT_URL + menuName.getPhoto())
                .into(holder.productImg);


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView productImg;
        MaterialTextView addStore, productName, tvVariantValue, catName, subCat, subtosubCat, actualPrice, tvQuantity, tvTotPrice, tvTotalPrice;
        MaterialCardView cardView;
        SwitchCompat pdSwitch;

        MyViewHolder(View view) {
            super(view);
            productImg = view.findViewById(R.id.productImg);
            productName = view.findViewById(R.id.productName);
            catName = view.findViewById(R.id.catName);
            pdSwitch = view.findViewById(R.id.pdSwitch);
            tvVariantValue = view.findViewById(R.id.tvVariantValue);
            addStore = view.findViewById(R.id.addStore);
            subCat = view.findViewById(R.id.subCat);
            subtosubCat = view.findViewById(R.id.subtosubCat);
            actualPrice = view.findViewById(R.id.actualPrice);
            tvQuantity = view.findViewById(R.id.tvQuantity);
            tvTotPrice = view.findViewById(R.id.tvTotPrice);
            tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
            cardView = view.findViewById(R.id.cardView);

        }
    }

    public interface OnMeneuClickListnser {
        void onOptionClick(VendorShopProduct liveTest);

        void onProductSwitch(VendorShopProduct data);
    }


}



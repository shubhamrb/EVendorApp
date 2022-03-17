package com.dbcorp.vendorapp.adapter;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */

import static com.dbcorp.vendorapp.network.ApiService.IMG_PRODUCT_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.PaymentRequestModel;
import com.dbcorp.vendorapp.model.ProductRequestModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductRequestAdapter extends RecyclerView.Adapter<ProductRequestAdapter.MyViewHolder> {
    private ArrayList<ProductRequestModel> listData;

    Context mContext;

    public ProductRequestAdapter(ArrayList<ProductRequestModel> getListData, Context context) {
        this.listData = getListData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_request_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductRequestModel data = listData.get(position);

        Glide.with(mContext)
                .load(IMG_PRODUCT_URL + data.getPhoto())
                .into(holder.productImg);

        holder.date.setText(data.getAdd_date());
        holder.tvCategoryName.setText(data.getCategory());
        holder.tvProductName.setText(data.getProduct_name());
        holder.tvDesc.setText(data.getDescription());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView date, tvCategoryName, tvProductName, tvDesc;
        CircleImageView productImg;
        MyViewHolder(View view) {
            super(view);

            productImg = view.findViewById(R.id.productImg);
            date = view.findViewById(R.id.date);
            tvCategoryName = view.findViewById(R.id.tvCategoryName);
            tvProductName = view.findViewById(R.id.tvProductName);
            tvDesc = view.findViewById(R.id.tvDesc);

        }
    }
}



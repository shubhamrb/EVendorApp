package com.dbcorp.vendorapp.adapter.serviceprovider;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.OfferModel;
import com.dbcorp.vendorapp.model.ServicePackage;
import com.dbcorp.vendorapp.model.ServicePackageDetail;
import com.dbcorp.vendorapp.model.ServicePackagePoint;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import static com.dbcorp.vendorapp.network.Constants.IMAGE_OFFER_LATTER;

public class ServicePlanAdapter extends RecyclerView.Adapter<ServicePlanAdapter.MyViewHolder> implements ServicePlanPointAdapter.OnClickListener {

    private final OnClickListener onClicklistener;

    ArrayList<ServicePackage> listData;
    Context mContext;
    ServicePlanPointAdapter servicePlanPointAdapter;

    ArrayList<ServicePackagePoint> pointList;
    ArrayList<ServicePackage> servicePackages;

    public ServicePlanAdapter(ArrayList<ServicePackage> list, OnClickListener onLiveTestClickListener, Context context) {

        this.listData = list;
        this.onClicklistener = onLiveTestClickListener;

        this.mContext = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ServicePackage data = listData.get(position);
        ServicePackageDetail servicePackageDetail = data.getServicePackageDetails().get(0);
        holder.planName.setText(servicePackageDetail.getName());
        holder.tvDays.setText("For " + servicePackageDetail.getNoOfDays() + " Days");
        holder.tvDiscountPrice.setText("Rs . " + servicePackageDetail.getDiscountPrice() + "/");
        holder.tvActualPrice.setText(servicePackageDetail.getPrice());
        holder.tvActualPrice.setPaintFlags(holder.tvActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (servicePackageDetail.getPrice().equalsIgnoreCase("0")) {
            holder.buyBtn.setText("Free");
        } else {
            holder.buyBtn.setText("Buy Now");
        }
        holder.buyBtn.setOnClickListener(v -> {
            onClicklistener.onOptionClick(data, position);
        });
        if (data.getServicePackagePoints()!=null){
            servicePlanPointAdapter = new ServicePlanPointAdapter(data.getServicePackagePoints(), ServicePlanAdapter.this::onOptionClick, mContext);
            holder.planList.setAdapter(servicePlanPointAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void onOptionClick(ServicePackagePoint data, int pos) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RecyclerView planList;
        MaterialTextView planName, tvDays, tvDiscountPrice, tvActualPrice, buyBtn;

        MyViewHolder(View view) {
            super(view);
            planName = view.findViewById(R.id.planName);
            tvDiscountPrice = view.findViewById(R.id.tvDiscountPrice);
            tvActualPrice = view.findViewById(R.id.tvActualPrice);
            tvDays = view.findViewById(R.id.tvDays);
            buyBtn = view.findViewById(R.id.buyBtn);
            planList = view.findViewById(R.id.planPointList);
            planList.setHasFixedSize(true);
            planList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        }
    }

    public interface OnClickListener {
        void onOptionClick(ServicePackage data, int pos);
    }


}



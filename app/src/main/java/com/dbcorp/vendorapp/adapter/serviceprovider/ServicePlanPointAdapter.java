package com.dbcorp.vendorapp.adapter.serviceprovider;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.OfferModel;
import com.dbcorp.vendorapp.model.ServicePackage;
import com.dbcorp.vendorapp.model.ServicePackagePoint;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ServicePlanPointAdapter extends RecyclerView.Adapter<ServicePlanPointAdapter.MyViewHolder> {

    private final OnClickListener onClicklistener;

    ArrayList<ServicePackagePoint> listData;
    Context mContext;

    public ServicePlanPointAdapter(ArrayList<ServicePackagePoint> list, OnClickListener onLiveTestClickListener, Context context) {

        this.listData=list;
        this.onClicklistener = onLiveTestClickListener;

        this.mContext=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_list_name, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ServicePackagePoint data=listData.get(position);
        holder.tvName.setText(data.getDetail());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvName;

         MyViewHolder(View view) {
            super(view);
             tvName=view.findViewById(R.id.tvName);
         }
    }
    public interface OnClickListener{
        void onOptionClick(ServicePackagePoint data, int pos);
    }


 }



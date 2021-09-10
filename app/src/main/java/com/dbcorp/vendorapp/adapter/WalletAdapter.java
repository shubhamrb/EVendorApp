package com.dbcorp.vendorapp.adapter;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.Category;
import com.dbcorp.vendorapp.model.DroupDownModel;
import com.dbcorp.vendorapp.model.WalletsData;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MyViewHolder> implements Filterable {
    private ArrayList<WalletsData> listData;
    private ArrayList<WalletsData> searchArray;

    private final OnMeneuClickListnser onMenuListClicklistener;
    Context mContext;
    public WalletAdapter(ArrayList<WalletsData> getListData, OnMeneuClickListnser onLiveTestClickListener, Context context) {
        this.listData = getListData;
        this.searchArray=new ArrayList<>();
        this.onMenuListClicklistener = onLiveTestClickListener;
        this.searchArray.addAll(getListData);
        this.mContext=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WalletsData data=listData.get(position);

        holder.tvTransaction.setText(data.getWalletTxnID());
        holder.tvRemark.setText(data.getRemark());
        holder.tvAmount.setText(data.getAmount()+ "  ("+data.getStatus()+")");

        holder.payMode.setText(data.getPayType());

        holder.cardView.setOnClickListener(v->{
          onMenuListClicklistener.onOptionClick(data,position);
        });


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterString = charSequence.toString().toLowerCase();
                //arraylist1.clear();
                FilterResults results = new FilterResults();
                int count = searchArray.size();
                final List<WalletsData> list = new ArrayList<>();

                WalletsData filterableString;

                for (int i = 0; i < count; i++) {
                    filterableString = searchArray.get(i);
                    if (searchArray.get(i).getStatus().toLowerCase().contains(filterString)) {
                        list.add(filterableString);
                    }


                }

                results.values = list;
                results.count = list.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listData = (ArrayList<WalletsData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
        MaterialTextView tvTransaction,tvRemark,tvAmount,payMode;
         MyViewHolder(View view) {
            super(view);

             tvTransaction=view.findViewById(R.id.tvTransaction);
             cardView=view.findViewById(R.id.cardView);
             tvRemark=view.findViewById(R.id.tvRemark);
             tvAmount=view.findViewById(R.id.tvAmount);
             payMode=view.findViewById(R.id.payMode);

        }
    }
    public interface OnMeneuClickListnser{
        void onOptionClick(WalletsData data, int pos);
    }


 }



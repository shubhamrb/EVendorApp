package com.dbcorp.vendorapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.model.DroupDownModel;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxMenuAdapter extends RecyclerView.Adapter<CheckBoxMenuAdapter.MyViewHolder> implements Filterable {

    private ArrayList<DroupDownModel> menuClassList;
    private final OnCheckListener onCheckListener;
    static int getPosition;
    private int row_index;

    Context mContext;
    private ArrayList<DroupDownModel> searchArray;

    public CheckBoxMenuAdapter(ArrayList<DroupDownModel> menuClassList, OnCheckListener onLiveTestClickListener, Context context) {
        this.menuClassList = menuClassList;
        this.onCheckListener = onLiveTestClickListener;
        this.searchArray = new ArrayList<>();
        this.searchArray.addAll(menuClassList);
        this.mContext = context;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkboxmenu, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DroupDownModel data = menuClassList.get(position);
        holder.nameText.setText(data.getDescription());
        //holder.number.setText(ticket.getTicketNumber());

        holder.openpanel.setOnClickListener(v -> {

            if(data.getStatus().equals("0")){
                data.setStatus("1");
            }else {
                data.setStatus("0");
            }
            notifyDataSetChanged();
            onCheckListener.onOptionCheckClick(menuClassList,menuClassList.get(position));
        });

        if(data.getStatus().equals("0")){
            holder.nameText.setTextColor(Color.BLACK);
            holder.openpanel.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.white));
        }else {
            holder.nameText.setTextColor(Color.WHITE);
            holder.openpanel.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorPrimary));

        }
    }

    void add(DroupDownModel ticket) {
        this.menuClassList.add(ticket);
    }
    public void addAll(List<DroupDownModel> tickets) {
        this.menuClassList.addAll(tickets);
    }
    @Override
    public int getItemCount() {
        return menuClassList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number, nameText;
        MaterialCardView openpanel;
        MyViewHolder(View view) {
            super(view);
            openpanel = view.findViewById(R.id.openpanel);
            nameText = view.findViewById(R.id.name);
        }
    }
    public interface OnCheckListener {
        void onOptionCheckClick(ArrayList<DroupDownModel> arrayList,DroupDownModel data);
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
                final List<DroupDownModel> list = new ArrayList<>();

                DroupDownModel filterableString;

                for (int i = 0; i < count; i++) {
                    filterableString = searchArray.get(i);
                    if (searchArray.get(i).getName().toLowerCase().contains(filterString)
                            || searchArray.get(i).getDescription().toLowerCase().contains(filterString)) {
                        list.add(filterableString);
                    }


                }

                results.values = list;
                results.count = list.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                menuClassList = (ArrayList<DroupDownModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}


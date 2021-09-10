package com.dbcorp.vendorapp.adapter.chating;

/**
 * Created by Bhupesh Sen on 26-01-2021.
 */
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.model.ChatBooking;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.UserMessage;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ChatCustomerAdapter extends RecyclerView.Adapter<ChatCustomerAdapter.MyViewHolder> {

    private final OnMeneuClickListnser onMenuListClicklistener;
    static int  getPosition;
    //Tag for tracking self message
    private int SELF = 786;
    private int row_index;
    Context mContext;
    int select;
    private int userId;

    ArrayList<ChatBooking> userList;
    LoginDetails loginDetails;
    public ChatCustomerAdapter(ArrayList<ChatBooking> list, OnMeneuClickListnser onLiveTestClickListener, Context context) {
        this.userList = list;
        this.onMenuListClicklistener = onLiveTestClickListener;
        loginDetails=new SqliteDatabase(context).getLogin();
        this.mContext=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        //if view type is self
        if (viewType == SELF) {
            //Inflating the layout self
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_text_left, parent, false);
        } else {
            //else inflating the layout others
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_text_right, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatBooking message=userList.get(position);
        holder.textViewMessage.setText(message.getMessage());
        holder.textViewTime.setText(message.getChatTime());



    }

    @Override
    public int getItemViewType(int position) {
        ChatBooking message = userList.get(position);

        //If its owner  id is  equals to the logged in user id
        if (Integer.parseInt(message.getChatBy()) == Integer.parseInt(loginDetails.getUser_id())) {
            //Returning self
            return SELF;
        }
        //else returning position
        return position;

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MaterialTextView textViewMessage;
        public MaterialTextView textViewTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewMessage =  itemView.findViewById(R.id.textViewMessage);
            textViewTime =   itemView.findViewById(R.id.textViewTime);
        }

    }
    public interface OnMeneuClickListnser{
        void onOptionClick(String liveTest, int pos);
    }


 }



package com.dbcorp.vendorapp.adapter;

/**
 * Created by Bhupesh Sen on 10-02-2021.
 */
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;

import com.dbcorp.vendorapp.model.UserMessage;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


//Class extending RecyclerviewAdapter
public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ViewHolder> {

    //user id
    private int userId;
    private Context context;

    //Tag for tracking self message
    private int SELF = 786;

    //ArrayList of messages object containing all the messages in the thread
    private ArrayList<UserMessage> messages;

    //Constructor
    public ThreadAdapter(Context context, ArrayList<UserMessage> messages, int userId){
        this.userId = userId;
        this.messages = messages;
        this.context = context;
    }

    //IN this method we are tracking the self message
    @Override
    public int getItemViewType(int position) {
        //getting message object of current position
        UserMessage message = messages.get(position);

        //If its owner  id is  equals to the logged in user id
        if (Integer.parseInt(message.getUserId()) == userId) {
            //Returning self
            return SELF;
        }
        //else returning position
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Creating view
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
        //returing the view
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Adding messages to the views
        UserMessage message = messages.get(position);
        holder.textViewMessage.setText(message.getMessage());
        holder.textViewTime.setText(message.getName()+", "+message.getInsertTime());
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }

    //Initializing views
    public class ViewHolder extends RecyclerView.ViewHolder {
        public MaterialTextView textViewMessage;
        public MaterialTextView textViewTime;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewMessage =  itemView.findViewById(R.id.textViewMessage);
            textViewTime =   itemView.findViewById(R.id.textViewTime);
        }
    }
}
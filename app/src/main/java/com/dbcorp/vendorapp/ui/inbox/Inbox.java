package com.dbcorp.vendorapp.ui.inbox;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.chating.ChatCustomerAdapter;


public class Inbox extends Fragment implements ChatCustomerAdapter.OnMeneuClickListnser {

    Context mContext;
    ChatCustomerAdapter chatCustomerAdapter;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    String arrItems[] = new String[]{};
    RecyclerView list;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         view=inflater.inflate(R.layout.fragment_inbox,container,false);
        init();
        return view;
    }


    public void init() {

        list = view.findViewById(R.id.list);

        arrItems = (getActivity().getResources().getStringArray(R.array.arr_nav_service_items));

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

//        chatCustomerAdapter = new ChatCustomerAdapter(arrItems, this, mContext);
//        list.setAdapter(chatCustomerAdapter);


    }
    @Override
    public void onOptionClick(String liveTest, int pos) {

    }
}

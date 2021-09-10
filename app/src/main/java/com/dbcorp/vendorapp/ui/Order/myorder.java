package com.dbcorp.vendorapp.ui.Order;

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
import com.dbcorp.vendorapp.adapter.OrderViewListAdapter;
import com.dbcorp.vendorapp.adapter.RecentOrderListAdapter;
import com.dbcorp.vendorapp.model.OrderDetails;


public class myorder extends Fragment implements OrderViewListAdapter.OnMeneuClickListnser ,RecentOrderListAdapter.OnMeneuClickListnser {

    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }


    OrderViewListAdapter orderViewListAdapter;
    RecentOrderListAdapter recentOrderListAdapter;

    View view;
    String arrItems[] = new String[]{};
    RecyclerView overView, recentOrder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         view=inflater.inflate(R.layout.fragment_order,container,false);

         init();
        return view;
    }

    public void init() {

        overView = view.findViewById(R.id.overView);
        recentOrder = view.findViewById(R.id.recentOrder);

        arrItems = (getActivity().getResources().getStringArray(R.array.arr_nav_service_items));
          orderViewListAdapter = new OrderViewListAdapter(arrItems, this, mContext);



        overView.setHasFixedSize(true);
        overView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recentOrder.setHasFixedSize(true);
        recentOrder.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));



        overView.setAdapter(orderViewListAdapter);
        recentOrder.setAdapter(recentOrderListAdapter);




    }


    @Override
    public void onOptionClick(String liveTest, int pos) {

    }


    @Override
    public void onOptionClick(OrderDetails data, int pos) {

    }
}

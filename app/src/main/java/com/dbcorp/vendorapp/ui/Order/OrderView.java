package com.dbcorp.vendorapp.ui.Order;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.MenuListAdapter;

import java.util.Objects;


public class OrderView extends Fragment implements MenuListAdapter.OnMeneuClickListnser{
    Context mContext;
    String arrItems[] = new String[]{"Order Log","Product Details", "Instructions", "Customer Details", "Delivery Address"};
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }
    OrderView listner;
    MenuListAdapter menuListAdapter;
    RecyclerView menuList;
    View view;
    FrameLayout frameLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_order_details,container,false);
         init();
         return view;
    }

    //-------init function----------
    private void init() {
        arrItems = (Objects.requireNonNull(getActivity()).getResources().getStringArray(R.array.arr_nav_service_items));
        menuList.setHasFixedSize(true);
        menuList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        menuListAdapter = new MenuListAdapter(arrItems, listner, mContext);
        menuList.setAdapter(menuListAdapter);
        frameLayout = view.findViewById(R.id.frameLayout);
    }
    public void loadFragment(Fragment fragment, String fragName) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onOptionClick(String liveTest, int pos) {

    }
}

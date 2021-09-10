package com.dbcorp.vendorapp.ui.Order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.MenuListAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.model.LoginDetails;

public class MyOrderActivity extends AppCompatActivity implements MenuListAdapter.OnMeneuClickListnser {
    String arrItems[] = new String[]{"Alll", "Pending", "Complete", "Rejected"};
    LoginDetails loginDetails;
    RecyclerView menuList;
    FrameLayout frameLayout;
    MenuListAdapter menuListAdapter;
    Context mContext;
    MyOrderActivity listner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_actity);
        loginDetails = new SqliteDatabase(this).getLogin();
        mContext = this;
        listner = this;
        init();
    }

    //-------init function----------
    private void init() {
        arrItems = (MyOrderActivity.this.getResources().getStringArray(R.array.arr_nav_service_items));
        menuList.setHasFixedSize(true);
        menuList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        menuListAdapter = new MenuListAdapter(arrItems, listner, mContext);
        menuList.setAdapter(menuListAdapter);
        frameLayout = findViewById(R.id.frameLayout);
    }

    //-------load fragment--------------
    public void loadFragment(Fragment fragment, String fragName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onOptionClick(String liveTest, int pos) {
        switch (pos) {
            case 0:
                getOrderByStatus dataObj = getOrderByStatus.getInstance("0");
                loadFragment(dataObj, "");
                break;
            case 1:
                getOrderByStatus objTwo = getOrderByStatus.getInstance("1");
                loadFragment(objTwo, "");
                break;
            case 2:
                getOrderByStatus objThree = getOrderByStatus.getInstance("2");
                loadFragment(objThree, "");
                break;
            case 3:
                getOrderByStatus objFour = getOrderByStatus.getInstance("3");
                loadFragment(objFour, "");
                break;
        }

    }
}

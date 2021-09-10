package com.dbcorp.vendorapp.ui.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.MenuListAdapter;
import com.dbcorp.vendorapp.adapter.ProductAdapter;
import com.dbcorp.vendorapp.adapter.SideMenuListAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.Product;
import com.dbcorp.vendorapp.model.Services;
import com.dbcorp.vendorapp.network.ApiService;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Login;
import com.dbcorp.vendorapp.ui.Order.Order;
import com.dbcorp.vendorapp.ui.coupons.coupons;
import com.dbcorp.vendorapp.ui.inbox.Inbox;
import com.dbcorp.vendorapp.ui.offer.offer;
import com.dbcorp.vendorapp.ui.payment.payment;
import com.dbcorp.vendorapp.ui.product.VendorProduct;
import com.dbcorp.vendorapp.ui.product.Warehouseproduct;
import com.dbcorp.vendorapp.ui.services.BookingService;
import com.dbcorp.vendorapp.ui.services.PlanService;
import com.dbcorp.vendorapp.ui.services.chatingservice.ChatService;
import com.dbcorp.vendorapp.ui.services.vendorservices;
import com.dbcorp.vendorapp.ui.servicevendor.VendorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements MenuListAdapter.OnMeneuClickListnser, SideMenuListAdapter.OnMeneuClickListnser {
    FrameLayout frameLayout;
    public Toolbar toolbar;
    DrawerLayout drawer;
    Context mContext;
    ActionBarDrawerToggle toggle;
    String arrItems[] = new String[]{};
    RecyclerView menuList, sideMenuList;
    String arrSideItems[] = new String[]{"Shop Setting"};
    MenuListAdapter menuListAdapter;
    SideMenuListAdapter sideMenuListAdapter;
    LinearLayoutCompat llRoot;
    LoginDetails login;
    MaterialTextView tvName, tvMobile, tvTn, tvNameWlc;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        login = new SqliteDatabase(this).getLogin();
        toolbar=findViewById(R.id.toolbar);
     //   toolbar.setTitle("Your Setting");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        //String spliteStr[]=login.getName().split(" ");
        Log.e("sk", login.getSk());
        getVendorSetting();
        tvTn.setText(login.getName());
        tvName.setText(login.getName());
        tvMobile.setText(login.getNumber());
        tvNameWlc.setText("Welcome \n" + login.getName());
        toolbar.setTitle("Dashboard");
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }
    private  void getVendorSetting(){
        if (InternetConnection.checkConnection(mContext)) {
          //  Util.showDialog("Please wait..",mContext);
            Map<String, String> params = new HashMap<>();
            params.put("vendorId", login.getUser_id());

            Log.e("vendor_id",params.toString());
            RestClient.post().getVendorSetting(ApiService.APP_DEVICE_ID,login.getSk(),params).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, Response<String> response) {
                    Gson gson = new Gson();
                    JSONObject object = null;
                    try {

                        object = new JSONObject(response.body());
                        Log.e("message",object.getString("message"));

                        if (object.getBoolean("status")) {

            //                Util.hideDialog();
                            JSONObject vendorSetting=object.getJSONObject("listData");
                            toolbar.setTitle(vendorSetting.getString("shop_name"));



                        } else {
                            Util.hideDialog();
                            Util.show(mContext, "something is wrong");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Util.hideDialog();

                    }


                }

                @Override
                public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {

                    try {
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Util.hideDialog();
                }
            });

        }
    }

    public void init() {

        llRoot = findViewById(R.id.llRoot);
        menuList = findViewById(R.id.menuList);
        tvName = findViewById(R.id.tvName);
        sideMenuList = findViewById(R.id.sideMenuList);
        tvTn = findViewById(R.id.tvTn);
        tvMobile = findViewById(R.id.tvMobile);
        tvNameWlc = findViewById(R.id.tvNameWlc);


        if (login.getMasterCatId().equalsIgnoreCase("1")) {

            arrItems = (HomeActivity.this.getResources().getStringArray(R.array.arr_nav_service_items));
            //Intent mv2 = new Intent(mContext, PlanService.class);
            loadFragment(new vendorservices(), "Service");
        } else {
            arrItems = (HomeActivity.this.getResources().getStringArray(R.array.arr_nav_e_com_items));
            loadFragment(new Home(), "Home");
        }
        menuList.setHasFixedSize(true);
        menuList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        menuListAdapter = new MenuListAdapter(arrItems, this, mContext);
        menuList.setAdapter(menuListAdapter);


        sideMenuList.setHasFixedSize(true);
        sideMenuList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        sideMenuListAdapter = new SideMenuListAdapter(arrSideItems, this, mContext);
        sideMenuList.setAdapter(sideMenuListAdapter);
        frameLayout = findViewById(R.id.frameLayout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        drawer.setScrimColor(Color.TRANSPARENT);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

//                float slideX = drawerView.getWidth() * slideOffset;
//                content.setTranslationX(slideX);

                float scaleFactor = 80f;

                float slideX = drawerView.getWidth() * slideOffset;
                llRoot.setTranslationX(slideX);
                llRoot.setScaleX(1 - (slideOffset / scaleFactor));
                llRoot.setScaleY(1 - (slideOffset / scaleFactor));
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Logout();
        });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();
                        UpdateNotification();
                        // Log and toast
                        Log.e("msg_token_fmt", token);
                        Toast.makeText(HomeActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private  void UpdateNotification(){
        if (InternetConnection.checkConnection(mContext)) {
            Util.showDialog("Please wait..",mContext);
            Map<String, String> params = new HashMap<>();
            params.put("user_id", login.getUser_id());
            params.put("notification_token", token);


            Log.e("vendor_id",params.toString());
            RestClient.post().updateNotification(ApiService.APP_DEVICE_ID,login.getSk(),params).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, Response<String> response) {
                    Gson gson = new Gson();
                    JSONObject object = null;
                    try {

                        object = new JSONObject(response.body());
                        Log.e("message",object.getString("message"));

                        if (object.getBoolean("status")) {

                            Util.show(mContext,"Successfully updated your setting data");


                            Util.hideDialog();
                        } else {
                            Util.hideDialog();
                            Util.show(mContext, "something is wrong");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Util.hideDialog();

                    }


                }

                @Override
                public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {

                    try {
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Util.hideDialog();
                }
            });

        }
    }
    // Logout Function ///////////
    private void Logout() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        new SqliteDatabase(mContext).removeLoginUser();
                        voidLogout();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(HomeActivity.this, Login.class));
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Do you want to logout from the app?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    public void loadFragment(Fragment fragment, String fragName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onOptionClick(String liveTest, int pos) {

        //Log.e("post",String.valueOf(pos));
        switch (pos) {

            case 0:

                if (login.getMasterCatId().equalsIgnoreCase("1")) {
                    loadFragment(new vendorservices(), "Home");
                } else {
                    loadFragment(new Home(), "Home");
                }

                break;
            case 1:
                if (login.getMasterCatId().equalsIgnoreCase("1")) {
                    loadFragment(new BookingService(), "Home");
                } else {
                    Toast.makeText(mContext, liveTest + "Can't find current address, " + pos, Toast.LENGTH_SHORT).show();

                    loadFragment(new Order(), "Home");
                }
                break;
            case 2:
                loadFragment(new VendorProduct(), "service");
                break;
            case 3:
                if (login.getMasterCatId().equalsIgnoreCase("1")) {
                    loadFragment(new ChatService(), "Home");
                } else {
                    loadFragment(new Warehouseproduct(), "services");
                }

                break;
            case 4:

                loadFragment(new payment(), "payment");
                //loadFragment(new Inbox(), "inbox");

                break;
            case 5:
                loadFragment(new offer(), "offer");
                break;
            case 6:
                loadFragment(new coupons(), "offer");
                break;


        }

    }

    @Override
    public void onSideMenuClick(String liveTest, int pos) {
        if(pos==0){
            Intent mv2 = new Intent(HomeActivity.this, VendorActivity.class);
            startActivity(mv2);
        }else{

        }
    }


    public void voidLogout(){
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("userId", login.getUser_id());


            Util.showDialog("Please wait..",mContext);


            // Calling JSON
            Call<String> call = RestClient.post().logoutUser("1234", login.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            Log.e("datares",response.body());
                            JSONObject object=new JSONObject(response.body());
                            if(object.getBoolean("status")){

                                startActivity(new Intent(mContext, Login.class));
                                finish();

                                Util.hideDialog();
                            }else{
                                Util.show(mContext,object.getString("message"));
                                Util.hideDialog();
                            }
                            Util.hideDialog();
                        } catch (Exception e) {
                            Util.hideDialog();
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            assert response.errorBody() != null;
                            Toast.makeText(mContext, "error message" + response.errorBody().string(), Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            Toast.makeText(mContext, "error message" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        Util.hideDialog();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(mContext, R.string.string_some_thing_wrong, Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Util.hideDialog();
            Toast.makeText(mContext, R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }


}

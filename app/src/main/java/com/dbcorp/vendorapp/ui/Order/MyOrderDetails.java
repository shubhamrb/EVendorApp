package com.dbcorp.vendorapp.ui.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.SubtoSubCatListAdapter;
import com.dbcorp.vendorapp.adapter.order.OrderInstruction;
import com.dbcorp.vendorapp.adapter.order.OrderLog;
import com.dbcorp.vendorapp.adapter.order.OrdersitemAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.SubToSubCategory;
import com.dbcorp.vendorapp.model.VendorShopProduct;
import com.dbcorp.vendorapp.model.orderview.Instruction;
import com.dbcorp.vendorapp.model.orderview.InstructionLog;
import com.dbcorp.vendorapp.model.orderview.OrderCustomerDetails;
import com.dbcorp.vendorapp.model.orderview.OrderProduct;
import com.dbcorp.vendorapp.model.orderview.Orders;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class MyOrderDetails extends AppCompatActivity implements OrderInstruction.OnMeneuClickListnser, OrderLog.OnMeneuClickListnser, OrdersitemAdapter.OnMeneuClickListnser {


    LoginDetails loginDetails;
    Context mContext;
    Intent g;
    OrdersitemAdapter ordersitemAdapter;
    OrderInstruction orderInstruction;
    OrderLog orderLog;
    ArrayList<OrderProduct> orderProductsList;
    ArrayList<Instruction> instructionsList;
    ArrayList<InstructionLog> instructionLogArrayList;
    ArrayList<OrderCustomerDetails> orderCustomerDetailsArrayList;
    Orders orders;
    private String orderStatus, OrderMenuStatus;
    LinearLayoutCompat rejectLayout, btnLayout;
    MaterialTextView acceptOrder, rejectMap, orderNumber, tvCustomerName, orderDate, tvPrice, deliveryAddress;
    RecyclerView litemItemCustomer, orderLogList, instructionList;
    private Toolbar toolbar;
    MyOrderDetails listner;
    boolean checkVendorSelectProduct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);

        g = getIntent();
        orders = (Orders) getIntent().getSerializableExtra("MyClass");
        Log.e("bhsgetStatus",orders.getStatus());
        OrderMenuStatus = g.getStringExtra("OrderStatus");
        mContext = this;
        listner = this;
        loginDetails = new SqliteDatabase(this).getLogin();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Order Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void init() {
        orderProductsList = new ArrayList<>();
        instructionsList = new ArrayList<>();
        instructionLogArrayList = new ArrayList<>();
        orderCustomerDetailsArrayList = new ArrayList<>();
        litemItemCustomer = findViewById(R.id.litemItemCustomer);
        litemItemCustomer.setHasFixedSize(true);
        litemItemCustomer.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));


        orderLogList = findViewById(R.id.orderLogList);
        orderLogList.setHasFixedSize(true);
        orderLogList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));


        instructionList = findViewById(R.id.instructionList);
        instructionList.setHasFixedSize(true);
        instructionList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        orderNumber = findViewById(R.id.orderNumber);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        orderDate = findViewById(R.id.orderDate);
        tvPrice = findViewById(R.id.tvPrice);
        acceptOrder = findViewById(R.id.acceptOrder);
        rejectMap = findViewById(R.id.rejectMap);
        btnLayout = findViewById(R.id.btnLayout);
        rejectLayout = findViewById(R.id.rejectLayout);
        deliveryAddress = findViewById(R.id.deliveryAddress);
        orderDate.setText(orders.getOrderDate());
        tvCustomerName.setText(orders.getCustomerName());
        orderNumber.setText(orders.getOrderNumber());

        if (OrderMenuStatus.equalsIgnoreCase("All")) {
            btnLayout.setVisibility(View.GONE);
        } else {
            if (orders.getStatus().equals("0")) {

                rejectMap.setVisibility(View.GONE);
                rejectLayout.setVisibility(View.GONE);
                //ContextCompat.getColor(mContext, R.color.myCustomColor)
                acceptOrder.setBackgroundColor(0xFFd55c1f);
                acceptOrder.setText("Order Processed");
                orderStatus = "1";
                btnLayout.setVisibility(View.VISIBLE);
                acceptOrder.setOnClickListener(v -> {
                    orderStatus = "1";
                    for (int p = 0; p < orderProductsList.size(); p++) {
                        if (orderProductsList.get(p).getVendor_selected_product().equalsIgnoreCase("0")) {
                            checkVendorSelectProduct = true;
                        } else {
                            checkVendorSelectProduct = false;
                            break;
                        }
                    }

                    if (checkVendorSelectProduct) {
                        Util.show(mContext, "Please Select Minimum One Product");
                    } else {
String msg=acceptOrder.getText().toString();
                        orderAcceptedByVendor(msg);
                    }
                });
            } else if (orders.getStatus().equals("1")) {
                //ContextCompat.getColor(mContext, R.color.myCustomColor)
                acceptOrder.setBackgroundColor(0xFF019E5F);
                rejectMap.setBackgroundColor(0xFFFF2851);
                acceptOrder.setText("Accept");
                rejectMap.setText("Reject");
                rejectMap.setOnClickListener(v -> {
                    orderStatus = "3";
                    String msg=rejectMap.getText().toString();
                    orderAcceptedByVendor(msg);
                });
                acceptOrder.setOnClickListener(v -> {
                    orderStatus = "2";
                    String msg=acceptOrder.getText().toString();
                    orderAcceptedByVendor(msg);
                });


                btnLayout.setVisibility(View.VISIBLE);
            } else if (orders.getStatus().equals("2")) {
                btnLayout.setVisibility(View.VISIBLE);
                rejectMap.setVisibility(View.GONE);
                rejectLayout.setVisibility(View.GONE);

                acceptOrder.setBackgroundColor(0xFF000000);
                acceptOrder.setText("Prepared");
                //ContextCompat.getColor(mContext, R.color.myCustomColor)
                acceptOrder.setOnClickListener(v -> {
                    orderStatus = "4";
                    String msg=acceptOrder.getText().toString();
                    orderAcceptedByVendor(msg);
                });
                acceptOrder.setBackgroundColor(Color.RED);

            } else {
                btnLayout.setVisibility(View.GONE);
            }

        }


        viewOrder();
    }

    private void viewOrder() {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("vendorId", loginDetails.getUser_id());//loginDetails.getUser_id()
            params.put("orders_id", orders.getOrdersId());//g.getStringExtra("11")
            params.put("token", orders.getToken());//g.getStringExtra("")

            // Calling JSON
            Call<String> call = RestClient.post().getOrderDetails("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            Gson gson = new Gson();

                            JSONObject object = new JSONObject(response.body());
                            if (object.getBoolean("status")) {

                                Type OrderProductType = new TypeToken<ArrayList<OrderProduct>>() {
                                }.getType();
                                orderProductsList = gson.fromJson(object.getJSONArray("orderList").toString(), OrderProductType);
                                if (orderProductsList != null) {
                                    ordersitemAdapter = new OrdersitemAdapter(OrderMenuStatus, orderProductsList, listner, mContext);
                                    litemItemCustomer.setAdapter(ordersitemAdapter);
                                }
                                Type instructionsListType = new TypeToken<ArrayList<Instruction>>() {

                                }.getType();

                                instructionsList = gson.fromJson(object.getJSONArray("instruction").toString(), instructionsListType);
                                if (instructionsList != null) {
                                    orderInstruction = new OrderInstruction(instructionsList, listner, mContext);
                                    instructionList.setAdapter(orderInstruction);
                                }
                                Type instructionLogArrayListType = new TypeToken<ArrayList<InstructionLog>>() {
                                }.getType();
                                instructionLogArrayList = gson.fromJson(object.getJSONArray("instructionOrderLog").toString(), instructionLogArrayListType);
                                if (instructionLogArrayList != null) {
                                    orderLog = new OrderLog(instructionLogArrayList, listner, mContext);
                                    orderLogList.setAdapter(orderLog);
                                }

                                Type orderCustomerDetailsArrayListType = new TypeToken<ArrayList<OrderCustomerDetails>>() {
                                }.getType();
                                orderCustomerDetailsArrayList = gson.fromJson(object.getJSONArray("customer").toString(), orderCustomerDetailsArrayListType);

                                if (orderCustomerDetailsArrayList != null) {
                                    deliveryAddress.setText(orderCustomerDetailsArrayList.get(0).getAddress());
                                }
                            } else {
                                Util.show(mContext, object.getString("message"));
                            }

                            Log.e("datasubcateee", response.body());
                        } catch (Exception e) {

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

                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(mContext, R.string.string_some_thing_wrong, Toast.LENGTH_SHORT).show();
                }

            });
        } else {

            Toast.makeText(mContext, R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onOptionClick(OrderProduct data) {
        changeStatus(data);
    }

    private void orderAcceptedByVendor(String msg) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        orderAccept();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);

        builder.setMessage("Want to "+msg+" This Order?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    private void changeStatus(OrderProduct data) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        productSwitch(data);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        if (data.getVendor_selected_product().equalsIgnoreCase("1")) {
            builder.setMessage("Are you sure. you have this product in your store?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        } else {
            builder.setMessage("Are you sure. you have don't have this product in your store?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }


    }

    private void productSwitch(OrderProduct data) {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("orders_detail_id", data.getOrdersDetailId());

            // Calling JSON
            Call<String> call = RestClient.post().checkBoxProduct("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            Log.e("datares", response.body());
                            Gson gson = new Gson();

                            JSONObject object = new JSONObject(response.body());
                            if (object.getBoolean("status")) {

                                Util.show(mContext, object.getString("message"));
                            } else {
                                Util.show(mContext, object.getString("message"));
                            }

                        } catch (Exception e) {


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

                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(mContext, R.string.string_some_thing_wrong, Toast.LENGTH_SHORT).show();
                }

            });
        } else {

            Toast.makeText(mContext, R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    private void orderAccept() {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("orders_id", orders.getOrdersId());
            params.put("vendor_id", loginDetails.getUser_id());
            params.put("status", orderStatus);
            // Calling JSON
            Call<String> call = RestClient.post().orderAccept("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            Log.e("datares", response.body());
                            Gson gson = new Gson();

                            JSONObject object = new JSONObject(response.body());
                            if (object.getBoolean("status")) {

                                Util.show(mContext, object.getString("productStatus"));
                                orders.setStatus(object.getString("productStatus"));
                                Intent mv=new Intent(MyOrderDetails.this,MyOrderDetails.class);
                                mv.putExtra("MyClass", orders);
                                mv.putExtra("OrderStatus", object.getString("productStatus"));
                                startActivity(mv);
                                finish();
                            } else {
                                Util.show(mContext, object.getString("message"));
                            }

                        } catch (Exception e) {


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

                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(mContext, R.string.string_some_thing_wrong, Toast.LENGTH_SHORT).show();
                }

            });
        } else {

            Toast.makeText(mContext, R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOptionClick(InstructionLog data, int pos) {

    }

    @Override
    public void onOptionClick(Instruction data, int pos) {

    }
}
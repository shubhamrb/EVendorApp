package com.dbcorp.vendorapp.ui.Order;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.DroupdownMenuAdapter;
import com.dbcorp.vendorapp.adapter.MenuListAdapter;
import com.dbcorp.vendorapp.adapter.order.OrdersAdapter;
import com.dbcorp.vendorapp.adapter.order.OrdersDetailsAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.DroupDownModel;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.orderview.CustomerOrderDetails;
import com.dbcorp.vendorapp.model.orderview.Orders;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Order extends Fragment implements OrdersDetailsAdapter.OnMeneuClickListnser, OrdersAdapter.OnMeneuClickListnser, MenuListAdapter.OnMeneuClickListnser, DroupdownMenuAdapter.OnMeneuClickListnser {

    Context mContext;
    PopupWindow popupWindow;
    private int select = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    View view;
    Order listnerContext;
    MenuListAdapter menuListAdapter;
    RecyclerView orderList, menuList;
    OrdersDetailsAdapter ordersAdapter;
    LoginDetails loginDetails;
    ProgressBar progressBar;
    private TimePickerDialog mTimePicker;
    DatePickerDialog datePickerDialog;
    int day1, day2;
    String mDay3, mDay4, mDay5, mDay6;
    int mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2;
    MaterialTextView tvError;
    ArrayList<Orders> selectedProductData;
    MaterialTextView tvTotal, tvPT;
    MaterialButton tvCancel, tvAccept, tvSelectTime, tvSelectDate;
    private String currentStatus = "";
    Orders selectedData;
    private ArrayList<DroupDownModel> deliveryBoyList;
    String arrItems[] = new String[]{"All", "New Order", "Accepted", "Preparing", "Canceled", "Assigned", "On The Way", "Delivered","On hold"};
    ArrayList<CustomerOrderDetails> orders;
    LinearLayoutCompat acceptLayout, dateTimeLayout;

    String strTime = "", strDate = "";
    ArrayList<String> selectedIdes;
    ArrayList<String> tokensList;
    AppCompatImageView imgClose;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {

        view = inflater.inflate(R.layout.fragment_order, container, false);
        loginDetails = new SqliteDatabase(getActivity()).getLogin();
        listnerContext = this;

        Bundle bundle1=getArguments();
        if (bundle1 != null) {
            select = bundle1.getInt("pos");
        }
        init(select);
        return view;
    }

    public void init(int select) {
        orders = new ArrayList<>();
        tokensList = new ArrayList<>();
        selectedIdes = new ArrayList<>();
        selectedProductData = new ArrayList<>();
        deliveryBoyList = new ArrayList<>();
        tvPT = view.findViewById(R.id.tvPT);
        imgClose = view.findViewById(R.id.imgClose);
        tvCancel = view.findViewById(R.id.tvCancel);
        tvAccept = view.findViewById(R.id.tvAccept);
        acceptLayout = view.findViewById(R.id.acceptLayout);
        orderList = view.findViewById(R.id.orderList);
        menuList = view.findViewById(R.id.menuList);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvError = view.findViewById(R.id.tvError);
        tvSelectTime = view.findViewById(R.id.tvSelectTime);
        tvSelectDate = view.findViewById(R.id.tvSelectDate);
        dateTimeLayout = view.findViewById(R.id.dateTimeLayout);
        progressBar = view.findViewById(R.id.progressBar);
        menuList.setHasFixedSize(true);
        menuList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        menuListAdapter = new MenuListAdapter(arrItems, listnerContext, mContext, select);
        menuList.setAdapter(menuListAdapter);
        menuList.scrollToPosition(select);


        orderList.setHasFixedSize(true);
        orderList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        tvSelectTime.setOnClickListener(view1 -> {

            showTime();
        });
        tvSelectDate.setOnClickListener(view1 -> {
            showDate();
        });

        onOptionClick("", select);

        getDeliveryBoy();
        imgClose.setOnClickListener(v -> {
            acceptLayout.setVisibility(View.GONE);
        });
        tvCancel.setOnClickListener(v -> {
            if (selectedIdes.size() < 1) {
                Util.show(mContext, "please Select Minimum one product ");
                return;
            }
            orderAcceptedByVendor("Rejected", "3");
        });
        tvAccept.setOnClickListener(v -> {
            if (selectedIdes.size() < 1) {
                Util.show(mContext, "please Select Minimum one product ");
                return;
            }

            if (currentStatus.equalsIgnoreCase("2")) {
                orderAcceptedByVendor("Accepted", "4");
            } else if (currentStatus.equalsIgnoreCase("3")) {
                messageAlert();
            } else if (currentStatus.equalsIgnoreCase("1")) {
                if (strDate.length() == 0 || strTime.length() == 0) {
                    Util.show(mContext, "Please select deliver date and time");
                    return;
                }
                orderAcceptedByVendor("Accepted", "2");
            }

        });

    }

    @SuppressLint("SetTextI18n")
    public void showDate() {
        final Calendar c = Calendar.getInstance();
        mYear1 = c.get(Calendar.YEAR);
        mMonth1 = c.get(Calendar.MONTH);
        mDay1 = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(mContext,
                (view, year, monthOfYear, dayOfMonth) -> {
                    day1 = dayOfMonth;
                    mDay3 = dayOfMonth + "/"
                            + (monthOfYear + 1) + "/" + year;
                    mDay5 = year + "-"
                            + (monthOfYear + 1) + "-" + dayOfMonth;
                    if (monthOfYear <= 9) {
                        int md = monthOfYear + 1;
                        tvSelectDate.setText(year + "-"
                                + "0" + md + "-" + dayOfMonth);
                    } else {
                        tvSelectDate.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                    strDate = tvSelectDate.getText().toString();

                }, mYear1, mMonth1, mDay1);
        datePickerDialog.show();
    }

    private void showTime() {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                tvSelectTime.setText(selectedHour + ":" + selectedMinute + ":" + "00");
                strTime = tvSelectTime.getText().toString();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void getOrder(String status) {
        acceptLayout.setVisibility(View.GONE);
        orders = new ArrayList<>();
        orderList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText("Please Wait...");
        tvTotal.setText("");
        if (InternetConnection.checkConnection(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("vendorId", loginDetails.getUser_id());
            params.put("status", status);
            // Calling JSON
            Log.e("params", params.toString());
            Call<String> call = RestClient.post().getOrder("1234", loginDetails.getSk(), params);

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
                                Type type = new TypeToken<ArrayList<CustomerOrderDetails>>() {
                                }.getType();
                                orders = gson.fromJson(object.getJSONArray("orderList").toString(), type);

                                if (orders.size() > 0) {
                                    orderList.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    tvError.setVisibility(View.GONE);
                                    tvTotal.setText("" + orders.size());
                                    ordersAdapter = new OrdersDetailsAdapter(orders, listnerContext, mContext, status);
                                    orderList.setAdapter(ordersAdapter);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    tvError.setVisibility(View.VISIBLE);
                                    tvError.setText(object.getString("message"));
                                    orders.clear();
                                }
                                ordersAdapter.notifyDataSetChanged();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                tvError.setVisibility(View.VISIBLE);
                                tvError.setText(object.getString("message"));
                            }

                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            tvError.setVisibility(View.GONE);
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
                        progressBar.setVisibility(View.GONE);
                        tvError.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(mContext, R.string.string_some_thing_wrong, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    tvError.setVisibility(View.GONE);
                }

            });
        } else {

            Toast.makeText(mContext, R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onOptionClick(ArrayList<Orders> list, int pos) {

    }

    @Override
    public void orderAssign(Orders data, int pos) {
        selectedData = data;
        messageAlert();

    }

    @Override
    public void onResume() {
        super.onResume();
        init(select);
    }

    private void getDeliveryBoy() {
        deliveryBoyList = new ArrayList<>();
        if (InternetConnection.checkConnection(mContext)) {
            Map<String, String> params = new HashMap<>();

            // Calling JSON
            Call<String> call = RestClient.post().getDriverList("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.getBoolean("status")) {
                                JSONArray list = object.getJSONArray("list");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject str = list.getJSONObject(i);
                                    DroupDownModel downModel = new DroupDownModel();
                                    downModel.setId(str.getString("user_id"));
                                    downModel.setDescription(str.getString("name") + "\n(" + str.getString("number") + ") ");
                                    downModel.setName(str.getString("number"));
                                    downModel.setStatus("0");
                                    deliveryBoyList.add(downModel);
                                }

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
    public void onOptionClick(String liveTest, int pos) {
        menuListAdapter.select=pos;
        menuListAdapter.notifyDataSetChanged();

        orders.clear();
        switch (pos) {
            case 0:
                getOrder("All");
                currentStatus = "0";
                break;
            case 1:
                tvCancel.setVisibility(View.VISIBLE);
                tvAccept.setText("Accepted");
                getOrder("1");
                currentStatus = "1";
                break;
            case 2:
                tvCancel.setVisibility(View.GONE);
                tvAccept.setText("Preparing");
                getOrder("2");
                currentStatus = "2";
                break;
            case 3:
                tvCancel.setVisibility(View.GONE);
                tvAccept.setText("Order Assign");
                getOrder("4");
                currentStatus = "3";
                break;
            case 4:
                getOrder("3");
                break;
            case 5:
                getOrder("6");
                break;
            case 6:
                getOrder("5");
                break;
            case 7:
                //e
                getOrder("6");
                break;
            case 8:
                getOrder("7");
                break;
        }
    }

    @Override
    public void onOptionClick(DroupDownModel data) {
        Util.hideDropDown();
        assignDriver(data.getId());
    }

    private void messageAlert() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        acceptLayout.setVisibility(View.GONE);
                        Util.showDropDown(deliveryBoyList, "", mContext, listnerContext);
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
        builder.setMessage("Are you sure you want to assign this order to deliver boy ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void assignDriver(String driverId) {
        if (InternetConnection.checkConnection(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("vendor_id", loginDetails.getUser_id());
            params.put("status", "6");
            params.put("assigned_user_id", driverId);
            params.put("remark", "Assigned");
            params.put("orders_id", selectedIdes.toString().replace("[", "").replace("]", "").replace(" ", ""));
            Log.e("data", params.toString());

            // Calling JSON
            Call<String> call = RestClient.post().orderAssign("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.getBoolean("status")) {
                                getOrder("4");
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
    public void viewDetails(CustomerOrderDetails data, int pos) {

    }

    @Override
    public void selectProduct(ArrayList<CustomerOrderDetails> listData) {
        selectedIdes = new ArrayList<>();
        selectedProductData = new ArrayList<>();
        for (int i = 0; i < listData.size(); i++) {
            for (int p = 0; p < listData.get(i).getProductDetails().size(); p++) {
                Log.e("ordecheck" + p, listData.get(i).getProductDetails().get(p).getCustomerId() + "=" + listData.get(i).getProductDetails().get(p).getProductName());
                if (listData.get(i).getProductDetails().get(p).getCheckbox().equalsIgnoreCase("1")) {

                    selectedProductData.add(listData.get(i).getProductDetails().get(p));
                    selectedIdes.add(listData.get(i).getProductDetails().get(p).getOrdersDetailId());

                    if (!tokensList.contains(listData.get(i).getProductDetails().get(p).getCustomerId()))
                        tokensList.add(listData.get(i).getProductDetails().get(p).getCustomerId());
                }
            }
        }
        if (currentStatus.equalsIgnoreCase("1")) {
            strTime = "";
            strDate = "";
            dateTimeLayout.setVisibility(View.VISIBLE);
        } else {
            dateTimeLayout.setVisibility(View.GONE);
        }
        acceptLayout.setVisibility(View.VISIBLE);
        tvPT.setText(String.valueOf(selectedProductData.size()));

        JSONArray mJsonArray = new JSONArray(selectedProductData);

        Log.e("orderIdes", selectedIdes.toString().replace("[", "").replace("]", ""));
        Util.show(mContext, selectedIdes.toString().replace("[", "").replace("]", ""));
    }


    private void orderAcceptedByVendor(String msg, String status) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        orderAccept(msg, status);
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

        builder.setMessage("Want to " + msg + " This Order?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    private void orderAccept(String msg, String status) {
        if (InternetConnection.checkConnection(mContext)) {
            Util.showDialog("Please wait..", mContext);
            Map<String, String> params = new HashMap<>();
            params.put("orders_id", selectedIdes.toString().replace("[", "").replace("]", "").replace(" ", ""));
            params.put("vendor_id", loginDetails.getUser_id());
            params.put("status", status);
            params.put("tokens", tokensList.toString().replace("[", "").replace("]", "").replace(" ", ""));
            params.put("remark", msg);
            if (status.equalsIgnoreCase("2")) {
                params.put("delivery_date", tvSelectDate.getText().toString() + " " + tvSelectTime.getText().toString().replace("pm", "").replace("am", ""));
            } else {
                params.put("delivery_date", "no");
            }
            Log.e("params", tokensList.toString());
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
                            JSONObject object = new JSONObject(response.body());
                            if (object.getBoolean("status")) {
                                Util.hideDialog();
                                Util.show(mContext, "Updated ");
                                getOrder(currentStatus);
                            } else {
                                Util.hideDialog();
                                Util.show(mContext, object.getString("message"));
                            }

                        } catch (Exception e) {
                            Util.hideDialog();
                            e.printStackTrace();
                        }

                    } else {
                        Util.hideDialog();
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
                    Util.hideDialog();
                    Toast.makeText(mContext, R.string.string_some_thing_wrong, Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Util.hideDialog();
            Toast.makeText(mContext, R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }
}

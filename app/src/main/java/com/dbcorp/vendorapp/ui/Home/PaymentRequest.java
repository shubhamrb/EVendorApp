package com.dbcorp.vendorapp.ui.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.PaymentRequestAdapter;
import com.dbcorp.vendorapp.adapter.WalletAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.PaymentRequestModel;
import com.dbcorp.vendorapp.model.WalletsData;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRequest extends AppCompatActivity implements WalletAdapter.OnMeneuClickListnser, PaymentRequestAdapter.OnMeneuClickListnser {


    private static AlertDialog alertDialog;
    Context mContext;
    MaterialTextView totalCollection, tvPayRequest, tvCredit, tvDebit, requestList, tvRequest;
    ArrayList<WalletsData> list;
    Intent g;
    ArrayList<PaymentRequestModel> payList;
    RecyclerView listItem;
    LoginDetails loginDetails;
    PaymentRequest listnerContext;
    WalletAdapter walletAdapter;
    PaymentRequestAdapter paymentRequestAdapter;
    Dialog dialog;
    String amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_request);
        this.mContext = this;
        this.listnerContext = this;
        g = getIntent();
        amount = g.getStringExtra("amount");
        loginDetails = new SqliteDatabase(this).getLogin();
        init();
    }

    public void init() {
        payList = new ArrayList<>();
        list = new ArrayList<>();
        tvPayRequest = findViewById(R.id.tvPayRequest);
        tvCredit = findViewById(R.id.tvCredit);
        tvDebit = findViewById(R.id.tvDebit);
        totalCollection = findViewById(R.id.totalCollection);

        requestList = findViewById(R.id.requestList);
        listItem = findViewById(R.id.listItem);
        totalCollection.setText(amount);
        tvCredit.setOnClickListener(v -> {

            getWallet("1");

            if (walletAdapter != null) {
                walletAdapter.getFilter().filter("Credit");
                walletAdapter.notifyDataSetChanged();
            }
            tvCredit.setTextColor(Color.WHITE);
            tvPayRequest.setVisibility(View.GONE);
            tvCredit.setBackgroundResource(R.drawable.red_square_gradient);
            tvDebit.setTextColor(Color.BLACK);
            tvDebit.setBackground(null);
            requestList.setTextColor(Color.BLACK);
            requestList.setBackground(null);
            listItem.setVisibility(View.VISIBLE);
        });

        tvDebit.setOnClickListener(v -> {
            getWallet("2");
            tvPayRequest.setVisibility(View.GONE);
            if (walletAdapter != null) {
                walletAdapter.getFilter().filter("Debit");
                walletAdapter.notifyDataSetChanged();
            }

            tvDebit.setTextColor(Color.WHITE);
            tvDebit.setBackgroundResource(R.drawable.red_square_gradient);
            requestList.setTextColor(Color.BLACK);
            requestList.setBackground(null);
            tvCredit.setTextColor(Color.BLACK);
            tvCredit.setBackground(null);
            listItem.setVisibility(View.VISIBLE);
        });

        requestList.setOnClickListener(v -> {
            listItem.setVisibility(View.GONE);
            tvPayRequest.setVisibility(View.VISIBLE);
            requestList.setTextColor(Color.WHITE);
            requestList.setBackgroundResource(R.drawable.red_square_gradient);

            tvCredit.setTextColor(Color.BLACK);
            tvCredit.setBackground(null);

            tvDebit.setTextColor(Color.BLACK);
            tvDebit.setBackground(null);
            getPayment();
        });
        tvPayRequest.setOnClickListener(v -> {
            sendRequest();
        });
        getWallet("1");
    }


    public void sendRequest() {
        dialog = new Dialog(mContext);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
        builder2.setCancelable(false);
        View view2 = LayoutInflater.from(mContext).inflate(R.layout.payment_request, null);

        builder2.setView(view2);
        TextInputEditText et_price = view2.findViewById(R.id.price);
        TextInputEditText et_remark = view2.findViewById(R.id.remark);
        AppCompatImageView close = view2.findViewById(R.id.close);
        MaterialButton subtm = view2.findViewById(R.id.submit_btn);

        close.setOnClickListener(v -> {
            alertDialog.cancel();
            alertDialog.dismiss();
        });
        subtm.setOnClickListener(v -> {
            String price = et_price.getText().toString();
            String remark = et_remark.getText().toString();

            if (price.isEmpty()) {
                Toast.makeText(mContext, "Please enter price.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (remark.isEmpty()) {
                Toast.makeText(mContext, "Please enter remark.", Toast.LENGTH_SHORT).show();
                return;
            }
            sendRequestData(remark, price);
        });
        builder2.setView(view2);
        alertDialog = builder2.create();
        alertDialog.show();
    }

    private void sendRequestData(String remark, String price) {
        if (InternetConnection.checkConnection(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("vendor_id", loginDetails.getUser_id());
            params.put("remark", remark);
            params.put("amount", price);
            params.put("status", "1");


            // Calling JSON
            Log.e("alertDialog", params.toString());
            Call<String> call = RestClient.post().addPaymentRequest("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {
                            Log.e("datares", response.body());
                            Gson gson = new Gson();
                            Util.show(mContext, "We save your data");
                            alertDialog.dismiss();
                            JSONObject object = new JSONObject(response.body());
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

    private void getWallet(String Status) {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("userId", loginDetails.getUser_id());
            params.put("status", Status);
            // Calling JSON
            Call<String> call = RestClient.post().getWallet("1234", loginDetails.getSk(), params);

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
                                Type type = new TypeToken<ArrayList<WalletsData>>() {
                                }.getType();
                                list = gson.fromJson(object.getJSONArray("walletlist").toString(), type);
                            }

                            walletAdapter = new WalletAdapter(list, listnerContext, mContext);
                            listItem.setAdapter(walletAdapter);
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

    private void getPayment() {
        if (InternetConnection.checkConnection(mContext)) {

            payList.clear();
            Map<String, String> params = new HashMap<>();
            params.put("vendorId", loginDetails.getUser_id());

            // Calling JSON
            Call<String> call = RestClient.post().getPaymentRequest("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            Log.e("datares", response.body());
                            Gson gson = new Gson();
                            listItem.setVisibility(View.VISIBLE);
                            JSONObject object = new JSONObject(response.body());
                            Type type = new TypeToken<ArrayList<PaymentRequestModel>>() {
                            }.getType();
                            payList = gson.fromJson(object.getJSONArray("list").toString(), type);

                            paymentRequestAdapter = new PaymentRequestAdapter(payList, listnerContext, mContext);
                            listItem.setAdapter(paymentRequestAdapter);
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
    public void onOptionClick(WalletsData data, int pos) {

    }

    @Override
    public void onOptionClick(PaymentRequestModel data, int pos) {

    }
}
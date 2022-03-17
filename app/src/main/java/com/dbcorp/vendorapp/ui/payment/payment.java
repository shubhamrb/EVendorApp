package com.dbcorp.vendorapp.ui.payment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.PaymentRequestAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.PaymentRequestModel;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.PaymentRequest;
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


public class payment extends Fragment implements PaymentRequestAdapter.OnMeneuClickListnser {

    Context mContext;
    ArrayList<PaymentRequestModel> payList;
    private LoginDetails loginDetails;
    private RecyclerView listItem;
    private PaymentRequestAdapter paymentRequestAdapter;
    payment listnerContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.listnerContext = this;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        loginDetails = new SqliteDatabase(mContext).getLogin();
        payList = new ArrayList<>();
        listItem = view.findViewById(R.id.listItem);
        getPayment();

        return view;

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
    public void onOptionClick(PaymentRequestModel data, int pos) {

    }
}

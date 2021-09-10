package com.dbcorp.vendorapp.ui.Order;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.RecentOrderListAdapter;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.OfferModel;
import com.dbcorp.vendorapp.model.OrderDetails;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.offer.viewOffer;
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


public class getOrderByStatus extends Fragment implements  RecentOrderListAdapter.OnMeneuClickListnser {
    Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }
    public static getOrderByStatus getInstance(String data) {
        getOrderByStatus myExamFragment = new getOrderByStatus();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);

        myExamFragment.setArguments(bundle);
        return myExamFragment;
    }
    RecentOrderListAdapter recentOrderListAdapter;
    View view;
    String status;
    LoginDetails loginDetails;
    ArrayList<OrderDetails> orderDetails;
    getOrderByStatus listener;
    RecyclerView  recentOrder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_payment,container,false);

        init();
        return view;
    }


    private void init(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            status  =  bundle.getString("data");
        }
        recentOrder=view.findViewById(R.id.recentOrder);
        recentOrder = view.findViewById(R.id.recentOrder);
        recentOrder.setHasFixedSize(true);
        recentOrder.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getOrderData();
        recentOrderListAdapter = new RecentOrderListAdapter(status,orderDetails, listener, mContext);
        recentOrder.setAdapter(recentOrderListAdapter);

    }



    private void getOrderData(){

        if (InternetConnection.checkConnection(mContext)) {

            Map<String, String> params = new HashMap<>();
            params.put("vendorId",loginDetails.getUser_id());
            params.put("status", status);
            // Calling JSON
            Call<String> call = RestClient.post().getHome("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {




                            Gson gson = new Gson();

                            JSONObject object=new JSONObject(response.body());

                            Log.e("response",object.toString());


                            Type type = new TypeToken<ArrayList<OrderDetails>>(){}.getType();
                            orderDetails=gson.fromJson(object.getJSONArray("orderList").toString(),type);

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
    public void onOptionClick(OrderDetails data, int pos) {

    }
}

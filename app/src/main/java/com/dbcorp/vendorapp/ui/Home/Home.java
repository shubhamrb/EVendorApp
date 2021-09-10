package com.dbcorp.vendorapp.ui.Home;

import android.content.Context;
import android.content.Intent;
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
import com.dbcorp.vendorapp.adapter.OrderViewListAdapter;
import com.dbcorp.vendorapp.adapter.RecentOrderListAdapter;
import com.dbcorp.vendorapp.adapter.order.OrdersDetailsAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.OrderDetails;
import com.dbcorp.vendorapp.model.orderview.CustomerOrderDetails;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
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


public class Home extends Fragment implements OrderViewListAdapter.OnMeneuClickListnser ,RecentOrderListAdapter.OnMeneuClickListnser ,OrdersDetailsAdapter.OnMeneuClickListnser{

    Context mContext;

    Home listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    String arrItems[] = new String[]{};
    RecyclerView overView, recentOrder, transactionList;
    View view;
    OrdersDetailsAdapter ordersAdapter;
    RecentOrderListAdapter recentOrderListAdapter;



    LoginDetails loginDetails;

    ArrayList<CustomerOrderDetails> orderDetailsList;

    MaterialTextView payRequest, Cancel,OnHold,NotDelivered,Delivered,OntheWay,Arrived,Assign,total,New,Accepted,Rejected,Preparing,totalCollection,acceptCollection,rejectedCollection, tvcn,tvct,tvth,tvf,tvfive,tvsix;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        init();
        return view;
    }

    public void init() {


        orderDetailsList=new ArrayList<>();
        payRequest=view.findViewById(R.id.payRequest);
        Delivered=view.findViewById(R.id.Delivered);
        OntheWay=view.findViewById(R.id.OntheWay);
        Arrived=view.findViewById(R.id.Arrived);
        Assign=view.findViewById(R.id.assigne);
        total=view.findViewById(R.id.total);
        New=view.findViewById(R.id.New);
        Accepted=view.findViewById(R.id.Accepted);
        Rejected=view.findViewById(R.id.Rejected);
        Preparing=view.findViewById(R.id.Preparing);
        Cancel=view.findViewById(R.id.Cancel);
        OnHold=view.findViewById(R.id.OnHold);
        NotDelivered=view.findViewById(R.id.NotDelivered);

        totalCollection=view.findViewById(R.id.totalCollection);
         acceptCollection=view.findViewById(R.id.acceptCollection);
         rejectedCollection =view.findViewById(R.id.rejectedCollection);


        loginDetails=new SqliteDatabase(getActivity()).getLogin();
        recentOrder = view.findViewById(R.id.recentOrder);
        recentOrder.setHasFixedSize(true);
        recentOrder.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

       // arrItems = (getActivity().getResources().getStringArray(R.array.arr_nav_items));







        payRequest.setOnClickListener(v->{
            Intent mv=new Intent(getActivity(),PaymentRequest.class);
            mv.putExtra("amount",totalCollection.getText().toString());
            startActivity(mv);


        });
        getData();
    }


    public void getData(){

        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("vendorId", loginDetails.getUser_id());

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

                            if(object.getBoolean("status")){
                                Log.e("response",object.toString());
                                totalCollection.setText("₹ "+object.getString("totalCollection"));
                                rejectedCollection.setText("₹ "+object.getString("rejectedCollection"));
                                acceptCollection.setText("₹ "+object.getString("acceptCollection"));
                                Delivered.setText(object.getJSONObject("totalOrder").getString("Delivered"));
                                OntheWay.setText(object.getJSONObject("totalOrder").getString("OntheWay"));
                                Arrived.setText(object.getJSONObject("totalOrder").getString("Arrived"));
                                Assign.setText(object.getJSONObject("totalOrder").getString("Assign"));
                                total.setText(object.getJSONObject("totalOrder").getString("total"));
                                New.setText(object.getJSONObject("totalOrder").getString("New"));
                                Accepted.setText(object.getJSONObject("totalOrder").getString("Accepted"));
                                Rejected.setText(object.getJSONObject("totalOrder").getString("Rejected"));
                                Preparing.setText(object.getJSONObject("totalOrder").getString("Preparing"));


                                NotDelivered.setText(object.getJSONObject("totalOrder").getString("NotDelivered"));
                                OnHold.setText(object.getJSONObject("totalOrder").getString("OnHold"));
                                Cancel.setText(object.getJSONObject("totalOrder").getString("Cancel"));

                                Type type = new TypeToken<ArrayList<CustomerOrderDetails>>(){}.getType();
                                orderDetailsList=gson.fromJson(object.getJSONArray("orderList").toString(),type);
                                ordersAdapter = new OrdersDetailsAdapter(orderDetailsList, listener, mContext,"All");
                                recentOrder.setAdapter(ordersAdapter);
                                ordersAdapter.notifyDataSetChanged();
                            }else{

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
    public void onOptionClick(OrderDetails data, int pos) {

    }

    @Override
    public void onOptionClick(String liveTest, int pos) {

    }

    @Override
    public void viewDetails(CustomerOrderDetails data, int pos) {

    }

    @Override
    public void selectProduct(ArrayList<CustomerOrderDetails> listData) {

    }
}

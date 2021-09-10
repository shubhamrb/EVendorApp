package com.dbcorp.vendorapp.ui.services.chatingservice;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.chating.ChatCustomerAdapter;
import com.dbcorp.vendorapp.adapter.chating.ChatingRequestAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.BookingServiceChat;
import com.dbcorp.vendorapp.model.ChatBooking;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.network.ApiService;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.dbcorp.vendorapp.ui.inbox.chating;
import com.dbcorp.vendorapp.ui.product.ProductList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatService extends Fragment implements  ChatingRequestAdapter.OnClickListener {

    Context mContext;

    LoginDetails loginDetails;
    ChatService listener;
    BookingServiceChat bookingChat;
    ChatingRequestAdapter chatingRequestAdapter;
    ArrayList<BookingServiceChat> listData;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    private View view;
    private RecyclerView list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_service_chat_booking, container, false);
        listener = this;
        mContext = getActivity();
        loginDetails = new SqliteDatabase(getActivity()).getLogin();
        init();
        return view;
    }


    public void init() {
        list = view.findViewById(R.id.list);
        list.setHasFixedSize(true);
         // list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        getChatRequest();
    }


    private  void getChatRequest(){
        if (InternetConnection.checkConnection(mContext)) {

            Map<String, String> params = new HashMap<>();
            params.put("vendor_id", loginDetails.getUser_id());

Log.e("vendor_id",params.toString());
            RestClient.post().getBooking(ApiService.APP_DEVICE_ID,loginDetails.getSk(),params).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, Response<String> response) {
                    Gson gson = new Gson();
                    JSONObject object = null;
                    try {

                        object = new JSONObject(response.body());
                        Log.e("message",object.getString("message"));

                        if (object.getBoolean("status")) {

                            Type type = new TypeToken<ArrayList<BookingServiceChat>>() {
                            }.getType();
                            listData = gson.fromJson(object.getJSONArray("serviceChat").toString(), type);

                            chatingRequestAdapter = new ChatingRequestAdapter(listData, listener, mContext);
                            list.setAdapter(chatingRequestAdapter);

                        } else {
                            Util.show(mContext, "something is wrong");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {

                    try {
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }



    @Override
    public void onClickCard(BookingServiceChat list) {
        chating productFragment=chating.getInstance(list);
        ((HomeActivity) Objects.requireNonNull(mContext)).loadFragment(productFragment, "");

    }
}

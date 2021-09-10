package com.dbcorp.vendorapp.ui.inbox;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.chating.ChatCustomerAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.BookingServiceChat;
import com.dbcorp.vendorapp.model.ChatBooking;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.SubCategory;
import com.dbcorp.vendorapp.model.UserMessage;
import com.dbcorp.vendorapp.network.ApiService;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.product.ProductList;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class chating extends Fragment implements ChatCustomerAdapter.OnMeneuClickListnser {

    Context mContext;
    private static final long DELAY = 5000;

    ChatCustomerAdapter chatCustomerAdapter;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }
    private Handler handler;
    public static chating getInstance(BookingServiceChat data) {
        chating myExamFragment = new chating();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);

        myExamFragment.setArguments(bundle);
        return myExamFragment;
    }
    BookingServiceChat data;
    String arrItems[] = new String[]{};
    RecyclerView list;
    View view;
    LoginDetails loginDetails;
    String lastChatId="";
    String firstEnter="yes";
    String chat_to_id="";
    ArrayList<ChatBooking> chatBookings;
    ArrayList<ChatBooking> chatBookingsNewList;
    TextInputEditText tvMsg;

    AppCompatImageView ImgSend;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.chatactivity,container,false);
        loginDetails=new SqliteDatabase(getContext()).getLogin();
        handler = new Handler();
        chatBookings=new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            data = (BookingServiceChat) bundle.getSerializable("data");
        }
        init();
        return view;
    }




    chating listener;
    private static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
    private void sendMessage() {

        if (InternetConnection.checkConnection(mContext)) {

            ChatBooking m = new ChatBooking(loginDetails.getUser_id(), Objects.requireNonNull(tvMsg.getText()).toString(), getTimeStamp(), data.getService_chat_booking_id());
            chatBookings.add(m);
            chatCustomerAdapter.notifyDataSetChanged();
            scrollToBottom();
            Map<String, String> params = new HashMap<>();
            params.put("chat_by", loginDetails.getUser_id());
            params.put("chat_to", data.getUser_id());
            params.put("service_chat_booking_id", data.getService_chat_booking_id());
            params.put("message", tvMsg.getText().toString());
            // Calling JSON
            Log.e("bhs",params.toString());
            Call<String> call = RestClient.post().addMessage("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {

                            Gson gson = new Gson();

                            JSONObject object = new JSONObject(response.body());
//                            Type type = new TypeToken<ArrayList<SubCategory>>() {
//                            }.getType();
//                            listData = gson.fromJson(object.getJSONArray("subCateogory").toString(), type);
//
//                            serviceAdapter = new SubCategoryAdapter(listData, listenerContext, mContext);
//                            list.setAdapter(serviceAdapter);
//

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


    private void scrollToBottom() {
        chatCustomerAdapter.notifyDataSetChanged();
        if (chatCustomerAdapter.getItemCount() > 1){
           // list.smoothScrollToPosition(chatCustomerAdapter.getItemCount());
           list.getLayoutManager().smoothScrollToPosition(list, null, chatCustomerAdapter.getItemCount() - 1);

        }
    }
    public void init() {
        tvMsg=view.findViewById(R.id.tvMsg);
        list = view.findViewById(R.id.list);
        ImgSend=view.findViewById(R.id.ImgSend);

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        ImgSend.setOnClickListener(v->{
            sendMessage();
            tvMsg.setText("");
        });

        chatCustomerAdapter = new ChatCustomerAdapter(chatBookings, listener, mContext);
        list.setAdapter(chatCustomerAdapter);
        scrollToBottom();
        getChatRequest();
        startGettingOnlineDataFromCar();


    }
    void startGettingOnlineDataFromCar() {
        handler.post(mStatusChecker);
    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                getLastMessage();
            } catch (Exception e) {
                Log.e("TAG", e.getMessage());
            }
            handler.postDelayed(mStatusChecker, DELAY);
        }
    };
    private  void getChatRequest(){
        chatBookings=new ArrayList<>();
        if (InternetConnection.checkConnection(mContext)) {

            Map<String, String> params = new HashMap<>();
            params.put("service_chat_booking_id",  data.getService_chat_booking_id());


            RestClient.post().getMyChat(ApiService.APP_DEVICE_ID,loginDetails.getSk(),params).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, Response<String> response) {
                    Gson gson = new Gson();
                    JSONObject object = null;
                    try {

                        object = new JSONObject(response.body());
                        Log.e("message",object.getString("message"));

                        if (object.getBoolean("status")) {

                            Type type = new TypeToken<ArrayList<ChatBooking>>() {
                            }.getType();
                            chatBookings = gson.fromJson(object.getJSONArray("listData").toString(), type);
                            Collections.sort(chatBookings, ChatBooking.byChatId);
                            chatCustomerAdapter = new ChatCustomerAdapter(chatBookings, listener, mContext);
                            list.setAdapter(chatCustomerAdapter);
                            scrollToBottom();
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
    private  void getLastMessage(){
        chatBookingsNewList=new ArrayList<>();
        if (InternetConnection.checkConnection(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("service_chat_booking_id",data.getService_chat_booking_id());
            params.put("chat_to_id",data.getUser_id());

            Log.e("bhslast",params.toString());
            RestClient.post().getLastMessage(ApiService.APP_DEVICE_ID,loginDetails.getSk(),params).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, Response<String> response) {
                    Gson gson = new Gson();
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.body());
                        Log.e("message",object.getString("message"));
                        if (object.getBoolean("status")) {
                            Type type = new TypeToken<ArrayList<ChatBooking>>() {
                            }.getType();
                            chatBookingsNewList = gson.fromJson(object.getJSONArray("listData").toString(), type);
                            Collections.sort(chatBookingsNewList, ChatBooking.byChatId);

                            if(firstEnter.equalsIgnoreCase("yes")){
                                firstEnter="no";

                                lastChatId=chatBookingsNewList.get(chatBookingsNewList.size()-1).getChatId();
                                //Util.show(mContext,lastChatId);
                                chatBookings.addAll(chatBookingsNewList);
                                chatCustomerAdapter.notifyDataSetChanged();
                            }else{
                               // Util.show(mContext,"bhs"+chatBookingsNewList.get(chatBookingsNewList.size()-1).getChatId());
                                if(lastChatId.equalsIgnoreCase(chatBookingsNewList.get(chatBookingsNewList.size()-1).getChatId())){

                                }else{
                                    lastChatId=chatBookingsNewList.get(chatBookingsNewList.size()-1).getChatId();
                                    chatBookings.addAll(chatBookingsNewList);
                                    chatCustomerAdapter.notifyDataSetChanged();
                                }
                            }



                            //if(lastChatId.equalsIgnoreCase(chatBookingsNewList.get(chatBookingsNewList.size()-1).getChatId())){
                              //  Util.show(mContext, "bhs"+lastChatId);
                            //}else{
                                //chat_to_id=chatBookingsNewList.get(chatBookingsNewList.size()-1).getChatId();

                            //}
                            //chatCustomerAdapter = new ChatCustomerAdapter(chatBookings, listener, mContext);
                            // list.setAdapter(chatCustomerAdapter);
                            scrollToBottom();
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
    public void onOptionClick(String liveTest, int pos) {

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

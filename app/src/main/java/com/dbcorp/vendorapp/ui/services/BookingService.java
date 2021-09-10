package com.dbcorp.vendorapp.ui.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.BookingLayoutAdapter;
import com.dbcorp.vendorapp.adapter.DroupdownMenuAdapter;
import com.dbcorp.vendorapp.adapter.VendorServiceAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.Category;
import com.dbcorp.vendorapp.model.CustomerServiceBooking;
import com.dbcorp.vendorapp.model.DroupDownModel;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.VendorService;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookingService extends Fragment implements DroupdownMenuAdapter.OnMeneuClickListnser, BookingLayoutAdapter.OnMeneuClickListnser {

    Context mContext;
    BookingService listenerContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    private LoginDetails loginDetails;
    private View view;

    private RecyclerView list;
    private BookingLayoutAdapter bookingLayoutAdapter;
    private ArrayList<CustomerServiceBooking> bookingArrayList;


    String category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.booking_service_layout, container, false);
        listenerContext = this;
        mContext = getActivity();
        loginDetails = new SqliteDatabase(getActivity()).getLogin();
        init();
        return view;
    }


    public void init() {
        bookingArrayList = new ArrayList<>();

        list = view.findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        //list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        getVendorService();


    }


    private void getVendorService() {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("vendor_id", loginDetails.getUser_id());

            // Calling JSON
            Call<String> call = RestClient.post().getBookingService("1234", loginDetails.getSk(), params);

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


                                Type type = new TypeToken<ArrayList<CustomerServiceBooking>>() {
                                }.getType();
                                //arrayList assigne booking data from api
                                bookingArrayList = gson.fromJson(object.getJSONArray("listData").toString(), type);
                               //pass the data in adapter with constructor
                                bookingLayoutAdapter = new BookingLayoutAdapter(bookingArrayList, listenerContext, mContext);
                                //set list data in recycler view
                                list.setAdapter(bookingLayoutAdapter);

                            } else {
                                Util.show(mContext, object.getString("message"));

                            }


                        } catch (Exception e) {
                            Util.show(mContext, e.getMessage());
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
    public void onOptionClick(CustomerServiceBooking data, int pos) {

        changeStatus(data.getServicesBookedId());
        //      ProductList productFragment=ProductList.getInstance(data);
//        ((HomeActivity) Objects.requireNonNull(mContext)).loadFragment(productFragment, "");

    }

    private void changeStatus(String sid) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to change the service status?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
    @Override
    public void onOptionClick(DroupDownModel liveTest) {
        Util.hideDropDown();
        category = liveTest.getId();

    }
}

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
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.DroupdownMenuAdapter;
import com.dbcorp.vendorapp.adapter.VendorServiceAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.Category;
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


public class vendorservices extends Fragment implements DroupdownMenuAdapter.OnMeneuClickListnser, VendorServiceAdapter.OnMeneuClickListnser {

    Context mContext;
    vendorservices listenerContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    MaterialTextView tvCategory;

    private ArrayList<Category> listData;
    private View view;
    private RecyclerView list;
    VendorServiceAdapter vendorServiceAdapter;

    private LoginDetails loginDetails;

    ArrayList<DroupDownModel> categoryList;
    MaterialCardView addService;
    ArrayList<VendorService> vendorServicesList;
    String category;
      PopupWindow popupWindow;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_service, container, false);
        listenerContext = this;
        mContext = getActivity();
        loginDetails = new SqliteDatabase(getActivity()).getLogin();
        init();
        return view;
    }


    public void init() {
        categoryList = new ArrayList<>();


        addService = view.findViewById(R.id.addService);
        // list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list = view.findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        getServiceCat();
        getVendorService();
        addService.setOnClickListener(v -> {

            Log.e("categoryList",categoryList.toString());
            otpWindow();
        });

    }

    @SuppressLint("SetTextI18n")
    public void otpWindow() {

        View popupView = null;


        popupView = LayoutInflater.from(mContext).inflate(R.layout.add_service, null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
           popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(popupView, Gravity.TOP, 0, 0);
        TextInputEditText tvName = popupView.findViewById(R.id.tvName);
        AppCompatImageView closeId=popupView.findViewById(R.id.closeId);
        closeId.setOnClickListener(v->{
            popupWindow.dismiss();
        });
        tvCategory = popupView.findViewById(R.id.tvCategory);

        tvCategory.setOnClickListener(v -> {
            Util.showDropDown(categoryList, "Select Category", mContext, listenerContext);
        });
        MaterialButton submitBtn = popupView.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(v -> {

            if (tvName.getText().length() == 0) {
                Util.show(mContext, "Please Enter Service Name");
                return;
            }
            if (tvCategory.getText().length() == 0) {
                Util.show(mContext, "Please Enter Service Name");
                return;
            }
            addVendorService(tvName.getText().toString());
        });

    }

    private void getServiceCat() {
        if (InternetConnection.checkConnection(mContext)) {

            categoryList = new ArrayList<>();
            Map<String, String> params = new HashMap<>();


            // Calling JSON
            Call<String> call = RestClient.post().getServiceCat("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;


                        JSONObject object = null;
                        try {

                            object = new JSONObject(response.body());
                            Log.e("object", object.toString());
                           // Log.e("message", object.getString("message"));

                            if (object.getBoolean("status")) {

                                JSONArray array = object.getJSONArray("listData");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    DroupDownModel model = new DroupDownModel();
                                    model.setName(jsonObject.getString("master_category_id"));
                                    model.setDescription(jsonObject.getString("name"));
                                    model.setId(jsonObject.getString("category_id"));
                                    categoryList.add(model);
                                }
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

    private void getVendorService() {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("vendorId", loginDetails.getUser_id());
            Log.e("datasubcateee", params.toString());
            // Calling JSON
            Call<String> call = RestClient.post().getVendorService("1234", loginDetails.getSk(), params);

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


                                Type type = new TypeToken<ArrayList<VendorService>>() {
                                }.getType();
                                vendorServicesList = gson.fromJson(object.getJSONArray("list").toString(), type);
                                vendorServiceAdapter = new VendorServiceAdapter(vendorServicesList, listenerContext, mContext);
                                list.setAdapter(vendorServiceAdapter);

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


    private void addVendorService(String name) {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("vendor_id", loginDetails.getUser_id());
            params.put("name", name);
            params.put("category_id", category);

            // Calling JSON
            Call<String> call = RestClient.post().addVendorService("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;


                        try {



                            JSONObject object = new JSONObject(response.body());

                            if (object.getBoolean("status")) {
                                popupWindow.dismiss();
                                Util.show(mContext, object.getString("message"));
getVendorService();
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


    private void serviceStatus(String sid) {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("services_id",sid);

            // Calling JSON
            Call<String> call = RestClient.post().changeStatus("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;


                        try {



                            JSONObject object = new JSONObject(response.body());

                            if (object.getBoolean("status")) {
                                getVendorService();
                                Util.show(mContext, object.getString("message"));

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
    public void onOptionClick(VendorService data, int pos) {

        changeStatus(data.getServicesId());
        //      ProductList productFragment=ProductList.getInstance(data);
//        ((HomeActivity) Objects.requireNonNull(mContext)).loadFragment(productFragment, "");

    }

    private void changeStatus(String sid) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {

                        serviceStatus(sid);
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
        tvCategory.setText(liveTest.getDescription());
    }
}

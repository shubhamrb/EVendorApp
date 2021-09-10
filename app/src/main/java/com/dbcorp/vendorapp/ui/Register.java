package com.dbcorp.vendorapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.MenuPopupWindow;


import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.CategoryAdapter;
import com.dbcorp.vendorapp.adapter.CheckBoxMenuAdapter;
import com.dbcorp.vendorapp.adapter.DroupdownMenuAdapter;
import com.dbcorp.vendorapp.adapter.ServiceAdapter;
import com.dbcorp.vendorapp.adapter.VendorProductAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.Category;
import com.dbcorp.vendorapp.model.CategoryResponse;
import com.dbcorp.vendorapp.model.DroupDownModel;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.OTP;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register extends AppCompatActivity implements DroupdownMenuAdapter.OnMeneuClickListnser,CheckBoxMenuAdapter.OnCheckListener{

    MaterialButton submitBtn;
    Context mContext;
    LinearLayout optlayout;
    Register listenerContext;
    MaterialTextView tvCity,service,ecommerce,tvCategory;
    private VendorProductAdapter vendorProductAdapter;

    TextInputEditText editMobile, edit_email, edit_name, edit_pass, enterOtp, edit_confirm_pass;
    private ArrayList<DroupDownModel> cityListArray;
    private ArrayList<DroupDownModel> selectCategory;
    private ArrayList<String> stringArrayList;
    private ArrayList<String> stringCatArrayList;
    private ArrayList<Category> productCatList;
    private String catId="2",city_id="";
    ServiceAdapter serviceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register_one);
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        listenerContext=this;
        mContext = this;
        init();
        clickListener();

    }


    // initialization
    public void init() {
        //city array initialization
        cityListArray=new ArrayList<>();

        submitBtn = findViewById(R.id.submit_btn);
        tvCategory=findViewById(R.id.tvCategory);
        edit_name = findViewById(R.id.edit_name);
        editMobile = findViewById(R.id.mobile);
        edit_email = findViewById(R.id.edit_email);
        tvCity=findViewById(R.id.tvCity);
        edit_pass = findViewById(R.id.edit_pass);
        service=findViewById(R.id.service);
        ecommerce=findViewById(R.id.ecommerce);
        edit_confirm_pass = findViewById(R.id.edit_confirm_pass);
        enterOtp = findViewById(R.id.enterOtp);
        optlayout = findViewById(R.id.optlayout);
        getCity();
        getCategory();

    }

    // event listener
    public void clickListener() {
        submitBtn.setOnClickListener(v -> {




//

            if (editMobile.getText().length() == 0 || editMobile.getText().length() < 10) {
                Util.show(mContext, "Please enter valid mobile no");
                return;
            }
            if (edit_name.getText().length() == 0) {
                Util.show(mContext, "Please enter your name");
                return;
            }
            if (edit_email.getText().length() == 0) {
                Util.show(mContext, "Please enter email");
                return;
            }
            if (edit_pass.getText().length() == 0) {
                Util.show(mContext, "Please enter valid password");
                return;
            }
            if (tvCategory.getText().length() == 0) {
                Util.show(mContext, "Please enter category");
                return;
            }
            if (tvCity.getText().length() == 0) {
                Util.show(mContext, "Please enter city");
                return;
            }
            if (edit_confirm_pass.getText().toString().equalsIgnoreCase(edit_pass.getText().toString())) {


            } else {
                Util.show(mContext, "Please enter same password");
                return;
            }
            verifyOtp();
        });

        tvCity.setOnClickListener(v->{
            Util.showDropDown(cityListArray,"Please Select City",mContext,listenerContext);
        });
        tvCategory.setOnClickListener(v->{
            Util.showCheckBox(selectCategory,"Please Select Category",mContext,listenerContext);
        });

        ecommerce.setOnClickListener(v->{
            catId="2";
            tvCategory.setText("");
            service.setTextColor(Color.parseColor("#000000"));
            ecommerce.setTextColor(Color.parseColor("#FFFFFF"));
            ecommerce.setBackgroundResource(R.drawable.red_gredient_bg);
            service.setBackgroundResource(R.drawable.white_gredient_bg);
            getCategory();
        });
        service.setOnClickListener(v->{
            catId="1";
            tvCategory.setText("");
            ecommerce.setTextColor(Color.parseColor("#000000"));
            service.setTextColor(Color.parseColor("#FFFFFF"));
            service.setBackgroundResource(R.drawable.red_gredient_bg);
            ecommerce.setBackgroundResource(R.drawable.white_gredient_bg);
            getCategory();
        });
    }

    // Api Calling Function


    //1 . otp verify
    public void verifyOtp() {
        if (InternetConnection.checkConnection(mContext)) {

            Map<String, String> params = new HashMap<>();
            params.put("number", editMobile.getText().toString());

            RestClient.post().getOtp(params).enqueue(new Callback<OTP>() {
                @Override
                public void onResponse(@NotNull Call<OTP> call, Response<OTP> response) {

                    OTP obj = response.body();
                    assert obj != null;
                    Log.e("data", obj.getMessage());
                    if(obj.getStatus()){
                        otpWindow(obj.getTitle());
                        optlayout.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onFailure(@NotNull Call<OTP> call, @NotNull Throwable t) {

                    try {
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }


    @SuppressLint("SetTextI18n")
    public void otpWindow(String msg) {

        View popupView = null;


        popupView = LayoutInflater.from(mContext).inflate(R.layout.otp_layout, null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(popupView, Gravity.TOP, 0, 0);
        enterOtp = popupView.findViewById(R.id.edit_otp);
        MaterialTextView tvMsg = popupView.findViewById(R.id.message);
        MaterialButton btnVrify = popupView.findViewById(R.id.verify_btn);
        tvMsg.setText("Kindly check your SMS send on +91 " + editMobile.getText().toString() + " and\n" +
                "your email " + Objects.requireNonNull(edit_email.getText()).toString() + "\n" + msg);


        btnVrify.setOnClickListener(v -> {

            if (enterOtp.getText().length() < 4) {
                Util.show(mContext, "Please enter valid otp");
                return;
            }

            addRegister();
        });


    }





    private void getCity() {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("mastCatId", catId);

            // Calling JSON
            Call<String> call = RestClient.post().getCity("1234", params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            JSONObject object = new JSONObject(response.body());
                            Log.e("bhs",object.toString());
                            if(object.getBoolean("status")){

                                JSONArray cityList=object.getJSONArray("city");

                                for(int i=0;i<cityList.length();i++){

                                    JSONObject str=cityList.getJSONObject(i);
                                    DroupDownModel downModel=new DroupDownModel();
                                    downModel.setId(str.getString("city_id"));
                                    downModel.setDescription(str.getString("name"));
                                    downModel.setStatus("0");

                                    downModel.setName(str.getString("name"));
                                    cityListArray.add(downModel);
                                }

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
    private void getCategory() {
        selectCategory=new ArrayList<>();
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("mastCatId", catId);

            // Calling JSON
            Call<String> call = RestClient.post().getCategory("1234", params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            JSONObject object = new JSONObject(response.body());
                            Log.e("bhs",object.toString());
                            if(object.getBoolean("status")){

                                JSONArray cityList=object.getJSONArray("cateogory");

                                for(int i=0;i<cityList.length();i++){

                                    JSONObject str=cityList.getJSONObject(i);
                                    DroupDownModel downModel=new DroupDownModel();
                                    downModel.setId(str.getString("category_id"));
                                    downModel.setDescription(str.getString("name"));
                                    downModel.setName(str.getString("name"));
                                    downModel.setStatus("0");
                                    selectCategory.add(downModel);
                                }

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




    private void addRegister(){


        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("otp", enterOtp.getText().toString());
            params.put("number", editMobile.getText().toString());
            params.put("name", edit_name.getText().toString());
            params.put("email", edit_email.getText().toString());
            params.put("password", edit_pass.getText().toString());
            params.put("p_category_id", stringArrayList.toString().replace("[","").replace("]","").replace(" ",""));
            params.put("master_category_id", catId);
            params.put("city_id", city_id);

            // Calling JSON
            Call<String> call = RestClient.post().addRegister("1234", params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            JSONObject object = new JSONObject(response.body());
                            Log.e("bhs",object.toString());
                            if(object.getBoolean("status")){

                                LoginDetails loginDetails = new LoginDetails();
                                loginDetails.setUser_id(object.getString("user_id"));
                                loginDetails.setName(object.getString("name"));
                                loginDetails.setNumber(object.getString("number"));
                                loginDetails.setMasterCatId(object.getString("masterCatId"));
                                loginDetails.setMastercatname(object.getString("mastercatname"));
                                loginDetails.setEmail(object.getString("email"));
                                loginDetails.setPhoto(object.getString("photo"));
                                loginDetails.setPickupPoint(object.getString("pickup_point"));
                                loginDetails.setSk(object.getString("sk"));
                                new SqliteDatabase(mContext).addLogin(loginDetails);
                                    startActivity(new Intent(mContext, HomeActivity.class));
                                    finish();


                            }else{
                                Util.show(mContext,object.getString("message"));

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
    public void onOptionClick(DroupDownModel data) {

        tvCity.setText(data.getDescription());
        city_id=data.getId();
        Util.hideDropDown();

    }

    @Override
    public void onOptionCheckClick(ArrayList<DroupDownModel> arrayList,DroupDownModel data) {
       // Util.hideCheckDialog();

        stringArrayList=new ArrayList<>();
        stringCatArrayList=new ArrayList<>();
        for(int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).getStatus().equals("1")){
                stringArrayList.add(arrayList.get(i).getId());
                stringCatArrayList.add(arrayList.get(i).getName());
            }
        }

        tvCategory.setText(stringCatArrayList.toString().replace("[","").replace("]",""));
    }
}

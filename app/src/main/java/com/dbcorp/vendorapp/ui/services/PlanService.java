package com.dbcorp.vendorapp.ui.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dbcorp.apkaaada.ui.auth.fragments.location.maps.MapsFragment;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.ProductAdapter;
import com.dbcorp.vendorapp.adapter.serviceprovider.ServicePlanAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.DroupDownModel;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.OfferModel;
import com.dbcorp.vendorapp.model.Product;
import com.dbcorp.vendorapp.model.ServicePackage;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.Home;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanService extends AppCompatActivity implements  ServicePlanAdapter.OnClickListener {

    RecyclerView planList;
    LoginDetails loginDetails;
    Context mContext;
    ServicePlanAdapter servicePlanAdapter;
    ArrayList<ServicePackage> servicePackages;
    PlanService listnerContext;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_service);
        mContext=this;
        listnerContext=this;

        loginDetails=new SqliteDatabase(this).getLogin();
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Service Plan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init(){
        servicePackages=new ArrayList<>();
        planList=findViewById(R.id.planList);
        planList.setHasFixedSize(true);
        planList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //planList.setLayoutManager(new GridLayoutManager(mContext, 3));

      getPlanStatus();


    }


    public void getPlanStatus(){

        if(InternetConnection.checkConnection(mContext)) {

            Log.e("service","bhbhbhb");
            Map<String, String> params = new HashMap<>();
            params.put("vendorId",loginDetails.getUser_id());
            // Calling JSON
            Call<String> call = RestClient.post().checkPlanStatus("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {

                            JSONObject object = new JSONObject(response.body());

                            if(object.getBoolean("status")){
                                Gson gson = new Gson();
                                Log.e("plan",object.toString());
                                if(object.getString("planStatus").equalsIgnoreCase("2")){
                                    Log.e("bhsssssplan",object.getString("planStatus"));
                                    startActivity(new Intent(mContext, HomeActivity.class));
                                    finish();
                                }else  if(object.getString("planStatus").equalsIgnoreCase("3")){

                                    getPlan();
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
    public void getPlan(){

        if(InternetConnection.checkConnection(mContext)) {

            Log.e("service","bhbhbhb");
            Map<String, String> params = new HashMap<>();
            // Calling JSON
            Call<String> call = RestClient.post().getServicePlan("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {

                            JSONObject object = new JSONObject(response.body());
                            Log.e("serplan",object.toString());
                            if(object.getBoolean("status")){
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<ServicePackage>>(){}.getType();
                                servicePackages=gson.fromJson(object.getJSONArray("listData").toString(),type);
                                servicePlanAdapter = new ServicePlanAdapter(servicePackages, listnerContext, mContext);
                                planList.setAdapter(servicePlanAdapter);
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
    public void onOptionClick(ServicePackage data, int pos) {
        addPlan(data);
    }


    public void addPlan(ServicePackage data){

        if(InternetConnection.checkConnection(mContext)) {

            Log.e("service","bhbhbhb");
            Map<String, String> params = new HashMap<>();
            params.put("vendor_id",loginDetails.getUser_id());
            params.put("package_id",data.getServicePackageDetails().get(0).getPackageId());


            Util.showDialog("Please wait..",mContext);
            // Calling JSON
            Call<String> call = RestClient.post().subScribePlan("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {

                            JSONObject object = new JSONObject(response.body());
                            Log.e("serplan",object.toString());
                            if(object.getBoolean("status")){
                                Gson gson = new Gson();

                                startActivity(new Intent(mContext, HomeActivity.class));
                                finish();


                                Util.hideDialog();
                            }else{
                                Util.hideDialog();


                            }




                        } catch (Exception e) {
                            Util.hideDialog();

                            e.printStackTrace();
                        }

                    } else {
                        try {
                            assert response.errorBody() != null;
                            Toast.makeText(mContext, "error message" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            Util.hideDialog();

                        } catch (IOException e) {
                            Toast.makeText(mContext, "error message" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        Util.hideDialog();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(mContext, R.string.string_some_thing_wrong, Toast.LENGTH_SHORT).show();
                    Util.hideDialog();

                }

            });
        } else {

            Toast.makeText(mContext, R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }
}
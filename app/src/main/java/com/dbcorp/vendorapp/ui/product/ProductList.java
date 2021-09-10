package com.dbcorp.vendorapp.ui.product;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.ProductAdapter;
import com.dbcorp.vendorapp.adapter.ServiceAdapter;
import com.dbcorp.vendorapp.adapter.SubtoSubCatListAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.Category;
import com.dbcorp.vendorapp.model.CategoryResponse;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.Product;
import com.dbcorp.vendorapp.model.SubCategory;
import com.dbcorp.vendorapp.model.SubToSubCategory;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.JsonArrayResponse;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductList extends Fragment implements ProductAdapter.OnMeneuClickListnser,SubtoSubCatListAdapter.OnMeneuClickListnser {

    Context mContext;

    ProductList listnerContext;

    public static ProductList getInstance(SubCategory data) {
        ProductList myExamFragment = new ProductList();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);

        myExamFragment.setArguments(bundle);
        return myExamFragment;
    }

    SubCategory Subcategory;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    ProductAdapter productAdapter;
    SubtoSubCatListAdapter subtoSubCatListAdapter;
    String arrItems[] = new String[]{};

    ArrayList<SubToSubCategory> subCatList;
    ArrayList<Product> listData;
    View view;
    RecyclerView list,listSubCat;

    LoginDetails loginDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         view=inflater.inflate(R.layout.fragment_product_list,container,false);

        loginDetails=new SqliteDatabase(getActivity()).getLogin();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Subcategory = (SubCategory) bundle.getSerializable("data");
        }
        listnerContext=this;
        init();
        return view;
    }

    public void init() {

        subCatList=new ArrayList<>();
        list = view.findViewById(R.id.listView);
        listSubCat=view.findViewById(R.id.listSubCat);


        listSubCat.setHasFixedSize(true);
        listSubCat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        list.setHasFixedSize(true);
        //  list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        getSubToSubCat();
        getProduct();



    }



    private void getSubToSubCat() {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("subCatId", "1");

            // Calling JSON
            Call<String> call = RestClient.post().getSubtoSubCategory("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            Gson gson = new Gson();

                            JSONObject object=new JSONObject(response.body());
                            Type type = new TypeToken<ArrayList<SubToSubCategory>>(){}.getType();
                            subCatList=gson.fromJson(object.getJSONArray("cateogory").toString(),type);
                            subtoSubCatListAdapter = new SubtoSubCatListAdapter(subCatList, listnerContext, mContext);
                            listSubCat.setAdapter(subtoSubCatListAdapter);
                            Log.e("datasubcateee",response.body());
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
    private void getProduct() {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("category_id", "1");

            // Calling JSON
            Call<String> call = RestClient.post().getProdct("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            Log.e("datares",response.body());
                            Gson gson = new Gson();

                            JSONObject object=new JSONObject(response.body());
                            Type type = new TypeToken<ArrayList<Product>>(){}.getType();
                            listData=gson.fromJson(object.getJSONArray("data").toString(),type);


                            productAdapter = new ProductAdapter(listData, listnerContext, mContext);
                            list.setAdapter(productAdapter);
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
    public void onOptionClick(Product data, int pos) {
        ProductAdd productFragment=ProductAdd.getInstance(data);
        ((HomeActivity) Objects.requireNonNull(mContext)).loadFragment(new ProductAdd(), "");

    }

    @Override
    public void onOptionClick(SubToSubCategory liveTest, int pos) {

    }
}

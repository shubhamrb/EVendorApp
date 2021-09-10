package com.dbcorp.vendorapp.ui.category;

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
import com.dbcorp.vendorapp.adapter.ServiceAdapter;
import com.dbcorp.vendorapp.adapter.SubCategoryAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.Category;
import com.dbcorp.vendorapp.model.CategoryResponse;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.Product;
import com.dbcorp.vendorapp.model.SubCategory;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.dbcorp.vendorapp.ui.product.ProductList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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


public class subcategory extends Fragment implements SubCategoryAdapter.OnMeneuClickListnser {

    Context mContext;
    subcategory listenerContext;

    public static subcategory getInstance(Category data) {
        subcategory myExamFragment = new subcategory();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);

        myExamFragment.setArguments(bundle);
        return myExamFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    SubCategoryAdapter serviceAdapter;

    private ArrayList<SubCategory> listData;
    private View view;
    private RecyclerView list;

    private LoginDetails loginDetails;

    Category category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_service, container, false);
        listenerContext = this;
        mContext = getActivity();
        loginDetails = new SqliteDatabase(getActivity()).getLogin();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            category = (Category) bundle.getSerializable("data");
        }
        init();

        return view;
    }


    public void init() {

        listData = new ArrayList<>();
        list = view.findViewById(R.id.list);

        list.setHasFixedSize(true);
        //   list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list.setLayoutManager(new GridLayoutManager(getActivity(), 3));


        getSubCategory();


    }


    private void getSubCategory() {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("catId", category.getCategory_id());

            // Calling JSON
            Call<String> call = RestClient.post().getSubCategory("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {

                            Gson gson = new Gson();

                            JSONObject object = new JSONObject(response.body());
                            Type type = new TypeToken<ArrayList<SubCategory>>() {
                            }.getType();
                            listData = gson.fromJson(object.getJSONArray("subCateogory").toString(), type);

                            serviceAdapter = new SubCategoryAdapter(listData, listenerContext, mContext);
                            list.setAdapter(serviceAdapter);


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
    public void onOptionClick(SubCategory data, int pos) {
         ProductList productFragment=ProductList.getInstance(data);
        ((HomeActivity) Objects.requireNonNull(mContext)).loadFragment(productFragment, "");

    }
}

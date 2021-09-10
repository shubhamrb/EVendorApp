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
import com.dbcorp.vendorapp.adapter.CategoryAdapter;
import com.dbcorp.vendorapp.adapter.ServiceAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.model.Category;
import com.dbcorp.vendorapp.model.CategoryResponse;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.dbcorp.vendorapp.ui.product.ProductList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class category extends Fragment implements CategoryAdapter.OnMeneuClickListnser {

    Context mContext;
    category listenerContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    CategoryAdapter serviceAdapter;

    private ArrayList<Category> listData;
    private View view;
    private RecyclerView list;

    private LoginDetails loginDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_service, container, false);
        listenerContext=this;
        mContext=getActivity();
        loginDetails=new SqliteDatabase(getActivity()).getLogin();
        init();
        return view;
    }


    public void init() {

        list = view.findViewById(R.id.list);
        list.setHasFixedSize(true);
     //   list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
     list.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        getCategory();


    }


    private void getCategory() {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("mastCatId", "2");

            // Calling JSON
            Call<CategoryResponse> call = RestClient.post().getCategory("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<CategoryResponse>() {
                @Override
                public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {
                            CategoryResponse obj = response.body();


                           listData = obj.getCateogory();
                            Log.e("datammm", obj.getMessage());
                           // Util.show(mContext, obj.getMessage());

                           serviceAdapter = new CategoryAdapter(listData, listenerContext, mContext);
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
                public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                    Toast.makeText(mContext, R.string.string_some_thing_wrong, Toast.LENGTH_SHORT).show();
                }

            });
        } else {

            Toast.makeText(mContext, R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onOptionClick(Category data, int pos) {
        subcategory productFragment=subcategory.getInstance(data);
        ((HomeActivity) Objects.requireNonNull(mContext)).loadFragment(productFragment, "");

    }
}

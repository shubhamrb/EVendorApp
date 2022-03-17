package com.dbcorp.vendorapp.ui.product;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.ProductAdapter;
import com.dbcorp.vendorapp.adapter.WarehouseProductAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.Product;
import com.dbcorp.vendorapp.model.WareHouseProduct;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dbcorp.vendorapp.network.ApiService.IMG_PRODUCT_URL;


public class Warehouseproduct extends Fragment implements WarehouseProductAdapter.OnMeneuClickListnser {

    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    ArrayList<WareHouseProduct> list;
    WarehouseProductAdapter warehouseProductAdapter;
    LoginDetails loginDetails;
    RecyclerView listView;
    Warehouseproduct listnerContext;
    View view;
    MaterialTextView payRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_product_list, container, false);
        loginDetails = new SqliteDatabase(mContext).getLogin();
        listView = view.findViewById(R.id.listView);
        listnerContext = this;
        init();
        return view;
    }

    private void init() {
        list = new ArrayList<>();
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        getProduct();

    }

    private void getProduct() {
        if (InternetConnection.checkConnection(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("vendorId", loginDetails.getUser_id());
            params.put("mastCatId", loginDetails.getMasterCatId());
            // Calling JSON
            Call<String> call = RestClient.post().getWareHouseProduct("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            Log.e("datares", response.body());
                            Gson gson = new Gson();

                            JSONObject object = new JSONObject(response.body());
                            if (object.getBoolean("status")) {
                                Type type = new TypeToken<ArrayList<WareHouseProduct>>() {
                                }.getType();
                                list = gson.fromJson(object.getJSONArray("productList").toString(), type);

                                warehouseProductAdapter = new WarehouseProductAdapter(list, listnerContext, mContext);
                                listView.setAdapter(warehouseProductAdapter);
                            } else {

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
    public void onOptionClick(WareHouseProduct data, int pos) {
        otpWindow(data);
    }

    @SuppressLint("SetTextI18n")
    public void otpWindow(WareHouseProduct data) {

        View popupView = null;


        popupView = LayoutInflater.from(mContext).inflate(R.layout.price_change, null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        CircleImageView productImg;
        MaterialTextView productName, catName, subCat, subtosubCat, actualPrice, tvQuantity, tvTotPrice, tvTotalPrice;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_gredient_bg));
        popupWindow.showAtLocation(popupView, Gravity.TOP, 0, 0);
        TextInputEditText tvPrice = popupView.findViewById(R.id.tvPrice);
        TextInputEditText tvMrp = popupView.findViewById(R.id.tvMrp);
        TextInputEditText tvProductQuantity = popupView.findViewById(R.id.tvProductQuantity);
        MaterialButton saveData = popupView.findViewById(R.id.saveData);


        productImg = popupView.findViewById(R.id.productImg);
        productName = popupView.findViewById(R.id.productName);
        catName = popupView.findViewById(R.id.catName);
        subCat = popupView.findViewById(R.id.subCat);
        subtosubCat = popupView.findViewById(R.id.subtosubCat);
        actualPrice = popupView.findViewById(R.id.actualPrice);
        tvQuantity = popupView.findViewById(R.id.tvQuantity);
        tvTotPrice = popupView.findViewById(R.id.tvTotPrice);
        tvTotalPrice = popupView.findViewById(R.id.tvTotalPrice);
        ImageView closeImg = popupView.findViewById(R.id.closeImg);

        productName.setText(data.getName());
        catName.setText(data.getCategoryName());
        tvTotPrice.setVisibility(View.GONE);
        actualPrice.setVisibility(View.GONE);
        tvTotalPrice.setVisibility(View.GONE);
        tvQuantity.setVisibility(View.GONE);
        subCat.setText(data.getSubCategoryName());
        subtosubCat.setText(data.getSubSubCategoryName());
        Glide.with(mContext)
                .load(IMG_PRODUCT_URL + data.getPhoto())
                .into(productImg);

        saveData.setOnClickListener(v -> {
            //popupWindow.dismiss();
            if(tvPrice.getText().toString().length()==0){
                Util.show(mContext,"Please enter the price");
                return;
            }if(tvMrp.getText().toString().length()==0){
                Util.show(mContext,"Please enter the MRP");
                return;
            }
            if(tvProductQuantity.getText().toString().length()==0){
                Util.show(mContext,"Please enter the quantity");
                return;
            }
            addProduct(data, tvPrice.getText().toString(),tvMrp.getText().toString(), tvProductQuantity.getText().toString());
        });

        closeImg.setOnClickListener(v -> {
            popupWindow.dismiss();

        });

    }


    private void addProduct(WareHouseProduct data, String price,String mrp, String quantity) {
        if (InternetConnection.checkConnection(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("product_id", data.getProductId());
            params.put("variant_id", data.getVariant_id());
            params.put("vendor_id", loginDetails.getUser_id());
            params.put("price", price);
            params.put("mrp", mrp);
            params.put("quantity", quantity);

            Log.e("data",params.toString());
            // Calling JSON
            Call<String> call = RestClient.post().addProduct("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {
                            Log.e("datares", response.body());
                            Gson gson = new Gson();

                            JSONObject object = new JSONObject(response.body());
                            if (object.getBoolean("status")) {
                                Util.show(mContext, object.getString("message"));
                            } else {
                                Util.show(mContext, object.getString("message"));
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
}

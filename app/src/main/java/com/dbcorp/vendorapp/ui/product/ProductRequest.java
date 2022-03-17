package com.dbcorp.vendorapp.ui.product;

import static com.dbcorp.vendorapp.ui.offer.addOffer.BITMAP_SAMPLE_SIZE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.PaymentRequestAdapter;
import com.dbcorp.vendorapp.adapter.ProductRequestAdapter;
import com.dbcorp.vendorapp.adapter.WalletAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.CameraUtils;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.PaymentRequestModel;
import com.dbcorp.vendorapp.model.ProductRequestModel;
import com.dbcorp.vendorapp.model.WalletsData;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRequest extends AppCompatActivity implements WalletAdapter.OnMeneuClickListnser, PaymentRequestAdapter.OnMeneuClickListnser {
    Context mContext;
    MaterialTextView tvPayRequest;
    ArrayList<WalletsData> list;
    Intent g;
    ArrayList<ProductRequestModel> payList;
    RecyclerView listItem;
    LoginDetails loginDetails;
    ProductRequest listnerContext;
    private String prodReqImage;
    private ProductRequestAdapter productRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_request);
        this.mContext = this;
        this.listnerContext = this;
        loginDetails = new SqliteDatabase(this).getLogin();
        init();
    }

    public void init() {
        payList = new ArrayList<>();
        list = new ArrayList<>();
        tvPayRequest = findViewById(R.id.payRequest);
        listItem = findViewById(R.id.listItem);

        tvPayRequest.setOnClickListener(v -> {
            requestProductWindow();
        });

        getPaymentRequestList();
    }

    private void getPaymentRequestList() {
        if (InternetConnection.checkConnection(mContext)) {
            payList.clear();
            Map<String, String> params = new HashMap<>();
            params.put("vendorId", loginDetails.getUser_id());

            // Calling JSON
            Call<String> call = RestClient.post().getProductRequests("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {
                            Log.e("prodres", response.body());
                            Gson gson = new Gson();
                            listItem.setVisibility(View.VISIBLE);
                            JSONObject object = new JSONObject(response.body());
                            Type type = new TypeToken<ArrayList<ProductRequestModel>>() {
                            }.getType();
                            payList = gson.fromJson(object.getJSONArray("data").toString(), type);

                           productRequestAdapter = new ProductRequestAdapter(payList, mContext);
                            listItem.setAdapter(productRequestAdapter);
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

    public void requestProductWindow() {

        View popupView = LayoutInflater.from(mContext).inflate(R.layout.product_request, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_gredient_bg));
        popupWindow.showAtLocation(popupView, Gravity.TOP, 0, 0);
        TextInputEditText tvCategoryName = popupView.findViewById(R.id.tvCategoryName);
        TextInputEditText tvProductName = popupView.findViewById(R.id.tvProductName);
        TextInputEditText edit_des = popupView.findViewById(R.id.edit_des);
        MaterialTextView tvPhoto = popupView.findViewById(R.id.tvPhoto);
        MaterialButton saveData = popupView.findViewById(R.id.saveData);

        ImageView closeImg = popupView.findViewById(R.id.closeImg);

        saveData.setOnClickListener(v -> {
            String catName = tvCategoryName.getText().toString();
            String prodName = tvProductName.getText().toString();
            String des = edit_des.getText().toString();
            if (catName.length() == 0) {
                Util.show(mContext, "Please enter the category name");
                return;
            }
            if (tvProductName.getText().toString().length() == 0) {
                Util.show(mContext, "Please enter the product name");
                return;
            }
            popupWindow.dismiss();
            requestProduct(catName, prodName, des);
        });

        tvPhoto.setOnClickListener(v -> {
            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();

        });

        closeImg.setOnClickListener(v -> {
            popupWindow.dismiss();

        });

    }

    private void requestProduct(String catName, String prodName, String des) {
        if (InternetConnection.checkConnection(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("vendor_id", loginDetails.getUser_id());
            params.put("category", catName);
            params.put("product_name", prodName);
            params.put("description", des);
            params.put("photo", prodReqImage);

            Log.e("data", params.toString());
            // Calling JSON
            Call<String> call = RestClient.post().productRequest("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {

                            Log.e("datares", response.body());

                            JSONObject object = new JSONObject(response.body());
                            if (object.getBoolean("status")) {
                                Util.show(mContext, "Product has been requested successfully.");
//                                getProduct();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            File file = ImagePicker.Companion.getFile(data);
            if (file != null) {
                Bitmap bitmapString = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, file.getAbsolutePath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapString.compress(Bitmap.CompressFormat.JPEG, 45, baos);
                byte[] imageBytes = baos.toByteArray();
//                img.setImageBitmap(bitmapString);
                prodReqImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//                tvPhoto.setText(file.getName());
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(mContext, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOptionClick(WalletsData data, int pos) {

    }

    @Override
    public void onOptionClick(PaymentRequestModel data, int pos) {

    }
}
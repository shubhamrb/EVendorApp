package com.dbcorp.vendorapp.ui.product;

import static android.app.Activity.RESULT_OK;
import static com.dbcorp.vendorapp.network.ApiService.IMG_PRODUCT_URL;
import static com.dbcorp.vendorapp.ui.offer.addOffer.BITMAP_SAMPLE_SIZE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.VendorShopProductAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.CameraUtils;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.VendorShopProduct;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VendorProduct extends Fragment implements VendorShopProductAdapter.OnMeneuClickListnser {

    Context mContext;
    private String prodReqImage;
    private AppCompatImageView img;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    ArrayList<VendorShopProduct> list;
    VendorShopProductAdapter vendorShopProductAdapter;
    LoginDetails loginDetails;
    RecyclerView listView;
    VendorProduct listnerContext;
    View view;
    MaterialTextView payRequest, tvPhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_product_list, container, false);
        loginDetails = new SqliteDatabase(mContext).getLogin();
        listView = view.findViewById(R.id.listView);
        payRequest = view.findViewById(R.id.payRequest);
        listnerContext = this;

        payRequest.setVisibility(View.VISIBLE);
        payRequest.setOnClickListener(v -> {
            startActivity(new Intent(mContext, ProductRequest.class));
        });

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

            // Calling JSON
            Call<String> call = RestClient.post().getVendorProduct("1234", loginDetails.getSk(), params);

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
                                Type type = new TypeToken<ArrayList<VendorShopProduct>>() {
                                }.getType();
                                list = gson.fromJson(object.getJSONArray("productList").toString(), type);

                                vendorShopProductAdapter = new VendorShopProductAdapter(list, listnerContext, mContext);
                                listView.setAdapter(vendorShopProductAdapter);
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
    public void onOptionClick(VendorShopProduct data) {
        otpWindow(data);
    }

    @Override
    public void onProductSwitch(int pos, VendorShopProduct data) {
        changeStatus(pos, data);
    }

    private void changeStatus(int pos, VendorShopProduct sid) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        productSwitch(sid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    vendorShopProductAdapter.notifyItemChanged(pos);
                    break;
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to change the product status?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void productSwitch(VendorShopProduct data) {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("vendor_product_id", data.getVendorProductId());

            // Calling JSON
            Call<String> call = RestClient.post().productStatus("1234", loginDetails.getSk(), params);

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
                                getProduct();
                                Util.show(mContext, "We have change the status of your product");
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

    @SuppressLint("SetTextI18n")
    public void otpWindow(VendorShopProduct data) {

        View popupView = LayoutInflater.from(mContext).inflate(R.layout.price_change, null);

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
        tvPrice.setText(data.getPrice());
        tvMrp.setText(data.getMrp());
        tvProductQuantity.setText(data.getQuantity());
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
            if (tvPrice.getText().toString().length() == 0) {
                Util.show(mContext, "Please enter the price");
                return;
            }
            if (tvMrp.getText().toString().length() == 0) {
                Util.show(mContext, "Please enter the MRP");
                return;
            }
            if (tvProductQuantity.getText().toString().length() == 0) {
                Util.show(mContext, "Please enter the quantity");
                return;
            }
            addProduct(data, tvPrice.getText().toString(),tvMrp.getText().toString(), tvProductQuantity.getText().toString());
        });

        closeImg.setOnClickListener(v -> {
            popupWindow.dismiss();

        });

    }

    private void addProduct(VendorShopProduct data, String price,String mrp, String quantity) {
        if (InternetConnection.checkConnection(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("product_id", data.getProductId());
            params.put("variant_id", data.getVariantId());
            params.put("vendor_id", loginDetails.getUser_id());
            params.put("price", price);
            params.put("mrp", mrp);
            params.put("quantity", quantity);

            Log.e("data", params.toString());
            // Calling JSON
            Call<String> call = RestClient.post().changePrice("1234", loginDetails.getSk(), params);

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
                                Util.show(mContext, "We have added the your product in your store");
                                getProduct();
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
        tvPhoto = popupView.findViewById(R.id.tvPhoto);
        MaterialButton saveData = popupView.findViewById(R.id.saveData);
        img = popupView.findViewById(R.id.img);

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
}

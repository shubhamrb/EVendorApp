package com.dbcorp.vendorapp.ui.offer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.OfferAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.OfferModel;
import com.dbcorp.vendorapp.model.Product;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.dbcorp.vendorapp.ui.product.ProductAdd;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dbcorp.vendorapp.network.Constants.IMAGE_OFFER_LATTER;


public class viewOffer extends Fragment {

    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }


    public static viewOffer getInstance(OfferModel data) {
        viewOffer myExamFragment = new viewOffer();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);

        myExamFragment.setArguments(bundle);
        return myExamFragment;
    }
    MaterialButton edit_btn;
    View view;
    OfferModel offerModel;
    MaterialTextView tvTittle,tvDes,tvRs,tvEndDate,tvStartDate;
    ShapeableImageView imageView;
    MaterialButton delete_btn;
    LoginDetails loginDetails;
    SwitchCompat aSwitch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_view_offer,container,false);
        init();
        return view;
    }

    public void init(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            offerModel  = (OfferModel) bundle.getSerializable("data");
        }
        loginDetails=new SqliteDatabase(getActivity()).getLogin();
        tvTittle=view.findViewById(R.id.tvTittle);
        tvDes=view.findViewById(R.id.tvDes);
        aSwitch=view.findViewById(R.id.switch1);
        delete_btn=view.findViewById(R.id.delete_btn);
        tvRs=view.findViewById(R.id.tvRs);
        tvEndDate=view.findViewById(R.id.tvEndDate);
        tvStartDate=view.findViewById(R.id.tvStartDate);
        imageView=view.findViewById(R.id.imageView);
        tvTittle.setText(offerModel.getTitle());
        tvDes.setText(offerModel.getDescription());
        tvRs.setText("Rs "+offerModel.getPrice());
        RequestOptions options = new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo);
        Glide.with(mContext).load(IMAGE_OFFER_LATTER+offerModel.getPhoto()).apply(options).into(imageView);
        tvStartDate.setText(offerModel.getStartdate());
        tvEndDate.setText(offerModel.getEnddate());
        edit_btn=view.findViewById(R.id.edit_btn);
        delete_btn.setOnClickListener(v->{
            deleteData();
        });
        aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            offerUpdate();
        });
        edit_btn.setOnClickListener(v->{
            ((HomeActivity) Objects.requireNonNull(mContext)).loadFragment(new addOffer(), "");
        });

    }

    //----------delete offer-------------
    private void deleteData(){
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("id", offerModel.getPhoto());

            // Calling JSON
            Call<String> call = RestClient.post().offerDelete("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {

                            Log.e("datares", response.body());

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
    //------------update offer------
    private void offerUpdate(){
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("id", offerModel.getOfferId());

            // Calling JSON
            Call<String> call = RestClient.post().offerUpdate("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {

                            Log.e("datares", response.body());

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

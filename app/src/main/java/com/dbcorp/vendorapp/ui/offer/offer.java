package com.dbcorp.vendorapp.ui.offer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.OfferAdapter;
import com.dbcorp.vendorapp.adapter.ProductAdapter;
import com.dbcorp.vendorapp.adapter.ServiceAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.OfferModel;
import com.dbcorp.vendorapp.model.Product;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.google.android.material.card.MaterialCardView;
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


public class offer extends Fragment implements OfferAdapter.OnMeneuClickListnser {

    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    offer listner;
    String arrItems[] = new String[]{};
    RecyclerView list;
    View view;
    OfferAdapter offerAdapter;
    MaterialCardView addOffer;
    ArrayList<OfferModel> offerModelsList;
    LoginDetails loginDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_offer, container, false);
        listner=this;
        init();

        return view;
    }

    public void init() {


        loginDetails = new SqliteDatabase(mContext).getLogin();
        list = view.findViewById(R.id.list);
        addOffer = view.findViewById(R.id.addOffer);
        addOffer.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.red_light));

        addOffer.setOnClickListener(v -> {
            ((HomeActivity) Objects.requireNonNull(mContext)).loadFragment(new addOffer(), "");
        });
        offerModelsList = new ArrayList<>();
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getOffer();
    }

    private void getOffer() {
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("vendorId", loginDetails.getUser_id());

            // Calling JSON
            Call<String> call = RestClient.post().getOffer("1234", loginDetails.getSk(), params);

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
                            Type type = new TypeToken<ArrayList<OfferModel>>() {
                            }.getType();
                            offerModelsList = gson.fromJson(object.getJSONArray("data").toString(), type);
                            offerAdapter = new OfferAdapter(offerModelsList, listner, mContext);
                            list.setAdapter(offerAdapter);
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
    public void onOptionClick(OfferModel data, int pos) {
        viewOffer viewOfferobj = viewOffer.getInstance(data);
        ((HomeActivity) Objects.requireNonNull(mContext)).loadFragment(viewOfferobj, "");

    }
}

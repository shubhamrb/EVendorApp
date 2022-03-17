package com.dbcorp.vendorapp.ui.coupons;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class addCoupon extends Fragment {

    Context mContext;

    int day1, day2;
    String mDay3, mDay4, mDay5, mDay6;
    int mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2;
    int discountType=0;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    TextInputEditText edit_Coupon, edit_des, edit_price, edit_user_limit,edit_coupon_limit,edit_min,edit_max;
    private AppCompatSpinner spinner;
    private View view;
    private MaterialTextView tvStart, endTime;
    private DatePickerDialog datePickerDialog;
    private LoginDetails loginDetails;
    MaterialButton submit_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.coupon_edt_add_details, container, false);
        init();
        return view;
    }

    private void init() {
        loginDetails = new SqliteDatabase(mContext).getLogin();
        endTime = view.findViewById(R.id.endTime);
        tvStart = view.findViewById(R.id.tvStart);
        submit_btn = view.findViewById(R.id.submit_btn);
        edit_user_limit = view.findViewById(R.id.edit_user_limit);
        edit_coupon_limit = view.findViewById(R.id.edit_coupon_limit);
        edit_min = view.findViewById(R.id.edit_min);
        edit_max = view.findViewById(R.id.edit_max);
        edit_price = view.findViewById(R.id.edit_price);
        edit_Coupon = view.findViewById(R.id.edit_Coupon);
        edit_des = view.findViewById(R.id.edit_des);
        spinner = view.findViewById(R.id.spinner);

        ArrayList<String>list = new ArrayList<>();
        list.add("Percentage (%)");
        list.add("Flat (₹)");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, list);

        spinner.setAdapter(adapter);
        endTime.setOnClickListener(v -> {
            showDate("end");
        });

        tvStart.setOnClickListener(v -> {
            showDate("start");
        });

        submit_btn.setOnClickListener(v -> {
            addCoupon();
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                discountType=pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showDate(String dateType) {
        final Calendar c = Calendar.getInstance();
        mYear1 = c.get(Calendar.YEAR);
        mMonth1 = c.get(Calendar.MONTH);
        mDay1 = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(mContext,
                (view, year, monthOfYear, dayOfMonth) -> {


                    if (dateType.equalsIgnoreCase("start")) {


                        day1 = dayOfMonth;

                        mDay3 = dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year;

                        mDay5 = year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth;

                        if (monthOfYear <= 9) {
                            int md = monthOfYear + 1;
                            tvStart.setText(Util.parseDateToddMMyyyy(year + "-"
                                    + "0" + md + "-" + dayOfMonth));
                        } else {
                            tvStart.setText(Util.parseDateToddMMyyyy(year + "-"
                                    + (monthOfYear + 1) + "-" + dayOfMonth));
                        }
                    } else {


                        day2 = dayOfMonth;
                        mDay4 = dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year;

                        mDay6 = year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth;

                        if (monthOfYear <= 9) {
                            int md = monthOfYear + 1;


                            endTime.setText(Util.parseDateToddMMyyyy(year + "-"
                                    + "0" + md + "-" + dayOfMonth));
                        } else {
                            endTime.setText(Util.parseDateToddMMyyyy(year + "-"
                                    + (monthOfYear + 1) + "-" + dayOfMonth));
                        }

                    }
                }, mYear1, mMonth1, mDay1);
        datePickerDialog.show();
    }

    private void addCoupon() {
        String coupon_code=edit_Coupon.getText().toString();
        String price=edit_price.getText().toString();
        String per_user_limit=edit_user_limit.getText().toString();
        String per_coupon_limit=edit_coupon_limit.getText().toString();
        String min_order=edit_min.getText().toString();
        String max_order=edit_max.getText().toString();

        if (coupon_code.isEmpty()){
            Toast.makeText(mContext,"Please enter coupon code.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (mDay5==null ||mDay5.isEmpty()){
            Toast.makeText(mContext,"Please enter start date.",Toast.LENGTH_SHORT).show();
            return;
        }
        if (mDay6==null ||mDay6.isEmpty()){
            Toast.makeText(mContext,"Please enter end date.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (price.isEmpty()){
            Toast.makeText(mContext,"Please enter price.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (per_user_limit.isEmpty()){
            Toast.makeText(mContext,"Please enter user limit.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (per_coupon_limit.isEmpty()){
            Toast.makeText(mContext,"Please enter coupon limit.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (min_order.isEmpty()){
            Toast.makeText(mContext,"Please enter minimum order limit.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (max_order.isEmpty()){
            Toast.makeText(mContext,"Please enter maximum order limit.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (InternetConnection.checkConnection(mContext)) {

            Map<String, String> params = new HashMap<>();
            params.put("coupon_code", coupon_code);
            params.put("description", edit_des.getText().toString());
            params.put("price", price);
            params.put("startdate", mDay5);
            params.put("enddate", mDay6);
            params.put("per_user_limit", per_user_limit);
            params.put("per_coupon_limit", per_coupon_limit);
            params.put("min_order", min_order);
            params.put("max_order", max_order);
            params.put("discount_type", String.valueOf(discountType));
            params.put("vendor_id", loginDetails.getUser_id());
            params.put("add_by", loginDetails.getUser_id());


            Log.e("map", params.toString());
            // Calling JSON
            Call<String> call = RestClient.post().addCoupon("1234", loginDetails.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {

                            JSONObject obj = new JSONObject(response.body());
                            Util.show(mContext, obj.getString("message"));
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            fm.popBackStack();

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    } else {
                        try {
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

package com.dbcorp.vendorapp.ui.coupons;

import android.app.DatePickerDialog;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class addCoupon extends Fragment {

    Context mContext;

    int  day1,day2;
    String mDay3, mDay4,mDay5,mDay6;
    int mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    TextInputEditText edit_Coupon,edit_des,edit_price,edit_limit;
    private View view;
    private MaterialTextView tvStart,endTime;
    private DatePickerDialog datePickerDialog;
    private LoginDetails loginDetails;
    MaterialButton submit_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.coupon_edt_add_details,container,false);
        init();
        return view;
    }

    private void init(){
        loginDetails=new SqliteDatabase(mContext).getLogin();
        endTime=view.findViewById(R.id.endTime);
        tvStart=view.findViewById(R.id.tvStart);
        submit_btn=view.findViewById(R.id.submit_btn);
        edit_limit=view.findViewById(R.id.edit_limit);
        edit_price=view.findViewById(R.id.edit_price);
        edit_Coupon=view.findViewById(R.id.edit_Coupon);
        edit_des=view.findViewById(R.id.edit_des);
        endTime.setOnClickListener(v->{
            showDate("end");
        });

        tvStart.setOnClickListener(v->{
            showDate("start");
        });

        submit_btn.setOnClickListener(v->{
            addCoupon();
        });
    }

    private void showDate(String dateType){
        final Calendar c = Calendar.getInstance();
        mYear1 = c.get(Calendar.YEAR);
        mMonth1 = c.get(Calendar.MONTH);
        mDay1 = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(mContext,
                (view, year, monthOfYear, dayOfMonth) -> {



                    if(dateType.equalsIgnoreCase("start")){


                        day1 = dayOfMonth;

                        mDay3 = dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year;

                        mDay5 = year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth;

                        if(monthOfYear<=9){
                            int md=monthOfYear + 1;
                            tvStart.setText(Util.parseDateToddMMyyyy(year + "-"
                                    + "0" +md + "-" + dayOfMonth));
                        }else{
                            tvStart.setText(Util.parseDateToddMMyyyy(year + "-"
                                    + (monthOfYear + 1) + "-" + dayOfMonth));
                        }
                    }else{


                        day2 = dayOfMonth;
                        mDay4 = dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year;

                        mDay6 = year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth;

                        if(monthOfYear<=9){
                            int md=monthOfYear + 1;


                            endTime.setText(Util.parseDateToddMMyyyy(year + "-"
                                    + "0" +md + "-" + dayOfMonth));
                        }else{
                            endTime.setText(Util.parseDateToddMMyyyy(year + "-"
                                    + (monthOfYear + 1) + "-" + dayOfMonth));
                        }

                    }
                }, mYear1, mMonth1, mDay1);
        datePickerDialog.show();
    }
    private void addCoupon(){
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("coupon_code", edit_Coupon.getText().toString());
            params.put("description", edit_des.getText().toString());
            params.put("price",edit_price.getText().toString());
            params.put("startdate",mDay5);
            params.put("enddate",mDay6);
            params.put("per_user_limit", edit_limit.getText().toString());
            params.put("vendor_id", loginDetails.getUser_id());
            params.put("add_by", loginDetails.getUser_id());


            Log.e("map",params.toString());
            // Calling JSON
            Call<String> call = RestClient.post().addCoupon("1234",loginDetails.getSk(),params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {

                            JSONObject obj=new JSONObject(response.body());
                            Util.show(mContext,obj.getString("message"));
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            fm.popBackStack();

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    } else {
                        try{
                            Toast.makeText(mContext, "error message"+response.errorBody().string(), Toast.LENGTH_SHORT).show();

                        }catch(IOException e){
                            Toast.makeText(mContext, "error message"+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
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

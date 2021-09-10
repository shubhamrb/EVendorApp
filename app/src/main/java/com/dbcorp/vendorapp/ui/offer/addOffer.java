package com.dbcorp.vendorapp.ui.offer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.CameraUtils;
import com.dbcorp.vendorapp.helper.ImageGalleryCode;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class addOffer extends Fragment {
    Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    public static final int PICK_IMAGE_CHOOSER_REQUEST_CODE = 200;
    ImageGalleryCode imageGalleryCode;
    static Bitmap bitmapString;
    static String getBitmapstr="";
    View view;
    MaterialButton choose;
    ShapeableImageView imageView;
    public static final int BITMAP_SAMPLE_SIZE = 0;
    LoginDetails loginDetails;
    TextInputEditText tvTittle,tvPrice,tvDes;
    DatePickerDialog datePickerDialog;

    MaterialButton submit_btn;
    int  day1,day2;
    String mDay3, mDay4,mDay5,mDay6;
    int mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2;
    MaterialTextView tvStart,endTime;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.offer_edt_add_details,container,false);
         init();
        return view;
    }

    public void init(){
        loginDetails=new SqliteDatabase(getActivity()).getLogin();
        tvTittle=view.findViewById(R.id.tvTittle);
        tvPrice=view.findViewById(R.id.tvPrice);
        tvDes=view.findViewById(R.id.tvDes);
        submit_btn=view.findViewById(R.id.saveData);
        choose=view.findViewById(R.id.choose);
        tvStart=view.findViewById(R.id.tvStart);
        endTime=view.findViewById(R.id.endTime);
        imageView=view.findViewById(R.id.imageView);
        choose.setOnClickListener(v->{
            ImagePicker.Companion.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        submit_btn.setOnClickListener(v->{
            Log.e("bhs","mks");
            addOffer();
        });
        endTime.setOnClickListener(v->{
            showDate("end");
        });

        tvStart.setOnClickListener(v->{
            showDate("start");
        });
    }





    public void showDate(String dateType){
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
    private void addOffer(){
        if (InternetConnection.checkConnection(mContext)) {


            Map<String, String> params = new HashMap<>();
            params.put("title", tvTittle.getText().toString());
            params.put("description", tvDes.getText().toString());
            params.put("photo",getBitmapstr);
            params.put("startdate",mDay5);
            params.put("enddate",mDay6);
            params.put("price", tvPrice.getText().toString());
            params.put("add_by", loginDetails.getUser_id());
            params.put("vendor_id", loginDetails.getUser_id());


            Log.e("map",params.toString());
            // Calling JSON
            Call<String> call = RestClient.post().addHome("1234",loginDetails.getSk(),params);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                }
                break;
            }

        }

        if (resultCode == RESULT_OK) {
            //Image Uri will not be null for RESULT_OK

            Uri compressUri= imageGalleryCode.getPickImageResultUri(getActivity(),data);
            //You can get File object from intent

            //Uri fileUri = data;
            File file = ImagePicker.Companion.getFile(data);

            imageView.setImageURI(compressUri);
            bitmapString = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, file.getAbsolutePath());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapString.compress(Bitmap.CompressFormat.JPEG, 45, baos);
            byte[] imageBytes = baos.toByteArray();
            getBitmapstr = Base64.encodeToString(imageBytes, Base64.DEFAULT);


            //You can get File object from intent

            //You can also get File Path from intent
            String filePath = ImagePicker.Companion.getFilePath(data);


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}

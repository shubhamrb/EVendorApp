package com.dbcorp.vendorapp.ui.servicevendor;

import static com.dbcorp.vendorapp.network.ApiService.SHOP_IMG_URL;
import static com.dbcorp.vendorapp.ui.offer.addOffer.BITMAP_SAMPLE_SIZE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.adapter.CheckBoxMenuAdapter;
import com.dbcorp.vendorapp.adapter.PlacesAdapter;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.database.UserSharedPreference;
import com.dbcorp.vendorapp.helper.CameraUtils;
import com.dbcorp.vendorapp.helper.ImageGalleryCode;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.DroupDownModel;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.network.ApiService;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.dbcorp.vendorapp.ui.Login;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VendorActivity extends AppCompatActivity implements CheckBoxMenuAdapter.OnCheckListener, OnMapReadyCallback {
    private Toolbar toolbar;
    private TextInputLayout edt_zip, today_default_message, edt_shop_name, edtDes,
            edtReason, min_cash, max_cash, gst_number, license_number;
    private MaterialTextView startTime, endTime, tvDays, addImage, upBtn;
    private SwitchCompat shopOnOff, gpsOnOff;
    private TimePickerDialog mTimePicker;
    AppCompatImageView selectedImageOne, selectedImageTwo, selectedImageThree, selectedImageFour,
            imageShopDoc, selectedImageAadhar, selectedImagePan, selectedImageFood, selectedImageMedical, selectedImageGst, selectedImageElectricity;
    String startDay, endDay, selectImgType;

    Dialog dialog;
    Context mContext;
    private ArrayList<String> stringArrayList;
    private ArrayList<String> stringCatArrayList;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    public static final int PICK_IMAGE_CHOOSER_REQUEST_CODE = 200;
    ImageGalleryCode imageGalleryCode;
    static Bitmap bitmapString, bitmapString2, bitmapString3, bitmapString4;
    static String getBitmapstr = "", image1 = "no", image2 = "no", image3 = "no", image4 = "no", shopDoc = "no",
            aadharDoc = "no", panDoc = "no", foodDoc = "no", medicalDoc = "no", gstDoc = "no", electricityDoc = "no";
    ImageView selectedImage;
    LoginDetails loginDetails;
    HashMap<String, String> getAddress;
    ArrayList<String> imagesList;
    VendorActivity listenerContext;
    UserSharedPreference sessionUser;
    private ArrayList<DroupDownModel> selectDays;
    public AutoCompleteTextView tvStartPoint, tvEndPoint;

    PlacesClient placesClient;

    MaterialTextView pinMap, savebtn, tvlati, tvLongi;
    PlacesAdapter startPointplaceAdapter;

    HashMap<String, String> address;
    private LoginDetails login;
    private AppCompatSpinner docTypeSpinner;
    private ArrayList<String> docTypeList;
    private String docType = "";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        mContext = this;
        listenerContext = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        login = new SqliteDatabase(this).getLogin();
        sessionUser = new UserSharedPreference(this);

        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }*/

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Setting");
        setSupportActionBar(toolbar);
        tvStartPoint = findViewById(R.id.edtStartPoint);
        tvlati = findViewById(R.id.tvlati);
        tvLongi = findViewById(R.id.tvLongi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        selectDays = new ArrayList<>();
        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyBAUTLllLoIeyoajizeO0BZS8HaTYHD2Y0", Locale.getDefault());
        }
        placesClient = Places.createClient(this);

        tvStartPoint.setOnFocusChangeListener((view, b) -> {
            if (b) {
                tvStartPoint.setThreshold(1);
                tvStartPoint.setOnItemClickListener(autocompleteClickListener);
                startPointplaceAdapter = new PlacesAdapter(VendorActivity.this, placesClient);
                tvStartPoint.setAdapter(startPointplaceAdapter);
            }
        });

        getDays();
        init();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    void getDays() {
        DroupDownModel obj1 = new DroupDownModel();
        obj1.setName("Sun");
        obj1.setDescription("Sun");
        obj1.setStatus("0");
        selectDays.add(obj1);
        DroupDownModel obj2 = new DroupDownModel();
        obj2.setName("Mon");
        obj2.setDescription("Mon");
        obj2.setStatus("0");
        selectDays.add(obj2);
        DroupDownModel obj3 = new DroupDownModel();
        obj3.setName("Tue");
        obj3.setDescription("Tue");
        obj3.setStatus("0");
        selectDays.add(obj3);
        DroupDownModel obj4 = new DroupDownModel();
        obj4.setName("Wed");
        obj4.setDescription("Wed");
        obj4.setStatus("0");
        selectDays.add(obj4);
        DroupDownModel obj5 = new DroupDownModel();
        obj5.setName("Thu");
        obj5.setDescription("Thu");
        obj5.setStatus("0");
        selectDays.add(obj5);
        DroupDownModel obj6 = new DroupDownModel();
        obj6.setName("Fri");
        obj6.setDescription("Fri");
        obj6.setStatus("0");
        selectDays.add(obj6);
        DroupDownModel obj7 = new DroupDownModel();
        obj7.setName("Sat");
        obj7.setDescription("Sat");
        obj7.setStatus("0");
        selectDays.add(obj7);
    }

    public void init() {
        imagesList = new ArrayList<>();
        getAddress = sessionUser.getAddress();
        loginDetails = new SqliteDatabase(mContext).getLogin();

        tvDays = findViewById(R.id.tvDays);
        upBtn = findViewById(R.id.upBtn);
        selectedImageOne = findViewById(R.id.selectedImageOne);
        selectedImageTwo = findViewById(R.id.selectedImageTwo);
        selectedImageFour = findViewById(R.id.selectedImageFour);
        selectedImageThree = findViewById(R.id.selectedImageThree);
        today_default_message = findViewById(R.id.today_default_message);
        addImage = findViewById(R.id.addImage);
        edt_zip = findViewById(R.id.edt_zip);
        edt_shop_name = findViewById(R.id.edt_shop_name);
        edtReason = findViewById(R.id.edtReason);
        edtDes = findViewById(R.id.edtDes);
        startTime = findViewById(R.id.tvStart);
        endTime = findViewById(R.id.endTime);
        shopOnOff = findViewById(R.id.shopOnOff);
        docTypeSpinner = findViewById(R.id.docTypeSpinner);

        imageShopDoc = findViewById(R.id.imageShopDoc);
        selectedImageAadhar = findViewById(R.id.adhaarDoc);
        selectedImagePan = findViewById(R.id.panDoc);
        selectedImageFood = findViewById(R.id.foodDoc);
        selectedImageMedical = findViewById(R.id.medicalDoc);
        selectedImageGst = findViewById(R.id.gstDoc);
        selectedImageElectricity = findViewById(R.id.electricityDoc);

        min_cash = findViewById(R.id.min_cash);
        max_cash = findViewById(R.id.max_cash);
        gst_number = findViewById(R.id.gst_number);
        license_number = findViewById(R.id.license_number);
        gpsOnOff = findViewById(R.id.gpsOnOff);


        // Spinner Drop down elements
        docTypeList = new ArrayList<String>();
        docTypeList.add("Select Type");
        docTypeList.add("Gumasta");
        docTypeList.add("Firm Registration Certificate");
        docTypeList.add("Pvt. Ltd.");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, docTypeList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        docTypeSpinner.setAdapter(dataAdapter);

        docTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    docType = String.valueOf(position);
                } else {
                    docType = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        shopOnOff.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtReason.setVisibility(View.GONE);
            } else {
                edtReason.setVisibility(View.VISIBLE);
            }
        });

        startTime.setOnClickListener(v -> {
            showDate("start");
        });
        endTime.setOnClickListener(v -> {
            showDate("end");
        });

        selectedImageOne.setOnClickListener(v -> {
            selectImgType = "1";
            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        selectedImageTwo.setOnClickListener(v -> {
            selectImgType = "2";
            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        selectedImageThree.setOnClickListener(v -> {
            selectImgType = "3";

            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)
                    //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        selectedImageFour.setOnClickListener(v -> {
            selectImgType = "4";

            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        imageShopDoc.setOnClickListener(v -> {
            selectImgType = "5";

            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        selectedImageAadhar.setOnClickListener(v -> {
            selectImgType = "6";

            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        selectedImagePan.setOnClickListener(v -> {
            selectImgType = "7";

            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        selectedImageFood.setOnClickListener(v -> {
            selectImgType = "8";

            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        selectedImageMedical.setOnClickListener(v -> {
            selectImgType = "9";

            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        selectedImageGst.setOnClickListener(v -> {
            selectImgType = "10";

            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        selectedImageElectricity.setOnClickListener(v -> {
            selectImgType = "11";

            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        tvDays.setOnClickListener(v -> {
            Util.showCheckBox(selectDays, "Please Select Days", mContext, listenerContext);
        });

        upBtn.setOnClickListener(v -> {
            if (stringCatArrayList.size() == 0) {
                Util.show(mContext, "Please Select Shop off days.");
                return;
            }
            if (docType.equals("")) {
                Util.show(mContext, "Please Select Shop Document type.");
                return;
            }
            if (shopDoc == null || shopDoc.equals("no")) {
                Util.show(mContext, "Please Select Shop Document.");
                return;
            }
            if (min_cash.getEditText().getText().toString().equals("")) {
                Util.show(mContext, "Please Enter Min Cash.");
                return;
            }
            if (max_cash.getEditText().getText().toString().equals("")) {
                Util.show(mContext, "Please Enter Max Cash.");
                return;
            }
            if (aadharDoc == null || aadharDoc.equals("no")) {
                Util.show(mContext, "Please Select Aadhaar Document.");
                return;
            }
            if (panDoc == null || panDoc.equals("no")) {
                Util.show(mContext, "Please Select Pan Document.");
                return;
            }
            updateVendorSetting();

        });

        switch (login.getIs_approve()) {
            case "1":
                gpsOnOff.setEnabled(true);
                license_number.setEnabled(true);
                gst_number.setEnabled(true);
                max_cash.setEnabled(true);
                min_cash.setEnabled(true);
                shopOnOff.setEnabled(true);
                imageShopDoc.setEnabled(true);
                docTypeSpinner.setEnabled(true);
                edt_shop_name.setEnabled(true);
                edt_zip.setEnabled(true);
                today_default_message.setEnabled(true);
                edtDes.setEnabled(true);
                edtReason.setEnabled(true);
                tvStartPoint.setEnabled(true);
                tvlati.setEnabled(true);
                tvLongi.setEnabled(true);
                tvDays.setEnabled(true);
                selectedImageOne.setEnabled(true);
                selectedImageTwo.setEnabled(true);
                selectedImageThree.setEnabled(true);
                selectedImageFour.setEnabled(true);
                selectedImageAadhar.setEnabled(true);
                selectedImagePan.setEnabled(true);
                selectedImageFood.setEnabled(true);
                selectedImageMedical.setEnabled(true);
                selectedImageGst.setEnabled(true);
                selectedImageElectricity.setEnabled(true);
                break;
            case "2":
                selectedImageElectricity.setEnabled(false);
                selectedImageGst.setEnabled(false);
                selectedImageMedical.setEnabled(false);
                selectedImageFood.setEnabled(false);
                selectedImagePan.setEnabled(false);
                selectedImageAadhar.setEnabled(false);
                gpsOnOff.setEnabled(false);
                license_number.setEnabled(false);
                gst_number.setEnabled(false);
                max_cash.setEnabled(false);
                min_cash.setEnabled(false);
                shopOnOff.setEnabled(false);
                imageShopDoc.setEnabled(false);
                docTypeSpinner.setEnabled(false);
                edt_shop_name.setEnabled(false);
                edt_zip.setEnabled(false);
                today_default_message.setEnabled(false);
                edtDes.setEnabled(false);
                edtReason.setEnabled(false);
                tvStartPoint.setEnabled(false);
                tvlati.setEnabled(false);
                tvLongi.setEnabled(false);
                tvDays.setEnabled(false);
                selectedImageOne.setEnabled(false);
                selectedImageTwo.setEnabled(false);
                selectedImageThree.setEnabled(false);
                selectedImageFour.setEnabled(false);
                break;
        }

        getVendorSetting();
    }

    private void showDate(String dateType) {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(VendorActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (dateType.equalsIgnoreCase("start")) {
                    startTime.setText(selectedHour + ":" + selectedMinute);
                } else {
                    endTime.setText(selectedHour + ":" + selectedMinute);

                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
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
            int type = Integer.parseInt(selectImgType);
            Uri compressUri = imageGalleryCode.getPickImageResultUri(mContext, data);
            File file = ImagePicker.Companion.getFile(data);
            Bitmap bitmapString = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, file.getAbsolutePath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapString.compress(Bitmap.CompressFormat.JPEG, 45, baos);
            if (type == 1) {
                selectedImageOne.setImageResource(0);
                byte[] imageBytes = baos.toByteArray();
                selectedImageOne.setImageBitmap(bitmapString);
                image1 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                selectImgType = "";
            } else if (type == 2) {
                selectedImageTwo.setImageResource(0);
                byte[] imageBytes = baos.toByteArray();
                selectedImageTwo.setImageBitmap(bitmapString);
                image2 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                selectImgType = "";
            } else if (type == 3) {
                selectedImageThree.setImageResource(0);
                byte[] imageBytes = baos.toByteArray();
                selectedImageThree.setImageBitmap(bitmapString);
                image3 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                selectImgType = "";

            } else if (type == 4) {
                selectedImageFour.setImageResource(0);
                byte[] imageBytes = baos.toByteArray();
                selectedImageFour.setImageBitmap(bitmapString);
                image4 = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            } else if (type == 5) {
                imageShopDoc.setImageResource(0);
                byte[] imageBytes = baos.toByteArray();
                imageShopDoc.setImageBitmap(bitmapString);
                shopDoc = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } else if (type == 6) {
                selectedImageAadhar.setImageResource(0);
                byte[] imageBytes = baos.toByteArray();
                selectedImageAadhar.setImageBitmap(bitmapString);
                aadharDoc = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            } else if (type == 7) {
                selectedImagePan.setImageResource(0);
                byte[] imageBytes = baos.toByteArray();
                selectedImagePan.setImageBitmap(bitmapString);
                panDoc = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            } else if (type == 8) {
                selectedImageFood.setImageResource(0);
                byte[] imageBytes = baos.toByteArray();
                selectedImageFood.setImageBitmap(bitmapString);
                foodDoc = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            } else if (type == 9) {
                selectedImageMedical.setImageResource(0);
                byte[] imageBytes = baos.toByteArray();
                selectedImageMedical.setImageBitmap(bitmapString);
                medicalDoc = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            } else if (type == 10) {
                selectedImageGst.setImageResource(0);
                byte[] imageBytes = baos.toByteArray();
                selectedImageGst.setImageBitmap(bitmapString);
                gstDoc = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            } else if (type == 11) {
                selectedImageElectricity.setImageResource(0);
                byte[] imageBytes = baos.toByteArray();
                selectedImageElectricity.setImageBitmap(bitmapString);
                electricityDoc = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            }


            //You can get File object from intent

            //You can also get File Path from intent
            //String filePath = ImagePicker.Companion.getFilePath(data);


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(mContext, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


    }


    private void getVendorSetting() {
        stringCatArrayList = new ArrayList<>();
        if (InternetConnection.checkConnection(mContext)) {
            Util.showDialog("Please wait..", mContext);
            Map<String, String> params = new HashMap<>();
            params.put("vendorId", loginDetails.getUser_id());

            Log.e("vendor_id", params.toString());
            RestClient.post().getVendorSetting(ApiService.APP_DEVICE_ID, loginDetails.getSk(), params).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, Response<String> response) {
                    JSONObject object;
                    try {

                        object = new JSONObject(response.body());
                        Log.e("message", object.getString("message"));

                        if (object.getBoolean("status")) {
                            Util.hideDialog();
                            JSONObject vendorSetting = object.getJSONObject("listData");
                            edt_shop_name.getEditText().setText(vendorSetting.getString("shop_name"));
                            edt_zip.getEditText().setText(vendorSetting.getString("zipcode"));
                            tvStartPoint.setText(vendorSetting.getString("address"));
                            tvlati.setText(vendorSetting.getString("lat"));
                            tvLongi.setText(vendorSetting.getString("lng"));
                            edtReason.getEditText().setText(vendorSetting.getString("order_received"));

                            today_default_message.getEditText().setText(vendorSetting.getString("today_default_message"));

                            startTime.setText(vendorSetting.getString("shop_open_time"));
                            endTime.setText(vendorSetting.getString("shop_close_time"));

                            edtDes.getEditText().setText(vendorSetting.getString("description"));


                            String[] catArray = vendorSetting.getString("shop_off_days").split(",");
                            stringCatArrayList.addAll(Arrays.asList(catArray));

                            tvDays.setText(vendorSetting.getString("shop_off_days"));

                            Glide.with(mContext)
                                    .load(SHOP_IMG_URL + vendorSetting.getString("image1"))
                                    .error(R.drawable.ic_baseline_add_photo_alternate_24)
                                    .placeholder(R.drawable.ic_baseline_add_photo_alternate_24)
                                    .into(selectedImageOne);

                            Glide.with(mContext)
                                    .load(SHOP_IMG_URL + vendorSetting.getString("image2"))
                                    .error(R.drawable.ic_baseline_add_photo_alternate_24)
                                    .placeholder(R.drawable.ic_baseline_add_photo_alternate_24)
                                    .into(selectedImageTwo);

                            Glide.with(mContext)
                                    .load(SHOP_IMG_URL + vendorSetting.getString("image3"))
                                    .error(R.drawable.ic_baseline_add_photo_alternate_24)
                                    .placeholder(R.drawable.ic_baseline_add_photo_alternate_24)
                                    .into(selectedImageThree);

                            Glide.with(mContext)
                                    .load(SHOP_IMG_URL + vendorSetting.getString("image4"))
                                    .error(R.drawable.ic_baseline_add_photo_alternate_24)
                                    .placeholder(R.drawable.ic_baseline_add_photo_alternate_24)
                                    .into(selectedImageFour);

                        } else {
                            Util.hideDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Util.hideDialog();

                    }


                }

                @Override
                public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {

                    try {
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Util.hideDialog();
                }
            });

        }
    }

    private void updateVendorSetting() {
        if (InternetConnection.checkConnection(mContext)) {
            Util.showDialog("Please wait..", mContext);
            Map<String, String> params = new HashMap<>();
            params.put("vendor_id", loginDetails.getUser_id());
            params.put("shop_name", edt_shop_name.getEditText().getText().toString());
            params.put("zipcode", edt_zip.getEditText().getText().toString());
            params.put("address", tvStartPoint.getText().toString());
            params.put("lat", tvlati.getText().toString());
            params.put("lng", tvLongi.getText().toString());
            params.put("order_received", edtReason.getEditText().getText().toString());
            params.put("today_default_message", today_default_message.getEditText().getText().toString());
            params.put("shop_open_time", startTime.getText().toString());
            params.put("shop_close_time", endTime.getText().toString());
            params.put("description", edtDes.getEditText().getText().toString());
            params.put("shop_off_days", stringCatArrayList.toString());
            params.put("image1", image1);
            params.put("image2", image2);
            params.put("image3", image3);
            params.put("image4", image4);
            params.put("doc_type", docType);
            params.put("min_limit_in_cod", min_cash.getEditText().getText().toString());
            params.put("max_limit_in_cod", max_cash.getEditText().getText().toString());
            params.put("gst_no", gst_number.getEditText().getText().toString());
            params.put("license_no", license_number.getEditText().getText().toString());
            params.put("gps_show", gpsOnOff.isChecked() ? "1" : "2");
            params.put("shop_doc", shopDoc);
            params.put("aadhar_doc", aadharDoc);
            params.put("pan_doc", panDoc);
            params.put("food_license_doc", foodDoc);
            params.put("medical_license_doc", medicalDoc);
            params.put("gst_certificate_doc", gstDoc);
            params.put("electricity_bill_doc", electricityDoc);
            Log.e("vendor_id", params.toString());
            RestClient.post().updateVendorSetting(ApiService.APP_DEVICE_ID, loginDetails.getSk(), params).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, Response<String> response) {
                    Gson gson = new Gson();
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.body());
                        Log.e("message", object.getString("message"));

                        if (object.getBoolean("status")) {
                            Util.hideDialog();
                            new AlertDialog.Builder(mContext)
//                                    .setTitle("Delete entry")
                                    .setMessage("Your profile has been created and sent for approval, Please login in a while to check the status.")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                            Logout();
                                        }
                                    })
                                    .show();
                        } else {
                            Util.hideDialog();
                            Util.show(mContext, "something is wrong");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Util.hideDialog();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {

                    try {
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Util.hideDialog();
                }
            });

        }
    }

    // Logout Function ///////////
    private void Logout() {
        if (InternetConnection.checkConnection(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("userId", login.getUser_id());
            Util.showDialog("Please wait..", mContext);
            // Calling JSON
            Call<String> call = RestClient.post().logoutUser("1234", login.getSk(), params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.getBoolean("status")) {
                                new SqliteDatabase(mContext).removeLoginUser();
                                startActivity(new Intent(mContext, Login.class));
                                finish();
                            } else {
                                Util.show(mContext, object.getString("message"));
                            }
                            Util.hideDialog();
                        } catch (Exception e) {
                            Util.hideDialog();
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
                        Util.hideDialog();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Util.hideDialog();
                    Toast.makeText(mContext, R.string.string_some_thing_wrong, Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Util.hideDialog();
            Toast.makeText(mContext, R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOptionCheckClick(ArrayList<DroupDownModel> arrayList, DroupDownModel data) {
        stringArrayList = new ArrayList<>();
        stringCatArrayList = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getStatus().equals("1")) {
                stringArrayList.add(arrayList.get(i).getId());
                stringCatArrayList.add(arrayList.get(i).getName());
            }
        }

        tvDays.setText(stringCatArrayList.toString().replace("[", "").replace("]", ""));

    }


    private final AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                final AutocompletePrediction item = startPointplaceAdapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }
//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.
                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }
                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {

                            final LatLng lotlonaddress = task.getPlace().getLatLng();
                            double ll = lotlonaddress.latitude;
                            double lo = lotlonaddress.longitude;
                            tvlati.setText(String.valueOf(lotlonaddress.latitude));
                            tvLongi.setText(String.valueOf(lotlonaddress.longitude));
                            sessionUser.setAddress(tvStartPoint.getText().toString(), String.valueOf(lotlonaddress.latitude), String.valueOf(lotlonaddress.longitude));


                            Log.e("dsfsdfsdf", String.valueOf(ll));
                            Log.e("dsfsdfsdf", String.valueOf(lo));


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("fghfjhfghfgh", e.getMessage());
                            // responseView.setText(e.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("fghfjhfghfgh", e.getMessage());
            }
        }


    };

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
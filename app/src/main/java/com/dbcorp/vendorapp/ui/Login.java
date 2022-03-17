package com.dbcorp.vendorapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.network.InternetConnection;
import com.dbcorp.vendorapp.network.RestClient;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.dbcorp.vendorapp.ui.services.PlanService;
import com.dbcorp.vendorapp.ui.servicevendor.VendorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    TextInputEditText mobile, password;
    Context mContext;
    String token;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mContext = this;
        init();

    }

    public void init() {
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);

        findViewById(R.id.submitBtn).setOnClickListener(v -> {
            String usermobile = "", pass = "";
            if (mobile.getText() != null) {
                usermobile = mobile.getText().toString();
            }
            if (password.getText() != null) {
                pass = password.getText().toString();
            }
            if (usermobile.equals("")) {
                Util.show(this, "Please Enter Username");
                return;
            }
            if (pass.equals("")) {
                Util.show(this, "Please Enter Password");
                return;
            }

            login(usermobile, pass);
        });

        findViewById(R.id.register).setOnClickListener(v -> {
            Intent mv = new Intent(Login.this, Register.class);
            startActivity(mv);

        });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    token = task.getResult();
                });
    }


    public void login(String mobile, String pass) {
        if (InternetConnection.checkConnection(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("number", mobile);
            params.put("password", pass);
            params.put("type", "4");
            params.put("token", token);

            Util.showDialog("Please wait..", mContext);
            // Calling JSON
            Call<LoginDetails> call = RestClient.post().loginUser("1234", "1", params);

            // Enqueue Callback will be call when get response...
            call.enqueue(new Callback<LoginDetails>() {
                @Override
                public void onResponse(@NonNull Call<LoginDetails> call, @NonNull Response<LoginDetails> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {
                            LoginDetails obj = response.body();
                            if (obj.getStatus()) {
                                if (obj.getMasterCatId().equalsIgnoreCase("1")) {
                                    new SqliteDatabase(mContext).addLogin(obj);
                                    startActivity(new Intent(mContext, PlanService.class));
                                    finish();
                                } else {
                                    new SqliteDatabase(mContext).addLogin(obj);
                                    switch (obj.getIs_approve()){
                                        case "1":
                                            startActivity(new Intent(mContext, VendorActivity.class));
                                            break;
                                        case "2":
                                            startActivity(new Intent(mContext, HomeActivity.class));
                                            break;
                                        case "3":
                                            break;
                                    }
                                    finish();
                                }

                            } else {
                                Util.show(mContext, obj.getMessage());
                            }
                            Util.hideDialog();

                        } catch (Exception e) {
                            Util.hideDialog();
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            Toast.makeText(mContext, "error message" + response.errorBody().string(), Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            Toast.makeText(mContext, "error message" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        Util.hideDialog();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginDetails> call, @NonNull Throwable t) {
                    Toast.makeText(mContext, R.string.string_some_thing_wrong, Toast.LENGTH_SHORT).show();
                    Util.hideDialog();
                }

            });
        } else {
            Util.hideDialog();
            Toast.makeText(mContext, R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }
}

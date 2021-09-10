package com.dbcorp.vendorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.helper.Util;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.dbcorp.vendorapp.ui.Login;

public class SplashScreen extends AppCompatActivity {

    LoginDetails login;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalce_screen);
        mContext = this;
        login = new SqliteDatabase(this).getLogin();



            if (login == null) {
                Intent intent = new Intent(mContext, Login.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                finish();
            }
   //     new MyThread().start();

    }


    class MyThread extends Thread {


        public void run() {
            try {
                Thread.sleep(3000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Log.e("status=====>>>>>>", String.valueOf(session.isLogin()));

            try {
                if (login == null) {
                    Intent intent = new Intent(mContext, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }


            } catch (Exception e) {
                Util.show(mContext,"Error : "+e.getMessage());
            }
        }
    }
}

package com.dbcorp.vendorapp.ui.offer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dbcorp.vendorapp.R;
import com.google.android.material.card.MaterialCardView;


public class editOffer extends Fragment {

    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }


    MaterialCardView edit_btn;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.offer_edt_add_details,container,false);

        return view;
    }

    public void init(){

        edit_btn=view.findViewById(R.id.edit_btn);

    }





}

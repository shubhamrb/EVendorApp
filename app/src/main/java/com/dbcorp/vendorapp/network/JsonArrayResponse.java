package com.dbcorp.vendorapp.network;

import androidx.annotation.NonNull;

import com.dbcorp.vendorapp.model.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by kipl104 on 04-Apr-17.
 */

public class JsonArrayResponse<T>  {

    @SerializedName("data")
    public List<T> list;
    public ArrayList<T> listData;


}

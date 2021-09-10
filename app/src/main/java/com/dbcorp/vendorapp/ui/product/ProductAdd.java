package com.dbcorp.vendorapp.ui.product;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dbcorp.vendorapp.R;
import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.model.Category;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.Product;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;


public class ProductAdd extends Fragment {

    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static ProductAdd getInstance(Product data) {
        ProductAdd myExamFragment = new ProductAdd();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);

        myExamFragment.setArguments(bundle);
        return myExamFragment;
    }

    Product product;
    LoginDetails userDetails;
    View view;
    MaterialTextView tvProduct, tvDes, tvQuantity;
    TextInputEditText edtPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_product, container, false);

        init();
        setData();
        return view;
    }

    public void init() {
        userDetails=new SqliteDatabase(getActivity()).getLogin();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            product  = (Product) bundle.getSerializable("data");
        }
        tvProduct = view.findViewById(R.id.tvProduct);
        tvDes = view.findViewById(R.id.tvDes);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        edtPrice=view.findViewById(R.id.edtPrice);
    }

    public void setData() {

        tvProduct.setText(product.getName());
        tvDes.setText(product.getDescription());
        tvQuantity.setText(product.getDiscount());
        edtPrice.setText(product.getPrice());

    }


}

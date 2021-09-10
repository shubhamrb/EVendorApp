package com.dbcorp.vendorapp.model.orderview;

import com.dbcorp.vendorapp.model.OrderDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bhupesh Sen on 02-05-2021.
 */
public class CustomerOrderDetails implements Serializable {

    @SerializedName("orderDetails")
    @Expose
    private OrderParam orderDetails;
    @SerializedName("view")
    @Expose
    private Boolean viewList;
    @SerializedName("productDetails")
    @Expose
    private ArrayList<Orders> productDetails = null;

    public OrderParam  getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderParam orderDetails) {
        this.orderDetails = orderDetails;
    }

    public ArrayList<Orders> getProductDetails() {
        return productDetails;
    }

    public Boolean getViewList() {
        return viewList;
    }

    public void setViewList(Boolean viewList) {
        this.viewList = viewList;
    }

    public void setProductDetails(ArrayList<Orders> productDetails) {
        this.productDetails = productDetails;
    }
}

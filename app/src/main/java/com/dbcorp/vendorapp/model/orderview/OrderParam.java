package com.dbcorp.vendorapp.model.orderview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Bhupesh Sen on 02-05-2021.
 */
public class OrderParam implements Serializable {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("orders_id")
    @Expose
    private String ordersId;
    @SerializedName("OrderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("payment_mode_id")
    @Expose
    private String paymentModeId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("assign_user_id")
    @Expose
    private Object assignUserId;
    @SerializedName("deliveryBoyName")
    @Expose
    private Object deliveryBoyName;
    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(String paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Object getAssignUserId() {
        return assignUserId;
    }

    public void setAssignUserId(Object assignUserId) {
        this.assignUserId = assignUserId;
    }

    public Object getDeliveryBoyName() {
        return deliveryBoyName;
    }

    public void setDeliveryBoyName(Object deliveryBoyName) {
        this.deliveryBoyName = deliveryBoyName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}

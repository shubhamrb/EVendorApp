package com.dbcorp.vendorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Bhupesh Sen on 07-02-2021.
 */
public class OrderDetails implements Serializable {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("add_date")
    @Expose
    private String addDate;
    @SerializedName("OrderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("statusName")
    @Expose
    private String statusName;
    @SerializedName("statusColor")
    @Expose
    private String statusColor;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }
}

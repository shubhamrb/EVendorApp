package com.dbcorp.vendorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Bhupesh Sen on 21-03-2021.
 */
public class CustomerServiceBooking implements Serializable {

    @SerializedName("services_booked_id")
    @Expose
    private String servicesBookedId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("add_by")
    @Expose
    private String addBy;
    @SerializedName("add_date")
    @Expose
    private String addDate;
    @SerializedName("update_date")
    @Expose
    private String updateDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pincode")
    @Expose
    private Object pincode;
    @SerializedName("house_address")
    @Expose
    private Object houseAddress;
    @SerializedName("street_name")
    @Expose
    private Object streetName;

    public String getServicesBookedId() {
        return servicesBookedId;
    }

    public void setServicesBookedId(String servicesBookedId) {
        this.servicesBookedId = servicesBookedId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getAddBy() {
        return addBy;
    }

    public void setAddBy(String addBy) {
        this.addBy = addBy;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getPincode() {
        return pincode;
    }

    public void setPincode(Object pincode) {
        this.pincode = pincode;
    }

    public Object getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(Object houseAddress) {
        this.houseAddress = houseAddress;
    }

    public Object getStreetName() {
        return streetName;
    }

    public void setStreetName(Object streetName) {
        this.streetName = streetName;
    }

}

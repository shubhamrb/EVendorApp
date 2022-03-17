package com.dbcorp.vendorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Bhupesh Sen on 29-05-2021.
 */
public class ProductRequestModel implements Serializable {
    @SerializedName("product_request_id")
    @Expose
    private String product_request_id;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("photo")
    @Expose
    private String photo;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("is_approve")
    @Expose
    private String is_approve;

    @SerializedName("add_date")
    @Expose
    private String add_date;

    @SerializedName("active")
    @Expose
    private String active;

    public String getProduct_request_id() {
        return product_request_id;
    }

    public void setProduct_request_id(String product_request_id) {
        this.product_request_id = product_request_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIs_approve() {
        return is_approve;
    }

    public void setIs_approve(String is_approve) {
        this.is_approve = is_approve;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}

package com.dbcorp.vendorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bhupesh Sen on 25-04-2021.
 */

public class ServicePackage implements Serializable {

    @SerializedName("ServicePackageDetails")
    @Expose
    private ArrayList<ServicePackageDetail> servicePackageDetails = null;
    @SerializedName("ServicePackagePoints")
    @Expose
    private ArrayList<ServicePackagePoint> servicePackagePoints = null;

    public ArrayList<ServicePackageDetail> getServicePackageDetails() {
        return servicePackageDetails;
    }

    public void setServicePackageDetails(ArrayList<ServicePackageDetail> servicePackageDetails) {
        this.servicePackageDetails = servicePackageDetails;
    }

    public ArrayList<ServicePackagePoint> getServicePackagePoints() {
        return servicePackagePoints;
    }

    public void setServicePackagePoints(ArrayList<ServicePackagePoint> servicePackagePoints) {
        this.servicePackagePoints = servicePackagePoints;
    }

}
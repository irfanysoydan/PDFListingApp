package com.cbu.pdflistingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RetrofitModel implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("dataFile")
    @Expose
    private String dataFile;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("size")
    @Expose
    private int size;

    @SerializedName("createdAt")
    @Expose
    private String cratedAt;

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCratedAt() {
        return cratedAt;
    }

    public void setCratedAt(String cratedAt) {
        this.cratedAt = cratedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

package com.cbu.pdflistingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class PDFModel implements Serializable {
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
    private Date cratedAt;

    @SerializedName("updatedAt")
    @Expose
    private Date updatedAt;

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

    public Date getCratedAt() {
        return cratedAt;
    }

    public void setCratedAt(Date cratedAt) {
        this.cratedAt = cratedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

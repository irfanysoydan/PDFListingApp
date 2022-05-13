package com.cbu.pdflistingapp.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IPDFDataService {

    @GET("files")
    Call<List<IPDFDataService>> getAllPDF();

    @GET("files/{id}")
    Call<IPDFDataService> getPDF(@Path("id") int id);

    @GET("files/download/{id}")
    Call<IPDFDataService> downloadPDF(@Path("id") int id);

    @FormUrlEncoded
    @POST("files")
    Call<IPDFDataService> createPDF(@Body Retrofit model);


    @DELETE("files/{id}")
    Call<IPDFDataService> deletePDF(@Path("id") int id);



}

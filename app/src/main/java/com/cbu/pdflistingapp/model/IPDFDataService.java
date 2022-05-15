package com.cbu.pdflistingapp.model;

import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface IPDFDataService {

    @GET("/")
    Call<List<RetrofitModel>> getAllPDF();

    @GET("{id}")
    Call<RetrofitModel> getPDF(@Path("id") String id);

    /*@Streaming
    @GET("download/{id}")
    Call<ResponseBody> downloadPDF(@Path("id") String id);*/

    @GET
    Call<ResponseBody> downloadPDF(@Url String fileUrl);

    @FormUrlEncoded
    @POST("")
    Call<RetrofitModel> createPDF(@Body Retrofit model);


    @DELETE("{id}")
    Call<RetrofitModel> deletePDF(@Path("id") String id);



}

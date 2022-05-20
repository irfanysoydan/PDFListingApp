package com.cbu.pdflistingapp.network.interfaces;

import com.cbu.pdflistingapp.model.PDFModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface IPDFDataService {

    @GET("files")
    Call<List<PDFModel>> getAllPDF();

    @GET("files/{id}")
    Call<PDFModel> getPDF(@Path("id") String id);

    @GET("files/download/{id}")
    Call<ResponseBody> downloadPDF(@Path("id") String fileUrl);

    @DELETE("files/{id}")
    Call<PDFModel> deletePDF(@Path("id") String id);

    @Multipart
    @POST("files")
    Call<PDFModel> createPDF(@Part MultipartBody.Part file);


}

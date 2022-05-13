package com.cbu.pdflistingapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cbu.pdflistingapp.R;
import com.cbu.pdflistingapp.model.IPDFDataService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private IPDFDataService dataService;
    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit  = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dataService = retrofit.create(IPDFDataService.class);
    }
}
package com.cbu.pdflistingapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.cbu.pdflistingapp.R;
import com.cbu.pdflistingapp.model.IPDFDataService;
import com.cbu.pdflistingapp.model.RetrofitInstance;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IPDFDataService dataService = RetrofitInstance.getRetrofitInstance().create(IPDFDataService.class);
        dataService.downloadPDF("627eb1eeac16b7bd48d0d5f6").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("test", "j");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });



    }
}
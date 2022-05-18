package com.cbu.pdflistingapp.network.concrete.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.cbu.pdflistingapp.model.PDFModel;
import com.cbu.pdflistingapp.network.concrete.RetrofitInstance;
import com.cbu.pdflistingapp.network.interfaces.IPDFDataService;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PDFRepository {
    private String TAG = this.getClass().getName();
    private MutableLiveData<HashMap<String,List<PDFModel>>> mutablePDFListLiveData = new MutableLiveData<>();
    private MutableLiveData<PDFModel> mutablePDFLiveData = new MutableLiveData<>();
    private Application application;

    public PDFRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<HashMap<String,List<PDFModel>>>  getAllPDFMutableLiveData() {
        IPDFDataService pdfDataService = RetrofitInstance.getRetrofitInstance().create(IPDFDataService.class);
        Call<List<PDFModel>> call = pdfDataService.getAllPDF();
        call.enqueue(new Callback<List<PDFModel>>() {
            @Override
            public void onResponse(Call<List<PDFModel>> call, Response<List<PDFModel>> response) {
                HashMap<String,List<PDFModel>> groupList = new HashMap<>();
                List<PDFModel> pdfModelList = response.body();
                groupList.put("A",pdfModelList);
                List<PDFModel> pdfModelList1 = response.body();
                groupList.put("B",pdfModelList1);
                List<PDFModel> pdfModelList2 = response.body();
                groupList.put("C",pdfModelList2);
                List<PDFModel> pdfModelList3 = response.body();
                groupList.put("D",pdfModelList3);
                if (pdfModelList != null) {
                    mutablePDFListLiveData.setValue(groupList);
                }
            }

            @Override
            public void onFailure(Call<List<PDFModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t);
            }
        });
        return mutablePDFListLiveData;
    }

    public MutableLiveData<PDFModel> getByIdPDFMutableLiveData(String id) {
        IPDFDataService pdfDataService = RetrofitInstance.getRetrofitInstance().create(IPDFDataService.class);
        Call<PDFModel> call = pdfDataService.getPDF(id);
        call.enqueue(new Callback<PDFModel>() {
            @Override
            public void onResponse(Call<PDFModel> call, Response<PDFModel> response) {
                PDFModel pdfModelList = response.body();
                if (pdfModelList != null) {
                    mutablePDFLiveData.setValue(pdfModelList);
                }
            }

            @Override
            public void onFailure(Call<PDFModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t);
            }
        });
        return mutablePDFLiveData;
    }

    public MutableLiveData<PDFModel> createPDFMutableLiveData(PDFModel model) {
        IPDFDataService pdfDataService = RetrofitInstance.getRetrofitInstance().create(IPDFDataService.class);
        Call<PDFModel> call = pdfDataService.createPDF(model);
        call.enqueue(new Callback<PDFModel>() {
            @Override
            public void onResponse(Call<PDFModel> call, Response<PDFModel> response) {
                PDFModel pdfModelList = response.body();
                if (pdfModelList != null) {
                    mutablePDFLiveData.setValue(pdfModelList);
                }
            }

            @Override
            public void onFailure(Call<PDFModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t);
            }
        });
        return mutablePDFLiveData;
    }

    public MutableLiveData<PDFModel> deletePDFMutableLiveData(String id) {
        IPDFDataService pdfDataService = RetrofitInstance.getRetrofitInstance().create(IPDFDataService.class);
        Call<PDFModel> call = pdfDataService.deletePDF(id);
        call.enqueue(new Callback<PDFModel>() {
            @Override
            public void onResponse(Call<PDFModel> call, Response<PDFModel> response) {
                PDFModel pdfModelList = response.body();
                if (pdfModelList != null) {
                    mutablePDFLiveData.setValue(pdfModelList);
                }
            }

            @Override
            public void onFailure(Call<PDFModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t);
            }
        });
        return mutablePDFLiveData;
    }

}

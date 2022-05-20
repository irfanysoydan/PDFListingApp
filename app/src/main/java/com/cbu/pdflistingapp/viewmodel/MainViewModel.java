package com.cbu.pdflistingapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cbu.pdflistingapp.model.PDFModel;
import com.cbu.pdflistingapp.network.concrete.repositories.PDFRepository;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainViewModel  extends AndroidViewModel {
    private PDFRepository pdfRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.pdfRepository = new PDFRepository(application);
    }

    public LiveData<HashMap<String,List<PDFModel>>> getAllGroupedPDFs() {
        return pdfRepository.getAllPDFMutableLiveData();
    }

    public LiveData<PDFModel> getByIdPDF(String id) {
        return pdfRepository.getByIdPDFMutableLiveData(id);
    }

    public LiveData<PDFModel> uploadPDF(File file) {
        return pdfRepository.createPDFMutableLiveData(file);
    }

    public LiveData<PDFModel> deletePDF(String id) {
        return pdfRepository.deletePDFMutableLiveData(id);
    }

    public LiveData<ResponseBody> downloadPDF(String id) {
        return  pdfRepository.downloadPDFMutableLiveData(id);
    }

}

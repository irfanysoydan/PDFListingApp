package com.cbu.pdflistingapp.network.concrete.repositories;

import android.app.Application;
import android.os.Build;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.cbu.pdflistingapp.model.PDFModel;
import com.cbu.pdflistingapp.network.concrete.RetrofitInstance;
import com.cbu.pdflistingapp.network.interfaces.IPDFDataService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PDFRepository {
    private String TAG = this.getClass().getName();
    private MutableLiveData<HashMap<String,List<PDFModel>>> mutablePDFListLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseBody> mutableResponseBodyLiveData = new MutableLiveData<>();
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
                List<PDFModel> pdfModelList = response.body();
                if (pdfModelList != null) {
                    Map<String,List<PDFModel>> listMap = new HashMap<>();
                    for (PDFModel model:pdfModelList ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            String pattern = "dd MMMM yyyy";
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                            String date = simpleDateFormat.format(model.getCratedAt());
                            listMap.computeIfAbsent(date.toString(),k -> new ArrayList<>()).add(model);
                            Log.e(TAG, "onResponse: "+date);
                        }
                    }
                    mutablePDFListLiveData.setValue((HashMap<String, List<PDFModel>>) listMap);
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

    public MutableLiveData<PDFModel> createPDFMutableLiveData(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/from-data"),file);
        IPDFDataService pdfDataService = RetrofitInstance.getRetrofitInstance().create(IPDFDataService.class);
        MultipartBody.Part requestFilePart = MultipartBody.Part.createFormData("dataFile",file.getName(),requestFile);
        Call<PDFModel> call = pdfDataService.createPDF(requestFilePart);

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
                Log.e(TAG, "createPDFTESTMutableLiveData onFailure: ",t);
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

    public MutableLiveData<ResponseBody> downloadPDFMutableLiveData(String id){

        IPDFDataService pdfDataService = RetrofitInstance.getRetrofitInstance().create(IPDFDataService.class);
        Call<ResponseBody> call = pdfDataService.downloadPDF(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(),response.headers().get("file_name"));
                            Log.e("asd","Downloaded successfully " + writtenToDisk);

                            return null;
                        }
                    }.execute();
                }
                else{
                    Log.e(TAG, "Server Contact Failed" );
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("asd","Downloaded failed");
            }
        });
        return mutableResponseBodyLiveData;
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String name) {
        try {

            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),name);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
        catch (IOException e) {
            return false;
        }
    }

}

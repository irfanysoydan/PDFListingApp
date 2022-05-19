package com.cbu.pdflistingapp.network.concrete.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.cbu.pdflistingapp.model.PDFModel;
import com.cbu.pdflistingapp.network.concrete.RetrofitInstance;
import com.cbu.pdflistingapp.network.interfaces.IPDFDataService;
import com.cbu.pdflistingapp.view.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import okhttp3.internal.http2.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PDFRepository {
    private String TAG = this.getClass().getName();
    private MutableLiveData<HashMap<String,List<PDFModel>>> mutablePDFListLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseBody> mutableResponseBodyLiveData = new MutableLiveData<>();
    private MutableLiveData<PDFModel> mutablePDFLiveData = new MutableLiveData<>();
    private Application application;
    private IPDFDataService pdfDataService;
    private String pdfName;

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

    public MutableLiveData<ResponseBody> downloadPDFMutableLiveData(String id){

        pdfDataService = RetrofitInstance.getRetrofitInstance().create(IPDFDataService.class);
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

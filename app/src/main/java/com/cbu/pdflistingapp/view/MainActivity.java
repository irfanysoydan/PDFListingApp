package com.cbu.pdflistingapp.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;

import com.cbu.pdflistingapp.R;
import com.cbu.pdflistingapp.databinding.ActivityMainBinding;
import com.cbu.pdflistingapp.model.PDFModel;
import com.cbu.pdflistingapp.utilities.FileUtil;
import com.cbu.pdflistingapp.view.adapter.ExpandableListViewAdapter;
import com.cbu.pdflistingapp.viewmodel.MainViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG = this.getClass().getName();
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private ActivityMainBinding viewBinding;
    private MainViewModel mainViewModel;
    private HashMap<String, List<PDFModel>> pdfModelList;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        checkSelfPermission();
        fillExpandableListView();
        floatingButtonListeners();

    }

    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent intent = result.getData();
                Uri uri = intent.getData();
                uploadFile(new File(FileUtil.getPath(uri,MainActivity.this)));
            }
        }
    });

    private void checkSelfPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    private void setExpandableListView() {
        expandableListView = viewBinding.mainExpandableListView;
        expandableTitleList = new ArrayList<>(pdfModelList.keySet());
        expandableListAdapter = new ExpandableListViewAdapter(this, expandableTitleList, pdfModelList) {
        };
        expandableListView.setAdapter(expandableListAdapter);
    }

    private void expandableListViewListeners() {
        expandableListView.setOnItemLongClickListener((parent, view, position, id) -> {
            PDFModel model = (PDFModel) parent.getItemAtPosition(position);
            if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.detail_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_download:
                            downloadPDF(model.getId());
                            return true;
                        case R.id.menu_delete:
                            deletePDF(model.getId());
                            return true;
                        case R.id.menu_detail:
                            return true;
                        default:
                            return false;
                    }
                });
                popup.show();
                return true;
            }
            return false;
        });
    }

    private void fillExpandableListView() {
        mainViewModel.getAllGroupedPDFs().observe(this, pdfModels -> {
            pdfModelList = pdfModels;
            setExpandableListView();
            expandableListViewListeners();
        });
    }

    private void downloadPDF(String id) {
        mainViewModel.downloadPDF(id).observe(this, responseBody -> {
        });
    }

    private void deletePDF(String id) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        alertBuilder.setMessage("Are you sure you really want to delete the this PDF file?");
        alertBuilder.setCancelable(true);
        alertBuilder.setPositiveButton("Yes, I'm sure.",(dialog, i) -> {
            mainViewModel.deletePDF(id).observe(this, model -> {
                fillExpandableListView();
            });
            dialog.cancel();
        });
        alertBuilder.setNegativeButton("No",(dialog,i) -> dialog.cancel());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void uploadFile(File file) {
        mainViewModel.uploadPDF(file).observe(this, model -> {
            Log.e(TAG, "addPDF: İşlem Başarılı" );
            fillExpandableListView();
        });
    }

    private void floatingButtonListeners() {
        viewBinding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/pdf");
            intent = Intent.createChooser(intent,"Choose PDF");
            sActivityResultLauncher.launch(intent);
        });
    }
}
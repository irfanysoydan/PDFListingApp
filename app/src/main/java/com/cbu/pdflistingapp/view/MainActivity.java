package com.cbu.pdflistingapp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.cbu.pdflistingapp.R;
import com.cbu.pdflistingapp.databinding.ActivityMainBinding;
import com.cbu.pdflistingapp.model.PDFModel;
import com.cbu.pdflistingapp.view.adapter.ExpandableListViewAdapter;
import com.cbu.pdflistingapp.viewmodel.MainViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Are you sure you really want to delete the this PDF file?");
        alertBuilder.setCancelable(true);
        alertBuilder.setPositiveButton("Yes, I'm sure.",(dialog, i) -> {
            mainViewModel.deletePDF(id).observe(this, model -> {
                Toast.makeText(MainActivity.this, model.getName() + " Was deleted", Toast.LENGTH_LONG).show();
                fillExpandableListView();
            });
            dialog.cancel();
        });
        alertBuilder.setNegativeButton("No",(dialog,i) -> dialog.cancel());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void addPDF() {
        mainViewModel.createPDF(null).observe(this, model -> {

        });
    }

    private void floatingButtonListeners() {
        viewBinding.fab.setOnClickListener(view -> {
            // addPDF();
            Toast.makeText(MainActivity.this, "TEST", Toast.LENGTH_LONG).show();
        });
    }

}
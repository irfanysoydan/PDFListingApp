package com.cbu.pdflistingapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.cbu.pdflistingapp.R;
import com.cbu.pdflistingapp.databinding.ActivityMainBinding;
import com.cbu.pdflistingapp.model.PDFModel;
import com.cbu.pdflistingapp.view.adapter.ExpandableListViewAdapter;
import com.cbu.pdflistingapp.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding viewBinding;
    private MainViewModel mainViewModel;
    private HashMap<String, List<PDFModel>> pdfModelList;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableTitleList;
    private HashMap<String, List<String>> expandableDetailList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        getALLPDF();


    }

   private void setExpandableListView(){
        expandableListView = viewBinding.mainExpandableListView;
        expandableTitleList = new ArrayList<>(pdfModelList.keySet());
        expandableListAdapter = new ExpandableListViewAdapter(this, expandableTitleList, pdfModelList) {
        };
        expandableListView.setAdapter(expandableListAdapter);
    }

   private void expandableListViewListeners(){
       expandableListView.setOnGroupExpandListener(groupPosition -> Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show());
       // This method is called when the group is collapsed
       expandableListView.setOnGroupCollapseListener(groupPosition -> Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show());
       expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
           Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition)
                   + " -> "
                   + expandableDetailList.get(
                   expandableTitleList.get(groupPosition)).get(
                   childPosition), Toast.LENGTH_SHORT
           ).show();
           return false;
       });
   }

   private void getALLPDF(){
        mainViewModel.getAllPDF().observe(this, pdfModels -> {
            pdfModelList = pdfModels;
            setExpandableListView();
            expandableListViewListeners();
        });
   }
}
package com.cbu.pdflistingapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.cbu.pdflistingapp.R;
import com.cbu.pdflistingapp.databinding.ActivityMainBinding;
import com.cbu.pdflistingapp.model.PDFModel;
import com.cbu.pdflistingapp.view.adapter.ExpandableListViewAdapter;
import com.cbu.pdflistingapp.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
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

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
       fillExpandableListView();



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
           return false;
       });
       expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               final ExpandableListAdapter adapter = ((ExpandableListView) parent).getExpandableListAdapter();
               long packedPos = ((ExpandableListView) parent).getExpandableListPosition(position);
               int groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
               int childPosition = ExpandableListView.getPackedPositionChild(packedPos);
               Log.e("TAG", "onMenuItemClick: "+groupPosition);
               Log.e("TAG", "onMenuItemClick: "+parent.getItemAtPosition(childPosition));
               if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                   PopupMenu popup = new PopupMenu(MainActivity.this, view);
                   MenuInflater inflater = popup.getMenuInflater();
                   inflater.inflate(R.menu.detail_menu, popup.getMenu());
                   popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                       @Override
                       public boolean onMenuItemClick(MenuItem menuItem) {
                           switch (menuItem.getItemId()) {
                               case R.id.menu_download:

                                   return true;
                               case R.id.menu_delete:

                                   return true;
                               case R.id.menu_detail:

                                   return true;
                               default:
                                   return false;
                           }
                       }
                   });
                   popup.show();
                   return true;
               }

               return false;
           }
       });


   }

   private void fillExpandableListView(){
        mainViewModel.getAllGroupedPDFs().observe(this, pdfModels -> {
            pdfModelList = pdfModels;
            setExpandableListView();
            expandableListViewListeners();
        });
   }

   private void getDownloadPDF(String id){
        mainViewModel.downloadPDF(id).observe(this, responseBody -> {
        });
   }
}
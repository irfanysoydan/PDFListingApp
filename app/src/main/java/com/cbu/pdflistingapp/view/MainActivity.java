package com.cbu.pdflistingapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.cbu.pdflistingapp.R;
import com.cbu.pdflistingapp.view.adapter.ExpandableListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ExpandableListView expandableListViewExample;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableTitleList;
    HashMap<String, List<String>> expandableDetailList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListViewExample = (ExpandableListView) findViewById(R.id.main_expandable_list_view);
        expandableDetailList = ExpandableListDataItems.getData();
        expandableTitleList = new ArrayList<String>(expandableDetailList.keySet());
        expandableListAdapter = new ExpandableListViewAdapter(this, expandableTitleList, expandableDetailList) {
        };
        expandableListViewExample.setAdapter(expandableListAdapter);

        expandableListViewExample.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
            }
        });

        // This method is called when the group is collapsed
        expandableListViewExample.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
            }
        });
        expandableListViewExample.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition)
                        + " -> "
                        + expandableDetailList.get(
                        expandableTitleList.get(groupPosition)).get(
                        childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }

}
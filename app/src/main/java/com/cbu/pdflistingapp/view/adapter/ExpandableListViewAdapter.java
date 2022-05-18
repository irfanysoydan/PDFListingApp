package com.cbu.pdflistingapp.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cbu.pdflistingapp.R;
import com.cbu.pdflistingapp.model.PDFModel;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableTitleList;
    private HashMap<String, List<PDFModel>> pdfModelListHashMap;

    public ExpandableListViewAdapter(Context context, List<String> expandableTitleList, HashMap<String, List<PDFModel>> pdfModelListHashMap) {
        this.context = context;
        this.expandableTitleList = expandableTitleList;
        this.pdfModelListHashMap = pdfModelListHashMap;
    }

    @Override
    public int getGroupCount() {
        return this.expandableTitleList.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return listPosition;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableTitleList.get(listPosition);
    }

    @Override
    public PDFModel getChild(int listPosition, int expandedListPosition) {
        return this.pdfModelListHashMap.get(this.expandableTitleList.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_indicator, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.main_expandable_list_view_group_indicator_TV_date);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = getChild(listPosition, expandedListPosition).getName();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_indicator, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.main_expandable_list_view_chlid_indicator_TV_name);
        expandedListTextView.setText(expandedListText);
        Log.e("Test", expandedListTextView.getText().toString());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}

package com.hydratech19gmail.notify;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jaelse on 16/12/16.
 */

public class SearchResultAdapter extends ArrayAdapter<SearchResult> {
    LayoutInflater inflater;

    SearchResultAdapter.ViewHolder mViewHolder;

    private static final String TAG = "SearchResultAdapter";

    static class ViewHolder {
        public TextView resultName;
    }

    public SearchResultAdapter(Context context, List<SearchResult> data) {
        super(context,R.layout.search_result_row, data);

        inflater = LayoutInflater.from(getContext());
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_result_row,parent,false);

            mViewHolder = new ViewHolder();
            mViewHolder.resultName = (TextView) convertView.findViewById(R.id.result_name);

            convertView.setTag(mViewHolder);


        } else {
            mViewHolder = (SearchResultAdapter.ViewHolder) convertView.getTag();

        }

        SearchResult searchResult = getItem(position);

        mViewHolder.resultName.setText(searchResult.getResultName());


        return convertView;
    }

}

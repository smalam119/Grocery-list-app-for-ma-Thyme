package com.test.smalam.grocerylist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


public class PreviousListsFragment extends Fragment {

    ListView listView;
    Spinner spnr;
    Context context;
    ViewGroup parent;



    public PreviousListsFragment(){}




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_previous_lists, container, false);

        return rootView;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        View view = getView();
        if (view != null) {
            TextView title = (TextView) view.findViewById(R.id.textView_title_previous_list);
            listView = (ListView) view.findViewById(R.id.listView);
            listView.setAdapter(new CustomAdapterForPreviousList(getContext()));
        }
    }




}
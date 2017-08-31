package com.pseudozero.thyme.thyme.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pseudozero.thyme.thyme.R;


public class ArchivedListFragment extends Fragment {

    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_archived_list, container, false);

        return rootView;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        View view = getView();
        if (view != null) {
            listView = (ListView) view.findViewById(R.id.listView);
            listView.setAdapter(new ArchivedListCustomAdapter(getContext()));
        }
    }




}

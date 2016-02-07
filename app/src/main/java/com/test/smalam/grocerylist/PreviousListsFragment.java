package com.test.smalam.grocerylist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


public class PreviousListsFragment extends Fragment implements SearchView.OnQueryTextListener{

    ListView listView;
    SearchView searchBar;
    CustomAdapterForPreviousList adapterForPreviousList;


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
            adapterForPreviousList = new CustomAdapterForPreviousList(getContext());
            TextView title = (TextView) view.findViewById(R.id.textView_title_previous_list);
            listView = (ListView) view.findViewById(R.id.listView);
            listView.setAdapter(adapterForPreviousList);
            searchBar = (SearchView) view.findViewById(R.id.search_view);
            searchBar.setOnQueryTextListener(this);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterForPreviousList.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

}

package com.test.smalam.grocerylist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;


public class PreviousListsFragment extends Fragment implements SearchView.OnQueryTextListener{

    ListView listView;
    SearchView searchBar;
    CustomAdapterForPreviousList adapterForPreviousList;
    RadioGroup searchOptions;
    String typeOfSearch = "all";


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

            listView = (ListView) view.findViewById(R.id.listView);
            adapterForPreviousList = new CustomAdapterForPreviousList(getContext(),typeOfSearch);
            listView.setAdapter(adapterForPreviousList);


            searchOptions = (RadioGroup) view.findViewById(R.id.search_options);
            searchOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.all:
                            adapterForPreviousList = new CustomAdapterForPreviousList(getContext(),"all");
                            listView.setAdapter(adapterForPreviousList);
                            break;
                        case R.id.fav:
                            adapterForPreviousList = new CustomAdapterForPreviousList(getContext(),"favorites");
                            listView.setAdapter(adapterForPreviousList);
                            break;
                        case R.id.note:
                            adapterForPreviousList = new CustomAdapterForPreviousList(getContext(),"notes");
                            listView.setAdapter(adapterForPreviousList);
                            break;
                        case R.id.to_do:
                            adapterForPreviousList = new CustomAdapterForPreviousList(getContext(),"toDo");
                            listView.setAdapter(adapterForPreviousList);
                            break;
                    }
                }
            });


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

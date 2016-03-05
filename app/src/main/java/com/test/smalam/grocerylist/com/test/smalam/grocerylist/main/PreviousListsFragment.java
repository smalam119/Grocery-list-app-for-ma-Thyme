package com.test.smalam.grocerylist.com.test.smalam.grocerylist.main;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;

import com.test.smalam.grocerylist.R;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.settings.Settings;


public class PreviousListsFragment extends Fragment implements SearchView.OnQueryTextListener{

    ListView listView;
    SearchView searchBar;
    CustomAdapterForPreviousList adapterForPreviousList;
    RadioGroup searchOptions;
    String typeOfSearch = "all";
    Settings settings;


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

        settings = new Settings();

        settings.getSetting(PreviousListsFragment.this);

        if (view != null) {

            listView = (ListView) view.findViewById(R.id.listView);
            adapterForPreviousList = new CustomAdapterForPreviousList(getContext(),typeOfSearch);
            listView.setAdapter(adapterForPreviousList);


            searchOptions = (RadioGroup) view.findViewById(R.id.search_options);

            RadioButton all,fav,notes,toDo;

            all = (RadioButton) view.findViewById(R.id.all);
            fav = (RadioButton) view.findViewById(R.id.fav);
            notes = (RadioButton) view.findViewById(R.id.note);
            toDo = (RadioButton) view.findViewById(R.id.to_do);

            all.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), settings.getFont(settings.getFontNumber())));
            fav.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), settings.getFont(settings.getFontNumber())));
            notes.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), settings.getFont(settings.getFontNumber())));
            toDo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), settings.getFont(settings.getFontNumber())));

            all.setChecked(true);


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

                        default:
                            adapterForPreviousList = new CustomAdapterForPreviousList(getContext(),"all");
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

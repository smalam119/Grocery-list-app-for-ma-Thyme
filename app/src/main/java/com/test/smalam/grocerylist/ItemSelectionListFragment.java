package com.test.smalam.grocerylist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class ItemSelectionListFragment extends Fragment implements View.OnClickListener {

    Button button;
    ListView listView;
    ArrayAdapter<String> adapter;
    private ArrayList<String> itemData = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_selection_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        itemData.add("ABCD");
        View view = getView();
        button = (Button) view.findViewById(R.id.finish_btn);
        listView = (ListView) view.findViewById(R.id.list);
       // adapter = new ArrayAdapter<String>(getContext(),
                //android.R.layout.simple_list_item_multiple_choice, itemData);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
                selectedItems.add(adapter.getItem(position));
        }

        String[] outputStrArr = new String[selectedItems.size()];


    }
}

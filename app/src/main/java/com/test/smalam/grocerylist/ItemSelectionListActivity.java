package com.test.smalam.grocerylist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemSelectionListActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;
    ListView listView;
    ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<String>();
    public static final String LIST_ID = "drinkNo";
    private String listId,concataneted;
    private List<String> listOfItems;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selection_list);

        try
        {
            SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
            db = groceryListDatabaseHelper.getReadableDatabase();
        }
        catch(SQLiteException e)
        {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = (Button) findViewById(R.id.finish_btn_activity);
        listView = (ListView) findViewById(R.id.list_activity);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, items);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        button.setOnClickListener(this);
        listId = (String)getIntent().getExtras().get(LIST_ID);

        fetchItemsOfAList();

        for(int i = 0; i < listOfItems.size(); i++)
        {
            items.add(listOfItems.get(i));
        }


    }

    public void fetchItemsOfAList()
    {
        Cursor cursor = db.query("LISTS",
                new String[] {"NAME","ITEMS"},
                "NAME=?",
                new String[] {listId},
                null,null,null);

        if(cursor.moveToFirst())
        {
            concataneted = cursor.getString(1).replaceAll("\\[", "").replaceAll("\\]", "");
            listOfItems = Arrays.asList(concataneted.split(","));
        }
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

        for (int i = 0; i < selectedItems.size(); i++) {
            outputStrArr[i] = selectedItems.get(i);
        }

        ContentValues cv = new ContentValues();
        cv.put("ARCHIVED",1);
        db.update("LISTS",cv,"NAME=?",new String[] {listId});

        Toast.makeText(this,"Shopping Finished and list sent to Archive!",Toast.LENGTH_SHORT).show();

    }
}

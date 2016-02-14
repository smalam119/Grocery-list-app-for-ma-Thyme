package com.test.smalam.grocerylist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToDoViewerActivity extends AppCompatActivity  {

    private ArrayList<String> items = new ArrayList<String>();
    public static final String LIST_ID = "drinkNo";
    private String listId, itemsConcatenated, checkStatusConcatenated, title;
    private List<String> listOfItems;
    private List<String> listOfChecks;
    private SQLiteDatabase db;
    ArrayList<Boolean> isSelectedList = new ArrayList<>();
    Button button;
    ListView listView;
    ArrayAdapter<String> adapter;
    TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_viwer);

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

        titleTv = (TextView) findViewById(R.id.title_to_do);

        listView = (ListView) findViewById(R.id.list_activity);
        listId = String.valueOf((int)getIntent().getExtras().get(LIST_ID));

        fetchItemsOfAList();

        titleTv.setText(title);

        for(int i = 0; i < listOfItems.size(); i++)
        {
            items.add(listOfItems.get(i).trim());
        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, items);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        for(int i = 0; i < listOfChecks.size(); i++)
        {
            if(listOfChecks.get(i).trim().equals("true") )
            {
                listView.setItemChecked(i,true);
            } else if(listOfChecks.get(i).trim().equals("false"))
            {
                listView.setItemChecked(i,false);
            }
        }

        Toast.makeText(this,listOfChecks.toString(),Toast.LENGTH_LONG).show();


    }

    public void fetchItemsOfAList()
    {
        Cursor cursor = db.query("LISTS",
                new String[] {"_id","NAME","ITEMS","CHECK_LIST_STATUS"},
                "_id=?",
                new String[] {listId},
                null,null,null);

        if(cursor.moveToFirst())
        {
            itemsConcatenated = cursor.getString(2).replaceAll("\\[", "").replaceAll("\\]", "");
            checkStatusConcatenated = cursor.getString(3).replaceAll("\\[", "").replaceAll("\\]", "");
            listOfItems = Arrays.asList(itemsConcatenated.split(","));
            listOfChecks = Arrays.asList(checkStatusConcatenated.split(","));
            title = cursor.getString(1);
        }
    }


    /*@Override
    public void onClick(View v)
    {
        for (int i = 0; i < listOfItems.size(); i++)
        {
            isSelectedList.add(listView.isItemChecked(i));
        }


        ContentValues cv = new ContentValues();
        cv.put("CHECK_LIST_STATUS", isSelectedList.toString());
        db.update("LISTS", cv, "_id=?", new String[]{listId});


    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        for (int i = 0; i < listOfItems.size(); i++)
        {
            isSelectedList.add(listView.isItemChecked(i));
        }


        ContentValues cv = new ContentValues();
        cv.put("CHECK_LIST_STATUS", isSelectedList.toString());
        db.update("LISTS", cv, "_id=?", new String[]{listId});
    }
}

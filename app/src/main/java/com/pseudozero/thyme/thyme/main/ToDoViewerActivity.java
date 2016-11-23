package com.pseudozero.thyme.thyme.main;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pseudozero.thyme.thyme.R;
import com.pseudozero.thyme.thyme.database.GroceryListDatabaseHelper;
import com.pseudozero.thyme.thyme.settings.Settings;

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
    ListView listView;
    ArrayAdapter<String> adapter;
    TextView titleTv;;
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_viwer);

        settings = new Settings();
        settings.getSetting(this);

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
        titleTv.setTypeface(Typeface.createFromAsset(getAssets(), settings.getFont(settings.getFontNumber())));
        listView = (ListView) findViewById(R.id.list_activity);
        listId = String.valueOf(getIntent().getExtras().get(LIST_ID));

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

    public void sendToTrash(String s)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ARCHIVED", 1);
        db.update("LISTS", cv, "_id=?", new String[]{s});
    }

    public void favUpdate(String s)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();

        int fav = 0;

        Cursor cursor = db.query("LISTS",
                new String[]{"_id", "FAVORITE"},
                "_id=?",
                new String[]{listId},
                null, null, null);

        if (cursor.moveToFirst()) {
            fav = cursor.getInt(1);
        }

        ContentValues cv = new ContentValues();
        if(fav == 0)
        {
            cv.put("FAVORITE", 1);

            Toast.makeText(this,"Added as favorite",Toast.LENGTH_SHORT).show();
        }
        else if(fav == 1)
        {
            cv.put("FAVORITE", 0);

            Toast.makeText(this,"Removed as favorite",Toast.LENGTH_SHORT).show();
        }
        db.update("LISTS",cv, "_id=?", new String[] {s});
    }



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.to_do_viewer_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:
                favUpdate(listId);
                return true;

            case R.id.action_edit:
                Intent i = new Intent(this,EditToDoActivity.class);
                i.putExtra(EditToDoActivity.LIST_ID,listId);
                startActivity(i);
                finish();
                return true;

            case R.id.action_delete:
                sendToTrash(listId);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.test.smalam.grocerylist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EditListActivity extends AppCompatActivity {

    public static final String LIST_TITLE = "listTitle";
    private String listId,concataneted;
    private List<String> listOfItems;
    private LinearLayout childLayout;
    EditText ed,titleEd;
    List<EditText> allEds = new ArrayList<EditText>();
    private int id=0;
    private String title;
    Button btn, save;
    private ArrayList<String> itemData = new ArrayList<String>();
    private SQLiteDatabase db;
    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try
        {
            SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
            db = groceryListDatabaseHelper.getWritableDatabase();
        }
        catch(SQLiteException e)
        {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        listId = (String)getIntent().getExtras().get(LIST_TITLE);

        fetchItemsOfAList();

        for(int i =0; i < listOfItems.size(); i++ )
        {
            createEditText(listOfItems.get(i));
        }

        titleEd = (EditText) findViewById(R.id.title_e);
        titleEd.setText(title);

        btn = (Button) findViewById(R.id.add_row_e);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditText();
            }
        });

        save = (Button) findViewById(R.id.save_e);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemData.clear();

                for (int i = 0; i < allEds.size(); i++) {
                    String s = allEds.get(i).getText().toString();
                    if (!s.equals("")) {
                        itemData.add(s);
                    }

                    title = titleEd.getText().toString();
                }

                if (title.equals("")) {
                    Toast.makeText(getBaseContext(), "Your list must have a title", Toast.LENGTH_LONG).show();
                } else {
                    updateList(db, currentDateTimeString, title, itemData.toString());
                    Toast.makeText(getBaseContext(), "List Saved", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void fetchItemsOfAList()
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS",
                new String[] {"NAME","ITEMS"},
                "NAME=?",
                new String[] {listId},
                null,null,null);

        if(cursor.moveToFirst())
        {
            concataneted = cursor.getString(1).replaceAll("\\[", "").replaceAll("\\]","");
            listOfItems = Arrays.asList(concataneted.split(","));
            title = cursor.getString(0);

        }
    }

    public void createEditText(String content)
    {

        childLayout = (LinearLayout) findViewById(R.id.child_lay_e);
        ed = new EditText(this);
        allEds.add(ed);
        ed.setId(id);
        ed.setText(content);
        ed.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
        childLayout.addView(ed);
        id++;
    }

    public void createEditText()
    {

        childLayout = (LinearLayout) findViewById(R.id.child_lay_e);
        ed = new EditText(this);
        allEds.add(ed);
        ed.setId(id);
        ed.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
        childLayout.addView(ed);
        id++;
    }

    public void updateList(SQLiteDatabase db,String date, String name, String items)
    {
        ContentValues cv = new ContentValues();
        cv.put("DATE", date);
        cv.put("NAME", name);
        cv.put("ITEMS", items);
        db.update("LISTS",cv, "NAME=?", new String[] {listId});
    }





}

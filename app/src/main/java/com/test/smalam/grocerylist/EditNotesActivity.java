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
import android.widget.Toast;

import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EditNotesActivity extends AppCompatActivity
{
    public static final String LIST_ID = "listID";
    private SQLiteDatabase db;
    EditText note,title;
    Button save;
    String noteText,titleText;
    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    public String listId,fetchedNoteText,fetchedTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listId = String.valueOf((int)getIntent().getExtras().get(LIST_ID));

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

        fetchItemsOfAList();

        note = (EditText) findViewById(R.id.note_body);
        note.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
        note.setText(fetchedNoteText);

        title = (EditText) findViewById(R.id.title_note);
        title.setText(fetchedTitle);

        save = (Button) findViewById(R.id.save_note);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noteText = note.getText().toString();
                titleText = title.getText().toString();

                updateList(db, currentDateTimeString, titleText, noteText);
            }
        });
    }

    public void fetchItemsOfAList() {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS",
                new String[]{"NAME", "ITEMS"},
                "_id=?",
                new String[]{listId},
                null, null, null);

        if (cursor.moveToFirst()) {
            fetchedNoteText = cursor.getString(1).replaceAll("\\[", "").replaceAll("\\]", "").trim();
            fetchedTitle = cursor.getString(0);

        }

    }

    public void updateList(SQLiteDatabase db,String date, String name, String noteText)
    {
        ContentValues cv = new ContentValues();
        cv.put("DATE", date);
        cv.put("NAME", name);
        cv.put("ITEMS",noteText);
        cv.put("FAVORITE", 0);
        db.update("LISTS",cv, "_id=?", new String[] {listId});
    }


    }



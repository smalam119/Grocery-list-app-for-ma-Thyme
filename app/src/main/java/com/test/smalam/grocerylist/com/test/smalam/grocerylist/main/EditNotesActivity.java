package com.test.smalam.grocerylist.com.test.smalam.grocerylist.main;

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
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.test.smalam.grocerylist.R;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.settings.Settings;

import java.text.DateFormat;
import java.util.Date;

public class EditNotesActivity extends AppCompatActivity
{
    public static final String LIST_ID = "listID";
    private SQLiteDatabase db;
    EditText note,title;
    ImageButton save;
    String noteText,titleText;
    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    public String listId,fetchedNoteText,fetchedTitle;
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listId = String.valueOf(getIntent().getExtras().get(LIST_ID));

        settings = new Settings();

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

        settings.getSetting(this);

        fetchItemsOfAList();

        note = (EditText) findViewById(R.id.note_body_e);
        note.setTypeface(Typeface.createFromAsset(getAssets(), settings.getFont(settings.getFontNumber())));
        note.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));
        note.setText(fetchedNoteText);

        title = (EditText) findViewById(R.id.title_note_e);
        title.setTypeface(Typeface.createFromAsset(getAssets(), settings.getFont(settings.getFontNumber())));
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));
        title.setText(fetchedTitle);
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        noteText = note.getText().toString();
        titleText = title.getText().toString();

        if(titleText.isEmpty())
        {
            Toast.makeText(getBaseContext(),"You must give a title",Toast.LENGTH_SHORT).show();
        }
        else
        {
            updateList(db, currentDateTimeString, titleText, noteText);
            Toast.makeText(getBaseContext(),"Note added",Toast.LENGTH_SHORT).show();
        }

        Intent i = new Intent(this,NotesViewerActivity.class);
        i.putExtra(NotesViewerActivity.LIST_ID,listId);
        startActivity(i);
    }
}



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
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.pseudozero.thyme.thyme.R;
import com.pseudozero.thyme.thyme.database.GroceryListDatabaseHelper;
import com.pseudozero.thyme.thyme.settings.SettingsData;
import com.pseudozero.thyme.thyme.settings.SettingsActivity;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.DateFormat;
import java.util.Date;

public class EditNotesActivity extends AppCompatActivity
{
    public static final String LIST_ID = "listID";
    private SQLiteDatabase db;
    EditText note,title;
    String noteText,titleText;
    String currentDateTimeString;
    public String listId,fetchedNoteText,fetchedTitle;
    SettingsData settings;
    private boolean favButtonState;
    ImageButton fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listId = String.valueOf(getIntent().getExtras().get(LIST_ID));

        settings = new SettingsData();

        try
        {
            SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
            db = groceryListDatabaseHelper.getWritableDatabase();
        }
        catch(SQLiteException e)
        {
            TastyToast.makeText(getApplicationContext(), "Database unavailable", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }

        settings.getSetting(this);

        fetchItemsOfAList();

        fav = (ImageButton) findViewById(R.id.fav_button_e);
        fav.setBackgroundResource(R.drawable.unselected_fav_icon);

        if(favButtonState == true)
        {
            fav.setBackgroundResource(R.drawable.option_menu_fav_blue);
        }

        else if(favButtonState == false)
        {
            fav.setBackgroundResource(R.drawable.unselected_fav_icon);
        }

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (favButtonState == false) {

                    fav.setBackgroundResource(R.drawable.option_menu_fav_blue);
                    favButtonState = true;
                    TastyToast.makeText(getBaseContext(), "Added as favorite", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                } else if (favButtonState == true) {

                    fav.setBackgroundResource(R.drawable.unselected_fav_icon);
                    favButtonState = false;
                    TastyToast.makeText(getBaseContext(), "Removed from favorites", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                }
            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();
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
                new String[]{"NAME", "ITEMS","FAVORITE"},
                "_id=?",
                new String[]{listId},
                null, null, null);

        if (cursor.moveToFirst()) {
            fetchedNoteText = cursor.getString(1).replaceAll("\\[", "").replaceAll("\\]", "").trim();
            fetchedTitle = cursor.getString(0);
            int fav = cursor.getInt(2);

            if (fav == 0)
            {
                favButtonState = false;
            }
            else if (fav == 1)
            {
                favButtonState = true;
            }

        }


    }

    public void updateList(SQLiteDatabase db,String date, String name, String noteText)
    {
        ContentValues cv = new ContentValues();
        cv.put("DATE", date);
        cv.put("NAME", name);
        cv.put("ITEMS",noteText);
        if(favButtonState)
        {
            cv.put("FAVORITE", 1);
        }
        else if(!favButtonState)
        {
            cv.put("FAVORITE", 0);
        }
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
            TastyToast.makeText(getApplicationContext(), "You must give a title", TastyToast.LENGTH_LONG, TastyToast.WARNING);
        }
        else
        {
            currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            updateList(db, currentDateTimeString, titleText, noteText);
            TastyToast.makeText(getBaseContext(), "Note Updated", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        }

        Intent i = new Intent(this,NotesViewerActivity.class);
        i.putExtra(NotesViewerActivity.LIST_ID,listId);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.setting:
                Intent i = new Intent(this,SettingsActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



package com.test.smalam.grocerylist.com.test.smalam.grocerylist.main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.test.smalam.grocerylist.R;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.service.ReminderService;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.settings.Settings;

import java.text.DateFormat;
import java.util.Calendar;

public class NotesViewerActivity extends AppCompatActivity
{

    TextView note,title;
    public static final String LIST_ID = "listID";
    public String listId,fetchedNoteText,fetchedTitle;
    private SQLiteDatabase db;
    Settings settings;
    DateFormat fmtDateAndTime= DateFormat.getDateTimeInstance();
    TextView dateAndTimeLabel;
    Calendar dateAndTime=Calendar.getInstance();
    Calendar currentDateAndTime=Calendar.getInstance();
    long dif;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = new Settings();

        listId = String.valueOf(getIntent().getExtras().get(LIST_ID));

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

        settings.getSetting(this);

        note = (TextView) findViewById(R.id.note_body);
        note.setTypeface(Typeface.createFromAsset(getAssets(), settings.getFont(settings.getFontNumber())));
        note.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));
        note.setText(fetchedNoteText);

        title = (TextView) findViewById(R.id.title_note);
        title.setTypeface(Typeface.createFromAsset(getAssets(), settings.getFont(settings.getFontNumber())));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_viewer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(this,EditNotesActivity.class);
                i.putExtra(EditNotesActivity.LIST_ID,listId);
                startActivity(i);
                finish();
                return true;

            case R.id.action_delete:
                sendToTrash(listId);
                finish();
                return true;

            case R.id.action_reminder:
                //startNotificationService();
                new TimePickerDialog(NotesViewerActivity.this,
                        t,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE),
                        true).show();

                new DatePickerDialog(NotesViewerActivity.this,d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH)).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
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

    public void startNotificationService()
    {
        Intent i = new Intent(this, ReminderService.class);
        i.putExtra(ReminderService.EXTRA_MESSAGE,fetchedTitle);
        i.putExtra(ReminderService.LIST_ID, listId);
        i.putExtra(ReminderService.DELAY,""+dif);
        startService(i);
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //updateLabel();
        }
    };
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            updateLabel();
        }
    };

    private void updateLabel()
    {
        //Toast.makeText(this, "" + fmtDateAndTime.format(dateAndTime.getTime()), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "" + fmtDateAndTime.format(currentDateAndTime.getTime()), Toast.LENGTH_SHORT).show();

        dif = dateAndTime.getTimeInMillis()-currentDateAndTime.getTimeInMillis();
        Toast.makeText(this, "" + dif, Toast.LENGTH_SHORT).show();

        startNotificationService();
    }
    }

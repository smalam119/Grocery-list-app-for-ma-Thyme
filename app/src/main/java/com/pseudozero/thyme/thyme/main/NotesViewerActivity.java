package com.pseudozero.thyme.thyme.main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import com.pseudozero.thyme.thyme.R;
import com.pseudozero.thyme.thyme.database.GroceryListDatabaseHelper;
import com.pseudozero.thyme.thyme.service.ReminderService;
import com.pseudozero.thyme.thyme.settings.SettingsData;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.DateFormat;
import java.util.Calendar;

public class NotesViewerActivity extends AppCompatActivity
{

    TextView note,title;
    public static final String LIST_ID = "listID";
    public String listId,fetchedNoteText,fetchedTitle;
    private SQLiteDatabase db;
    SettingsData settings;
    DateFormat fmtDateAndTime= DateFormat.getDateTimeInstance();
    TextView dateAndTimeLabel;
    Calendar dateAndTime=Calendar.getInstance();
    Calendar currentDateAndTime=Calendar.getInstance();
    long dif;
    boolean isAlarmed;
    String alarmDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = new SettingsData();

        isAlarmed = false;

        listId = String.valueOf(getIntent().getExtras().get(LIST_ID));

        try
        {
            SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
            db = groceryListDatabaseHelper.getWritableDatabase();
        }
        catch(SQLiteException e)
        {
            TastyToast.makeText(this, "Database unavailable", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }

        fetchItemsOfAList();

        settings.getSetting(this);

        note = (TextView) findViewById(R.id.note_body);
        note.setTypeface(Typeface.createFromAsset(getAssets(), settings.getFont(settings.getFontNumber())));
        note.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));
        note.setText(fetchedNoteText);

        title = (TextView) findViewById(R.id.title_to_do);
        title.setTypeface(Typeface.createFromAsset(getAssets(), settings.getFont(settings.getFontNumber())));
        title.setText(fetchedTitle);
    }

    public void fetchItemsOfAList() {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS",
                new String[]{"NAME", "ITEMS","IS_ALARMED","DATE_ALARM"},
                "_id=?",
                new String[]{listId},
                null, null, null);

        if (cursor.moveToFirst()) {
            fetchedNoteText = cursor.getString(1).replaceAll("\\[", "").replaceAll("\\]", "").trim();
            fetchedTitle = cursor.getString(0);
            if(cursor.getInt(2) == 1)
            {
                isAlarmed = true;
            }
            else
            {
                isAlarmed = false;
            }

            alarmDate = cursor.getString(3);

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
                if(!isAlarmed)
                {
                    new TimePickerDialog(NotesViewerActivity.this,
                            t,
                            dateAndTime.get(Calendar.HOUR_OF_DAY),
                            dateAndTime.get(Calendar.MINUTE),
                            true).show();

                    new DatePickerDialog(NotesViewerActivity.this,d,
                            dateAndTime.get(Calendar.YEAR),
                            dateAndTime.get(Calendar.MONTH),
                            dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
                }
                else
                {
                    TastyToast.makeText(this, "Alarm is set to "+alarmDate, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }


                return true;

            case R.id.action_favorite:
                favUpdate(listId);
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
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();

        dif = dateAndTime.getTimeInMillis()-currentDateAndTime.getTimeInMillis();
        //Toast.makeText(this, "" + dif, Toast.LENGTH_SHORT).show();
        ContentValues cv = new ContentValues();
        cv.put("IS_ALARMED", 1);
        cv.put("DATE_ALARM",dateAndTime.getTime()+"");
        TastyToast.makeText(this,"Reminder Set to " +dateAndTime.getTime(),TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        db.update("LISTS", cv, "_id=?", new String[]{listId});
        startNotificationService();
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

            TastyToast.makeText(this, "Added as favorite", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        }
        else if(fav == 1)
        {
            cv.put("FAVORITE", 0);

            TastyToast.makeText(this, "Removed from favorites", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        }


        db.update("LISTS", cv, "_id=?", new String[]{s});
    }

    }

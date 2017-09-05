package com.pseudozero.thyme.thyme.main;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import com.pseudozero.thyme.thyme.R;
import com.pseudozero.thyme.thyme.database.GroceryListDatabaseHelper;
import com.pseudozero.thyme.thyme.settings.SettingsData;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.DateFormat;
import java.util.Date;


public class CreateNoteFragment extends Fragment
{
    private SQLiteDatabase db;
    EditText note,title;
    ImageButton save,fav;
    String noteText,titleText;
    String currentDateTimeString;
    SettingsData settings;
    public boolean isSaved = false;
    int latestId;
    private boolean favButtonState;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_take_anote, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        try
        {
            SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(getContext());
            db = groceryListDatabaseHelper.getWritableDatabase();
        }
        catch(SQLiteException e)
        {
            TastyToast.makeText(getContext(), "Database unavailable", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }

        View v = getView();

        favButtonState = false;
        fav = (ImageButton) v.findViewById(R.id.fav_button);
        fav.setBackgroundResource(R.drawable.unselected_fav_icon);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (favButtonState == false) {

                    fav.setBackgroundResource(R.drawable.option_menu_fav_blue);
                    favButtonState = true;
                    TastyToast.makeText(getContext(), "Added as favorite", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                } else if (favButtonState == true) {

                    fav.setBackgroundResource(R.drawable.unselected_fav_icon);
                    favButtonState = false;
                    TastyToast.makeText(getContext(), "Removed from favorite", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                }
            }
        });

        settings = new SettingsData();
        settings.getSetting(CreateNoteFragment.this);

        note = (EditText) v.findViewById(R.id.note_body);
        note.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), settings.getFont(settings.getFontNumber())));
        note.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));

        title = (EditText) v.findViewById(R.id.title_to_do);
        title.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), settings.getFont(settings.getFontNumber())));
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));

        save = (ImageButton) v.findViewById(R.id.save_note);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noteText = note.getText().toString().trim();
                titleText = title.getText().toString();


                if(titleText.isEmpty())
                {
                    TastyToast.makeText(getContext(), "You must have a title", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                }
                else
                {
                    if(!isSaved)
                    {
                        currentDateTimeString=DateFormat.getDateTimeInstance().format(new Date());
                        insertList(db, currentDateTimeString,titleText,noteText);
                        isSaved = true;
                    }
                    else
                    {
                        updateList(db, currentDateTimeString, titleText, noteText);
                    }
                    TastyToast.makeText(getContext(), "Note added", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                }
            }
        });
    }

    public void insertList(SQLiteDatabase db,String date, String name, String noteText)
    {
        ContentValues cv = new ContentValues();
        cv.put("DATE",date);
        cv.put("NAME", name);
        cv.put("ITEMS",noteText);
        cv.put("ARCHIVED",0);
        cv.put("CHECK_LIST_STATUS", "");
        cv.put("IS_TO_DO_LIST",0);
        cv.put("DATE_ALARM","");
        if(favButtonState)
        {
            cv.put("FAVORITE", 1);
        }
        else if(!favButtonState)
        {
            cv.put("FAVORITE", 0);
        }
        db.insert("LISTS", null, cv);
    }

    public void updateList(SQLiteDatabase db,String date, String name, String items)
    {
        ContentValues cv = new ContentValues();
        cv.put("DATE", date);
        cv.put("NAME", name);
        cv.put("ITEMS", noteText);
        if(favButtonState)
        {
            cv.put("FAVORITE", 1);
        }
        else if(!favButtonState)
        {
            cv.put("FAVORITE", 0);
        }
        db.update("LISTS", cv, "_id=?", new String[]{getLatestId() + ""});
    }

    public int getLatestId()
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(getContext());
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS",
                new String[]{"_id"},
                "NAME=?",
                new String[]{title.getText().toString()},
                null, null, null);

        if (cursor.moveToFirst())
        {
            latestId = cursor.getInt(0);
        }

        return latestId;
    }

}

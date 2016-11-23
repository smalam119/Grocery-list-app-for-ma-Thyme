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
import android.widget.Toast;
import com.pseudozero.thyme.thyme.R;
import com.pseudozero.thyme.thyme.database.GroceryListDatabaseHelper;
import com.pseudozero.thyme.thyme.settings.Settings;
import java.text.DateFormat;
import java.util.Date;


public class CreateTakeANoteFragment extends Fragment
{
    private SQLiteDatabase db;
    EditText note,title;
    ImageButton save,fav;
    String noteText,titleText;
    String currentDateTimeString;
    Settings settings;
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
            Toast toast = Toast.makeText(getContext(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
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
                } else if (favButtonState == true) {

                    fav.setBackgroundResource(R.drawable.unselected_fav_icon);
                    favButtonState = false;
                }
            }
        });

        settings = new Settings();
        settings.getSetting(CreateTakeANoteFragment.this);

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
                    Toast.makeText(getContext(),"You must give a title",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(),"Note added",Toast.LENGTH_SHORT).show();
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

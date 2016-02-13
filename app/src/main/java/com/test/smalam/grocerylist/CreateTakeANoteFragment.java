package com.test.smalam.grocerylist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;

import java.text.DateFormat;
import java.util.Date;


public class CreateTakeANoteFragment extends Fragment
{
    private SQLiteDatabase db;
    EditText note,title;
    Button save;
    String noteText,titleText;
    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        note = (EditText) v.findViewById(R.id.note_body);
        //note.setBackgroundResource(R.drawable.apptheme_textfield_disabled_holo_light);

        title = (EditText) v.findViewById(R.id.title_note);

        save = (Button) v.findViewById(R.id.save_note);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noteText = note.getText().toString().trim();
                titleText = title.getText().toString();


                if(titleText.isEmpty())
                {
                    Toast.makeText(getContext(),"You must have give a title",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    insertList(db, currentDateTimeString,titleText,noteText);
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
        cv.put("FAVORITE", 0);
        db.insert("LISTS", null, cv);
    }

}

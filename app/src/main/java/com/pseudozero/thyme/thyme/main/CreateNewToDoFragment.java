package com.pseudozero.thyme.thyme.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.pseudozero.thyme.thyme.R;
import com.pseudozero.thyme.thyme.database.GroceryListDatabaseHelper;
import com.pseudozero.thyme.thyme.settings.Settings;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CreateNewToDoFragment extends Fragment {

    public int id=1;
    private LinearLayout childLayout;
    private ArrayList<String> itemData = new ArrayList<String>();
    private ArrayList<String> checks = new ArrayList<String>();
    private SQLiteDatabase db;
    private String title,firstEdValue;
    private boolean favButtonState;
    List<EditText> allEds = new ArrayList<EditText>();
    String currentDateTimeString;
    ImageButton save,btn,fav;
    EditText ed,titleEd,firstEd;
    int fontNumber,fontColorNumber;
    int latestId;
    public Settings settings;
    public boolean isSaved = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_new_list, container, false);


        return rootView;
    }


    public void createEditText()
    {
        View view = getView();
        childLayout = (LinearLayout)view.findViewById(R.id.child_lay);
        ed = new EditText(getContext());
        ed.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), settings.getFont(settings.getFontNumber())));
        ed.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));
        ed.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        allEds.add(ed);
        ed.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        ed.setHint("" + id + ".");
        childLayout.addView(ed);
        ed.requestFocus();
        id++;
    }

    public void insertList(SQLiteDatabase db,String date, String name, String items)
    {
        ContentValues cv = new ContentValues();
        cv.put("DATE", date);
        cv.put("NAME", name);
        cv.put("ITEMS", items);
        cv.put("ARCHIVED", 0);
        cv.put("CHECK_LIST_STATUS", checks.toString());
        cv.put("IS_TO_DO_LIST", 1);
        cv.put("IS_ALARMED",0);
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
        cv.put("ITEMS", items);
        if(favButtonState)
        {
            cv.put("FAVORITE", 1);
        }
        else if(!favButtonState)
        {
            cv.put("FAVORITE", 0);
        }
        db.update("LISTS", cv, "_id=?", new String[]{getLatestId()+""});
    }

    public int getLatestId()
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(getContext());
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS",
                new String[]{"_id"},
                "NAME=?",
                new String[]{titleEd.getText().toString()},
                null, null, null);

        if (cursor.moveToFirst())
        {
            latestId = cursor.getInt(0);
        }

        return latestId;
    }

    public void getSetting()
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(getContext());
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("SETTINGS",
                new String[]{"FONT_FAMILY", "FONT_COLOR"},
                "_id=?",
                new String[]{"1"},
                null, null, null);

        if (cursor.moveToFirst())
        {
            fontNumber = cursor.getInt(0);
            fontColorNumber = cursor.getInt(1);
        }
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

        settings = new Settings();
        settings.getSetting(CreateNewToDoFragment.this);

        View view = getView();

        createEditText();

        //firstEd = (EditText) view.findViewById(R.id.first_ed);
        //firstEd.setHint("1.");
        //firstEd.requestFocus();
        //firstEd.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), settings.getFont(settings.getFontNumber())));
        //firstEd.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                //getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));
        //allEds.add(firstEd);

        favButtonState = false;
        fav = (ImageButton) view.findViewById(R.id.fav_button);
        fav.setBackgroundResource(R.drawable.unselected_fav_icon);

        titleEd = (EditText) view.findViewById(R.id.title_to_do);
        titleEd.requestFocus();
        titleEd.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),settings.getFont(settings.getFontNumber())));
        titleEd.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));
        btn = (ImageButton) view.findViewById(R.id.add_row);

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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                createEditText();
                if(id == 6)
                {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        save = (ImageButton) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemData.clear();
                String s = "";

                for (int i = 0; i < allEds.size(); i++) {
                    s = allEds.get(i).getText().toString();
                    if (!s.equals("")) {
                        itemData.add(s);
                        checks.add("false");
                    }

                    title = titleEd.getText().toString();
                    //firstEdValue = firstEd.getText().toString();

                }

                if (title.isEmpty() || s.isEmpty())
                {
                    Toast.makeText(getContext(), "Your list must have a title and at least an item", Toast.LENGTH_LONG).show();
                }
                else {
                    if(!isSaved)
                    {
                        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        insertList(db, currentDateTimeString, title, itemData.toString());
                        Toast.makeText(getContext(), "List Saved", Toast.LENGTH_LONG).show();
                        isSaved = true;
                    }
                    else
                    {
                        updateList(db, currentDateTimeString, title, itemData.toString());
                        Toast.makeText(getContext(), "List Saved", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }


}
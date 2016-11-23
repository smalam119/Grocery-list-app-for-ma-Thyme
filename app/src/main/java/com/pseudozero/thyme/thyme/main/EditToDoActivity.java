package com.pseudozero.thyme.thyme.main;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EditToDoActivity extends AppCompatActivity {

    public static final String LIST_ID = "listID";
    private String listId, concatenated;
    private List<String> listOfItems;
    private LinearLayout childLayout;
    private String title;
    private ArrayList<String> itemData = new ArrayList<String>();
    private SQLiteDatabase db;
    private boolean favButtonState;
    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    List<EditText> allEds = new ArrayList<EditText>();
    ImageButton btn,fav,iv;
    EditText ed,titleEd;
    private int id = 1;
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = new Settings();
        settings.getSetting(this);

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

        listId = String.valueOf(getIntent().getExtras().get(LIST_ID));
        iv = (ImageButton) findViewById(R.id.fav_button_e);

        fetchItemsOfAList(listOfItems);

        if(favButtonState == true)
        {
            iv.setBackgroundResource(R.drawable.option_menu_fav_blue);
        }

        else if(favButtonState == false)
        {
            iv.setBackgroundResource(R.drawable.unselected_fav_icon);
        }




        titleEd = (EditText) findViewById(R.id.title_to_do_e);
        titleEd.setText(title);
        titleEd.setTypeface(Typeface.createFromAsset(getAssets(), settings.getFont(settings.getFontNumber())));
        titleEd.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));

        btn = (ImageButton) findViewById(R.id.add_row_e);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditText();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        fav = (ImageButton) findViewById(R.id.fav_button_e);
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

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        itemData.clear();

        for (int i = 0; i < allEds.size(); i++) {
            String s = allEds.get(i).getText().toString();
            if (!s.equals("")) {
                itemData.add(s);
            }

            title = titleEd.getText().toString();
        }

        if (title.isEmpty())
        {
            Toast.makeText(getBaseContext(), "Your list must have a title", Toast.LENGTH_LONG).show();
        } else
        {
            currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            updateList(db, currentDateTimeString, title, itemData.toString());
            Toast.makeText(getBaseContext(), "List Saved", Toast.LENGTH_LONG).show();
        }

        Intent i = new Intent(this,ToDoViewerActivity.class);
        i.putExtra(ToDoViewerActivity.LIST_ID,listId);
        startActivity(i);

    }

    public void fetchItemsOfAList(List<String> list) {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS",
                new String[]{"NAME", "ITEMS", "FAVORITE"},
                "_id=?",
                new String[]{listId},
                null, null, null);

        if (cursor.moveToFirst()) {
            int fav = cursor.getInt(2);
            concatenated = cursor.getString(1).replaceAll("\\[", "").replaceAll("\\]", "").trim();
            list = Arrays.asList(concatenated.split(","));
            title = cursor.getString(0);

            if (fav == 0)
            {
                favButtonState = false;
            }
            else if (fav == 1)
            {
                favButtonState = true;
            }
        }

        for(int i =0; i < list.size(); i++ )
        {
            createEditText(list.get(i).trim());
            id++;
        }

    }



    public void createEditText(String content)
    {

        childLayout = (LinearLayout) findViewById(R.id.child_lay_e);
        ed = new EditText(this);
        ed.setTypeface(Typeface.createFromAsset(getAssets(), settings.getFont(settings.getFontNumber())));
        ed.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));
        ed.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        allEds.add(ed);
        ed.setText(content);
        childLayout.addView(ed);
        ed.requestFocus();
    }

    public void createEditText()
    {

        childLayout = (LinearLayout) findViewById(R.id.child_lay_e);
        ed = new EditText(this);
        ed.setTypeface(Typeface.createFromAsset(getAssets(), settings.getFont(settings.getFontNumber())));
        ed.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(settings.getFontSize(settings.getFontSizeNumber())));
        ed.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        allEds.add(ed);
        ed.setHint(""+id+".");
        childLayout.addView(ed);
        id++;
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
        db.update("LISTS", cv, "_id=?", new String[]{listId});
    }


}

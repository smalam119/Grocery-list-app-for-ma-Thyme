package com.test.smalam.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CreateNewToDoFragment extends Fragment {

    public int id=2;
    private LinearLayout childLayout;
    private ArrayList<String> itemData = new ArrayList<String>();
    private ArrayList<String> checks = new ArrayList<String>();
    private SQLiteDatabase db;
    private String title,firstEdValue;
    private boolean favButtonState;
    List<EditText> allEds = new ArrayList<EditText>();
    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    ImageButton save,btn,fav;
    EditText ed,titleEd,firstEd;


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
        ed.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        allEds.add(ed);
        ed.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        ed.setHint("" + id + ".");
        //ed.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
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

        View view = getView();

        firstEd = (EditText) view.findViewById(R.id.first_ed);
        firstEd.setHint("1.");
        firstEd.requestFocus();
        allEds.add(firstEd);

                favButtonState = false;
        fav = (ImageButton) view.findViewById(R.id.fav_button);
        fav.setBackgroundResource(R.drawable.unselected_fav_icon);

        titleEd = (EditText) view.findViewById(R.id.title_note);
        btn = (ImageButton) view.findViewById(R.id.add_row);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (favButtonState == false) {

                    fav.setBackgroundResource(R.drawable.fav_icon);
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


                for (int i = 0; i < allEds.size(); i++) {
                    String s = allEds.get(i).getText().toString();
                    if (!s.equals("")) {
                        itemData.add(s);
                        checks.add("false");
                    }

                    title = titleEd.getText().toString();
                    firstEdValue = firstEd.getText().toString();

                }

                if (title.isEmpty() || firstEdValue.isEmpty()) {
                    Toast.makeText(getContext(), "Your list must have a title and at least an item", Toast.LENGTH_LONG).show();
                } else {
                    insertList(db, currentDateTimeString, title, itemData.toString());
                    Toast.makeText(getContext(), "List Saved", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


}

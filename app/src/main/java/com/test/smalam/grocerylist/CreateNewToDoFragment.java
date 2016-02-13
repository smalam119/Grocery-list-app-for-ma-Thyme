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
import android.widget.LinearLayout;
import android.widget.Toast;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CreateNewToDoFragment extends Fragment {

    private int id=0;
    private LinearLayout childLayout;
    private ArrayList<String> itemData = new ArrayList<String>();
    private ArrayList<String> checks = new ArrayList<String>();
    private SQLiteDatabase db;
    private String title;
    private boolean favButtonState;
    List<EditText> allEds = new ArrayList<EditText>();
    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    Button btn,save,fav;
    EditText ed,titleEd;

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
        allEds.add(ed);
        ed.setId(id);
        ed.setHint(R.string.hint);
        ed.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
        childLayout.addView(ed);
        ed.requestFocus();
        id++;
    }

    public void insertList(SQLiteDatabase db,String date, String name, String items)
    {
        ContentValues cv = new ContentValues();
        cv.put("DATE",date);
        cv.put("NAME",name);
        cv.put("ITEMS",items);
        cv.put("ARCHIVED",0);
        cv.put("CHECK_LIST_STATUS",checks.toString());
        cv.put("IS_TO_DO_LIST",1);
        if(favButtonState)
        {
            cv.put("FAVORITE", 1);
        }
        else if(!favButtonState)
        {
            cv.put("FAVORITE", 0);
        }

        db.insert("LISTS",null,cv);
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

        favButtonState = false;
        fav = (Button) view.findViewById(R.id.fav_button);
        fav.setBackgroundResource(R.drawable.unselected_fav_icon);

        titleEd = (EditText) view.findViewById(R.id.title);
        btn = (Button) view.findViewById(R.id.add_row);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(favButtonState == false){

                    fav.setBackgroundResource(R.drawable.fav_icon);
                    favButtonState = true;
                }

                else if(favButtonState == true){

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
            }
        });

        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                itemData.clear();

                for (int i = 0; i < allEds.size(); i++) {
                    String s = allEds.get(i).getText().toString();
                    if(!s.equals("")) {
                        itemData.add(s);
                        checks.add("false");
                    }

                    title = titleEd.getText().toString();
                }

                if(title.isEmpty())
                {
                    Toast.makeText(getContext(),"Your list must have a title",Toast.LENGTH_LONG).show();
                }
                else
                {
                    insertList(db, currentDateTimeString, title, itemData.toString());
                    Toast.makeText(getContext(), "List Saved", Toast.LENGTH_LONG).show();
                }

            }
        });


    }




}

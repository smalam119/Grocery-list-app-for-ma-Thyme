package com.test.smalam.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by SAYED on 1/13/2016.
 */
public class CustomAdapterForArchivedList extends BaseAdapter {
    Context context;
    ArrayList<SingleRow> a;
    final String[] spinnerOptions = {
            "",
            "Delete",
            "Restore",
    };



    CustomAdapterForArchivedList(Context c) {
        context = c;
        a = new ArrayList<SingleRow>();
        final SingleRow temp;

        readAllArchivedLists();
    }


    public void readAllArchivedLists()
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS", new String[] {"_id","NAME","DATE","IS_TO_DO_LIST"},"ARCHIVED = ?", new String[] {"1"},null,null,null);

        while (cursor.moveToNext())
        {
            if(cursor.getInt(3) == 1)
            {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_list_icon_black, 0));
            }

            else if(cursor.getInt(3) == 0)
            {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.note_trashed, 0));
            }

        }

    }

    public void deleteList(SingleRow s)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        db.delete("LISTS",
                "_id=?",
                new String[]{String.valueOf(s.getId())});
    }

    public void sendToPreviousList(SingleRow s)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ARCHIVED", 0);
        db.update("LISTS", cv, "_id=?", new String[]{String.valueOf(s.getId())});
    }

    @Override
    public int getCount() {
        return a.size();
    }

    @Override
    public Object getItem(int position) {
        return a.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.single_row, parent, false);

        TextView tvTitle = (TextView) rowView.findViewById(R.id.textView_title_single_row);
        TextView  tvDate = (TextView) rowView.findViewById(R.id.textView_date);
        ImageView iv = (ImageView) rowView.findViewById(R.id.imageView1);
        ImageView s = (ImageView) rowView.findViewById(R.id.option_menu_spinner);
        final SingleRow temp = a.get(position);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v,temp);
            }
        });

        tvTitle.setText(temp.title);
        tvDate.setText(temp.date);
        iv.setImageResource(temp.imageResource);

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showMenu(v, temp);
                return true;
            }
        });


        /*final Spinner spnr = (Spinner) rowView.findViewById(R.id.option_menu_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerOptions);
        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spnr.getSelectedItemPosition();
                        // TODO Auto-generated method stub
                        if (position == 1)
                        {
                            a.remove(temp);
                            CustomAdapterForArchivedList.this.notifyDataSetChanged();
                            deleteList(temp);
                            Toast.makeText(context, "List Deleted", Toast.LENGTH_LONG).show();
                        }

                        if(position == 2)
                        {
                            a.remove(temp);
                            CustomAdapterForArchivedList.this.notifyDataSetChanged();
                            sendToPreviousList(temp);
                            Toast.makeText(context, "Restored", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                }
        );*/

        return rowView;
    }

    public void showMenu(View v, final SingleRow t) {
        PopupMenu popup = new PopupMenu(context, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.delete_a:
                        a.remove(t);
                        CustomAdapterForArchivedList.this.notifyDataSetChanged();
                        deleteList(t);
                        Toast.makeText(context, "List Deleted", Toast.LENGTH_LONG).show();
                        return true;

                    case R.id.restore_a:
                        a.remove(t);
                        CustomAdapterForArchivedList.this.notifyDataSetChanged();
                        sendToPreviousList(t);
                        Toast.makeText(context, "Restored", Toast.LENGTH_LONG).show();
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.archived_list_option_menu);
        popup.show();
    }


}

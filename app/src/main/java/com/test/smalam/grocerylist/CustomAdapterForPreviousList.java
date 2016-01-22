package com.test.smalam.grocerylist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by SAYED on 1/13/2016.
 */
public class CustomAdapterForPreviousList extends BaseAdapter {
    Context context;
    ArrayList<SingleRow> a;
    final String[] spinnerOptions = {
            "",
            "Edit",
            "Delete",
            "Send"
    };

    CustomAdapterForPreviousList(Context c) {
        context = c;
        a = new ArrayList<SingleRow>();
        final SingleRow temp;

        readAllLists();
    }

    public void readAllLists()
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS", new String[]{"_id", "NAME", "DATE", "FAVORITE"}, "ARCHIVED = ?", new String[]{"0"}, null, null, null);

        while (cursor.moveToNext()){

            int favorite = cursor.getInt(3);

            if(favorite == 1)
            {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2), spinnerOptions[cursor.getCount()],R.drawable.fav_icon));
            }
            else if(favorite == 0)
            {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2), spinnerOptions[cursor.getCount()],R.drawable.previous_list_icon));
            }
        }

    }


    public void deleteList(SingleRow s)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        db.delete("LISTS",
                "NAME=?",
                new String[] {s.title});
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

        final View rowView = inflater.inflate(R.layout.single_row, parent ,false);

        TextView tvTitle = (TextView) rowView.findViewById(R.id.textView_title_single_row);
        TextView  tvDate = (TextView) rowView.findViewById(R.id.textView_date);
        ImageView iv = (ImageView) rowView.findViewById(R.id.imageView1);
        final SingleRow temp = a.get(position);

        tvTitle.setText(temp.title);
        tvDate.setText(temp.date);
        iv.setImageResource(temp.imageResource);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context,ItemSelectionListActivity.class);
                intent.putExtra(ItemSelectionListActivity.LIST_ID, temp.id);
                context.startActivity(intent);
            }
        });

        final Spinner spnr = (Spinner) rowView.findViewById(R.id.option_menu_spinner);
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
                        if(position == 1)
                        {
                            Intent intent = new Intent(context,EditListActivity.class);
                            intent.putExtra(EditListActivity.LIST_ID, temp.id);
                            context.startActivity(intent);
                        }
                        if (position == 2)
                        {
                            a.remove(temp);
                            CustomAdapterForPreviousList.this.notifyDataSetChanged();
                            deleteList(temp);
                            Toast.makeText(context, "List Deleted", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                }
        );

        return rowView;
    }

}

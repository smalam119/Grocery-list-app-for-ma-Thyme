package com.pseudozero.thyme.thyme.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.pseudozero.thyme.thyme.R;
import com.pseudozero.thyme.thyme.database.GroceryListDatabaseHelper;
import com.pseudozero.thyme.thyme.settings.SettingsData;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

/**
 * Created by SAYED on 1/13/2016.
 */
public class ArchivedListCustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<SingleRowToDoList> a;
    SettingsData settings;



    ArchivedListCustomAdapter(Context c) {
        context = c;
        a = new ArrayList<SingleRowToDoList>();
        settings = new SettingsData();
        settings.getSetting(context);

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
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_list_icon_black, 0,R.drawable.option_menu_fav_blue,R.color.white));
            }

            else if(cursor.getInt(3) == 0)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_note_icon_black, 0,R.drawable.option_menu_fav_blue,R.color.white));
            }

        }

    }

    public void deleteList(SingleRowToDoList s)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        db.delete("LISTS",
                "_id=?",
                new String[]{String.valueOf(s.getId())});
    }

    public void deleteAll()
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        db.delete("LISTS",
                "ARCHIVED=?",
                new String[]{"1"});
    }

    public void sendToPreviousList(SingleRowToDoList s)
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
        ImageView isFav = (ImageView) rowView.findViewById(R.id.is_fav);
        final SingleRowToDoList temp = a.get(position);
        isFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v, temp);
            }
        });

        tvTitle.setText(temp.title);
        tvTitle.setTypeface(Typeface.createFromAsset(context.getAssets(), settings.getFont(settings.getFontNumber())));
        tvDate.setText(temp.date);
        tvDate.setTypeface(Typeface.createFromAsset(context.getAssets(), settings.getFont(settings.getFontNumber())));
        iv.setImageResource(temp.imageResource);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v, temp);
                //return true;
            }
        });

        return rowView;
    }

    public void showMenu(View v, final SingleRowToDoList t) {
        PopupMenu popup = new PopupMenu(context, v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.delete_a:
                        a.remove(t);
                        ArchivedListCustomAdapter.this.notifyDataSetChanged();
                        deleteList(t);
                        TastyToast.makeText(context, "Item Deleted", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        return true;

                    case R.id.delete_all:
                        a.clear();
                        ArchivedListCustomAdapter.this.notifyDataSetChanged();
                        deleteAll();
                        TastyToast.makeText(context, "All Items Are Deleted", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        return true;

                    case R.id.restore_a:
                        a.remove(t);
                        ArchivedListCustomAdapter.this.notifyDataSetChanged();
                        sendToPreviousList(t);
                        TastyToast.makeText(context, "Item Restored", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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

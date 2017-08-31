package com.pseudozero.thyme.thyme.main;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.pseudozero.thyme.thyme.R;
import com.pseudozero.thyme.thyme.database.GroceryListDatabaseHelper;
import com.pseudozero.thyme.thyme.settings.SettingsData;
import java.util.ArrayList;


/**
 * Created by SAYED on 1/13/2016.
 */
public class PreviousListCustomAdapter extends BaseAdapter
{
    Context context;
    ArrayList<SingleRowToDoList> a;
    ArrayList<SingleRowToDoList> a1;
    ValueFilter valueFilter;
    final String[] spinnerOptions = {
            "",
            "Edit",
            "Delete",
            "Send"
    };

    SettingsData settings;


    public  static String queryType;

    PreviousListCustomAdapter(Context c, String typeOfSearch) {
        context = c;
        a = new ArrayList<SingleRowToDoList>();
        a1 = new ArrayList<SingleRowToDoList>();
        settings = new SettingsData();
        settings.getSetting(context);
        final SingleRowToDoList temp;

        if(typeOfSearch.equals("all"))
        {
            readAllLists();
            queryType = "all";
        }
        else if(typeOfSearch.equals("favorites"))
        {
            readOnlyFavorites();
            queryType = "favorite";
        }
        else if(typeOfSearch.equals("notes"))
        {
            readOnlyNotes();
            queryType = "notes";
        }
        else if(typeOfSearch.equals("toDo"))
        {
            readOnlyToDo();
            queryType = "toDo";
        }


    }

    public void readAllLists() {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS", new String[]{"_id", "NAME", "DATE", "FAVORITE","IS_TO_DO_LIST","IS_ALARMED"}, "ARCHIVED = ?", new String[]{"0"}, null, null, null);

        while (cursor.moveToNext()) {

            int favorite = cursor.getInt(3);
            int isToDo = cursor.getInt(4);
            int isAlarmed = cursor.getInt(5);

            if (favorite == 1 && isToDo == 1 && isAlarmed == 1)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }
            else if (favorite == 0  && isToDo ==1 && isAlarmed == 0 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_grey,cursor.getInt(4),R.color.white,R.color.white));
            }
            else if (favorite == 1  && isToDo ==1 && isAlarmed == 0 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }
            else if (favorite == 0  && isToDo ==1 && isAlarmed == 1 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_grey,cursor.getInt(4),R.color.white,R.color.white));
            }
            else if (isToDo ==1)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_grey,cursor.getInt(4),R.color.white,R.color.white));
            }

            if (favorite == 1 && isToDo == 0 && isAlarmed == 1)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_note_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.drawable.option_menu_reminder_blue));
            }
            else if (favorite == 0  && isToDo == 0 && isAlarmed == 0 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_grey,cursor.getInt(4),R.color.white,R.color.white));
            }
            else if (favorite == 1  && isToDo == 0 && isAlarmed == 0 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }
            else if (favorite == 0  && isToDo == 0 && isAlarmed == 1 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_grey,cursor.getInt(4),R.color.white,R.drawable.option_menu_reminder_blue));
            }
            else if (isToDo == 0)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_grey,cursor.getInt(4),R.color.white,R.color.white));
            }



        }

    }

    public void readOnlyFavorites() {

        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();

        Cursor cursor = db.query("LISTS", new String[]{"_id", "NAME", "DATE", "FAVORITE","IS_TO_DO_LIST","IS_ALARMED"}, "ARCHIVED = ? AND FAVORITE = ?", new String[]{"0","1"}, null, null, null);

        while (cursor.moveToNext()) {

            int favorite = cursor.getInt(3);
            int isToDo = cursor.getInt(4);
            int isAlarmed = cursor.getInt(5);

            if (isToDo == 1 && isAlarmed == 1)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }
            else if (isToDo ==1 && isAlarmed == 0 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }
            else if (isToDo ==1 && isAlarmed == 0 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }
            else if ( isToDo ==1 && isAlarmed == 1 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }
            else if (isToDo ==1)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }

            if (isToDo == 0 && isAlarmed == 1)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_note_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.drawable.option_menu_reminder_blue));
            }
            else if (isToDo == 0 && isAlarmed == 0 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }
            else if (isToDo == 0 && isAlarmed == 0 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }
            else if (isToDo == 0 && isAlarmed == 1 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.drawable.option_menu_reminder_blue));
            }
            else if (isToDo == 0)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }



        }

    }

   public void readOnlyNotes() {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS", new String[]{"_id", "NAME", "DATE", "FAVORITE","IS_TO_DO_LIST","IS_ALARMED"}, "ARCHIVED = ? AND IS_TO_DO_LIST = ?", new String[]{"0", "0"}, null, null, null);

        while (cursor.moveToNext()) {

            int favorite = cursor.getInt(3);
            int isToDo = cursor.getInt(4);
            int isAlarmed = cursor.getInt(5);

            if (favorite == 1 && isAlarmed == 1)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_note_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.drawable.option_menu_reminder_blue));
            }
            else if (favorite == 0  && isAlarmed == 0 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_grey,cursor.getInt(4),R.color.white,R.color.white));
            }
            else if (favorite == 1  && isAlarmed == 0 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }
            else if (favorite == 0  && isToDo == 0 && isAlarmed == 1 )
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_grey,cursor.getInt(4),R.color.white,R.drawable.option_menu_reminder_blue));
            }


        }


    }

    public void readOnlyToDo() {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS", new String[]{"_id", "NAME", "DATE", "FAVORITE","IS_TO_DO_LIST","IS_ALARMED"}, "ARCHIVED = ? AND IS_TO_DO_LIST = ?", new String[]{"0", "1"}, null, null, null);

        while (cursor.moveToNext()) {

            int favorite = cursor.getInt(3);
            int isToDo = cursor.getInt(4);
            int isAlarmed = cursor.getInt(5);

            if (favorite == 1)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue,R.color.white));
            }
            else if (favorite == 0)
            {
                a.add(new SingleRowToDoList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_grey,cursor.getInt(4),R.color.white,R.color.white));
            }

        }


    }

    public void sendToTrash(SingleRowToDoList s)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ARCHIVED", 1);
        db.update("LISTS", cv, "_id=?", new String[]{String.valueOf(s.getId())});
    }


    public void deleteList(SingleRowToDoList s)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        db.delete("LISTS",
                "NAME=?",
                new String[]{s.title});
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.single_row, parent, false);

        TextView tvTitle = (TextView) rowView.findViewById(R.id.textView_title_single_row);
        TextView tvDate = (TextView) rowView.findViewById(R.id.textView_date);
        ImageView icon = (ImageView) rowView.findViewById(R.id.imageView1);
        final SingleRowToDoList temp = a.get(position);

        ImageView isFav = (ImageView) rowView.findViewById(R.id.is_fav);

        ImageView isAlarmed = (ImageView) rowView.findViewById(R.id.is_alarmed);

        tvTitle.setText(temp.title);
        tvTitle.setTypeface(Typeface.createFromAsset(context.getAssets(), settings.getFont(settings.getFontNumber())));
        tvDate.setText(temp.date);
        tvDate.setTypeface(Typeface.createFromAsset(context.getAssets(), settings.getFont(settings.getFontNumber())));
        icon.setImageResource(temp.imageResource);
        isFav.setImageResource(temp.isFavImage);
        isAlarmed.setImageResource(temp.isAlarmed);

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            showMenu(v,temp);
            return true;
            }
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (temp.getIsToDoList() == 1) {
                    Intent intent = new Intent(context, ToDoListActivity.class);
                    intent.putExtra(ToDoListActivity.LIST_ID, temp.id);
                    context.startActivity(intent);
                } else if (temp.getIsToDoList() == 0) {
                    Intent intent = new Intent(context, NotesViewerActivity.class);
                    intent.putExtra(NotesViewerActivity.LIST_ID, temp.getId());
                    context.startActivity(intent);
                }
            }
        });

        return rowView;
    }

    public void showMenu(View v, final SingleRowToDoList t) {
        PopupMenu popup = new PopupMenu(context, v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        if(t.getIsToDoList() == 1) {
                            Intent intent = new Intent(context, EditToDoActivity.class);
                            intent.putExtra(EditToDoActivity.LIST_ID, t.getId());
                            context.startActivity(intent);
                        }

                        else if (t.getIsToDoList() == 0)
                        {
                            Intent intent = new Intent(context, EditNotesActivity.class);
                            intent.putExtra(EditNotesActivity.LIST_ID, t.getId());
                            context.startActivity(intent);
                        }
                        return true;

                    case R.id.delete:
                        a.remove(t);
                        PreviousListCustomAdapter.this.notifyDataSetChanged();
                        sendToTrash(t);
                        Toast.makeText(context, "List Deleted", Toast.LENGTH_LONG).show();
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.previous_lists_option_menu);
        popup.show();
    }

    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<SingleRowToDoList> filterList = new ArrayList<SingleRowToDoList>();
                for (int i = 0; i < a.size(); i++) {
                    if ((a.get(i).getTitle().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        SingleRowToDoList sr = new SingleRowToDoList(a.get(i).getId(), a.get(i).getTitle(), a.get(i).getDate(), a.get(i).getImageResource(),a.get(i).getId(),a.get(i).getIsFavImage(),a.get(i).getIsAlarmed());

                        filterList.add(sr);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else {

                if(PreviousListCustomAdapter.queryType.equals("all"))
                {
                    a.clear();
                    readAllLists();
                    results.count = a.size();
                    results.values = a;
                }
                else if(PreviousListCustomAdapter.queryType.equals("favorite"))
                {
                    a.clear();
                    readOnlyFavorites();
                    results.count = a.size();
                    results.values = a;
                }
                else if(PreviousListCustomAdapter.queryType.equals("notes"))
                {
                    a.clear();
                    readOnlyNotes();
                    results.count = a.size();
                    results.values = a;
                }
                else if(PreviousListCustomAdapter.queryType.equals("toDo"))
                {
                    a.clear();
                    readOnlyToDo();
                    results.count = a.size();
                    results.values = a;
                }
            }
            return results;

        }


        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results)
        {
            a = (ArrayList<SingleRowToDoList>) results.values;
            notifyDataSetChanged();
        }


    }
}
